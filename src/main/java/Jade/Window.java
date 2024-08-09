package Jade;

import org.lwjgl.*;
import org.lwjgl.glfw.GLFWAllocateCallback;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryUtil;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {
    private int width, height;
    private String title;
    private long glfwWindow;
    private float r,g,b,a;
    private boolean fadeToBlack = false;

    private static Window window = null;

    private Window() {
        this.width = 1920;
        this.height = 1080;
        this.title = "Mario";
        this.r = 1;
        this.g = 1;
        this.b = 1;
        this.a = 1;
    }

    public static Window get() {
        if (Window.window == null) {
            Window.window = new Window();
        }
        return Window.window;
    }

    public void run() {
        System.out.println("Hello LWJGL " + Version.getVersion() + "!");
        init();
        loop();

        //Free the Memory
        glfwFreeCallbacks(glfwWindow);
        glfwDestroyWindow(glfwWindow);

        //terminate glfw and free the error callback
        glfwTerminate();
        glfwSetErrorCallback(null).free();

    }

    public void init() {
        //Setup Error Callback
        GLFWErrorCallback.createPrint(System.err).set();

        //Initialize GLFW
        if(!glfwInit()){
            throw new IllegalStateException("Unable to Initialize GLFW");
        }

        //Configure GLFW
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE,GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE,GLFW_TRUE);
        glfwWindowHint(GLFW_MAXIMIZED,GLFW_TRUE);

        //Create Window
        glfwWindow = glfwCreateWindow(this.width,this.height,this.title, NULL,NULL);

        if ( glfwWindow == NULL )
            throw new RuntimeException("Failed to create the GLFW window");

        glfwSetCursorPosCallback(glfwWindow,MouseListener::mousePosCallback);
        glfwSetMouseButtonCallback(glfwWindow,MouseListener::mouseButtonCallback);
        glfwSetScrollCallback(glfwWindow,MouseListener::mouseScrollCallback);
        glfwSetKeyCallback(glfwWindow,KeyListener::keyCallback);
        // Make the OpenGL Context current
        glfwMakeContextCurrent(glfwWindow);

        //Enable V-sync
        glfwSwapInterval(1);

        //Make the window Visible
        glfwShowWindow(glfwWindow);

        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the GLCapabilities instance and makes the OpenGL
        // bindings available for use.
        GL.createCapabilities();

    }

    public void loop() {
        int a1 = 1;
        while ( !glfwWindowShouldClose(glfwWindow) ) {
            // Poll for window events. The key callback above will only be
            // invoked during this call.
            glfwPollEvents();

            glClearColor(r,g,b,a);
            glClear(GL_COLOR_BUFFER_BIT); // clear the framebuffer
            if (fadeToBlack){
                r = Math.max(r - (a1 * 0.01f),0);
                g = Math.max(g - (a1 * 0.01f),0);
                b = Math.max(b - (a1 * 0.01f),0);
            }
            if(KeyListener.isKeyPressed(GLFW_KEY_SPACE)){
                fadeToBlack = true;
            }
            glfwSwapBuffers(glfwWindow);
        }
    }

}
