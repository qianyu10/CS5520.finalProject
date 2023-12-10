package com.project.xiangmu.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.project.xiangmu.App;
import com.project.xiangmu.R;
import com.project.xiangmu.widget.CircleImageView;
import com.wildma.pictureselector.PictureBean;
import com.wildma.pictureselector.PictureSelector;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * 修改用户信息
 */
public class ChangeUserInfoActivity extends AppCompatActivity {


    @BindView(R.id.rl_back)
    RelativeLayout rlBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_right)
    ImageView tvRight;
    @BindView(R.id.image_head)
    CircleImageView imageHead;
    @BindView(R.id.rl_head)
    RelativeLayout rlHead;
    @BindView(R.id.tv_name)
    EditText tvName;
    @BindView(R.id.tv_id_card)
    EditText tvIdCard;
    @BindView(R.id.tv_mobile)
    EditText tvMobile;
    @BindView(R.id.tv_password)
    EditText tvPassword;
    @BindView(R.id.tv_change)
    TextView tvChange;

    String path= "";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_userinfo);

        ButterKnife.bind(this);
        tvTitle.setText("Change User Info");
        if (App.getInstance().userBean != null){
            tvName.setText(App.getInstance().userBean.getName());
            tvIdCard.setText(App.getInstance().userBean.getStudent_num());
            tvMobile.setText(App.getInstance().userBean.getMobile());
            tvPassword.setText(App.getInstance().userBean.getPassword());
            if (!TextUtils.isEmpty(App.getInstance().userBean.getHead_url())){
                path = App.getInstance().userBean.getHead_url();
                Glide.with(this).load(App.getInstance().userBean.getHead_url()).into(imageHead);
            }
        }

    }



    @OnClick({R.id.rl_back, R.id.rl_head, R.id.tv_change})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_back:
                finish();
                break;
            case R.id.rl_head:
                PictureSelector
                        .create(ChangeUserInfoActivity.this, PictureSelector.SELECT_REQUEST_CODE)
                        .selectPicture();
                break;
            case R.id.tv_change:
                String name = tvName.getText().toString();
                String idcard = tvIdCard.getText().toString();
                String mobile = tvMobile.getText().toString();
                String password = tvPassword.getText().toString();
                if (TextUtils.isEmpty(name) || TextUtils.isEmpty(idcard) || TextUtils.isEmpty(mobile) ||TextUtils.isEmpty(password) ){
                    Toast.makeText(getApplicationContext(),"请检查输入信息是否完整",Toast.LENGTH_SHORT).show();
                    return;
                }else {
                    App.getInstance().userBean.setHead_url(path);
                    App.getInstance().userBean.setMobile(mobile);
                    App.getInstance().userBean.setName(name);
                    App.getInstance().userBean.setPassword(password);
                    App.getInstance().userBean.setStudent_num(idcard);
                    App.mUserMap.put(name,new Gson().toJson(App.getInstance().userBean));
                    App.mDatabase.child("user").setValue(App.mUserMap);
                    Toast.makeText(getApplicationContext(),"Change Success",Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PictureSelector.SELECT_REQUEST_CODE) {
            if (data != null) {
                PictureBean pictureBean = data.getParcelableExtra(PictureSelector.PICTURE_RESULT);
                Glide.with(this).load(pictureBean.getPath()).into(imageHead);
                path = pictureBean.getPath();
            }
        }
    }
}
