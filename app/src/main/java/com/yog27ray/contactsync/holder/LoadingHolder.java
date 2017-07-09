package com.yog27ray.contactsync.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import butterknife.ButterKnife;

public class LoadingHolder extends RecyclerView.ViewHolder {

  public LoadingHolder(View view) {
    super(view);
    ButterKnife.bind(this, view);
  }
}