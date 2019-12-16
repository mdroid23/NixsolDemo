package com.nixsol.mahesh.common.api;

import com.nixsol.mahesh.common.constants.UrlConstant;
import com.nixsol.mahesh.model.response.FactResponse;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;

public class ApiClient implements ApiInterface{

    public static final String BASE_URL = UrlConstant.API_BASE;
    private ApiInterface api;

    public ApiClient() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        this.api = retrofit.create(ApiInterface.class);
    }


    @Override
    public Observable<FactResponse> getFacts() {
        return this.api.getFacts();
    }
}
