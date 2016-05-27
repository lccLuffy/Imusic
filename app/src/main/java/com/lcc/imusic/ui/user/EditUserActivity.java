package com.lcc.imusic.ui.user;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.lcc.imusic.R;
import com.lcc.imusic.base.activity.BaseActivity;
import com.lcc.imusic.bean.LoginBean;
import com.lcc.imusic.bean.Msg;
import com.lcc.imusic.manager.NetManager_;
import com.lcc.imusic.manager.UserManager;
import com.lcc.imusic.wiget.StateLayout;
import com.orhanobut.logger.Logger;

import butterknife.Bind;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditUserActivity extends BaseActivity {

    @Bind(R.id.stateLayout)
    StateLayout stateLayout;

    @Bind(R.id.safe_ques)
    EditText et_safe_ques;

    @Bind(R.id.safe_ans)
    EditText et_safe_ans;

    @Bind(R.id.real_name)
    EditText realName;

    @Bind(R.id.phone_number)
    EditText phoneNum;

    @Bind(R.id.email_address)
    EditText emailAddress;

    @Bind(R.id.update_me)
    Button updateMe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(UserManager.username());

        updateMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateMe();
            }
        });
        stateLayout.setErrorAndEmptyAction(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                me();
            }
        });
    }

    private void me() {
        stateLayout.showProgressView();
        NetManager_.API().me().enqueue(new Callback<Msg<LoginBean>>() {
            @Override
            public void onResponse(Call<Msg<LoginBean>> call, Response<Msg<LoginBean>> response) {
                stateLayout.showContentView();
                LoginBean loginBean = response.body().Result;
                if (loginBean != null) {
                    realName.setText(loginBean.realname);
                    phoneNum.setText(loginBean.phone);
                    emailAddress.setText(loginBean.mail);
                }
                else {
                    Logger.i("null login bean");
                }

            }

            @Override
            public void onFailure(Call<Msg<LoginBean>> call, Throwable t) {
                stateLayout.showErrorView(t.toString());
            }
        });
    }

    private void updateMe() {
        NetManager_.API().updateMe(
                et_safe_ques.getText().toString(),
                et_safe_ans.getText().toString(),
                realName.getText().toString(),
                phoneNum.getText().toString(),
                emailAddress.getText().toString()
        ).enqueue(new Callback<Msg<LoginBean>>() {
            @Override
            public void onResponse(Call<Msg<LoginBean>> call, Response<Msg<LoginBean>> response) {
                if (response.body() != null && response.body().Code == 100) {
                    LoginBean loginBean = response.body().Result;
                } else {
                    toast("更新失败");
                }
            }

            @Override
            public void onFailure(Call<Msg<LoginBean>> call, Throwable t) {
                toast("更新失败");
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_edit_user;
    }
}
