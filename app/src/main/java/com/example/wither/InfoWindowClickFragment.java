package com.example.wither;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class InfoWindowClickFragment extends Fragment{
    MakeDatabase database ;
    private SharedViewModel sharedViewModel_to_InfoWindowClickFragment;

    String username;
    public InfoWindowClickFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_info_window_click, container, false);

        Bundle bundle = getArguments();
        if(bundle!=null){
            username = bundle.getString("username");
        }

        // HomeFragment에서 정보창 띄운 마커의 데이터베이스를 가져와서 정보창을 누르면 정보 뜨는 fragment 보이게 하기
        sharedViewModel_to_InfoWindowClickFragment = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        sharedViewModel_to_InfoWindowClickFragment.getLiveData().observe(getViewLifecycleOwner(), new Observer<MakeDatabase>() {
            @Override
            public void onChanged(MakeDatabase database) {
                setDatabase(database);
                getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                Button category_btn = (Button) view.findViewById(R.id.info_category_btn);
                TextView meeting_name = (TextView) view.findViewById(R.id.info_creat_name);
                TextView meeting_person_num = (TextView) view.findViewById(R.id.info_create_person_num);
                TextView meeting_for_friend_text = (TextView)view.findViewById(R.id.info_create_for_friend);
                TextView date_text = (TextView)view.findViewById(R.id.info_date_text);

                category_btn.setText(getDatabase().getMeeting_category());
                date_text.setText(date());
                meeting_name.setText(getDatabase().getMeeting_name());
                meeting_person_num.setText(Integer.toString(getDatabase().getMeeting_person()));
                meeting_for_friend_text.setText(getDatabase().getText_for_meeting_friend());

            }
        });

        // 취소 버튼 눌렀을 때
        Button make_button = view.findViewById(R.id.info_create_cancel_btn);
        make_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                FragmentManager fm = getParentFragmentManager();
                fm.beginTransaction().remove(fm.findFragmentById(R.id.homeFragmentFrame)).commit();
            }
        });

//         참여 버튼 눌렀을 때
        Button meet_button = view.findViewById(R.id.info_create_meet_btn);
        meet_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                Bundle bundle_1 = new Bundle();
                bundle_1.putString("meeting_name",database.getMeeting_name());

                ChattingFragment chattingFragment = new ChattingFragment();
                chattingFragment.setArguments(bundle_1);

                try{
                    Intent intent = new Intent(getActivity(),ChattingActivity.class);
//                    intent.putExtra("database",getDatabase());
                    intent.putExtra("chatting_room_string",database.toString());
                    intent.putExtra("chatting_room_name_string",database.getMeeting_name());
                    intent.putExtra("chatting_person_num_int",database.getMeeting_person());
                    intent.putExtra("username",username);

                    startActivity(intent);
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        });

        return view;
    }

    public MakeDatabase getDatabase() {
        return database;
    }

    public void setDatabase(MakeDatabase database) {
        this.database = database;
    }

    public String date(){
        return database.getYear() + "." + database.getMonth() + "." + database.getDay();
    }

    void showDialog() {
        AlertDialog.Builder msgBuilder = new AlertDialog.Builder(getActivity())
                .setTitle("앱 끈다?")
                .setMessage("진짜 끈다?")
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getActivity(), "확인", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getActivity(), "취소", Toast.LENGTH_SHORT).show();
                    }
                });
        AlertDialog msgDlg = msgBuilder.create();
        msgDlg.show();
    }
}

