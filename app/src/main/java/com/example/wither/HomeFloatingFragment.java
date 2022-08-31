package com.example.wither;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.overlay.InfoWindow;
import com.naver.maps.map.overlay.Marker;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Random;
import java.util.TimeZone;

public class HomeFloatingFragment extends Fragment implements Serializable {

    // 데이터베이스에 저장

    //실시간 데이터 주고 받기(homeFragment와 homeFloatingFragment)
    private SharedViewModel sharedViewModel;

    // fragment에서 context를 쓸려면 getActivity()

    // 키보드 조작
    private InputMethodManager keyboardDown;

    // 날짜 버튼 옆에 날자 보이게 하는 변수
    TextView dateText;

    // 달력 생성
    DatePickerDialog datePickerDialog;
    Calendar calendar = Calendar.getInstance();
    private int today_Year = calendar.get(Calendar.YEAR); //년
    private int today_Month = calendar.get(Calendar.MONTH);//월
    private int today_Day = calendar.get(Calendar.DAY_OF_MONTH);//일

    // 선택 날짜 저장 변수
    private int date_year = 0;
    private int date_month = 0;
    private int date_day = 0;

    // 이름, 인원, 하고싶은말 저장 변수
    private String create_name_variable;
    private int create_person_num_variable;
    private String create_for_friend_variable;

    // 카테고리 list
    ArrayList<String> category_list;
    ArrayList<String> person_num_list;

    private int category_true = 0;
    private int person_num_true = 0;
    private String category_string = null;

    // mainactivity에서 위도 경도 받아오기
    private double latitude, longitude;

    // 마커와 마커에 해당 정보창 띄우기 위해 필요한 객체
    Marker marker;
    InfoWindow infoWindow;

