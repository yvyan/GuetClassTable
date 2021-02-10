package top.yvyan.guettable;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import top.yvyan.guettable.util.BackgroundUtil;
import top.yvyan.guettable.util.ToastUtil;

public class PersonalizedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personalized);

        TextView title = findViewById(R.id.title);
        title.setText(getString(R.string.person_personalized));
    }

    public void doBack(View view) {
        finish();
    }

    public void cleanBackground(View view) {
        BackgroundUtil.deleteBackground(getApplicationContext());
        ToastUtil.showToast(getApplicationContext(), "清除背景成功！");
    }

    public void setBackground(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 4);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 4) {
            if (data == null)
                return;
            Uri uri = data.getData();
            photoClip(uri);

        } else if (requestCode == 3) {
            if (resultCode == RESULT_OK) {
                ToastUtil.showToast(getApplicationContext(), "设置背景成功！");
            } else {
                ToastUtil.showToast(getApplicationContext(), "取消设置，已恢复默认背景！");
            }
        }
    }

    private void photoClip(Uri uri) {
        // 调用系统中自带的图片剪裁
        Intent intent = new Intent("com.android.camera.action.CROP");
        Uri imageUri = Uri.parse("file://" + "/" + BackgroundUtil.getPath(this));
        BackgroundUtil.deleteBackground(this);
        Log.d("1586", imageUri.toString());
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        intent.setDataAndType(uri, "image/*");
        // 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 9);
        intent.putExtra("aspectY", 16);
        intent.putExtra("return-data", false);
        intent.putExtra("output", imageUri);
        startActivityForResult(intent, 3);
    }
}