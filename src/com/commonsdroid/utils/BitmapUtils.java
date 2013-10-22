package com.commonsdroid.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Environment;

/**
 * The Class <code>BitmapUtils.</code>.<br/>
 * provides utility methods to perform some common bitmap operations
 * @author siddhesh
 * @version 12.10.2013
 */
public class BitmapUtils {
	
	/** The Constant IO_BUFFER_SIZE. */
	private static final int IO_BUFFER_SIZE = 1024;
	
	/** The Constant BOOST_BLUE. */
	public static final int BOOST_RED = 1, BOOST_GREEN = 2, BOOST_BLUE = 3;


	/**
	 * Gets the from url.
	 * 
	 * @param url
	 *            the url
	 * @return the from url
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static Bitmap getFromUrl(String url) throws IOException {
		URL myFileUrl = null;
		try {
			myFileUrl = new URL(url);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();
		conn.setDoInput(true);
		conn.connect();
		InputStream is = conn.getInputStream();

		Bitmap bmImg = BitmapFactory.decodeStream(is);

		return bmImg;
	}

	/**
	 * Save to local.
	 *
	 * @param bmp the bmp
	 * @param fileName the file name
	 * @return the absolute path of saved file
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static String saveToLocal(Bitmap bmp, String fileName)
			throws IOException {
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		bmp.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

		File f = new File(Environment.getExternalStorageDirectory()
				+ File.separator + fileName);
		f.createNewFile();
		FileOutputStream fo = new FileOutputStream(f);
		fo.write(bytes.toByteArray());

		fo.close();
		return ""+f.getAbsolutePath();
	}

	/**
	 * Downloads file from specified URL and stores as filename mentioned as
	 * parameter, in default external storage directory.
	 *
	 * @param url the url
	 * @param fileName the file name
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void download(String url, String fileName) throws IOException {
		Bitmap bmp = getFromUrl(url);

		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		bmp.compress(Bitmap.CompressFormat.PNG, 0, bytes);

		// you can create a new file name "test.jpg" in sdcard folder.
		File f = new File(Environment.getExternalStorageDirectory()
				+ File.separator + fileName);
		f.createNewFile();
		// write the bytes in file
		FileOutputStream fo = new FileOutputStream(f);
		fo.write(bytes.toByteArray());

		// remember close de FileOutput
		fo.close();
	}

	/**
	 * Downloads file from specified URL and stores as filename mentioned as
	 * parameter in directory name specified use this method when you want to
	 * download your imahe into a specific directory and not in default external
	 * storage.
	 *
	 * @param url the url
	 * @param directoryName the directory name
	 * @param fileName the file name
	 * @return absolute path of saved file
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static String download(String url, String directoryName,
			String fileName) throws IOException {
		Bitmap bmp = getFromUrl(url);

		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		bmp.compress(Bitmap.CompressFormat.PNG, 0, bytes);

		File dir = new File(Environment.getExternalStorageDirectory()
				+ File.separator + directoryName);

		if (!dir.exists()) {
			dir.mkdirs();
		}

		File f = new File(Environment.getExternalStorageDirectory()
				+ File.separator + directoryName + File.separator + fileName);
		f.createNewFile();
		// write the bytes in file
		FileOutputStream fo = new FileOutputStream(f);
		fo.write(bytes.toByteArray());

		// remember close de FileOutput
		fo.close();
		
		return f.getAbsolutePath();
	}
	
	/**
	 * Gets the bitmap.
	 *
	 * @param context the context
	 * @param inputStream the input stream
	 * @return the bitmap
	 */
	public static Bitmap getBitmap(Context context, InputStream inputStream) {

		File cacheDir;

		// Find the dir to save cached images
		if (android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED))
			cacheDir = new File(
					android.os.Environment.getExternalStorageDirectory(),
					"Zoolook");
		else
			cacheDir = context.getCacheDir();
		if (!cacheDir.exists())
			cacheDir.mkdirs();

		File f = new File(cacheDir, "banner");
		try {
			Bitmap bitmap = null;
			OutputStream os = new FileOutputStream(f);
			CopyStream(inputStream, os);
			os.close();
			bitmap = decodeF(f);
			return bitmap;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Copy stream.
	 *
	 * @param is the is
	 * @param os the os
	 */
	public static void CopyStream(InputStream is, OutputStream os) {
		final int buffer_size = 1024;
		try {
			byte[] bytes = new byte[buffer_size];
			for (;;) {
				int count = is.read(bytes, 0, buffer_size);
				if (count == -1)
					break;
				os.write(bytes, 0, count);
			}
		} catch (Exception ex) {
		}
	}
	
	/**
	 * Decode f.
	 *
	 * @param f the f
	 * @return the bitmap
	 */
	private static Bitmap decodeF(File f) {
		Bitmap b = null;
		try {

			BitmapFactory.Options bfo = new BitmapFactory.Options();
			FileInputStream fis = new FileInputStream(f);
			b = BitmapFactory.decodeStream(fis, null, bfo);
			fis.close();

		} catch (Exception e) {

			e.printStackTrace();

		}
		return b;

	}

	/**
	 * Crop.
	 * 
	 * @param bitmap
	 *            the bitmap
	 * @param newWidth
	 *            the new width
	 * @param newHeight
	 *            the new height
	 * @return the bitmap
	 */
	public static Bitmap crop(Bitmap bitmap, float newWidth, float newHeight) {
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;

		// createa matrix for the manipulation
		Matrix matrix = new Matrix();
		// resize the bit map
		matrix.postScale(scaleWidth, scaleHeight);

		// recreate the new Bitmap
		// The following line is the place where you can crop the image.
		// If you do not want to resize image then in above line keep
		// scaledWidth and
		// scaledHeight to 1.
		// Then in te following line, you can change following four parameters
		// 0,0,width,height
		// Thease four parameters mark the four axis readings of bounding box.
		// Change it and
		// see how it works

		Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height,
				matrix, true);

		// make a Drawable from Bitmap to allow to set the BitMap
		// to the ImageView, ImageButton or what ever
		return resizedBitmap;
	}

