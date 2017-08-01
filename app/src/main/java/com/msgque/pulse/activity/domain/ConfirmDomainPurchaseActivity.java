package com.msgque.pulse.activity.domain;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;

import com.google.gson.reflect.TypeToken;
import com.msgque.pulse.App;
import com.msgque.pulse.R;
import com.msgque.pulse.activity.GroupListActivity;
import com.msgque.pulse.common.JsonConverter;
import com.msgque.pulse.common.constant.IntentConstant;
import com.msgque.pulse.connectivity.ServerConnection;
import com.msgque.pulse.databinding.ActivityConfirmDomainPurchaseBinding;
import com.msgque.pulse.helper.ListHelper;
import com.msgque.pulse.helper.UIHelper;
import com.msgque.pulse.listener.ListListener;
import com.msgque.pulse.model.DomainModel;

import org.jdeferred.DoneCallback;
import org.jdeferred.FailCallback;

import java.util.List;
import java.util.Locale;

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
        return Float.parseFloat(item.getPrice());
      }
    });
    binding.price.setText(String.format(Locale.ENGLISH, "%.2f/", totalAmount));
  }

  public void onClickConfirm() {
    conn.createDomains(domains)
        .then(new DoneCallback<Boolean>() {
          @Override
          public void onDone(Boolean result) {
            Intent i = GroupListActivity.createIntent(ConfirmDomainPurchaseActivity.this);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
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
