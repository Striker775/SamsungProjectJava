package com.example.samsungprojectjava;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yandex.mapkit.Animation;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.map.CameraPosition;
import com.yandex.mapkit.map.InputListener;
import com.yandex.mapkit.map.Map;
import com.yandex.mapkit.map.PlacemarkMapObject;
import com.yandex.mapkit.mapview.MapView;
import com.yandex.runtime.image.ImageProvider;

import java.util.ArrayList;

public class MapFragment extends Fragment {
    MapView mapview;
    ArrayList<ScheduleClass> objects = new ArrayList<ScheduleClass>();
    public static Bitmap getBitmapFromVectorDrawable(Context context, int drawableId) {
        Drawable drawable = ContextCompat.getDrawable(context, drawableId);

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }
    public Bitmap drawSimpleBitmap(String number) {
        int picSize = 64;
        Bitmap bitmap = Bitmap.createBitmap(picSize, picSize, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(picSize / 2, picSize / 2, picSize / 2, paint);
        paint.setColor(Color.WHITE);
        paint.setAntiAlias(true);
        paint.setTextSize(16);
        paint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(number, picSize / 2,
                picSize / 2 - ((paint.descent() + paint.ascent()) / 2), paint);
        return bitmap;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View res_view = inflater.inflate(R.layout.fragment_map, container, false);
        mapview = (MapView)res_view.findViewById(R.id.mapview);
        TextView hint_map = res_view.findViewById(R.id.hint_map);
        hint_map.setVisibility(((LaaActivity)getActivity()).set_coords ? View.VISIBLE : View.INVISIBLE);
        boolean start_point_found = false;
        Point start_point = null;
        Bundle extras = getArguments();
        if (extras != null) {
            objects = extras.getParcelableArrayList("arraylist");
            LaaActivity m = (LaaActivity)getActivity();
            for (int i = 0; i < objects.size(); i++)
                if (objects.get(i).login.equals(m.login) && objects.get(i).coords) {
                    if (!start_point_found) {
                        start_point_found = true;
                        start_point = new Point(objects.get(i).latitude, objects.get(i).longitude);
                    }
                    Point point = new Point(objects.get(i).latitude, objects.get(i).longitude);
                    PlacemarkMapObject mark = mapview.getMap().getMapObjects().addPlacemark(point);
                    mark.setOpacity(1f);
                    //mark.setIcon(ImageProvider.fromBitmap(getBitmapFromVectorDrawable(getContext(), R.drawable.ic_baseline_location_on_24)));
                    mark.setIcon(ImageProvider.fromBitmap(drawSimpleBitmap(objects.get(i).name)));
                }
        }
        if (!start_point_found)
            start_point  = new Point(55.386799, 43.814133);
        mapview.getMap().addInputListener(new InputListener() {
            @Override
            public void onMapTap(@NonNull Map map, @NonNull Point point) {
                LaaActivity m = (LaaActivity)getActivity();
                if (m.set_coords){
                    m.latitude = point.getLatitude();
                    m.longitude = point.getLongitude();
                    final FragmentTransaction ft = m.getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.fragment_a, new AddEventFragment(), "TheFragment");
                    ft.commit();
                }
            }

            @Override
            public void onMapLongTap(@NonNull Map map, @NonNull Point point) {

            }
        });
        mapview.getMap().move(
                new CameraPosition(start_point, start_point_found ? 15.0f : 12.0f, 0.0f, 0.0f),
                new Animation(Animation.Type.SMOOTH, 0),
                null);
        return res_view;
    }
    @Override
    public void onStart(){
        mapview.onStart();
        MapKitFactory.getInstance().onStart();
        super.onStart();
    }
    @Override
    public void onStop(){
        mapview.onStop();
        MapKitFactory.getInstance().onStop();
        super.onStop();
    }
}