
package com.turkmencargo;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.*;
import android.graphics.Typeface;

public class MainActivity extends Activity {

    LinearLayout layout;
    TextView title, subtitle;
    Button tk, ru, tr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setGravity(Gravity.CENTER);
        layout.setPadding(30, 30, 30, 30);
        layout.setBackgroundColor(Color.rgb(12, 25, 40));

        title = new TextView(this);
        title.setText("TURKMEN CARGO");
        title.setTextSize(32);
        title.setTypeface(null, Typeface.BOLD);
        title.setTextColor(Color.WHITE);
        title.setGravity(Gravity.CENTER);

        subtitle = new TextView(this);
        subtitle.setText("Choose your language");
        subtitle.setTextSize(19);
        subtitle.setTextColor(Color.WHITE);
        subtitle.setGravity(Gravity.CENTER);

        layout.addView(title);
        layout.addView(subtitle);

        tk = makeButton("🇹🇲 Türkmen dili");
        ru = makeButton("🇷🇺 Русский язык");
        tr = makeButton("🇹🇷 Türkçe");

        layout.addView(tk);
        layout.addView(ru);
        layout.addView(tr);

        tk.setOnClickListener(v -> setLanguage("Türkmen diline hoş geldiňiz!"));
        ru.setOnClickListener(v -> setLanguage("Добро пожаловать!"));
        tr.setOnClickListener(v -> setLanguage("Türkçe hoş geldiniz!"));

        setContentView(layout);
    }

    Button makeButton(String text) {
        Button b = new Button(this);
        b.setText(text);
        return b;
    }

    void setLanguage(String message) {
        subtitle.setText(message);
    }
}
