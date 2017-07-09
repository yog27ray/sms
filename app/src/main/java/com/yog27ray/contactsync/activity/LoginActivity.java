package com.yog27ray.contactsync.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;

import com.yog27ray.contactsync.App;
import com.yog27ray.contactsync.R;
import com.yog27ray.contactsync.common.JsonConverter;
import com.yog27ray.contactsync.common.SPUtility;
import com.yog27ray.contactsync.connectivity.ServerConnection;
import com.yog27ray.contactsync.model.UserModel;

import org.jdeferred.DoneCallback;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

public class LoginActivity extends AppCompatActivity {

  @BindView(R.id.number)
  AppCompatEditText number;
  @BindView(R.id.otp)
  AppCompatEditText otp;
  @Inject
  SPUtility spUtility;
  @Inject
  ServerConnection conn;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);
    App.getInternetComponent().inject(this);
    ButterKnife.bind(this);
    if (spUtility.isUserLoggedIn()) {
      startActivity(new Intent(this, GroupListActivity.class));
      finish();
    }
  }

  @OnClick(R.id.login)
  public void onClickLogin() {
    final String numberText = number.getText().toString();
    final String otpText = otp.getText().toString();
    conn.verifyOTP(String.format("91%s", numberText), otpText)
        .then(new DoneCallback<Boolean>() {
          @Override
          public void onDone(Boolean result) {
            conn.loginUser(String.format("91%s", numberText), otpText)
                .then(new DoneCallback<UserModel>() {
                  @Override
                  public void onDone(UserModel user) {
                    Timber.e(new JsonConverter().toJson(user));
                    startActivity(new Intent(LoginActivity.this, GroupListActivity.class));
                    finish();
                  }
                });
          }
        });
  }

  @OnClick(R.id.generate_otp)
  public void onClickGenerateOTP() {
    String numberText = number.getText().toString();
    conn.generateOtp(String.format("91%s", numberText));
  }
}
