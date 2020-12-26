package top.yvyan.guettable.util;

import android.content.Context;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import top.yvyan.guettable.R;

public class TextDialog {

    private static AlertDialog dialog;

    /**
     * 显示弹窗
     *
     * @param context 上下文
     * @param text    自定义显示的文字
     */
    public static void showScanNumberDialog(final Context context, String text) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        dialog = builder.create();
        dialog.show();
        dialog.getWindow().clearFlags(
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        dialog.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        Window window = dialog.getWindow();
        window.setContentView(R.layout.text_dialog);
        TextView tv_scan_number = (TextView) window
                .findViewById(R.id.text_dialog);
        tv_scan_number.setText(text);
        Button btn_hint_yes = window.findViewById(R.id.btn_text_yes);
        btn_hint_yes.setOnClickListener(arg0 -> {
            dialog.dismiss();
        });
    }

}
