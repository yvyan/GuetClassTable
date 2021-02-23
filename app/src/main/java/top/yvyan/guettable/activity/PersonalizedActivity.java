package top.yvyan.guettable.activity;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.umeng.umcrash.UMCrash;
import com.xuexiang.xui.widget.picker.XSeekBar;
import com.zhuangfei.timetable.TimetableView;
import com.zhuangfei.timetable.listener.OnItemBuildAdapter;
import com.zhuangfei.timetable.model.Schedule;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import top.yvyan.guettable.R;
import top.yvyan.guettable.bean.CourseBean;
import top.yvyan.guettable.data.SingleSettingData;
import top.yvyan.guettable.util.AppUtil;
import top.yvyan.guettable.util.BackgroundUtil;
import top.yvyan.guettable.util.DensityUtil;
import top.yvyan.guettable.util.ToastUtil;

public class PersonalizedActivity extends AppCompatActivity {

    private SingleSettingData singleSettingData;

    private ImageView background;
    private TimetableView mTimetableView;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private Switch showOtherWeek;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personalized);

        TextView title = findViewById(R.id.title);
        title.setText(getString(R.string.person_personalized));

        singleSettingData = SingleSettingData.newInstance(getApplicationContext());

        Window window = this.getWindow();
        //透明状态栏
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        View addStatus = findViewById(R.id.add_status);
        ViewGroup.LayoutParams lp = addStatus.getLayoutParams();
        lp.height = lp.height + AppUtil.getStatusBarHeight(Objects.requireNonNull(getApplicationContext()));
        addStatus.setLayoutParams(lp);
        background = findViewById(R.id.background);

        initCourseTable();
        initAlphaSeekBar();
        initLengthSeekBar();

        showOtherWeek = findViewById(R.id.switch1);
        showOtherWeek.setChecked(!singleSettingData.isHideOtherWeek());
    }

    private void initAlphaSeekBar() {
        XSeekBar seekBarDateAlpha = findViewById(R.id.seekBar_date_alpha);
        XSeekBar seekBarSlideAlpha = findViewById(R.id.seekBar_slide_alpha);
        XSeekBar seekBarItemAlpha = findViewById(R.id.seekBar_item_alpha);
        seekBarDateAlpha.setDefaultValue((int) (singleSettingData.getDateAlpha() * 20));
        seekBarSlideAlpha.setDefaultValue((int) (singleSettingData.getSlideAlpha() * 20));
        seekBarItemAlpha.setDefaultValue((int) (singleSettingData.getItemAlpha() * 20));
        seekBarDateAlpha.setOnSeekBarListener(onSeekBarAlphaListener);
        seekBarSlideAlpha.setOnSeekBarListener(onSeekBarAlphaListener);
        seekBarItemAlpha.setOnSeekBarListener(onSeekBarAlphaListener);
    }

    private final XSeekBar.OnSeekBarListener onSeekBarAlphaListener = (seekBar, newValue) -> {
        float dateAlpha = singleSettingData.getDateAlpha();
        float slideAlpha = singleSettingData.getSlideAlpha();
        float itemAlpha = singleSettingData.getItemAlpha();
        boolean update = false;
        if (R.id.seekBar_date_alpha == seekBar.getId()) {
            if (dateAlpha != newValue / 20.0f) {
                dateAlpha = newValue / 20.0f;
                update = true;

            }
        } else if (R.id.seekBar_slide_alpha == seekBar.getId()) {
            if (slideAlpha != newValue / 20.0f) {
                slideAlpha = newValue / 20.0f;
                update = true;
            }
        } else if (R.id.seekBar_item_alpha == seekBar.getId()) {
            if (itemAlpha != newValue / 20.0f) {
                itemAlpha = newValue / 20.0f;
                update = true;
            }
        }
        if (update && BackgroundUtil.isSetBackground(getApplicationContext())) {
            singleSettingData.setAlpha(dateAlpha, slideAlpha, itemAlpha);
            mTimetableView
                    .alpha(dateAlpha, slideAlpha, itemAlpha)
                    .updateView();
        }
    };

    private void initLengthSeekBar() {
        XSeekBar seekBarItemLength = findViewById(R.id.seekBar_item_length);
        seekBarItemLength.setDefaultValue((singleSettingData.getItemLength() - 40) / 5);
        seekBarItemLength.setOnSeekBarListener(onSeekBarLengthListener);
    }

    private final XSeekBar.OnSeekBarListener onSeekBarLengthListener = (seekBar, newValue) -> {
        int itemLength = singleSettingData.getItemLength();
        if (itemLength != (newValue * 5) + 40) {
            itemLength = (newValue * 5) + 40;
            singleSettingData.setItemLength(itemLength);
            mTimetableView
                    .itemHeight(DensityUtil.dip2px(getApplicationContext(), itemLength))
                    .updateView();
        }
    };

    private void initCourseTable() {
        List<Schedule> schedules = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            Schedule schedule = new Schedule();
            schedule.setName("示例课程" + i + 1);
            schedule.setRoom("教室" + i + 1);
            schedule.setStart(1);
            schedule.setStep(2);
            schedule.setDay(i * 2 + 1);
            List<Integer> weekList = new ArrayList<>();
            weekList.add(1);
            schedule.setWeekList(weekList);
            schedule.putExtras(CourseBean.TYPE, 0);
            schedules.add(schedule);
        }
        for (int i = 0; i < 2; i++) {
            Schedule schedule = new Schedule();
            schedule.setName("示例课程" + (i) / 2 + 3);
            schedule.setRoom("教室" + (i) / 2 + 3);
            schedule.setStart(1);
            schedule.setStep(2);
            schedule.setDay(i + 5);
            List<Integer> weekList = new ArrayList<>();
            weekList.add(2);
            schedule.setWeekList(weekList);
            schedule.putExtras(CourseBean.TYPE, 0);
            schedules.add(schedule);
        }

        mTimetableView = findViewById(R.id.id_timetableView);
        mTimetableView.curWeek(1)
                .data(schedules)
                .maxSlideItem(12)
                .monthWidthDp(18)
                .itemHeight(DensityUtil.dip2px(getApplicationContext(), singleSettingData.getItemLength()))
                .callback(new OnItemBuildAdapter() {
                    @Override
                    public String getItemText(Schedule schedule, boolean isThisWeek) {
                        if (schedule.getRoom() != null) {
                            return schedule.getName() + "@" + schedule.getRoom();
                        } else {
                            return schedule.getName();
                        }
                    }
                })
                //隐藏旗标布局
                .isShowFlaglayout(false)
                .isShowNotCurWeek(!singleSettingData.isHideOtherWeek())
                .updateView();
    }

    public void doBack(View view) {
        finish();
    }

    public void cleanBackground(View view) {
        BackgroundUtil.deleteBackground(getApplicationContext());
        updateBackground();
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
                } catch (IOException e1) {
                    ToastUtil.showToast(getApplicationContext(), "读取错误，背景设计失败！");
                    UMCrash.generateCustomLog(e1, "IOException");
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

    @Override
    protected void onStart() {
        super.onStart();
        updateBackground();
    }

    private void updateBackground() {
        View addStatus = findViewById(R.id.add_status);
        View titleBar = findViewById(R.id.func_base_constraintLayout);
        if (BackgroundUtil.isSetBackground(this)) {
            BackgroundUtil.setBackground(this, background);
            addStatus.setBackgroundColor(getResources().getColor(R.color.colorPrimaryTransparent));
            titleBar.setBackgroundColor(getResources().getColor(R.color.colorPrimaryTransparent));
            mTimetableView.colorPool().setUselessColor(0xCCCCCC);
            mTimetableView.alpha(singleSettingData.getDateAlpha(), singleSettingData.getSlideAlpha(), singleSettingData.getItemAlpha());
        } else {
            background.setImageBitmap(null);
            addStatus.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            titleBar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            mTimetableView.colorPool().setUselessColor(0xE0E0E0);
            mTimetableView.alpha(1, 1, 1);
        }
        mTimetableView.updateView();
    }

    private void photoClip(Uri uri) {
        // 调用系统中自带的图片剪裁
        Intent intent = new Intent("com.android.camera.action.CROP");
        Uri imageUri = Uri.parse("file://" + "/" + BackgroundUtil.getPath(this));
        BackgroundUtil.deleteBackground(this);
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

    public void showOtherWeek(View view) {
        singleSettingData.setHideOtherWeek(!singleSettingData.isHideOtherWeek());
        showOtherWeek.setChecked(!singleSettingData.isHideOtherWeek());
        mTimetableView
                .isShowNotCurWeek(!singleSettingData.isHideOtherWeek())
                .updateView();
    }
}