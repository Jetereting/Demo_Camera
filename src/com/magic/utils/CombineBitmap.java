package com.magic.utils;

import java.io.File;
import java.io.FileOutputStream;

import com.magic.demo_camera.R;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;

public class CombineBitmap {
	public static Bitmap combine(Bitmap bitmap1,Bitmap bitmap2,int x,int y){
		Bitmap bitmap3 = Bitmap.createBitmap(170*7,77*4, bitmap1.getConfig());
		Canvas canvas = new Canvas(bitmap3);
		canvas.drawBitmap(bitmap1, new Matrix(), null);
		canvas.drawBitmap(bitmap2, x, y, null);  //40、20为bitmap2写入点的x、y坐标
		return bitmap3;
	}
}
