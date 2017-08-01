package com.msgque.pulse.activity.domain;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;

import com.msgque.pulse.App;
import com.msgque.pulse.R;
import com.msgque.pulse.adapter.DomainListAdapter;
import com.msgque.pulse.connectivity.ServerConnection;
import com.msgque.pulse.databinding.ActivitySearchDomainBinding;
import com.msgque.pulse.helper.ListHelper;
import com.msgque.pulse.helper.UIHelper;
import com.msgque.pulse.listener.AdapterListener;
import com.msgque.pulse.listener.ListListener;
import com.msgque.pulse.model.DomainModel;

import org.jdeferred.DoneCallback;

import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

public class SearchDomainActivity extends AppCompatActivity {

  @Inject
  ServerConnection conn;
  @Inject
  ListHelper listHelper;
  @Inject
  UIHelper uiHelper;

  private ActivitySearchDomainBinding binding;
  private DomainListAdapter adapter;
  private AdapterListener.SelectionChange<DomainModel> selectionChangeListener =
      new AdapterListener.SelectionChange<DomainModel>() {
        @Override
        public void change(List<DomainModel> list) {
          list = listHelper.filter(list, new ListListener.Filter<DomainModel>() {
            @Override
            public boolean check(DomainModel item) {
              return item.isSelected();
            }
          });
          float totalAmount = listHelper.reduce(list, 0, new ListListener.Reduce<DomainModel>() {
            @Override
            public float reduce(DomainModel item) {
              return Float.parseFloat(item.getPrice());
            }
          });
          binding.totalAmount.setText(String.format(Locale.ENGLISH, "$%.2f", totalAmount));
        }
      };

  public static Intent createIntent(Context context) {
    return new Intent(context, SearchDomainActivity.class);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    App.getInternetComponent().inject(this);
    binding = DataBindingUtil.setContentView(this, R.layout.activity_search_domain);
    binding.setActivity(this);
    binding.toolbar.setTitle(R.string.register_new_domain);
    setSupportActionBar(binding.toolbar);

    LinearLayoutManager layoutManager = new LinearLayoutManager(this);
    binding.recyclerView.setLayoutManager(layoutManager);
    adapter = new DomainListAdapter();
    binding.recyclerView.setHasFixedSize(true);
    binding.recyclerView.setAdapter(adapter);
    adapter.setSelectionChangeListener(selectionChangeListener);
  }

  public void getDomains() {
    String domain = binding.search.getEditableText().toString();
    if (domain.isEmpty()) return;
    uiHelper.hideKeyboard(this);
    conn.getDomainAvailability(domain)
        .then(new DoneCallback<List<DomainModel>>() {
          @Override
          public void onDone(List<DomainModel> result) {
            adapter.clear();
            adapter.addAll(result);
            selectionChangeListener.change(result);
            adapter.notifyDataSetChanged();
          }
        });
  }

  public void onClickNext() {
    startActivity(ConfirmDomainPurchaseActivity.createIntent(this,
        listHelper.filter(adapter.getList(), new ListListener.Filter<DomainModel>() {
          @Override
          public boolean check(DomainModel item) {
            return item.isSelected();
          }
        })));
  }

}
