package top.yvyan.guettable;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;
import android.view.View;
import android.widget.TextView;

import top.yvyan.guettable.data.TokenData;

public class SettingActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        setTheme(R.style.ThemeText);

        TextView title = findViewById(R.id.title);
        title.setText("设置");
    }

    public void doBack(View view) {
        finish();
    }

    public static class SettingFragment extends PreferenceFragment {

        public static final String BASE_SETTINGS = "basic_settings";
        public static final String REFRESH_DATA = "refresh_data";
        public static final String REFRESH_DATA_FREQUENCY = "refresh_data_frequency";
        public static final String SHOW_LIB_ON_TABLE = "show_lib_on_table";
        public static final String SHOW_EXAM_ON_TABLE = "show_exam_on_table";
        public static final String SHOW_TOOLS_ON_DAY_CLASS = "show_tools_on_day_class";
        public static final String APP_CHECK_UPDATE = "app_check_update";
        public static final String CLASS_LENGTH = "class_length";
        public static final String DEVELOPER_MODE = "developer_mode";


        private PreferenceCategory basicSettings;
        private ListPreference refreshDataFrequency;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_setting);

            basicSettings = (PreferenceCategory) findPreference(BASE_SETTINGS);

            SwitchPreference refreshData = (SwitchPreference) findPreference(REFRESH_DATA);
            refreshData.setOnPreferenceChangeListener((preference, newValue) -> {
                if((boolean)newValue) {
                    basicSettings.addPreference(refreshDataFrequency);
                } else {
                    basicSettings.removePreference(refreshDataFrequency);
                }
                return true;
            });

            refreshDataFrequency = (ListPreference) findPreference(REFRESH_DATA_FREQUENCY);
            refreshDataFrequency.setSummary("刷新频率：" + refreshDataFrequency.getValue() + "天");
            refreshDataFrequency.setOnPreferenceChangeListener((preference, newValue) -> {
                refreshDataFrequency.setSummary("刷新频率：" + newValue.toString() + "天");
                return true;
            });

            SwitchPreference developMode = (SwitchPreference) findPreference(DEVELOPER_MODE);
            developMode.setOnPreferenceChangeListener((preference, newValue) -> {
                if (!(boolean)newValue) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { //尽可能自动关闭调试
                        TokenData tokenData = TokenData.newInstance(getContext());
                        if (tokenData.isDevelop()) {
                            tokenData.setDevelop(false);
                            tokenData.setVPNToken("");
                        }
                    }
                }
                return true;
            });
        }
    }
}