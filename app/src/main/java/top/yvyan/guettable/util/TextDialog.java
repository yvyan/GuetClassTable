package top.yvyan.guettable.util;

import android.content.Context;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import top.yvyan.guettable.R;

public class TextDialog {

    /**
     * showDialog点击处理事件
     */
    public interface IDialogService {
        int onClickYes();
        int onClickBack();
    }

    /**
     * 显示文字弹窗(带事件处理)
     *
     * @param context  context
     * @param title    标题
     * @param canClose 是否显示右上角关闭按钮
     * @param yesText  确定按钮显示的文字
     * @param backText 返回按钮显示的文字
     * @param comm     详细内容
     * @param dialogService 点击处理事件
     */
    public static void showDialog(final Context context, String title, boolean canClose, String yesText, String backText, String comm, IDialogService dialogService) {
        AlertDialog dialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        // 创建对话框
        dialog = builder.create();
        // 弹出对话框
        dialog.show();
        // 以下两行代码是对话框的EditText点击后不能显示输入法的
        dialog.getWindow().clearFlags(
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        dialog.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        // *** 主要就是在这里实现这种效果的.
        // 设置窗口的内容页面,shrew_exit_dialog.xml文件中定义view内容
        Window window = dialog.getWindow();
        window.setContentView(R.layout.general_dialog);
        ImageView close = (ImageView) window.findViewById(R.id.dialog_close);
        if (!canClose) {
            close.setVisibility(View.GONE);
        }
        TextView titleView = (TextView) window.findViewById(R.id.dialog_title);
        TextView commView = (TextView) window.findViewById(R.id.dialog_comm);
        Button buttonYes = (Button) window.findViewById(R.id.dialog_yes);
        Button buttonBack = (Button) window.findViewById(R.id.dialog_back);

        titleView.setText(title);
        commView.setText(comm);
        close.setOnClickListener(view -> dialog.dismiss());
        buttonYes.setText(yesText);
        buttonYes.setOnClickListener(view -> {
            dialogService.onClickYes();
            dialog.dismiss();
        });
        buttonBack.setText(backText);
        buttonBack.setOnClickListener(view -> {
            dialogService.onClickBack();
            dialog.dismiss();
        });
    }

    /**
     * 显示弹窗
     *
     * @param context 上下文
     * @param text    自定义显示的文字
     */
    public static void showScanNumberDialog(final Context context, String text) {
        AlertDialog dialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        dialog = builder.create();
        dialog.show();
        dialog.setCanceledOnTouchOutside(false);
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
