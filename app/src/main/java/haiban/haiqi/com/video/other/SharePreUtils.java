package haiban.haiqi.com.video.other;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Set;


/**
 * Created by 54hk on 2017/12/3.
 */
public class SharePreUtils {

    public static final String PREF_NAME = "Config";
    private static Context context;

    public static boolean getBoolean(String key,
                                     boolean defaultValue) {
        SharedPreferences sp = App.context.getSharedPreferences(PREF_NAME,
                Context.MODE_PRIVATE);
        return sp.getBoolean(key, defaultValue);
    }

    public static boolean getBoolean(Context ctx, String key,
                                     boolean defaultValue) {
        SharedPreferences sp = ctx.getSharedPreferences(PREF_NAME,
                Context.MODE_PRIVATE);
        return sp.getBoolean(key, defaultValue);
    }

    public static void setBoolean(String key, boolean value) {
        SharedPreferences sp = App.context.getSharedPreferences(PREF_NAME,
                Context.MODE_PRIVATE);
        sp.edit().putBoolean(key, value).commit();
    }

    public static String getString(String key, String defaultValue) {
        SharedPreferences sp = App.context.getSharedPreferences(PREF_NAME,
                Context.MODE_PRIVATE);
        return sp.getString(key, defaultValue);
    }

    public static void setString(String key, String value) {
        SharedPreferences sp = App.context.getSharedPreferences(PREF_NAME,
                Context.MODE_PRIVATE);
        sp.edit().putString(key, value).commit();
    }

    public static void setLogin(String key, String value) {
        SharedPreferences sp = App.context.getSharedPreferences("login",
                Context.MODE_PRIVATE);
        sp.edit().putString(key, value).commit();
    }

    public static int getInt(String key, int defaultValue) {
        SharedPreferences sp = App.context.getSharedPreferences(PREF_NAME,
                Context.MODE_PRIVATE);
        return sp.getInt(key, defaultValue);
    }

    public static void setInt(String key, int value) {
        SharedPreferences sp = App.context.getSharedPreferences(PREF_NAME,
                Context.MODE_PRIVATE);
        sp.edit().putInt(key, value).commit();
    }

    public static void setSet(String key, Set<String> value) {
        SharedPreferences sp = App.context.getSharedPreferences(PREF_NAME,
                Context.MODE_PRIVATE);
        sp.edit().remove(key).commit();
        sp.edit().putStringSet(key, value).commit();
    }

    public static Set<String> getSet(String key) {
        SharedPreferences sp = App.context.getSharedPreferences(PREF_NAME,
                Context.MODE_PRIVATE);
        return sp.getStringSet(key, null);
    }

    public static void clear() {
        SharedPreferences sp = App.context.getSharedPreferences(PREF_NAME,
                Context.MODE_PRIVATE);
        sp.edit().clear().commit();
    }
}
