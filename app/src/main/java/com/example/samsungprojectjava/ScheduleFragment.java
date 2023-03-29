package com.example.samsungprojectjava;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
//Фрагмент расписания
public class ScheduleFragment extends Fragment {
    RecyclerView recyclerView;
    ArrayList<ScheduleClass> objects = new ArrayList<ScheduleClass>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragment_view = inflater.inflate(R.layout.fragment_schedule, container, false);

        Bundle extras = getArguments();
        if (extras != null) {
            objects = extras.getParcelableArrayList("arraylist");
            LaaActivity m = (LaaActivity)getActivity();
            for (int i = 0; i < objects.size(); i++)
                if (!objects.get(i).login.equals(m.login)) {
                    objects.remove(i);
                    i--;
                }
        }
        recyclerView = (RecyclerView) fragment_view.findViewById(R.id.rec_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        ScheduleClassAdapter adapter = new ScheduleClassAdapter(getActivity(), objects);
        recyclerView.setAdapter(adapter);
        return fragment_view;
    }
}