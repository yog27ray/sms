package com.msgque.play.activity.domain;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.msgque.play.App;
import com.msgque.play.R;
import com.msgque.play.activity.CampaignListActivity;
import com.msgque.play.connectivity.ServerConnection;
import com.msgque.play.databinding.ActivityExistingDomainBinding;
import com.msgque.play.helper.UIHelper;
import com.msgque.play.model.DomainModel;

import org.jdeferred.DoneCallback;
import org.jdeferred.FailCallback;

import java.util.Arrays;

import javax.inject.Inject;

public class ExistingDomainActivity extends AppCompatActivity {

  @Inject
  ServerConnection conn;
  @Inject
  UIHelper uiHelper;

  private ActivityExistingDomainBinding binding;
  public static Intent createIntent(Context context) {
    return new Intent(context, ExistingDomainActivity.class);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    App.getInternetComponent().inject(this);
    binding = DataBindingUtil.setContentView(this, R.layout.activity_existing_domain);
    binding.setActivity(this);
    DomainModel domain = new DomainModel();
    domain.setExisting(true);
    binding.setDomain(domain);
  }

  public void onClickConfirm(View view) {
    uiHelper.startProgressBar(this, R.string.creating_with_dots);
    conn.createDomains(Arrays.asList(binding.getDomain()))
        .then(new DoneCallback<Boolean>() {
          @Override
          public void onDone(Boolean result) {
            uiHelper.stopProgressBar();
            Intent i = CampaignListActivity.createIntent(ExistingDomainActivity.this);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
            finish();
          }
        })
        .fail(new FailCallback<Exception>() {
          @Override
          public void onFail(Exception result) {
            uiHelper.stopProgressBar();
            Snackbar.make(binding.getRoot(), R.string.error_unable_to_save, Snackbar.LENGTH_LONG)
                .show();
          }
        });
  }
}
