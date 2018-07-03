package com.example.arnauddemion.prochesdemoi;

import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.sql.Date;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

class CurrentUser extends Personne {
    private static final CurrentUser ourInstance = new CurrentUser();
    private static final Integer fuzzyDistance = 300;
    private final String TAG = getClass().getSimpleName();
    private RESTService APIService = RetrofitClient.getInstance().getAPI();
    private List<Personne> friends;
    private List<Personne> persons;

    // Boolean variable for tricky method that need boolean return value.
    private static boolean rtVal;

    static CurrentUser getInstance() {
        return ourInstance;
    }

    private CurrentUser() {
        friends = new ArrayList<>();
        persons = new ArrayList<>();
    }

    public List<Personne> getFriends() {
        return friends;
    }

    public List<Personne> getPersons() {
        for (Personne personne : persons) {
            if (getId() == personne.getId()) {
            persons.remove(personne);
            }
        }
        return persons;
    }

    public void hashPassword(String password) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-256");
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        //FIXME: Make use of a salt
        md.reset();
        byte[] hashedPasswordBytes = md.digest(password.getBytes());
        //Convert it to hexadecimal format
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < hashedPasswordBytes.length; i++)
        {
            sb.append(Integer.toString((hashedPasswordBytes[i] & 0xff) + 0x100, 16).substring(1));
        }
        String hashedPassword = sb.toString();
        Log.d(TAG, "Password: " + password + ", hashed password: " + hashedPassword);
        setPassword(hashedPassword);
    }

    public boolean authenticate() {
        Call<Personne> call = APIService.authenticatePerson(ourInstance);
        call.enqueue(new Callback<Personne>() {
            @Override
            public void onResponse(Call<Personne> call, Response<Personne> response) {
                if (response.code() == 401) {
                    Log.d(TAG, "Authentication failure");
                    rtVal = false;
                } else {
                    Personne user = response.body();
                    setId(user.getId());
                    setFirstname(user.getFirstname());
                    setLastname(user.getLastname());
                    putOnline();
                    rtVal = true;
                }
            }

            @Override
            public void onFailure(Call<Personne> call, Throwable throwable) {
                Log.e(TAG, "Authentication REST resource call failure");
                Log.e(TAG, throwable.toString());
                rtVal = false;
            }
        });
        return rtVal;
    }

    private Date timestampToDate(long timestamp){
        try {
            return new Date(timestamp);
        }
        catch (Exception ex) {
            Log.d(TAG, "");
            return null;
        }
    }
    
    public void updateLocation(double latitude, double longitude, long timestamp) {
        MyLocation location = new MyLocation(latitude, longitude, timestampToDate(timestamp));
        Call<ResponseBody> call = APIService.updatePersonLocation(getId(), location);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                Log.e(TAG, "Set person " + getId() + " location REST resource call failure");
                Log.e(TAG, throwable.toString());
            }
        });

    }

    public void putOnline() {
        Call<ResponseBody> call = APIService.setPersonOnline(getId());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                Log.e(TAG, "Set person " + getId() + " online REST resource call failure");
                Log.e(TAG, throwable.toString());
            }
        });
    }

    public void putOffline() {
        Call<ResponseBody> call = APIService.setPersonOffline(getId());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                Log.e(TAG, "Set person " + getId() + " offline REST resource call failure");
                Log.e(TAG, throwable.toString());
            }
        });
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
                                if (!friends.contains(personiter)) {
                                    friends.add(personiter);
                                }
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

    public void fetchPersons() {
        Call<List<Personne>> call = APIService.getPersons();
        call.enqueue(new Callback<List<Personne>>() {
            @Override
            public void onResponse(Call<List<Personne>> call, Response<List<Personne>> response) {
                persons = response.body();
            }

            @Override
            public void onFailure(Call<List<Personne>> call, Throwable throwable) {
                Log.e(TAG, "All persons for person " + getId() + " REST resource call failure");
                Log.e(TAG, throwable.toString());
            }
        });
    }

}