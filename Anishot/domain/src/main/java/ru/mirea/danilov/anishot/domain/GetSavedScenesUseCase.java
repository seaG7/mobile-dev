package ru.mirea.danilov.anishot.domain;

import java.util.ArrayList;
import java.util.List;

import ru.mirea.danilov.anishot.domain.models.SceneMatch;
import ru.mirea.danilov.anishot.domain.models.User;
import ru.mirea.danilov.anishot.domain.repository.AuthRepository;
import ru.mirea.danilov.anishot.domain.repository.SceneRepository;

/** Получить сохранённые сцены */
public class GetSavedScenesUseCase {
    private final SceneRepository sceneRepository;
    private final AuthRepository authRepository;

    public GetSavedScenesUseCase(SceneRepository sceneRepository, AuthRepository authRepository) {
        this.sceneRepository = sceneRepository;
        this.authRepository = authRepository;
    }

    public List<SceneMatch> execute() {
        User user = authRepository.getProfile();
        if (user.isGuest()) {
            return new ArrayList<>();
        }
        return sceneRepository.getSavedScenes();
    }
}
