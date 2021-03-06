package com.msgque.play.connectivity;

import com.google.gson.reflect.TypeToken;
import com.msgque.play.App;
import com.msgque.play.common.JsonConverter;
import com.msgque.play.common.SPHelper;
import com.msgque.play.common.constant.EndPoints;
import com.msgque.play.model.AccessToken;
import com.msgque.play.model.CampaignModel;
import com.msgque.play.model.ContactModel;
import com.msgque.play.model.DomainModel;
import com.msgque.play.model.GroupModel;
import com.msgque.play.model.MetaModel;
import com.msgque.play.model.RequestBodyModel;
import com.msgque.play.model.RouteModel;
import com.msgque.play.model.SenderIdModel;
import com.msgque.play.model.SmsModel;
import com.msgque.play.model.UserModel;

import org.jdeferred.Deferred;
import org.jdeferred.DoneCallback;
import org.jdeferred.FailCallback;
import org.jdeferred.Promise;
import org.jdeferred.impl.DeferredObject;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;


public class ServerConnection {
  private final RetroFitConnector apiConnector;
  private final RetroFitConnector dlrMsgqueConnector;
  private final SPHelper spHelper;
  private final JsonConverter jsonConverter;

  public ServerConnection(List<RetroFitConnector> retroFitConnector, JsonConverter jsonConverter,
                          SPHelper SPHelper) {
    apiConnector = retroFitConnector.get(0);
    dlrMsgqueConnector = retroFitConnector.get(1);
    this.spHelper = SPHelper;
    this.jsonConverter = jsonConverter;
  }

  public Promise<Boolean, Exception, Object> signUpUserGoogle(final UserModel user) {
    final Deferred<Boolean, Exception, Object> deferred = new DeferredObject<>();
    user.roleId = 4;
    createEndUser(user)
        .then(new DoneCallback<Boolean>() {
          @Override
          public void onDone(Boolean result) {
            googleLogin(user)
                .then(new DoneCallback<UserModel>() {
                  @Override
                  public void onDone(UserModel result) {
                    deferred.resolve(true);
                  }
                })
                .fail(new FailCallback<Exception>() {
                  @Override
                  public void onFail(Exception result) {
                    deferred.reject(result);
                  }
                });
          }
        })
        .fail(new FailCallback<Exception>() {
          @Override
          public void onFail(Exception result) {
            deferred.reject(result);
          }
        });
    return deferred.promise();
  }

