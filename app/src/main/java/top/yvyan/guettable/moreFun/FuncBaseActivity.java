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

public abstract class FuncBaseActivity extends AppCompatActivity implements IMoreFun {

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.icon_back)
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
        setContentView(R.layout.activity_func_base);
        ButterKnife.bind(this);

        back.setOnClickListener(this::onBackClick);
        more.setVisibility(View.GONE);
        more.setOnClickListener(this::showPopMenu);

        init();

        updateView();
        MoreFunService moreFunService = new MoreFunService(this, this);
        moreFunService.update();
    }

    /**
     * 设置内容视图
     * @param layoutResId 内容视图
     */
    public void baseSetContentView(int layoutResId) {
        LinearLayout llContent = findViewById(R.id.content);
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
     * @param titleText 标题
     */
    protected void setTitle(String titleText) {
        title.setText(titleText);
    }

    @Override
    public void updateView(String hint, int stateNum) {
        state.setText(hint);
        if (stateNum == 5 && update) {
            updateView();
        }
    }

    protected abstract void init();

    protected void onBackClick(View v) {
        finish();
    }

    protected void showPopMenu(View v) {}

    protected abstract void updateView();
}