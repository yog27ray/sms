package com.msgque.pulse.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.msgque.pulse.App;
import com.msgque.pulse.R;
import com.msgque.pulse.activity.domain.AddDomainActivity;
import com.msgque.pulse.common.SPHelper;
import com.msgque.pulse.connectivity.ServerConnection;
import com.msgque.pulse.databinding.ActivityEditProfileBinding;
import com.msgque.pulse.helper.UIHelper;
import com.msgque.pulse.model.DomainModel;

import org.jdeferred.DeferredManager;
import org.jdeferred.DoneCallback;
import org.jdeferred.FailCallback;
import org.jdeferred.impl.DefaultDeferredManager;
import org.jdeferred.multiple.MultipleResults;
import org.jdeferred.multiple.OneReject;

import java.util.List;

import javax.inject.Inject;

public class EditProfileActivity extends AppCompatActivity {

  @Inject
  SPHelper spHelper;
  @Inject
  ServerConnection conn;
  @Inject
  UIHelper uiHelper;

  private ActivityEditProfileBinding binding;
  private View.OnClickListener submitListener = new View.OnClickListener() {
    @Override
    public void onClick(View v) {
      uiHelper.startProgressBar(EditProfileActivity.this, R.string.updating_with_dots);
      DeferredManager deferredManager = new DefaultDeferredManager();
      deferredManager.when(conn.updateUserInfo(binding.getUser()),
          conn.getDomain())
          .then(new DoneCallback<MultipleResults>() {
            @Override
            public void onDone(MultipleResults result) {
              List<DomainModel> domains = (List<DomainModel>) result.get(1).getResult();
              uiHelper.stopProgressBar();
              spHelper.setCurrentUser(binding.getUser());
              spHelper.setProfileCompleted(true);
              startActivity(domains.size() > 0
                  ? GroupListActivity.createIntent(EditProfileActivity.this)
                  : AddDomainActivity.createIntent(EditProfileActivity.this));
              finish();
            }
          })
          .fail(new FailCallback<OneReject>() {
            @Override
            public void onFail(OneReject result) {
              uiHelper.stopProgressBar();
              Snackbar.make(binding.getRoot(), R.string.error_unable_to_save, Snackbar.LENGTH_LONG)
                  .show();
            }
          });
    }
  };

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    App.getInternetComponent().inject(this);
    binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_profile);
    binding.toolbar.setTitle(R.string.profile);
    setSupportActionBar(binding.toolbar);
    binding.setUser(spHelper.getCurrentUser());
    binding.submit.setOnClickListener(submitListener);
  }

  public static Intent createIntent(Context context) {
    return new Intent(context, EditProfileActivity.class);
  }
}