  public Promise<Boolean, Exception, Object> signUpUser(UserModel user) {
    final Deferred<Boolean, Exception, Object> deferred = new DeferredObject<>();
    ApiService service = apiConnector.createLoginService(ApiService.class);
    Call<ResponseBody> request = service.signUpUser(user);
    request.enqueue(new Callback<ResponseBody>() {
      @Override
      public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        if (response.isSuccessful() || response.code() == 409) {
          deferred.resolve(true);
          return;
        }
        try {
          Timber.d(response.code() + ":" + response.message());
          Timber.d(response.errorBody().string());
        } catch (IOException e) {
          e.printStackTrace();
        }
        deferred.reject(new Exception());
      }

      @Override
      public void onFailure(Call<ResponseBody> call, Throwable t) {
        Timber.w(t);
        deferred.reject(null);
      }
    });
    return deferred.promise();
  }

  public Promise<Boolean, Exception, Object> createEndUser(UserModel user) {
    final Deferred<Boolean, Exception, Object> deferred = new DeferredObject<>();
    ApiService service = apiConnector.createService(ApiService.class, this);
    Call<UserModel> request = service.createEndCustomer(user);
    request.enqueue(new Callback<UserModel>() {
      @Override
      public void onResponse(Call<UserModel> call, Response<UserModel> response) {
        if (response.isSuccessful() || response.code() == 409) {
          deferred.resolve(true);
          return;
        }
        try {
          Timber.d(response.code() + ":" + response.message());
          Timber.d(response.errorBody().string());
        } catch (IOException e) {
          e.printStackTrace();
        }
        deferred.reject(new Exception());
      }

      @Override
      public void onFailure(Call<UserModel> call, Throwable t) {
        Timber.w(t);
        deferred.reject(null);
      }
    });
    return deferred.promise();
  }

  public Promise<UserModel, Exception, Object> googleLogin(UserModel user) {
    final DeferredObject<UserModel, Exception, Object> deferred = new DeferredObject<>();
    ApiService service = apiConnector.createLoginService(ApiService.class);
    Timber.e(jsonConverter.toJson(user));
    Call<AccessToken> request = service.googleLogin(user);
    request.enqueue(new Callback<AccessToken>() {
      @Override
      public void onResponse(Call<AccessToken> call, Response<AccessToken> response) {
        if (response.isSuccessful()) {
          spHelper.setAccessToken(response.body());
          getUserInfo()
              .done(new DoneCallback<UserModel>() {
                @Override
                public void onDone(UserModel user) {
                  deferred.resolve(user);
                }
              })
              .fail(new FailCallback<Exception>() {
                @Override
                public void onFail(Exception result) {
                  deferred.reject(result);
                }
              });
          return;
        }
        try {
          Timber.d(response.code() + ":" + response.message());
          Timber.d(response.errorBody().string());
        } catch (IOException e) {
          e.printStackTrace();
        }
        deferred.reject(new Exception());
      }

      @Override
      public void onFailure(Call<AccessToken> call, Throwable t) {
        Timber.d(t);
        deferred.reject(new Exception(t));
      }
    });
    return deferred.promise();
  }

  public Promise<UserModel, Exception, Object> loginUser(final String username, final String password) {
    final DeferredObject<UserModel, Exception, Object> deferred = new DeferredObject<>();
    ApiService service = apiConnector.createLoginService(ApiService.class);
    Call<AccessToken> request = service.loginUser(username, password, "password",
        EndPoints.CLIENT_APP_ID, EndPoints.CLIENT_SECRET);
    request.enqueue(new Callback<AccessToken>() {
      @Override
      public void onResponse(Call<AccessToken> call, Response<AccessToken> response) {
        if (response.isSuccessful()) {
          spHelper.setAccessToken(response.body());
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
      public void onFailure(Call<AccessToken> call, Throwable t) {
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
      public void onResponse(Call<UserModel> call, Response<UserModel> response) {
        if (!response.isSuccessful()) {
          Timber.e("Failure in response");
          deferred.reject(null);
          return;
        }
        UserModel user = response.body();
        spHelper.setCurrentUser(user);
        deferred.resolve(user);
      }

      @Override
      public void onFailure(Call<UserModel> call, Throwable t) {
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
      if (response.isSuccessful()) {
        spHelper.setAccessToken(response.body());
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
      public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        deferred.resolve(true);
      }

      @Override
      public void onFailure(Call<ResponseBody> call, Throwable t) {
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
      public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        deferred.resolve(true);
      }

      @Override
      public void onFailure(Call<ResponseBody> call, Throwable t) {
        t.printStackTrace(System.err);
        deferred.reject(new Exception(t));
      }
    });
    return deferred;
  }

  public DeferredObject<Boolean, Exception, Object> sendContacts(String groupName,
                                                                 List<ContactModel> contacts) {
    final DeferredObject<Boolean, Exception, Object> deferred = new DeferredObject<>();
    ApiService service = apiConnector.createService(ApiService.class, this);
    RequestBodyModel body = new RequestBodyModel();
    body.contacts = contacts;
    body.name = groupName;
    Call<ResponseBody> request = service.syncContact(body);
    request.enqueue(new Callback<ResponseBody>() {
      @Override
      public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        deferred.resolve(true);
      }

      @Override
      public void onFailure(Call<ResponseBody> call, Throwable t) {
        t.printStackTrace(System.err);
        deferred.reject(new Exception(t));
      }
    });
    return deferred;
  }

  public DeferredObject<List<GroupModel>, Exception, Object> fetchGroups() {
    final DeferredObject<List<GroupModel>, Exception, Object> deferred = new DeferredObject<>();
    ApiService service = apiConnector.createService(ApiService.class, this);
    Call<List<GroupModel>> request = service.fetchGroups();
    request.enqueue(new Callback<List<GroupModel>>() {
      @Override
      public void onResponse(Call<List<GroupModel>> call, Response<List<GroupModel>> response) {
        if (response.isSuccessful()) {
          deferred.resolve(response.body());
          return;
        }
        deferred.reject(new Exception());
      }

      @Override
      public void onFailure(Call<List<GroupModel>> call, Throwable t) {
        t.printStackTrace(System.err);
        deferred.reject(new Exception(t));
      }
    });
    return deferred;
  }

  public Promise<List<SenderIdModel>, Exception, Object> fetchSenderIds(String fl, String status) {
    final DeferredObject<List<SenderIdModel>, Exception, Object> deferred = new DeferredObject<>();
    ApiService service = apiConnector.createService(ApiService.class, this);
    Call<List<SenderIdModel>> request = service.fetchSenderIds(fl, status);
    request.enqueue(new Callback<List<SenderIdModel>>() {
      @Override
      public void onResponse(Call<List<SenderIdModel>> call, Response<List<SenderIdModel>> response) {
        if (response.isSuccessful()) {
          deferred.resolve(response.body());
          return;
        }
        deferred.reject(new Exception());
      }

      @Override
      public void onFailure(Call<List<SenderIdModel>> call, Throwable t) {
        t.printStackTrace(System.err);
        deferred.reject(new Exception(t));
      }
    });
    return deferred.promise();
  }

  public Promise<List<RouteModel>, Exception, Object> fetchRoutes() {
    final DeferredObject<List<RouteModel>, Exception, Object> deferred = new DeferredObject<>();
    ApiService service = apiConnector.createService(ApiService.class, this);
    Call<List<RouteModel>> request = service.fetchRoutes();
    request.enqueue(new Callback<List<RouteModel>>() {
      @Override
      public void onResponse(Call<List<RouteModel>> call, Response<List<RouteModel>> response) {
        if (response.isSuccessful()) {
          deferred.resolve(response.body());
          return;
        }
        Timber.e("Error");
        deferred.reject(new Exception());
      }

      @Override
      public void onFailure(Call<List<RouteModel>> call, Throwable t) {
        t.printStackTrace(System.err);
        deferred.reject(new Exception(t));
      }
    });
    return deferred.promise();
  }

  public Promise<Boolean, Exception, Object> sendSms(SmsModel body) {
    final DeferredObject<Boolean, Exception, Object> deferred = new DeferredObject<>();
    ApiService service = apiConnector.createService(ApiService.class, this);
    Call<ResponseBody> request = service.sendSms(body);
    request.enqueue(new Callback<ResponseBody>() {
      @Override
      public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        if (response.isSuccessful()) {
          deferred.resolve(true);
          return;
        }
        deferred.reject(new Exception());
      }

      @Override
      public void onFailure(Call<ResponseBody> call, Throwable t) {
        t.printStackTrace(System.err);
        deferred.reject(new Exception(t));
      }
    });
    return deferred.promise();
  }

  public Promise<Boolean, Exception, Object> updateUserInfo(UserModel user) {
    final Deferred<Boolean, Exception, Object> deferred = new DeferredObject<>();
    ApiService service = apiConnector.createService(ApiService.class, this);
    Call<ResponseBody> request = service.setUserInfo(user);
    request.enqueue(new Callback<ResponseBody>() {
      @Override
      public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        if (response.isSuccessful()) {
          deferred.resolve(true);
          return;
        }
        deferred.reject(new Exception());
      }

      @Override
      public void onFailure(Call<ResponseBody> call, Throwable t) {
        deferred.reject(new Exception(t));
      }
    });
    return deferred.promise();
  }

  public Promise<List<DomainModel>, Exception, Object> getDomainAvailability(String domain) {
    final Deferred<List<DomainModel>, Exception, Object> deferred = new DeferredObject<>();
    DlrMesqueService service = dlrMsgqueConnector.createServiceNoAuthentication(DlrMesqueService.class);
    Call<HashMap<String, DomainModel>> request = service.getDomainAvailability(domain);
    request.enqueue(new Callback<HashMap<String, DomainModel>>() {
      @Override
      public void onResponse(Call<HashMap<String, DomainModel>> call,
                             Response<HashMap<String, DomainModel>> response) {
        if (response.isSuccessful()) {
          List<DomainModel> result = new ArrayList<>();
          HashMap<String, DomainModel> data = response.body();
          for (String name : data.keySet()) {
            DomainModel domain = data.get(name);
            domain.setName(name);
            result.add(domain);
          }
          deferred.resolve(result);
          return;
        }
        deferred.reject(new Exception());
      }

      @Override
      public void onFailure(Call<HashMap<String, DomainModel>> call, Throwable t) {
        deferred.reject(new Exception(t));
      }
    });
    return deferred.promise();
  }

  public Promise<List<DomainModel>, Exception, Object> getDomain() {
    final Deferred<List<DomainModel>, Exception, Object> deferred = new DeferredObject<>();
    ApiService service = apiConnector.createService(ApiService.class, this);
    Call<List<DomainModel>> request = service.getDomains();
    request.enqueue(new Callback<List<DomainModel>>() {
      @Override
      public void onResponse(Call<List<DomainModel>> call,
                             Response<List<DomainModel>> response) {
        if (response.isSuccessful()) {
          deferred.resolve(response.body());
          return;
        }
        deferred.reject(new Exception());
      }

      @Override
      public void onFailure(Call<List<DomainModel>> call, Throwable t) {
        deferred.reject(new Exception(t));
      }
    });
    return deferred.promise();
  }

  public Promise<List<DomainModel>, Exception, Object> createDomains(final List<DomainModel> domains) {
    final Deferred<List<DomainModel>, Exception, Object> deferred = new DeferredObject<>();
    ApiService service = apiConnector.createService(ApiService.class, this);
    Call<ResponseBody> request;
    Timber.e(jsonConverter.toJson(domains.get(0)));
    if (domains.size() == 1) request = service.createDomains(domains.get(0));
    else request = service.createDomains(domains);
    request.enqueue(new Callback<ResponseBody>() {
      @Override
      public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        if (response.isSuccessful()) {
          List<DomainModel> result = new ArrayList<>();
          try {
            if (domains.size() == 1) {
              result.add(jsonConverter.fromJson(response.body().string(), DomainModel.class));
            } else {
              result = jsonConverter.fromJson(response.body().string(),
                  new TypeToken<List<DomainModel>>(){}.getType());
            }
          } catch (IOException e) {
            e.printStackTrace();
          }
          deferred.resolve(result);
          return;
        }
        deferred.reject(new Exception());
      }

      @Override
      public void onFailure(Call<ResponseBody> call, Throwable t) {
        deferred.reject(new Exception(t));
      }
    });
    return deferred.promise();
  }

  public DeferredObject<List<CampaignModel>, Exception, Object> fetchCampaigns() {
    final DeferredObject<List<CampaignModel>, Exception, Object> deferred = new DeferredObject<>();
    ApiService service = apiConnector.createService(ApiService.class, this);
    Call<ResponseBody> request = service.fetchCampaigns();
    request.enqueue(new Callback<ResponseBody>() {
      @Override
      public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        if (response.isSuccessful()) {
          List<CampaignModel> result = new ArrayList<>();
          try {
            JSONObject object = new JSONObject(response.body().string());
            result = jsonConverter.fromJson(object.getString("items"),
                new TypeToken<List<CampaignModel>>() {
                }.getType());
          } catch (Exception e) {
            e.printStackTrace();
          }
          deferred.resolve(result);
          return;
        }
        deferred.reject(new Exception());
      }

      @Override
      public void onFailure(Call<ResponseBody> call, Throwable t) {
        t.printStackTrace(System.err);
        deferred.reject(new Exception(t));
      }
    });
    return deferred;
  }

  public Promise<CampaignModel, Exception, Object> fetchCampaign(String campaign) {
    final Deferred<CampaignModel, Exception, Object> deferred = new DeferredObject<>();
    ApiService service = apiConnector.createService(ApiService.class, this);
    Call<CampaignModel> request = service.fetchCampaign(campaign);
    request.enqueue(new Callback<CampaignModel>() {
      @Override
      public void onResponse(Call<CampaignModel> call, Response<CampaignModel> response) {
        if (response.isSuccessful()) {
          deferred.resolve(response.body());
          return;
        }
        deferred.reject(new Exception());
      }

      @Override
      public void onFailure(Call<CampaignModel> call, Throwable t) {
        deferred.reject(new Exception(t));
      }
    });
    return deferred.promise();
  }

  public Promise<MetaModel, Exception, Object> getMetaDetail(String email) {
    final Deferred<MetaModel, Exception, Object> deferred = new DeferredObject<>();
    DlrMesqueService service = dlrMsgqueConnector.createService(DlrMesqueService.class, this);
    Call<MetaModel> request = service.getMetaDetail("meta", email);
    request.enqueue(new Callback<MetaModel>() {
      @Override
      public void onResponse(Call<MetaModel> call, Response<MetaModel> response) {
        if (response.isSuccessful()) {
          deferred.resolve(response.body());
          return;
        }
        deferred.reject(new Exception());
      }

      @Override
      public void onFailure(Call<MetaModel> call, Throwable t) {
        t.printStackTrace(System.err);
        deferred.reject(new Exception(t));
      }
    });
    return deferred.promise();
  }
}
