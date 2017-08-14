package com.msgque.play.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.msgque.play.App;
import com.msgque.play.R;
import com.msgque.play.adapter.RouteSpinnerAdapter;
import com.msgque.play.adapter.SenderIdAutoCompleteAdapter;
import com.msgque.play.common.JsonConverter;
import com.msgque.play.common.constant.IntentConstant;
import com.msgque.play.connectivity.ServerConnection;
import com.msgque.play.databinding.ActivitySendSmsBinding;
import com.msgque.play.helper.ListHelper;
import com.msgque.play.listener.ListListener;
import com.msgque.play.model.CampaignModel;
import com.msgque.play.model.GroupModel;
import com.msgque.play.model.RouteModel;
import com.msgque.play.model.SenderIdModel;
import com.msgque.play.model.SmsModel;

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
  private SenderIdAutoCompleteAdapter senderIdAdapter;

  public static Intent createIntent(Context context, List<GroupModel> groups, String campaign) {
    JsonConverter jsonConverter = new JsonConverter();
    Intent i = new Intent(context, SendSmsActivity.class);
    if (groups != null) i.putExtra(IntentConstant.GROUP_IDS, jsonConverter.toJson(groups));
    if (campaign != null) i.putExtra(IntentConstant.CAMPAIGN, campaign);
    return i;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    binding = DataBindingUtil.setContentView(this, R.layout.activity_send_sms);
    binding.setActivity(this);
    binding.toolbar.setTitle(R.string.compose);
    setSupportActionBar(binding.toolbar);
    if (getSupportActionBar() != null) {
      getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    SmsModel sms = new SmsModel();
    binding.setSms(sms);
    App.getInternetComponent().inject(this);

    if (getIntent().hasExtra(IntentConstant.GROUP_IDS)) {
      groupIds = jsonConverter.fromJson(getIntent().getStringExtra(IntentConstant.GROUP_IDS),
          new TypeToken<List<GroupModel>>() {
          }.getType());
    }
    if (getIntent().hasExtra(IntentConstant.CAMPAIGN)) {
      sms.setCampaign(getIntent().getStringExtra(IntentConstant.CAMPAIGN));
      binding.senderId.setEnabled(false);
      binding.routes.setEnabled(false);
      binding.campaign.setEnabled(false);
      binding.message.requestFocus();
    }

    senderIdAdapter = new SenderIdAutoCompleteAdapter(this);
    binding.senderId.setThreshold(1);
    binding.senderId.setAdapter(senderIdAdapter);

    routeAdapter = new RouteSpinnerAdapter(this);
    binding.setRouteAdapter(routeAdapter);

    binding.senderId.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {

      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {

      }

      @Override
      public void afterTextChanged(Editable s) {
        if (s.toString().compareTo(s.toString().toUpperCase()) != 0) {
          binding.senderId.setText(s.toString().toUpperCase());
          binding.senderId.setSelection(s.toString().length());
        }
      }
    });
    fetchRoutes();
    fetchSenderIds();
  }

  private void fetchCampaign(String campaign) {
    conn.fetchCampaign(campaign)
        .then(new DoneCallback<CampaignModel>() {
          @Override
          public void onDone(CampaignModel campaign) {
            binding.senderId.setText(campaign.senderId);
            if (campaign.numbers != null) binding.numbers.setText(campaign.numbers);
            int i = 0;
            for (; i < routeAdapter.getRoutes().size(); i++) {
              if (routeAdapter.getRoutes().get(i).getId() == campaign.routeId) {
                binding.routes.setSelection(i);
                break;
              }
            }
          }
        });
  }

  public void onRouteSelected(AdapterView<?> parent, View view, int position, long id) {
    RouteModel route = routeAdapter.getItemAt(position);
    binding.getSms().setRouteId(route.getId());
  }

  public void onSenderIdClick(AdapterView<?> parent, View view, int position, long id) {
    SenderIdModel senderId = (SenderIdModel) senderIdAdapter.getItem(position);
    binding.senderId.setText(senderId.getName());
    binding.senderId.setSelection(senderId.getName().length());
  }

  private void fetchSenderIds() {
    String fl = "id,name,senderIdStatusId";
    String status = "1,2";
    conn.fetchSenderIds(fl, status)
        .then(new DoneCallback<List<SenderIdModel>>() {
          @Override
          public void onDone(List<SenderIdModel> result) {
            senderIdAdapter.addAll(result);
            senderIdAdapter.notifyDataSetChanged();
          }
        });
  }

  private void fetchRoutes() {
    conn.fetchRoutes()
        .then(new DoneCallback<List<RouteModel>>() {
          @Override
          public void onDone(List<RouteModel> result) {
            routeAdapter.addAllRoute(result);
            routeAdapter.notifyDataSetChanged();
            if (getIntent().hasExtra(IntentConstant.CAMPAIGN)) {
              fetchCampaign(getIntent().getStringExtra(IntentConstant.CAMPAIGN));
            }
          }
        });
  }

  private void sendSms() {
    SmsModel sms = binding.getSms();
    if (sms.routeId < 0) return;

    sms.routeId = routeAdapter.getItemAt(binding.routes.getSelectedItemPosition()).getId();
    if (groupIds != null) {
      sms.groupId = listHelper.join(groupIds, new ListListener.Join<GroupModel>() {
        @Override
        public String join(GroupModel item) {
          return String.valueOf(item.id);
        }
      });
    }
    Timber.e(new Gson().toJson(sms));
//    conn.sendSms(sms);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.compose, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case android.R.id.home:
        onBackPressed();
        return true;
      case R.id.send:
        sendSms();
        return true;
    }
    return super.onOptionsItemSelected(item);
  }

}
