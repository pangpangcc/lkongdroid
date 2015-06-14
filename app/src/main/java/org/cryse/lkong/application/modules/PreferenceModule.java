package org.cryse.lkong.application.modules;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import org.cryse.lkong.application.qualifier.PrefsDefaultAccountUid;
import org.cryse.lkong.application.qualifier.PrefsImageDownloadPolicy;
import org.cryse.lkong.application.qualifier.PrefsVersionCode;
import org.cryse.lkong.application.qualifier.PrefsNightMode;
import org.cryse.lkong.application.qualifier.PrefsPostTail;
import org.cryse.lkong.application.qualifier.PrefsReadFontSize;
import org.cryse.lkong.application.qualifier.PrefsThemeColor;
import org.cryse.lkong.application.qualifier.PrefsUseInAppBrowser;
import org.cryse.utils.preference.BooleanPreference;
import org.cryse.utils.preference.IntegerPreference;
import org.cryse.utils.preference.LongPreference;
import org.cryse.utils.preference.PreferenceConstant;
import org.cryse.utils.preference.StringPreference;

import dagger.Module;
import dagger.Provides;

@Module
public class PreferenceModule {
    Application mApplicationContext;
    public PreferenceModule(Application application) {
        this.mApplicationContext = application;
    }

    @Provides
    public SharedPreferences provideSharedPreferences() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mApplicationContext);
        return prefs;
    }

    @Provides
    @PrefsNightMode
    BooleanPreference provideIsNightMode(SharedPreferences preferences) {
        return new BooleanPreference(preferences, PreferenceConstant.SHARED_PREFERENCE_IS_NIGHT_MODE, PreferenceConstant.SHARED_PREFERENCE_IS_NIGHT_MODE_VALUE);
    }

    @Provides
    @PrefsDefaultAccountUid
    LongPreference provideDefaultAccountUid(SharedPreferences preferences) {
        return new LongPreference(preferences, PreferenceConstant.SHARED_PREFERENCE_DEFAULT_ACCOUNT_UID, PreferenceConstant.SHARED_PREFERENCE_DEFAULT_ACCOUNT_UID_VALUE);
    }

    @Provides
    @PrefsPostTail
    StringPreference providePostTailText(SharedPreferences preferences) {
        return new StringPreference(preferences, PreferenceConstant.SHARED_PREFERENCE_POST_TAIL_TEXT, PreferenceConstant.SHARED_PREFERENCE_POST_TAIL_TEXT_VALUE);
    }

    @Provides
    @PrefsImageDownloadPolicy
    StringPreference provideImageDownloadPolicy(SharedPreferences preferences) {
        return new StringPreference(preferences, PreferenceConstant.SHARED_PREFERENCE_IMAGE_DOWNLOAD_POLICY, PreferenceConstant.SHARED_PREFERENCE_IMAGE_DOWNLOAD_POLICY_VALUE);
    }

    @Provides
    @PrefsThemeColor
    IntegerPreference provideThemeColorIndex(SharedPreferences preferences) {
        return new IntegerPreference(preferences, PreferenceConstant.SHARED_PREFERENCE_THEME_COLOR, PreferenceConstant.SHARED_PREFERENCE_THEME_COLOR_VALUE);
    }

    @Provides
    @PrefsReadFontSize
    StringPreference provideReadFontSize(SharedPreferences preferences) {
        return new StringPreference(preferences, PreferenceConstant.SHARED_PREFERENCE_READ_FONT, PreferenceConstant.SHARED_PREFERENCE_READ_FONT_VALUE);
    }

    @Provides
    @PrefsUseInAppBrowser
    BooleanPreference provideUseInAppBrowser(SharedPreferences preferences) {
        return new BooleanPreference(preferences, PreferenceConstant.SHARED_PREFERENCE_USE_IN_APP_BROWSER, PreferenceConstant.SHARED_PREFERENCE_USE_IN_APP_BROWSER_VALUE);
    }

    @Provides
    @PrefsVersionCode
    IntegerPreference provideVersionCode(SharedPreferences preferences) {
        return new IntegerPreference(preferences, PreferenceConstant.SHARED_PREFERENCE_VERSION_CODE, PreferenceConstant.SHARED_PREFERENCE_VERSION_CODE_VALUE);
    }
}
