package com.example.samsungprojectjava;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;
import java.util.Calendar;

public class AddEventFragment extends Fragment {
    String date_time = "";
    int mYear;
    int mMonth;
    int mDay;
    EditText edtTime;
    int mHour;
    int mMinute;
    LaaActivity m;
    public Calendar datetime_string_to_cal(String in){
        Calendar cal = Calendar.getInstance();
        cal.set(Integer.valueOf(in.substring(6,10)), Integer.valueOf(in.substring(3,5)) - 1, Integer.valueOf(in.substring(0,2)),
                Integer.valueOf(in.substring(11,13)), Integer.valueOf(in.substring(14,16)), 0);
        return cal;
    }
    public String datetime_cal_to_string(Calendar cal){
        return String.format("%02d.%02d.%04d %02d:%02d",cal.get(Calendar.DAY_OF_MONTH),cal.get(Calendar.MONTH) + 1,cal.get(Calendar.YEAR)
                ,cal.get(Calendar.HOUR) + cal.get(Calendar.AM_PM) * 12,cal.get(Calendar.MINUTE));
    }

    private void timePicker(){
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,int minute) {
                        mHour = hourOfDay;
                        mMinute = minute;
                        edtTime.setText(date_time + String.format(" %02d:%02d",mHour, mMinute));
                        date_time = "";
                    }
                }, mHour, mMinute, true);
        timePickerDialog.show();
    }
    private void datePicker(){
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,int monthOfYear, int dayOfMonth) {

                        date_time += String.format("%02d.%02d.%04d",dayOfMonth, monthOfYear + 1, year);
                        timePicker();
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View res_view = inflater.inflate(R.layout.fragment_add_event, container, false);
        m = (LaaActivity)getActivity();
        Button btn_accept = res_view.findViewById(R.id.btn_commit);
        EditText edtName = res_view.findViewById(R.id.edt_name);
        edtTime = res_view.findViewById(R.id.edt_time);
        edtTime.setInputType(InputType.TYPE_NULL);
        EditText edtImportance = res_view.findViewById(R.id.edt_importance);
        edtTime.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(MotionEvent.ACTION_UP == motionEvent.getAction()) {
                    date_time = "";
                    datePicker();
                }
                return false;
            }
        });
        btn_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(edtImportance.getText().toString().equals("1") || edtImportance.getText().toString().equals("2") || edtImportance.getText().toString().equals("3"))) {
                    edtImportance.setText("");
                    Toast.makeText(m, "Неверно задан уровень важности", Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    m.mDBConnector.insert(edtName.getText().toString(), datetime_string_to_cal(edtTime.getText().toString()).getTimeInMillis(), Integer.valueOf(edtImportance.getText().toString()), m.login, m.longitude, m.latitude, m.set_coords, 0);
                    final FragmentTransaction ft = m.getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.fragment_a, new HomeFragment(), "TheFragment");
                    ft.commit();
                    Toast.makeText(m, "Событие успешно создано", Toast.LENGTH_SHORT).show();

                    m.createService();
                }
                catch (java.lang.NumberFormatException exception) {
                    Toast.makeText(m, "Проверьте правильность введённых данных", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return res_view;
    }
}