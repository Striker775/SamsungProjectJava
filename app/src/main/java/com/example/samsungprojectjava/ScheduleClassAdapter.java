package com.example.samsungprojectjava;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class ScheduleClassAdapter extends RecyclerView.Adapter<ScheduleClassAdapter.ViewHolder> {

    private List<ScheduleClass> mData;
    private LayoutInflater mInflater;
    public ViewHolder holder;


    ScheduleClassAdapter(Context context, List<ScheduleClass> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.schedule_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        this.holder = holder;
        ScheduleClass obj = mData.get(position);
        holder.id = obj.id;
        holder.TextName.setText(obj.name);
        holder.TextImportance.setText(String.format("%d", obj.importance));
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(obj.time);

        int mYear = calendar.get(Calendar.YEAR);
        int mMonth = calendar.get(Calendar.MONTH) + 1;
        int mDay = calendar.get(Calendar.DAY_OF_MONTH);
        int mHour = calendar.get(Calendar.HOUR) + 12 * calendar.get(Calendar.AM_PM);
        int mMinute = calendar.get(Calendar.MINUTE);
        if (System.currentTimeMillis() > obj.time)
            holder.TextTime.setTextColor(Color.RED);
        holder.TextTime.setText(String.format(Locale.ENGLISH, "%02d.%02d.%02d %02d:%02d", mDay, mMonth, mYear % 100, mHour, mMinute));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView TextName, TextTime, TextImportance;
        long id = -1;
        ViewHolder(View itemView) {
            super(itemView);
            TextName = itemView.findViewById(R.id.event_name);
            TextTime = itemView.findViewById(R.id.event_date);
            TextImportance = itemView.findViewById(R.id.event_importance);
            ImageButton btn_delete = itemView.findViewById(R.id.btn_delete_event);
            btn_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LaaActivity m = (LaaActivity) itemView.getContext();
                    m.mDBConnector.delete(id);
                    m.createService();
                    m.findViewById(R.id.button_schedule).callOnClick();
                }
            });
        }
    }

    ScheduleClass getItem(int id) {
        return mData.get(id);
    }
}
