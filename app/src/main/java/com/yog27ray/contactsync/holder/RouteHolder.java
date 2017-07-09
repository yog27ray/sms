package com.yog27ray.contactsync.holder;

import android.support.v7.widget.RecyclerView;

import com.yog27ray.contactsync.databinding.RowRouteBinding;

public class RouteHolder extends RecyclerView.ViewHolder {

  public final RowRouteBinding binding;

  public RouteHolder(RowRouteBinding binding) {
    super(binding.getRoot());
    binding.getRoot().setTag(this);
    this.binding = binding;
  }
}