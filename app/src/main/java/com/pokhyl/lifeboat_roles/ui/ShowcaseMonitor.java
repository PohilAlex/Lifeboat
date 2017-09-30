package com.pokhyl.lifeboat_roles.ui;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;

import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.SimpleShowcaseEventListener;
import com.github.amlcurran.showcaseview.targets.PointTarget;
import com.github.amlcurran.showcaseview.targets.ViewTarget;
import com.pokhyl.lifeboat_roles.R;
import com.pokhyl.lifeboat_roles.storage.SettingsStorage;

public class ShowcaseMonitor {

    private final Activity activity;

    private ShowcaseView roleShowcase;
    private ShowcaseView settingsShowcase;
    private SettingsStorage settingsStorage;

    public ShowcaseMonitor(Activity activity) {
        this.activity = activity;
        settingsStorage = new SettingsStorage(activity);
    }

    public void showIntoTips() {
        new ShowcaseView.Builder(activity)
                .setContentTitle(R.string.welcome_msg)
                .setContentText(R.string.welcome_tip_text)
                .setStyle(R.style.CustomShowcaseTheme)
                .blockAllTouches()
                .setShowcaseEventListener(new SimpleShowcaseEventListener() {
                    @Override
                    public void onShowcaseViewHide(ShowcaseView showcaseView) {
                        showRoleTips();
                        settingsStorage.setAppTourProgress(AppTourProgress.INTRO_SHOWN);
                    }
                })
                .build();
    }

    public void showRoleTips() {
        RecyclerView recyclerView = (RecyclerView) activity.findViewById(R.id.person_grid);
        View targetView = recyclerView.getLayoutManager().getChildAt(0);
        roleShowcase = new ShowcaseView.Builder(activity)
                .setTarget(new ViewTarget(targetView))
                .setContentTitle(R.string.role_tip_title)
                .setContentText(R.string.role_tip_text)
                .setStyle(R.style.CustomShowcaseTheme)
                .setShowcaseEventListener(new SimpleShowcaseEventListener() {
                    @Override
                    public void onShowcaseViewHide(ShowcaseView showcaseView) {
                        showSettingsTips();
                        settingsStorage.setAppTourProgress(AppTourProgress.ROLE_TIPS_SHOWN);
                    }
                })
                .build();
    }

    public void showSettingsTips() {
        DisplayMetrics metrics = activity.getResources().getDisplayMetrics();
        settingsShowcase = new ShowcaseView.Builder(activity)
                .setTarget(new PointTarget((int) (metrics.widthPixels *0.9), (int) (metrics.heightPixels * 0.1)))
                .setContentTitle(R.string.settings_tip_title)
                .setContentText(R.string.settings_tip_text)
                .setStyle(R.style.CustomShowcaseTheme)
                .setShowcaseEventListener(new SimpleShowcaseEventListener() {
                    @Override
                    public void onShowcaseViewHide(ShowcaseView showcaseView) {
                        settingsStorage.setAppTourProgress(AppTourProgress.NEW_GAME_TIPS_SHOWN);
                    }
                })
                .build();
    }


    public void hideRoleTips() {
        if (roleShowcase != null && roleShowcase.isShowing()) {
            roleShowcase.hide();
        }
    }

    public void hideSettingsTips() {
        if (settingsShowcase != null && settingsShowcase.isShowing()) {
            settingsShowcase.hide();
        }
    }

    public static enum AppTourProgress {
        NOT_SHOW_YET,
        INTRO_SHOWN,
        ROLE_TIPS_SHOWN,
        NEW_GAME_TIPS_SHOWN
    }

}
