package com.example.samsungprojectjava;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import java.util.ArrayList;


public class LaaActivity extends AppCompatActivity {
    String login = "";
    int h_tasks;
    int m_tasks;
    int l_tasks;
    int remember;
    //Можно лучше [!!!]
    double longitude = 0f;
    double latitude = 0f;
    boolean set_coords = false;
    DBScheduleClass mDBConnector;
    Context mContext;
    ImageButton btn_home, btn_map, btn_settings, btn_schedule;
    static String CHANNEL_ID = "111";
    public void createService(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "schedule_channel";
            String description = "to_provide_notifications";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
        Intent i = new Intent(getApplicationContext(), NotificationService.class);
        ArrayList<ScheduleClass> objects = mDBConnector.selectAll();
        i.putExtra("array", objects);
        i.putExtra("login", login);
        i.putExtra("h_tasks", h_tasks);
        i.putExtra("m_tasks", m_tasks);
        i.putExtra("l_tasks", l_tasks);
        startService(i);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laa);
        login = getIntent().getStringExtra("login");
        remember = getIntent().getIntExtra("remember", 0);

        h_tasks = getIntent().getIntExtra("h_tasks", 120);
        m_tasks = getIntent().getIntExtra("m_tasks", 60);
        l_tasks = getIntent().getIntExtra("l_tasks", 30);
        mContext = this;
        mDBConnector = new DBScheduleClass(this, login);
        createService();
        btn_home = findViewById(R.id.button_home);
        btn_map = findViewById(R.id.button_map);
        btn_settings = findViewById(R.id.button_settings);
        btn_schedule = findViewById(R.id.button_schedule);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment fragment = new HomeFragment();
        ft.replace(R.id.fragment_a, fragment, "TheFragment");
        ft.commit();
        btn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.fragment_a, new HomeFragment(), "TheFragment");
                ft.commit();
            }
        });
        btn_schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ScheduleFragment new_fragment = new ScheduleFragment();
                ArrayList<ScheduleClass> objects = mDBConnector.selectAll();
                Bundle extras = new Bundle();
                extras.putParcelableArrayList("arraylist", objects);
                new_fragment.setArguments(extras);
                final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.fragment_a, new_fragment, "TheFragment");
                ft.commit();
            }
        });
        btn_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MapFragment new_fragment = new MapFragment();
                set_coords = false;
                ArrayList<ScheduleClass> objects = mDBConnector.selectAll();
                Bundle extras = new Bundle();
                extras.putParcelableArrayList("arraylist", objects);
                new_fragment.setArguments(extras);
                final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.fragment_a, new_fragment, "TheFragment");
                ft.commit();
            }
        });
        btn_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.fragment_a, new SettingsFragment(), "TheFragment");
                ft.commit();
            }
        });
        /*
        (готово) Вход в аккаунт - локальная БД1 - логин и пароль как глобальные переменные
        (готово) Список запланированных действий - в БД2 - выгрузка в список
        (готово) Фрагменты (кнопка сверху в меню для перехода между фрагментами):
        (готово) - меню-переключение, рабочая область;
        (готово) - карта, запланированные события
        (готово) Добавить ссылку: о программе (правила пользования Yandex Maps) https://yandex.ru/legal/maps_termsofuse
        (готово) Пароль и логин без галочки в локальной бд затираются, с галочкой остаются и вставляются при следующем входе
        (готово) Связь с Яндекс-Картами - Интернет; метки на карте (брать координаты в бд с расписанием)
        (готово) Настройка пользовательская - за сколько времени (в бд у пользователя) до события напоминать
        (готово) Напоминалки (пуш-уведомления)
        (готово) Firebase аутентификация
        (в будущем) Связать таблицы баз данных в одной бд(оптимизация: логин/пароль)
        (в будущем) Кнопка синхронизации расписания с БД FireBase
        (не нужно [есть бд]) Кнопка выгрузить данные о планировании в bin-файл
         */
    }
}