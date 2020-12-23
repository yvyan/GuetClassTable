package top.yvyan.guettable;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Process;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.xiaomi.channel.commonutils.logger.LoggerInterface;
import com.xiaomi.market.sdk.UpdateStatus;
import com.xiaomi.market.sdk.XiaomiUpdateAgent;
import com.xiaomi.mipush.sdk.Logger;
import com.xiaomi.mipush.sdk.MiPushClient;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import top.yvyan.guettable.data.GeneralData;
import top.yvyan.guettable.data.SettingData;
import top.yvyan.guettable.fragment.CourseTableFragment;
import top.yvyan.guettable.fragment.DayClassFragment;
import top.yvyan.guettable.fragment.MoreFragment;
import top.yvyan.guettable.fragment.OnButtonClick;
import top.yvyan.guettable.fragment.PersonFragment;
import top.yvyan.guettable.helper.ViewPagerAdapter;
import top.yvyan.guettable.util.NotificationUtil;
import top.yvyan.guettable.util.TimeUtil;

public class MainActivity extends AppCompatActivity implements OnButtonClick {

    public static final String APP_ID = "2882303761518881128";
    public static final String APP_KEY = "5601888146128";
    public static final String TAG = "MainActivityInfo";

    private BottomNavigationView bottomNavigationView;
    private ViewPagerAdapter viewPagerAdapter;
    private ViewPager viewPager;
    private MenuItem menuItem;

    private GeneralData generalData;

    private DayClassFragment dayClassFragment;
    private PersonFragment personFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_main);

        if (shouldInit()) {
            MiPushClient.registerPush(this, APP_ID, APP_KEY);
        }
        LoggerInterface newLogger = new LoggerInterface() {
            @Override
            public void setTag(String tag) {
                // ignore
            }
            @Override
            public void log(String content, Throwable t) {
                Log.d(TAG, content, t);
            }
            @Override
            public void log(String content) {
                Log.d(TAG, content);
            }
        };
        Logger.setLogger(this, newLogger);
        //检查更新
        generalData = GeneralData.newInstance(this);
        if (SettingData.newInstance(this).isAppCheckUpdate()) {
            if (generalData.getAppLastUpdateTime() == -1 || TimeUtil.calcDayOffset(new Date(generalData.getAppLastUpdateTime()), new Date()) >= 3) {
                checkUpdate();
            }
        }

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
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);
        List<Fragment> list = new ArrayList<>();
        list.add(DayClassFragment.newInstance());
        list.add(CourseTableFragment.newInstance());
        list.add(MoreFragment.newInstance());
        list.add(PersonFragment.newInstance());
        viewPagerAdapter.setList(list);

        dayClassFragment = DayClassFragment.newInstance();
        dayClassFragment.setOnButtonClick(this);
        personFragment = PersonFragment.newInstance();
        personFragment.setOnButtonClick(this);
    }

    private void checkUpdate() {
        XiaomiUpdateAgent.update(this);
        XiaomiUpdateAgent.setUpdateAutoPopup(false);
        XiaomiUpdateAgent.setUpdateListener((i, updateResponse) -> {
            switch (i) {
                case UpdateStatus.STATUS_UPDATE: //有更新
                    NotificationUtil.showMessage(this, UpdateActivity.class, "课程表有新版本了", updateResponse.updateLog, 101);
                    generalData.setAppLastUpdateTime(System.currentTimeMillis());
                    break;
                default:
                    break;
            }
        });
    }


    @Override
    public void onClick(int n) {
        viewPager.setCurrentItem(n);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemReselectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

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
}