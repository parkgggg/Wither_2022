package com.example.wither;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.naver.maps.map.overlay.InfoWindow;
import com.naver.maps.map.overlay.Marker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MakeDatabase implements Parcelable {
    // 9개 정보
    private static  ArrayList<MakeDatabase> GET_dabase = new ArrayList<MakeDatabase>();
    private static String username;
    private int id;
    private double latitude;
    private double longitude;
    private String meeting_name;
    private String meeting_category;
    private int meeting_person;
    private int year;
    private int month;
    private int day;
    private String text_for_meeting_friend;
    private int resourceID;
    private int left_room_person;

    // marker 객체 저장
    Marker marker;
    InfoWindow infoWindow;

    MakeDatabase(){
    }

    public MakeDatabase(double latitude, double longitude, String meeting_name,
            int meeting_person,String meeting_category, String text_for_meeting_friend,
                        int year, int month, int day
                        ) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.meeting_name = meeting_name;
        this.meeting_category = meeting_category;
        this.meeting_person = meeting_person;
        this.year = year;
        this.month = month;
        this.day = day;
        this.text_for_meeting_friend = text_for_meeting_friend;
        setResourceID(getMarkerIcon_int(meeting_category));

        Marker marker = new Marker();
        InfoWindow infoWindow = new InfoWindow();

        this.setMarker(marker);
        this.setInfoWindow(infoWindow);
    }

    protected MakeDatabase(Parcel in) {
        id = in.readInt();
        latitude = in.readDouble();
        longitude = in.readDouble();
        meeting_name = in.readString();
        meeting_category = in.readString();
        meeting_person = in.readInt();
        year = in.readInt();
        month = in.readInt();
        day = in.readInt();
        text_for_meeting_friend = in.readString();
        resourceID = in.readInt();
    }

    public static final Creator<MakeDatabase> CREATOR = new Creator<MakeDatabase>() {
        @Override
        public MakeDatabase createFromParcel(Parcel in) {
            return new MakeDatabase(in);
        }

        @Override
        public MakeDatabase[] newArray(int size) {
            return new MakeDatabase[size];
        }
    };

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

    public String getMeeting_name() {
        return meeting_name;
    }

    public void setMeeting_name(String meeting_name) {
        this.meeting_name = meeting_name;
    }

    public String getMeeting_category() {
        return meeting_category;
    }

    public void setMeeting_category(String meeting_category) {
        this.meeting_category = meeting_category;
    }

    public int getMeeting_person() {
        return meeting_person;
    }

    public void setMeeting_person(int meeting_person) {
        this.meeting_person = meeting_person;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public String getText_for_meeting_friend() {
        return text_for_meeting_friend;
    }

    public void setText_for_meeting_friend(String text_for_meeting_friend) {
        this.text_for_meeting_friend = text_for_meeting_friend;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public int getResourceID() {
        return resourceID;
    }

    public void setResourceID(int resourceID) {
        this.resourceID = resourceID;
    }

    public Marker getMarker() {
        return marker;
    }

    public void setMarker(Marker marker) {
        this.marker = marker;
    }

    public InfoWindow getInfoWindow() {
        return infoWindow;
    }

    public void setInfoWindow(InfoWindow infoWindow) {
        this.infoWindow = infoWindow;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLeft_room_person() {
        return left_room_person;
    }

    public void setLeft_room_person(int left_room_person) {
        this.left_room_person = left_room_person;
    }

    public static ArrayList<MakeDatabase> getGET_dabase() {
        return GET_dabase;
    }

    public static void setGET_dabase(ArrayList<MakeDatabase> GET_dabase) {
        MakeDatabase.GET_dabase = GET_dabase;
    }

    public static String getUsername() {
        return username;
    }

    public static void setUsername(String username) {
        MakeDatabase.username = username;
    }

    public String toString(){
        return getMeeting_name()+"///"+getMeeting_person()+getYear()+getMonth()+getDay()+getText_for_meeting_friend();
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeDouble(latitude);
        parcel.writeDouble(longitude);
        parcel.writeString(meeting_name);
        parcel.writeString(meeting_category);
        parcel.writeInt(meeting_person);
        parcel.writeInt(year);
        parcel.writeInt(month);
        parcel.writeInt(day);
        parcel.writeString(text_for_meeting_friend);
        parcel.writeInt(resourceID);
    }

    public int getMarkerIcon_int(String meeting_category){
        if(meeting_category.equals("음악") ){
            return(R.drawable.category_ic_music);
        }else if(meeting_category.equals("맛집")){
            return(R.drawable.category_ic_restaurant);
        }else if(meeting_category.equals("OTT")){
            return(R.drawable.category_ic_netflix);
        }else if(meeting_category.equals("문화/예술")){
            return(R.drawable.category_ic_culture);
        }else if(meeting_category.equals("종교")){
            return(R.drawable.category_ic_religion);
        }else if(meeting_category.equals("독서/스터디")){
            return(R.drawable.category_ic_study);
        }else if(meeting_category.equals("어학")){
            return(R.drawable.category_ic_english);
        }else if(meeting_category.equals("쇼핑")){
            return(R.drawable.category_ic_shopping);
        }else if(meeting_category.equals("반려동물 산책")){
            return(R.drawable.category_ic_dog);
        }else if(meeting_category.equals("여행/캠핑")){
            return(R.drawable.category_ic_travel);
        }else if(meeting_category.equals("건강/다이어트")){
            return(R.drawable.category_ic_health);
        }else if(meeting_category.equals("게임")){
            return(R.drawable.category_ic_game);
        }else if(meeting_category.equals("봉사")){
            return(R.drawable.category_ic_volunteer);
        }else if(meeting_category.equals("help")){
            return(R.drawable.category_ic_help);
        }else if(meeting_category.equals("일상/이야기")){
            return(R.drawable.category_ic_communication);
        }else if(meeting_category.equals("주식/가상화폐")){
            return(R.drawable.category_ic_coin);
        }

        return 0;
    }

    // 객체를 json으로 바꾼 후 mongodb에 전달
//    public String toJson(MakeDatabase database) {
//        Gson gson = new Gson();
//        String jsonString = gson.toJson(database);
//        System.out.println(jsonString);
//        return jsonString;
//    }

    // mongodb에 넣어줄 정보들
    // 여기에 파라미터로 아무것도 주면 안된다. 이상하게 파라미터로 MakeDatabase의 객체를 줬는데 오류가 나서 mongodb에 저장이 안됐다.
    // 파라미터를 빼니까 실행이 된다. 왜그런지는 모르겠다.
    public String toJson() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("latitude",this.getLatitude());
        jsonObject.put("longitude",this.getLongitude());
        jsonObject.put("meeting_name",this.getMeeting_name());
        jsonObject.put("meeting_person_num",this.getMeeting_person());
        jsonObject.put("meeting_category",this.getMeeting_category());
        jsonObject.put("text_for_meeting_friend",this.getText_for_meeting_friend());
        jsonObject.put("year",this.getYear());
        jsonObject.put("month",this.getMonth());
        jsonObject.put("day",this.getDay());

        return jsonObject.toString();
    }

    // mongodb에 MakeDatabase에 정보를 저장한다.
    public void POST(MakeDatabase database){
        // 요청할 파라미터의 정보를 입력한다.
        try {
            URL url = new URL("https://jhiwjrpj98.execute-api.us-west-2.amazonaws.com/2022-04-18/board"); //요청 URL을 입력
            HttpURLConnection con = (HttpURLConnection)url.openConnection();    // HttpUrlConnection 객체 가져오기
            con.setRequestMethod("POST"); //요청 방식을 설정 (default : GET)
            con.setRequestProperty("Content-Type", "application/json; utf-8");  // 요청 본문을 json으로 보내기 위해선 꼭 필요
            con.setRequestProperty("Accept", "application/json"); // 원하는 형식으로 응답을 읽으려면 Accept 용청 헤드를 application/json으로 설정
            con.setDoInput(true); //input을 사용하도록 설정 (default : true)
            con.setDoOutput(true); //output을 사용하도록 설정 (default : false)

            con.setConnectTimeout(4000); //타임아웃 시간 설정 (default : 무한대기)
            con.setDoOutput(true);
            con.setReadTimeout(5000);
//            OutputStream os = con.getOutputStream();
//            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8")); //캐릭터셋 설정

            String jsonInputString = toJson();

            try(OutputStream os = con.getOutputStream()){
                byte[] input = jsonInputString.getBytes("UTF-8");
                os.write(input, 0, input.length);
                os.close();
                os.flush();
            }catch(Exception e){
                Log.e("slkdfj",e.toString());
            }
            con.connect();
            int code = con.getResponseCode();
//            System.out.println(code);

            try(BufferedReader br = new BufferedReader(
                    new InputStreamReader(con.getInputStream(), "UTF-8"))) {
                StringBuilder response = new StringBuilder();
                String responseLine = null;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
//                System.out.println(response.toString());

            }catch(Exception e){
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // mongodb에 저장해둔 모든 정보를 가져온다.
    public static ArrayList GET() {
        HttpURLConnection conn = null;
        JSONArray responseJson = null;
        ArrayList<String> list = new ArrayList<String>();
        try {
            URL url = new URL("https://jhiwjrpj98.execute-api.us-west-2.amazonaws.com/2022-04-18/board");

            conn = (HttpURLConnection)url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);
            conn.setRequestMethod("GET");
//            conn.setDoOutput(true);

            JSONArray commands = new JSONArray();

            int responseCode = conn.getResponseCode();
            if (responseCode == 400 || responseCode == 401 || responseCode == 500 ) {
                System.out.println(responseCode + " Error!");
            } else {
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line = "";
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                responseJson = new JSONArray(sb.toString());
//                System.out.println(responseJson);

                for (int i=0; i<responseJson.length(); i++) {
                    list.add( responseJson.getString(i) );
                }

            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
//            System.out.println("not JSON Format response");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;

    }

    public static ArrayList<MakeDatabase> json_to_database_set(ArrayList<String> json_String_database){
        ArrayList<MakeDatabase> database_set = new ArrayList<MakeDatabase>();
        MakeDatabase database;
        for(int i = 0 ; i < json_String_database.size();i++){
            String[] jS = json_String_database.get(i).split(":|,");
            jS[11] = jS[11].replace("\\","");
            String jS_7 = jS[7].replace("\"","");
            String jS_11 = jS[11].replace("\"","");
            String jS_13 = jS[13].replace("\"","");
            database = new MakeDatabase(Double.parseDouble(jS[3]),Double.parseDouble(jS[5]),jS_7,Integer.parseInt(jS[9]),
                    jS_11,jS_13,
                    Integer.parseInt(jS[15]),
                    Integer.parseInt(jS[17]),
                    Integer.parseInt(jS[19]));
            database.setId(Integer.parseInt(jS[21]));
            database_set.add(database);
        }
        return database_set;
    }
}