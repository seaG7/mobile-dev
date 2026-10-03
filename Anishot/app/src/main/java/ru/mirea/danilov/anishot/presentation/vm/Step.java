package ru.mirea.danilov.anishot.presentation.vm;

public final class Step {
    public enum Where { HOME, AUTH }

    private final Where where;
    private boolean taken;

    public Step(Where where) {
        this.where = where;
    }

    public Where take() {
        if (taken) {
            return null;
        }
        taken = true;
        return where;
    }
}
