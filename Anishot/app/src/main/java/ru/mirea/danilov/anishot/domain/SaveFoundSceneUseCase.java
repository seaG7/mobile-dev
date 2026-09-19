package ru.mirea.danilov.anishot.domain;

import ru.mirea.danilov.anishot.domain.models.SceneMatch;
import ru.mirea.danilov.anishot.domain.models.User;
import ru.mirea.danilov.anishot.domain.repository.AuthRepository;
import ru.mirea.danilov.anishot.domain.repository.SceneRepository;

/** Сохранить найденную сцену */
public class SaveFoundSceneUseCase {
    private SceneRepository sceneRepository;
    private AuthRepository authRepository;

    public SaveFoundSceneUseCase(SceneRepository sceneRepository, AuthRepository authRepository) {
        this.sceneRepository = sceneRepository;
        this.authRepository = authRepository;
    }

    public boolean execute(SceneMatch match) {
        User user = authRepository.getProfile();
        if (user.isGuest()) {
            return false;
        }
        return sceneRepository.saveScene(match);
    }
}
