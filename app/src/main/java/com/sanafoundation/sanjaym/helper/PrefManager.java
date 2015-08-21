package com.sanafoundation.sanjaym.helper;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by IM028 on 6/25/15.
 */
public class PrefManager {
    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    SharedPreferences.Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Shared pref file name
    private static final String PREF_NAME = "SanjayM";

    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";

    // Email address
    private static final String KEY_USER_NAME = "user";

    // Password
    private static final String KEY_PASSWORD = "password";

    // Object Id
    private static final String KEY_OBJECT_ID = "objectId";

    // Full Name
    private static final String KEY_FULL_NAME = "fullName";

    // Profile Pic
    private static final String KEY_PROFILR_PIC = "profilePic";

    // Constructor
    public PrefManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    /**
     * Create login session
     */
    public void createLoginSession(String user, String objectId, String fullName) {
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);

        // Storing email in pref
        editor.putString(KEY_USER_NAME, user);

        editor.putString(KEY_OBJECT_ID, objectId);

        editor.putString(KEY_FULL_NAME, fullName);

        // commit changes
        editor.commit();
    }

    public void createProfilePic(String profile_pic) {

        editor.putString(KEY_PROFILR_PIC, profile_pic);
        editor.commit();
    }

    public String getUserName() {
        return pref.getString(KEY_USER_NAME, null);
    }

    public String getObjectId() {
        return pref.getString(KEY_OBJECT_ID, null);
    }

    public String getUserFullName() {
        return pref.getString(KEY_FULL_NAME, null);
    }

    public String getProfilePic() {
        return pref.getString(KEY_PROFILR_PIC, null);
    }

    public boolean isLoggedIn() {
        return pref.getBoolean(IS_LOGIN, false);
    }

    public void logout() {
        editor.clear();
        editor.commit();
    }
}
