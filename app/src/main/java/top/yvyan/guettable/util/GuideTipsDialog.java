package top.yvyan.guettable.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.xuexiang.xui.widget.dialog.BaseDialog;
import com.xuexiang.xutil.common.ObjectUtils;
import com.xuexiang.xutil.net.type.TypeBuilder;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;
import com.zzhoujay.richtext.RichText;

import java.lang.reflect.Type;
import java.util.List;

import okhttp3.Call;
import top.yvyan.guettable.Http.entity.ApiResult;
import top.yvyan.guettable.R;
import top.yvyan.guettable.bean.TipInfo;

public class GuideTipsDialog extends BaseDialog implements View.OnClickListener {

    private List<TipInfo> mTips;
    private int mIndex = -1;

    private TextView mTvPrevious;
    private TextView mTvNext;

    private TextView mTvTitle;
    private TextView mTvContent;

    /**
     * 显示提示
     *
     * @param activity 上下文
     * @param url      URL
     */
    public static void showTips(final Activity activity, String url) {
        showTipsForce(activity, url);
    }

    /**
     * 强制显示提示
     *
     * @param context 上下文
     * @param url     URL
     */
    public static void showTipsForce(Context context, String url) {
        OkHttpUtils.get()
                .url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        showTips(context, response);
                    }
                });
    }


    private static void showTips(Context context, String response) {
        Type type = TypeBuilder.newInstance(ApiResult.class)
                .beginSubType(List.class)
                .addTypeParam(TipInfo.class)
                .endSubType()
                .build();

        ApiResult<List<TipInfo>> apiResult = new Gson().fromJson(response, type);
        if (apiResult != null) {
            List<TipInfo> tips = apiResult.getData();
            if (ObjectUtils.isNotEmpty(tips)) {
                new GuideTipsDialog(context, tips).show();
            }
        }
    }

    public GuideTipsDialog(Context context, @NonNull List<TipInfo> tips) {
        super(context, R.layout.dialog_guide);
        initViews();
        updateTips(tips);
    }

    /**
     * 初始化弹窗
     */
    private void initViews() {
        mTvTitle = findViewById(R.id.tv_title);
        mTvContent = findViewById(R.id.tv_content);
        ImageView ivClose = findViewById(R.id.iv_close);

        mTvPrevious = findViewById(R.id.tv_previous);
        mTvNext = findViewById(R.id.tv_next);

        if (ivClose != null) {
            ivClose.setOnClickListener(this);
        }
        mTvPrevious.setOnClickListener(this);
        mTvNext.setOnClickListener(this);
        mTvPrevious.setEnabled(false);
        mTvNext.setEnabled(true);
        setCancelable(false);
        setCanceledOnTouchOutside(true);
    }

    /**
     * 更新提示信息
     *
     * @param tips 提示信息
     */
    private void updateTips(List<TipInfo> tips) {
        mTips = tips;
        if (mTips != null && mTips.size() > 0 && mTvContent != null) {
            mIndex = 0;
            showRichText(mTips.get(mIndex));
        }
    }

    /**
     * 切换提示信息
     *
     * @param index 索引
     */
    private void switchTipInfo(int index) {
        if (mTips != null && mTips.size() > 0 && mTvContent != null) {
            if (index >= 0 && index <= mTips.size() - 1) {
                showRichText(mTips.get(index));
                if (index == 0) {
                    mTvPrevious.setEnabled(false);
                    mTvNext.setEnabled(true);
                } else if (index == mTips.size() - 1) {
                    mTvPrevious.setEnabled(true);
                    mTvNext.setEnabled(false);
                } else {
                    mTvPrevious.setEnabled(true);
                    mTvNext.setEnabled(true);
                }
            }
        }
    }

    /**
     * 显示富文本
     *
     * @param tipInfo 提示信息
     */
    private void showRichText(TipInfo tipInfo) {
        mTvTitle.setText(tipInfo.getTitle());
        RichText.fromHtml(tipInfo.getContent())
                .bind(this)
                .into(mTvContent);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_close:
                dismiss();
                break;
            case R.id.tv_previous:
                if (mIndex > 0) {
                    mIndex--;
                    switchTipInfo(mIndex);
                }
                break;
            case R.id.tv_next:
                if (mIndex < mTips.size() - 1) {
                    mIndex++;
                    switchTipInfo(mIndex);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onDetachedFromWindow() {
    }
}
