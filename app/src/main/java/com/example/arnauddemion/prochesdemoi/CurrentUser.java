package com.example.arnauddemion.prochesdemoi;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

class CurrentUser extends Personne {
    private static final CurrentUser ourInstance = new CurrentUser();
    private static final Integer fuzzyDistance = 300;
    private final String TAG = getClass().getSimpleName();
    private RESTService APIService = RetrofitClient.getInstance().getAPI();
    private List<Personne> friends;

    static CurrentUser getInstance() {
        return ourInstance;
    }

    private CurrentUser() {
        friends = new ArrayList<>();
    }

    public List<Personne> getFriends() {
        return friends;
    }

    public void authenticate() {

    }

    public void fetchFriends() {
        Call<List<Friend>> call = APIService.getPersonFriends(getId());
        call.enqueue(new Callback<List<Friend>>() {
            @Override
            public void onResponse(Call<List<Friend>> call, Response<List<Friend>> response) {
                List<Friend> friendList = response.body();
                if (friendList != null) {
                    for (Friend friend : friendList) {
                        Personne personiter = friend.getFriend();
                        Call<MyLocation> subcall = APIService.getPersonLocation(personiter.getId(), fuzzyDistance);
                        subcall.enqueue(new Callback<MyLocation>() {
                            @Override
                            public void onResponse(Call<MyLocation> call, Response<MyLocation> response) {
                                MyLocation currentPersonLocation = response.body();
                                if (currentPersonLocation != null) {
                                    personiter.setLocation(currentPersonLocation);
                                } else {
                                    Log.d(TAG, "Person " + personiter.getId() + " has no location(s) yet");
                                }
                                friends.add(personiter);
                            }

                            @Override
                            public void onFailure(Call<MyLocation> call, Throwable throwable) {
                                Log.e(TAG, "Friend " + personiter.getId() + " localisation REST resource call failure");
                                Log.e(TAG, throwable.toString());
                            }
                        });
                    }
                } else {
                    Log.d(TAG, "Person " + getId() + " has no friends(s) yet");
                }
            }

            @Override
            public void onFailure(Call<List<Friend>> call, Throwable throwable) {
                Log.e(TAG, "Friends for person " + getId() + " REST resource call failure");
                Log.e(TAG, throwable.toString());
            }
        });

    }


}
