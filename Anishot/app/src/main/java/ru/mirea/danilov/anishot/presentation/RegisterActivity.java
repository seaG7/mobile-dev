package ru.mirea.danilov.anishot.presentation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import ru.mirea.danilov.anishot.AnishotApp;
import ru.mirea.danilov.anishot.R;
import ru.mirea.danilov.anishot.domain.RegisterUseCase;
import ru.mirea.danilov.anishot.domain.repository.AuthCallback;

public class RegisterActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        AnishotApp app = (AnishotApp) getApplication();
        RegisterUseCase registerUseCase = new RegisterUseCase(app.authRepository());
        EditText email = findViewById(R.id.editEmail);
        EditText password = findViewById(R.id.editPassword);
        EditText repeat = findViewById(R.id.editPasswordRepeat);
        TextView error = findViewById(R.id.textError);

        findViewById(R.id.buttonRegister).setOnClickListener(view -> {
            error.setVisibility(View.GONE);
            registerUseCase.execute(
                    String.valueOf(email.getText()),
                    String.valueOf(password.getText()),
                    String.valueOf(repeat.getText()),
                    new AuthCallback() {
                        @Override
                        public void onSuccess() {
                            startActivity(new Intent(RegisterActivity.this, HomeActivity.class));
                            finish();
                        }

                        @Override
                        public void onError(String message) {
                            error.setVisibility(View.VISIBLE);
                            error.setText(message);
                        }
                    }
            );
        });
        findViewById(R.id.buttonBack).setOnClickListener(view -> finish());
    }
}
