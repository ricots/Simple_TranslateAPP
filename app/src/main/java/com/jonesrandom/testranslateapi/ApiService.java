package com.jonesrandom.testranslateapi;

import com.jonesrandom.testranslateapi.Translate.ResponseTranslate;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiService {

    @FormUrlEncoded
    @POST("api/v1.5/tr.json/getLangs")
    Call<ResponseBody> getLangs(@Field("key") String Key, @Field("ui") String Ui);

    @FormUrlEncoded
    @POST("api/v1.5/tr.json/translate")
    Call<ResponseTranslate> getTranslate(@Field("key") String Key, @Field("text") String Text, @Field("lang") String Lang);
}
