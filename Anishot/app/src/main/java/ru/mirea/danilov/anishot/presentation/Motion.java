package ru.mirea.danilov.anishot.presentation;

import android.view.MotionEvent;
import android.view.View;

public final class Motion {
    private Motion() {
    }

    public static void press(View view) {
        view.setOnTouchListener((target, event) -> {
            int action = event.getActionMasked();
            if (action == MotionEvent.ACTION_DOWN) {
                target.animate().scaleX(0.97f).scaleY(0.97f).setDuration(90).start();
            } else if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL) {
                target.animate().scaleX(1f).scaleY(1f).setDuration(160).start();
            }
            return false;
        });
    }

    public static void rise(View view) {
        view.setAlpha(0f);
        view.setTranslationY(28f);
        view.animate().alpha(1f).translationY(0f).setDuration(340).start();
    }
}
