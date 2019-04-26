package lopez.example.com.launcher;

import android.app.Application;

import com.alipay.mobile.framework.LauncherApplicationAgent;
import com.alipay.mobile.nebula.provider.H5AppCenterPresetProvider;
import com.alipay.mobile.nebula.provider.H5ReplaceResourceProvider;
import com.alipay.mobile.nebula.util.H5Utils;
import com.mpaas.nebula.adapter.api.MPNebula;
import com.mpaas.nebula.adapter.api.MPNebulaOfflineInfo;

import lopez.example.com.provider.H5AppCenterPresetProviderImpl;
import lopez.example.com.provider.H5UaProviderImpl;
import lopez.example.com.provider.H5ViewProviderImpl;

/**
 * Created by seker on 16/2/18.
 */
public class MockLauncherApplicationAgent extends LauncherApplicationAgent {

    public MockLauncherApplicationAgent(Application context, Object bundleContext) {
        super(context, bundleContext);
    }

    @Override
    public void preInit() {
        super.preInit();
    }

    @Override
    public void postInit() {
        super.postInit();
        //加载普通离线包
        MPNebula.loadOfflineNebula("h5_json.json", new MPNebulaOfflineInfo("20001010_1.0.0.0.amr", "20001010", "1.0.0.0"));

        //加载全局离线包
        H5Utils.getH5ProviderManager().setProvider(H5AppCenterPresetProvider.class.getName(), new H5AppCenterPresetProviderImpl());

        //设置自定义UserAgent
        MPNebula.setUa(new H5UaProviderImpl());

        //若您要设置自定义标题栏，请先设置自己工程的 bundle name，否则可能会导致资源找不到
        H5Utils.setProvider(H5ReplaceResourceProvider.class.getName(), new H5ReplaceResourceProvider() {
            @Override
            public String getReplaceResourcesBundleName() {
                return lopez.example.com.launcher.BuildConfig.BUNDLE_NAME;
            }
        });

        //设置自定义容器View
        MPNebula.setCustomViewProvider(new H5ViewProviderImpl());
    }

}
