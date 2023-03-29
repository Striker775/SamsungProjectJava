package com.example.samsungprojectjava;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
//Фрагмент домашней страницы
public class HomeFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        Button btn_event_add = view.findViewById(R.id.btn_add_event);
        Button btn_event_add_map = view.findViewById(R.id.btn_add_event_map);
        Button btn_logout = view.findViewById(R.id.btn_logout);
        //Добавление события без карты
        btn_event_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LaaActivity m = (LaaActivity) getActivity();
                m.set_coords = false;
                final FragmentTransaction ft = m.getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.fragment_a, new AddEventFragment(), "TheFragment");
                ft.commit();
            }
        });
        //Добавление события на карте
        btn_event_add_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LaaActivity m = (LaaActivity) getActivity();
                m.set_coords = true;
                MapFragment new_fragment = new MapFragment();
                ArrayList<ScheduleClass> objects = m.mDBConnector.selectAll();
                Bundle extras = new Bundle();
                extras.putBoolean("set_coords", true);
                extras.putParcelableArrayList("arraylist", objects);
                new_fragment.setArguments(extras);
                final FragmentTransaction ft = m.getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.fragment_a, new_fragment, "TheFragment");
                ft.commit();
            }
        });
        //Выход из аккаунта
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LaaActivity m = (LaaActivity) getActivity();
                m.login = "";
                Intent i;
                i = new Intent(m, MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });
        return view;
    }
}