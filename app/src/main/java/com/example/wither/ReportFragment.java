package com.example.wither;

import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

public class ReportFragment extends Fragment {
    public static ReportFragment newInstance() {
        return new ReportFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
                             savedInstanceState) {
        View fv = inflater.inflate(R.layout.user_report, container, false);

        return fv;
    }
}
