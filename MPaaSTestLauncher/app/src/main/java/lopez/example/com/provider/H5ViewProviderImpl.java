package lopez.example.com.provider;

import android.content.Context;
import android.view.ViewGroup;

import com.alipay.mobile.nebula.provider.H5ViewProvider;
import com.alipay.mobile.nebula.view.H5NavMenuView;
import com.alipay.mobile.nebula.view.H5PullHeaderView;
import com.alipay.mobile.nebula.view.H5TitleView;
import com.alipay.mobile.nebula.view.H5WebContentView;

/**
 * @author Lopez
 * @date 2019/4/26
 * @time 2:12 PM
 */

public class H5ViewProviderImpl implements H5ViewProvider {

    @Override
    public H5TitleView createTitleView(Context context) {
        // 此处返回您自定义的 WebView 的承载布局，若返回 null 则使用默认 View
        return new H5TitleViewImpl(context);
    }

    @Override
    public H5NavMenuView createNavMenu() {
        // 此处返回您自定义的标题栏，若返回 null 则使用默认 View
        return null;
    }

    @Override
    public H5PullHeaderView createPullHeaderView(Context context, ViewGroup viewGroup) {
        // 此处返回您自定义的下拉刷新头部，若返回 null 则使用默认 View
        return null;
    }

    @Override
    public H5WebContentView createWebContentView(Context context) {
        // 此处返回您自定义的导航菜单，若返回 null 则使用默认 View
        return null;
    }

}
