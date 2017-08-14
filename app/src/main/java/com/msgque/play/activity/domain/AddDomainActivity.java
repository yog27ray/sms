package com.msgque.play.activity.domain;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.msgque.play.R;
import com.msgque.play.activity.CampaignListActivity;
import com.msgque.play.databinding.ActivityAddDomainBinding;

public class AddDomainActivity extends AppCompatActivity{

  private ActivityAddDomainBinding binding;

  public static Intent createIntent(Context context) {
    return new Intent(context, AddDomainActivity.class);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    binding = DataBindingUtil.setContentView(this, R.layout.activity_add_domain);
    binding.setActivity(this);
  }

  public void onClickRegisterNewDomain() {
    startActivity(SearchDomainActivity.createIntent(this));
    finish();
  }

  public void onClickSkip() {
    startActivity(CampaignListActivity.createIntent(this));
    finish();
  }

}
