package com.msgque.pulse.connectivity;

import com.msgque.pulse.model.DomainModel;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


interface DlrMesqueService {

  @GET("/domains/index.php")
  Call<HashMap<String, DomainModel>> getDomainAvailability(@Query("domain_name") String domain);
}
