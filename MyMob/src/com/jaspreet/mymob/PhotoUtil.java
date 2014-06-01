package com.jaspreet.mymob;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;


import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.view.Display;
import android.view.WindowManager;

public class PhotoUtil {
	private static String currentFileName = null;

	private static final String TAG = PhotoUtil.class.getName();

	public static final String LANDSCAPE = "LANDSCAPE";

	public static final String PORTRAIT = "PORTRAIT";

	public static final String SQUARE = "SQUARE";

	private static String imageName = null;
	static int digree;

	public static int getImageOrientation(String target) throws Exception {

		int minDim = 10, scale;
		InputStream stream = null;
		BitmapFactory.Options op = new BitmapFactory.Options();
		stream = new FileInputStream(target);
		op.inJustDecodeBounds = true;
		BitmapFactory.decodeStream(stream, null, op);
		stream = new FileInputStream(target);
		Matrix matrix = new Matrix();
		ExifInterface exif = new ExifInterface(target);
		String orientation = exif.getAttribute(ExifInterface.TAG_ORIENTATION);
		if (orientation.equals(ExifInterface.ORIENTATION_NORMAL)) {
			digree = 0;
		} else if (orientation.equals(ExifInterface.ORIENTATION_ROTATE_90 + "")) {
			matrix.postRotate(90);
			digree = 90;
		} else if (orientation
				.equals(ExifInterface.ORIENTATION_ROTATE_180 + "")) {
			matrix.postRotate(180);
			digree = 180;
		} else if (orientation
				.equals(ExifInterface.ORIENTATION_ROTATE_270 + "")) {
			matrix.postRotate(270);
			digree = 270;
		}
		scale = Math.max(op.outWidth, op.outHeight) / minDim;
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inDither = false;
		options.inPurgeable = true;
		options.inTempStorage = new byte[16 * 1024];
		options.inScaled = false;
		options.inPreferredConfig = Bitmap.Config.RGB_565;
		options.inSampleSize = Math.round(scale);
		/*
		 * if (Common.DEBUG) { Log.e(TAG,
		 * "******************** getImageOrientation ********************");
		 * Log.i(TAG, "orientation : " + orientation); Log.i(TAG, "");
		 * Log.i(TAG, "op.outHeight : " + op.outHeight); Log.i(TAG,
		 * "op.outWidth : " + op.outWidth); Log.i(TAG, "scale : " + scale); }
		 */
		Bitmap bitmap = BitmapFactory.decodeStream(stream, null, options);
		Bitmap sourceBitmapSmall = Bitmap.createBitmap(bitmap, 0, 0,
				bitmap.getWidth(), bitmap.getHeight(), matrix, true);
		/*
		 * if (Common.DEBUG) { Log.i(TAG, "bitmap.getHeight() : " +
		 * bitmap.getHeight()); Log.i(TAG, "bitmap.getWidth() : " +
		 * bitmap.getWidth()); Log.i(TAG, "sourceBitmapSmall.getHeight() : " +
		 * sourceBitmapSmall.getHeight()); Log.i(TAG,
		 * "sourceBitmapSmall.getWidth() : " + sourceBitmapSmall.getWidth()); }
		 */
		bitmap.recycle();
		stream.close();
		/*
		 * if (sourceBitmapSmall.getWidth() > sourceBitmapSmall.getHeight()) {
		 * sourceBitmapSmall.recycle(); return LANDSCAPE; } else if
		 * (sourceBitmapSmall.getWidth() < sourceBitmapSmall.getHeight()) {
		 * sourceBitmapSmall.recycle(); return PORTRAIT; } else {
		 * sourceBitmapSmall.recycle(); return SQUARE; }
		 */
		return digree;
	}

