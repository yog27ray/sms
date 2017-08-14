package com.msgque.play.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.msgque.play.App;
import com.msgque.play.R;
import com.msgque.play.adapter.CampaignListAdapter;
import com.msgque.play.common.constant.RandomConstant;
import com.msgque.play.connectivity.ServerConnection;
import com.msgque.play.dagger.component.InternetComponent;
import com.msgque.play.databinding.ActivityCampaignListBinding;
import com.msgque.play.helper.ContactHelper;
import com.msgque.play.helper.ListHelper;
import com.msgque.play.helper.PermissionHelper;
import com.msgque.play.model.CampaignModel;

import org.jdeferred.DoneCallback;
import org.jdeferred.FailCallback;

import java.util.List;

import javax.inject.Inject;

import timber.log.Timber;

public class CampaignListActivity extends NavBaseActivity {

  @Inject
  PermissionHelper permissionHelper;
  @Inject
  ContactHelper contactHelper;
  @Inject
  ServerConnection conn;
  @Inject
  ListHelper listHelper;
  private ActivityCampaignListBinding binding;
  private CampaignListAdapter adapter;

  public static Intent createIntent(Context context) {
    return new Intent(context, CampaignListActivity.class);
  }

  @Override
  protected void injectDependencies(InternetComponent component) {
    App.getInternetComponent().inject(this);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    binding = setUpNewLayout(R.layout.activity_campaign_list);
    binding.setActivity(this);
    markRowSelected(RandomConstant.NAV_CAMPAIGN);

    if (getSupportActionBar() != null) {
      getSupportActionBar().setHomeAsUpIndicator(R.drawable.logo);
    }
    LinearLayoutManager layoutManager = new LinearLayoutManager(this);
    binding.recyclerView.setLayoutManager(layoutManager);
    adapter = new CampaignListAdapter(this);
    binding.recyclerView.setHasFixedSize(true);
    binding.recyclerView.setAdapter(adapter);
    fetchCampaigns();
  }

  private void fetchCampaigns() {
    conn.fetchCampaigns()
        .then(new DoneCallback<List<CampaignModel>>() {
          @Override
          public void onDone(List<CampaignModel> result) {
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
    startActivity(SendSmsActivity.createIntent(this, null, null));
  }
}
