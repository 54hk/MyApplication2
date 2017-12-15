package haiban.haiqi.com.video.voide;

import android.os.Handler;

import com.netease.nimlib.sdk.avchat.model.AVChatData;


import haiban.haiqi.com.video.yunxin.DemoCache;

/**
 * Created by 54hk on 2017/12/6.
 */

public class AVChatProfile {

    private final String TAG = "AVChatProfile";

    private boolean isAVChatting = false; // 是否正在音视频通话

    public static AVChatProfile getInstance() {
        return InstanceHolder.instance;
    }

    public boolean isAVChatting() {
        return isAVChatting;
    }

    public void setAVChatting(boolean chating) {
        isAVChatting = chating;
    }

    private static class InstanceHolder {
        public final static AVChatProfile instance = new AVChatProfile();
    }

    Handler handler = new Handler();
    public void launchActivity(final AVChatData data, final int source) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                // 启动，如果 task正在启动，则稍等一下
                if (!DemoCache.isMainTaskLaunching()) {
//                    AVChatActivity.launch(DemoCache.getContext(), data, source);
                } else {
                    launchActivity(data, source);
                }
            }
        };

        handler.postDelayed(runnable, 200);
    }
}
