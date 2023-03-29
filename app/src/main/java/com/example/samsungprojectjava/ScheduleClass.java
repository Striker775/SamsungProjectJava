package com.example.samsungprojectjava;

import android.os.Parcel;
import android.os.Parcelable;
//Класс расписания
public class ScheduleClass implements Parcelable {
    long id;
    String name;
    long time;
    int importance;
    String login;
    boolean coords = false;
    double longitude = 0f;
    double latitude = 0f;
    int mentioned;
    ScheduleClass(long id, String name, long time, int importance, String login, int mentioned){
        this.id = id;
        this.name = name;
        this.time = time;
        this.importance = importance;
        this.login = login;
        this.mentioned = mentioned;
    }
    ScheduleClass(long id, String name, long time, int importance, String login, int mentioned, double longitude, double latitude, int coords){
        this.id = id;
        this.name = name;
        this.time = time;
        this.importance = importance;
        this.login = login;
        this.mentioned = mentioned;
        this.longitude = longitude;
        this.latitude = latitude;
        this.coords = coords == 1;
    }
    public void setCoords(double lon, double lat){
        this.longitude = lon;
        this.latitude = lat;
        this.coords = true;
    }
    public int describeContents() {
        return 0;
    }
    public void writeToParcel(Parcel out, int flags) {
        out.writeLong(id);
        out.writeString(name);
        out.writeLong(time);
        out.writeInt(importance);
        out.writeString(login);
        out.writeInt(mentioned);
        out.writeDouble(longitude);
        out.writeDouble(latitude);
        out.writeInt(coords ? 1 : 0);
    }
    public static final Parcelable.Creator<ScheduleClass> CREATOR
            = new Parcelable.Creator<ScheduleClass>() {
        public ScheduleClass createFromParcel(Parcel in) {
            return new ScheduleClass(in.readLong(), in.readString(), in.readLong(), in.readInt(), in.readString(), in.readInt(), in.readDouble(), in.readDouble(), in.readInt());
        }

        public ScheduleClass[] newArray(int size) {
            return new ScheduleClass[size];
        }
    };
}
