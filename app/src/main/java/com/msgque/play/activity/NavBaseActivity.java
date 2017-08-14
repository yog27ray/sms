package com.msgque.play.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;

import com.msgque.play.App;
import com.msgque.play.R;
import com.msgque.play.common.SPHelper;
import com.msgque.play.common.constant.RandomConstant;
import com.msgque.play.connectivity.ServerConnection;
import com.msgque.play.dagger.component.InternetComponent;
import com.msgque.play.databinding.ActivityNavBaseBinding;
import com.msgque.play.databinding.RowNavItemBinding;
import com.msgque.play.helper.LoginHelper;
import com.msgque.play.model.MetaModel;
import com.msgque.play.model.NavItem;
import com.msgque.play.model.UserModel;
import com.squareup.picasso.Picasso;

import org.jdeferred.DoneCallback;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import static android.databinding.DataBindingUtil.inflate;

public abstract class NavBaseActivity extends AppCompatActivity {
  @Inject
  protected SPHelper spHelper;
  @Inject
  protected ServerConnection conn;
  @Inject
  protected LoginHelper loginHelper;
  @Inject
  protected UserModel user;
  protected ActionBar actionBar;
  protected ActivityNavBaseBinding navBaseBinding;
  private LayoutInflater inflater;
  private String defaultSelection;
  private String currentSelected;
  private List<RowNavItemBinding> rowBindingList;

  protected abstract void injectDependencies(InternetComponent component);

  protected void hideToolbar() {
    navBaseBinding.toolbar.setVisibility(View.GONE);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    navBaseBinding = DataBindingUtil.setContentView(this, R.layout.activity_nav_base);
    injectDependencies(App.getInternetComponent());
    loginHelper.initGoogleLogin(this);
    fetchAddress();
  }

  protected <T extends ViewDataBinding> T setUpNewLayout(@LayoutRes int childViewId) {
    inflater = LayoutInflater.from(this);
    ViewDataBinding childActivityBinding = inflate(inflater, childViewId,
        navBaseBinding.frame, false);
    navBaseBinding.frame.addView(childActivityBinding.getRoot());
    navBaseBinding.toolbar.setContentInsetStartWithNavigation(0);
    navBaseBinding.menu.setUser(user);
    navBaseBinding.menu.setLoginListener(loginListener);
    navBaseBinding.menu.setLogoutListener(logoutListener);
    setSupportActionBar(navBaseBinding.toolbar);
    actionBar = getSupportActionBar();
    if (actionBar != null) {
      actionBar.setDisplayHomeAsUpEnabled(true);
      actionBar.setHomeButtonEnabled(true);
    }
    navBaseBinding.drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
      @Override
      public void onDrawerSlide(View drawerView, float slideOffset) {

      }

      @Override
      public void onDrawerOpened(View drawerView) {

      }

      @Override
      public void onDrawerClosed(View drawerView) {

      }

      @Override
      public void onDrawerStateChanged(int newState) {

      }
    });
    return (T) childActivityBinding;
  }

  private void fetchAddress() {
    UserModel user = spHelper.getCurrentUser();
    conn.getMetaDetail(user != null ? user.email : null)
        .then(new DoneCallback<MetaModel>() {
          @Override
          public void onDone(MetaModel meta) {
            navBaseBinding.menu.address.setText(meta.address);
            if (meta.name != null && !meta.name.isEmpty()) {
              navBaseBinding.menu.name.setText(meta.name);
            } else if (spHelper.getCurrentUser() != null) {
              navBaseBinding.menu.name.setText(spHelper.getCurrentUser().name);
            } else {
              navBaseBinding.menu.name.setVisibility(View.GONE);
            }

            LinearLayoutCompat menuList = navBaseBinding.menu.menuList;
            rowBindingList = new ArrayList<>();
            meta.menu.add(0, new NavItem(RandomConstant.NAV_CAMPAIGN,
                CampaignListActivity.createIntent(NavBaseActivity.this)));
            for (NavItem item : meta.menu) {
              RowNavItemBinding rowBinding = DataBindingUtil.inflate(inflater,
                  R.layout.row_nav_item, menuList, false);
              rowBindingList.add(rowBinding);
              rowBinding.setItem(item);
              rowBinding.setListener(navItemClickListener);
              menuList.addView(rowBinding.getRoot());
            }
            menuList.post(new Runnable() {
              @Override
              public void run() {
                markRowSelected(null);
              }
            });
          }
        });
  }

  protected void markRowSelected(String title) {
    if (rowBindingList == null) {
      defaultSelection = title;
      return;
    }
    if (title == null) {
      title = defaultSelection;
    }
    for (RowNavItemBinding binding : rowBindingList) {
      NavItem row = (NavItem) binding.getRoot().getTag();
      if (!row.inApp) {
        Picasso.with(this)
            .load(title != null && row.name.compareTo(title) == 0 ? row.activeIcon : row.icon)
            .into(binding.image);
      } else {

      }
      if (title != null && row.name.compareTo(title) == 0) currentSelected = row.name;
    }
    defaultSelection = null;
  }

  private View.OnClickListener navItemClickListener = new View.OnClickListener() {
    @Override
    public void onClick(View v) {
      NavItem item = (NavItem) v.getTag();
      if (currentSelected != null && item.name.equalsIgnoreCase(currentSelected)) {
        return;
      }
      navBaseBinding.drawerLayout.closeDrawer(GravityCompat.START);
      if (item.outsideapp) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(item.link));
        startActivity(browserIntent);
        return;
      }
      currentSelected = item.name;
      startActivity(item.inApp
          ? item.open
          : WebActivity.createIntent(NavBaseActivity.this, item.name, item.link));
    }
  };


  private View.OnClickListener loginListener = new View.OnClickListener() {
    @Override
    public void onClick(View v) {
      openLoginActivity();
    }
  };
  private View.OnClickListener logoutListener = new View.OnClickListener() {
    @Override
    public void onClick(View v) {
      loginHelper.setLogoutListener(new LoginHelper.LogoutListener() {
        @Override
        public void logout(boolean status) {
          spHelper.reset();
          openLoginActivity();
        }
      });
      loginHelper.logout();
    }
  };

  private void openLoginActivity() {
    startActivity(LoginActivity.createIntent(NavBaseActivity.this));
    finish();
  }


  @Override
  public void onBackPressed() {
    if (navBaseBinding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
      navBaseBinding.drawerLayout.closeDrawer(GravityCompat.START);
      return;
    }
    super.onBackPressed();
  }

  @Override
  protected void onResume() {
    super.onResume();
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case android.R.id.home:
        navBaseBinding.drawerLayout.openDrawer(GravityCompat.START);
        return true;
    }
    return super.onOptionsItemSelected(item);
  }
}
