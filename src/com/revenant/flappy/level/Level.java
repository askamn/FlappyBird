package com.revenant.flappy.level;

import java.util.Random;

import com.revenant.flappy.graphics.Shader;
import com.revenant.flappy.graphics.Texture;
import com.revenant.flappy.graphics.VertexArray;
import com.revenant.flappy.math.Matrix4f;
import com.revenant.flappy.math.Vector3f;

public class Level {
	
	private VertexArray background;
	private Texture bgTexture;
	
	private int map = 0;
	private int xScroll = 0;
	
	private Bird bird;
	private Pipe[] pipes = new Pipe[5 * 2];
	
	private int index = 0;
	
	private Random random = new Random();
	
	private final int OFFSET = 5;
	private boolean control = true; // Player controls bird, control = true otherwise false
	public Level()
	{
		float[] vertices = new float[] {
			-10.0f, -10.0f * 9.0f / 16.0f, 0.0f,
			-10.0f,  10.0f * 9.0f / 16.0f, 0.0f,
			 0.0f,   10.0f * 9.0f / 16.0f, 0.0f,
			 0.0f,  -10.0f * 9.0f / 16.0f, 0.0f,
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
		
		background = new VertexArray(vertices, indices, textureCoordinates);
		bgTexture = new Texture("res/bg.jpeg");
		
		bird = new Bird();
		generatePipes();
	}
	
	public void update()
	{
		if( control )
		{
			xScroll--;
			
			if( -xScroll % 335 == 0 )
			{
				++map;
			}
			
			if( -xScroll > 250 && -xScroll % 120 == 0 )
			{
				updatePipes();
			}	
		}
		
		bird.Update();
		
		if( control && this.collision() )
		{
			bird.Fall();
			control = false;
		}
	}
	
	private void generatePipes()
	{
		Pipe.create();
		for( int i = 0; i < 5 * 2; i+= 2 )
		{
			pipes[i]   = new Pipe(OFFSET + index * 3.0f + random.nextFloat(), random.nextFloat() * 4.0f);
			pipes[i+1] = new Pipe(pipes[i].getX(), pipes[i].getY() - 11.5f);
			index += 2;
		}
	}
	
	private void updatePipes()
	{
		pipes[ index % 10 ] = new Pipe(OFFSET + index * 3.0f + random.nextFloat(), random.nextFloat() * 4.0f);
		pipes[ (index + 1) % 10 ] = new Pipe(pipes[index % 10].getX(), pipes[index % 10].getY() - 11.5f);
		index += 2;
	}
	
	private void renderPipes()
	{
		Shader.Pipe.enable();
		Shader.Pipe.setUniformMat4("vw_matrix", Matrix4f.Translation(new Vector3f(xScroll * 0.05f, 0.0f, 0.0f)));
		Pipe.getTexture().bind();
		Pipe.getMesh().Bind();

		for(int i = 0; i < 5 * 2; i++)
		{
			Shader.Pipe.setUniformMat4("ml_matrix", pipes[i].getMlMatrix());
			Shader.Pipe.setUniform1i( "pipePos", i%2 == 0 ? 1 : 0 );
			
			Pipe.getMesh().Draw();
		}
		
		Pipe.getTexture().unbind();
		Pipe.getMesh().UnBind();
		
		Shader.Pipe.disable();
	}
	
	private boolean collision()
	{
		for( int i = 0; i < 5 * 2; ++i )
		{
			float bx = -xScroll * 0.05f;
			float by = bird.getY();
			
			float px = pipes[i].getX();
			float py = pipes[i].getY();
			
			float bx0 = bx - bird.getSize() / 2.0f;
			float bx1 = bx + bird.getSize() / 2.0f;
			float by0 = by - bird.getSize() / 2.0f;
			float by1 = by + bird.getSize() / 2.0f;
			
			float px0 = px;
			float px1 = px + Pipe.getWidth();
			float py0 = py;
			float py1 = py + Pipe.getHeight();
			
			if( bx1 > px0 && bx0 < px1 )
			{
				if( by1 > py0 && by0 < py1 )
				{
					return true;
				}
			}	
		}
		
		return false;
	}
	
	public void Render()
	{
		bgTexture.bind();
		Shader.BG.enable();
		
		background.Bind();
		for( int i = map; i < map+3; i++ )
		{	
			
			Shader.BG.setUniformMat4( "vw_matrix", Matrix4f.Translation( new Vector3f( i * 10.0f + xScroll * 0.03f, 0.0f, 0.0f ) ) );
			background.Draw();
		}
		
		Shader.BG.disable();
		bgTexture.unbind();
		
		renderPipes();
		bird.Render();
	}
	
}
