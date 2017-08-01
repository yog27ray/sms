package com.msgque.pulse.adapter;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.msgque.pulse.R;
import com.msgque.pulse.common.constant.CardTypes;
import com.msgque.pulse.databinding.RowDomainBinding;
import com.msgque.pulse.holder.DomainHolder;
import com.msgque.pulse.holder.LoadingHolder;
import com.msgque.pulse.listener.AdapterListener;
import com.msgque.pulse.model.DomainModel;

import java.util.ArrayList;
import java.util.List;

public class DomainListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
  private final List<DomainModel> domains = new ArrayList<>();
  private AdapterListener.SelectionChange<DomainModel> selectionChangeListener;
  private View.OnClickListener clickListener = new View.OnClickListener() {
    @Override
    public void onClick(View v) {
      DomainHolder holder = (DomainHolder) v.getTag();
      DomainModel item = domains.get(holder.getAdapterPosition());
      item.setSelected(!item.isSelected());
      if (selectionChangeListener != null) selectionChangeListener.change(domains);
    }
  };

  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View cardView;
    RecyclerView.ViewHolder baseViewHolder;
    if (viewType == CardTypes.DOMAIN) {
      RowDomainBinding binding = DataBindingUtil
          .inflate(LayoutInflater.from(parent.getContext()), R.layout.row_domain, parent, false);
      binding.setClickListener(clickListener);
      baseViewHolder = new DomainHolder(binding);
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
    RowDomainBinding binding = ((DomainHolder) viewHolder).binding;
    DomainModel item = domains.get(position);
    binding.setItem(item);
  }

  @Override
  public int getItemViewType(int position) {
    return CardTypes.DOMAIN;
  }

  @Override
  public int getItemCount() {
    return domains.size();
  }

  public void addAll(List<DomainModel> list) {
    domains.addAll(list);
  }

  public void clear() {
    domains.clear();
  }

  public void add(DomainModel item) {
    domains.add(item);
  }

  public List<DomainModel> getList() {
    return domains;
  }

  public void setSelectionChangeListener(AdapterListener.SelectionChange<DomainModel>
                                             selectionChangeListener) {
    this.selectionChangeListener = selectionChangeListener;
  }
}
