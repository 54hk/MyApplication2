package haiban.haiqi.com.video;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.auth.AuthService;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.netease.nimlib.sdk.avchat.AVChatCallback;
import com.netease.nimlib.sdk.avchat.AVChatManager;
import com.netease.nimlib.sdk.avchat.AVChatStateObserverLite;
import com.netease.nimlib.sdk.avchat.constant.AVChatEventType;
import com.netease.nimlib.sdk.avchat.constant.AVChatMediaCodecMode;
import com.netease.nimlib.sdk.avchat.constant.AVChatType;
import com.netease.nimlib.sdk.avchat.constant.AVChatVideoQuality;
import com.netease.nimlib.sdk.avchat.constant.AVChatVideoScalingType;
import com.netease.nimlib.sdk.avchat.model.AVChatAudioFrame;
import com.netease.nimlib.sdk.avchat.model.AVChatCalleeAckEvent;
import com.netease.nimlib.sdk.avchat.model.AVChatCameraCapturer;
import com.netease.nimlib.sdk.avchat.model.AVChatData;
import com.netease.nimlib.sdk.avchat.model.AVChatNetworkStats;
import com.netease.nimlib.sdk.avchat.model.AVChatNotifyOption;
import com.netease.nimlib.sdk.avchat.model.AVChatParameters;
import com.netease.nimlib.sdk.avchat.model.AVChatSessionStats;
import com.netease.nimlib.sdk.avchat.model.AVChatSurfaceViewRenderer;
import com.netease.nimlib.sdk.avchat.model.AVChatVideoCapturerFactory;
import com.netease.nimlib.sdk.avchat.model.AVChatVideoFrame;

import java.util.Map;

import haiban.haiqi.com.video.voide.AVChatSurface;
import haiban.haiqi.com.video.voide.AVChatVideo;

/**
 * 1020025  1020723
 */
public class MainActivity extends AppCompatActivity implements AVChatSurface.TouchZoneCallback ,AVChatStateObserverLite {

