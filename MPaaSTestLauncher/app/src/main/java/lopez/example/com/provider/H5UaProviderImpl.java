package lopez.example.com.provider;

import com.alipay.mobile.nebula.provider.H5UaProvider;

/**
 * @author Lopez
 * @date 2019/4/26
 * @time 2:08 PM
 */

public class H5UaProviderImpl implements H5UaProvider {

    @Override
    public String getUa(String defaultUaStr) {
        //请不要修改defaultUaStr，或者返回一个不包含defaultUaStr的结果
        return defaultUaStr + " AlipayClient/mPaaS";
    }

}
