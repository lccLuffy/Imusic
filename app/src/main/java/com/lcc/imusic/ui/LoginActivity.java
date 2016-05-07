package com.lcc.imusic.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.lcc.imusic.R;
import com.lcc.imusic.base.activity.BaseActivity;
import com.lcc.imusic.bean.LoginBean;
import com.lcc.imusic.bean.Msg;
import com.lcc.imusic.manager.NetManager_;
import com.lcc.imusic.manager.UserManager;

import butterknife.Bind;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.username)
    EditText et_username;

    @Bind(R.id.password)
    EditText et_password;

    @Bind(R.id.login)
    Button bt_login;

    @Bind(R.id.register)
    TextView register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("登录");
        actionBar.setDisplayHomeAsUpEnabled(false);
        if (UserManager.isLogin()) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
            return;
        }
        bt_login.setOnClickListener(this);
        register.setOnClickListener(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.login) {
            login();
        } else if (v.getId() == R.id.register) {
            startActivity(new Intent(this, RegisterActivity.class));
        }
    }


    ProgressDialog progressDialog;

    private void login() {
        String username = et_username.getText().toString().trim();
        String password = et_password.getText().toString().trim();

        if (TextUtils.isEmpty(username)) {
            toast("用户名为空");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            toast("密码为空");
            return;
        }
        if (progressDialog == null)
            progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("正在登录...");
        progressDialog.setCancelable(false);
        progressDialog.show();


        Call<Msg<LoginBean>> call = NetManager_.API().login(username, password);
        call.enqueue(new Callback<Msg<LoginBean>>() {
            @Override
            public void onResponse(Call<Msg<LoginBean>> call, Response<Msg<LoginBean>> response) {
                progressDialog.dismiss();
                Msg<LoginBean> msg = response.body();
                if (msg.Code == 100) {
                    UserManager.save(msg.Result);
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                } else {
                    toast("登陆失败");
                }
            }

            @Override
            public void onFailure(Call<Msg<LoginBean>> call, Throwable t) {
                progressDialog.dismiss();
                toast("登陆失败");
            }
        });

        /*startActivity(new Intent(LoginActivity.this, MainActivity.class));
        progressDialog.dismiss();
        finish();*/
    }
}
