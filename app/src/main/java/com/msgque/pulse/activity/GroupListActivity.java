package com.msgque.pulse.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.msgque.pulse.App;
import com.msgque.pulse.R;
import com.msgque.pulse.adapter.GroupListAdapter;
import com.msgque.pulse.common.constant.ActivityRequestCodes;
import com.msgque.pulse.connectivity.ServerConnection;
import com.msgque.pulse.databinding.ActivityGroupListBinding;
import com.msgque.pulse.helper.ContactHelper;
import com.msgque.pulse.helper.ListHelper;
import com.msgque.pulse.helper.PermissionHelper;
import com.msgque.pulse.listener.ListListener;
import com.msgque.pulse.model.GroupModel;

import org.jdeferred.DoneCallback;
import org.jdeferred.FailCallback;

import java.util.List;

import javax.inject.Inject;

import timber.log.Timber;

public class GroupListActivity extends AppCompatActivity {

  @Inject
  PermissionHelper permissionHelper;
  @Inject
  ContactHelper contactHelper;
  @Inject
  ServerConnection conn;
  @Inject
  ListHelper listHelper;
  private ActivityGroupListBinding binding;
  private GroupListAdapter adapter;

  public static Intent createIntent(Context context) {
    return new Intent(context, GroupListActivity.class);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    App.getInternetComponent().inject(this);
    binding = DataBindingUtil.setContentView(this, R.layout.activity_group_list);
    binding.setActivity(this);

    LinearLayoutManager layoutManager = new LinearLayoutManager(this);
    binding.recyclerView.setLayoutManager(layoutManager);
    adapter = new GroupListAdapter();
    binding.recyclerView.setHasFixedSize(true);
    binding.recyclerView.setAdapter(adapter);
    onClickContactSync();
    fetchGroups();
  }

  private void fetchGroups() {
    conn.fetchGroups()
        .then(new DoneCallback<List<GroupModel>>() {
          @Override
          public void onDone(List<GroupModel> result) {
            adapter.clear();
            adapter.addAll(result);
            adapter.notifyDataSetChanged();
          }
        })
        .fail(new FailCallback<Exception>() {
          @Override
          public void onFail(Exception result) {
            Timber.e(result);
          }
        });
  }

  public void sendSms(View v) {
    List<GroupModel> items = listHelper
        .filter(adapter.getList(), new ListListener.Filter<GroupModel>() {
          @Override
          public boolean check(GroupModel item) {
            return item.isSelected();
          }
        });
    startActivity(SendSmsActivity.createIntent(this, items));
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                         @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    if (requestCode == ActivityRequestCodes.READ_CONTACTS
        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
      onClickContactSync();
    } else {
      fetchGroups();
    }
  }

  public void onClickContactSync() {
    if (permissionHelper.requestPermission(this, new String[]{Manifest.permission.READ_CONTACTS},
        ActivityRequestCodes.READ_CONTACTS)) {
      conn.sendContacts("All Contacts", contactHelper.getAllContacts(this))
          .then(new DoneCallback<Boolean>() {
            @Override
            public void onDone(Boolean result) {
              fetchGroups();
            }
          })
          .fail(new FailCallback<Exception>() {
            @Override
            public void onFail(Exception result) {
              fetchGroups();
            }
          });
    }
  }
}
