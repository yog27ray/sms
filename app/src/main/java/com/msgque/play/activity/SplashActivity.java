package com.msgque.play.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.msgque.play.App;
import com.msgque.play.R;
import com.msgque.play.common.SPHelper;

import javax.inject.Inject;

public class SplashActivity extends AppCompatActivity {
  @Inject
  SPHelper spHelper;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    App.getStorageComponent().inject(this);
    setContentView(R.layout.activity_splash);
    new CountDownTimer(2000, 2000) {
      @Override
      public void onTick(long millisUntilFinished) {

      }

      @Override
      public void onFinish() {
        moveToNextScreen();
      }
    }.start();
  }

  private void moveToNextScreen() {
    Intent i;
    if (spHelper.isUserLoggedIn()) {
      if (spHelper.isProfileCompleted()) i = CampaignListActivity.createIntent(this);
      else i = EditProfileActivity.createIntent(this);
    } else i = LoginActivity.createIntent(this);
    startActivity(i);
    finish();
  }
}
