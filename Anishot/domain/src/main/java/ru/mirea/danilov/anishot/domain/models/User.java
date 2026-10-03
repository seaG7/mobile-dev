package ru.mirea.danilov.anishot.domain.models;

public class User {
    private int id;
    private String login;
    private boolean guest;
    private String lastLoginAt;

    public User(int id, String login, boolean guest, String lastLoginAt) {
        this.id = id;
        this.login = login;
        this.guest = guest;
        this.lastLoginAt = lastLoginAt;
    }

    public int getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public boolean isGuest() {
        return guest;
    }

    public String getLastLoginAt() {
        return lastLoginAt;
    }
}
