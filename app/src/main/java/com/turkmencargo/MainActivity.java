
package com.turkmencargo;

import android.Manifest;
import android.app.Activity;
import android.os.Bundle;
import android.content.*;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.location.*;
import android.net.Uri;
import android.view.Gravity;
import android.widget.*;
import org.json.*;

import java.util.Locale;

public class MainActivity extends Activity {

    int NAVY = Color.rgb(10,22,39);
    int GOLD = Color.rgb(224,177,83);
    int PANEL = Color.rgb(25,43,63);

    LinearLayout page;
    SharedPreferences prefs;
    String lang = "tr";
    String gps = "";

    @Override
    public void onCreate(Bundle b) {
        super.onCreate(b);
        getWindow().setStatusBarColor(NAVY);
        getWindow().setNavigationBarColor(NAVY);

        prefs = getSharedPreferences("cargo", MODE_PRIVATE);
        lang = prefs.getString("lang","tr");

        home();
    }

    String tr(String tk,String ru,String tr) {
        if(lang.equals("tk")) return tk;
        if(lang.equals("ru")) return ru;
        return tr;
    }

    GradientDrawable bg(int color) {
        GradientDrawable d = new GradientDrawable();
        d.setColor(color);
        d.setCornerRadius(22);
        return d;
    }

    void start(String title) {
        ScrollView scroll = new ScrollView(this);

        page = new LinearLayout(this);
        page.setOrientation(1);
        page.setPadding(20,20,20,30);
        page.setBackgroundColor(NAVY);

        scroll.addView(page);
        setContentView(scroll);

        TextView brand = text("TURKMEN CARGO",24,GOLD);
        brand.setTypeface(null,Typeface.BOLD);
        page.addView(brand);

        if(title != null) {
            page.addView(text(title,24,Color.WHITE));
        }
    }

    TextView text(String s,int size,int color) {
        TextView t = new TextView(this);
        t.setText(s);
        t.setTextSize(size);
        t.setTextColor(color);
        t.setPadding(5,12,5,12);
        return t;
    }

    void label(String s) {
        page.addView(text(s,14,GOLD));
    }

    EditText input(String hint,String value) {
        EditText e = new EditText(this);
        e.setHint(hint);
        e.setText(value);
        e.setTextColor(Color.WHITE);
        e.setHintTextColor(Color.LTGRAY);
        e.setPadding(15,10,15,10);
        e.setBackground(bg(PANEL));
        page.addView(e);
        return e;
    }

    void button(String title,Runnable action) {
        Button b = new Button(this);
        b.setText(title);
        b.setAllCaps(false);
        b.setTextColor(NAVY);
        b.setTypeface(null,Typeface.BOLD);
        b.setBackground(bg(GOLD));

        LinearLayout.LayoutParams p =
            new LinearLayout.LayoutParams(-1,-2);
        p.topMargin = 9;
        page.addView(b,p);

        b.setOnClickListener(v -> action.run());
    }

    void card(String title,String detail) {
        LinearLayout box = new LinearLayout(this);
        box.setOrientation(1);
        box.setPadding(15,10,15,10);
        box.setBackground(bg(PANEL));

        box.addView(text(title,17,GOLD));
        box.addView(text(detail,13,Color.WHITE));

        LinearLayout.LayoutParams p =
            new LinearLayout.LayoutParams(-1,-2);
        p.topMargin = 9;
        page.addView(box,p);
    }

    void home() {
        start(tr("Baş sahypa","Главная","Ana Sayfa"));

        card("🚛 TURKMEN CARGO",
            tr("Ýüküňiz howpsuz ýolda",
            "Ваш груз в надёжных руках",
            "Yükünüz güvenli yolda"));

        card("📦",tr("Ýük hyzmatlary",
            "Грузовые услуги","Nakliye hizmetleri"));

        button("📦  "+tr("Ýük döretmek",
            "Создать груз","Yük oluştur"),this::createLoad);

        button("📋  "+tr("Ýüklerim",
            "Мои грузы","Yüklerim"),this::loads);

        button("🚚  "+tr("Daşaýjy",
            "Перевозчик","Taşıyıcı"),this::carrier);

        button("🧮  "+tr("Bahany hasapla",
            "Рассчитать цену","Fiyat hesapla"),this::calculator);

        button("📍  "+tr("Konum",
            "Местоположение","Konum"),this::location);

        button("⚙  "+tr("Sazlamalar",
            "Настройки","Ayarlar"),this::settings);
    }

    JSONArray getLoads() {
        try {
            return new JSONArray(
                prefs.getString("loads","[]"));
        } catch(Exception e) {
            return new JSONArray();
        }
    }

