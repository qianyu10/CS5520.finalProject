package com.project.xiangmu;

import android.app.Application;
import android.util.ArrayMap;
import android.util.Log;

import com.baidu.location.LocationClient;
import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.xiangmu.entity.UserBean;

import org.xutils.DbManager;
import org.xutils.x;

import java.util.Map;


public class App extends Application {

    private static App instance;
    public static DatabaseReference mDatabase;
    public static Map<String, Object> mUserMap;

    public static String TAG = "-------";

    public UserBean userBean = null;
    public static DbManager dbManager;//数据库存储
    static DbManager.DaoConfig daoConfig = new DbManager.DaoConfig()
            .setDbVersion(1)
            .setDbUpgradeListener(new DbManager.DbUpgradeListener() {
                @Override
                public void onUpgrade(DbManager db, int oldVersion, int newVersion) {

                }
            });

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(this);
//        mDatabase = FirebaseDatabase.getInstance("https://test-fc97d-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();
//          mDatabase = FirebaseDatabase.getInstance("https://pethometest-default-rtdb.firebaseio.com/").getReference();
          mDatabase = FirebaseDatabase.getInstance("https://pethome-254f8-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();


        instance = this;
        x.Ext.init(this);
        //x.Ext.setDebug(BuildConfig.DEBUG); // 是否输出debug日志, 开启debug会影响性能.
        if (dbManager == null) {
            dbManager = x.getDb(daoConfig);
        }
        getUserData();
        SDKInitializer.setAgreePrivacy(getApplicationContext(),true);
        SDKInitializer.initialize(getApplicationContext());
        //自4.3.0起，百度地图SDK所有接口均支持百度坐标和国测局坐标，用此方法设置您使用的坐标类型.
        //包括BD09LL和GCJ02两种坐标，默认是BD09LL坐标。
        SDKInitializer.setCoordType(CoordType.BD09LL);
        LocationClient.setAgreePrivacy(true);
    }


    public static App getInstance() {
        return instance;
    }

    private void getUserData() {
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //拿普通类的数据
//                mUserMap = dataSnapshot.getValue(Map.class);

                //拿map数据
                mUserMap = new ArrayMap<>();
                for (DataSnapshot snapshotNode: dataSnapshot.getChildren()) {
                    Log.e("hao", "App onDataChange(): "+snapshotNode.getKey());
                    Log.e("hao", "App onDataChange(): "+snapshotNode.getValue());
                    mUserMap.put(snapshotNode.getKey(),snapshotNode.getValue());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        mDatabase.child("user").addValueEventListener(postListener);
    }
}
