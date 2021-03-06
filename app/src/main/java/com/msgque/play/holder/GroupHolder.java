package com.msgque.play.holder;

import android.support.v7.widget.RecyclerView;

import com.msgque.play.databinding.RowGroupBinding;

public class GroupHolder extends RecyclerView.ViewHolder {

  public final RowGroupBinding binding;

  public GroupHolder(RowGroupBinding binding) {
    super(binding.getRoot());
    binding.setHolder(this);
    this.binding = binding;
  }
}