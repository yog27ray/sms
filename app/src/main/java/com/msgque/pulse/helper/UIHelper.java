package com.msgque.pulse.helper;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class UIHelper {
  private ProgressDialog progressDialog;

  public void startProgressBar(Context context, @StringRes int resId) {
    startProgressBar(context, context.getString(resId));
  }

  public void startProgressBar(Context context, String message) {
    if (progressDialog != null) stopProgressBar();
    progressDialog = new ProgressDialog(context);
    progressDialog.setMessage(message);
    progressDialog.setCancelable(false);
    progressDialog.show();
  }

  public void stopProgressBar() {
    if (progressDialog == null) return;
    progressDialog.dismiss();
    progressDialog = null;
  }

  public void hideKeyboard(Context context) {
    View view = ((AppCompatActivity) context).getCurrentFocus();
    if (view != null) {
      InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
      imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
  }
}
