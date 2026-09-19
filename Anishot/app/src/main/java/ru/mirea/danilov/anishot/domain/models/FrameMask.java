package ru.mirea.danilov.anishot.domain.models;

public class FrameMask {
    private String label;
    private boolean hasPerson;

    public FrameMask(String label, boolean hasPerson) {
        this.label = label;
        this.hasPerson = hasPerson;
    }

    public String getLabel() {
        return label;
    }

    public boolean hasPerson() {
        return hasPerson;
    }
}
