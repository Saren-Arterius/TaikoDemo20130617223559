package com.owataiko.taikodemo;

import java.io.ByteArrayOutputStream;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.Typeface;

import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.luzi82.elly.render.EbTextRender;

public class TextRenderFactory implements EbTextRender.Factory {
	
	public AssetManager mAssertManager;
	
	public TextRenderFactory(AssetManager aAssertManager){
		mAssertManager=aAssertManager;
	}

	public EbTextRender create(String aText, float aSize) {
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setTextSize(aSize);
		paint.setTypeface(Typeface.DEFAULT);
//		paint.setTypeface(Typeface.createFromAsset(mAssertManager, "fonts/Roboto-Regular.ttf"));
		paint.setColor(Color.WHITE);
		paint.setStyle(Paint.Style.FILL);

		Rect result = new Rect();
		paint.getTextBounds(aText, 0, aText.length(), result);

		int bmpWidth = 1;
		while (bmpWidth < result.width()) {
			bmpWidth <<= 1;
		}
		int bmpHeight = 1;
		while (bmpHeight < result.height()) {
			bmpHeight <<= 1;
		}
		System.err.println("EbTextRender:" + aText + " bmpWidth=" + bmpWidth
				+ " bmpHeight=" + bmpHeight);

//		float textureRatio = ((float) bmpWidth) / bmpHeight;
		float widthRatio = ((float) result.right) / bmpWidth;
		float heightRatio = ((float) result.height()) / bmpHeight;
		float meshRatio = (float) result.right / (float) result.height();

		Bitmap bmp = Bitmap.createBitmap(bmpWidth, bmpHeight, Config.ARGB_8888);
		Canvas canvas = new Canvas(bmp);
//		canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.SRC);
		canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
		canvas.drawText(aText, 0, -result.top, paint);

		System.err.println("EbTextRender:" + aText + " bmp.getPixel(0, 0):" + Integer.toHexString(bmp.getPixel(0, 0)));

		ByteArrayOutputStream bufOut = new ByteArrayOutputStream(1 << 16);
		bmp.compress(CompressFormat.PNG, 0, bufOut);
		byte[] buf = bufOut.toByteArray();

		Pixmap pixmap = new Pixmap(buf, 0, buf.length);
		Texture texture = new Texture(pixmap);
		System.err.println("EbTextRender:" + aText + " pixmap.getWidth()=" + pixmap.getWidth()
				+ " pixmap.getHeight()=" + pixmap.getHeight()+" pixmap.getPixel(0, 0)="+Integer.toHexString(pixmap.getPixel(0, 0)));

		VertexAttributes va;
		va = new VertexAttributes( //
				new VertexAttribute(VertexAttributes.Usage.Position, 3,
						"position"),//
				new VertexAttribute(VertexAttributes.Usage.TextureCoordinates,
						2, "texturecoordinates")//
		);

		Mesh mesh = new Mesh(true, 4, 4, va);
//		mesh.setVertices(new float[] { //
//		0f, 1f, 0f, 0f, 0f,//
//				textureRatio, 1f, 0f, widthRatio, 0f,//
//				0f, 0f, 0f, 0f, heightRatio,//
//				textureRatio, 0f, 0f, widthRatio, heightRatio,//
//		});
		mesh.setVertices(new float[] { //
		0f, 1f, 0f, 0f, 0f,//
		meshRatio, 1f, 0f, widthRatio, 0f,//
				0f, 0f, 0f, 0f, heightRatio,//
				meshRatio, 0f, 0f, widthRatio, heightRatio,//
		});
		mesh.setIndices(new short[] { 0, 1, 2, 3 });

		EbTextRender ret = new EbTextRender();
		ret.mTexture = texture;
		ret.mMesh = mesh;
		ret.mWidth = meshRatio;
		ret.mHeight = 1f;

		return ret;
	}

}
