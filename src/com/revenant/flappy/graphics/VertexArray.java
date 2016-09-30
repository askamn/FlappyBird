package com.revenant.flappy.graphics;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import com.revenant.flappy.utils.BufferUtils;

public class VertexArray {
	
	// Vertex Array Object
	private int vao;
	
	// Vertex Buffer Object
	private int vbo;
	
	// Index Buffer Object
	private int ibo;
	
	// Texture Coordinates Object
	private int tco;
	
	// Count of indices
	private int count;
	
	public VertexArray(float[] vertices, byte[] indices, float[] textureCoordinates)
	{
		count = indices.length;
		
		vao = glGenVertexArrays();
		glBindVertexArray(vao);
		
		vbo = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vbo);
		glBufferData(GL_ARRAY_BUFFER, BufferUtils.createFloatBuffer(vertices), GL_STATIC_DRAW);
		glVertexAttribPointer(Shader.VERTEX_ATTR, 3, GL_FLOAT, false, 0, 0);
		glEnableVertexAttribArray(Shader.VERTEX_ATTR);
		
		tco = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, tco);
		glBufferData(GL_ARRAY_BUFFER, BufferUtils.createFloatBuffer(textureCoordinates), GL_STATIC_DRAW);
		glVertexAttribPointer(Shader.TEXTURE_COORDINATES_ATTR, 2, GL_FLOAT, false, 0, 0);
		glEnableVertexAttribArray(Shader.TEXTURE_COORDINATES_ATTR);
		
		ibo = glGenBuffers();
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, BufferUtils.createByteBuffer(indices), GL_STATIC_DRAW);
		
		// Unbind the buffers
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		glBindVertexArray(0);
	}
	
	public void Bind()
	{
		glBindVertexArray(vao);
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
	}
	
	public void UnBind()
	{
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
		glBindVertexArray(0);
	}
	
	public void Draw()
	{
		glDrawElements(GL_TRIANGLES, count, GL_UNSIGNED_BYTE, 0);	
	}
	
	public void Render()
	{
		this.Bind();
		this.Draw();
	}
}
  