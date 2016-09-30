package com.revenant.flappy.math;

import java.nio.FloatBuffer;

import com.revenant.flappy.utils.BufferUtils;

public class Matrix4f 
{
	public static final int SIZE = 4 * 4;
	public float elements[] = new float[SIZE];
	
	public Matrix4f()
	{
		
	}
	
	public Matrix4f(float diagonal)
	{
		for( int i = 0; i < SIZE; i++ )
		{
			this.elements[i] = ( ( i % 4 - i / 4 ) == 0.0f ) ? diagonal : 0.0f;
		}		
	}
	
	public static Matrix4f Identity()
	{		
		return new Matrix4f(1.0f);
	}
	
	public static Matrix4f Orthographic(float left, float right, float bottom, float top, float near, float far)
	{
		Matrix4f result = Matrix4f.Identity();//new Matrix4f(1.0f);
		
		// Diagonal elements
		result.elements[0 + 0 * 4] = 2.0f / (right - left);
		result.elements[1 + 1 * 4] = 2.0f / (top - bottom);
		result.elements[2 + 2 * 4] = 2.0f / (near - far);
		
		// Elements of the last column
		result.elements[0 + 3 * 4] = (left + right) / (left - right);
		result.elements[1 + 3 * 4] = (bottom + top) / (bottom - top);
		result.elements[2 + 3 * 4] = (far + near) / (far - near);
		
		return result;
	}

	public static Matrix4f Perspective(float fov, float aspectRatio, float near, float far)
	{
		Matrix4f result = Matrix4f.Identity();
		
		float q = (float) (1.0f / Math.tan(Math.toRadians(0.5f * fov)));
		float a = 1 / aspectRatio;
		float b = (near + far) / (near - far);
		float c = (2.0f * near * far) / (near - far);

		result.elements[0 + 0 * 4] = a;
		result.elements[1 + 1 * 4] = q;
		result.elements[2 + 2 * 4] = b;
		result.elements[3 + 2 * 4] = -1.0f;
		result.elements[2 + 3 * 4] = c;
	
		return result;
	}
	
	public static Matrix4f Translation(Vector3f translation)
	{
		Matrix4f result = Matrix4f.Identity();
		
		result.elements[0 + 3 * 4] = translation.x;
		result.elements[1 + 3 * 4] = translation.y;
		result.elements[2 + 3 * 4] = translation.z;
		
		return result;
	}
	
	public static Matrix4f Rotation(float angle)
	{
		Matrix4f result = Matrix4f.Identity();
		
		float r   = (float) Math.toRadians(angle);
		float cos = (float) Math.cos(r);
		float sin = (float) Math.sin(r);
		
		result.elements[ 0 + 0 * 4 ] = cos;
		result.elements[ 1 + 0 * 4 ] = sin;
		
		result.elements[ 0 + 1 * 4 ] = -sin;
		result.elements[ 1 + 1 * 4 ] = cos;
		
		return result;
	}
	
	public Matrix4f Multiply(Matrix4f matrix)
	{
		Matrix4f result = new Matrix4f();
		
		for (int j = 0; j < 4; ++j)
		{
			for (int i = 0; i < 4; ++i)
			{
				float sum = 0.0f;
				for (int e = 0; e < 4; ++e)
				{
					sum += elements[i + e * 4] * matrix.elements[e + j * 4];
				}

				result.elements[i + j * 4] = sum;
			}
		}

		return result;
	}
	
	public FloatBuffer toFloatBuffer()
	{
		return BufferUtils.createFloatBuffer(elements);
	}
}
