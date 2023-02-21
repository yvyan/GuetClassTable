package top.yvyan.guettable;

import static top.yvyan.guettable.widget.WidgetUtil.notifyWidgetUpdate;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Process;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.taobao.sophix.SophixManager;
import com.umeng.analytics.MobclickAgent;
import com.umeng.cconfig.RemoteConfigSettings;
import com.umeng.cconfig.UMRemoteConfig;
import com.umeng.commonsdk.UMConfigure;
import com.xiaomi.mipush.sdk.MiPushClient;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import top.yvyan.guettable.activity.WebViewActivity;
import top.yvyan.guettable.baseFun.Notification;
import top.yvyan.guettable.baseFun.UpdateApp;
import top.yvyan.guettable.data.SingleSettingData;
import top.yvyan.guettable.fragment.CourseTableFragment;
import top.yvyan.guettable.fragment.DayClassFragment;
import top.yvyan.guettable.fragment.MoreFragment;
import top.yvyan.guettable.fragment.PersonFragment;
import top.yvyan.guettable.util.BackgroundUtil;

public class MainActivity extends AppCompatActivity {
    //小米推送KEY
    public static final String APP_ID = "2882303761518881128";
    public static final String APP_KEY = "5601888146128";
    //友盟KEY
    private static final String UMengKey = "600e610a6a2a470e8f8942f9";

    private SingleSettingData singleSettingData;
    private BottomNavigationView bottomNavigationView;
    private View bottomLine;
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
        // 透明状态栏
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        background = findViewById(R.id.background);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(onNavigationItemReselectedListener);
        bottomLine = findViewById(R.id.bottom_line);
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
        // 小米推送服务
        if (shouldInit()) {
            MiPushClient.registerPush(this, APP_ID, APP_KEY);
        }
        // 友盟
        UMRemoteConfig.getInstance().setConfigSettings(new RemoteConfigSettings.Builder().setAutoUpdateModeEnabled(true).build()); //在线参数
        UMRemoteConfig.getInstance().setDefaults(R.xml.cloud_config_parms);
        UMConfigure.init(this, UMengKey, "Umeng", UMConfigure.DEVICE_TYPE_PHONE, ""); //数据统计
        MobclickAgent.setPageCollectionMode(MobclickAgent.PageMode.AUTO);
        //XUpdate初始化
        UpdateApp.initXUpdate(this);
        // 获取更新
        UpdateApp.check(this);

        //获取通知
        Notification.getNotification(this);
        //清理webView缓存
        WebViewActivity.cleanCash(this);
        initTimeReceiver();
        notifyWidgetUpdate(getApplicationContext());
        SophixManager.getInstance().queryAndLoadNewPatch();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (BackgroundUtil.isSetBackground(this)) {
            bottomNavigationView.getBackground().setAlpha((int) singleSettingData.getTitleBarAlpha());
            BackgroundUtil.setBackground(this, background);
            bottomLine.setVisibility(View.GONE);
        } else {
            bottomNavigationView.getBackground().setAlpha(255);
            background.setImageBitmap(null);
            bottomLine.setVisibility(View.VISIBLE);
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
        List<ActivityManager.RunningAppProcessInfo> processInfo = am.getRunningAppProcesses();
        String mainProcessName = getPackageName();
        //获取本App的唯一标识
        int myPid = Process.myPid();
        //利用一个增强for循环取出手机里的所有进程
        for (ActivityManager.RunningAppProcessInfo info : processInfo) {
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
            recreate();
        }
    }

    /**
     * 用于监听时间变化，在过12点后刷新微件
     */
    BroadcastReceiver dateChangeReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent == null) {
                return;
            }
            String action = intent.getAction();
            if (action == null || action.isEmpty()) {
                return;
            }
            if (action.equals(Intent.ACTION_TIME_TICK)) {
                Calendar c = Calendar.getInstance();
                int hour = c.get(Calendar.HOUR_OF_DAY);
                int minute = c.get(Calendar.MINUTE);
                if (hour == 0 && minute == 0) {
                    notifyWidgetUpdate(getApplicationContext());
                }
            } else if (action.equals(Intent.ACTION_TIME_CHANGED)) {
                notifyWidgetUpdate(getApplicationContext());
            }
        }
    };

    /**
     * 注册时间监听的广播
     */
    private void initTimeReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_TIME_TICK);
        filter.addAction(Intent.ACTION_TIME_CHANGED);
        registerReceiver(dateChangeReceiver, filter);
    }

    /**
     * 底部栏Adaptor
     */
    @SuppressWarnings("deprecation")
    private static class ViewPagerAdapter extends FragmentPagerAdapter {

        private List<Fragment> list;

        public void setList(List<Fragment> list) {
            this.list = list;
            notifyDataSetChanged();
        }

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @NotNull
        @Override
        public Fragment getItem(int position) {
            return list.get(position);
        }

        @Override
        public int getCount() {
            return list != null ? list.size() : 0;
        }
    }

}

