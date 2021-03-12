package top.yvyan.guettable;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Process;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.umeng.analytics.MobclickAgent;
import com.umeng.cconfig.RemoteConfigSettings;
import com.umeng.cconfig.UMRemoteConfig;
import com.umeng.commonsdk.UMConfigure;
import com.xiaomi.mipush.sdk.MiPushClient;

import java.util.ArrayList;
import java.util.List;

import top.yvyan.guettable.data.SingleSettingData;
import top.yvyan.guettable.fragment.CourseTableFragment;
import top.yvyan.guettable.fragment.DayClassFragment;
import top.yvyan.guettable.fragment.MoreFragment;
import top.yvyan.guettable.fragment.PersonFragment;
import top.yvyan.guettable.helper.ViewPagerAdapter;
import top.yvyan.guettable.service.app.UpdateApp;
import top.yvyan.guettable.util.BackgroundUtil;

public class MainActivity extends AppCompatActivity {

    public static final String APP_ID = "2882303761518881128";
    public static final String APP_KEY = "5601888146128";

    private SingleSettingData singleSettingData;
    private BottomNavigationView bottomNavigationView;
    private ViewPager viewPager;
    private MenuItem menuItem;
    private ImageView background;

    private static int themeID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        singleSettingData = SingleSettingData.newInstance(this);
        themeID = singleSettingData.getThemeId();
        BackgroundUtil.setPageTheme(this, themeID);
        setContentView(R.layout.activity_main);

        //透明状态栏
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        background = findViewById(R.id.background);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(onNavigationItemReselectedListener);
        viewPager = findViewById(R.id.vp);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            //用于页面滑动时底部Tab切换
            @Override
            public void onPageSelected(int position) {
                if (menuItem != null) {
                    menuItem.setChecked(false);
                } else {
                    bottomNavigationView.getMenu().getItem(0).setChecked(false);
                }
                menuItem = bottomNavigationView.getMenu().getItem(position);
                menuItem.setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);
        List<Fragment> list = new ArrayList<>();
        list.add(DayClassFragment.newInstance());
        list.add(CourseTableFragment.newInstance());
        list.add(MoreFragment.newInstance());
        list.add(PersonFragment.newInstance());
        viewPagerAdapter.setList(list);

        if (shouldInit()) {
            MiPushClient.registerPush(this, APP_ID, APP_KEY);
        }

        //友盟
        UMRemoteConfig.getInstance().setConfigSettings(new RemoteConfigSettings.Builder().setAutoUpdateModeEnabled(true).build()); //在线参数
        UMRemoteConfig.getInstance().setDefaults(R.xml.cloud_config_parms);
        UMConfigure.init(this, "600e610a6a2a470e8f8942f9", "Umeng", UMConfigure.DEVICE_TYPE_PHONE, ""); //数据统计
        MobclickAgent.setPageCollectionMode(MobclickAgent.PageMode.AUTO);

        UpdateApp.check(this, 1);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (BackgroundUtil.isSetBackground(this)) {
            bottomNavigationView.getBackground().setAlpha((int) singleSettingData.getTitleBarAlpha());
            BackgroundUtil.setBackground(this, background);
        } else {
            bottomNavigationView.getBackground().setAlpha(255);
            background.setImageBitmap(null);
        }
    }

    public void onClick(int n) {
        viewPager.setCurrentItem(n);
    }

    private final BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemReselectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @SuppressLint("NonConstantResourceId")
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            menuItem = item;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    viewPager.setCurrentItem(0);
                    return true;
                case R.id.navigation_dashboard:
                    viewPager.setCurrentItem(1);
                    return true;
                case R.id.navigation_func:
                    viewPager.setCurrentItem(2);
                    return true;
                case R.id.navigation_person:
                    viewPager.setCurrentItem(3);
                    return true;
            }
            return false;
        }
    };

    private boolean shouldInit() {
        //通过ActivityManager我们可以获得系统里正在运行的activities
        //包括进程(Process)等、应用程序/包、服务(Service)、任务(Task)信息。
        ActivityManager am = ((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE));
        List<ActivityManager.RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
        String mainProcessName = getPackageName();
        //获取本App的唯一标识
        int myPid = Process.myPid();
        //利用一个增强for循环取出手机里的所有进程
        for (ActivityManager.RunningAppProcessInfo info : processInfos) {
            //通过比较进程的唯一标识和包名判断进程里是否存在该App
            if (info.pid == myPid && mainProcessName.equals(info.processName)) {
                return true;
            }
        }
        return false;
    }

    // 只显示一次启动页
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (themeID != singleSettingData.getThemeId()) {
            themeID = singleSettingData.getThemeId();
            BackgroundUtil.setPageTheme(this, themeID);
            TypedValue typedValue = new TypedValue();
            Resources.Theme theme = getTheme();
            theme.resolveAttribute(R.attr.color_bar, typedValue, true);
            int[][] states = new int[2][];
            int[] color = new int[2];
            TypedArray typedArray = this.obtainStyledAttributes(new int[]{R.attr.color_bar});
            int attrColor = typedArray.getColor(0, 0);
            typedArray.recycle();
            states[0] = new int[]{android.R.attr.state_checked};
            states[1] = new int[]{-android.R.attr.state_checked};
            color[0] = attrColor;
            color[1] = R.color.tab_unchecked;
            ColorStateList itemIconTintList = new ColorStateList(states, color);
            bottomNavigationView.setItemIconTintList(itemIconTintList);
            bottomNavigationView.setItemTextColor(itemIconTintList);
            viewPager.setCurrentItem(0, false);
        }
    }

}