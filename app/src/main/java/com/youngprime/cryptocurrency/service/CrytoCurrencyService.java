package com.youngprime.cryptocurrency.service;

import com.youngprime.cryptocurrency.model.CountryCurrency;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

/**
 * Created by bd_dev_murtala on 19/10/2017.
 */

public interface CrytoCurrencyService {

    @Headers("Content-Type: application/json")
    @GET("data/price")
    Call<Object> getConversion(@Query("fsym") String fsym, @Query("tsyms") String tsyms);

}
