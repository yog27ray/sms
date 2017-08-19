package com.msgque.play.activity.domain;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.msgque.play.App;
import com.msgque.play.R;
import com.msgque.play.adapter.DomainListAdapter;
import com.msgque.play.connectivity.ServerConnection;
import com.msgque.play.databinding.ActivitySearchDomainBinding;
import com.msgque.play.helper.ListHelper;
import com.msgque.play.helper.UIHelper;
import com.msgque.play.listener.AdapterListener;
import com.msgque.play.listener.ListListener;
import com.msgque.play.model.DomainModel;

import org.jdeferred.DoneCallback;

import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
              Pattern regex = Pattern.compile("(\\d+(?:\\.\\d+)?)");
              Matcher matcher = regex.matcher(item.getPrice());
              if (matcher.find()) {
                return Float.parseFloat(matcher.group(0));
              }
              return 0;
            }
          });
          String currency = "₹";
          if (list.size() > 0) {
            Pattern regex = Pattern.compile("[^0-9.]");
            Matcher matcher = regex.matcher(list.get(0).getPrice());
            if (matcher.find()) {
              currency = matcher.group(0);
            }
          }
          binding.totalAmount.setText(String.format(Locale.ENGLISH, "%s%.2f", currency, totalAmount));
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
    List<DomainModel> selected = listHelper.filter(adapter.getList(),
        new ListListener.Filter<DomainModel>() {
          @Override
          public boolean check(DomainModel item) {
            return item.isSelected();
          }
        });
    if (selected.size() > 0) {
      startActivity(ConfirmDomainPurchaseActivity.createIntent(this, selected));
    }
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
        onClickNext();
        return true;
    }
    return super.onOptionsItemSelected(item);
  }

}
