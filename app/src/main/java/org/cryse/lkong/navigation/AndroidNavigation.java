package org.cryse.lkong.navigation;


import android.content.Context;

import org.cryse.lkong.application.LKongApplication;

public class AndroidNavigation {
    public LKongApplication mApplication;
    public AndroidNavigation(Context context) {
        this.mApplication = (LKongApplication)context;
    }
}
