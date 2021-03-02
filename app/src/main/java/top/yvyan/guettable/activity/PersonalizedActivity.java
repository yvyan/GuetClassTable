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
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.umeng.umcrash.UMCrash;
import com.xuexiang.xui.widget.spinner.materialspinner.MaterialSpinner;
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
import top.yvyan.guettable.data.GeneralData;
import top.yvyan.guettable.data.SingleSettingData;
import top.yvyan.guettable.util.AppUtil;
import top.yvyan.guettable.util.BackgroundUtil;
import top.yvyan.guettable.util.DensityUtil;
import top.yvyan.guettable.util.ToastUtil;

public class PersonalizedActivity extends AppCompatActivity implements MaterialSpinner.OnItemSelectedListener {

    private static String[] themes = {"桂电蓝", "B站粉", "京东红", "淘宝橙", "微信绿"};
    private SingleSettingData singleSettingData;
    private GeneralData generalData;

    private ImageView background;
    private TimetableView mTimetableView;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private Switch showOtherWeek;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        singleSettingData = SingleSettingData.newInstance(getApplicationContext());
        generalData = GeneralData.newInstance(getApplicationContext());
        setPageTheme(singleSettingData.getThemeId());
        setContentView(R.layout.activity_personalized);

        TextView title = findViewById(R.id.title);
        title.setText(getString(R.string.person_personalized));

        MaterialSpinner spinner = findViewById(R.id.spinner_theme);
        spinner.setItems(themes);
        spinner.setSelectedIndex(singleSettingData.getThemeId());
        spinner.setOnItemSelectedListener(this);

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
        initMaxWeek2SeekBar();

