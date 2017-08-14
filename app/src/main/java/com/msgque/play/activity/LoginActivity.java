package com.msgque.play.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.msgque.play.App;
import com.msgque.play.R;
import com.msgque.play.common.SPHelper;
import com.msgque.play.connectivity.ServerConnection;
import com.msgque.play.databinding.ActivityLoginBinding;
import com.msgque.play.helper.LoginHelper;
import com.msgque.play.helper.UIHelper;
import com.msgque.play.model.UserModel;

import org.jdeferred.DoneCallback;
import org.jdeferred.FailCallback;

import javax.inject.Inject;

import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity {

  @Inject
  SPHelper spHelper;
  @Inject
  ServerConnection conn;
  @Inject
  LoginHelper loginHelper;
  @Inject
  UIHelper uiHelper;

  private ActivityLoginBinding binding;

  public static Intent createIntent(Context context) {
    return new Intent(context, LoginActivity.class);
  }

  private View.OnClickListener googleListener = new View.OnClickListener() {
    @Override
    public void onClick(View v) {
      spHelper.reset();
      loginHelper.googleSignIn();
    }
  };

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
    App.getInternetComponent().inject(this);
    ButterKnife.bind(this);
    loginHelper.initGoogleLogin(this);
    loginHelper.setLoginListener(loginListener);
    binding.googleLogin.setOnClickListener(googleListener);
  }

  private LoginHelper.LoginListener loginListener = new LoginHelper.LoginListener() {
    @Override
    public void success(UserModel user) {
      uiHelper.startProgressBar(LoginActivity.this, "Loading...");
      conn.signUpUserGoogle(user)
          .then(new DoneCallback<Boolean>() {
            @Override
            public void onDone(Boolean result) {
              uiHelper.stopProgressBar();
              startActivity(EditProfileActivity.createIntent(LoginActivity.this));
              finish();
            }
          })
          .fail(new FailCallback<Exception>() {
            @Override
            public void onFail(Exception result) {
              uiHelper.stopProgressBar();
              Snackbar.make(binding.getRoot(), R.string.error_login, Snackbar.LENGTH_LONG).show();
            }
          });
    }

    @Override
    public void failure() {
      Snackbar.make(binding.getRoot(), R.string.error_login, Snackbar.LENGTH_SHORT).show();
    }
  };

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    loginHelper.onActivityResult(requestCode, resultCode, data);
  }

}
