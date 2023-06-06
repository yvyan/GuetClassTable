package top.yvyan.guettable.moreFun;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import top.yvyan.guettable.R;
import top.yvyan.guettable.data.SingleSettingData;
import top.yvyan.guettable.service.IMoreFun;
import top.yvyan.guettable.service.MoreFunService;
import top.yvyan.guettable.util.BackgroundUtil;

public abstract class BaseFuncActivity extends AppCompatActivity implements IMoreFun {

    @SuppressLint("NonConstantResourceId")

    protected boolean update = true;
    protected int stateNum = -1;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.state)
    TextView state;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.title)
    TextView title;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.more)
    ImageView more;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.func_base_constraintLayout)
    ConstraintLayout header;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SingleSettingData singleSettingData = SingleSettingData.newInstance(getApplicationContext());
        BackgroundUtil.setPageTheme(this, singleSettingData.getThemeId());
        setContentView(R.layout.activity_base_func);
        ButterKnife.bind(this);
        more.setVisibility(View.GONE);
        more.setOnClickListener(this::showPopMenu);
        state.setOnClickListener(this::stateOnClick);
        init();
        header.getBackground().setAlpha(255);
        BackgroundUtil.setFullAlphaStatus(this);
    }

    private void init() {

        childInit();
        MoreFunService moreFunService = new MoreFunService(this, this);
        showContent();
        moreFunService.update();
    }

    /**
     * 设置内容视图
     *
     * @param layoutResId 内容视图
     */
    protected void baseSetContentView(int layoutResId) {
        LinearLayout llContent = findViewById(R.id.content);
        llContent.removeAllViews();
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(layoutResId, null);
        llContent.addView(v);
    }

    /**
     * 设置是否显示更多按钮
     *
     * @param isShow 是否
     */
    protected void setShowMore(boolean isShow) {
        if (isShow) {
            more.setVisibility(View.VISIBLE);
        } else {
            more.setVisibility(View.GONE);
        }
    }

    /**
     * 调用此方法：内容视图只有在数据同步成功后发生变化时才刷新
     */
    protected void openUpdate() {
        update = false;
    }

    /**
     * 设置标题
     *
     * @param titleText 标题
     */
    protected void setTitle(String titleText) {
        title.setText(titleText);
    }

    /**
     * 设置空白页面
     */
    protected void showEmptyPage() {
        baseSetContentView(R.layout.more_func_empty);
    }

    @Override
    public void updateView(String hint, int stateNum) {
        this.stateNum = stateNum;
        state.setText(hint);
        if (stateNum == 5 && update) {
            showContent();
        }
    }

    protected void stateOnClick(View view) {
        if (this.stateNum != 5) {
            if (this.stateNum == -3) {
                this.init(); // 调用init方法重新获取验证码
            }
        }
    }

    /**
     * 子类init (不包含设置内容视图)
     */
    protected abstract void childInit();

    /**
     * 显示菜单
     *
     * @param v view
     */
    protected void showPopMenu(View v) {
    }

    /**
     * 显示内容 (包含设置内容视图)
     */
    protected abstract void showContent();

    public void doBack(View view) {
        finish();
    }

}