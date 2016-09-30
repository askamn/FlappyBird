package com.revenant.flappy.utils;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.*;

public class ShaderUtils 
{
	private ShaderUtils()
	{
		
	}

	public static int load(String vertexShaderPath, String fragmentShaderPath)
	{
		String vertex   = FileUtils.loadAsString(vertexShaderPath);
		String fragment = FileUtils.loadAsString(fragmentShaderPath);
		
		return create(vertex, fragment);
	}
	
	public static int create(String vertex, String fragment)
	{
		int program = glCreateProgram();
		int vertID = glCreateShader(GL_VERTEX_SHADER);
		int fragID = glCreateShader(GL_FRAGMENT_SHADER);

		glShaderSource(vertID, vertex);
		glShaderSource(fragID, fragment);
		
		glCompileShader(vertID);
		if( glGetShaderi(vertID, GL_COMPILE_STATUS) == GL_FALSE )
		{
			System.err.println("Failed to compile vertex shader.");
			System.err.println(glGetShaderInfoLog(vertID, 2048));
			
			return -1;
		}
		
		glCompileShader(fragID);
		if( glGetShaderi(fragID, GL_COMPILE_STATUS) == GL_FALSE )
		{
			System.err.println("Failed to compile fragment shader.");
			System.err.println(glGetShaderInfoLog(fragID, 2048));
			
			return -1;
		}
		glAttachShader(program, vertID);
		glAttachShader(program, fragID);
		glLinkProgram(program);
		glValidateProgram(program);
		
		glDeleteShader(vertID);
		glDeleteShader(fragID);
		
		return program;	
	}
}
