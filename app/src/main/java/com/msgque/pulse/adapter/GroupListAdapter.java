package com.msgque.pulse.adapter;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.msgque.pulse.R;
import com.msgque.pulse.common.constant.CardTypes;
import com.msgque.pulse.databinding.RowGroupBinding;
import com.msgque.pulse.holder.GroupHolder;
import com.msgque.pulse.holder.LoadingHolder;
import com.msgque.pulse.model.GroupModel;

import java.util.ArrayList;
import java.util.List;

public class GroupListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
  private final List<GroupModel> groups = new ArrayList<>();
  private View.OnClickListener clickListener = new View.OnClickListener() {
    @Override
    public void onClick(View v) {
      GroupHolder holder = (GroupHolder) v.getTag();
      GroupModel item = groups.get(holder.getAdapterPosition());
      item.setSelected(!item.isSelected());
    }
  };

  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View cardView;
    RecyclerView.ViewHolder baseViewHolder;
    if (viewType == CardTypes.GROUP) {
      RowGroupBinding binding = DataBindingUtil
          .inflate(LayoutInflater.from(parent.getContext()), R.layout.row_group, parent, false);
      binding.setClickListener(clickListener);
      baseViewHolder = new GroupHolder(binding);
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
    RowGroupBinding binding = ((GroupHolder) viewHolder).binding;
    GroupModel item = groups.get(position);
    binding.setItem(item);
  }

  @Override
  public int getItemViewType(int position) {
    if (groups.get(position).id == CardTypes.LOADING) return CardTypes.LOADING;
    return CardTypes.GROUP;
  }

  @Override
  public int getItemCount() {
    return groups.size();
  }

  public void addAll(List<GroupModel> list) {
    groups.addAll(list);
  }

  public void clear() {
    groups.clear();
  }

  public void add(GroupModel item) {
    groups.add(item);
  }

  public List<GroupModel> getList() {
    return groups;
  }
}
