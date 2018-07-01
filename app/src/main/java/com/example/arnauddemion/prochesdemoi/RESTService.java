package com.example.arnauddemion.prochesdemoi;

import java.util.List;

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
    Call<Personne> updatePerson(@Path("id") Integer id);

    @POST("api/person/authenticate")
    Call<Personne> authenticatePerson(@Body Personne person);

    @POST("api/person/{id}/localisation")
    Call updatePersonLocation(@Body MyLocation location);

    @PUT("api/person/{id}/online")
    Call setPersonOnline(@Path("id") Integer id);

    @PUT("api/person/{id}/offline")
    Call setPersonOffline(@Path("id") Integer id);

    @GET("api/person/{id}")
    Call<Personne> getPerson(@Path("id") Integer id);

    @GET("api/person/{id}/friends")
    Call<Friends> getPersonFriends(@Path("id") Integer id);

    @GET("api/person/{id}/friendswithme")
    Call<FriendsWithMe> getPersonFriendsWithMe(@Path("id") Integer id);

    @GET("api/person/{id}/localisation/fuzzy/{distance}")
    Call<MyLocation> getPersonLocation(@Path("id") Integer id, @Path("distance") Integer distance);

}
