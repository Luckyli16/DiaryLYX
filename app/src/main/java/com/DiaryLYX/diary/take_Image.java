package com.DiaryLYX.diary;

import android.graphics.Bitmap;
import android.graphics.Matrix;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class take_Image {
    /**
     * 缩小图片
     * @param bitmap
     * @param width
     * @param height
     * @return
     */
    //缩小图片尺寸的方法。它接受一个 Bitmap 对象和目标宽度和高度作为参数，返回缩小后的 Bitmap 对象。
    public static Bitmap zoomBitmap(Bitmap bitmap, int width, int height) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        Matrix matrix = new Matrix();
        float scaleWidth = ((float) width / w);
        float scaleHeight = ((float) height / h);
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
        return newbmp;
    }

    /**
     * 将图片保存到内部存储
     * @param photoBitmap
     * @param photoName
     * @param path
     */
    //将图片保存到内部存储的方法。它接受一个 Bitmap 对象、保存路径和图片名称作为参数，并将图片保存为 PNG 格式。
    // 如果保存成功，图片将被保存到指定的路径。
    public static void savePhotoToSDCard(Bitmap photoBitmap,String path,String photoName){
        if (true) {
            //创建存储目录
            File dir = new File(path);
            if (!dir.exists()){
                dir.mkdirs();
            }
            //创建要保存的图像文件
            File photoFile = new File(path , photoName + ".png");
            //创建文件输出流，以便将图像数据写入文件。
            FileOutputStream fileOutputStream = null;
            try {
                //压缩和保存图像：
                fileOutputStream = new FileOutputStream(photoFile);
                if (photoBitmap != null) {
                    //如果是非空的，使用 compress() 方法将 Bitmap 对象以 PNG 格式压缩，并将数据写入文件输出流。
                    //压缩质量为 100，表示不压缩，保持最佳质量。
                    //调用 flush() 方法确保数据被完全写入文件。
                    if (photoBitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream)) {
                        fileOutputStream.flush();
                    }
                }
                //异常处理：
                // 在捕获 FileNotFoundException 和 IOException 异常时，删除部分或完整的图像文件。
                //关闭文件输出流。
            } catch (FileNotFoundException e) {
                photoFile.delete();
                e.printStackTrace();
            } catch (IOException e) {
                photoFile.delete();
                e.printStackTrace();
            } finally{
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
