package com.pokhyl.lifeboat.ui;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;

import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.SimpleShowcaseEventListener;
import com.github.amlcurran.showcaseview.targets.PointTarget;
import com.github.amlcurran.showcaseview.targets.ViewTarget;
import com.pokhyl.lifeboat.R;
import com.pokhyl.lifeboat.storage.SettingsStorage;

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
                .setContentTitle("Добро пожаловать на борт")
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
                .setContentTitle("Шкет к рулю")
                .setContentText("Чтобы узнать своего друга и врага кликини на тайл Шкета")
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
                .setContentTitle("Отлично сделано")
                .setContentText("Нажмите чтобы сгенерировать новую игру")
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
