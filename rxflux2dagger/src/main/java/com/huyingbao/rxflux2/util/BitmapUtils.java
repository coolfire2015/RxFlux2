package com.huyingbao.rxflux2.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Base64;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import okio.BufferedSink;
import okio.Okio;

import static android.graphics.BitmapFactory.decodeFile;

/**
 * Created by liujunfeng on 2017/1/1.
 */
public class BitmapUtils {

    /**
     * 压缩成webp
     *
     * @param uriPath
     */
    public static String compressWebp(String uriPath) {
        try {
            File file = new File(uriPath);
            //byte[] bytes = compressBitmapToBytes(file.getPath(), 600, 0, 60, Bitmap.CompressFormat.JPEG);
            //分别是图片路径，宽度高度，质量，和图片类型，重点在这里
            byte[] bytes1 = compressBitmapToBytes(file.getPath(), 600, 0, 60, Bitmap.CompressFormat.WEBP);
            File webp = new File(file.getParentFile(), file.getName().replaceAll("[.][^.]+$", "") + ".webp");
            //org.apache.commons.io.FileUtils.writeByteArrayToFile(webp, bytes1);
            BufferedSink bufferedSink = Okio.buffer(Okio.sink(file));
            bufferedSink.write(bytes1);
            bufferedSink.close();
            return webp.getPath();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * bitmap转成对应格式的byte[]
     *
     * @param filePath
     * @param reqWidth
     * @param reqHeight
     * @param quality
     * @param format
     * @return
     */
    public static byte[] compressBitmapToBytes(String filePath, int reqWidth, int reqHeight, int quality, Bitmap.CompressFormat format) {
        Bitmap bitmap = getSmallBitmap(filePath, reqWidth, reqHeight);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(format, quality, baos);
        byte[] bytes = baos.toByteArray();
        bitmap.recycle();
        return bytes;
    }

    /**
     * 将字符串转换成Bitmap类型
     */
    public static Bitmap stringToBitmap(String string) {
        Bitmap bitmap = null;
        try {
            byte[] bitmapArray;
            bitmapArray = Base64.decode(string, Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }


    /**
     * 将Bitmap转换成字符串
     */
    public static String bitmapToString(Bitmap bitmap) {
        String string = null;
        ByteArrayOutputStream bStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, bStream);
        byte[] bytes = bStream.toByteArray();
        string = Base64.encodeToString(bytes, Base64.DEFAULT);
        return string;
    }

    /**
     * 返回编码之后的bitmap
     *
     * @param filePath
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static Bitmap getSmallBitmap(String filePath, int reqWidth, int reqHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        decodeFile(filePath, options);
        //计算压缩比例
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        //options.inPreferQualityOverSpeed = true;
        Bitmap bm = BitmapFactory.decodeFile(filePath, options);
        if (bm == null) return null;
        int degree = readPictureDegree(filePath);
        bm = rotateBitmap(bm, degree);
        ByteArrayOutputStream baos = null;
        try {
            baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG, 30, baos);
        } finally {
            try {
                if (baos != null) baos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bm;
    }

    /**
     * 计算压缩比例
     *
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        int h = options.outHeight;
        int w = options.outWidth;
        int inSampleSize = 0;
        if (h > reqHeight || w > reqWidth) {
            float ratioW = (float) w / reqWidth;
            float ratioH = (float) h / reqHeight;
            inSampleSize = (int) Math.min(ratioH, ratioW);
        }
        inSampleSize = Math.max(1, inSampleSize);
        return inSampleSize;
    }

    /**
     * 写图片文件到SD卡
     *
     * @throws IOException
     */
    public static void saveImageToSD(String filePath, Bitmap bitmap, int quality) throws IOException {
        if (bitmap != null) {
            File file = new File(filePath.substring(0, filePath.lastIndexOf(File.separator)));
            if (!file.exists()) file.mkdirs();
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, bos);
            bos.flush();
            bos.close();
        }
    }

    /**
     * 将uri转成bitmap
     *
     * @param context
     * @param uri
     * @return
     */
    public static Bitmap decodeUriAsBitmap(Context context, Uri uri) {
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        return bitmap;
    }

    /**
     * 得到图片方向Orientation
     *
     * @param context
     * @param uri
     * @return
     */
    public static int getImageOrientation(Context context, Uri uri) {
        String[] cols = {MediaStore.Images.Media.ORIENTATION};
        Cursor cursor = context.getContentResolver().query(uri, cols, null, null, null);
        int rotate = 0;
        if (cursor != null) {
            cursor.moveToPosition(0);
            rotate = cursor.getInt(0);
            cursor.close();
        }
        return rotate;
    }


    /**
     * Gets the content:// URI from the given corresponding path to a file
     * 从给定的相应路径文件取得content://uri
     *
     * @param context
     * @param imageFile
     * @return content Uri
     */
    public static Uri getImageContentUri(Context context, File imageFile) {
        String filePath = imageFile.getAbsolutePath();
        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[]
                {MediaStore.Images.Media._ID}, MediaStore.Images.Media.DATA + "=? ", new String[]
                {filePath}, null);
        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
            Uri baseUri = Uri.parse("content://media/external/images/media");
            cursor.close();
            return Uri.withAppendedPath(baseUri, "" + id);
        } else {
            if (imageFile.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, filePath);
                return context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }

    public static Bitmap revitionImageSize(String path) throws IOException {
        BufferedInputStream in = new BufferedInputStream(new FileInputStream(new File(path)));
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(in, null, options);
        in.close();
        int i = 0;
        Bitmap bitmap = null;
        while (true) {
            if ((options.outWidth >> i <= 1080) && (options.outHeight >> i <= 1280)) {
                in = new BufferedInputStream(new FileInputStream(new File(path)));
                options.inSampleSize = (int) Math.pow(2.0D, i);
                options.inJustDecodeBounds = false;
                bitmap = BitmapFactory.decodeStream(in, null, options);
                int degree = readPictureDegree(path);
                bitmap = rotateBitmap(bitmap, degree);
                break;
            }
            i += 1;
        }
        return bitmap;
    }

    /**
     * 获取小bitmap
     *
     * @param filePath
     * @return
     */
    public static Bitmap getSmallBitmap(String filePath) {
        return getSmallBitmap(filePath, 0, 0);
    }


    /**
     * 设置图片旋转角度
     *
     * @param bitmap
     * @param rotate
     * @return
     */
    public static Bitmap rotateBitmap(Bitmap bitmap, int rotate) {
        if (bitmap == null) return null;

        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        // Setting post rotate to 90
        Matrix mtx = new Matrix();
        mtx.postRotate(rotate);
        return Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, true);
    }

    /**
     * 获取图片旋转角度
     *
     * @param path
     * @return
     */
    private static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * 获取图片旋转角度
     *
     * @param filepath
     * @return
     */
    public static int getExifOrientation(String filepath) {
        int degree = 0;
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(filepath);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        if (exif != null) {
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, -1);
            if (orientation != -1) {
                // We only recognise a subset of orientation tag values.
                switch (orientation) {
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        degree = 90;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_180:
                        degree = 180;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_270:
                        degree = 270;
                        break;
                }

            }
        }

        return degree;
    }
}
