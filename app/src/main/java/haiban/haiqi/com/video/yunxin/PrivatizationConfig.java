package haiban.haiqi.com.video.yunxin;

import com.netease.nimlib.sdk.ServerAddresses;

/**
 * Created by 54hk on 2017/12/6.
 */

public class PrivatizationConfig {

    static ServerAddresses getServerAddresses() {
        return null;
    }

    static String getAppKey() {
        return null;
    }

    private static ServerAddresses get() {
        ServerAddresses addresses = new ServerAddresses();
        addresses.nosDownload = "nos.netease.com";
        addresses.nosAccess = "{bucket}.nosdn.127.net/{object}";
        return addresses;
    }
}
