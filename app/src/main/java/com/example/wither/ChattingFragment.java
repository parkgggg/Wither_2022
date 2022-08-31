package com.example.wither;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.fasterxml.jackson.core.json.DupDetector;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class ChattingFragment extends Fragment {
    private ListView listView;
    private ArrayAdapter<String> arrayAdapter;
    private ArrayList<String> arr_roomList = new ArrayList<>();
    private String str_room;
    private DatabaseReference reference = FirebaseDatabase.getInstance("https://wither-chatting-default-rtdb.firebaseio.com/")
            .getReference().getRoot();

    int true_in_username = 0;

    ArrayList<MakeDatabase> GET_database_set = new ArrayList<MakeDatabase>();

    MakeDatabase database;

    String meeting_name;

    Map<String, Object> map = new HashMap<String, Object>();
    public ChattingFragment() {
        // Required empty public constructor

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        try{
            GET_database_set = MakeDatabase.getGET_dabase();

            // 채팅방 리스트
            listView = (ListView) view.findViewById(R.id.list);
            arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, arr_roomList);
            listView.setAdapter(arrayAdapter);

//            meeting_name = "asldfjalskdfjalksdfjaklsf";
//            map.put(meeting_name, "");
//            reference.updateChildren(map);

            // 여기서부터 firebase에서 방 id(String) 가져와서 listview로 보여주는 코드
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    Set<String> set = new HashSet<String>();
                    Iterator i = dataSnapshot.getChildren().iterator();

                    while (i.hasNext()) {
                        try{

                            DataSnapshot firebase_chatting_name = ((DataSnapshot) i.next());
                            String a = firebase_chatting_name.getKey();
                            String[] split_a = a.split("///");

                            String b =  firebase_chatting_name.toString();
                            b = b.replaceAll("userid=","////");
                            String[] split_b = b.split("////");
                            for(int j = 1 ; j < split_b.length;j++){
                                String[] split_b_split = split_b[j].split(",");
                                a = split_b_split[0];
                                if(a.equals(MakeDatabase.getUsername())){
                                    true_in_username = 1;
                                    break;
                                }
                            }

                            if(true_in_username == 1){
                                set.add(split_a[0]);
                            }
                            true_in_username = 0;

                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }

                    arr_roomList.clear();
                    arr_roomList.addAll(set);

                    arrayAdapter.notifyDataSetChanged();
                }

                @Override public void onCancelled(DatabaseError databaseError) {

                }
            });

        }catch(Exception e){
            e.printStackTrace();
        }
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                try{
                    // listview의 원소를 클릭하면 해당 원소의 textview 그대로를 받아온다.
                    // 그 textview와 meeting_name이 일치하는 방을 찾아서 채팅방에 들어간다.
                    String room_name = ((TextView) view).getText().toString();
                    for(int j = 0 ; j < GET_database_set.size();j++){
                        if(GET_database_set.get(j).getMeeting_name().equals(room_name)){
                            database = GET_database_set.get(j);
                            break;
                        }
                    }

                    Intent intent = new Intent(getActivity(),ChattingActivity.class);
                    intent.putExtra("chatting_room_string",database.toString());
                    intent.putExtra("chatting_room_name_string",database.getMeeting_name());
                    intent.putExtra("chatting_person_num_int",database.getMeeting_person());
                    intent.putExtra("username",MakeDatabase.getUsername());
                    startActivity(intent);
                }catch(Exception e){
                    e.printStackTrace();
                }

            }
        });
        return view;
    }
}