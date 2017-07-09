package com.yog27ray.contactsync.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.google.common.base.Joiner;
import com.google.gson.reflect.TypeToken;
import com.yog27ray.contactsync.App;
import com.yog27ray.contactsync.R;
import com.yog27ray.contactsync.adapter.RouteSpinnerAdapter;
import com.yog27ray.contactsync.adapter.SenderIdSpinnerAdapter;
import com.yog27ray.contactsync.common.JsonConverter;
import com.yog27ray.contactsync.common.constant.IntentConstant;
import com.yog27ray.contactsync.connectivity.ServerConnection;
import com.yog27ray.contactsync.databinding.ActivitySendSmsBinding;
import com.yog27ray.contactsync.helper.ListHelper;
import com.yog27ray.contactsync.listener.ListListener;
import com.yog27ray.contactsync.model.GroupModel;
import com.yog27ray.contactsync.model.RouteModel;
import com.yog27ray.contactsync.model.SenderIdModel;
import com.yog27ray.contactsync.model.SmsModel;

import org.jdeferred.DoneCallback;

import java.util.List;

import javax.inject.Inject;

import timber.log.Timber;

public class SendSmsActivity extends AppCompatActivity {
  @Inject
  JsonConverter jsonConverter;
  @Inject
  ListHelper listHelper;
  @Inject
  ServerConnection conn;

  private ActivitySendSmsBinding binding;
  private List<GroupModel> groupIds;
  private RouteSpinnerAdapter routeAdapter;
  private SenderIdSpinnerAdapter senderIdAdapter;
  private View.OnClickListener sendSmsListener = new View.OnClickListener() {
    @Override
    public void onClick(View v) {
      if (routeAdapter.getCount() == 0 || senderIdAdapter.getCount() == 0) {
        return;
      }
      SmsModel sms = new SmsModel();
      sms.numbers = binding.numbers.getText().toString();
      sms.text = binding.message.getText().toString();
      sms.campaign = binding.campaign.getText().toString();
      sms.senderId = senderIdAdapter.getItemAt(binding.senderIds.getSelectedItemPosition())
          .getName();
      sms.routeId = routeAdapter.getItemAt(binding.senderIds.getSelectedItemPosition()).getId();
      if (groupIds != null) {
        sms.groupId = Joiner.on(",").join(listHelper.map(groupIds,
            new ListListener.Map<GroupModel, Integer>() {
              @Override
              public Integer map(GroupModel item) {
                return item.id;
              }
            }));
      }
      Timber.e(jsonConverter.toJson(sms));
      conn.sendSms(sms);
    }
  };

  public static Intent createIntent(Context context, List<GroupModel> groups) {
    JsonConverter jsonConverter = new JsonConverter();
    Intent i = new Intent(context, SendSmsActivity.class);
    i.putExtra(IntentConstant.GROUP_IDS, jsonConverter.toJson(groups));
    return i;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    binding = DataBindingUtil.setContentView(this, R.layout.activity_send_sms);
    binding.setActivity(this);
    binding.setSendSmsListener(sendSmsListener);
    App.getInternetComponent().inject(this);

    if (getIntent().hasExtra(IntentConstant.GROUP_IDS)) {
      groupIds = jsonConverter.fromJson(getIntent().getStringExtra(IntentConstant.GROUP_IDS),
          new TypeToken<List<GroupModel>>() {
          }.getType());
    }

    routeAdapter = new RouteSpinnerAdapter(this);
    senderIdAdapter = new SenderIdSpinnerAdapter(this);

    binding.setRouteAdapter(routeAdapter);
    binding.setSenderIdAdapter(senderIdAdapter);

    fetchRoutes();
    fetchSenderIds();
  }

  private void fetchSenderIds() {
    String fl = "id,name,senderIdStatusId";
    String status = "1,2";
    conn.fetchSenderIds(fl, status)
        .then(new DoneCallback<List<SenderIdModel>>() {
          @Override
          public void onDone(List<SenderIdModel> result) {
            senderIdAdapter.addAllRoute(result);
            senderIdAdapter.notifyDataSetChanged();
          }
        });
  }

  private void fetchRoutes() {
    conn.fetchRoutes()
        .then(new DoneCallback<List<RouteModel>>() {
          @Override
          public void onDone(List<RouteModel> result) {
            Timber.e("Response:"+result.size());
            routeAdapter.addAllRoute(result);
            routeAdapter.notifyDataSetChanged();
          }
        });
  }

}
