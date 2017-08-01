package com.msgque.pulse.helper;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

public class PermissionHelper {
  public boolean requestPermission(Activity activity, String[] permissions, int requestCode) {
    boolean permissionGranted = true;
    for (String permission : permissions) {
      if (ContextCompat
          .checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
        permissionGranted = false;
      }
    }
    if (!permissionGranted) {
      ActivityCompat.requestPermissions(activity, permissions, requestCode);
      return false;
    }
    return true;
  }
}
