package com.msgque.play.holder;

import android.support.v7.widget.RecyclerView;

import com.msgque.play.databinding.RowSenderIdBinding;

public class SenderIdHolder extends RecyclerView.ViewHolder {

  public final RowSenderIdBinding binding;

  public SenderIdHolder(RowSenderIdBinding binding) {
    super(binding.getRoot());
    binding.getRoot().setTag(this);
    this.binding = binding;
  }
}