	public static void rotate(String path, Uri uri, int degrees, Context context) {
		Bitmap b = null;
		InputStream inputStream;

		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inTempStorage = new byte[16 * 1024];
		options.inJustDecodeBounds = true;
		try {
			BitmapFactory
					.decodeStream(new FileInputStream(path), null, options);
		} catch (FileNotFoundException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		int IMAGE_MAX_SIZE = 700;
		int scale = 1;
		if (options.outHeight > IMAGE_MAX_SIZE
				|| options.outWidth > IMAGE_MAX_SIZE) {
			scale = (int) Math.pow(
					2,
					(int) Math.round(Math.log(IMAGE_MAX_SIZE
							/ (double) Math.max(options.outHeight,
									options.outWidth))
							/ Math.log(0.5)));
		}
		// Decode with inSampleSize
		BitmapFactory.Options o2 = new BitmapFactory.Options();
		o2.inSampleSize = scale;
		o2.inPurgeable = true;
		WindowManager manager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		Display display = manager.getDefaultDisplay();
		o2.outHeight = display.getHeight();
		o2.outWidth = display.getWidth();
		try {
			inputStream = context.getContentResolver().openInputStream(uri);
			b = BitmapFactory.decodeStream(inputStream, null, o2);
			inputStream.close();
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (degrees != 0 && b != null) {
			Matrix m = new Matrix();

			m.setRotate(degrees, (float) b.getWidth() / 2,
					(float) b.getHeight() / 2);
			try {
				Bitmap b2 = Bitmap.createBitmap(b, 0, 0, b.getWidth(),
						b.getHeight(), m, true);
				if (b != b2) {
					b.recycle();
					b = b2;
				}
			} catch (OutOfMemoryError ex) {
				throw ex;
			}
		}
		File file = new File(path);
		if (file.exists()) {
			file.delete();
		}
		if (file.exists()) {

		} else {
			try {
				file.createNewFile();
				FileOutputStream stream = new FileOutputStream(file.getPath());
				b.compress(CompressFormat.JPEG, 100, stream);
				stream.flush();
				stream.close();
				b.recycle();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

	public static boolean checkOrrintation(String orrintation, Bitmap bitmap) {
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		if (width > height) {
			if (orrintation.equals(LANDSCAPE)) {
				return false;
			} else {
				return true;
			}
		} else if (width < height) {
			if (orrintation.equals(PORTRAIT)) {
				return false;
			} else {
				return true;
			}
		}
		return false;
	}
	
	public static Uri getHidenImageUri(Context context) {
		ContextWrapper cw = new ContextWrapper(context);
        // path to /data/data/yourapp/app_data/imageDir
       File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
       // Create imageDir
       currentFileName = "" + System.currentTimeMillis() + ".jpeg";
       File mypath=new File(directory,currentFileName);
       Uri imgUri = Uri.fromFile(mypath);
		System.out.println("Image uri" + imgUri);
		return imgUri;
		
		
	}

	
	public static Uri getHidenVideoUri(Context context) {
		ContextWrapper cw = new ContextWrapper(context);
        // path to /data/data/yourapp/app_data/imageDir
       File directory = cw.getDir("videoDir", Context.MODE_PRIVATE);
       // Create imageDir
       currentFileName = "" + System.currentTimeMillis() + ".mp4";
       File mypath=new File(directory,currentFileName);
       Uri imgUri = Uri.fromFile(mypath);
		System.out.println("Image uri" + imgUri);
		return imgUri;
		
		
	}
	public static Uri getImageUri(Context context) {
		//Marapreferences marapreferences=Marapreferences.getInstance(context);
		boolean ismedia=true;
		File file = null;

		File file2 = new File(Environment.getExternalStorageDirectory()
				+ "/mara_messenger/images");
		File file3 = new File(Environment.getExternalStorageDirectory()
				+ "/mara_messenger/.images");
		if (!file2.exists()) {
			file2.mkdirs();
		}
		if (!file3.exists()) {
			file3.mkdirs();
		}
		currentFileName = "" + System.currentTimeMillis() + ".jpeg";
		if(ismedia){
		imageName = Environment.getExternalStorageDirectory()
				+ "/mara_messenger/images/" + currentFileName;
		}else{
			imageName =file3.getAbsolutePath()+"/" + currentFileName;	
		}
		file = new File(imageName);
		Uri imgUri = Uri.fromFile(file);
		System.out.println("Image uri" + imgUri);
		return imgUri;
	}
	
	public static Uri getWallpaperUri(Context context) {
	//	Marapreferences marapreferences=Marapreferences.getInstance(context);
		boolean ismedia=true;
		File file = null;

		File file2 = new File(Environment.getExternalStorageDirectory()
				+ "/mara_messenger/wallpaper");
		if (!file2.exists()) {
			file2.mkdirs();
		}
		currentFileName = "" + System.currentTimeMillis() + ".jpeg";
		
		imageName = Environment.getExternalStorageDirectory()
				+ "/mara_messenger/wallpaper/" + currentFileName;
	
		file = new File(imageName);
		Uri imgUri = Uri.fromFile(file);
		System.out.println("Image uri" + imgUri);
		return imgUri;
	}
	
	public static Uri getVideoUri(Context context) {
		//Marapreferences marapreferences=Marapreferences.getInstance(context);
		boolean ismedia=true;
		File file = null;

		File file2 = new File(Environment.getExternalStorageDirectory()
				+ "/mara_messenger/videos");
		if (!file2.exists()) {
			file2.mkdirs();
		}
		File file3 = new File(Environment.getExternalStorageDirectory()
				+ "/mara_messenger/.videos");
		
		if (!file3.exists()) {
			file3.mkdirs();
		}
		
		currentFileName = "" + System.currentTimeMillis() + ".mp4";
		if(ismedia){
		imageName = Environment.getExternalStorageDirectory()
				+ "/mara_messenger/videos/" + currentFileName;
		file = new File(imageName);
		
		}else{
			imageName = file3.getAbsolutePath()+"/"  + currentFileName;	
			file = new File(imageName);
		}
		
		Uri imgUri = Uri.fromFile(file);
		System.out.println("Image uri" + imgUri);
		return imgUri;
	}
	public static Uri getThumbUri(Context context) {
	//	Marapreferences marapreferences=Marapreferences.getInstance(context);
		boolean ismedia=true;
		File file = null;

		File file2 = new File(Environment.getExternalStorageDirectory()
				+ "/mara_messenger/thumbnails");
		if (!file2.exists()) {
			file2.mkdirs();
		}
		File file3 = new File(Environment.getExternalStorageDirectory()
				+ "/mara_messenger/.thumbnails");
		
		if (!file3.exists()) {
			file3.mkdirs();
		}
		currentFileName = "" + System.currentTimeMillis() + ".jpeg";
		if(ismedia){
		imageName = Environment.getExternalStorageDirectory()
				+ "/mara_messenger/thumbnails/" + currentFileName;
		}else{
			imageName = file3.getAbsolutePath()+"/"  + currentFileName;	
		}
		file = new File(imageName);
		Uri imgUri = Uri.fromFile(file);
		System.out.println("Image uri" + imgUri);
		return imgUri;
	}
	
}
