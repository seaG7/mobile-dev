package ru.mirea.danilov.Lesson9;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import ru.mirea.danilov.Lesson9.domain.models.Movie;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(MainActivity.class.getSimpleName(), "MainActivity created");
        setContentView(R.layout.activity_main);
        EditText text = findViewById(R.id.editTextMovie);
        TextView textView = findViewById(R.id.textViewMovie);
        MainViewModel movieModel = new ViewModelProvider(this, new ViewModelFactory(this))
                .get(MainViewModel.class);
        movieModel.getFavoriteMovie().observe(this, title -> textView.setText(title));
        findViewById(R.id.buttonSaveMovie).setOnClickListener(view ->
                movieModel.setText(new Movie(2, text.getText().toString())));
        findViewById(R.id.buttonGetMovie).setOnClickListener(view -> movieModel.getText());
    }
}
