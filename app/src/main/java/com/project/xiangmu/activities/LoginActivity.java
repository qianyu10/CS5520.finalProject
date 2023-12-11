package com.project.xiangmu.activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.google.gson.Gson;
import com.project.xiangmu.App;
import com.project.xiangmu.R;
import com.project.xiangmu.base.BaseActivity;
import com.project.xiangmu.entity.UserBean;
import com.project.xiangmu.utils.SPUtil;
import com.project.xiangmu.utils.StringUtil;

import java.util.Map;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity {
    @BindView(R.id.input_name)
    EditText inputName;
    @BindView(R.id.input_password)
    EditText inputPassword;
    @BindView(R.id.btn_login)
    AppCompatTextView btnLogin;
    @BindView(R.id.btn_register)
    AppCompatTextView btnRegister;
    @BindView(R.id.checkbox)
    CheckBox checkbox;
    boolean isSave = false;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void init() {
        initPermission();
        SPUtil spUtil = new SPUtil(getApplicationContext(), "save");
        String state = spUtil.getString("state", "");
        if (!TextUtils.isEmpty(state)) {
            String name = spUtil.getString("name", "");
            String password = spUtil.getString("password", "");
            checkbox.setChecked(true);
            inputPassword.setText(password);
            inputName.setText(name);
        }
        checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                isSave = b;
                if (b) {
                    SPUtil spUtil = new SPUtil(getApplicationContext(), "save");
                    spUtil.putString("name", inputName.getText().toString());
                    spUtil.putString("password", inputPassword.getText().toString());
                    spUtil.putString("state", "true");
                } else {
                    SPUtil spUtil = new SPUtil(getApplicationContext(), "save");
                    spUtil.putString("name", "");
                    spUtil.putString("password", "");
                    spUtil.putString("state", "true");
                }
            }
        });
    }

    private void initPermission() {
        if(ContextCompat.checkSelfPermission(LoginActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(LoginActivity.this,new String[]{android.Manifest.permission.ACCESS_NETWORK_STATE
                    ,android.Manifest.permission.READ_EXTERNAL_STORAGE,android.Manifest.permission.WRITE_EXTERNAL_STORAGE},123);
        }
    }


    @OnClick({R.id.btn_login, R.id.btn_register})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                Login();
                break;
            case R.id.btn_register:
                showActivity(LoginActivity.this, RegisterActivity.class);
                break;
        }
    }


    private void Login() {
        String name = inputName.getText().toString();
        String pwd = inputPassword.getText().toString();
        if (TextUtils.isEmpty(name)) {
            showToast("type in user name");
            return;
        }

        if (TextUtils.isEmpty(pwd)) {
            showToast("type in password");
            return;
        }

        Map<String, Object> map = App.mUserMap;
        if (map == null || map.size() == 0) {
            showToast("account does not exist ");
        } else {
            String s = (String) map.get(name);
            if (s == null || "".equals(s)) {
                showToast("account does not exist");
            } else {
                UserBean user = new Gson().fromJson(s, UserBean.class);
                if (user == null) {
                    showToast("Login failed. Please check if the input information is correct");
                } else {
                    String password = user.getPassword();
                    if (StringUtil.isEmpty(password) || pwd.endsWith(password)) {
                        if (isSave) {
                            SPUtil spUtil = new SPUtil(getApplicationContext(), "save");
                            spUtil.putString("name", inputName.getText().toString());
                            spUtil.putString("password", inputPassword.getText().toString());
                            spUtil.putString("state", "true");
                        }
                        App.getInstance().userBean = user;
                        Log.e("User info", App.getInstance().userBean.toString());
                        startActivity(new Intent(LoginActivity.this, DrawLayoutActivity.class));
                        finish();
                    }
                }
            }
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
