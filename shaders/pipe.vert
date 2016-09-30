#version 330 core

layout (location = 0) in vec4 position;
layout (location = 1) in vec2 textureCoordinates;

uniform mat4 pr_matrix;
uniform mat4 vw_matrix = mat4(1.0f);
uniform mat4 ml_matrix = mat4(1.0f);
uniform int pipePos;

out DATA
{
	vec2 tc;
} vs_out;

void main()
{
	gl_Position = pr_matrix * vw_matrix * ml_matrix * position;
	vs_out.tc = textureCoordinates;
	
	if( pipePos == 1 )
		vs_out.tc.y = 1-vs_out.tc.y;
		
}