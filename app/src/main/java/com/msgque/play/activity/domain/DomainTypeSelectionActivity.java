package com.msgque.play.activity.domain;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.msgque.play.R;
import com.msgque.play.activity.CampaignListActivity;
import com.msgque.play.databinding.ActivityDomainTypeSelectionBinding;

public class DomainTypeSelectionActivity extends AppCompatActivity{

  private ActivityDomainTypeSelectionBinding binding;

  public static Intent createIntent(Context context) {
    return new Intent(context, DomainTypeSelectionActivity.class);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    binding = DataBindingUtil.setContentView(this, R.layout.activity_domain_type_selection);
    binding.setActivity(this);
  }

  public void onClickRegisterNewDomain(View view) {
    startActivity(SearchDomainActivity.createIntent(this));
    finish();
  }

  public void onClickSkip(View view) {
    startActivity(CampaignListActivity.createIntent(this));
    finish();
  }

  public void onClickExsisting(View view) {
    startActivity(ExistingDomainActivity.createIntent(this));
    finish();
  }
}
