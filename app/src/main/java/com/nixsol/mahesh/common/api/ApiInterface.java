package com.nixsol.mahesh.common.api;

import com.nixsol.mahesh.common.constants.UrlConstant;
import com.nixsol.mahesh.model.response.FactResponse;

import retrofit2.http.GET;
import rx.Observable;

public interface ApiInterface {

    @GET(UrlConstant.API_GET_FACTS)
    Observable<FactResponse> getFacts();

}
