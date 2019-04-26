package lopez.example.com.provider;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alipay.mobile.h5container.api.H5Page;
import com.alipay.mobile.h5container.api.H5Param;
import com.alipay.mobile.h5container.api.H5Plugin;
import com.alipay.mobile.nebula.util.H5Log;
import com.alipay.mobile.nebula.util.H5StatusBarUtils;
import com.alipay.mobile.nebula.util.H5Utils;
import com.alipay.mobile.nebula.view.H5TitleBarFrameLayout;
import com.alipay.mobile.nebula.view.H5TitleView;
import com.alipay.mobile.nebula.view.IH5TinyPopMenu;

import java.util.ArrayList;
import java.util.List;

import lopez.example.com.launcher.R;

/**
 * Created by omg on 2018/7/23.
 */

public class H5TitleViewImpl implements H5TitleView, View.OnClickListener {

    private static final String TAG = "H5TitleViewImpl";

    private Context mContext;

    private H5TitleBarFrameLayout contentView;

    private String title;

    // 此处定义标题栏的基本控件
    private TextView mTitleView;

    private TextView mSubTitleView;

    private View mCloseButton;

    private View mBackButton;

    private View vDivider;

    private View hDivider;

    private View statusBarAdjustView;

    /**
     * ==== OptionMenu的各种View Start ====
     */
    // OptionMenu的Container
    public View h5NavOptions;
    public View h5NavOptions1;
    public List<View> h5NavOptionsList = new ArrayList<View>();

    // ---- OptionMenu 三种形态 Start ---- //
    // 1. OptionType.MENU(默认) -默认的Option按钮
    public TextView btMenu;
    public TextView btMenu1;
    public List<TextView> btMenuList = new ArrayList<TextView>();

    // 2. OptionType.ICON - 通过setOptionMenu手动设置的icon
    public ImageButton btIcon;
    public ImageButton btIcon1;
    public List<ImageButton> btIconList = new ArrayList<ImageButton>();

    // 3. OptionType.TEXT - 文字
    public TextView btText;
    public TextView btText1;
    public List<TextView> btTextList = new ArrayList<TextView>();
    // ---- OptionMenu 三种形态 Over ---- //

    // Web页面的实例接口类，可用于控制web行为
    private H5Page h5Page;

    private int visibleOptionNum = 0;

