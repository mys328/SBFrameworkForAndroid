package com.sb.framework.bitmap;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import junit.framework.Assert;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.text.TextUtils;


public class BitmapUtils4 {
	public static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		bmp.compress(CompressFormat.PNG, 100, output);
		if (needRecycle) {
			bmp.recycle();
		}
		
		byte[] result = output.toByteArray();
		try {
			output.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	public static byte[] resizeBitmap(byte[] pic, int newWidth, int newHeight) {
		if(pic == null) {
			return null;
		}
		Bitmap oldPhoto = BitmapFactory.decodeByteArray(pic, 0, pic.length);
		int oldWidth = oldPhoto.getWidth();
		int oldHeight = oldPhoto.getHeight();
		float scaleWidth = ((float) newWidth) / oldWidth;
		float scaleHeight = ((float) newHeight) / oldHeight;
		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);
		Bitmap newPhoto = Bitmap.createBitmap(oldPhoto, 0, 0, oldWidth, oldHeight, matrix, true);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		newPhoto.compress(Bitmap.CompressFormat.PNG, 100, baos);
		byte[] bytes = baos.toByteArray();
		try {
			baos.close();
			return bytes;
		} catch (IOException e) {
			return null;
		}
	}
	
	public static Bitmap crop(Bitmap bm) {
		if (bm == null)
			return null;
		int width = bm.getWidth();
		int height = bm.getHeight();
		int side = Math.min(width, height);
		return Bitmap.createBitmap(bm, 0, 0, side, side);
	}

    public static Bitmap scaleBitmap(Bitmap src, float w, float h) {
        if (src == null)
            return null;
        int srcW = src.getWidth();
        int srcH = src.getHeight();
        Matrix matrix = new Matrix();
        matrix.setScale(w / srcW, h / srcH);
        Bitmap result = Bitmap.createBitmap(src, 0, 0, srcW, srcH, matrix, false);
        return result;
    }

	public static Bitmap fromByteArray(byte[] pic) {
		if (pic == null || pic.length == 0) {
			return null;
		}
		Bitmap bm = BitmapFactory.decodeByteArray(pic, 0, pic.length);
		return bm;
	}
	
	public static Bitmap getBitmapFromFile(String name) {
	    if(TextUtils.isEmpty(name)) {
	    	return null;
	    }
	    return BitmapFactory.decodeFile(name);
	}
	
	private static final int MAX_DECODE_PICTURE_SIZE = 1920 * 1440;
	public static Bitmap extractThumbNail(final String path, final int height, final int width, final boolean crop) {
		Assert.assertTrue(path != null && !path.equals("") && height > 0 && width > 0);

		BitmapFactory.Options options = new BitmapFactory.Options();

		try {
			options.inJustDecodeBounds = true;
			Bitmap tmp = BitmapFactory.decodeFile(path, options);
			if (tmp != null) {
				tmp.recycle();
				tmp = null;
			}

			final double beY = options.outHeight * 1.0 / height;
			final double beX = options.outWidth * 1.0 / width;
			options.inSampleSize = (int) (crop ? (beY > beX ? beX : beY) : (beY < beX ? beX : beY));
			if (options.inSampleSize <= 1) {
				options.inSampleSize = 1;
			}

			// NOTE: out of memory error
			while (options.outHeight * options.outWidth / options.inSampleSize > MAX_DECODE_PICTURE_SIZE) {
				options.inSampleSize++;
			}

			int newHeight = height;
			int newWidth = width;
			if (crop) {
				if (beY > beX) {
					newHeight = (int) (newWidth * 1.0 * options.outHeight / options.outWidth);
				} else {
					newWidth = (int) (newHeight * 1.0 * options.outWidth / options.outHeight);
				}
			} else {
				if (beY < beX) {
					newHeight = (int) (newWidth * 1.0 * options.outHeight / options.outWidth);
				} else {
					newWidth = (int) (newHeight * 1.0 * options.outWidth / options.outHeight);
				}
			}

			options.inJustDecodeBounds = false;

			Bitmap bm = BitmapFactory.decodeFile(path, options);
			if (bm == null) {
				//WLog.e(BitmapUtils.class, "bitmap decode failed");
				return null;
			}

			final Bitmap scale = Bitmap.createScaledBitmap(bm, newWidth, newHeight, true);
			if (scale != null) {
				bm.recycle();
				bm = scale;
			}

			if (crop) {
				final Bitmap cropped = Bitmap.createBitmap(bm, (bm.getWidth() - width) >> 1, (bm.getHeight() - height) >> 1, width, height);
				if (cropped == null) {
					return bm;
				}

				bm.recycle();
				bm = cropped;
			}
			return bm;

		} catch (final OutOfMemoryError e) {
			//WLog.e(BitmapUtils.class, "decode bitmap failed: " + e.getMessage());
			options = null;
		}

		return null;
	}

	public static InputStream BitmapToInputStream(Bitmap bitmap) {
	    ByteArrayOutputStream baos = new ByteArrayOutputStream();
	    bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
	    InputStream is = new ByteArrayInputStream(baos.toByteArray());  
	    return is;
	}

    public static Bitmap efficientLoader(Context ctx, int id, int reqWidth, int reqHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        // First decode with inJustDecodeBounds=true to check dimensions
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(ctx.getResources(), id, options);
        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        // Decode bitmap with inSampleSize set
        return BitmapFactory.decodeResource(ctx.getResources(), id, options);
    }

    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
    
        if (height > reqHeight || width > reqWidth) {
            if (width > height) {
                inSampleSize = Math.round((float)height / (float)reqHeight);
            } else {
                inSampleSize = Math.round((float)width / (float)reqWidth);
            }
        }
        return inSampleSize;
    }
}
