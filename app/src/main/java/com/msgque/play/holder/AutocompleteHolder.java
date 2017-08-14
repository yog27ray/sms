package com.msgque.play.holder;

import android.support.v7.widget.RecyclerView;

import com.msgque.play.databinding.RowAutocompleteBinding;

public class AutocompleteHolder extends RecyclerView.ViewHolder {

  public final RowAutocompleteBinding binding;

  public AutocompleteHolder(RowAutocompleteBinding binding) {
    super(binding.getRoot());
    binding.setHolder(this);
    binding.getRoot().setTag(this);
    this.binding = binding;
  }
}