package com.revenant.flappy.utils;

import org.lwjgl.glfw.GLFWWindowSizeCallback;
import static org.lwjgl.opengl.GL11.*;

public class WindowResize extends GLFWWindowSizeCallback {
	public void invoke(long window, int width, int height) {
		glViewport(0, 0, width, height);
	}	
}