    void createLoad() {
        start(tr("Täze ýük","Новый груз","Yeni yük"));

        label(tr("Nireden","Откуда","Nereden"));
        EditText from = input("Aşgabat","");

        label(tr("Nirä","Куда","Nereye"));
        EditText to = input("Türkmenabat","");

        label(tr("Aralyk (km)","Расстояние (км)","Mesafe (km)"));
        EditText km = input("100","");

        label(tr("Agram (kg)","Вес (кг)","Ağırlık (kg)"));
        EditText weight = input("1000","");

        label(tr("Bahasy (TMT)","Цена (TMT)","Fiyat (TMT)"));
        EditText price = input("TMT","");

        Spinner payment = new Spinner(this);
        String[] methods = {
            tr("Nagt","Наличные","Nakit"),
            tr("Bank kartasy","Банковская карта","Banka kartı")
        };

        payment.setAdapter(new ArrayAdapter<String>(
            this,android.R.layout.simple_spinner_dropdown_item,
            methods));

        page.addView(payment);

        button(tr("Bahany hasapla",
            "Рассчитать цену","Fiyatı hesapla"),() -> {
            try {
                double distance =
                    Double.parseDouble(km.getText().toString());

                price.setText(String.format(
                    Locale.US,"%.2f",distance * 3.0));

            } catch(Exception e) {
                msg(tr("Aralygy giriziň",
                    "Введите расстояние","Mesafeyi girin"));
            }
        });

        button(tr("Ýüki sakla","Сохранить груз",
            "Yükü kaydet"),() -> {
            if(from.length()==0 || to.length()==0 ||
                price.length()==0) {
                msg(tr("Maglumatlary dolduryň",
                    "Заполните данные","Bilgileri doldurun"));
                return;
            }

            try {
                JSONObject item = new JSONObject();
                item.put("from",from.getText().toString());
                item.put("to",to.getText().toString());
                item.put("km",km.getText().toString());
                item.put("weight",weight.getText().toString());
                item.put("price",price.getText().toString());
                item.put("payment",payment.getSelectedItem().toString());

                JSONArray a = getLoads();
                a.put(item);

                prefs.edit().putString("loads",a.toString()).apply();

                msg(tr("Ýük saklandy","Груз сохранён",
                    "Yük kaydedildi"));
                loads();

            } catch(Exception e) {
                msg("Error");
            }
        });

        button("← "+tr("Baş sahypa","Главная","Ana sayfa"),
            this::home);
    }

    void loads() {
        start(tr("Ýüklerim","Мои грузы","Yüklerim"));

        JSONArray a = getLoads();

        if(a.length()==0) {
            card("📦",tr("Heniz ýük ýok",
                "Пока нет грузов","Henüz yük yok"));
        }

        for(int i=0;i<a.length();i++) {
            JSONObject o = a.optJSONObject(i);
            if(o==null) continue;

            card(o.optString("from")+" → "+o.optString("to"),
                o.optString("price")+" TMT  |  "+
                o.optString("km")+" km");
        }

        button("+ "+tr("Täze ýük","Новый груз","Yeni yük"),
            this::createLoad);

        button("← "+tr("Baş sahypa","Главная","Ana sayfa"),
            this::home);
    }

    void carrier() {
        start(tr("Daşaýjy profili",
            "Профиль перевозчика","Taşıyıcı profili"));

        label(tr("Ady","Имя","Ad soyad"));
        EditText name = input("Ad soyad",
            prefs.getString("name",""));

        label(tr("Telefon","Телефон","Telefon"));
        EditText phone = input("+993",
            prefs.getString("phone",""));

        label(tr("Kompaniýa","Компания","Şirket"));
        EditText company = input("Şirket",
            prefs.getString("company",""));

        label(tr("Ulag görnüşi","Тип транспорта","Araç türü"));
        EditText vehicle = input("Kamyon",
            prefs.getString("vehicle",""));

        label(tr("Plaka","Госномер","Plaka"));
        EditText plate = input("TM 00 00 AA",
            prefs.getString("plate",""));

        label(tr("Göterijilik (kg)",
            "Грузоподъёмность (кг)","Taşıma kapasitesi (kg)"));
        EditText capacity = input("kg",
            prefs.getString("capacity",""));

        label(tr("Resminama maglumatlary",
            "Данные документов","Belge bilgileri"));
        EditText docs = input("Belge belgileri",
            prefs.getString("docs",""));

        button(tr("Profili sakla",
            "Сохранить профиль","Profili kaydet"),() -> {
            prefs.edit()
                .putString("name",name.getText().toString())
                .putString("phone",phone.getText().toString())
                .putString("company",company.getText().toString())
                .putString("vehicle",vehicle.getText().toString())
                .putString("plate",plate.getText().toString())
                .putString("capacity",capacity.getText().toString())
                .putString("docs",docs.getText().toString())
                .apply();

            msg(tr("Profil saklandy",
                "Профиль сохранён","Profil kaydedildi"));
        });

        card("🛡",tr("Resminamalar administrator tarapyndan barlanmaly",
            "Документы должны быть проверены администратором",
            "Belgeler yönetici tarafından doğrulanmalıdır"));

        button("← "+tr("Baş sahypa","Главная","Ana sayfa"),
            this::home);
    }

