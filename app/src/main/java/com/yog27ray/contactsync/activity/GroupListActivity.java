package com.yog27ray.contactsync.activity;

import android.Manifest;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.yog27ray.contactsync.App;
import com.yog27ray.contactsync.R;
import com.yog27ray.contactsync.adapter.GroupListAdapter;
import com.yog27ray.contactsync.common.constant.ActivityRequestCodes;
import com.yog27ray.contactsync.connectivity.ServerConnection;
import com.yog27ray.contactsync.databinding.ActivityGroupListBinding;
import com.yog27ray.contactsync.helper.ContactHelper;
import com.yog27ray.contactsync.helper.ListHelper;
import com.yog27ray.contactsync.helper.PermissionHelper;
import com.yog27ray.contactsync.listener.ListListener;
import com.yog27ray.contactsync.model.GroupModel;

import org.jdeferred.DoneCallback;
import org.jdeferred.FailCallback;

import java.util.List;

import javax.inject.Inject;

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

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    binding = DataBindingUtil.setContentView(this, R.layout.activity_group_list);
    binding.setActivity(this);
    App.getInternetComponent().inject(this);

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
    if (requestCode == ActivityRequestCodes.READ_CONTACTS) {
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
