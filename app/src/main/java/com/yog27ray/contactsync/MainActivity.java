package com.yog27ray.contactsync;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.yog27ray.contactsync.common.constant.ActivityRequestCodes;
import com.yog27ray.contactsync.connectivity.ServerConnection;
import com.yog27ray.contactsync.helper.ContactHelper;
import com.yog27ray.contactsync.helper.PermissionHelper;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity {

  @Inject
  PermissionHelper permissionHelper;
  @Inject
  ContactHelper contactHelper;
  @Inject
  ServerConnection conn;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    App.getInternetComponent().inject(this);
    ButterKnife.bind(this);
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    if (requestCode == ActivityRequestCodes.READ_CONTACTS) {
      onClickContactSync();
    }
  }

  @OnClick(R.id.contact_sync)
  public void onClickContactSync() {
    if (permissionHelper.requestPermission(this, new String[]{Manifest.permission.READ_CONTACTS},
        ActivityRequestCodes.READ_CONTACTS)) {
      conn.sendContacts(contactHelper.getAllContacts(this));
    }
  }
}
