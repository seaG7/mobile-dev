package ru.mirea.danilov.anishot;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.List;

import ru.mirea.danilov.anishot.data.repository.AnimeRepositoryImpl;
import ru.mirea.danilov.anishot.data.repository.AuthRepositoryImpl;
import ru.mirea.danilov.anishot.data.repository.FrameMaskRepositoryImpl;
import ru.mirea.danilov.anishot.data.repository.ListRepositoryImpl;
import ru.mirea.danilov.anishot.data.repository.SceneRepositoryImpl;
import ru.mirea.danilov.anishot.domain.BuildFrameMaskUseCase;
import ru.mirea.danilov.anishot.domain.GetAnimeCatalogUseCase;
import ru.mirea.danilov.anishot.domain.GetMyListUseCase;
import ru.mirea.danilov.anishot.domain.GetProfileUseCase;
import ru.mirea.danilov.anishot.domain.IdentifyAnimeByFrameUseCase;
import ru.mirea.danilov.anishot.domain.LoginUseCase;
import ru.mirea.danilov.anishot.domain.SaveToMyListUseCase;
import ru.mirea.danilov.anishot.domain.models.Anime;
import ru.mirea.danilov.anishot.domain.models.FrameMask;
import ru.mirea.danilov.anishot.domain.models.ListEntry;
import ru.mirea.danilov.anishot.domain.models.SceneMatch;
import ru.mirea.danilov.anishot.domain.models.User;
import ru.mirea.danilov.anishot.domain.repository.AnimeRepository;
import ru.mirea.danilov.anishot.domain.repository.AuthRepository;
import ru.mirea.danilov.anishot.domain.repository.FrameMaskRepository;
import ru.mirea.danilov.anishot.domain.repository.ListRepository;
import ru.mirea.danilov.anishot.domain.repository.SceneRepository;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        AuthRepository authRepository = new AuthRepositoryImpl(this);
        AnimeRepository animeRepository = new AnimeRepositoryImpl(this);
        SceneRepository sceneRepository = new SceneRepositoryImpl(this);
        ListRepository listRepository = new ListRepositoryImpl(this);
        FrameMaskRepository frameMaskRepository = new FrameMaskRepositoryImpl(this);

        EditText editTextLogin = findViewById(R.id.editTextLogin);
        TextView textViewResult = findViewById(R.id.textViewResult);

        findViewById(R.id.buttonLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean result = new LoginUseCase(authRepository)
                        .execute(editTextLogin.getText().toString(), "test");
                User user = new GetProfileUseCase(authRepository).execute();
                textViewResult.setText(String.format("Login result %s, user %s",
                        result, user.getLogin()));
            }
        });

        findViewById(R.id.buttonIdentify).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FrameMask mask = new BuildFrameMaskUseCase(frameMaskRepository).execute();
                SceneMatch match = new IdentifyAnimeByFrameUseCase(sceneRepository).execute();
                textViewResult.setText(String.format(
                        "Mask: %s\nMatch: %s, ep %s, at %.1fs, sim %.3f",
                        mask.getLabel(),
                        match.getTitle(),
                        match.getEpisode(),
                        match.getAt(),
                        match.getSimilarity()
                ));
            }
        });

        findViewById(R.id.buttonCatalog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<Anime> catalog = new GetAnimeCatalogUseCase(animeRepository).execute();
                StringBuilder builder = new StringBuilder();
                for (Anime anime : catalog) {
                    builder.append(anime.getTitle())
                            .append(" (")
                            .append(anime.getAverageScore())
                            .append(")\n");
                }
                textViewResult.setText(builder.toString());
            }
        });

        findViewById(R.id.buttonMyList).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<ListEntry> myList = new GetMyListUseCase(listRepository, authRepository).execute();
                if (myList.isEmpty()) {
                    textViewResult.setText("Список пуст (гость не видит сохранённое)");
                    return;
                }
                StringBuilder builder = new StringBuilder();
                for (ListEntry entry : myList) {
                    builder.append(entry.getTitle())
                            .append(" — ")
                            .append(entry.getStatus())
                            .append(" / ")
                            .append(entry.getScore())
                            .append("\n");
                }
                textViewResult.setText(builder.toString());
            }
        });

        findViewById(R.id.buttonSave).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean result = new SaveToMyListUseCase(listRepository, authRepository)
                        .execute(new ListEntry(21, "One Piece", "plan", 0));
                textViewResult.setText(String.format("Save result %s", result));
            }
        });
    }
}
