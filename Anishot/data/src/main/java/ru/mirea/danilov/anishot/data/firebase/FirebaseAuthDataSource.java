package ru.mirea.danilov.anishot.data.firebase;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import ru.mirea.danilov.anishot.domain.repository.AuthCallback;

public class FirebaseAuthDataSource {
    private final FirebaseAuth firebaseAuth;

    public FirebaseAuthDataSource() {
        firebaseAuth = FirebaseAuth.getInstance();
    }

    public void signIn(String email, String password, AuthCallback callback) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        callback.onSuccess();
                    } else {
                        callback.onError(messageOf(task.getException()));
                    }
                });
    }

    public void signUp(String email, String password, AuthCallback callback) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        callback.onSuccess();
                    } else {
                        callback.onError(messageOf(task.getException()));
                    }
                });
    }

    public void signOut() {
        firebaseAuth.signOut();
    }

    public FirebaseUser currentUser() {
        return firebaseAuth.getCurrentUser();
    }

    private String messageOf(Exception exception) {
        if (exception == null || exception.getMessage() == null) {
            return "Ошибка Firebase Auth";
        }
        return exception.getMessage();
    }
}
