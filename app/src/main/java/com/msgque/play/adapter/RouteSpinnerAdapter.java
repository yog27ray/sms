package com.msgque.play.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.msgque.play.R;
import com.msgque.play.databinding.RowRouteBinding;
import com.msgque.play.holder.RouteHolder;
import com.msgque.play.model.RouteModel;

import java.util.ArrayList;
import java.util.List;

public class RouteSpinnerAdapter extends ArrayAdapter<String> {

  private final LayoutInflater inflater;
  private final List<RouteModel> routes;
  private final List<String> list;

  public RouteSpinnerAdapter(Context context) {
    this(context, new ArrayList<String>());
  }

  public RouteSpinnerAdapter(Context context, ArrayList<String> list) {
    super(context, R.layout.row_route, list);
    this.list = list;
    this.routes = new ArrayList<>();
    inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
  }

  @Override
  public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
    if (convertView == null) {
      RowRouteBinding binding = DataBindingUtil.inflate(inflater, R.layout.row_route, parent, false);
      convertView = binding.getRoot();
      new RouteHolder(binding);
    }
    RouteHolder holder = (RouteHolder) convertView.getTag();
    holder.binding.setItem(routes.get(position));
    return convertView;
  }

  @Override
  @NonNull
  public View getView(int position, View convertView, @NonNull ViewGroup parent) {
    if (convertView == null) {
      RowRouteBinding binding = DataBindingUtil.inflate(inflater, R.layout.row_route, parent, false);
      convertView = binding.getRoot();
      new RouteHolder(binding);
    }
    RouteHolder holder = (RouteHolder) convertView.getTag();
    holder.binding.setItem(routes.get(position));
    return convertView;
  }

  public void addRoute(RouteModel route) {
    list.add(route.getName());
    routes.add(route);
  }

  public void addAllRoute(List<RouteModel> routes) {
    for (RouteModel r: routes) addRoute(r);
  }

  public void clearList() {
    list.clear();
    routes.clear();
  }

  public RouteModel getItemAt(int position) {
    return routes.get(position);
  }

  public List<RouteModel> getRoutes() {
    return routes;
  }
}