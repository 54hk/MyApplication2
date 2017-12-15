package haiban.haiqi.com.video.yunxin;

import android.app.Notification;
import android.content.Context;
import android.util.SparseArray;

import com.netease.nimlib.sdk.StatusBarNotificationConfig;

/**
 * Created by 54hk on 2017/12/6.
 */

public class DemoCache {

    private static Context context;

    private static String account;

    private static StatusBarNotificationConfig notificationConfig;

    private static SparseArray<Notification> notifications = new SparseArray<>();

    public static SparseArray<Notification> getNotifications() {
        return notifications;
    }

    public static void clear() {
        account = null;
    }

    public static String getAccount() {
        return account;
    }

    private static boolean mainTaskLaunching;

    public static void setAccount(String account) {
        DemoCache.account = account;
//        NimUIKit.setAccount(account);
    }

    public static void setNotificationConfig(StatusBarNotificationConfig notificationConfig) {
        DemoCache.notificationConfig = notificationConfig;
    }

    public static StatusBarNotificationConfig getNotificationConfig() {
        return notificationConfig;
    }

    public static Context getContext() {
        return context;
    }

    public static void setContext(Context context) {
        DemoCache.context = context.getApplicationContext();
    }

    public static void setMainTaskLaunching(boolean mainTaskLaunching) {
        DemoCache.mainTaskLaunching = mainTaskLaunching;
    }

    public static boolean isMainTaskLaunching() {
        return mainTaskLaunching;
    }
}
