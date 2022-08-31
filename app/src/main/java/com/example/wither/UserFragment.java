import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.wither.CategoryFragment;
import com.example.wither.ReportFragment;

/**import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.wither.CategoryFragment;
import com.example.wither.ReportFragment;

/**package com.example.wither;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ReportFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.wither.CategoryFragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
   /** public class UserFragment extends Fragment implements

    public static UserFragment newInstance() {
        return new UserFragment();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.UserFragment);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fv = inflater.inflate(R.layout.fragment_user, container,
                false);
        Button category, report, settings;
        category = (Button) fv.findViewById(R.id.category_btn);
        category.setOnClickListener((View.OnClickListener) this);
        report = (Button) fv.findViewById(R.id.report_btn);
        report.setOnClickListener((View.OnClickListener) this);
        settings = (Button) fv.findViewById(R.id.settings);
        settings.setOnClickListener((View.OnClickListener) this);

        return fv;
    }

    @Override
    public void Onclick(View view) {

        Fragment fg;
        switch(view.getId()) {
            case R.id.category_btn:
                fg = CategoryFragment.newInstance();
                setUserFragment(fg);
                break;
            case R.id.report_btn:
                fg = ReportFragment.newInstance();
                setUserFragment(fg);
                break;
        }
    }

    private void setUserFragment(Fragment user) {
        FragmentTransaction userFt = getUserFragmentManager().beginTransaction();

        if(!user.isAdded()) {
            userFt.replace(R.id.user_fragment_container, user);
            userFt.addToBackStack(null);
            userFt.commit();
        }
    }
}**/