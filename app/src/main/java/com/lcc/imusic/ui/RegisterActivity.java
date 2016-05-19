package com.lcc.imusic.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.JsonObject;
import com.lcc.imusic.R;
import com.lcc.imusic.base.activity.BaseActivity;
import com.lcc.imusic.manager.NetManager_;

import butterknife.Bind;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends BaseActivity {
    @Bind(R.id.username)
    EditText et_username;

    @Bind(R.id.password)
    EditText et_password;

    @Bind(R.id.password_confirm)
    EditText et_password_confirm;

    @Bind(R.id.safe_ques)
    EditText et_safe_ques;

    @Bind(R.id.safe_ans)
    EditText et_safe_ans;

    @Bind(R.id.register)
    Button bt_register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("注册");
        bt_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });
    }

    private void register() {
        String username = et_username.getText().toString().trim();
        if (TextUtils.isEmpty(username)) {
            toast("用户名为空");
            return;
        }

        String password = et_password.getText().toString().trim();
        if (TextUtils.isEmpty(password)) {
            toast("密码为空");
            return;
        }
        if (!password.equals(et_password_confirm.getText().toString().trim())) {
            toast("两次密码不一致");
            return;
        }
        String ques = et_safe_ques.getText().toString().trim();
        String ans = et_safe_ans.getText().toString().trim();
        if (TextUtils.isEmpty(ques) || TextUtils.isEmpty(ans)) {
            toast("密保问题不能为空");
            return;
        }
        NetManager_.API().signUp(username,password,ques,ans).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                toast(response.body().toString());
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                toast(t.toString());
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_register;
    }
}
