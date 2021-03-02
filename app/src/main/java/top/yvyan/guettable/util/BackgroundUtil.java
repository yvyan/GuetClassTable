package top.yvyan.guettable.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.widget.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import top.yvyan.guettable.R;

public class BackgroundUtil {

    /**
     * 获得背景图片存放路径
     *
     * @param context context
     * @return 路径
     */
    public static String getPath(Context context) {
        return context.getExternalFilesDir(Environment.DIRECTORY_PICTURES).toString() + "/userBackground.jpg";
    }

    /**
     * 判断用户是否设置背景(根据是否有背景图片文件)
     *
     * @param context context
     * @return 结果
     */
    public static boolean isSetBackground(Context context) {
        File imageFile = new File(BackgroundUtil.getPath(context));
        return imageFile.exists();
    }

    /**
     * 删除背景图片
     *
     * @param context context
     */
    public static void deleteBackground(Context context) {
        File imageFile = new File(BackgroundUtil.getPath(context));
        if (imageFile.exists()) {
            imageFile.delete();
        }
    }

    public static void setBackground(Context context, ImageView imageView) {
        if (isSetBackground(context)) {
            try {
                FileInputStream stream = new FileInputStream(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES).toString() + "/userBackground.jpg");
                Bitmap bitmap = BitmapFactory.decodeStream(stream);
                imageView.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                deleteBackground(context);
            }
        }
    }

    public static void setPageTheme(Context context, int themeID) {
        if(context != null){
            switch (themeID) {
                case 0:
                    context.setTheme(R.style.AppTheme);
                    break;
                case 1:
                    context.setTheme(R.style.AppTheme_Pink);
                    break;
                case 2:
                    context.setTheme(R.style.AppTheme_Red);
                    break;
                case 3:
                    context.setTheme(R.style.AppTheme_Orange);
                    break;
                case 4:
                    context.setTheme(R.style.AppTheme_Green);
                    break;
            }
        }
    }
}
