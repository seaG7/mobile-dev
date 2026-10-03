package ru.mirea.danilov.anishot.presentation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;

import ru.mirea.danilov.anishot.AnishotApp;
import ru.mirea.danilov.anishot.R;
import ru.mirea.danilov.anishot.domain.LoginUseCase;
import ru.mirea.danilov.anishot.domain.repository.AuthCallback;

public class AuthActivity extends AppCompatActivity {
    private static final String[] KICKER = {"Кадр", "Узнавание", "Дневник"};
    private static final String[] TITLE = {
            "Найди аниме\nпо одному кадру",
            "Серия, таймкод\nи сходство",
            "Список, как\nдневник просмотра"
    };
    private static final String[] LEAD = {
            "Фото или стоп-кадр. Тайтл, серия и таймкод.",
            "Покажем серию, минуту и насколько кадр похож.",
            "Гость листает каталог. В список пишет свой аккаунт."
    };

    private View[] dots;
    private TextView textKicker;
    private TextView textHeroTitle;
    private TextView textHeroLead;
    private View buttonNext;
    private View formAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        AnishotApp app = (AnishotApp) getApplication();
        LoginUseCase loginUseCase = new LoginUseCase(app.authRepository());
        EditText email = findViewById(R.id.editEmail);
        EditText password = findViewById(R.id.editPassword);
        TextView error = findViewById(R.id.textError);
        textKicker = findViewById(R.id.textKicker);
        textHeroTitle = findViewById(R.id.textHeroTitle);
        textHeroLead = findViewById(R.id.textHeroLead);
        buttonNext = findViewById(R.id.buttonNext);
        formAuth = findViewById(R.id.formAuth);
        dots = new View[]{
                findViewById(R.id.dot0),
                findViewById(R.id.dot1),
                findViewById(R.id.dot2)
        };

        View sheet = findViewById(R.id.sheet);
        int sheetBottom = sheet.getPaddingBottom();
        ViewCompat.setOnApplyWindowInsetsListener(sheet, (view, insets) -> {
            Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            view.setPadding(view.getPaddingLeft(), view.getPaddingTop(), view.getPaddingRight(),
                    sheetBottom + bars.bottom);
            return insets;
        });

        ViewPager2 pager = findViewById(R.id.pager);
        pager.setAdapter(new OnboardingAdapter());
        pager.setOffscreenPageLimit(2);
        pager.setPageTransformer((page, position) -> {
            View art = page.findViewById(R.id.imageArt);
            if (art != null) {
                art.setTranslationX(-position * 48f * page.getResources().getDisplayMetrics().density);
            }
        });
        pager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                showPage(position);
            }
        });

        Motion.press(buttonNext);
        Motion.press(findViewById(R.id.buttonLogin));
        buttonNext.setOnClickListener(view -> pager.setCurrentItem(pager.getCurrentItem() + 1, true));
        findViewById(R.id.buttonLogin).setOnClickListener(view -> {
            error.setVisibility(View.GONE);
            loginUseCase.execute(
                    String.valueOf(email.getText()),
                    String.valueOf(password.getText()),
                    new AuthCallback() {
                        @Override
                        public void onSuccess() {
                            openHome();
                        }

                        @Override
                        public void onError(String message) {
                            error.setVisibility(View.VISIBLE);
                            error.setText(message);
                        }
                    }
            );
        });
        findViewById(R.id.buttonRegister).setOnClickListener(view ->
                startActivity(new Intent(this, RegisterActivity.class)));
        findViewById(R.id.buttonGuest).setOnClickListener(view -> {
            app.authRepository().continueAsGuest();
            openHome();
        });
        showPage(0);
    }

    private void showPage(int position) {
        paintDots(position);
        textKicker.setText(KICKER[position]);
        textHeroTitle.setText(TITLE[position]);
        textHeroLead.setText(LEAD[position]);
        boolean last = position == TITLE.length - 1;
        buttonNext.setVisibility(last ? View.GONE : View.VISIBLE);
        if (last && formAuth.getVisibility() != View.VISIBLE) {
            formAuth.setAlpha(0f);
            formAuth.setTranslationY(28f);
            formAuth.setVisibility(View.VISIBLE);
            formAuth.animate().alpha(1f).translationY(0f).setDuration(340).start();
        } else if (!last) {
            formAuth.setVisibility(View.GONE);
        }
    }

    private void paintDots(int index) {
        for (int i = 0; i < dots.length; i++) {
            boolean on = i == index;
            dots[i].setBackgroundResource(on ? R.drawable.bg_dot_on : R.drawable.bg_dot_off);
            ViewGroup.LayoutParams params = dots[i].getLayoutParams();
            params.width = dp(on ? 22 : 8);
            dots[i].setLayoutParams(params);
        }
    }

    private int dp(int value) {
        return Math.round(value * getResources().getDisplayMetrics().density);
    }

    private void openHome() {
        startActivity(new Intent(this, HomeActivity.class));
        finish();
    }
}
