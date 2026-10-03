package ru.mirea.danilov.anishot.presentation;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import ru.mirea.danilov.anishot.R;
import ru.mirea.danilov.anishot.presentation.vm.AnishotFactory;
import ru.mirea.danilov.anishot.presentation.vm.RegisterViewModel;
import ru.mirea.danilov.anishot.presentation.vm.Step;

public class RegisterActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        RegisterViewModel viewModel = new ViewModelProvider(this, AnishotFactory.from(getApplication()))
                .get(RegisterViewModel.class);
        EditText email = findViewById(R.id.editEmail);
        EditText password = findViewById(R.id.editPassword);
        EditText repeat = findViewById(R.id.editPasswordRepeat);
        TextView error = findViewById(R.id.textError);

        viewModel.error().observe(this, message -> {
            if (message == null || message.isEmpty()) {
                error.setVisibility(android.view.View.GONE);
                return;
            }
            error.setVisibility(android.view.View.VISIBLE);
            error.setText(message);
        });
        viewModel.navigation().observe(this, step -> {
            if (step != null && step.take() == Step.Where.HOME) {
                startActivity(new Intent(this, HomeActivity.class));
                finish();
            }
        });
        findViewById(R.id.buttonRegister).setOnClickListener(view -> viewModel.register(
                String.valueOf(email.getText()),
                String.valueOf(password.getText()),
                String.valueOf(repeat.getText())
        ));
        findViewById(R.id.buttonBack).setOnClickListener(view -> finish());
    }
}
