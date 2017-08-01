package com.msgque.pulse.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.msgque.pulse.R;
import com.msgque.pulse.databinding.RowSenderIdBinding;
import com.msgque.pulse.holder.SenderIdHolder;
import com.msgque.pulse.model.SenderIdModel;

import java.util.ArrayList;
import java.util.List;

public class SenderIdSpinnerAdapter extends ArrayAdapter<String> {

  private final LayoutInflater inflater;
  private final List<SenderIdModel> senderIds;
  private final List<String> list;

  public SenderIdSpinnerAdapter(Context context) {
    this(context, new ArrayList<String>());
  }

  public SenderIdSpinnerAdapter(Context context, ArrayList<String> list) {
    super(context, R.layout.row_route, list);
    this.list = list;
    this.senderIds = new ArrayList<>();
    inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
  }

  @Override
  public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
    return row(position, convertView, parent);
  }

  @Override
  @NonNull
  public View getView(int position, View convertView, @NonNull ViewGroup parent) {
    return row(position, convertView, parent);
  }

  private View row(int position, View convertView, @NonNull ViewGroup parent) {
    SenderIdHolder holder;
    if (convertView == null) {
      RowSenderIdBinding binding = DataBindingUtil.inflate(inflater, R.layout.row_sender_id, parent, false);
      convertView = binding.getRoot();
      holder = new SenderIdHolder(binding);
    } else holder = (SenderIdHolder) convertView.getTag();
    holder.binding.setItem(senderIds.get(position));
    return convertView;
  }

  public void addRoute(SenderIdModel route) {
    list.add(route.getName());
    senderIds.add(route);
  }

  public void addAllRoute(List<SenderIdModel> routes) {
    for (SenderIdModel r: routes) addRoute(r);
  }

  public void clearList() {
    list.clear();
    senderIds.clear();
  }

  public SenderIdModel getItemAt(int position) {
    return senderIds.get(position);
  }
}