package ru.mirea.danilov.anishot.presentation.details;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.button.MaterialButton;

import ru.mirea.danilov.anishot.R;
import ru.mirea.danilov.anishot.domain.models.Anime;
import ru.mirea.danilov.anishot.presentation.AuthActivity;
import ru.mirea.danilov.anishot.presentation.ImageBinder;
import ru.mirea.danilov.anishot.presentation.Motion;
import ru.mirea.danilov.anishot.presentation.vm.AnishotFactory;
import ru.mirea.danilov.anishot.presentation.vm.DetailsViewModel;

public class DetailsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        int id = getIntent().getIntExtra("animeId", 1);
        DetailsViewModel viewModel = new ViewModelProvider(this, AnishotFactory.from(getApplication()))
                .get(DetailsViewModel.class);

        View notice = findViewById(R.id.cardNotice);
        ImageView art = findViewById(R.id.imageNotice);
        TextView status = findViewById(R.id.textStatus);
        MaterialButton openAuth = findViewById(R.id.buttonOpenAuth);
        MaterialButton add = findViewById(R.id.buttonAdd);
        Motion.press(add);
        findViewById(R.id.buttonBack).setOnClickListener(v -> finish());
        openAuth.setOnClickListener(v -> startActivity(new Intent(this, AuthActivity.class)));
        add.setOnClickListener(v -> viewModel.saveCurrent());

        viewModel.anime().observe(this, anime -> {
            if (anime == null) {
                finish();
                return;
            }
            bind(anime);
        });
        viewModel.save().observe(this, save -> {
            if (save == null) {
                return;
            }
            notice.setVisibility(View.VISIBLE);
            Motion.rise(notice);
            if (save.guest) {
                art.setVisibility(View.VISIBLE);
                openAuth.setVisibility(View.VISIBLE);
                status.setText("Войдите, чтобы сохранить тайтл в список.");
            } else if (save.saved) {
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
        viewModel.open(id);
    }

    private void bind(Anime anime) {
        ImageBinder.load((ImageView) findViewById(R.id.imagePoster), anime.getImageUrl());
        ((TextView) findViewById(R.id.textTitle)).setText(anime.getTitle());
        ((TextView) findViewById(R.id.textOriginal)).setText(anime.getOriginalTitle());
        ((TextView) findViewById(R.id.textMeta)).setText(anime.getMeta());
        ((TextView) findViewById(R.id.textDescription)).setText(anime.getDescription());
        ((TextView) findViewById(R.id.textScore)).setText(String.valueOf(anime.getAverageScore()));
    }
}
