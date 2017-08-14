package com.msgque.play.connectivity;

import com.msgque.play.model.DomainModel;
import com.msgque.play.model.MetaModel;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


interface DlrMesqueService {

  @GET("/domains/index.php")
  Call<HashMap<String, DomainModel>> getDomainAvailability(@Query("domain_name") String domain);


  @GET("/shoppre/")
  Call<MetaModel> getMetaDetail(@Query("file") String file, @Query("email") String email);
}
