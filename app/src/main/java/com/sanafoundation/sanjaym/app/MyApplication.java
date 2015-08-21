package com.sanafoundation.sanjaym.app;

import android.app.Application;
import android.content.Context;

import com.facebook.FacebookSdk;
import com.firebase.client.Firebase;
import com.parse.ParseACL;
import com.parse.ParseUser;
import com.sanafoundation.sanjaym.helper.ParseUtils;

/**
 * Created by ${Sanjay} on 6/25 /15.
 */
public class MyApplication extends Application {

    private static MyApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;

        // register with parse
        ParseUtils.registerParse(this);

        ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();

        // If you would like all objects to be private by default, remove this line.
        defaultACL.setPublicReadAccess(true);
        ParseACL.setDefaultACL(defaultACL, true);

        Firebase.setAndroidContext(this);

        FacebookSdk.sdkInitialize(getApplicationContext());

        /*Parse.initialize(this,
                AppConfig.PARSE_APPLICATION_ID,
                AppConfig.PARSE_CLIENT_KEY
        );*/

        //ParseFacebookUtils.initialize(mInstance);

    }

    public static Context getContext() {
        return mInstance;
    }

    public static synchronized MyApplication getInstance() {
        return mInstance;
    }

}