        showOtherWeek = findViewById(R.id.switch1);
        showOtherWeek.setChecked(!singleSettingData.isHideOtherWeek());
    }

    private void initAlphaSeekBar() {
        SeekBar dateAlphaSeekBar = findViewById(R.id.seekBar_date_alpha);
        SeekBar slideAlphaSeekBar = findViewById(R.id.seekBar_slide_alpha);
        SeekBar itemAlphaSeekBar = findViewById(R.id.seekBar_item_alpha);
        SeekBar titleBarAlphaSeekBar = findViewById(R.id.seekBar_titleBar_alpha);
        TextView dateAlphaTextView = findViewById(R.id.textView_date_alpha);
        TextView slideAlphaTextView = findViewById(R.id.textView_slide_alpha);
        TextView itemAlphaTextView = findViewById(R.id.textView_item_alpha);
        TextView titleBarAlphaTextView = findViewById(R.id.textView_titleBar_alpha);            // .
        dateAlphaSeekBar.setProgress((int) (singleSettingData.getDateAlpha() * 20));
        slideAlphaSeekBar.setProgress((int) (singleSettingData.getSlideAlpha() * 20));
        itemAlphaSeekBar.setProgress((int) (singleSettingData.getItemAlpha() * 20));
        titleBarAlphaSeekBar.setProgress((int) (singleSettingData.getTitleBarAlpha() / 12.75f));    // .
        dateAlphaTextView.setText(String.valueOf((int) (singleSettingData.getDateAlpha() * 20)));
        slideAlphaTextView.setText(String.valueOf((int) (singleSettingData.getSlideAlpha() * 20)));
        itemAlphaTextView.setText(String.valueOf((int) (singleSettingData.getItemAlpha() * 20)));
        titleBarAlphaTextView.setText(String.valueOf((int) (singleSettingData.getTitleBarAlpha() / 12.75f)));     // .
        dateAlphaSeekBar.setOnSeekBarChangeListener(onAlphaSeekBarListener);
        slideAlphaSeekBar.setOnSeekBarChangeListener(onAlphaSeekBarListener);
        itemAlphaSeekBar.setOnSeekBarChangeListener(onAlphaSeekBarListener);
        titleBarAlphaSeekBar.setOnSeekBarChangeListener(onAlphaSeekBarListener);
    }

    private final SeekBar.OnSeekBarChangeListener onAlphaSeekBarListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            float dateAlpha = singleSettingData.getDateAlpha();
            float slideAlpha = singleSettingData.getSlideAlpha();
            float itemAlpha = singleSettingData.getItemAlpha();
            float titleBarAlpha = singleSettingData.getTitleBarAlpha();
            if (R.id.seekBar_date_alpha == seekBar.getId()) {
                dateAlpha = i / 20.0f;
                TextView dateAlphaTextView = findViewById(R.id.textView_date_alpha);
                dateAlphaTextView.setText(String.valueOf(i));
            } else if (R.id.seekBar_slide_alpha == seekBar.getId()) {
                slideAlpha = i / 20.0f;
                TextView slideAlphaTextView = findViewById(R.id.textView_slide_alpha);
                slideAlphaTextView.setText(String.valueOf(i));
            } else if (R.id.seekBar_item_alpha == seekBar.getId()) {
                itemAlpha = i / 20.0f;
                TextView itemAlphaTextView = findViewById(R.id.textView_item_alpha);
                itemAlphaTextView.setText(String.valueOf(i));
            } else if (R.id.seekBar_titleBar_alpha == seekBar.getId()) {
                titleBarAlpha = 12.75f * i;
                TextView titleBarTextView = findViewById(R.id.textView_titleBar_alpha);
                titleBarTextView.setText(String.valueOf(i));
            }
            if (BackgroundUtil.isSetBackground(getApplicationContext())) {
                singleSettingData.setAlpha(dateAlpha, slideAlpha, itemAlpha, titleBarAlpha);
                ConstraintLayout header = findViewById(R.id.func_base_constraintLayout);
                header.getBackground().setAlpha((int) titleBarAlpha);
                mTimetableView
                        .alpha(dateAlpha, slideAlpha, itemAlpha)
                        .updateView();
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
        }
    };

    private void initLengthSeekBar() {
        SeekBar itemLengthSeekBar = findViewById(R.id.seekBar_item_length);
        TextView itemLengthTextView = findViewById(R.id.textView_item_length);
        itemLengthSeekBar.setProgress((singleSettingData.getItemLength() - 40) / 5);
        itemLengthTextView.setText(String.valueOf(itemLengthSeekBar.getProgress()));
        itemLengthSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                itemLengthTextView.setText(String.valueOf(i));
                singleSettingData.setItemLength(i * 5 + 40);
                mTimetableView
                        .itemHeight(DensityUtil.dip2px(getApplicationContext(), singleSettingData.getItemLength()))
                        .updateView();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    private void initMaxWeek2SeekBar() {
        SeekBar maxWeekSeekBar = findViewById(R.id.seekBar_max_week);
        TextView textView = findViewById(R.id.textView_max_week);
        maxWeekSeekBar.setProgress(generalData.getMaxWeek() - 15);
        textView.setText(String.valueOf(generalData.getMaxWeek()));
        maxWeekSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                textView.setText(String.valueOf(15 + i));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                generalData.setMaxWeek(Integer.parseInt(String.valueOf(textView.getText())));
            }
        });
    }

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
        ConstraintLayout titleBar = findViewById(R.id.func_base_constraintLayout);
        if (BackgroundUtil.isSetBackground(this)) {
            BackgroundUtil.setBackground(this, background);
            addStatus.setBackgroundColor(getResources().getColor(R.color.colorPrimaryTransparent));
            titleBar.getBackground().setAlpha((int) singleSettingData.getTitleBarAlpha());
            mTimetableView.colorPool().setUselessColor(0xCCCCCC);
            mTimetableView.alpha(singleSettingData.getDateAlpha(), singleSettingData.getSlideAlpha(), singleSettingData.getItemAlpha());
        } else {
            background.setImageBitmap(null);
            addStatus.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            titleBar.getBackground().setAlpha(255);
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

    void setPageTheme(int id) {
        switch (id) {
            case 0:
                setTheme(R.style.AppTheme);
                break;
            case 1:
                setTheme(R.style.AppTheme_Pink);
                break;
            case 2:
                setTheme(R.style.AppTheme_Red);
                break;
            case 3:
                setTheme(R.style.AppTheme_Orange);
                break;
            case 4:
                setTheme(R.style.AppTheme_Green);
                break;
        }
    }

    @Override
    public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
        setPageTheme(position);
        singleSettingData.setThemeId(position);
        finish();
        ToastUtil.showToast(this, "修改主题成功！");
    }
}