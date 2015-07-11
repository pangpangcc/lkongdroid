package org.cryse.lkong.view;

import org.cryse.lkong.model.UserInfoModel;

public interface UserProfileView extends ContentView {
    void onLoadUserProfileComplete(UserInfoModel userInfoModel);
    void onLoadUserProfileError(Throwable throwable, Object... extraInfo);
}