	/**
	 * Crop from 4 sides.
	 *
	 * @param bitmap the bitmap
	 * @param x the x
	 * @param y the y
	 * @param newWidth the new width
	 * @param newHeight the new height
	 * @return the bitmap
	 */
	public static Bitmap crop(Bitmap bitmap, int x, int y, float newWidth,
			float newHeight) {
//		int width = bitmap.getWidth();
//		int height = bitmap.getHeight();

//		Logger.debug(BitmapUtils.class, "WIDTH : " + width + " HEIGHT : "
//				+ height);
		Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, x, y,
				(int) newWidth, (int) newHeight, null, true);
		return resizedBitmap;
	}

	/**
	 * Resize.
	 * 
	 * @param bitmap
	 *            the bitmap
	 * @param newWidth
	 *            the new width
	 * @param newHeight
	 *            the new height
	 * @return the bitmap
	 */
	public static Bitmap resize(Bitmap bitmap, int newWidth, int newHeight) {

		Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, newWidth,
				newHeight);

		// make a Drawable from Bitmap to allow to set the BitMap
		// to the ImageView, ImageButton or what ever
		return resizedBitmap;
	}

	/**
	 * Gets the blurred bitmap.
	 *
	 * @param original the original
	 * @param radius the radius
	 * @return the blurred bitmap
	 */
	public Bitmap getBlurredBitmap(Bitmap original, int radius) {
		if (radius < 1)
			return null;

		int width = original.getWidth();
		int height = original.getHeight();
		int wm = width - 1;
		int hm = height - 1;
		int wh = width * height;
		int div = radius + radius + 1;
		int r[] = new int[wh];
		int g[] = new int[wh];
		int b[] = new int[wh];
		int rsum, gsum, bsum, x, y, i, p, p1, p2, yp, yi, yw;
		int vmin[] = new int[Math.max(width, height)];
		int vmax[] = new int[Math.max(width, height)];
		int dv[] = new int[256 * div];
		for (i = 0; i < 256 * div; i++)
			dv[i] = i / div;

		int[] blurredBitmap = new int[wh];
		original.getPixels(blurredBitmap, 0, width, 0, 0, width, height);

		yw = 0;
		yi = 0;

		for (y = 0; y < height; y++) {
			rsum = 0;
			gsum = 0;
			bsum = 0;
			for (i = -radius; i <= radius; i++) {
				p = blurredBitmap[yi + Math.min(wm, Math.max(i, 0))];
				rsum += (p & 0xff0000) >> 16;
				gsum += (p & 0x00ff00) >> 8;
				bsum += p & 0x0000ff;
			}
			for (x = 0; x < width; x++) {
				r[yi] = dv[rsum];
				g[yi] = dv[gsum];
				b[yi] = dv[bsum];

				if (y == 0) {
					vmin[x] = Math.min(x + radius + 1, wm);
					vmax[x] = Math.max(x - radius, 0);
				}
				p1 = blurredBitmap[yw + vmin[x]];
				p2 = blurredBitmap[yw + vmax[x]];

				rsum += ((p1 & 0xff0000) - (p2 & 0xff0000)) >> 16;
				gsum += ((p1 & 0x00ff00) - (p2 & 0x00ff00)) >> 8;
				bsum += (p1 & 0x0000ff) - (p2 & 0x0000ff);
				yi++;
			}
			yw += width;
		}

		for (x = 0; x < width; x++) {
			rsum = gsum = bsum = 0;
			yp = -radius * width;
			for (i = -radius; i <= radius; i++) {
				yi = Math.max(0, yp) + x;
				rsum += r[yi];
				gsum += g[yi];
				bsum += b[yi];
				yp += width;
			}
			yi = x;
			for (y = 0; y < height; y++) {
				blurredBitmap[yi] = 0xff000000 | (dv[rsum] << 16)
						| (dv[gsum] << 8) | dv[bsum];
				if (x == 0) {
					vmin[y] = Math.min(y + radius + 1, hm) * width;
					vmax[y] = Math.max(y - radius, 0) * width;
				}
				p1 = x + vmin[y];
				p2 = x + vmax[y];

				rsum += r[p1] - r[p2];
				gsum += g[p1] - g[p2];
				bsum += b[p1] - b[p2];

				yi += width;
			}
		}

		return Bitmap.createBitmap(blurredBitmap, width, height,
				Bitmap.Config.RGB_565);
	}
	
	
	/**
	 * Load bitmap.
	 *
	 * @param url the url
	 * @return the bitmap
	 */
	public static Bitmap loadBitmap(String url) {
		Bitmap bitmap = null;
		InputStream in = null;
		BufferedOutputStream out = null;

		try {
			in = new BufferedInputStream(new URL(url).openStream(),
					IO_BUFFER_SIZE);

			final ByteArrayOutputStream dataStream = new ByteArrayOutputStream();
			out = new BufferedOutputStream(dataStream, IO_BUFFER_SIZE);
			copy(in, out);
			out.flush();

			final byte[] data = dataStream.toByteArray();
			BitmapFactory.Options options = new BitmapFactory.Options();

			bitmap = BitmapFactory.decodeByteArray(data, 0, data.length,
					options);
		} catch (IOException e) {
//			Log.e(TAG, "Could not load Bitmap from: " + url);
		} finally {
			try {
				in.close();
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

		return bitmap;
	}
	
	
	/**
	 * Copy.
	 *
	 * @param in the in
	 * @param out the out
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private static void copy(InputStream in, BufferedOutputStream out)
			throws IOException {
		int byte_;
		while ((byte_ = in.read()) != -1)
			out.write(byte_);
	}
	
	/**
	 * Make colors in bitmap vivid.
	 * 
	 * @param src
	 *            the src
	 * @param type
	 *            the type
	 * @param percent
	 *            the percent
	 * @return the bitmap
	 * @author siddhesh
	 */
	public static Bitmap makeVivid(Bitmap src, int type, float percent) {
		int width = src.getWidth();
		int height = src.getHeight();
		Bitmap bmOut = Bitmap.createBitmap(width, height, src.getConfig());

		int A, R, G, B;
		int pixel;

		for (int x = 0; x < width; ++x) {
			for (int y = 0; y < height; ++y) {
				pixel = src.getPixel(x, y);
				A = Color.alpha(pixel);
				R = Color.red(pixel);
				G = Color.green(pixel);
				B = Color.blue(pixel);
//				Log.e("CHECK", "RGB : R = " + R + " G = " + G + " B = " + B);
				R = (int) (R * (1 + percent));
				if (R > 255)
					R = 255;
				G = (int) (G * (1 + percent));
				if (G > 255)
					G = 255;
				B = (int) (B * (1 + percent));
				if (B > 255)
					B = 255;
				bmOut.setPixel(x, y, Color.argb(A, R, G, B));
			}
		}
		return bmOut;
	}
	
	/**
	 * Make colors of bitmap saturated.
	 * 
	 * @param source
	 *            the source
	 * @param level
	 *            the level
	 * @return the bitmap
	 * @author siddhesh
	 */
	public static Bitmap makeSaturated(Bitmap source, int level) {
		// get image size
		int width = source.getWidth();
		int height = source.getHeight();
		int[] pixels = new int[width * height];
		float[] HSV = new float[3];
		// get pixel array from source
		source.getPixels(pixels, 0, width, 0, 0, width, height);

		int index = 0;
		// iteration through pixels
		for (int y = 0; y < height; ++y) {
			for (int x = 0; x < width; ++x) {
				// get current index in 2D-matrix
				index = y * width + x;
				// convert to HSV
				Color.colorToHSV(pixels[index], HSV);
				// increase Saturation level
				HSV[1] *= level;
				HSV[1] = (float) Math.max(0.0, Math.min(HSV[1], 1.0));
				// take color back
				pixels[index] |= Color.HSVToColor(HSV);
			}
		}
		// output bitmap
		Bitmap bmOut = Bitmap.createBitmap(width, height,
				Bitmap.Config.ARGB_8888);
		bmOut.setPixels(pixels, 0, width, 0, 0, width, height);
		return bmOut;
	}
	
	/**
	 * Adjust contrast of bitmap.
	 * 
	 * @param src
	 *            the src
	 * @param value
	 *            the value
	 * @return the bitmap
	 * @author siddhesh
	 */
	public static Bitmap adjustedContrast(Bitmap src, double value) {
		// image size
		int width = src.getWidth();
		int height = src.getHeight();
		// create output bitmap

		// create a mutable empty bitmap
		Bitmap bmOut = Bitmap.createBitmap(width, height, src.getConfig());

		// create a canvas so that we can draw the bmOut Bitmap from source
		// bitmap
		Canvas c = new Canvas();
		c.setBitmap(bmOut);

		// draw bitmap to bmOut from src bitmap so we can modify it
		c.drawBitmap(src, 0, 0, new Paint(Color.BLACK));

		// color information
		int A, R, G, B;
		int pixel;
		// get contrast value
		double contrast = Math.pow((100 + value) / 100, 2);

		// scan through all pixels
		for (int x = 0; x < width; ++x) {
			for (int y = 0; y < height; ++y) {
				// get pixel color
				pixel = src.getPixel(x, y);
				A = Color.alpha(pixel);
				// apply filter contrast for every channel R, G, B
				R = Color.red(pixel);
				R = (int) (((((R / 255.0) - 0.5) * contrast) + 0.5) * 255.0);
				if (R < 0) {
					R = 0;
				} else if (R > 255) {
					R = 255;
				}

				G = Color.green(pixel);
				G = (int) (((((G / 255.0) - 0.5) * contrast) + 0.5) * 255.0);
				if (G < 0) {
					G = 0;
				} else if (G > 255) {
					G = 255;
				}

				B = Color.blue(pixel);
				B = (int) (((((B / 255.0) - 0.5) * contrast) + 0.5) * 255.0);
				if (B < 0) {
					B = 0;
				} else if (B > 255) {
					B = 255;
				}

				// set new pixel color to output bitmap
				bmOut.setPixel(x, y, Color.argb(A, R, G, B));
			}
		}
		return bmOut;
	}
	
	/**
	 * Adjust brightness of bitmap.
	 * 
	 * @param src
	 *            the src
	 * @param value
	 *            the value
	 * @return the bitmap
	 * @author siddhesh
	 */
	public static Bitmap doBrightness(Bitmap src, int value) {
		// image size
		int width = src.getWidth();
		int height = src.getHeight();
		// create output bitmap
		Bitmap bmOut = Bitmap.createBitmap(width, height, src.getConfig());
		// color information
		int A, R, G, B;
		int pixel;

		// scan through all pixels
		for (int x = 0; x < width; ++x) {
			for (int y = 0; y < height; ++y) {
				// get pixel color
				pixel = src.getPixel(x, y);
				A = Color.alpha(pixel);
				R = Color.red(pixel);
				G = Color.green(pixel);
				B = Color.blue(pixel);

				// increase/decrease each channel
				R += value;
				if (R > 255) {
					R = 255;
				} else if (R < 0) {
					R = 0;
				}

				G += value;
				if (G > 255) {
					G = 255;
				} else if (G < 0) {
					G = 0;
				}

				B += value;
				if (B > 255) {
					B = 255;
				} else if (B < 0) {
					B = 0;
				}

				// apply new pixel color to output bitmap
				bmOut.setPixel(x, y, Color.argb(A, R, G, B));
			}
		}

		// return final image
		return bmOut;
	}
	
	/**
	 * Increase green.
	 *
	 * @param src the src
	 * @param value the value
	 * @return the bitmap
	 */
	public static Bitmap increaseGreen(Bitmap src, int value) {
		int width = src.getWidth();
		int height = src.getHeight();
		final float factor = 1.1f;

		Bitmap bmOut = Bitmap.createBitmap(width, height, src.getConfig());

		int[] pix = new int[width * height];
		src.getPixels(pix, 0, width, 0, 0, width, height);

		int A, R, G, B;

		int i = 0;
		for (int pixel : pix) {
			A = Color.alpha(pixel);
			R = Color.red(pixel);
			G = Color.green(pixel);
			B = Color.blue(pixel);

			// R = (int)(R * factor);
			// if(R > 255) R = 255;

			G = (int) (G * factor);
			if (G > 255)
				G = 255;

			// B = (int)(B * factor);
			// if(B > 255) B = 255;

			pix[i] = Color.argb(A, R, G, B);
			i++;
		}

		bmOut.setPixels(pix, 0, width, 0, 0, width, height);
		// src.recycle();
		return bmOut;
	}
	
	/**
	 * Change hue of bitmap.
	 * 
	 * @param bmp
	 *            the bmp
	 * @param hue
	 *            the hue
	 * @param width
	 *            the width
	 * @param height
	 *            the height
	 * @return the bitmap
	 * @author siddhesh
	 */
	public static Bitmap changeHue(Bitmap bmp, int hue, int width, int height) {
		if (bmp == null) {
			return null;
		}
		// int srcwidth = bmp.getWidth();
		// int srcheight = bmp.getHeight();
		// create output bitmap

		// create a mutable empty bitmap
		Bitmap bitmap = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(),
				bmp.getHeight());
		if ((hue < 0) || (hue > 360)) {
			return null;
		}

		int size = width * height;
		int[] all_pixels = new int[size];
		int top = 0;
		int left = 0;
		int offset = 0;
		int stride = width;

		bitmap.getPixels(all_pixels, offset, stride, top, left, width, height);

		int pixel = 0;
		int alpha = 0;
		float[] hsv = new float[3];

		for (int i = 0; i < size; i++) {
			pixel = all_pixels[i];
			alpha = Color.alpha(pixel);
			Color.colorToHSV(pixel, hsv);

			// You could specify target color including Saturation for
			// more precise results
			hsv[0] = hue;
			hsv[1] = 0.2f;

			all_pixels[i] = Color.HSVToColor(alpha, hsv);
		}

		bitmap.setPixels(all_pixels, offset, stride, top, left, width, height);
		return bitmap;
	}
	
	/**
	 * Combine bitmap images.
	 * 
	 * @param c
	 *            the c
	 * @param s
	 *            the s
	 * @return the bitmap
	 * @author siddhesh
	 */
	public static Bitmap combineImages(Bitmap c, Bitmap s) { // can add a 3rd
																// parameter
																// 'String loc'
																// if you want
																// to save the
																// new image -
																// left some
																// code to do
																// that at the
																// bottom
		Bitmap cs = null;

		/*
		 * int width, height = 0;
		 * 
		 * if(c.getWidth() > s.getWidth()) { width = c.getWidth() +
		 * s.getWidth(); height = c.getHeight(); } else { width = s.getWidth() +
		 * s.getWidth(); height = c.getHeight(); }
		 */

		cs = Bitmap.createBitmap(c, 0, 0, c.getWidth(), c.getHeight());

		Canvas comboImage = new Canvas(cs);

		// comboImage.drawBitmap(c, 0f, 0f, null);
		comboImage.drawBitmap(s, 0f, 0f, null);

		// this is an extra bit I added, just incase you want to save the new
		// image somewhere and then return the location
		/*
		 * String tmpImg = String.valueOf(System.currentTimeMillis()) + ".png";
		 * 
		 * OutputStream os = null; try { os = new FileOutputStream(loc +
		 * tmpImg); cs.compress(CompressFormat.PNG, 100, os); }
		 * catch(IOException e) { Log.e("combineImages",
		 * "problem combining images", e); }
		 */

		return cs;
	}
	
	/**
	 * Tilt shift effect on bitmap.
	 * 
	 * @param sentBitmap
	 *            the sent bitmap
	 * @param radius
	 *            the radius
	 * @param x
	 *            the x
	 * @param y
	 *            the y
	 * @return the bitmap
	 * @author siddhesh
	 */
	public static Bitmap tiltShift(Bitmap sentBitmap, int radius, int x, int y) {

		Bitmap bmp = sentBitmap.copy(sentBitmap.getConfig(), true);

		int w = sentBitmap.getWidth();
		int h = sentBitmap.getHeight();
		int[] pix = new int[w * h];

		bmp.getPixels(pix, 0, w, 0, 0, w, h);
		int radiusSquared = 110 * 110;

		for (int r = radius; r > 1; r /= 2) {
			for (int i = r; i < h - r; i++) {
				for (int j = r; j < w - r; j++) {
					double distanceSquared = Math.pow(i / 2 - x, 2)
							+ Math.pow(j - y, 2);

					if (distanceSquared >= radiusSquared) {
						int tl = pix[(i - r) * w + j - r];
						int tr = pix[(i - r) * w + j + r];
						int tc = pix[(i - r) * w + j];
						int bl = pix[(i + r) * w + j - r];
						int br = pix[(i + r) * w + j + r];
						int bc = pix[(i + r) * w + j];
						int cl = pix[i * w + j - r];
						int cr = pix[i * w + j + r];

						pix[(i * w) + j] = 0xFF000000
								| (((tl & 0xFF) + (tr & 0xFF) + (tc & 0xFF)
										+ (bl & 0xFF) + (br & 0xFF)
										+ (bc & 0xFF) + (cl & 0xFF) + (cr & 0xFF)) >> 3)
								& 0xFF
								| (((tl & 0xFF00) + (tr & 0xFF00)
										+ (tc & 0xFF00) + (bl & 0xFF00)
										+ (br & 0xFF00) + (bc & 0xFF00)
										+ (cl & 0xFF00) + (cr & 0xFF00)) >> 3)
								& 0xFF00
								| (((tl & 0xFF0000) + (tr & 0xFF0000)
										+ (tc & 0xFF0000) + (bl & 0xFF0000)
										+ (br & 0xFF0000) + (bc & 0xFF0000)
										+ (cl & 0xFF0000) + (cr & 0xFF0000)) >> 3)
								& 0xFF0000;
					}

				}

			}
		}
		bmp.setPixels(pix, 0, w, 0, 0, w, h);
		return bmp;
	}
	
	/**
	 * Fastblur in bitmap.
	 * 
	 * @param sentBitmap
	 *            the sent bitmap
	 * @param radius
	 *            the radius
	 * @return the bitmap
	 * @author siddhesh
	 */
	public static Bitmap fastblur(Bitmap sentBitmap, int radius) {
		Bitmap bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);
		Bitmap origBitmap = sentBitmap.copy(sentBitmap.getConfig(), true);

		if (radius < 1) {
			return (null);
		}

		int w = bitmap.getWidth();
		int h = bitmap.getHeight();

		int[] pix = new int[w * h];
		int[] opix = new int[w * h];
//		Log.e("pix", w + " " + h + " " + pix.length);
		bitmap.getPixels(pix, 0, w, 0, 0, w, h);
		origBitmap.getPixels(opix, 0, w, 0, 0, w, h);

		final int width = sentBitmap.getWidth();
		final int height = sentBitmap.getHeight();

		final float xc = width / 2;
		final float yc = height / 2;
		final float a1 = width / 6;
		final float b1 = height / 2;

		final double a1Square = Math.pow(a1, 2);
		final double b1Square = Math.pow(b1, 2);

		final double area = Math.PI * a1 * b1; // / area of Ellipse to calculate
												// no of pixels this ellipse can
												// hold //

		int[] intArrEllipsePosition = new int[(int) area];
		int[] intArrEllipseData = new int[(int) area];
		int[] overlayBmp = new int[width * height];

		for (int i = 0; i < overlayBmp.length; i++) {
			overlayBmp[i] = -1;
		}

		// Log.d(TAG, overlayBmp+"");

		sentBitmap.getPixels(overlayBmp, 0, width, 0, 0, width, height);

		int noOfPixelsInEllipse = 0;
		int currPosition = 0;
		for (int currY = 0; currY < height; currY++) {
			for (int currX = 0; currX < width; currX++) {

				double computeEllipseEqn = (Math.pow(currX - xc, 2) / a1Square)
						+ (Math.pow(currY - yc, 2) / b1Square); // /////
																// computation
																// to check if
																// current (x,y)
																// pixel lies in
																// or on ellipse
																// region///

				if (computeEllipseEqn <= 1) {

					currPosition = (currY * width) + currX + 1; // // mapping
																// between
																// current x,y
																// pixel wrt
																// pixel array
																// ///
					intArrEllipsePosition[noOfPixelsInEllipse] = currPosition;

					if (computeEllipseEqn > 0.90) {
						// Log.d("PIX ", "" + computeEllipseEqn);
						intArrEllipseData[noOfPixelsInEllipse] = Color.argb(
								0x26, Color.red(pix[currPosition]),
								Color.green(pix[currPosition]),
								Color.blue(pix[currPosition]));// pix[currPosition];
					} else if (computeEllipseEqn > 0.70
							&& computeEllipseEqn < 0.90) {
						// Log.d("PIX 0.70", "" + computeEllipseEqn);
						intArrEllipseData[noOfPixelsInEllipse] = Color.argb(
								0x10, Color.red(pix[currPosition]),
								Color.green(pix[currPosition]),
								Color.blue(pix[currPosition]));// pix[currPosition];
					} else if (computeEllipseEqn > 0.65
							&& computeEllipseEqn < 0.70) {
						// Log.d("PIX 0.70", "" + computeEllipseEqn);
						intArrEllipseData[noOfPixelsInEllipse] = Color.argb(
								0x10, Color.red(pix[currPosition]),
								Color.green(pix[currPosition]),
								Color.blue(pix[currPosition]));// pix[currPosition];
					} else if (computeEllipseEqn > 0.63
							&& computeEllipseEqn < 0.65) {
						// Log.d("PIX 0.70", "" + computeEllipseEqn);
						intArrEllipseData[noOfPixelsInEllipse] = Color.argb(
								0x01, Color.red(pix[currPosition]),
								Color.green(pix[currPosition]),
								Color.blue(pix[currPosition]));// pix[currPosition];
					} else {
						intArrEllipseData[noOfPixelsInEllipse] = pix[currPosition];
					}

					// intArrEllipseData[noOfPixelsInEllipse] =
					// intArrEllipseData[noOfPixelsInEllipse] = Color.argb(0x00,
					// Color.red(pix[currPosition]),
					// Color.green(pix[currPosition]),
					// Color.blue(pix[currPosition]));
					noOfPixelsInEllipse++;
				}

			}
		}

		// Log.d("Check: ",
		// "chk: intArrElipsePosition"+intArrElipsePosition.length +
		// " noOfPixelsInEllipse: "+noOfPixelsInEllipse);

		int wm = w - 1;
		int hm = h - 1;
		int wh = w * h;
		int div = radius + radius + 1;

		int r[] = new int[wh];
		int g[] = new int[wh];
		int b[] = new int[wh];
		int rsum, gsum, bsum, x, y, i, p, yp, yi, yw;
		int vmin[] = new int[Math.max(w, h)];

		int divsum = (div + 1) >> 1;
		divsum *= divsum;
		int dv[] = new int[256 * divsum];
		for (i = 0; i < 256 * divsum; i++) {
			dv[i] = (i / divsum);
		}

		yw = yi = 0;

		int[][] stack = new int[div][3];
		int stackpointer;
		int stackstart;
		int[] sir;
		int rbs;
		int r1 = radius + 1;
		int routsum, goutsum, boutsum;
		int rinsum, ginsum, binsum;

		for (y = 0; y < h; y++) {
			rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
			for (i = -radius; i <= radius; i++) {
				p = pix[yi + Math.min(wm, Math.max(i, 0))];
				sir = stack[i + radius];
				sir[0] = (p & 0xff0000) >> 16;
				sir[1] = (p & 0x00ff00) >> 8;
				sir[2] = (p & 0x0000ff);
				// sir[0] = p>>16;
				// sir[1] = p>>8;
				// sir[2] = p;
				rbs = r1 - Math.abs(i);
				rsum += sir[0] * rbs;
				gsum += sir[1] * rbs;
				bsum += sir[2] * rbs;
				if (i > 0) {
					rinsum += sir[0];
					ginsum += sir[1];
					binsum += sir[2];
				} else {
					routsum += sir[0];
					goutsum += sir[1];
					boutsum += sir[2];
				}
				// radius = radius + 1;
			}
			stackpointer = radius;

			for (x = 0; x < w; x++) {

				{
					r[yi] = dv[rsum];
					g[yi] = dv[gsum];
					b[yi] = dv[bsum];

					rsum -= routsum;
					gsum -= goutsum;
					bsum -= boutsum;

					stackstart = stackpointer - radius + div;
					sir = stack[stackstart % div];

					routsum -= sir[0];
					goutsum -= sir[1];
					boutsum -= sir[2];

					if (y == 0) {
						vmin[x] = Math.min(x + radius + 1, wm);
					}
					p = pix[yw + vmin[x]];

					sir[0] = (p & 0xff0000) >> 16;
					sir[1] = (p & 0x00ff00) >> 8;
					sir[2] = (p & 0x0000ff);
					//
					// sir[0] = p>>16;
					// sir[1] = p>>8;
					// sir[2] = p;

					rinsum += sir[0];
					ginsum += sir[1];
					binsum += sir[2];

					rsum += rinsum;
					gsum += ginsum;
					bsum += binsum;

					stackpointer = (stackpointer + 1) % div;
					sir = stack[(stackpointer) % div];

					routsum += sir[0];
					goutsum += sir[1];
					boutsum += sir[2];

					rinsum -= sir[0];
					ginsum -= sir[1];
					binsum -= sir[2];

					yi++;
				}
			}
			yw += w;
		}
		for (x = 0; x < w; x++) {
			rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
			yp = -radius * w;
			for (i = -radius; i <= radius; i++) {
				yi = Math.max(0, yp) + x;

				sir = stack[i + radius];

				sir[0] = r[yi];
				sir[1] = g[yi];
				sir[2] = b[yi];

				rbs = r1 - Math.abs(i);

				rsum += r[yi] * rbs;
				gsum += g[yi] * rbs;
				bsum += b[yi] * rbs;

				if (i > 0) {
					rinsum += sir[0];
					ginsum += sir[1];
					binsum += sir[2];
				} else {
					routsum += sir[0];
					goutsum += sir[1];
					boutsum += sir[2];
				}

				if (i < hm) {
					yp += w;
				}
			}
			yi = x;
			stackpointer = radius;
			for (y = 0; y < h; y++) {
				{
					// Preserve alpha channel: ( 0xff000000 & pix[yi] )
					pix[yi] = (0xff000000 & pix[yi]) | (dv[rsum] << 16)
							| (dv[gsum] << 8) | dv[bsum];

					rsum -= routsum;
					gsum -= goutsum;
					bsum -= boutsum;

					stackstart = stackpointer - radius + div;
					sir = stack[stackstart % div];

					routsum -= sir[0];
					goutsum -= sir[1];
					boutsum -= sir[2];

					if (x == 0) {
						vmin[y] = Math.min(y + r1, hm) * w;
					}
					p = x + vmin[y];

					sir[0] = r[p];
					sir[1] = g[p];
					sir[2] = b[p];

					rinsum += sir[0];
					ginsum += sir[1];
					binsum += sir[2];

					rsum += rinsum;
					gsum += ginsum;
					bsum += binsum;

					stackpointer = (stackpointer + 1) % div;
					sir = stack[stackpointer];

					routsum += sir[0];
					goutsum += sir[1];
					boutsum += sir[2];

					rinsum -= sir[0];
					ginsum -= sir[1];
					binsum -= sir[2];

					yi += w;

				}
			}
		}

		// Log.e("pix", w + " " + h + " " + pix.length);

		for (int k = 0; k < width * height; k++) {
			// Log.d(TAG, "PIXEL FOUND "+overlayBmp[k]);

			overlayBmp[k] = Color.argb(0x000000, Color.red(pix[currPosition]),
					Color.green(pix[currPosition]),
					Color.blue(pix[currPosition]));

		}

		// /// update pixels in blurred array with the pixels picked from
		// original pixels intersecting the ellipse region ///
		for (int tmpEllipsePixel = 0; tmpEllipsePixel < noOfPixelsInEllipse; tmpEllipsePixel++) {
			// pix[intArrEllipsePosition[tmpEllipsePixel]] =
			// intArrEllipseData[tmpEllipsePixel];
			overlayBmp[intArrEllipsePosition[tmpEllipsePixel]] = intArrEllipseData[tmpEllipsePixel];
		}

		// Log.d(TAG, overlayBmp+"");

		// pix[intArrEllipsePosition[tmpEllipsePixel]] =
		// intArrEllipseData[tmpEllipsePixel];
		//
		bitmap.setPixels(pix, 0, w, 0, 0, w, h);
		//
		Bitmap canvasOverlay = Bitmap.createBitmap(origBitmap.getWidth(),
				origBitmap.getHeight(), origBitmap.getConfig());
		canvasOverlay.setPixels(overlayBmp, 0, w, 0, 0, w, h);
		// canvasOverlay.setHasMipMap(true);

		//
		Canvas canvas = new Canvas(bitmap);
		canvas.drawBitmap(canvasOverlay, new Matrix(), null);

		return (bitmap);
	}
	
	/**
	 * Fastblur.
	 *
	 * @param sentBitmap the sent bitmap
	 * @return the bitmap
	 */
	public static Bitmap fastblur(Bitmap sentBitmap) {
		int[] radius = new int[]{1,2,3,6};
		int[] ellipseWidth = new int[]{6,4,3,2};
		Bitmap bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);
