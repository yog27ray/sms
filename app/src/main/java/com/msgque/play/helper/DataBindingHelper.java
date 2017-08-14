package com.msgque.play.helper;

import android.databinding.BindingAdapter;
import android.support.v7.widget.AppCompatTextView;

public class DataBindingHelper {

  @BindingAdapter("android:textStyle")
  public static void setTypeface(AppCompatTextView view, int style) {
    view.setTypeface(null, style);
  }

}