    Button login, outLogin, huaTou;
    private final String[] BASIC_PERMISSIONS = new String[]{android.Manifest.permission.CAMERA,};
    private AVChatCameraCapturer mVideoCapturer;
    private AVChatSurfaceViewRenderer textureViewRenderer;
    private Boolean isBoolean = false;
    private AVChatSurface avChatSurface;
    private AVChatVideo avChatVideo;
    //render
    private AVChatSurfaceViewRenderer smallRender;
    private AVChatSurfaceViewRenderer largeRender;
    // data
    private String largeAccount; // 显示在大图像的用户id
    private String smallAccount; // 显示在小图像的用户id
    private RequestCallback<LoginInfo> callback =
            new RequestCallback<LoginInfo>() {
                @Override
                public void onSuccess(LoginInfo param) {
//                        TODO 登录成功 存储信息 保证下次自动登录 1020025  1020723

                    Toast.makeText(MainActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                    isBoolean = true;
                }

                @Override
                public void onFailed(int code) {
                    Toast.makeText(MainActivity.this, "登录成功错误吗" + code, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onException(Throwable exception) {
                    Toast.makeText(MainActivity.this, "登录成功exception" + exception.getMessage(), Toast.LENGTH_SHORT).show();
                }
                // 可以在此保存LoginInfo到本地，下次启动APP做自动登录用
            };
    // view
    private LinearLayout largeSizePreviewLayout;
    private FrameLayout smallSizePreviewFrameLayout;
    private LinearLayout smallSizePreviewLayout;
    private ImageView smallSizePreviewCoverImg;//stands for peer or local close camera
    private View largeSizePreviewCoverLayout;//stands for peer or local close camera
    private View touchLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initID();
        initLinstener();
//        登录
        doLogin();
//        被叫
        registerAVChatIncomingCallObserver(true);

        this.smallRender = new AVChatSurfaceViewRenderer(this);
        this.largeRender = new AVChatSurfaceViewRenderer(this);

      /*  avChatSurface = new AVChatSurface(this
                , touchLayout
                , smallSizePreviewFrameLayout
                , smallSizePreviewLayout
                , smallSizePreviewCoverImg
                , largeSizePreviewLayout
                , largeSizePreviewCoverLayout
                , this);*/
    }

    private void initID() {
        login = (Button) findViewById(R.id.login);
        outLogin = (Button) findViewById(R.id.out_login);
        huaTou = (Button) findViewById(R.id.hua_tou);
        touchLayout = findViewById(R.id.touch_zone);
        smallSizePreviewFrameLayout = (FrameLayout) findViewById(R.id.small_size_preview_layout);
        smallSizePreviewFrameLayout.setOnTouchListener(smallPreviewTouchListener);
        smallSizePreviewLayout = (LinearLayout) findViewById(R.id.small_size_preview);
        smallSizePreviewCoverImg = (ImageView) findViewById(R.id.smallSizePreviewCoverImg);
        largeSizePreviewLayout = (LinearLayout) findViewById(R.id.large_size_preview);
        largeSizePreviewCoverLayout = findViewById(R.id.notificationLayout);
    }


    private void initLinstener() {
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isBoolean) {
//                    MZ  1020025      小米  1020723
                    outGoingCalling("1020723", AVChatType.VIDEO);
                }
            }
        });
        outLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NIMClient.getService(AuthService.class).logout();
            }
        });
        huaTou.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    /**
     * 拨打音视频
     * 用户名                          类型 ： 语音 / 视频
     * String account, final AVChatType callTypeEnum
     * utGoingCalling(String account, final AVChatType callTypeEnum)
     */
    public void outGoingCalling(String account, final AVChatType callTypeEnum) {
        AVChatNotifyOption notifyOption = new AVChatNotifyOption();
        //附加字段
        notifyOption.extendMessage = "extra_data";
        //是否兼容WebRTC模式
//        notifyOption.webRTCCompat = webrtcCompat;
        //默认forceKeepCalling为true，开发者如果不需要离线持续呼叫功能可以将forceKeepCalling设为false
        notifyOption.forceKeepCalling = false;

//        this.callingState = (callTypeEnum == AVChatType.VIDEO ? CallStateEnum.VIDEO : CallStateEnum.AUDIO);

        //视频通话
        if (callTypeEnum == AVChatType.VIDEO) {
            //打开Rtc模块
            AVChatManager.getInstance().enableRtc();

            //创建视频采集模块并且设置到系统中
            if (mVideoCapturer == null) {
                mVideoCapturer = AVChatVideoCapturerFactory.createCameraCapturer();
                AVChatManager.getInstance().setupVideoCapturer(mVideoCapturer);
            }
            mVideoCapturer.setFocus();
            mVideoCapturer.setAutoFocus(true);
//        mVideoCapturer.switchCamera(); // 切换摄像头
            mVideoCapturer.setFlash(false);

            //设置自己需要的可选参数
            AVChatParameters avChatParameters = new AVChatParameters();
            avChatParameters.setBoolean(AVChatParameters.KEY_AUDIO_CALL_PROXIMITY, false);
            //        设置分辨率
            avChatParameters.setInteger(AVChatParameters.KEY_VIDEO_QUALITY, AVChatVideoQuality.QUALITY_480P);
            // 默认是用前摄像头
            avChatParameters.setBoolean(AVChatParameters.KEY_VIDEO_DEFAULT_FRONT_CAMERA, true);
            //* 视频硬件编码模式   KEY_VIDEO_MAX_BITRATE
            avChatParameters.setString(AVChatParameters.KEY_VIDEO_ENCODER_MODE, AVChatMediaCodecMode.MEDIA_CODEC_AUTO);
//        设置最大码率
            avChatParameters.setInteger(AVChatParameters.KEY_VIDEO_MAX_BITRATE, 2 * 1024);
            avChatParameters.setBoolean(AVChatParameters.KEY_VIDEO_LOCAL_PREVIEW_MIRROR, true);
            AVChatManager.getInstance().setParameters(avChatParameters);

            //开始视频预览
            //打开视频模块
            AVChatManager.getInstance().enableVideo();
            AVChatManager.getInstance().startVideoPreview();
        }

        //呼叫
        AVChatManager.getInstance().call2(account, callTypeEnum, notifyOption, new AVChatCallback<AVChatData>() {
            @Override
            public void onSuccess(AVChatData data) {

                if (callTypeEnum == AVChatType.VIDEO) {
//                    提示摄像头权限

                  /*  List<String> deniedPermissions = BaseMPermission.getDeniedPermissions(MainActivity.this, BASIC_PERMISSIONS);
                    if (deniedPermissions != null && !deniedPermissions.isEmpty()) {
                        avChatVideo.showNoneCameraPermissionView(true);
                        return;
                    }
*/
                    //                启动视频
//                    avChatSurface.initLargeSurfaceView(String.valueOf(data.getChatId()));
//                    avChatSurface.initSmallSurfaceView(data.getAccount());
                    initSmallSurfaceView(data.getAccount());
                    //发起会话成功
                    Toast.makeText(MainActivity.this, "拨打视频成功", Toast.LENGTH_SHORT).show();
                    CalleeAck();
                }
//                avChatData = data;
            }

            @Override
            public void onFailed(int code) {
                Toast.makeText(MainActivity.this, "拨打视频失败", Toast.LENGTH_SHORT).show();
//                closeRtc();
//                closeSessions(-1);
            }

            @Override
            public void onException(Throwable exception) {
                Toast.makeText(MainActivity.this, "拨打视频exception", Toast.LENGTH_SHORT).show();
//                closeRtc();
//                closeSessions(-1);
            }
        });
    }

    /**
     * 被叫收到的回调
     */
    private void registerAVChatIncomingCallObserver(boolean register) {
//        通话的基本 信息
        AVChatManager.getInstance().observeIncomingCall(new Observer<AVChatData>() {
            @Override
            public void onEvent(final AVChatData data) {
                String extra = data.getExtra();
//                拒绝请求
//                AVChatManager.getInstance().hangUp2(data.getChatId(), null);

//                这是接受 请求过来的     receiveInComingCall(data);
                receiveInComingCall(data);
//                 当前来电。 如果选择继续原来的通话，挂断当前来电，最好能够先发送一个正忙的指令给对方
//                AVChatManager.getInstance().sendControlCommand(data.getChatId(), AVChatControlCommand.BUSY, null);

                // 有网络来电打开AVChatActivity
//                AVChatProfile.getInstance().setAVChatting(true);
//                AVChatActivity.launch(DemoCache.getContext(), data, AVChatActivity.FROM_BROADCASTRECEIVER);
            }
        }, register);
    }

    /**
     * 被叫响应通话请求 接听来电
     */
    private void receiveInComingCall(AVChatData avChatData) {
        //接听，告知服务器，以便通知其他端

        AVChatManager.getInstance().enableRtc();
        if (mVideoCapturer == null) {
            mVideoCapturer = AVChatVideoCapturerFactory.createCameraCapturer();
            AVChatManager.getInstance().setupVideoCapturer(mVideoCapturer);
        }

//        if (callingState == CallStateEnum.VIDEO_CONNECTING) {  AVChatType.VIDEO
//            判断视频链接
        AVChatManager.getInstance().enableVideo();
        AVChatManager.getInstance().startVideoPreview();
//        }

        AVChatManager.getInstance().accept2(avChatData.getChatId(), new AVChatCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(MainActivity.this, "被叫接听接受成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailed(int code) {
                if (code == -1) {
                    Toast.makeText(MainActivity.this, "被叫接听本地音视频启动失败", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "被叫接听建立连接失败", Toast.LENGTH_SHORT).show();
                }

                handleAcceptFailed();
            }

            @Override
            public void onException(Throwable exception) {
                handleAcceptFailed();
            }
        });
    }

    private void handleAcceptFailed() {
        AVChatManager.getInstance().stopVideoPreview();
        AVChatManager.getInstance().disableRtc();
    }

    //    主叫收到被叫响应回调
    private Observer<AVChatCalleeAckEvent> callAckObserver = new Observer<AVChatCalleeAckEvent>() {
        @Override
        public void onEvent(AVChatCalleeAckEvent ackInfo) {
            if (ackInfo.getEvent() == AVChatEventType.CALLEE_ACK_BUSY) {
                // 对方正在忙
                Toast.makeText(MainActivity.this, "主叫相应对方正在忙", Toast.LENGTH_SHORT).show();
            } else if (ackInfo.getEvent() == AVChatEventType.CALLEE_ACK_REJECT) {
                // 对方拒绝接听
                Toast.makeText(MainActivity.this, "主叫相应对方拒绝接听", Toast.LENGTH_SHORT).show();
            } else if (ackInfo.getEvent() == AVChatEventType.CALLEE_ACK_AGREE) {
                // 对方同意接听
                Toast.makeText(MainActivity.this, "主叫相应对方同意接听", Toast.LENGTH_SHORT).show();

            }
        }
    };

    private void CalleeAck() {
        /**
         * Observer<AVChatCalleeAckEvent> observer, boolean register
         * 是否注册观察者，注册为 true， 注销为 false
         */
        AVChatManager.getInstance().observeCalleeAckNotification(callAckObserver, true);
    }

    @Override
    public void onTouch() {

    }

