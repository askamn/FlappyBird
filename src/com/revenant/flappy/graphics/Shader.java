package com.revenant.flappy.graphics;

import com.revenant.flappy.math.*;
import static org.lwjgl.opengl.GL20.*;

import java.util.HashMap;
import java.util.Map;

import com.revenant.flappy.utils.ShaderUtils;

public class Shader 
{
	public static final int VERTEX_ATTR = 0;
	public static final int TEXTURE_COORDINATES_ATTR = 1;
	
	public static Shader BG;
	public static Shader Bird;
	public static Shader Pipe;
	
	public final int id;
	public Map<String, Integer> locationCache = new HashMap<String, Integer>();
	
	private boolean shaderEnabled = false;
	
	public Shader(String vertex, String fragment)
	{
		id = ShaderUtils.load(vertex, fragment);
	}
	
	public static void loadAll()
	{
		BG = new Shader("shaders/bg.vert", "shaders/bg.frag");
		Bird = new Shader("shaders/bird.vert", "shaders/bird.frag");
		Pipe = new Shader("shaders/pipe.vert", "shaders/pipe.frag");
	}
	
	public int getUniformLocation(String name)
	{
		if(locationCache.containsKey(name))
			return locationCache.get(name);
		
		int result = glGetUniformLocation(id, name);
		
		if( result == -1 )
			System.err.println("Could not find uniform variable '" + name + "'");
		else
			locationCache.put(name, result);

		return result;
	}
	
	public void setUniform1i(String name, int value)
	{
		if( this.shaderEnabled == false )
			this.enable();
		
		glUniform1i(getUniformLocation(name), value);
	}
	
	public void setUniform1f(String name, float value)
	{
		if( this.shaderEnabled == false )
			this.enable();
		
		glUniform1f(getUniformLocation(name), value);
	}
	
	public void setUniform2f(String name, float x, float y)
	{
		if( this.shaderEnabled == false )
			this.enable();
		
		glUniform2f(getUniformLocation(name), x, y);
	}
	
	public void setUniform3f(String name, Vector3f value)
	{
		if( this.shaderEnabled == false )
			this.enable();
		
		glUniform3f(getUniformLocation(name), value.x, value.y, value.z);
	}
	
	public void setUniformMat4(String name, Matrix4f mat4)
	{
		if( this.shaderEnabled == false )
			this.enable();
		
		glUniformMatrix4fv(getUniformLocation(name), false, mat4.toFloatBuffer());
	}
	
	public void enable()
	{
		this.shaderEnabled = true;
		glUseProgram(id);
	}
	
	public void disable()
	{
		this.shaderEnabled = false;
		glUseProgram(0);
	}
}