//		Bitmap origBitmap = sentBitmap.copy(sentBitmap.getConfig(), true);

		

		int w = bitmap.getWidth();
		int h = bitmap.getHeight();

		int[] pix = new int[w * h];
		final int width = sentBitmap.getWidth();
		final int height = sentBitmap.getHeight();
		
		final float xc = width / 2;
		final float yc = height / 2;
//		int[] opix = new int[w * h];
//		Log.e("pix", w + " " + h + " " + pix.length);
		for(int index=0;index<2;index++){
			if (radius[index] < 1) {
				return (null);
			}
		bitmap.getPixels(pix, 0, w, 0, 0, w, h);
//		origBitmap.getPixels(opix, 0, w, 0, 0, w, h);

		
		final float a1 = width / ellipseWidth[index];
		final float b1 = height / 2;

		final double a1Square = Math.pow(a1, 2);
		final double b1Square = Math.pow(b1, 2);

		final double area = Math.PI * a1 * b1; // / area of Ellipse to calculate
												// no of pixels this ellipse can
												// hold //

		int[] intArrEllipsePosition = new int[(int) area];
		int[] intArrEllipseData = new int[(int) area];

		int noOfPixelsInEllipse = 0;
		int currPosition = 0;
		for (int currY = 0; currY < height; currY++) {
			for (int currX = 0; currX < width; currX++) {

				double computeEllipseEqn = (Math.pow(currX - xc, 2) / a1Square)
						+ (Math.pow(currY - yc, 2) / b1Square); // /////
																// computation
																// to check if
																// current (x,y)
																// pixel lies in
																// or on ellipse
																// region///

				if (computeEllipseEqn <= 1) {

					currPosition = (currY * width) + currX + 1; // // mapping
																// between
																// current x,y
																// pixel wrt
																// pixel array
																// ///
					intArrEllipsePosition[noOfPixelsInEllipse] = currPosition;

					// if (computeEllipseEqn > 0.90) {
					// // Log.d("PIX ", "" + computeEllipseEqn);
					// intArrEllipseData[noOfPixelsInEllipse] = Color.argb(0x26,
					// Color.red(pix[currPosition]),
					// Color.green(pix[currPosition]),
					// Color.blue(pix[currPosition]));// pix[currPosition];
					// }
					// else if (computeEllipseEqn > 0.70 && computeEllipseEqn <
					// 0.90) {
					// // Log.d("PIX 0.70", "" + computeEllipseEqn);
					// intArrEllipseData[noOfPixelsInEllipse] = Color.argb(0x10,
					// Color.red(pix[currPosition]),
					// Color.green(pix[currPosition]),
					// Color.blue(pix[currPosition]));// pix[currPosition];
					// }
					// else if (computeEllipseEqn > 0.65 && computeEllipseEqn <
					// 0.70) {
					// // Log.d("PIX 0.70", "" + computeEllipseEqn);
					// intArrEllipseData[noOfPixelsInEllipse] = Color.argb(0x10,
					// Color.red(pix[currPosition]),
					// Color.green(pix[currPosition]),
					// Color.blue(pix[currPosition]));// pix[currPosition];
					// }
					// else if (computeEllipseEqn > 0.63 && computeEllipseEqn <
					// 0.65) {
					// // Log.d("PIX 0.70", "" + computeEllipseEqn);
					// intArrEllipseData[noOfPixelsInEllipse] = Color.argb(0x01,
					// Color.red(pix[currPosition]),
					// Color.green(pix[currPosition]),
					// Color.blue(pix[currPosition]));// pix[currPosition];
					// }
					// else
					// {
					intArrEllipseData[noOfPixelsInEllipse] = pix[currPosition];
					// }

					// intArrEllipseData[noOfPixelsInEllipse] =
					// intArrEllipseData[noOfPixelsInEllipse] = Color.argb(0x00,
					// Color.red(pix[currPosition]),
					// Color.green(pix[currPosition]),
					// Color.blue(pix[currPosition]));
					noOfPixelsInEllipse++;
				}

			}
		}


		int wm = w - 1;
		int hm = h - 1;
		int wh = w * h;
		int div = radius[index] + radius[index] + 1;

		int r[] = new int[wh];
		int g[] = new int[wh];
		int b[] = new int[wh];
		int rsum, gsum, bsum, x, y, i, p, yp, yi, yw;
		int vmin[] = new int[Math.max(w, h)];

		int divsum = (div + 1) >> 1;
		divsum *= divsum;
		int dv[] = new int[256 * divsum];
		for (i = 0; i < 256 * divsum; i++) {
			dv[i] = (i / divsum);
		}

		yw = yi = 0;

		int[][] stack = new int[div][3];
		int stackpointer;
		int stackstart;
		int[] sir;
		int rbs;
		int r1 = radius[index] + 1;
		int routsum, goutsum, boutsum;
		int rinsum, ginsum, binsum;

		for (y = 0; y < h; y++) {
			rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
			for (i = -radius[index]; i <= radius[index]; i++) {
				p = pix[yi + Math.min(wm, Math.max(i, 0))];
				sir = stack[i + radius[index]];
				sir[0] = (p & 0xff0000) >> 16;
				sir[1] = (p & 0x00ff00) >> 8;
				sir[2] = (p & 0x0000ff);
				// sir[0] = p>>16;
				// sir[1] = p>>8;
				// sir[2] = p;
				rbs = r1 - Math.abs(i);
				rsum += sir[0] * rbs;
				gsum += sir[1] * rbs;
				bsum += sir[2] * rbs;
				if (i > 0) {
					rinsum += sir[0];
					ginsum += sir[1];
					binsum += sir[2];
				} else {
					routsum += sir[0];
					goutsum += sir[1];
					boutsum += sir[2];
				}
				// radius = radius + 1;
			}
			stackpointer = radius[index];

			for (x = 0; x < w; x++) {

				{
					r[yi] = dv[rsum];
					g[yi] = dv[gsum];
					b[yi] = dv[bsum];

					rsum -= routsum;
					gsum -= goutsum;
					bsum -= boutsum;

					stackstart = stackpointer - radius[index] + div;
					sir = stack[stackstart % div];

					routsum -= sir[0];
					goutsum -= sir[1];
					boutsum -= sir[2];

					if (y == 0) {
						vmin[x] = Math.min(x + radius[index] + 1, wm);
					}
					p = pix[yw + vmin[x]];

					sir[0] = (p & 0xff0000) >> 16;
					sir[1] = (p & 0x00ff00) >> 8;
					sir[2] = (p & 0x0000ff);
					//
					// sir[0] = p>>16;
					// sir[1] = p>>8;
					// sir[2] = p;

					rinsum += sir[0];
					ginsum += sir[1];
					binsum += sir[2];

					rsum += rinsum;
					gsum += ginsum;
					bsum += binsum;

					stackpointer = (stackpointer + 1) % div;
					sir = stack[(stackpointer) % div];

					routsum += sir[0];
					goutsum += sir[1];
					boutsum += sir[2];

					rinsum -= sir[0];
					ginsum -= sir[1];
					binsum -= sir[2];

					yi++;
				}
			}
			yw += w;
		}
		for (x = 0; x < w; x++) {
			rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
			yp = -radius[index] * w;
			for (i = -radius[index]; i <= radius[index]; i++) {
				yi = Math.max(0, yp) + x;

				sir = stack[i + radius[index]];

				sir[0] = r[yi];
				sir[1] = g[yi];
				sir[2] = b[yi];

				rbs = r1 - Math.abs(i);

				rsum += r[yi] * rbs;
				gsum += g[yi] * rbs;
				bsum += b[yi] * rbs;

				if (i > 0) {
					rinsum += sir[0];
					ginsum += sir[1];
					binsum += sir[2];
				} else {
					routsum += sir[0];
					goutsum += sir[1];
					boutsum += sir[2];
				}

				if (i < hm) {
					yp += w;
				}
			}
			yi = x;
			stackpointer = radius[index];
			for (y = 0; y < h; y++) {
				{
					// Preserve alpha channel: ( 0xff000000 & pix[yi] )
					pix[yi] = (0xff000000 & pix[yi]) | (dv[rsum] << 16)
							| (dv[gsum] << 8) | dv[bsum];

					rsum -= routsum;
					gsum -= goutsum;
					bsum -= boutsum;

					stackstart = stackpointer - radius[index] + div;
					sir = stack[stackstart % div];

					routsum -= sir[0];
					goutsum -= sir[1];
					boutsum -= sir[2];

					if (x == 0) {
						vmin[y] = Math.min(y + r1, hm) * w;
					}
					p = x + vmin[y];

					sir[0] = r[p];
					sir[1] = g[p];
					sir[2] = b[p];

					rinsum += sir[0];
					ginsum += sir[1];
					binsum += sir[2];

					rsum += rinsum;
					gsum += ginsum;
					bsum += binsum;

					stackpointer = (stackpointer + 1) % div;
					sir = stack[stackpointer];

					routsum += sir[0];
					goutsum += sir[1];
					boutsum += sir[2];

					rinsum -= sir[0];
					ginsum -= sir[1];
					binsum -= sir[2];

					yi += w;

				}
			}
		}

		for (int tmpEllipsePixel = 0; tmpEllipsePixel < noOfPixelsInEllipse; tmpEllipsePixel++) {
			pix[intArrEllipsePosition[tmpEllipsePixel]] = intArrEllipseData[tmpEllipsePixel];
			// overlayBmp[intArrEllipsePosition[tmpEllipsePixel]] =
			// intArrEllipseData[tmpEllipsePixel];
		}

		bitmap.setPixels(pix, 0, w, 0, 0, w, h);
		}
		return (bitmap);
	}
	
	/**
	 * Brightness.
	 *
	 * @param src the src
	 * @param value the value
	 * @return the bitmap
	 */
	public static Bitmap brightness(Bitmap src, int value) {
	    // image size
	    int width = src.getWidth();
	    int height = src.getHeight();
	    // create output bitmap
	    Bitmap bmOut = Bitmap.createBitmap(width, height, src.getConfig());
	    // color information
	    int A, R, G, B;
	    int pixel;
	 
	    // scan through all pixels
	    for(int x = 0; x < width; ++x) {
	        for(int y = 0; y < height; ++y) {
	            // get pixel color
	            pixel = src.getPixel(x, y);
	            A = Color.alpha(pixel);
	            R = Color.red(pixel);
	            G = Color.green(pixel);
	            B = Color.blue(pixel);
	 
	            // increase/decrease each channel
	            R += value;
	            if(R > 255) { R = 255; }
	            else if(R < 0) { R = 0; }
	 
	            G += value;
	            if(G > 255) { G = 255; }
	            else if(G < 0) { G = 0; }
	 
	            B += value;
	            if(B > 255) { B = 255; }
	            else if(B < 0) { B = 0; }
	 
	            // apply new pixel color to output bitmap
	            bmOut.setPixel(x, y, Color.argb(A, R, G, B));
	        }
	    }
	 
	    // return final image
	    return bmOut;
	}
	
	/**
	 * Boost color intensity.
	 * 
	 * @param src
	 *            the src
	 * @return the bitmap
	 * @author siddhesh
	 */
	public static Bitmap boost(Bitmap src) {
		int width = src.getWidth();
		int height = src.getHeight();
		final float factor = 1.3f;

		Bitmap bmOut = Bitmap.createBitmap(width, height, src.getConfig());

		int[] pix = new int[width * height];
		src.getPixels(pix, 0, width, 0, 0, width, height);

		int A, R, G, B;

		int i = 0;
		for (int pixel : pix) {
			A = Color.alpha(pixel);
			R = Color.red(pixel);
			G = Color.green(pixel);
			B = Color.blue(pixel);

			R = (int) (R * factor);
			if (R > 255)
				R = 255;

			G = (int) (G * factor);
			if (G > 255)
				G = 255;

			B = (int) (B * factor);
			if (B > 255)
				B = 255;

			pix[i] = Color.argb(A, R, G, B);
			i++;
		}

		bmOut.setPixels(pix, 0, width, 0, 0, width, height);
		// src.recycle();
		return bmOut;
	}

}
