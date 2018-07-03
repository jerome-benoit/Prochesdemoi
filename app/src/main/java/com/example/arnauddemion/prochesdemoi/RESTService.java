package com.example.arnauddemion.prochesdemoi;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface RESTService {

    @POST("api/person/register")
    Call<Personne> createPerson(@Body Personne person);

    @DELETE("api/person/{id}")
    Call deletPerson(@Path("id") Integer id);

    @PUT("api/person/{id}")
    Call<Personne> updatePerson(@Path("id") Integer id, @Body Personne person);

    @POST("api/person/authenticate")
    Call<Personne> authenticatePerson(@Body Personne person);

    @POST("api/person/{id}/localisation")
    Call<ResponseBody> updatePersonLocation(@Path("id") Integer id, @Body MyLocation location);

    @PUT("api/person/{id}/online")
    Call<ResponseBody> setPersonOnline(@Path("id") Integer id);

    @PUT("api/person/{id}/offline")
    Call<ResponseBody> setPersonOffline(@Path("id") Integer id);

    @GET("api/person/{id}")
    Call<Personne> getPerson(@Path("id") Integer id);

    @GET("api/person/{id}/friends")
    Call<List<Friend>> getPersonFriends(@Path("id") Integer id);

    @GET("api/person/{id}/friendswithme")
    Call<List<FriendWithMe>> getPersonFriendsWithMe(@Path("id") Integer id);

    @GET("api/person/{id}/localisation/fuzzy/{distance}")
    Call<MyLocation> getPersonLocation(@Path("id") Integer id, @Path("distance") Integer distance);

}
