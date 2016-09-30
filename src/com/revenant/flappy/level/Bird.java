package com.revenant.flappy.level;

import org.lwjgl.glfw.GLFW;

import com.revenant.flappy.graphics.Shader;
import com.revenant.flappy.graphics.Texture;
import com.revenant.flappy.graphics.VertexArray;
import com.revenant.flappy.input.Input;
import com.revenant.flappy.math.Matrix4f;
import com.revenant.flappy.math.Vector3f;

public class Bird {
	private float SIZE = 1.0f;
	private float rotation;
	private float delta = 0.0f;
	
	private VertexArray mesh;
	private Vector3f position = new Vector3f();
	private Texture texture;
	
	public Bird()
	{
		float[] vertices = new float[] {
			-SIZE / 2.0f, -SIZE / 2.0f, 0.2f,
			-SIZE / 2.0f,  SIZE / 2.0f, 0.2f,
			 SIZE / 2.0f,  SIZE / 2.0f, 0.2f,
			 SIZE / 2.0f, -SIZE / 2.0f, 0.2f,
		};
		
		byte[] indices = new byte[] {
			0, 1, 2,
			2, 3, 0
		};
		
		float[] textureCoordinates = new float[] {
			0, 1,
			0, 0,
			1, 0,
			1, 1,
		};
		
		mesh = new VertexArray(vertices, indices, textureCoordinates);
		texture = new Texture("res/bird.png");
	}
	
	public void Update()
	{
		position.y -= delta;
		
		if(Input.isKeyDown( GLFW.GLFW_KEY_SPACE ))
			delta = -0.15f;
		else
			delta += 0.01f;
		
		this.rotation = -delta * 90.0f;
	}
	
	public void Render()
	{
		Shader.Bird.enable();
		Shader.Bird.setUniformMat4( "ml_matrix", Matrix4f.Translation( position ).Multiply( Matrix4f.Rotation( this.rotation ) ) );
		texture.bind();
		mesh.Render();
		Shader.Bird.disable();
	}
	
	public void Fall()
	{
		delta = -0.15f;
	}
	
	public float getX()
	{
		return position.x;
	}
	
	public float getY()
	{
		return position.y;
	}
	
	public float getSize()
	{
		return SIZE;
	}
}
