package com.yog27ray.contactsync.helper;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;

import com.yog27ray.contactsync.listener.ListListener;
import com.yog27ray.contactsync.model.ContactModel;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public class ContactHelper {

  private final ListHelper listHelper;

  public ContactHelper(ListHelper listHelper) {
    this.listHelper = listHelper;
  }

  public List<ContactModel> getAllContacts(Context context) {
    List<ContactModel> list = new ArrayList<>();
    Cursor phones =context.getContentResolver()
        .query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null,
        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");
    while (phones.moveToNext()) {
      String name = phones.getString(phones
          .getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
      String phoneNumber = phones.getString(phones
          .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
      list.add(new ContactModel(name, phoneNumber));
    }
    phones.close();
    Timber.e(":"+list.size());
    list = listHelper.removeDuplicate(list, new ListListener.Duplicate<ContactModel>() {
      @Override
      public boolean isDuplicate(ContactModel a, ContactModel b) {
        return a.number.equalsIgnoreCase(b.number);
      }
    });
    list = listHelper.map(list, new ListListener.Map<ContactModel, ContactModel>() {
      @Override
      public ContactModel map(ContactModel item) {
        item.number = item.number.replaceAll(" ", "");
        return item;
      }
    });
    return list;
  }
}