//    网易云 视频聊天登录

    /**
     * 主叫
     */
    public void doLogin() {
//       这两个都用的 UserID
//       MZ  1020025      小米  1020723
        LoginInfo info = new LoginInfo("1020025", "1020025"); // config...
        NIMClient.getService(AuthService.class).login(info).setCallback(callback);
    }


    /**
     * surface
     */

    /**
     * 大图像surfaceview 初始化
     *
     * @param account 显示视频的用户id
     */
    public void initLargeSurfaceView(String account) {
//        largeAccount = account;

        /**
         * 设置画布，加入到自己的布局中，用于呈现视频图像
         * account 要显示视频的用户帐号
         */
//        MZ  1020025      小米  1020723
        if ("1020723".equals(account)) {
            AVChatManager.getInstance().setupLocalVideoRender(largeRender, false, AVChatVideoScalingType.SCALE_ASPECT_BALANCED);
        } else {
            AVChatManager.getInstance().setupRemoteVideoRender(account, largeRender, false, AVChatVideoScalingType.SCALE_ASPECT_BALANCED);
        }
        addIntoLargeSizePreviewLayout(largeRender);

    }

    /**
     * 小图像surfaceview 初始化
     * @param account
     * @return
     */
    public void initSmallSurfaceView(String account) {
        smallAccount = account;
        smallSizePreviewFrameLayout.setVisibility(View.VISIBLE);
        /**
         * 设置画布，加入到自己的布局中，用于呈现视频图像
         * account 要显示视频的用户帐号 MZ  1020025      小米  1020723
         */
        if ("1020025".equals(account)) {
            AVChatManager.getInstance().setupLocalVideoRender(smallRender, false, AVChatVideoScalingType.SCALE_ASPECT_BALANCED);
        } else {
            AVChatManager.getInstance().setupRemoteVideoRender(account, smallRender, false, AVChatVideoScalingType.SCALE_ASPECT_BALANCED);
        }
        addIntoSmallSizePreviewLayout(smallRender);
        smallSizePreviewFrameLayout.bringToFront();
    }


    /**
     * 添加surfaceview到largeSizePreviewLayout
     * @param surfaceView
     */
    private void addIntoLargeSizePreviewLayout(SurfaceView surfaceView) {
        if (surfaceView.getParent() != null) {
            ((ViewGroup) surfaceView.getParent()).removeView(surfaceView);
        }
        largeSizePreviewLayout.addView(surfaceView);
        surfaceView.setZOrderMediaOverlay(false);

    }


    /**
     * 添加surfaceview到smallSizePreviewLayout
     */
    private void addIntoSmallSizePreviewLayout(SurfaceView surfaceView) {
        smallSizePreviewCoverImg.setVisibility(View.GONE);
        if (surfaceView.getParent() != null) {
            ((ViewGroup) surfaceView.getParent()).removeView(surfaceView);
        }
        smallSizePreviewLayout.addView(surfaceView);
        surfaceView.setZOrderMediaOverlay(true);
        smallSizePreviewLayout.setVisibility(View.VISIBLE);
    }


    private View.OnTouchListener smallPreviewTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(final View v, MotionEvent event) {


            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:

                    Toast.makeText(MainActivity.this , " 我这边下",Toast.LENGTH_SHORT).show();

                    break;
                case MotionEvent.ACTION_MOVE:
               Toast.makeText(MainActivity.this , " 我这边移动",Toast.LENGTH_SHORT).show();

                    break;
                case MotionEvent.ACTION_UP:

                        switchRender(smallAccount, largeAccount);
                    Toast.makeText(MainActivity.this , " 我这边提升",Toast.LENGTH_SHORT).show();
//                        switchAndSetLayout();

                    break;
            }

            return true;
        }
    };

    /**
     * 大小图像显示切换
     *
     * @param user1 用户1的account
     * @param user2 用户2的account  大
     */
    private void switchRender(String user1, String user2) {

        //先取消用户的画布
        if ("1020025".equals(user1)) {
            AVChatManager.getInstance().setupLocalVideoRender(null, false, 0);
        } else {
            AVChatManager.getInstance().setupRemoteVideoRender(user1, null, false, 0);
        }
        if ("1020025".equals(user2)) {
            AVChatManager.getInstance().setupLocalVideoRender(null, false, 0);
        } else {
            AVChatManager.getInstance().setupRemoteVideoRender(user2, null, false, 0);
        }
        //交换画布
        //如果存在多个用户,建议用Map维护account,render关系.
        //目前只有两个用户,并且认为这两个account肯定是对的
        AVChatSurfaceViewRenderer render1;
        AVChatSurfaceViewRenderer render2;
        if (user1.equals(smallAccount)) {
            render1 = largeRender;
            render2 = smallRender;
        } else {
            render1 = smallRender;
            render2 = largeRender;
        }

        //重新设置上画布
        if (user1 == "1020025") {
            AVChatManager.getInstance().setupLocalVideoRender(render1, false, AVChatVideoScalingType.SCALE_ASPECT_BALANCED);
        } else {
            AVChatManager.getInstance().setupRemoteVideoRender(user1, render1, false, AVChatVideoScalingType.SCALE_ASPECT_BALANCED);
        }
        if (user2 == "1020025") {
            AVChatManager.getInstance().setupLocalVideoRender(render2, false, AVChatVideoScalingType.SCALE_ASPECT_BALANCED);
        } else {
            AVChatManager.getInstance().setupRemoteVideoRender(user2, render2, false, AVChatVideoScalingType.SCALE_ASPECT_BALANCED);
        }

        if(!isBoolean){
            initLargeSurfaceView(user1);
            initSmallSurfaceView(user2);
            isBoolean = true;
            smallAccount = "1020025";
            largeAccount = "1020723";
        }else
        {
            initSmallSurfaceView(user1);
            initLargeSurfaceView(user2);
            isBoolean = false;
            smallAccount = "1020723";
            largeAccount = "1020025";
        }

    }


    @Override
    public void onJoinedChannel(int code, String audioFile, String videoFile, int elapsed) {

    }

    @Override
    public void onUserJoined(String account) {

    }

    @Override
    public void onUserLeave(String account, int event) {

    }

    @Override
    public void onLeaveChannel() {

    }

    @Override
    public void onProtocolIncompatible(int status) {

    }

    @Override
    public void onDisconnectServer() {

    }

    @Override
    public void onNetworkQuality(String user, int quality, AVChatNetworkStats stats) {

    }

    @Override
    public void onCallEstablished() {
        Toast.makeText(MainActivity.this , "我走了么",Toast.LENGTH_SHORT).show();
//        initLargeSurfaceView("1020025");
    }

    @Override
    public void onDeviceEvent(int code, String desc) {

    }

    @Override
    public void onConnectionTypeChanged(int netType) {

    }

    @Override
    public void onFirstVideoFrameAvailable(String account) {

    }

    @Override
    public void onFirstVideoFrameRendered(String user) {

    }

    @Override
    public void onVideoFrameResolutionChanged(String user, int width, int height, int rotate) {

    }

    @Override
    public void onVideoFpsReported(String account, int fps) {

    }

    @Override
    public boolean onVideoFrameFilter(AVChatVideoFrame frame, boolean maybeDualInput) {
        return false;
    }

    @Override
    public boolean onAudioFrameFilter(AVChatAudioFrame frame) {
        return false;
    }

    @Override
    public void onAudioDeviceChanged(int device) {

    }

    @Override
    public void onReportSpeaker(Map<String, Integer> speakers, int mixedEnergy) {

    }

    @Override
    public void onSessionStats(AVChatSessionStats sessionStats) {

    }

    @Override
    public void onLiveEvent(int event) {

    }
}
