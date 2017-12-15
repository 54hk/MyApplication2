package haiban.haiqi.com.video.other;

import android.content.Context;
import android.view.WindowManager;

/**
 * Created by 54hk on 2017/12/8.
 */

public class Utils {

    private final WindowManager wm;
    public  Utils( Context context){
        wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
    }

    public  int getWidth(){
      return wm.getDefaultDisplay().getWidth();
    }
}
