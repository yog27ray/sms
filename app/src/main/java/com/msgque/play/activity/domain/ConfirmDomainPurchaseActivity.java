package com.msgque.play.activity.domain;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;

import com.google.gson.reflect.TypeToken;
import com.msgque.play.App;
import com.msgque.play.R;
import com.msgque.play.activity.CampaignListActivity;
import com.msgque.play.common.JsonConverter;
import com.msgque.play.common.constant.IntentConstant;
import com.msgque.play.connectivity.ServerConnection;
import com.msgque.play.databinding.ActivityConfirmDomainPurchaseBinding;
import com.msgque.play.helper.ListHelper;
import com.msgque.play.helper.UIHelper;
import com.msgque.play.listener.ListListener;
import com.msgque.play.model.DomainModel;

import org.jdeferred.DoneCallback;
import org.jdeferred.FailCallback;

import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;

public class ConfirmDomainPurchaseActivity extends AppCompatActivity {

  @Inject
  JsonConverter jsonConverter;
  @Inject
  ServerConnection conn;
  @Inject
  ListHelper listHelper;
  @Inject
  UIHelper uiHelper;
  private ActivityConfirmDomainPurchaseBinding binding;
  private List<DomainModel> domains;

  public static Intent createIntent(Context context, List<DomainModel> list) {
    Intent i = new Intent(context, ConfirmDomainPurchaseActivity.class);
    i.putExtra(IntentConstant.DOMAINS, new JsonConverter().toJson(list));
    return i;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    App.getInternetComponent().inject(this);
    domains = jsonConverter.fromJson(getIntent().getStringExtra(IntentConstant.DOMAINS),
        new TypeToken<List<DomainModel>>() {
        }.getType());
    binding = DataBindingUtil.setContentView(this, R.layout.activity_confirm_domain_purchase);
    binding.setActivity(this);
    binding.toolbar.setTitle(R.string.pricing);
    setSupportActionBar(binding.toolbar);

    float totalAmount = listHelper.reduce(domains, 0, new ListListener.Reduce<DomainModel>() {
      @Override
      public float reduce(DomainModel item) {
        Pattern regex = Pattern.compile("(\\d+(?:\\.\\d+)?)");
        Matcher matcher = regex.matcher(item.getPrice());
        if (matcher.find()) {
          return Float.parseFloat(matcher.group(0));
        }
        return 0;
      }
    });
    String currency = "₹";
    if (domains.size() > 0) {
      Pattern regex = Pattern.compile("[^0-9.]");
      Matcher matcher = regex.matcher(domains.get(0).getPrice());
      if (matcher.find()) {
        currency = matcher.group(0);
      }
    }
    binding.setCurrency(currency);
    binding.price.setText(String.format(Locale.ENGLISH, "%.2f/", totalAmount));
  }

  public void onClickConfirm() {
    uiHelper.startProgressBar(this, R.string.creating_with_dots);
    conn.createDomains(domains)
        .then(new DoneCallback<List<DomainModel>>() {
          @Override
          public void onDone(List<DomainModel> result) {
            uiHelper.stopProgressBar();
            Intent i = result.size() == 0
                ? CampaignListActivity.createIntent(ConfirmDomainPurchaseActivity.this)
                : CampaignListActivity.createIntent(ConfirmDomainPurchaseActivity.this,
                result.get(0).getName());
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
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
