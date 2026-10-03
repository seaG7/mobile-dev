package ru.mirea.danilov.anishot.data.storage.client;

import ru.mirea.danilov.anishot.data.storage.client.models.ClientRecord;

public interface ClientStorage {
    ClientRecord get();

    boolean save(ClientRecord record);
}
