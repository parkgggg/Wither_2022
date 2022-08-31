package com.example.wither;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.lang.reflect.Array;

public class MainActivity extends AppCompatActivity {
    //채팅방 리스트 변수
    private ListView listView;
    private ArrayAdapter<String> arrayAdapter;
    private ArrayList<String> arr_roomList = new ArrayList<>();
    private String str_room;
    ArrayList<MakeDatabase> GET_database_set = new ArrayList<MakeDatabase>();

    // gps 관련 변수들
    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int PERMISSIONS_REQUEST_CODE = 100;
    String[] REQUIRED_PERMISSIONS = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
    private double latitude, longitude;

    // fragment 변수 선언
    private HomeFragment homeFragment;
    private ChattingFragment chattingFragment;
    //private UserFragment userFragment;
    private HomeFloatingFragment homeFloatingFragment;

    private FragmentActivity myContext;
    FloatingActionButton fab;

    String username;

    // 두번 뒤로가기 눌렀을 때 앱 종료 자바 클래스
    private final BackKeyHandler backKeyHandler = new BackKeyHandler(this);
// search 기능 진행중
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu,menu);
//
//        MenuItem menuItem = menu.findItem(R.id.action_search);
//
//        return super.onCreateOptionsMenu(menu);
//    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        homeFragment = new HomeFragment();
        chattingFragment = new ChattingFragment();
        //userFragment = new UserFragment();
        homeFloatingFragment = new HomeFloatingFragment();
//리스트뷰
//        firstInit(); //객체 초기화 및 생성
//        addItem(); //아이템 리스트 추가
//
//        mListItemsAdapter = new ListItemsAdapter(getApplicationContext(), mItems); //어댑터 객체 생성
//        list_items.setAdapter(mListItemsAdapter); //리스트뷰에 어댑터 적용

        //아이템 클릭했을 때 동작하는 클릭 리스너
//        list_items.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(getApplicationContext(), "position = "  + position + ", name=" + mItems.get(position), Toast.LENGTH_SHORT).show();
//            }
//        });



        // 위치 확인
        if (!checkLocationServicesStatus()) {
            showDialogForLocationServiceSetting();
        } else {
            checkRunTimePermission();
        }

        //현재 위치를 실시간으로 가져오는 코드
        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            // Logic to handle location object

                            latitude = location.getLatitude();
                            longitude = location.getLongitude();

                            Bundle bundle = new Bundle(2);
                            bundle.putDouble("latitude", latitude);
                            bundle.putDouble("longitude", longitude);
                            homeFragment.setArguments(bundle);
                            homeFloatingFragment.setArguments(bundle);
                        }
                    }
                });

        //밑에는 fragment 간 교체를 위한 코드
        // 첫번재 버전은 한번만 fragment를 불러오고 hide show하여 단지 안보이게 한다.
        // 이렇게 하면 뒤로가기를 하면 이전 fragment가 아닌 앱이 종료가 된다.
        // 하지만 다른 fragment로 넘나들 때 이 전께 그저 hide된 거기 때문에 다시 돌아오면 가기 전 상태 그대로 있다.

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.main_frame, homeFragment, "home")
                .commitAllowingStateLoss();

        Bundle bundle = new Bundle(1);
        bundle.putString("username",username);
        homeFragment.setArguments(bundle);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavi);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                /**각 탭에 아이디의 따라 다르게 띄워주기*/
                FragmentManager fragmentManager = getSupportFragmentManager();

                switch (item.getItemId()) {
                    case R.id.action_home: {
                        if (fragmentManager.findFragmentByTag("home") != null) {
                            //프래그먼트가 존재한다면 보여준다.
                            fragmentManager.beginTransaction().show(Objects.requireNonNull(fragmentManager.findFragmentByTag("home"))).commit();
                        } else {
                            //존재하지 않는다면 프래그먼트를 매니저에 추가
                            fragmentManager.beginTransaction().add(R.id.main_frame, new HomeFragment(), "home").commit();
                        }
                        if (fragmentManager.findFragmentByTag("chatting") != null) {
                            //다른프래그먼트가 보이면 가려준다.
                            fragmentManager.beginTransaction().hide(Objects.requireNonNull(fragmentManager.findFragmentByTag("chatting"))).commit();
                        }
                        if (fragmentManager.findFragmentByTag("user") != null) {
                            //다른프래그먼트가 보이면 가려준다.
                            fragmentManager.beginTransaction().hide(Objects.requireNonNull(fragmentManager.findFragmentByTag("user"))).commit();
                        }
                        return true;
                    }
                    case R.id.action_chat: {

                        if (fragmentManager.findFragmentByTag("chatting") != null) {
                            //if the fragment exists, show it.
                            fragmentManager.beginTransaction().show(Objects.requireNonNull(fragmentManager.findFragmentByTag("chatting"))).commit();
                        } else {
                            //if the fragment does not exist, add it to fragment manager.
                            fragmentManager.beginTransaction().add(R.id.main_frame, new ChattingFragment(), "chatting").commit();
                        }
                        if (fragmentManager.findFragmentByTag("home") != null) {
                            //if the other fragment is visible, hide it.
                            fragmentManager.beginTransaction().hide(Objects.requireNonNull(fragmentManager.findFragmentByTag("home"))).commit();
                        }
                        if (fragmentManager.findFragmentByTag("user") != null) {
                            //if the other fragment is visible, hide it.
                            fragmentManager.beginTransaction().hide(Objects.requireNonNull(fragmentManager.findFragmentByTag("user"))).commit();
                        }
                        return true;
                    }
                    case R.id.action_user: {

                        if (fragmentManager.findFragmentByTag("user") != null) {
                            //if the fragment exists, show it.
                            fragmentManager.beginTransaction().show(Objects.requireNonNull(fragmentManager.findFragmentByTag("user"))).commit();
                        } else {
                            //if the fragment does not exist, add it to fragment manager.
                            //fragmentManager.beginTransaction().add(R.id.main_frame, new UserFragment(), "user").commit();
                        }
                        if (fragmentManager.findFragmentByTag("home") != null) {
                            //if the other fragment is visible, hide it.
                            fragmentManager.beginTransaction().hide(Objects.requireNonNull(fragmentManager.findFragmentByTag("home"))).commit();
                        }
                        if (fragmentManager.findFragmentByTag("chatting") != null) {
                            //if the other fragment is visible, hide it.
                            fragmentManager.beginTransaction().hide(Objects.requireNonNull(fragmentManager.findFragmentByTag("chatting"))).commit();
                        }
                        return true;
                    }
                    default:
                        return false;
                }
            }
        });
    }


    // 두번째 버전은 하단메뉴를 누를 때마다 fragment가 replace되면서 뒤로가기 버튼을 누르면
    // 아까 전 fragment로 옮겨간다. 하지만 fragment가 계속 새로 생성되기 때문에 그 전에 정보들이 초기화된다.

