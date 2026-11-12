
package com.turkmencargo;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.Color;
import android.view.Gravity;
import android.graphics.Typeface;
import android.widget.*;

public class MainActivity extends Activity {

    LinearLayout layout;
    String language = "tk";

    int background = Color.rgb(12, 25, 40);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showLanguages();
    }

    void setupLayout() {
        layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setGravity(Gravity.CENTER);
        layout.setPadding(30, 30, 30, 30);
        layout.setBackgroundColor(background);
    }

    TextView makeText(String text, int size) {
        TextView t = new TextView(this);
        t.setText(text);
        t.setTextSize(size);
        t.setTextColor(Color.WHITE);
        t.setGravity(Gravity.CENTER);
        t.setPadding(10, 20, 10, 20);
        return t;
    }

    Button makeButton(String text) {
        Button b = new Button(this);
        b.setText(text);
        return b;
    }

    void showLanguages() {
        setupLayout();

        TextView title = makeText("TURKMEN CARGO", 30);
        title.setTypeface(null, Typeface.BOLD);

        TextView subtitle = makeText("Choose your language", 19);

        layout.addView(title);
        layout.addView(subtitle);

        Button tk = makeButton("🇹🇲 Türkmen dili");
        Button ru = makeButton("🇷🇺 Русский язык");
        Button tr = makeButton("🇹🇷 Türkçe");

        layout.addView(tk);
        layout.addView(ru);
        layout.addView(tr);

        tk.setOnClickListener(v -> {
            language = "tk";
            showHome();
        });

        ru.setOnClickListener(v -> {
            language = "ru";
            showHome();
        });

        tr.setOnClickListener(v -> {
            language = "tr";
            showHome();
        });

        setContentView(layout);
    }

    void showHome() {
        setupLayout();

        String welcome;
        String create;
        String myCargo;
        String contact;
        String back;

        if (language.equals("tk")) {
            welcome = "Hoş geldiňiz!";
            create = "Ýük döretmek";
            myCargo = "Ýüklerim";
            contact = "Habarlaşmak";
            back = "Dil saýlamak";
        } else if (language.equals("ru")) {
            welcome = "Добро пожаловать!";
            create = "Создать груз";
            myCargo = "Мои грузы";
            contact = "Контакты";
            back = "Выбрать язык";
        } else {
            welcome = "Hoş geldiniz!";
            create = "Gönderi Oluştur";
            myCargo = "Gönderilerim";
            contact = "İletişim";
            back = "Dil Seçimi";
        }

        TextView title = makeText("TURKMEN CARGO", 30);
        title.setTypeface(null, Typeface.BOLD);

        TextView subtitle = makeText(welcome, 22);

        layout.addView(title);
        layout.addView(subtitle);

        Button b1 = makeButton(create);
        Button b2 = makeButton(myCargo);
        Button b3 = makeButton(contact);
        Button b4 = makeButton(back);

        layout.addView(b1);
        layout.addView(b2);
        layout.addView(b3);
        layout.addView(b4);

        b1.setOnClickListener(v ->
            showMessage(create)
        );

        b2.setOnClickListener(v ->
            showMessage(myCargo)
        );

        b3.setOnClickListener(v ->
            showMessage(contact)
        );

        b4.setOnClickListener(v ->
            showLanguages()
        );

        setContentView(layout);
    }

    void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
