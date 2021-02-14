package top.yvyan.guettable.moreFun;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import top.yvyan.guettable.R;
import top.yvyan.guettable.service.table.IMoreFun;
import top.yvyan.guettable.service.table.MoreFunService;

public abstract class BaseFuncActivity extends AppCompatActivity implements IMoreFun {

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.back)
    ImageView back;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.state)
    TextView state;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.title)
    TextView title;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.more)
    ImageView more;

    protected boolean update = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_func);
        ButterKnife.bind(this);

        back.setOnClickListener(this::onBackClick);
        more.setVisibility(View.GONE);
        more.setOnClickListener(this::showPopMenu);

        init();
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
        state.setText(hint);
        if (stateNum == 5 && update) {
            showContent();
        }
    }

    /**
     * 子类init (不包含设置内容视图)
     */
    protected abstract void childInit();

    protected void onBackClick(View v) {
        finish();
    }

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
}