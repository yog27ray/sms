package com.msgque.pulse.holder;

import android.support.v7.widget.RecyclerView;

import com.msgque.pulse.databinding.RowSenderIdBinding;

public class SenderIdHolder extends RecyclerView.ViewHolder {

  public final RowSenderIdBinding binding;

  public SenderIdHolder(RowSenderIdBinding binding) {
    super(binding.getRoot());
    binding.getRoot().setTag(this);
    this.binding = binding;
  }
}