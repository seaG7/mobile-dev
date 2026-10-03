package ru.mirea.danilov.scrollviewapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.math.BigInteger;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LinearLayout column = findViewById(R.id.column);
        LayoutInflater inflater = getLayoutInflater();
        for (int index = 0; index < 100; index++) {
            BigInteger term = BigInteger.ONE.shiftLeft(index);
            View row = inflater.inflate(R.layout.item_term, column, false);
            TextView line = row.findViewById(R.id.textTerm);
            line.setText((index + 1) + "    " + term.toString());
            column.addView(row);
        }
    }
}
