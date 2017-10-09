package com.example.econo_104.jnu_bell;



import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;


import com.skp.Tmap.TMapData;
import com.skp.Tmap.TMapGpsManager;
import com.skp.Tmap.TMapMarkerItem;
import com.skp.Tmap.TMapPoint;
import com.skp.Tmap.TMapPolyLine;
import com.skp.Tmap.TMapView;




public class MainActivity extends AppCompatActivity implements TMapGpsManager.onLocationChangedCallback {

    public static final int BELL_NUMBER = 20;

    private double p2pDistance[];
    private TMapPoint SOS_bells[];
    private TMapMarkerItem SOS_bells_item[];
    private Button button = null;
    private boolean m_bTrackingMode = true;
    private TMapView tmapview = null;
    private TMapGpsManager tmapgps = null;
    private TMapPoint current_point = null;  // 현재 위치 좌표
    private TMapData tmapdata = null;
    private RelativeLayout relativeLayout;
    private int index;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


            setArray(); // 배열들 생성

            defaultSetting(); // 기초 세팅 작업

            addMarker();  //마커 추가 및 최단거리 구하기



            button = (Button) findViewById(R.id.findPath);
            button.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View view) {

                    int index = findFastPath(p2pDistance);   // 버튼을 누르면 제일 빠른 경로의 배열 인덱스를 알려준다

                    tmapdata = new TMapData();
                        tmapdata.findPathDataWithType(TMapData.TMapPathType.PEDESTRIAN_PATH, current_point, SOS_bells[index], // 인덱스 num을 사용
                                new TMapData.FindPathDataListenerCallback() {
                                    @Override
                                    public void onFindPathData(TMapPolyLine tMapPolyLine) {
                                            tMapPolyLine.setLineColor(Color.RED);
                                            tmapview.addTMapPath(tMapPolyLine);

                                    }
                                });
                }
            });

            button = (Button) findViewById(R.id.Navigation);
            button.setOnClickListener(new Button.OnClickListener(){
                @Override
                public void onClick(View view){
                    tmapview.setTrackingMode(true);   // 화면중심을 단말의 현재 위치로
                    tmapview.setSightVisible(true);   // 시야표출여부 (나침반모드랑 비슷?)
                    tmapview.setCompassMode(true); // 나침반 모드
                }
            });

            button = (Button) findViewById(R.id.RemovePath);
            button.setOnClickListener(new Button.OnClickListener(){
                @Override
                public void onClick(View view){
                    tmapview.removeAllTMapPolyLine(); // 경로 안내 선 지우기

                }
            });
        }





    @Override
    public void onLocationChange(Location location) {  // 위치가 변화할 때
        if (m_bTrackingMode) {
            tmapview.setLocationPoint(location.getLongitude(), location.getLatitude());
            current_point = tmapgps.getLocation(); // 현재 위치 반환
            saveDistance(35.174190, 126.900330,0);  // 현재 위치가 바뀔 때마다 현재 위치와 마커들 간의 거리를 측정한다
            saveDistance(35.175207, 126.900287,1);
            saveDistance(35.173870, 126.902931,2);
            saveDistance(35.176794, 126.901499,3);
            saveDistance(35.176584, 126.902261,4);
            saveDistance(35.176249, 126.902779,5);
            saveDistance(35.177056, 126.903615,6);
            saveDistance(35.176398, 126.905165,7);
            saveDistance(35.178029, 126.905980,8);
            saveDistance(35.180125, 126.905724,9);
            saveDistance(35.180555, 126.906873,10);
            saveDistance(35.179023, 126.908079,11);
            saveDistance(35.177765, 126.908939,12);
            saveDistance(35.179056, 126.909831,13);
            saveDistance(35.180274, 126.910555,14);
            saveDistance(35.178789, 126.911274,15);
            saveDistance(35.177407, 126.910768,16);
            saveDistance(35.176615, 126.909635,17);
            saveDistance(35.175705, 126.910257,18);
            saveDistance(35.174457, 126.911459,19);
        }
    }



    public void setArray(){
        SOS_bells = new TMapPoint[BELL_NUMBER]; // 벨들을 배열로 생성
        SOS_bells_item = new TMapMarkerItem[BELL_NUMBER];
        p2pDistance = new double[BELL_NUMBER];
    }



    public void defaultSetting(){
        ///////////////////////////////////////기본 세팅////////////////////////////////////////////////////////
        //선언
        relativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout);  // xml 파일에 존재하는 relativelayout 가져오기
        tmapview = new TMapView(this);  // T map view 객체 생성


        //키값
        tmapview.setSKPMapApiKey("d8bcc681-3617-35c5-93cb-9b3a6e1fa3ed");   // 웹에서 발급받은 API 키 삽입


        tmapview.setCompassMode(true); // 나침반 모드
        tmapview.setIconVisibility(true);  // 아이콘들 보이게하기
        tmapview.setZoomLevel(20);  // 줌
        tmapview.setMapType(TMapView.MAPTYPE_STANDARD);  // 맵 타입 기본형
        tmapview.setLanguage(TMapView.LANGUAGE_KOREAN);  // 맵 언어 코리안


        tmapgps = new TMapGpsManager(this);   // GPS 기능 사용하기

        tmapgps.setMinTime(1000);  // GPS 변경 최소 시간
        tmapgps.setMinDistance(5); // GPS 변경되는 최소 거리
        tmapgps.setProvider(tmapgps.NETWORK_PROVIDER); // GPS 네트워크(와이파이) 위치로 구분할지 아니면 스마트폰 GPS 로 구분할지.

        tmapgps.OpenGps();  // GPS 실행


        tmapview.setTrackingMode(true);   // 화면중심을 단말의 현재 위치로
        tmapview.setSightVisible(true);   // 시야표출여부 (나침반모드랑 비슷?)


        relativeLayout.addView(tmapview); // 맨 위에 선언한 relativelayout 에다가 세팅을 마친 tmapview를 넣는다.


        /////////////////////////////////default 현재 위치 저장//////////////////////////////////////////
        current_point = tmapgps.getLocation(); // 현재 위치 반환

        double Latitude = current_point.getLatitude();
        double Longtitude = current_point.getLongitude();


    }




    public void addMarker() {

        SOS_bells[0] = new TMapPoint(35.174190, 126.900330);  // 마커 위도 경도
        SOS_bells[1] = new TMapPoint(35.175207, 126.900287);  // 마커 위도 경도
        SOS_bells[2] = new TMapPoint(35.173870, 126.902931);  // 마커 위도 경도
        SOS_bells[3] = new TMapPoint(35.176794, 126.901499);  // 마커 위도 경도
        SOS_bells[4] = new TMapPoint(35.176584, 126.902261);  // 마커 위도 경도
        SOS_bells[5] = new TMapPoint(35.176249, 126.902779);  // 마커 위도 경도
        SOS_bells[6] = new TMapPoint(35.177056, 126.903615);  // 마커 위도 경도
        SOS_bells[7] = new TMapPoint(35.176398, 126.905165);  // 마커 위도 경도
        SOS_bells[8] = new TMapPoint(35.178029, 126.905980);  // 마커 위도 경도
        SOS_bells[9] = new TMapPoint(35.180125, 126.905724);  // 마커 위도 경도
        SOS_bells[10] = new TMapPoint(35.180555, 126.906873);  // 마커 위도 경도
        SOS_bells[11] = new TMapPoint(35.179023, 126.908079);  // 마커 위도 경도
        SOS_bells[12] = new TMapPoint(35.177765, 126.908939);  // 마커 위도 경도
        SOS_bells[13] = new TMapPoint(35.179056, 126.909831);  // 마커 위도 경도
        SOS_bells[14] = new TMapPoint(35.180274, 126.910555);  // 마커 위도 경도
        SOS_bells[15] = new TMapPoint(35.178789, 126.911274);  // 마커 위도 경도
        SOS_bells[16] = new TMapPoint(35.177407, 126.910768);  // 마커 위도 경도
        SOS_bells[17] = new TMapPoint(35.176615, 126.909635);  // 마커 위도 경도
        SOS_bells[18] = new TMapPoint(35.175705, 126.910257);  // 마커 위도 경도
        SOS_bells[19] = new TMapPoint(35.174457, 126.911459);  // 마커 위도 경도


        for (int i = 0; i < BELL_NUMBER; i++){
            setMarkerItem(i);  // 마커들의 정보를 순서대로 입력하는 함수
        }


    }

    public void setMarkerItem(int num){      // 마커들의 정보를 순서대로 입력하는 함수
        SOS_bells_item[num] = new TMapMarkerItem();  // 마커 아이템 세팅

        SOS_bells_item[num].setTMapPoint(SOS_bells[num]);
        SOS_bells_item[num].setName("비상벨"+(num+1));
        SOS_bells_item[num].setVisible(TMapMarkerItem.VISIBLE);
        SOS_bells_item[num].setPosition((float) 0.5, (float) 1.0);


        tmapview.addMarkerItem("bell"+(num+1), SOS_bells_item[num]);

    }

    public void saveDistance(double lat2, double lon2, int count){

        double EARTH_R, Rad, radLat1, radLat2, radDist;
        double distance, ret;
        EARTH_R = 6371000.0;
        Rad = Math.PI/180;
        radLat1 = Rad * current_point.getLatitude();
        radLat2 = Rad * lat2;
        radDist = Rad * (current_point.getLongitude() - lon2);

        distance = Math.sin(radLat1) * Math.sin(radLat2);
        distance = distance + Math.cos(radLat1) * Math.cos(radLat2) * Math.cos(radDist);
        ret = EARTH_R * Math.acos(distance);


        p2pDistance[count] = ret;  // 현재 위치와 마커 사이의 거리 값 저장


    }



    public static int findFastPath(double p2pDistance[]) {

        double min = p2pDistance[0];

        int index = 0;

        for(int i=1; i<p2pDistance.length; i++){

            if(p2pDistance[i]<min){

                min = p2pDistance[i];

                index=i;
            }
        }
        return index;   // 가장 거리가 가까운 곳의 마커 인덱스 저장
    }
}