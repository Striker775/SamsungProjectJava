package com.example.samsungprojectjava;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
//Фрагмент пользовательских настроек
public class SettingsFragment extends Fragment {
    DBLoginClass mDBConnector;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View res_view = inflater.inflate(R.layout.fragment_settings, container, false);
        Button btn_confirm = res_view.findViewById(R.id.btn_confirm);
        EditText edt_l = res_view.findViewById(R.id.edt_l);
        EditText edt_m = res_view.findViewById(R.id.edt_m);
        EditText edt_h = res_view.findViewById(R.id.edt_h);
        mDBConnector = new DBLoginClass(getContext());
        LoginClass data = mDBConnector.select_by_login(((LaaActivity)getActivity()).login);
        edt_l.setHint(String.format("%d", data.low_tasks));
        edt_m.setHint(String.format("%d", data.medium_tasks));
        edt_h.setHint(String.format("%d", data.high_tasks));
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    int l = Integer.valueOf(edt_l.getText().toString());
                    int m = Integer.valueOf(edt_m.getText().toString());
                    int h = Integer.valueOf(edt_h.getText().toString());
                    LoginClass data = mDBConnector.select_by_login(((LaaActivity)getActivity()).login);
                    mDBConnector.update(new LoginClass(data.id, data.login, data.password, data.remember, l, m, h));
                    LaaActivity lact = (LaaActivity)getActivity();
                    lact.l_tasks = l;
                    lact.m_tasks = m;
                    lact.h_tasks = h;
                    Toast.makeText(getContext(), "Данные обновлены", Toast.LENGTH_SHORT).show();
                }
                catch (java.lang.NumberFormatException ex){
                    Toast.makeText(getContext(), "Неверно заполнены поля", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return res_view;
    }
}