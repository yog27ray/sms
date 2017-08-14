package com.msgque.play.holder;

import android.support.v7.widget.RecyclerView;

import com.msgque.play.databinding.RowCampaignBinding;

public class CampaignHolder extends RecyclerView.ViewHolder {

  public final RowCampaignBinding binding;

  public CampaignHolder(RowCampaignBinding binding) {
    super(binding.getRoot());
    binding.setHolder(this);
    this.binding = binding;
  }
}