    void calculator() {
        start(tr("Bahany hasaplaýjy",
            "Калькулятор цены","Fiyat hesaplayıcı"));

        label(tr("Aralyk (km)","Расстояние (км)","Mesafe (km)"));
        EditText km = input("100","");

        label(tr("1 km nyrhy (TMT)",
            "Тариф за 1 км (TMT)","1 km ücreti (TMT)"));
        EditText rate = input("3","3");

        TextView result = text("0.00 TMT",25,GOLD);
        page.addView(result);

        button(tr("Hasapla","Рассчитать","Hesapla"),() -> {
            try {
                double total =
                    Double.parseDouble(km.getText().toString()) *
                    Double.parseDouble(rate.getText().toString());

                result.setText(String.format(Locale.US,
                    "%.2f TMT\\n8%% komissiýa: %.2f TMT",
                    total,total*0.08));

            } catch(Exception e) {
                msg("Invalid number");
            }
        });

        card("ℹ",tr("3 TMT/km diňe başlangyç nusga nyrhydyr",
            "3 TMT/км — примерный тариф",
            "3 TMT/km örnek başlangıç tarifidir"));

        button("← "+tr("Baş sahypa","Главная","Ana sayfa"),
            this::home);
    }

    void location() {
        start(tr("Konum","Геолокация","Konum"));

        TextView output = text(
            gps.isEmpty() ?
            tr("Konum alynmady","Местоположение не получено",
                "Konum alınmadı") : gps,
            16,Color.WHITE);

        page.addView(output);

        button(tr("GPS konumyny al",
            "Получить GPS","GPS konumunu al"),() -> {
            if(checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

                requestPermissions(new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                },41);

                return;
            }

            try {
                LocationManager lm =
                    (LocationManager)getSystemService(LOCATION_SERVICE);

                Location l = lm.getLastKnownLocation(
                    LocationManager.GPS_PROVIDER);

                if(l==null) {
                    l=lm.getLastKnownLocation(
                        LocationManager.NETWORK_PROVIDER);
                }

                if(l!=null) {
                    gps=String.format(Locale.US,
                        "%.5f, %.5f",
                        l.getLatitude(),l.getLongitude());
                    output.setText(gps);
                } else {
                    output.setText(tr("GPS maglumat tapmady",
                        "GPS не нашёл позицию",
                        "GPS konum bulamadı"));
                }

            } catch(Exception e) {
                msg("GPS error");
            }
        });

        button(tr("Kartada aç","Открыть карту",
            "Haritada aç"),() -> {
            if(gps.isEmpty()) {
                msg(tr("Ilki GPS alyň","Сначала получите GPS",
                    "Önce GPS konumu alın"));
                return;
            }

            Intent map = new Intent(Intent.ACTION_VIEW,
                Uri.parse("geo:"+gps+"?q="+gps));

            startActivity(map);
        });

        button("← "+tr("Baş sahypa","Главная","Ana sayfa"),
            this::home);
    }

    void settings() {
        start(tr("Sazlamalar","Настройки","Ayarlar"));

        sectionTitle(tr("Dil","Язык","Dil"));

        button("Türkmen dili",() -> setLang("tk"));
        button("Русский язык",() -> setLang("ru"));
        button("Türkçe",() -> setLang("tr"));

        card("💳 "+tr("Bank kartasy",
            "Банковская карта","Banka kartı"),
            tr("Hakyky töleg üçin töleg hyzmaty birikdirilmeli",
            "Для оплаты нужно подключить платёжный сервис",
            "Gerçek kart ödemesi için ödeme sağlayıcısı bağlanmalı"));

        card("💵 "+tr("Nagt töleg",
            "Наличные","Nakit ödeme"),
            tr("Daşaýjy bilen göni ylalaşylýar",
            "Согласуется с перевозчиком",
            "Taşıyıcıyla doğrudan kararlaştırılır"));

        card("8% "+tr("Komissiýa",
            "Комиссия","Komisyon"),
            tr("Her tamamlanan tölegden 8%. Awtomatiki geçirim üçin serwer we töleg API gerek.",
            "8% с завершённой оплаты. Для перевода нужен сервер и платёжный API.",
            "Tamamlanan ödemeden %8. Otomatik aktarım için sunucu ve ödeme API'si gerekir."));

        button("← "+tr("Baş sahypa","Главная","Ana sayfa"),
            this::home);
    }

    void sectionTitle(String s) {
        page.addView(text(s,17,GOLD));
    }

    void setLang(String s) {
        lang=s;
        prefs.edit().putString("lang",s).apply();
        home();
    }

    void msg(String s) {
        Toast.makeText(this,s,Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBackPressed() {
        home();
    }
               }
            
