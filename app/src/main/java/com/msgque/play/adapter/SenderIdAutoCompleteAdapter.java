package com.msgque.play.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import com.msgque.play.R;
import com.msgque.play.databinding.RowAutocompleteBinding;
import com.msgque.play.holder.AutocompleteHolder;
import com.msgque.play.model.SenderIdModel;

import java.util.ArrayList;
import java.util.List;

public class SenderIdAutoCompleteAdapter extends BaseAdapter implements Filterable {
  private final LayoutInflater inflater;
  List<SenderIdModel> displayDetailList = new ArrayList<>();
  List<SenderIdModel> senderIdList = new ArrayList<>();

  public SenderIdAutoCompleteAdapter(Context context) {
    inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
  }

  @Override
  public int getCount() {
    return displayDetailList.size();
  }

  @Override
  public Object getItem(int position) {
    return displayDetailList.get(position);
  }

  @Override
  public long getItemId(int position) {
    return position;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    if (convertView == null) {
      RowAutocompleteBinding binding = DataBindingUtil.inflate(inflater, R.layout.row_autocomplete,
          parent, false);
      convertView = binding.getRoot();
      new AutocompleteHolder(binding);
    }
    SenderIdModel senderId = displayDetailList.get(position);
    AutocompleteHolder holder = (AutocompleteHolder) convertView.getTag();
    holder.binding.name.setText(senderId.getName());
    return convertView;
  }

  @Override
  public Filter getFilter() {
    return new Filter() {
      @Override
      protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults results = new FilterResults();
        displayDetailList.clear();
        if (constraint != null) {
          for (SenderIdModel senderId : senderIdList) {
            if (senderId.getName().toLowerCase().contains(constraint.toString().toLowerCase())) {
              displayDetailList.add(senderId);
            }
          }
        }
        results.values = displayDetailList;
        results.count = displayDetailList.size();
        return results;
      }

      @Override
      protected void publishResults(CharSequence constraint, FilterResults results) {
        if (results != null && results.count > 0) {
          notifyDataSetChanged();
        } else {
          notifyDataSetInvalidated();
        }
      }
    };
  }

  public void addAll(List<SenderIdModel> items) {
    senderIdList.addAll(items);
  }
}