    public HomeFloatingFragment() {

    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        // 키보드 내리기 위한 코드
        keyboardDown = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

        View view = inflater.inflate(R.layout.fragment_home_floatingbtn, container, false);
        // 생성 버튼 리스너
        EditText create_name_edit_text = (EditText)view.findViewById(R.id.creat_name);
//        EditText create_person_num_edit_text = (EditText)view.findViewById(R.id.create_person_num);
        EditText create_for_friend_edit_text = (EditText)view.findViewById(R.id.create_for_friend);

        // 날짜 선택 버튼
        dateText = view.findViewById(R.id.date_picker_text);
        Button date_button = view.findViewById(R.id.date_btn);
        date_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDate();

                keyboradDownEditText(create_name_edit_text);
                keyboradDownEditText(create_for_friend_edit_text);
            }
        });

        // person textview(인원, 명)
        TextView person_textview1 = (TextView)view.findViewById(R.id.create_person_text);
        TextView person_textview2 = (TextView)view.findViewById(R.id.create_meung_text);
        TextView date_picker_text = (TextView)view.findViewById(R.id.date_picker_text);

        Button make_button = view.findViewById(R.id.create_make_btn);
        make_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                if ( create_name_edit_text.getText().toString().length() == 0 ) {
                    //모임 이름이 공백일 때 처리할 내용
                    Toast.makeText(getActivity(),"모임 이름을 설정해주세요",Toast.LENGTH_SHORT).show();
                } else {
                    //모임 이름이 공백이 아닐 때 처리할 내용
                    setCreate_name_variable(create_name_edit_text.getText().toString());
                    if ( getCreate_person_num_variable() == 0 ) {
                        //인원이 공백일 때 처리할 내용
                        Toast.makeText(getActivity(),"인원을 입력해주세요",Toast.LENGTH_SHORT).show();
                    } else {
                        //인원이 공백이 아닐 때 처리할 내용
                        if ( create_for_friend_edit_text.getText().toString().length() == 0 ) {
                            //친구 메세지가 공백일 때 처리할 내용
                            Toast.makeText(getActivity(),"친구들에게 메세지를 전하세요",Toast.LENGTH_SHORT).show();
                        } else {
                            //공백이 없을 때 처리할 내용
                            setCreate_for_friend_variable(create_for_friend_edit_text.getText().toString());
                            if(getDate_day() == 0){
                                // 날짜 선택을 안 했을 때
                                Toast.makeText(getActivity(),"날짜를 선택해주세요",Toast.LENGTH_SHORT).show();
                            }else{
                                // 날짜 선택 했을 때
                                if(getCategory_string() == null){
                                    // 카테고리 선택 안했을 때
                                    Toast.makeText(getActivity(),"카테고리를 선택해",Toast.LENGTH_SHORT).show();
                                }else{
                                    // 모든 선택을 다 했을 때

                                    // 생성 버튼 누르는 순간 좌표값을 받아온다.(homeFragment로 부터)
                                    setLatitude(getArguments().getDouble("latitude"));
                                    setLongitude(getArguments().getDouble("longitude"));
                                    marker = new Marker();
                                    infoWindow = new InfoWindow();

                                    Random random = new Random();


//
//
//                                    float randomNum = random.nextFloat()/1000f;
//                                    float random_plus_minus = random.nextFloat();
//                                    if(random_plus_minus > 0.5){
//                                        randomNum = randomNum * -1;
//                                    }
//
                                    LatLng c = new LatLng(getLatitude(),getLongitude());
                                    double d2r = Math.PI / 180;
                                    double r2d = 180 / Math.PI;
                                    double earth_rad = 6378000f; //지구 반지름 근사값

                                    double r = new Random().nextInt(30) + new Random().nextDouble();
                                    double rlat = (r / earth_rad) * r2d;
                                    double rlng = rlat / Math.cos(c.latitude * d2r);

                                    double theta = Math.PI * (new Random().nextInt(2) + new Random().nextDouble());
                                    double y = c.longitude + (rlng * Math.cos(theta));
                                    double x = c.latitude + (rlat * Math.sin(theta));

                                    //MakeDatabase에 저장.
                                    MakeDatabase database = new MakeDatabase(x,y,
                                            getCreate_name_variable(),getCreate_person_num_variable(),getCategory_string(),
                                            getCreate_for_friend_variable(),
                                            getDate_year(),getDate_month(),getDate_day()
                                    );

                                    // 날짜 초기화
                                    setDate_day(0);
                                    setDate_month(0);
                                    setDate_year(0);

                                    // 모든 값 초기화
                                    create_name_edit_text.setText(null);
                                    create_for_friend_edit_text.setText(null);
                                    setCategory_string(null);

                                    HomeFragment homeFragment = new HomeFragment();

                                    Bundle bundle = new Bundle(1);
                                    bundle.putInt("checking_floating",0);
                                    homeFragment.setArguments(bundle);

                                    // 플러스 버튼 fragment 종료
                                    FragmentManager fm = getParentFragmentManager();
                                    fm.beginTransaction().remove(fm.findFragmentById(R.id.homeFragmentFrame)).commit();


                                    // homeFragment로 database 객체 데이터 전송
                                    sharedViewModel.setLiveData(database);
                                    MakeDatabase.getGET_dabase().add(database);

                                    // Mongodb 데이터 저장.
                                    new Thread(()->{
                                        database.POST(database);
                                    }).start();

                                }
                            }
                        }
                    }
                }
            }
        });

        // 취소 버튼
        Button cancel_button = view.findViewById(R.id.create_cancel_btn);
        cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                //fm.beginTransaction().remove(fm.findFragmentById(R.id.for_hide_fragment)).commit();
                fm.beginTransaction().remove(fm.findFragmentById(R.id.homeFragmentFrame)).commit();
            }
        });

        Button person_num_button = view.findViewById(R.id.create_person_num_btn);
        person_num_list = new ArrayList<String>();

        ListView person_num_list_view = (ListView)view.findViewById(R.id.person_num_list);
        person_num_list_add();

        try{
            person_num_list_view.setVisibility(view.INVISIBLE);
            ArrayAdapter<String> listViewAdapter = new ArrayAdapter<String>(getActivity(),
                    R.layout.list_layout, person_num_list){
            };
            person_num_list_view.setAdapter(listViewAdapter);

        // 카테고리 리스트 클릭시 값 저장

        person_num_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 카테고리 리스트를 아무것도 터치하지 않았을 때
                if(getPerson_num_true() == 0){
                    // 키보드 내리기
                    keyboradDownEditText(create_name_edit_text);
                    keyboradDownEditText(create_for_friend_edit_text);

                    person_num_list_view.setVisibility(View.VISIBLE);
                    date_button.setVisibility(View.INVISIBLE);
//                    date_picker_text.setVisibility(View.INVISIBLE);
//                    create_for_friend_edit_text.setVisibility(View.INVISIBLE);
                    setPerson_num_true(1);
                }else{
                    // 키보드 내리기
                    keyboradDownEditText(create_name_edit_text);
                    keyboradDownEditText(create_for_friend_edit_text);

                    person_num_list_view.setVisibility(View.INVISIBLE);
                    date_button.setVisibility(View.VISIBLE);
//                    date_picker_text.setVisibility(View.VISIBLE);
//                    create_for_friend_edit_text.setVisibility(View.VISIBLE);
                    setPerson_num_true(0);
                }

                person_num_list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        try{
                            int check_position = person_num_list_view.getCheckedItemPosition();   //리스트뷰의 포지션을 가져옴.
                            setCreate_person_num_variable(Integer.parseInt((String)listViewAdapter.getItem(position)));  //리스트뷰의 포지션 내용을 가져옴.
                            //Toast.makeText(getActivity(),getCategory_string(),Toast.LENGTH_SHORT).show();
                            person_num_button.setText((String)listViewAdapter.getItem(position));

                            // 리스트 중 원하는 category를 클릭하면 바로 listview를 닫는다.
                            person_num_list_view.setVisibility(View.INVISIBLE);
                            date_button.setVisibility(View.VISIBLE);
//                            date_picker_text.setVisibility(View.VISIBLE);
//                            create_for_friend_edit_text.setVisibility(View.VISIBLE);
                            setPerson_num_true(0);
                        }catch(Exception e){
                            e.printStackTrace();
                        }

                    }
                });
            }
        });
        }catch(Exception e){
            e.printStackTrace();
        }
        // 인원수 버튼 리스트뷰 코드


        try{
            category_list = new ArrayList<String>();
            category_list_add();
            Collections.sort(category_list);

            //카테고리 리스트 보여주기
            ListView category_list_view = (ListView)view.findViewById(R.id.category_list);
            category_list_view.setVisibility(view.INVISIBLE);
            ArrayAdapter<String> listViewAdapter = new ArrayAdapter<String>(getActivity(),
                    android.R.layout.simple_list_item_1, category_list){

                // list text color -> black
                @Override
                public View getView(int position, View convertView, ViewGroup parent)
                {
                    View view = super.getView(position, convertView, parent);
                    TextView tv = (TextView) view.findViewById(android.R.id.text1);
                    tv.setTextColor(Color.BLACK);
                    return view;
                }
            };
            category_list_view.setAdapter(listViewAdapter);


            // 카테고리 리스트 클릭시 값 저장
            Button category_button = view.findViewById(R.id.category_btn);
            category_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // 카테고리 리스트를 아무것도 터치하지 않았을 때
                    if(getCategory_true() == 0){
                        // 키보드 내리기
                        keyboradDownEditText(create_name_edit_text);
                        keyboradDownEditText(create_for_friend_edit_text);

                        person_num_list_view.setVisibility(View.INVISIBLE);
                        category_list_view.setVisibility(View.VISIBLE);
                        person_num_button.setVisibility(View.INVISIBLE);
                        date_button.setVisibility(View.INVISIBLE);
                        person_textview1.setVisibility(View.INVISIBLE);
                        person_textview2.setVisibility(View.INVISIBLE);
                        date_picker_text.setVisibility(View.INVISIBLE);
                        create_for_friend_edit_text.setVisibility(View.INVISIBLE);
                        setCategory_true(1);
                    }else{
                        // 키보드 내리기
                        keyboradDownEditText(create_name_edit_text);
                        keyboradDownEditText(create_for_friend_edit_text);

                        category_list_view.setVisibility(View.INVISIBLE);
                        person_num_button.setVisibility(View.VISIBLE);
                        date_button.setVisibility(View.VISIBLE);
                        person_textview1.setVisibility(View.VISIBLE);
                        person_textview2.setVisibility(View.VISIBLE);
                        date_picker_text.setVisibility(View.VISIBLE);
                        create_for_friend_edit_text.setVisibility(View.VISIBLE);
                        setCategory_true(0);
                    }

                    category_list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            int check_position = category_list_view.getCheckedItemPosition();   //리스트뷰의 포지션을 가져옴.
                            setCategory_string((String)listViewAdapter.getItem(position));  //리스트뷰의 포지션 내용을 가져옴.
                            //Toast.makeText(getActivity(),getCategory_string(),Toast.LENGTH_SHORT).show();
                            category_button.setText(getCategory_string());

                            // 리스트 중 원하는 category를 클릭하면 바로 listview를 닫는다.
                            person_num_list_view.setVisibility(View.INVISIBLE);
                            category_list_view.setVisibility(View.INVISIBLE);
                            person_num_button.setVisibility(View.VISIBLE);
                            date_button.setVisibility(View.VISIBLE);
                            person_textview1.setVisibility(View.VISIBLE);
                            person_textview2.setVisibility(View.VISIBLE);
                            date_picker_text.setVisibility(View.VISIBLE);
                            create_for_friend_edit_text.setVisibility(View.VISIBLE);
                            setCategory_true(0);
                        }
                    });
                }
            });
        }catch(Exception e){
            e.printStackTrace();
        }
        // 카테고리 리스트


        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

    }

    void showDate() {
        //오늘 날짜(년,월,일) 변수에 담기

        datePickerDialog = new DatePickerDialog(getActivity(),R.style.DatePickerTheme,
                (datePicker, year, month, day) -> {

                    //1월은 0부터 시작하기 때문에 +1을 해준다.
                    month = month + 1;
                    String date = year + "." + month + "." + day;

                    setDate_year(year);
                    setDate_month(month);
                    setDate_day(day);

                    dateText.setText(date);
                }, today_Year, today_Month,today_Day);
        datePickerDialog.show();
    }

    private void category_list_add(){
        category_list.add("음악");
        category_list.add("맛집");
        category_list.add("OTT");
        category_list.add("문화/예술");
        category_list.add("종교");
        category_list.add("독서/스터디");
        category_list.add("어학");
        category_list.add("쇼핑");
        category_list.add("반려동물 산책");
        category_list.add("여행/캠핑");
        category_list.add("건강/다이어트");
        category_list.add("게임");
        category_list.add("봉사");
        category_list.add("help");
        category_list.add("일상/이야기");
        category_list.add("주식/가상화폐");
    }

    private void person_num_list_add(){
        person_num_list.add("2");
        person_num_list.add("3");
        person_num_list.add("4");
    }

    private void hideKeyboard()
    {
        if (getActivity() != null && getActivity().getCurrentFocus() != null)
        {
            // 프래그먼트기 때문에 getActivity() 사용
            InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    private void keyboradDownEditText(EditText editText){
        keyboardDown.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }


    public int getDate_day() {
        return date_day;
    }

    public void setDate_day(int date_day) {
        this.date_day = date_day;
    }

    public int getDate_month() {
        return date_month;
    }

    public void setDate_month(int date_month) {
        this.date_month = date_month;
    }

    public int getDate_year() {
        return date_year;
    }

    public void setDate_year(int date_year) {
        this.date_year = date_year;
    }

    public int getCategory_true(){
        return category_true;
    }

    public void setCategory_true(int category_true){
        this.category_true = category_true;
    }

    public int getPerson_num_true(){
        return person_num_true;
    }

    public void setPerson_num_true(int person_num_true){
        this.person_num_true = person_num_true;
    }

    public String getCreate_name_variable() {
        return create_name_variable;
    }

    public int getCreate_person_num_variable() {
        return create_person_num_variable;
    }

    public String getCreate_for_friend_variable() {
        return create_for_friend_variable;
    }

    public void setCreate_name_variable(String create_name_variable) {
        this.create_name_variable = create_name_variable;
    }

    public void setCreate_for_friend_variable(String create_for_friend_variable) {
        this.create_for_friend_variable = create_for_friend_variable;
    }

    public void setCreate_person_num_variable(int create_person_num_variable) {
        this.create_person_num_variable = create_person_num_variable;
    }

    public String getCategory_string() {
        return category_string;
    }

    public void setCategory_string(String category_string) {
        this.category_string = category_string;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

}