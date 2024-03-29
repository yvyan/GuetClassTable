package top.yvyan.guettable.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.Toast;

/**
 * Created by Kevin on 2017/2/17.
 * Blog:http://blog.csdn.net/student9128
 * Description: the utils for toast.
 */
@SuppressLint("ShowToast")
public class ToastUtil {

    private static Toast mShortToast;
    private static Toast mLongToast;

    public static void showToast(Context context, String message) {
        if (mShortToast == null) {
            mShortToast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        }
        mShortToast.setText(message);
        mShortToast.show();
    }

    public static void showLongToast(Context context, String message) {
        if (mLongToast == null) {
            mLongToast = Toast.makeText(context, message, Toast.LENGTH_LONG);
        }
        mLongToast.setText(message);
        mLongToast.show();
    }
}
