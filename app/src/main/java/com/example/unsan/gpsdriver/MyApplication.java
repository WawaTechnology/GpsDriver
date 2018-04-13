package com.example.unsan.gpsdriver;

import android.app.Activity;
import android.app.Application;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Unsan on 12/4/18.
 */

public class MyApplication extends Application {
    public static MyApplication ma;
    FirebaseDatabase fbd;


    public static MyApplication getInstance()
    {

        return ma;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ma =this;
        fbd=FirebaseDatabase.getInstance();
        fbd.setPersistenceEnabled(true);
        registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {

            @Override
            public void onActivityCreated(Activity activity,
                                          Bundle savedInstanceState) {

                // new activity created; force its orientation to portrait
                activity.setRequestedOrientation(
                        ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {

            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }


        });
    }
}
