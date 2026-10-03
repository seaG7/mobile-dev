package ru.mirea.danilov.recyclerviewapp;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView recycler = findViewById(R.id.eventList);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        EventAdapter adapter = new EventAdapter(event ->
                Toast.makeText(this, event.year + " · " + event.title, Toast.LENGTH_SHORT).show());
        recycler.setAdapter(adapter);
        adapter.setItems(chronicle());
    }

    private List<Event> chronicle() {
        List<Event> events = new ArrayList<>();
        events.add(new Event(1917, "Намакура-гатана", "Короткий японский ролик. Самурай и тупой меч.", 0xFF3D342C));
        events.add(new Event(1928, "Пароходик Вилли", "У мыши появляется голос, и звук садится в кадр.", 0xFF1C1714));
        events.add(new Event(1937, "Белоснежка", "Рисованный фильм на полный метр.", 0xFF8C3A2A));
        events.add(new Event(1958, "Белая змея", "Toei Doga собирает полный метр по старой легенде.", 0xFF5C4A3A));
        events.add(new Event(1963, "Астробой", "Тэдзука ставит сериал на каждую неделю.", 0xFF2E4A3A));
        events.add(new Event(1979, "Гандам", "Робот перестаёт быть игрушкой и попадает на войну.", 0xFF3A3A55));
        events.add(new Event(1984, "Навсикая", "Миядзаки выпускает первую большую ленту о долине.", 0xFF6B3A4A));
        events.add(new Event(1988, "Акира", "Ночной Токио и взрослая аудитория в одном фильме.", 0xFF1F3A4A));
        events.add(new Event(1995, "Призрак в доспехах", "Осии спрашивает, где кончается тело.", 0xFF4A2E3A));
        events.add(new Event(1997, "Принцесса Мононокэ", "Лес и железо спорят в одном кадре.", 0xFF3A4A2E));
        events.add(new Event(2001, "Унесённые призраками", "Сэн ищет дорогу через баню духов.", 0xFF4A3A2E));
        events.add(new Event(2016, "Твоё имя", "Два города встречаются из-за кометы.", 0xFF6B2E2A));
        events.add(new Event(2020, "Поезд «Бесконечный»", "Дорога, у которой нет конечной станции.", 0xFF2A3A4A));
        events.add(new Event(2023, "Мальчик и птица", "Миядзаки снова говорит о матери и другом мире.", 0xFFC4512E));
        return events;
    }
}
