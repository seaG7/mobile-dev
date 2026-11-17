package ru.mirea.danilov.anishot.domain;

import ru.mirea.danilov.anishot.domain.models.User;
import ru.mirea.danilov.anishot.domain.repository.AuthRepository;
import ru.mirea.danilov.anishot.domain.repository.ListRepository;

/** Обновить статус просмотра */
public class UpdateListStatusUseCase {
    private final ListRepository listRepository;
    private final AuthRepository authRepository;

    public UpdateListStatusUseCase(ListRepository listRepository, AuthRepository authRepository) {
        this.listRepository = listRepository;
        this.authRepository = authRepository;
    }

    public boolean execute(int animeId, String status) {
        User user = authRepository.getProfile();
        if (user.isGuest() || status == null || status.trim().isEmpty()) {
            return false;
        }
        return listRepository.updateStatus(animeId, status);
    }
}
