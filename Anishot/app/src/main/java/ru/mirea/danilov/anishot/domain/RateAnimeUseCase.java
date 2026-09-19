package ru.mirea.danilov.anishot.domain;

import ru.mirea.danilov.anishot.domain.models.User;
import ru.mirea.danilov.anishot.domain.repository.AuthRepository;
import ru.mirea.danilov.anishot.domain.repository.ListRepository;

/** Оценить аниме */
public class RateAnimeUseCase {
    private ListRepository listRepository;
    private AuthRepository authRepository;

    public RateAnimeUseCase(ListRepository listRepository, AuthRepository authRepository) {
        this.listRepository = listRepository;
        this.authRepository = authRepository;
    }

    public boolean execute(int animeId, int score) {
        User user = authRepository.getProfile();
        if (user.isGuest()) {
            return false;
        }
        if (score < 1 || score > 10) {
            return false;
        }
        return listRepository.rate(animeId, score);
    }
}