//        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavi);
//        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem menuitem) {
//                int itemId = menuitem.getItemId();
//                if (itemId == R.id.action_home) {
//                    setFrag(0);
//                } else if (itemId == R.id.action_chat) {
//                    setFrag(1);
//                } else if (itemId == R.id.action_user) {
//                    setFrag(2);
//                }
//                return true;
//            }
//        });
//
//        // 초기 fragment homeFragment로 설정
//        setFrag(0);


//    //프래그먼트 교체가 일어나는 실행문
//    private void setFrag(int n) {
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//
//        switch (n) {
//            case 0:
//                fragmentTransaction.replace(R.id.main_frame, homeFragment).addToBackStack(null);
//                fragmentTransaction.commit();
//
//                break;
//            case 1:
//                fragmentTransaction.replace(R.id.main_frame, chattingFragment).addToBackStack(null);
//                fragmentTransaction.commit();
//                break;
//            case 2:
//                fragmentTransaction.replace(R.id.main_frame, userFragment).addToBackStack(null);
//                fragmentTransaction.commit();
//                break;
//        }
//    }

    // 뒤로 가기 두번 눌렀을 때 앱이 종료하게 하는 메서드 호출
    public void onBackPressed() {
        /* 다음 4가지 형태 중 하나 선택해서 사용 */

        //backKeyHandler.onBackPressed();
        //backKeyHandler.onBackPressed("\'뒤로\' 버튼을 두 번 누르면 종료됩니다.\n입력한 내용이 지워집니다.");
        //backKeyHandler.onBackPressed(5);
        backKeyHandler.onBackPressed("\'뒤로\' 버튼을 한번 더 누르면,\n앱이 종료됩니다 ", 3.5);
    }


    // 여기서부터는 gps 받아오는 코드
    @Override
    public void onRequestPermissionsResult(int permsRequestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grandResults) {

        super.onRequestPermissionsResult(permsRequestCode, permissions, grandResults);
        if (permsRequestCode == PERMISSIONS_REQUEST_CODE && grandResults.length == REQUIRED_PERMISSIONS.length) {

            // 요청 코드가 PERMISSIONS_REQUEST_CODE 이고, 요청한 퍼미션 개수만큼 수신되었다면

            boolean check_result = true;


            // 모든 퍼미션을 허용했는지 체크합니다.

            for (int result : grandResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    check_result = false;
                    break;
                }
            }

            if (check_result) {

                //위치 값을 가져올 수 있음
                ;
            } else {
                // 거부한 퍼미션이 있다면 앱을 사용할 수 없는 이유를 설명해주고 앱을 종료합니다.2 가지 경우가 있습니다.

                if (ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[0])
                        || ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[1])) {

                    Toast.makeText(MainActivity.this, "퍼미션이 거부되었습니다. 앱을 다시 실행하여 퍼미션을 허용해주세요.", Toast.LENGTH_LONG).show();
                    finish();


                } else {

                    Toast.makeText(MainActivity.this, "퍼미션이 거부되었습니다. 설정(앱 정보)에서 퍼미션을 허용해야 합니다. ", Toast.LENGTH_LONG).show();

                }
            }

        }
    }

    void checkRunTimePermission() {

        //런타임 퍼미션 처리
        // 1. 위치 퍼미션을 가지고 있는지 체크합니다.
        int hasFineLocationPermission = ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.ACCESS_COARSE_LOCATION);


        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED &&
                hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED) {

            // 2. 이미 퍼미션을 가지고 있다면
            // ( 안드로이드 6.0 이하 버전은 런타임 퍼미션이 필요없기 때문에 이미 허용된 걸로 인식합니다.)


            // 3.  위치 값을 가져올 수 있음


        } else {  //2. 퍼미션 요청을 허용한 적이 없다면 퍼미션 요청이 필요합니다. 2가지 경우(3-1, 4-1)가 있습니다.

            // 3-1. 사용자가 퍼미션 거부를 한 적이 있는 경우에는
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, REQUIRED_PERMISSIONS[0])) {

                // 3-2. 요청을 진행하기 전에 사용자가에게 퍼미션이 필요한 이유를 설명해줄 필요가 있습니다.
                Toast.makeText(MainActivity.this, "이 앱을 실행하려면 위치 접근 권한이 필요합니다.", Toast.LENGTH_LONG).show();
                // 3-3. 사용자게에 퍼미션 요청을 합니다. 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions(MainActivity.this, REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);

            } else {
                // 4-1. 사용자가 퍼미션 거부를 한 적이 없는 경우에는 퍼미션 요청을 바로 합니다.
                // 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions(MainActivity.this, REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);
            }

        }

    }

    // 경도 위도를 통한 도로명 주소 도출 메서드
    public String getCurrentAddress(double latitude, double longitude) {

        //지오코더... GPS를 주소로 변환
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        List<Address> addresses;

        try {

            addresses = geocoder.getFromLocation(
                    latitude,
                    longitude,
                    7);

        } catch (IOException ioException) {
            //네트워크 문제
            Toast.makeText(this, "지오코더 서비스 사용불가", Toast.LENGTH_LONG).show();
            return "지오코더 서비스 사용불가";

        } catch (IllegalArgumentException illegalArgumentException) {
            Toast.makeText(this, "잘못된 GPS 좌표", Toast.LENGTH_LONG).show();
            return "잘못된 GPS 좌표";

        }

        if (addresses == null || addresses.size() == 0) {
            Toast.makeText(this, "주소 미발견", Toast.LENGTH_LONG).show();
            return "주소 미발견";

        }

        Address address = addresses.get(0);
        return address.getAddressLine(0).toString() + "\n";

    }

    //gps 설정 메서드
    private void showDialogForLocationServiceSetting() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this , R.style.MyDialogTheme);
        builder.setTitle("위치 서비스 비활성화");
        builder.setMessage("앱을 사용하기 위해서는 위치 서비스가 필요합니다.\n"
                + "위치 설정을 수정하시겠습니까?");
        builder.setCancelable(true);
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Intent callGPSSettingIntent
                        = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(callGPSSettingIntent, GPS_ENABLE_REQUEST_CODE);
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.create().show();
    }

    // gps 요청 메서드
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {

            case GPS_ENABLE_REQUEST_CODE:

                //사용자가 GPS 활성 시켰는지 검사
                if (checkLocationServicesStatus()) {
                    if (checkLocationServicesStatus()) {

                        Log.d("@@@", "onActivityResult : GPS 활성화 되있음");
                        checkRunTimePermission();
                        return;
                    }
                }

                break;
        }
    }

    // gps 허용 여부 메서드
    public boolean checkLocationServicesStatus() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }
}

