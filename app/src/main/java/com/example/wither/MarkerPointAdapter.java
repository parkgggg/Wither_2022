package com.example.wither;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.naver.maps.map.overlay.InfoWindow;

public class MarkerPointAdapter extends InfoWindow.DefaultViewAdapter{
    private final Context mContext;
    private final ViewGroup mParent;

    MakeDatabase database ;
    public MarkerPointAdapter(@NonNull Context context, ViewGroup parent)
    {
        super(context);
        mContext = context;
        mParent = parent;
    }

    @NonNull
    @Override
    protected View getContentView(@NonNull InfoWindow infoWindow)
    {

        View view = (View) LayoutInflater.from(mContext).inflate(R.layout.marker_pointer, mParent, false);

        ImageView category_ic = (ImageView) view.findViewById(R.id.category_ic);
        TextView meeting_name = (TextView) view.findViewById(R.id.meeting_name);
        TextView meeting_person_num = (TextView) view.findViewById(R.id.meeting_person_num);

        category_ic.setImageResource(getDatabase().getResourceID());
        meeting_name.setText(getDatabase().getMeeting_name());
        meeting_person_num.setText(Integer.toString(getDatabase().getMeeting_person()));

        return view;
    }

    public MakeDatabase getDatabase() {
        return database;
    }

    public void setDatabase(MakeDatabase database) {
        this.database = database;
    }

}
