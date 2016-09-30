package com.revenant.flappy;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.system.MemoryUtil.*;

import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

import com.revenant.flappy.graphics.Shader;
import com.revenant.flappy.input.Input;
import com.revenant.flappy.level.Level;
import com.revenant.flappy.math.Matrix4f;
import com.revenant.flappy.utils.WindowResize;

public class Main implements Runnable
{
	/* Window Parameters */
	private int width = 1366;//1280;
	private int height = 768;//720;
	private String windowTitle = "Flappy Bird";
	
	private long window;
	
	private boolean running = false;
	private Thread thread;
	
	private Level level;
	
	public void start()
	{
		running = true;
		
		thread = new Thread(this, "Game");
		thread.start();
		
	}
	
	private void init()
	{
		if(!glfwInit())
			throw new RuntimeException("Failed to initialize GLFW!");
		
		glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
		window = glfwCreateWindow( width, height, windowTitle, NULL, NULL );
		
		if( window == NULL )
			throw new RuntimeException("Failed to create the GLFW window.");
		
		GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
		glfwSetWindowPos(window, ( vidmode.width() - this.width ) / 2, ( vidmode.height() - this.height ) / 2);

		glfwSetKeyCallback(window, new Input());
		glfwSetWindowSizeCallback(window, new WindowResize());
		
		glfwMakeContextCurrent(window);
		glfwShowWindow(window);

		// Create the GL Capabilities for the current context
		GL.createCapabilities();
		
		// Set the window color
		glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
		glEnable(GL_DEPTH_TEST);
		glActiveTexture(GL_TEXTURE1);
		// Print the current OpenGL Version being used
		System.out.println("OpenGL Version: " + glGetString(GL_VERSION));		
		
		// Initialize the shaders
		Shader.loadAll();
	
		// Projection matrix variable
		Matrix4f pr_matrix = Matrix4f.Orthographic(-10.0f, 10.0f, -10.0f * 9.0f / 16.0f, 10.0f * 9.0f / 16.0f, -1.0f, 1.0f);
		Shader.BG.setUniformMat4("pr_matrix", pr_matrix);
		// Which texture to use?
		Shader.BG.setUniform1i("tex", 1);
		
		Shader.Bird.setUniformMat4("pr_matrix", pr_matrix);
		// Which texture to use?
		Shader.Bird.setUniform1i("tex", 1);
	
		Shader.Pipe.setUniformMat4("pr_matrix", pr_matrix);
		// Which texture to use?
		Shader.Pipe.setUniform1i("tex", 1);
		
		level = new Level();
	}
	
	public void run()
	{
		init();
		
		long lastTime = System.nanoTime();
		double ns = 1000000000 / 60.0;
		double delta = 0.0;
		int updates = 0;
		int frames = 0;
		
		long timer = System.currentTimeMillis();
		
		while(running)
		{
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			
			lastTime = now;
			
			// Since we need to update the game only 60 times per second (60 updates is not the same as 60 FPS...)
			if( delta >= 1.0 )
			{
				update();
				updates++;
				delta--;
			}
			
			render();
			frames++;
			
			if( System.currentTimeMillis() - timer > 1000 )
			{
				System.out.println( frames + "fps, " + updates + "ups" );
				glfwSetWindowTitle(this.window, this.windowTitle + " (" + frames + " fps)");
				timer += 1000;
				updates = 0;
				frames = 0; 
			}
			
			if(glfwWindowShouldClose(window))
			{
				running = false;
			}
		}
	}
	
	private void update()
	{
		glfwPollEvents();
		level.update();
	}
	
	private void render()
	{
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		level.Render();
		
		int error;
		if((error = glGetError()) != GL_NO_ERROR)
		{
			System.out.println(error);
		}
		
		glfwSwapBuffers(window);
	}
	
	// The beginning...
	public static void main(String[] args)
	{
		new Main().start();
	}
}
