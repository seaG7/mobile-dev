package ru.mirea.danilov.listviewapp;

import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListView list = findViewById(R.id.bookList);
        list.setAdapter(new BookAdapter(getLayoutInflater(), shelf()));
    }

    private List<Book> shelf() {
        List<Book> books = new ArrayList<>();
        books.add(new Book("Умберто Эко", "Имя розы"));
        books.add(new Book("Харуки Мураками", "Норвежский лес"));
        books.add(new Book("Фёдор Достоевский", "Идиот"));
        books.add(new Book("Лев Толстой", "Анна Каренина"));
        books.add(new Book("Михаил Булгаков", "Мастер и Маргарита"));
        books.add(new Book("Антон Чехов", "Вишнёвый сад"));
        books.add(new Book("Иван Тургенев", "Отцы и дети"));
        books.add(new Book("Николай Гоголь", "Мёртвые души"));
        books.add(new Book("Александр Пушкин", "Евгений Онегин"));
        books.add(new Book("Борис Пастернак", "Доктор Живаго"));
        books.add(new Book("Владимир Набоков", "Защита Лужина"));
        books.add(new Book("Габриэль Гарсиа Маркес", "Сто лет одиночества"));
        books.add(new Book("Хорхе Луис Борхес", "Вымыслы"));
        books.add(new Book("Итало Кальвино", "Если однажды зимней ночью путник"));
        books.add(new Book("Станислав Лем", "Солярис"));
        books.add(new Book("Аркадий и Борис Стругацкие", "Пикник на обочине"));
        books.add(new Book("Рэй Брэдбери", "451 градус по Фаренгейту"));
        books.add(new Book("Урсула Ле Гуин", "Левая рука тьмы"));
        books.add(new Book("Филип Дик", "Мечтают ли андроиды об электроовцах"));
        books.add(new Book("Уильям Гибсон", "Нейромант"));
        books.add(new Book("Нил Гейман", "История с кладбищем"));
        books.add(new Book("Хаяо Миядзаки", "Отправная точка. 1979–1996"));
        books.add(new Book("Скотт Макклауд", "Понимание комикса"));
        books.add(new Book("Дональд Ричи", "Сто лет японского кино"));
        books.add(new Book("Андрей Тарковский", "Запечатлённое время"));
        books.add(new Book("Сергей Эйзенштейн", "Избранные статьи"));
        books.add(new Book("Юрий Норштейн", "Снег на траве"));
        books.add(new Book("Эрих Мария Ремарк", "Три товарища"));
        books.add(new Book("Эрнест Хемингуэй", "Старик и море"));
        books.add(new Book("Джером Сэлинджер", "Над пропастью во ржи"));
        books.add(new Book("Джордж Оруэлл", "1984"));
        books.add(new Book("Олдос Хаксли", "О дивный новый мир"));
        books.add(new Book("Франц Кафка", "Превращение"));
        books.add(new Book("Альбер Камю", "Посторонний"));
        books.add(new Book("Хулио Кортасар", "Игра в классики"));
        books.add(new Book("Патрик Зюскинд", "Парфюмер"));
        books.add(new Book("Виктор Пелевин", "Generation «П»"));
        books.add(new Book("Михаил Лермонтов", "Герой нашего времени"));
        return books;
    }
}
