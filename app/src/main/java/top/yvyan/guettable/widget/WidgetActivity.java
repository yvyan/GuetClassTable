package top.yvyan.guettable.widget;

import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.xuexiang.xui.widget.tabbar.VerticalTabLayout;
import com.xuexiang.xui.widget.tabbar.vertical.ITabView;
import com.xuexiang.xui.widget.tabbar.vertical.TabAdapter;
import com.xuexiang.xui.widget.tabbar.vertical.TabView;
import com.xuexiang.xui.widget.toast.XToast;

import top.yvyan.guettable.R;
import top.yvyan.guettable.data.GeneralData;

public class WidgetActivity extends AppCompatActivity {

    private SeekBar seekBar;
    private VerticalTabLayout tabLayout;
    private static final String[] themes = {"red", "black", "blue", "pink", "green", "orange"};
    private GeneralData generalData;
    private TextView tv_alpha;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_widget);

        generalData = GeneralData.newInstance(this);
        seekBar = findViewById(R.id.seekBar_alpha_widget);
        seekBar.setProgress(generalData.getWidget_alpha());
        tabLayout = findViewById(R.id.tb_widget_theme);
        tabLayout.setTabAdapter(new MyAdapter());
        for (int i = 0; i < themes.length; i++) {
            if (themes[i].equals(generalData.getWidget_theme())) {
                tabLayout.setTabSelected(i);
            }
        }
        tv_alpha = findViewById(R.id.tv_alpha_number);
        tv_alpha.setText(String.valueOf(seekBar.getProgress()));

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tv_alpha.setText(String.valueOf(progress));

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        findViewById(R.id.btn_save_widget).setOnClickListener(v -> {
            int alpha = seekBar.getProgress();
            String color = themes[tabLayout.getSelectedTabPosition()];
            WidgetUtil.notifyWidgetUpdateColor(this, color);
            WidgetUtil.notifyWidgetUpdateAlpha(this, alpha);
            generalData.setWidget_alpha(alpha);
            generalData.setWidget_theme(color);
            XToast.info(this, "保存成功").show();
            finish();
        });

    }

    static class MyAdapter implements TabAdapter {

        @Override
        public int getCount() {
            return themes.length;
        }

        @Override
        public ITabView.TabBadge getBadge(int position) {
            return null;
        }

        @Override
        public ITabView.TabIcon getIcon(int position) {
            return null;
        }

        @Override
        public ITabView.TabTitle getTitle(int position) {
            String title = null;
            switch (themes[position]) {
                case "red":
                    title = "京东红";
                    break;
                case "black":
                    title = "小米黑";
                    break;
                case "blue":
                    title = "桂电蓝";
                    break;
                case "pink":
                    title = "B站粉";
                    break;
                case "green":
                    title = "微信绿";
                    break;
                case "orange":
                    title = "淘宝橙";
                    break;
            }
            if (null == title) {
                return null;
            }
            return new TabView.TabTitle.Builder()
                    .setContent(title)
                    .setTextColor(0xFF36BC9B, 0xFF757575)
                    .build();
        }

        @Override
        public int getBackground(int position) {
            return -1;
        }
    }
}
