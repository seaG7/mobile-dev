package ru.mirea.danilov.anishot.data.storage.client.models;

public class ClientRecord {
    private int id;
    private String email;
    private boolean guest;
    private String localDate;

    public ClientRecord(int id, String email, boolean guest, String localDate) {
        this.id = id;
        this.email = email;
        this.guest = guest;
        this.localDate = localDate;
    }

    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public boolean isGuest() {
        return guest;
    }

    public String getLocalDate() {
        return localDate;
    }
}
