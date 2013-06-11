package com.luzi82.elly.render;

import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;

public class MeshUtils {

	public static Mesh newBoxMesh(final float aX0, final float aY0,
			final float aX1, final float aY1) {
		Mesh mesh = new Mesh(false, 4, 4, new VertexAttributes(
				new VertexAttribute(VertexAttributes.Usage.Position, 3,
						"position")));
		mesh.setVertices(new float[] { //
		aX0, aY0, 0f,//
				aX1, aY0, 0f,//
				aX0, aY1, 0f,//
				aX1, aY1, 0f,//
		});
		mesh.setIndices(new short[] { 0, 1, 2, 3 });
		return mesh;
	}

	public static Mesh newTriangleMesh(final float aX0, final float aY0,
			final float aX1, final float aY1, final float aX2, final float aY2) {
		Mesh mesh = new Mesh(false, 3, 3, new VertexAttributes(
				new VertexAttribute(VertexAttributes.Usage.Position, 3,
						"position")));
		mesh.setVertices(new float[] { //
		aX0, aY0, 0f,//
				aX1, aY1, 0f,//
				aX2, aY2, 0f,//
		});
		mesh.setIndices(new short[] { 0, 1, 2 });
		return mesh;
	}

}
