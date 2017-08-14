package com.msgque.play.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.msgque.play.R;
import com.msgque.play.activity.SendSmsActivity;
import com.msgque.play.common.constant.CardTypes;
import com.msgque.play.databinding.RowCampaignBinding;
import com.msgque.play.holder.CampaignHolder;
import com.msgque.play.holder.LoadingHolder;
import com.msgque.play.model.CampaignModel;

import java.util.ArrayList;
import java.util.List;

public class CampaignListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
  private final List<CampaignModel> campaigns = new ArrayList<>();
  private Context context;

  public CampaignListAdapter(Context context) {
    this.context = context;
  }

  private View.OnClickListener clickListener = new View.OnClickListener() {
    @Override
    public void onClick(View v) {
      CampaignHolder holder = (CampaignHolder) v.getTag();
      CampaignModel item = campaigns.get(holder.getAdapterPosition());
      context.startActivity(SendSmsActivity.createIntent(context, null, item.name));
    }
  };

  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View cardView;
    RecyclerView.ViewHolder baseViewHolder;
    if (viewType == CardTypes.CAMPAIGN) {
      RowCampaignBinding binding = DataBindingUtil
          .inflate(LayoutInflater.from(parent.getContext()), R.layout.row_campaign, parent, false);
      binding.setClickListener(clickListener);
      baseViewHolder = new CampaignHolder(binding);
    } else {
      cardView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_progress_bar,
          parent, false);
      baseViewHolder = new LoadingHolder(cardView);
    }
    return baseViewHolder;
  }

  @Override
  public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
    if (getItemViewType(position) == CardTypes.LOADING) return;
    RowCampaignBinding binding = ((CampaignHolder) viewHolder).binding;
    CampaignModel item = campaigns.get(position);
    binding.setItem(item);
  }

  @Override
  public int getItemViewType(int position) {
    if (campaigns.get(position).id == CardTypes.LOADING) return CardTypes.LOADING;
    return CardTypes.CAMPAIGN;
  }

  @Override
  public int getItemCount() {
    return campaigns.size();
  }

  public void addAll(List<CampaignModel> list) {
    campaigns.addAll(list);
  }

  public void clear() {
    campaigns.clear();
  }

  public void add(CampaignModel item) {
    campaigns.add(item);
  }

  public List<CampaignModel> getList() {
    return campaigns;
  }
}
