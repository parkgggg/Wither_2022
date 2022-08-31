package com.example.wither;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ChattingActivity extends AppCompatActivity implements View.OnClickListener{

    Toolbar toolbar;
    ActionBarDrawerToggle toggle;

    public String username;
    // 로그용 TAG
    private final String TAG = getClass().getSimpleName();

    // 채팅을 입력할 입력창과 전송 버튼
    EditText content_et;
    ImageView send_iv;
    TextView title_tv;
    // 채팅 내용을 뿌려줄 RecyclerView 와 Adapter
    RecyclerView rv;
    ChatAdapter mAdapter;

    // 채팅 방 이름
    String chatroom = "";

    // 채팅 내용을 담을 배열
    List<ChatMsgVO> msgList = new ArrayList<>();

    // FirebaseDatabase 연결용 객체들
    FirebaseDatabase database = FirebaseDatabase.getInstance("https://wither-chatting-default-rtdb.firebaseio.com/");
    DatabaseReference myRef;

    private int max_person_num;

    public static Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting);

        try{
            toolbar = this.findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
        }catch(Exception e){
            e.printStackTrace();
        }

        try{
//            Intent intent = getIntent();
//            MakeDatabase room_database = (MakeDatabase)intent.getSerializableExtra("database");
//            chatting_room_name_string = room_database.getMeeting_name();
//            chatting_room_num_string = String.valueOf(room_database.getId());

        }catch(Exception e){
            e.printStackTrace();
        }

        try{
            Intent intent = getIntent();
            username = intent.getStringExtra("username");
            String chatting_room_string = intent.getStringExtra("chatting_room_string");
            String chatting_room_name_string = intent.getStringExtra("chatting_room_name_string");
            max_person_num = intent.getExtras().getInt("chatting_person_num_int");

            getSupportActionBar().setDisplayShowTitleEnabled(true);
            getSupportActionBar().setTitle(chatting_room_name_string);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

            content_et = this.findViewById(R.id.content_et);
            send_iv = this.findViewById(R.id.send_iv);
            title_tv = this.findViewById(R.id.title_tv);

            rv = this.findViewById(R.id.rv);
            send_iv.setOnClickListener(this);
            send_iv.setColorFilter(Color.parseColor("#0099FF"));

            chatroom = chatting_room_string;
//        title_tv.setText(chatting_room_name_string);
            rv.setLayoutManager(new LinearLayoutManager(this));
            rv.setAdapter(mAdapter);

            // Firebase Database 초기(방 번호인 chatroom을 받아서 database에 새로운 방을 만든다.)
            myRef = database.getReference(chatroom);

            mContext = this;
            // Firebase Database Listener 붙이기
            myRef.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    // Firebase 의 해당 DB 에 값이 추가될 경우 호출, 생성 후 최초 1번은 실행됨
                    Log.d(TAG, "onChild added");
                    Log.d(TAG, "onChild = "+dataSnapshot.getValue(ChatMsgVO.class).toString());

                    // Database 의 정보를 ChatMsgVO 객체에 담음
                    ChatMsgVO chatMsgVO = dataSnapshot.getValue(ChatMsgVO.class);
                    msgList.add(chatMsgVO);

                    // 채팅 메시지 배열에 담고 RecyclerView 다시 그리기
                    mAdapter = new ChatAdapter(msgList);
                    rv.setAdapter(mAdapter);
                    rv.scrollToPosition(msgList.size()-1);
                    Log.d(TAG, msgList.size()+"");
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) { }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) { }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) { }

                @Override
                public void onCancelled(DatabaseError databaseError) { }
            });

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.send_iv:
                try{
                    if(content_et.getText().toString().trim().length() >= 1){
                        Log.d(TAG, "입력처리");

                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                        // Database 에 저장할 객체 만들기
                        ChatMsgVO msgVO = new ChatMsgVO(username, df.format(new Date()).toString(), content_et.getText().toString().trim());

                        // 해당 DB 에 값 저장시키기
                        myRef.push().setValue(msgVO);

                        // 입력 필드 초기화
                        content_et.setText("");
                    }else
                    {
//                        Toast.makeText(, "메시지를 입력하세요.", Toast.LENGTH_SHORT).show();
                    }
                }catch(Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{ //toolbar의 back키 눌렀을 때 동작
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}