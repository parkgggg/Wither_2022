package com.example.wither;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;


public class SettingsFragment extends Fragment {
    public static SettingsFragment newInstance() {
            return new SettingsFragment();
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
                savedInstanceState) {
            View fv = inflater.inflate(R.layout.user_report, container, false);

            return fv;
        }
    }
