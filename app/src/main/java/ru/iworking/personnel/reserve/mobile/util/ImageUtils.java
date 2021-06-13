package ru.iworking.personnel.reserve.mobile.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Objects;

public class ImageUtils {

    public static byte[] toByteArray(BitmapDrawable drawable) {
        if (drawable != null) {
            try(ByteArrayOutputStream stream = new ByteArrayOutputStream()) {
                Bitmap bitmap = drawable.getBitmap();
                if (bitmap != null) {
                    Log.e("Original   dimensions", bitmap.getWidth()+" "+bitmap.getHeight());
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream);
                    Bitmap decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(stream.toByteArray()));
                    Log.e("Compressed dimensions", decoded.getWidth()+" "+decoded.getHeight());
                    return stream.toByteArray();
                }
            } catch (IOException e) {
                Log.e(ImageUtils.class.getSimpleName(), Objects.requireNonNull(e.getMessage()));
            }
        }
        return new byte[] {};
    }

    public static Bitmap toBitmap(byte[] byteArray) {
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
    }

    /**
     * reduces the size of the image
     * @param image
     * @param maxSize
     * @return
     */
    public static Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float)width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

}
