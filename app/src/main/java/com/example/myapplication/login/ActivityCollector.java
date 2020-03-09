package com.example.myapplication.login;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by szz on 2020/3/6.
 */

public class ActivityCollector {
    public static List<Activity> activities = new ArrayList<>();
    public static void addActivity(Activity activity){
        activities.add(activity);
    }
    public static void removeActivity(Activity activity){
        activities.remove(activity);
    }

    /**
     * 通过类名可以直接调用该方法，然后销毁所有添加到活动收集器里的活动
     */
    public static void finishAll(){
        for(Activity activity:activities){
            if(!activity.isFinishing()){
                activity.finish();
            }
        }
        activities.clear();
    }
}