//ver2


//adapter class
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        ListView listView = findViewById(R.id.listView);
//        CategoryAdapter adapter = new CategoryAdapter();
//        adapter.addItem(new CategoryList("blackFish", "010-1000-1000", R.drawable.category_ic_cigarette));
//        adapter.addItem(new CategoryList("redFish", "010-1000-1001", R.drawable.category_ic_baseball));
//        listView.setAdapter(adapter);
//
//    }

//    class CategoryAdapter extends BaseAdapter {
//        ArrayList<CategoryList> items = new ArrayList<CategoryList>();
//
//        @Override
//        public int getCount() {
//            return items.size();
//        }
//
//        public void addItem(CategoryList item){
//            items.add(item);
//        }
//
//        @Override
//        public Object getItem(int position) {
//            return items.get(position);
//        }
//
//        @Override
//        public long getItemId(int position) {
//            return position;
//        }
//
//        @Override
//        public View getView(int position, View convertView, ViewGroup parent) {
//            CategoryListView categoryListView = null;
//
//            if (convertView == null) {
//                categoryListView = new CategoryListView(getApplicationContext());
//            } else {
//                categoryListView = (CategoryListView) convertView;
//            }
//            CategoryList item = items.get(position);
//            categoryListView.setName(item.getName());
//            categoryListView.setMobile(item.getMobile());
//            categoryListView.setImage(item.getResld());
//            return categoryListView;
//            }
