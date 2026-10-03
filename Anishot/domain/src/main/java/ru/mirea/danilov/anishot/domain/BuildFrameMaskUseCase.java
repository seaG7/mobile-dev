package ru.mirea.danilov.anishot.domain;

import ru.mirea.danilov.anishot.domain.models.FrameMask;
import ru.mirea.danilov.anishot.domain.repository.FrameMaskRepository;

/** Построить маску кадра */
public class BuildFrameMaskUseCase {
    private FrameMaskRepository frameMaskRepository;

    public BuildFrameMaskUseCase(FrameMaskRepository frameMaskRepository) {
        this.frameMaskRepository = frameMaskRepository;
    }

    public FrameMask execute() {
        return frameMaskRepository.buildMask();
    }
}
