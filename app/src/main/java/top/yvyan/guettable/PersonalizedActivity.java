package top.yvyan.guettable;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import top.yvyan.guettable.util.BackgroundUtil;
import top.yvyan.guettable.util.ToastUtil;

public class PersonalizedActivity extends AppCompatActivity {

    //ImageView background;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personalized);

        TextView title = findViewById(R.id.title);
        title.setText(getString(R.string.person_personalized));

//        Window window = this.getWindow();
//        //透明状态栏
//        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        View addStatus = findViewById(R.id.add_status);
//        ViewGroup.LayoutParams lp = addStatus.getLayoutParams();
//        lp.height = lp.height + AppUtil.getStatusBarHeight(Objects.requireNonNull(getApplicationContext()));
//        addStatus.setLayoutParams(lp);
//        background = findViewById(R.id.background);
    }

    public void doBack(View view) {
        finish();
    }

    public void cleanBackground(View view) {
        BackgroundUtil.deleteBackground(getApplicationContext());
        //updateBackground();
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
        if (requestCode == 4 && resultCode == RESULT_OK) {
            if (data == null)
                return;
            Uri uri = data.getData();
            try {
                photoClip(uri);
            } catch (Exception e) {
                try {
                    ContentResolver resolver = getContentResolver();
                    FileInputStream inputStream = (FileInputStream) resolver.openInputStream(uri);
                    FileOutputStream outputStream = new FileOutputStream(BackgroundUtil.getPath(getApplicationContext()));
                    byte[] buffer = new byte[1024];
                    int byteRead;
                    while (-1 != (byteRead = inputStream.read(buffer))) {
                        outputStream.write(buffer, 0, byteRead);
                    }
                    inputStream.close();
                    outputStream.flush();
                    outputStream.close();
                    ToastUtil.showToast(getApplicationContext(), "设置背景成功！");
                } catch (IOException fileNotFoundException) {
                    ToastUtil.showToast(getApplicationContext(), "读取错误，背景设计失败！");
                }
            }
        } else if (requestCode == 3) {
            if (resultCode == RESULT_OK) {
                ToastUtil.showToast(getApplicationContext(), "设置背景成功！");
            } else {
                ToastUtil.showToast(getApplicationContext(), "取消设置，已恢复默认背景！");
            }
        }
    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        updateBackground();
//    }
//
//    private void updateBackground() {
//        View addStatus = findViewById(R.id.add_status);
//        View titleBar = findViewById(R.id.func_base_constraintLayout);
//        if (BackgroundUtil.isSetBackground(this)) {
//            BackgroundUtil.setBackground(this, background);
//            addStatus.setBackgroundColor(getResources().getColor(R.color.colorPrimaryTransparent));
//            titleBar.setBackgroundColor(getResources().getColor(R.color.colorPrimaryTransparent));
//        } else {
//            background.setImageBitmap(null);
//            addStatus.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
//            titleBar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
//        }
//    }

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