package com.project.xiangmu.activities;

import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.gson.Gson;
import com.project.xiangmu.App;
import com.project.xiangmu.R;
import com.project.xiangmu.base.BaseActivity;
import com.project.xiangmu.entity.UserBean;
import com.project.xiangmu.utils.StringUtil;

import java.util.Map;

import androidx.appcompat.widget.AppCompatTextView;
import butterknife.BindView;

public class RegisterActivity extends BaseActivity {
    @BindView(R.id.input_name)
    EditText inputName;
    @BindView(R.id.input_id_card)
    EditText inputIdCard;
    @BindView(R.id.input_phone)
    EditText inputPhone;
    @BindView(R.id.input_password)
    EditText inputPassword;
    @BindView(R.id.input_password_again)
    EditText inputPasswordAgain;
    @BindView(R.id.btn_register)
    AppCompatTextView btnRegister;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_register;
    }

    @Override
    protected void init() {
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = inputName.getText().toString();
                String mobile = inputPhone.getText().toString();
                String no = inputIdCard.getText().toString();
                String pwd = inputPassword.getText().toString();
                String pwd_2 = inputPasswordAgain.getText().toString();
                if (TextUtils.isEmpty(no) || TextUtils.isEmpty(name) || TextUtils.isEmpty(mobile)
                        || TextUtils.isEmpty(pwd) || TextUtils.isEmpty(pwd_2)) {
                    showToast("Please complete the information.");
                    return;
                }

                if (!pwd.equals(pwd_2)) {
                    showToast("The passwords do not match.");
                    return;
                }
                UserBean userBean = new UserBean();
                if (App.mUserMap == null || App.mUserMap.size() == 0){
                    userBean.setId(1);
                }else{
                    userBean.setId(App.mUserMap.size() + 1);
                }
                userBean.setMobile(mobile);
                userBean.setUser_id(System.currentTimeMillis() + "");
                userBean.setName(name);
                userBean.setPassword(pwd);
                userBean.setHead_url("https://pic1.zhimg.com/80/v2-0051dad77dcbe85d8c51e148387a3424_720w.webp");
                userBean.student_num = no;
                userBean.setStudent_num(no);
                Map<String, Object> map = App.mUserMap;
                if (map == null || map.size() == 0) {
                    map = new ArrayMap<>();
                    map.put(name,new Gson().toJson(userBean));
                    App.mDatabase.child("user").setValue(map);
                    showToast("Sign in Success");
                    finish();
                }else {
                    String o = (String) map.get(name);
                    if (StringUtil.isEmpty(o)) {
                        map.put(name,new Gson().toJson(userBean));
                        Log.e("hao", "RegisterActivity onClick(): "+map.toString());
                        Log.e("hao", "RegisterActivity onClick(): "+map.size());
                        App.mDatabase.child("user").setValue(map);
                        showToast("Sign in Success");
                        finish();
                    }else {
                        showToast("Username is already taken. Please choose a different username.");
                    }
                }

            }
        });
    }

}
