package top.yvyan.guettable;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.widget.Button;

import top.yvyan.guettable.util.ToastUtil;

public class ShareActivity extends AppCompatActivity {

    private Button butShare;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);

        butShare = findViewById(R.id.btn_copy);
        butShare.setOnClickListener(view -> {
            //获取剪贴板管理器：
            ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            // 创建普通字符型ClipData
            ClipData mClipData = ClipData.newPlainText("Label", getResources().getString(R.string.share_text));
            // 将ClipData内容放到系统剪贴板里。
            cm.setPrimaryClip(mClipData);
            ToastUtil.showToast(this, "复制成功，您可以粘贴发给同学了！");
        });
    }
}