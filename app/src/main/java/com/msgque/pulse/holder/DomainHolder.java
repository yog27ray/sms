package com.msgque.pulse.holder;

import android.support.v7.widget.RecyclerView;

import com.msgque.pulse.databinding.RowDomainBinding;

public class DomainHolder extends RecyclerView.ViewHolder {

  public final RowDomainBinding binding;

  public DomainHolder(RowDomainBinding binding) {
    super(binding.getRoot());
    binding.setHolder(this);
    this.binding = binding;
  }
}