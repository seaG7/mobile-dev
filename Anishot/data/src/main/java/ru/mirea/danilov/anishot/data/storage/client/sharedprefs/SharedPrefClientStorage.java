package ru.mirea.danilov.anishot.data.storage.client.sharedprefs;

import android.content.Context;
import android.content.SharedPreferences;

import java.time.LocalDate;

import ru.mirea.danilov.anishot.data.storage.client.ClientStorage;
import ru.mirea.danilov.anishot.data.storage.client.models.ClientRecord;

public class SharedPrefClientStorage implements ClientStorage {
    private static final String SHARED_PREFS_NAME = "client_prefs";
    private static final String KEY_EMAIL = "client_email";
    private static final String KEY_GUEST = "client_guest";
    private static final String KEY_DATE = "client_date";
    private static final String KEY_ID = "client_id";

    private final SharedPreferences sharedPreferences;

    public SharedPrefClientStorage(Context context) {
        sharedPreferences = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
    }

    @Override
    public ClientRecord get() {
        String email = sharedPreferences.getString(KEY_EMAIL, "guest");
        boolean guest = sharedPreferences.getBoolean(KEY_GUEST, true);
        String date = sharedPreferences.getString(KEY_DATE, String.valueOf(LocalDate.now()));
        int id = sharedPreferences.getInt(KEY_ID, 0);
        return new ClientRecord(id, email, guest, date);
    }

    @Override
    public boolean save(ClientRecord record) {
        sharedPreferences.edit()
                .putString(KEY_EMAIL, record.getEmail())
                .putBoolean(KEY_GUEST, record.isGuest())
                .putString(KEY_DATE, record.getLocalDate())
                .putInt(KEY_ID, record.getId())
                .commit();
        return true;
    }
}