    public H5TitleViewImpl(Context context) {
        mContext = context;
        ViewGroup parent = null;
        if (context instanceof Activity) {
            parent = (ViewGroup) ((Activity) mContext).findViewById(android.R.id.content);
        }
        contentView = (H5TitleBarFrameLayout) LayoutInflater.from(context).inflate(R.layout.h5_navigation_bar, parent, false);
        contentView.getContentBgView().setColor(context.getResources().getColor(R.color.h5_default_titlebar_color));
        mTitleView = (TextView) contentView.findViewById(R.id.h5_tv_title);
        mTitleView.setOnClickListener(this);
        mSubTitleView = (TextView) contentView.findViewById(R.id.h5_tv_subtitle);
        mSubTitleView.setOnClickListener(this);
        mCloseButton = contentView.findViewById(R.id.h5_nav_close);
        mCloseButton.setOnClickListener(this);
        mBackButton = contentView.findViewById(R.id.h5_tv_nav_back);
        mBackButton.setOnClickListener(this);
        vDivider = contentView.findViewById(R.id.h5_v_divider);
        hDivider = contentView.findViewById(R.id.h5_h_divider_intitle);
        h5NavOptions = contentView.findViewById(R.id.h5_nav_options);
        h5NavOptions1 = contentView.findViewById(R.id.h5_nav_options1);
        statusBarAdjustView = contentView.findViewById(R.id.h5_status_bar_adjust_view);

        btIcon = (ImageButton) contentView.findViewById(R.id.h5_bt_image);
        btText = (TextView) contentView.findViewById(R.id.h5_bt_text);
        btMenu = (TextView) contentView.findViewById(R.id.h5_bt_options);

        btIcon1 = (ImageButton) contentView.findViewById(R.id.h5_bt_image1);
        btText1 = (TextView) contentView.findViewById(R.id.h5_bt_text1);
        btMenu1 = (TextView) contentView.findViewById(R.id.h5_bt_options1);

        //add view to list
        h5NavOptionsList.add(h5NavOptions);
        h5NavOptionsList.add(h5NavOptions1);

        btIconList.add(btIcon);
        btIconList.add(btIcon1);

        btTextList.add(btText);
        btTextList.add(btText1);

        btMenuList.add(btMenu);
        btMenuList.add(btMenu1);

        btText.setOnClickListener(this);
        btIcon.setOnClickListener(this);
        btText1.setOnClickListener(this);
        btIcon1.setOnClickListener(this);
        btMenu.setOnClickListener(this);
        btMenu1.setOnClickListener(this);

    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public void setTitle(String s) {
        title = s;
        mTitleView.setText(s);
    }

    @Override
    public void setSubTitle(String s) {
        mSubTitleView.setVisibility(View.VISIBLE);
        mSubTitleView.setText(s);
    }

    @Override
    public void setImgTitle(Bitmap bitmap) {

    }

    @Override
    public void setImgTitle(Bitmap bitmap, String s) {

    }

    @Override
    public void showCloseButton(boolean b) {
        mCloseButton.setVisibility(b ? View.VISIBLE : View.GONE);
    }

    @Override
    public View getContentView() {
        return contentView;
    }

    @Override
    public ColorDrawable getContentBgView() {
        return contentView.getContentBgView();
    }

    @Override
    public TextView getMainTitleView() {
        return mTitleView;
    }

    @Override
    public TextView getSubTitleView() {
        return mSubTitleView;
    }

    @Override
    public void showBackButton(boolean b) {
        mBackButton.setVisibility(b ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showOptionMenu(boolean isShow) {
        if (visibleOptionNum == 1) {
            h5NavOptions1.setVisibility(View.GONE);
            h5NavOptions.setVisibility(View.VISIBLE);
        } else if (visibleOptionNum >= 2) {
            h5NavOptions1.setVisibility(View.VISIBLE);
            h5NavOptions.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void setOptionType(H5Param.OptionType optionType) {
        setOptionType(optionType, 0, true);
    }

    @Override
    public void setOptionType(H5Param.OptionType type, int num, boolean byIndex) {
        boolean icon = false;
        boolean text = false;
        boolean menu = false;
        if (type == H5Param.OptionType.ICON) {
            icon = true;
        } else if (type == H5Param.OptionType.TEXT) {
            text = true;
        } else if (type == H5Param.OptionType.MENU) {
            menu = true;
        }
        ctrlbtText(num, text ? View.VISIBLE : View.GONE, byIndex);
        ctrlbtIcon(num, icon ? View.VISIBLE : View.INVISIBLE, byIndex);
        ctrlbtMenu(num, menu ? View.VISIBLE : View.INVISIBLE, byIndex);
    }

    //view visible control
    private boolean isOutOfBound(int num, int length) {
        if (length == 0) {
            return true;
        }
        return length < num;
    }

    private void ctrlbtText(int num, int visible, boolean byIndex) {
        if (isOutOfBound(num, btTextList.size())) {
            return;
        }
        if (byIndex) {
            btTextList.get(num).setVisibility(visible);
        } else {
            for (int i = 0; i < num; i++) {
                btTextList.get(i).setVisibility(visible);
            }
        }
    }

    private void ctrlbtIcon(int num, int visible, boolean byIndex) {
        if (isOutOfBound(num, btIconList.size())) {
            return;
        }
        if (byIndex) {
            btIconList.get(num).setVisibility(visible);
        } else {
            for (int i = 0; i < num; i++) {
                btIconList.get(i).setVisibility(visible);
            }
        }
    }

    private void ctrlbtMenu(int num, int visible, boolean byIndex) {
        if (isOutOfBound(num, btMenuList.size())) {
            return;
        }
        if (byIndex) {
            btMenuList.get(num).setVisibility(visible);
        } else {
            for (int i = 0; i < num; i++) {
                btMenuList.get(i).setVisibility(visible);
            }
        }
    }

    @Override
    public void showTitleLoading(boolean b) {

    }

    @Override
    public void showTitleDisclaimer(boolean b) {

    }

    // 设置右上角按钮图标
    @Override
    public void setBtIcon(Bitmap btIcon, int index) {
        if (isOutOfBound(index, btIconList.size())) {
            return;
        }
        btIconList.get(index).setImageBitmap(btIcon);
    }

    @Override
    public void setH5Page(H5Page h5Page) {
        this.h5Page = h5Page;
    }

    @Override
    public void setOptionMenu(JSONObject params) {
        boolean reset = H5Utils.getBoolean(params, "reset", false);
        boolean override = H5Utils.getBoolean(params, "override", false);
        JSONArray menus = H5Utils.getJSONArray(params, "menus", null);
        if (reset) {
            h5NavOptions1.setVisibility(View.GONE);
            setOptionType(H5Param.OptionType.MENU, 0, true);
            visibleOptionNum = 1;
            return;
        }
        if (menus != null && !menus.isEmpty()) {
            visibleOptionNum = 0;
            if (override) {
                int menuSize = menus.size() > 2 ? 2 : menus.size();
                for (int i = 0; i < menuSize; i++) {
//                    h5NavOptionsList.get(i).setVisibility(View.VISIBLE);
                    JSONObject menusItem = menus.getJSONObject(i);
                    setOptionMenuInternal(menusItem, i);
                    visibleOptionNum++;
                }
            } else {
                visibleOptionNum = 2;
//                h5NavOptionsList.get(1).setVisibility(View.VISIBLE);
                JSONObject menusItem = menus.getJSONObject(0);
                setOptionMenuInternal(menusItem, 1);
            }
        } else {
            setOptionMenuInternal(params, 0);
            visibleOptionNum = 1;
        }
    }

    private void setOptionMenuInternal(JSONObject params, int index) {
        String title = H5Utils.getString(params, "title");
        String icon = H5Utils.getString(params, "icon");
        String icontype = H5Utils.getString(params, "icontype");
        String contentDesc = H5Utils.getString(params, "contentDesc");
        String colorText = H5Utils.getString(params, "color");

        // default white color
        int color = 0xFF108ee9;
        if (!TextUtils.isEmpty(colorText)) {
            try {
                color = Color.parseColor(colorText);
            } catch (Throwable ignore) {
                //can not find logutil
            }
            color = 0xFF000000 | color;
            btTextList.get(index).setTextColor(color);
        } else {
            int currentColor = mTitleView.getCurrentTextColor();
            currentColor = 0xFF000000 | currentColor;
            H5Log.d(TAG, "setOptionMenuInternal currentColor is " + currentColor);
            if (currentColor != 0xFF111111) {
                btText.setTextColor(0xFFFFFFFF);
                btText1.setTextColor(0xFFFFFFFF);
            } else {
                btText.setTextColor(0xFF108ee9);
                btText1.setTextColor(0xFF108ee9);
            }
        }

        if (!TextUtils.isEmpty(title)) {
            title = title.trim();
            btTextList.get(index).setText(title);
            setOptionType(H5Param.OptionType.TEXT, index, true);
            btTextList.get(index).setContentDescription(title);
        } else if (!TextUtils.isEmpty(icon) || !TextUtils.isEmpty(icontype)) {
            if (!TextUtils.isEmpty(contentDesc)) {
                btIconList.get(index).setContentDescription(contentDesc);
            }
        }
    }

    @Override
    public View getDivider() {
        return vDivider;
    }

    @Override
    public View getHdividerInTitle() {
        return hDivider;
    }

    @Override
    public View getPopAnchor() {
        return btMenu;
    }

    @Override
    public void resetTitleColor(int i) {

    }

    @Override
    public void switchToWhiteTheme() {

    }

    @Override
    public void switchToBlueTheme() {

    }

    @Override
    public void releaseViewList() {
        if (h5NavOptionsList != null) {
            h5NavOptionsList.clear();
        }
        if (btIconList != null) {
            btIconList.clear();
        }
        if (btTextList != null) {
            btTextList.clear();
        }
        if (btMenuList != null) {
            btMenuList.clear();
        }
    }

    @Override
    public void openTranslucentStatusBarSupport(int color) {
        if (H5StatusBarUtils.isSupport()) {
            int statusBarHeight = H5StatusBarUtils.getStatusBarHeight(mContext);

            if (statusBarHeight == 0) { //保护，万一有rom没办法拿到状态栏高度的话，则在这里不生效。
                return;
            }
            LinearLayout.LayoutParams layoutParams =
                    (LinearLayout.LayoutParams) statusBarAdjustView.getLayoutParams();
            layoutParams.height = statusBarHeight;
            statusBarAdjustView.setLayoutParams(layoutParams);
            statusBarAdjustView.setVisibility(View.VISIBLE);

            try {
                H5StatusBarUtils.setTransparentColor((Activity) mContext, color);
            } catch (Exception e) {
                H5Log.e(TAG, e);
            }
        }
    }

    @Override
    public void switchToTitleBar() {

    }

    @Override
    public View setTitleBarSearch(Bundle bundle) {
        return null;
    }

    @Override
    public void setBackCloseBtnImage(String s) {

    }

    @Override
    public void setTitleTxtColor(int i) {
        mTitleView.setTextColor(i);
        mSubTitleView.setTextColor(i);
    }

    @Override
    public View getOptionMenuContainer() {
        return h5NavOptions;
    }

    @Override
    public void setIH5TinyPopMenu(IH5TinyPopMenu ih5TinyPopMenu) {

    }

    @Override
    public void setTitleView(View view) {

    }

    @Override
    public void onClick(View view) {
        if (h5Page == null) {
            return ;
        }

        String eventName = null;
        JSONObject data = null;

        if (view == mBackButton) {
            eventName = H5Plugin.CommonEvents.H5_TOOLBAR_BACK; // 发送后退事件
        } else if (view == mCloseButton) {
            eventName = H5Plugin.CommonEvents.H5_TOOLBAR_CLOSE; // 发送关闭页面事件
        }else if (view.equals(btIcon) || view.equals(btText)) {
            eventName = H5Plugin.CommonEvents.H5_TITLEBAR_OPTIONS;
            data = new JSONObject();
            data.put("index", 0);
        } else if (view.equals(btIcon1) || view.equals(btText1)) {
            eventName = H5Plugin.CommonEvents.H5_TITLEBAR_OPTIONS;
            data = new JSONObject();
            data.put("index", 1);
        } else if (view.equals(btMenu) || view.equals(btMenu1)) {
            eventName = H5Plugin.CommonEvents.H5_TITLEBAR_OPTIONS;
            data = new JSONObject();
            data.put("fromMenu", true);
            data.put("index", view.equals(btMenu) ? 0 : 1);
        } else if (view.equals(mTitleView)) {
            eventName = H5Plugin.CommonEvents.H5_TITLEBAR_TITLE;
        } else if (view.equals(mSubTitleView)) {
            eventName = H5Plugin.CommonEvents.H5_TITLEBAR_SUBTITLE;
        }

        if (!TextUtils.isEmpty(eventName)) {
            h5Page.sendEvent(eventName, data);
        }
    }
}
