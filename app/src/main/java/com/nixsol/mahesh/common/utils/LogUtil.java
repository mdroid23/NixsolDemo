package com.nixsol.mahesh.common.utils;

import android.util.Log;

import com.google.gson.Gson;
import com.nixsol.mahesh.BuildConfig;

public class LogUtil {
    public static void info(String Tag, String message) {
        if (BuildConfig.BUILD_TYPE.equals("release"))
            return;
        Log.d(Tag, message);
    }

    public static void printObject(Object object) {
        if (BuildConfig.BUILD_TYPE.equals("release"))
            return;
        String string = ((new Gson()).toJson(object));
        LogUtil.info(object.getClass().getSimpleName(), string);
    }

    public static void printRetroFitError(Throwable error) {
        if (BuildConfig.BUILD_TYPE.equals("release"))
            return;
        error.printStackTrace();
    }

    public static void printException(Exception exception){
        if (BuildConfig.BUILD_TYPE.equals("release"))
            return;
        exception.printStackTrace();

    }
}
