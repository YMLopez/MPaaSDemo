package lopez.example.com.launcher;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.mobile.antui.basic.AUToast;
import com.alipay.mobile.h5container.api.H5Page;
import com.alipay.mobile.h5container.api.H5PageReadyListener;
import com.alipay.mobile.h5container.api.H5Param;
import com.alipay.mobile.nebula.appcenter.download.H5DownloadRequest;
import com.alipay.mobile.nebula.appcenter.model.AppInfo;
import com.alipay.mobile.nebula.provider.H5AppProvider;
import com.alipay.mobile.nebula.util.H5Utils;
import com.mpaas.nebula.adapter.api.MPNebula;
import com.mpaas.nebula.adapter.api.MpaasNebulaDownloadCallback;
import com.mpaas.nebula.adapter.api.MpaasNebulaInstallCallback;
import com.mpaas.nebula.adapter.api.MpaasNebulaUpdateCallback;


/**
 * Created by mPaaS on 16/9/28.
 */
public class MainActivity extends Activity implements View.OnClickListener {

    Button btn_open_h5;
    Button btn_header_custom;
    Button btn_pre_load;
    Button btn_download;
    Button btn_update;
    Button btn_install;
    Button btn_get_data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(lopez.example.com.launcher.R.layout.main);

        btn_open_h5 = findViewById(R.id.btn_open_h5);
        btn_header_custom = findViewById(R.id.btn_header_custom);
        btn_pre_load = findViewById(R.id.btn_pre_load);
        btn_download = findViewById(R.id.btn_download);
        btn_update = findViewById(R.id.btn_update);
        btn_install = findViewById(R.id.btn_install);
        btn_get_data = findViewById(R.id.btn_get_data);

        btn_open_h5.setOnClickListener(this);
        btn_header_custom.setOnClickListener(this);
        btn_pre_load.setOnClickListener(this);
        btn_download.setOnClickListener(this);
        btn_update.setOnClickListener(this);
        btn_install.setOnClickListener(this);
        btn_get_data.setOnClickListener(this);

        //更新H5应用
        MPNebula.updateAllApp(new MpaasNebulaUpdateCallback() {
            @Override
            public void onResult(final boolean success, final boolean isLimit) {
                super.onResult(success, isLimit);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        AUToast.makeToast(MainActivity.this,
                                success ? R.string.update_success : R.string.update_failure, 2000).show();
                    }
                });
            }
        });

        //校验安全签名 第一次打开离线包前调用此接口
        //MPNebula.enableAppVerification("publicKey");

        //获得H5的视图
        //getH5View();
    }

    private void getH5View() {
        //将单个容器的视图嵌入到页面
        Bundle param = new Bundle();
        param.putString("url", "https://www.baidu.com/");
        //同步的方法 获得目标View
        View h5View = MPNebula.getH5View(MainActivity.this, param);

        Bundle bundle = new Bundle();
        bundle.putString("url", "https://fanyi.baidu.com/");
        //异步的
        MPNebula.getH5ViewAsync(MainActivity.this, bundle, new H5PageReadyListener() {
            @Override
            public void getH5Page(H5Page h5Page) {
                String url = h5Page.getUrl();
                Log.d("==w", "getH5Page: " + url);
                //获取的目标View
                View target = h5Page.getContentView();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_open_h5:
                //Toast.makeText(this, "打开离线包", Toast.LENGTH_SHORT).show();
                //启动离线包
                Bundle bundle = new Bundle();
                bundle.putString(H5Param.LONG_URL, "/ynet_android/test/index.html");
                MPNebula.startApp("20001010", bundle);
                break;
            case R.id.btn_header_custom:
                Toast.makeText(this, "自定义H5网页头部\n先测试启动一个在线页面", Toast.LENGTH_SHORT).show();

                MPNebula.startUrl("https://www.baidu.com/");
                break;
            case R.id.btn_pre_load:
                Toast.makeText(this, "预加载", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_download:
                //Toast.makeText(this, "下载H5应用", Toast.LENGTH_SHORT).show();

                h5Download();
                break;
            case R.id.btn_update:
                //Toast.makeText(this, "更新H5应用", Toast.LENGTH_SHORT).show();

                h5Update();
                break;
            case R.id.btn_install:
                //Toast.makeText(this, "安装H5应用", Toast.LENGTH_SHORT).show();

                h5Install();
                break;
            case R.id.btn_get_data:
                //Toast.makeText(this, "获取H5应用信息", Toast.LENGTH_SHORT).show();

                h5GetData();
                break;
        }
    }

    private void h5GetData() {
        H5AppProvider provider = H5Utils.getProvider(H5AppProvider.class.getName());
        AppInfo appInfo = provider.getAppInfo("20001010"); //获取应用配置
        boolean isInstalled = provider.isInstalled("20001010", "1.0.0.0"); //某版本应用是否已经安装
        boolean isAvailable = provider.isAvailable("20001010", "1.0.0.0");//某版本应用离线包是否已经下载完成
        Toast.makeText(MainActivity.this, "appInfo: " + appInfo + "\nisInstalled: " + isInstalled + "\nisAvailable: " + isAvailable, Toast.LENGTH_SHORT).show();
    }

    private void h5Download() {
        MPNebula.downloadApp("20001010", new MpaasNebulaDownloadCallback() {
            @Override
            public void onFinish(@Nullable H5DownloadRequest h5DownloadRequest, String savePath) {
                super.onFinish(h5DownloadRequest, savePath);
                Toast.makeText(MainActivity.this, "下载H5应用成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailed(@Nullable H5DownloadRequest h5DownloadRequest, int errorCode, String errorMsg) {
                super.onFailed(h5DownloadRequest, errorCode, errorMsg);
                Toast.makeText(MainActivity.this, "下载H5应用失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void h5Update() {
        //更新H5应用
        MPNebula.updateAllApp(new MpaasNebulaUpdateCallback() {
            @Override
            public void onResult(final boolean success, final boolean isLimit) {
                super.onResult(success, isLimit);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        AUToast.makeToast(MainActivity.this,
                                success ? R.string.update_success : R.string.update_failure, 2000).show();
                    }
                });
            }
        });
    }

    private void h5Install() {
        MPNebula.installApp("20001010", new MpaasNebulaInstallCallback() {
            @Override
            public void onResult(boolean success, boolean isPatch) {
                super.onResult(success, isPatch);
                Toast.makeText(MainActivity.this, "安装H5应用成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAppHasInstalled(String appId, String version) {
                super.onAppHasInstalled(appId, version);
                Toast.makeText(MainActivity.this, "H5 APP已经安装了", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
