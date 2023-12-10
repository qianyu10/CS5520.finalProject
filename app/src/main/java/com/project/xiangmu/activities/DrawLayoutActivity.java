package com.project.xiangmu.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.project.xiangmu.App;
import com.project.xiangmu.R;
import com.project.xiangmu.adapter.MainPagerAdapter;
import com.project.xiangmu.fragment.TabMineFragment;
import com.project.xiangmu.widget.CircleImageView;
import com.project.xiangmu.widget.NoScrollViewPager;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;

public class DrawLayoutActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener{

    @BindView(R.id.main_pager)
    NoScrollViewPager mainPager;
    @BindView(R.id.tab_home)
    RadioButton tabHome;
    @BindView(R.id.tab_dynamic)
    RadioButton tabDynamic;
    @BindView(R.id.tab_rank)
    RadioButton tabRank;
    @BindView(R.id.tab_me)
    RadioButton tabMe;
    @BindView(R.id.radioGroup)
    RadioGroup radioGroup;
    @BindView(R.id.nav_view)
    NavigationView navView;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    private long clickTime;
    private CircleImageView imageView;
    private TextView tv_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw_layout);
        ButterKnife.bind(this);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        View view = navigationView.getHeaderView(0);
        imageView = view.findViewById(R.id.imageView);
        tv_name = view.findViewById(R.id.tv_name);

        if (TextUtils.isEmpty(App.getInstance().userBean.getHead_url())){
            Glide.with(this).load(R.mipmap.default_icon).into(imageView);
        }else {
            Glide.with(this).load(App.getInstance().userBean.getHead_url()).into(imageView);
        }
        tv_name.setText(App.getInstance().userBean.getName());

        List<Fragment> fragments = new ArrayList<>();

        fragments.add(new TabZyFragment());
        fragments.add(new TabLianxirenFragment());
        fragments.add(new TanCircleFragment());
        fragments.add(new TabMineFragment());

        MainPagerAdapter adapter = new MainPagerAdapter(getSupportFragmentManager(), fragments);
        mainPager.setOffscreenPageLimit(2);
        mainPager.setAdapter(adapter);
        radioGroup.setOnCheckedChangeListener(this);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DrawLayoutActivity.this,UserInfoActivity.class));
                drawer.closeDrawer(Gravity.LEFT);
            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();
                switch (id){
                    case R.id.nav_home:
                        startActivity(new Intent(DrawLayoutActivity.this,ChangeUserInfoActivity.class));
                        drawer.closeDrawer(Gravity.LEFT);
                        break;
                    case R.id.nav_gallery:
                        startActivity(new Intent(DrawLayoutActivity.this,AboutActivity.class));
                        drawer.closeDrawer(Gravity.LEFT);
                        break;
                    case R.id.nav_slideshow:
                        try {
                            //正常退出
                            android.os.Process.killProcess(android.os.Process.myPid());
                            System.exit(0);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                }
                return false;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (imageView != null){
            if (TextUtils.isEmpty(App.getInstance().userBean.getHead_url())){
                Glide.with(this).load(R.mipmap.default_icon).into(imageView);
            }else {
                Glide.with(this).load(App.getInstance().userBean.getHead_url()).into(imageView);
            }
            tv_name.setText(App.getInstance().userBean.getName());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.draw_layout, menu);
        return true;
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        switch (i) {
            case R.id.tab_home:
                mainPager.setCurrentItem(0, false);
                break;
            case R.id.tab_dynamic:
                mainPager.setCurrentItem(1, false);
                break;
            case R.id.tab_rank:
                mainPager.setCurrentItem(2, false);
                break;
            case R.id.tab_me:
                mainPager.setCurrentItem(3, false);
                break;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    private void exit() {
        if ((System.currentTimeMillis() - clickTime) > 2000) {
            Toast.makeText(this, "Click to return the app", Toast.LENGTH_SHORT).show();
            clickTime = System.currentTimeMillis();
        } else {
            try {
                //正常退出
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(0);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}