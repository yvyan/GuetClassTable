package top.yvyan.guettable.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Objects;

import top.yvyan.guettable.R;

@SuppressWarnings("ResultOfMethodCallIgnored")
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

    /**
     * 给某一视图设置背景图片
     *
     * @param context   context
     * @param imageView 背景图片视图
     */
    public static void setBackground(Context context, ImageView imageView) {
        if (isSetBackground(context)) {
            try {
                FileInputStream stream = new FileInputStream(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES).toString() + "/userBackground.jpg");
                Bitmap bitmap = BitmapFactory.decodeStream(stream);
                if (imageView != null) {
                    imageView.setImageBitmap(bitmap);
                }
            } catch (FileNotFoundException e) {
                deleteBackground(context);
            }
        }
    }

    /**
     * 设置主题
     *
     * @param context context
     * @param themeID 主题id
     */
    public static void setPageTheme(Context context, int themeID) {
        if (context != null) {
            switch (themeID) {
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
                default:
                    context.setTheme(R.style.AppTheme);
                    break;
            }
        }
    }

    /**
     * 设置无背景图Activity的沉浸式状态栏+满透明度
     *
     * @param context Activity
     */
    public static void setFullAlphaStatus(Activity context) {
        if (context != null) {
            context.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            View addStatus = context.findViewById(R.id.add_status);
            ViewGroup.LayoutParams lp = addStatus.getLayoutParams();
            lp.height = lp.height + AppUtil.getStatusBarHeight(Objects.requireNonNull(context.getApplicationContext()));
            addStatus.setLayoutParams(lp);
            addStatus.getBackground().setAlpha(255);
        }
    }
}
