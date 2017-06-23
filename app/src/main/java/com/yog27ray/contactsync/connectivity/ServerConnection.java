package com.yog27ray.contactsync.connectivity;

import com.squareup.okhttp.ResponseBody;
import com.yog27ray.contactsync.App;
import com.yog27ray.contactsync.common.JsonConverter;
import com.yog27ray.contactsync.common.SPUtility;
import com.yog27ray.contactsync.common.constant.EndPoints;
import com.yog27ray.contactsync.model.AccessToken;
import com.yog27ray.contactsync.model.ContactModel;
import com.yog27ray.contactsync.model.RequestBodyModel;
import com.yog27ray.contactsync.model.UserModel;

import org.jdeferred.DoneCallback;
import org.jdeferred.Promise;
import org.jdeferred.impl.DeferredObject;

import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;
import timber.log.Timber;


public class ServerConnection {
  private final RetroFitConnector apiConnector;
  private final SPUtility spUtility;
  private final JsonConverter jsonConverter;

  public ServerConnection(List<RetroFitConnector> retroFitConnector, JsonConverter jsonConverter,
                          SPUtility SPUtility) {
    apiConnector = retroFitConnector.get(0);
    this.spUtility = SPUtility;
    this.jsonConverter = jsonConverter;
  }

  public Promise<UserModel, Exception, Object> loginUser(final String username, final String password) {
    final DeferredObject<UserModel, Exception, Object> deferred = new DeferredObject<>();
    ApiService service = apiConnector.createLoginService(ApiService.class);
    Call<AccessToken> request = service.loginUser(username, password, "password",
        EndPoints.CLIENT_APP_ID, EndPoints.CLIENT_SECRET);
    request.enqueue(new Callback<AccessToken>() {
      @Override
      public void onResponse(Response<AccessToken> response, Retrofit retrofit) {
        if (response.isSuccess()) {
          spUtility.setAccessToken(response.body());
          getUserInfo().done(new DoneCallback<UserModel>() {
            @Override
            public void onDone(UserModel user) {
              deferred.resolve(user);
            }
          });
        } else {
          Timber.e("Response fail");
          deferred.reject(null);
        }
      }

      @Override
      public void onFailure(Throwable t) {
        t.printStackTrace(System.err);
        deferred.reject(new Exception(t));
      }
    });
    return deferred.promise();
  }

  private Promise<UserModel, Exception, Object> getUserInfo() {
    final DeferredObject<UserModel, Exception, Object> deferred = new DeferredObject<>();
    ApiService service = apiConnector.createService(ApiService.class, this);
    Call<UserModel> request = service.getUserInfo();
    request.enqueue(new Callback<UserModel>() {
      @Override
      public void onResponse(Response<UserModel> response, Retrofit retrofit) {
        if (!response.isSuccess()) {
          Timber.e("Failure in response");
          deferred.reject(null);
          return;
        }
        UserModel user = response.body();
        spUtility.setCurrentUser(user);
        deferred.resolve(user);
      }

      @Override
      public void onFailure(Throwable t) {
        t.printStackTrace(System.err);
        deferred.reject(new Exception(t));
      }
    });
    return deferred.promise();
  }

  public int refreshAccessToken(AccessToken accessToken) {
    ApiService service = apiConnector.createLoginService(ApiService.class);
    Call<AccessToken> request = service.refreshToken("refresh_token", accessToken.refresh_token);
    try {
      Response<AccessToken> response = request.execute();
      if (response.isSuccess()) {
        spUtility.setAccessToken(response.body());
      } else { // TODO check if no internet
        App.logout();
      }
      return response.code();
    } catch (Exception e) {
      Timber.e(e);
    }
    return 0;
  }

  public DeferredObject<Boolean, Exception, Object> generateOtp(String number) {
    final DeferredObject<Boolean, Exception, Object> deferred = new DeferredObject<>();
    ApiService service = apiConnector.createLoginService(ApiService.class);
    RequestBodyModel body = new RequestBodyModel();
    body.mobile = number;
    Call<ResponseBody> request = service.generateOtp(body);
    request.enqueue(new Callback<ResponseBody>() {
      @Override
      public void onResponse(Response<ResponseBody> response, Retrofit retrofit) {
        deferred.resolve(true);
      }

      @Override
      public void onFailure(Throwable t) {
        t.printStackTrace(System.err);
        deferred.reject(new Exception(t));
      }
    });
    return deferred;
  }

  public DeferredObject<Boolean, Exception, Object>
  verifyOTP(String number, String otp) {
    final DeferredObject<Boolean, Exception, Object> deferred = new DeferredObject<>();
    ApiService service = apiConnector.createLoginService(ApiService.class);
    RequestBodyModel body = new RequestBodyModel();
    body.mobile = number;
    body.otp = otp;
    Call<ResponseBody> request = service.verifyOtp(body);
    request.enqueue(new Callback<ResponseBody>() {
      @Override
      public void onResponse(Response<ResponseBody> response, Retrofit retrofit) {
        deferred.resolve(true);
      }

      @Override
      public void onFailure(Throwable t) {
        t.printStackTrace(System.err);
        deferred.reject(new Exception(t));
      }
    });
    return deferred;
  }

  public DeferredObject<Boolean, Exception, Object> sendContacts(List<ContactModel> contacts) {
    final DeferredObject<Boolean, Exception, Object> deferred = new DeferredObject<>();
    ApiService service = apiConnector.createService(ApiService.class, this);
    RequestBodyModel body = new RequestBodyModel();
    body.contacts = contacts;
    Call<ResponseBody> request = service.syncContact(body);
    request.enqueue(new Callback<ResponseBody>() {
      @Override
      public void onResponse(Response<ResponseBody> response, Retrofit retrofit) {
        deferred.resolve(true);
      }

      @Override
      public void onFailure(Throwable t) {
        t.printStackTrace(System.err);
        deferred.reject(new Exception(t));
      }
    });
    return deferred;
  }
}