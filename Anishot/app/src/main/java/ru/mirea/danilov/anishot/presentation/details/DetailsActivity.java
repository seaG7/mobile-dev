package ru.mirea.danilov.anishot.presentation.details;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

import ru.mirea.danilov.anishot.AnishotApp;
import ru.mirea.danilov.anishot.R;
import ru.mirea.danilov.anishot.domain.GetAnimeDetailsUseCase;
import ru.mirea.danilov.anishot.domain.SaveToMyListUseCase;
import ru.mirea.danilov.anishot.domain.models.Anime;
import ru.mirea.danilov.anishot.domain.models.ListEntry;
import ru.mirea.danilov.anishot.domain.models.User;
import ru.mirea.danilov.anishot.presentation.AuthActivity;
import ru.mirea.danilov.anishot.presentation.ImageBinder;
import ru.mirea.danilov.anishot.presentation.Motion;

public class DetailsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        AnishotApp app = (AnishotApp) getApplication();
        int id = getIntent().getIntExtra("animeId", 1);
        Anime anime = new GetAnimeDetailsUseCase(app.animeRepository()).execute(id);
        if (anime == null) {
            finish();
            return;
        }
        ImageBinder.load((ImageView) findViewById(R.id.imagePoster), anime.getImageUrl());
        ((TextView) findViewById(R.id.textTitle)).setText(anime.getTitle());
        ((TextView) findViewById(R.id.textOriginal)).setText(anime.getOriginalTitle());
        ((TextView) findViewById(R.id.textMeta)).setText(anime.getMeta());
        ((TextView) findViewById(R.id.textDescription)).setText(anime.getDescription());
        ((TextView) findViewById(R.id.textScore)).setText(String.valueOf(anime.getAverageScore()));
        findViewById(R.id.buttonBack).setOnClickListener(v -> finish());

        View notice = findViewById(R.id.cardNotice);
        ImageView art = findViewById(R.id.imageNotice);
        TextView status = findViewById(R.id.textStatus);
        MaterialButton openAuth = findViewById(R.id.buttonOpenAuth);
        MaterialButton add = findViewById(R.id.buttonAdd);
        Motion.press(add);
        openAuth.setOnClickListener(v -> startActivity(new Intent(this, AuthActivity.class)));
        add.setOnClickListener(v -> {
            User user = app.authRepository().getProfile();
            boolean ok = new SaveToMyListUseCase(app.listRepository(), app.authRepository())
                    .execute(new ListEntry(anime.getId(), anime.getTitle(), "watching", 0));
            notice.setVisibility(View.VISIBLE);
            Motion.rise(notice);
            if (user.isGuest()) {
                art.setVisibility(View.VISIBLE);
                openAuth.setVisibility(View.VISIBLE);
                status.setText("Войдите, чтобы сохранить тайтл в список.");
            } else if (ok) {
                art.setVisibility(View.GONE);
                openAuth.setVisibility(View.GONE);
                status.setText("Теперь в вашем списке.");
                add.setText("В списке");
                add.setEnabled(false);
            } else {
                art.setVisibility(View.GONE);
                openAuth.setVisibility(View.GONE);
                status.setText("Не удалось сохранить.");
            }
        });
    }
}
