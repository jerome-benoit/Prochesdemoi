package com.example.arnauddemion.prochesdemoi;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

class CurrentUser extends Personne {
    private static final CurrentUser ourInstance = new CurrentUser();
    private static final Integer fuzzyDistance = 300;
    private static final Integer nearDistance = 2;
    // Boolean private variable for tricky method that need boolean return value.
    private static boolean rtVal;
    private final String TAG = getClass().getSimpleName();
    private RESTService APIService = RetrofitClient.getInstance().getAPI();
    private List<Personne> friends;
    private List<Personne> persons;
    private List<Personne> searchList;

    private CurrentUser() {
        friends = new ArrayList<>();
        persons = new ArrayList<>();
    }

    static CurrentUser getInstance() {
        return ourInstance;
    }

    public static Integer getFuzzyDistance() {
        return fuzzyDistance;
    }

    public static Integer getNearDistance() {
        return nearDistance;
    }

    public List<Personne> getPersons() {
        //FIXME: this loop that should filter the current logged user delete everybody.
        /*for (Personne personne : persons) {
            if (getId() == personne.getId()) {
                persons.remove(personne);
            }
        }*/
        return persons;
    }

    public List<Personne> getFriends() {
        return friends;
    }

    public List<Personne> getNearFriends() {
        //TODO: Do it
        //      Add time criterion?
        return friends;
    }

    public double distanceCalculation(LatLng StartP, LatLng EndP) {
        final int Radius = 6371;// radius of earth in Km
        double lat1 = StartP.latitude;
        double lat2 = EndP.latitude;
        double lon1 = StartP.longitude;
        double lon2 = EndP.longitude;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
                * Math.sin(dLon / 2);
        double c = 2 * Math.asin(Math.sqrt(a));

        return Radius * c;
    }

    public void hashPassword(String password) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        //FIXME: Make use of a salt
        md.reset();
        byte[] hashedPasswordBytes = md.digest(password.getBytes());
        //Convert it to hexadecimal format
        StringBuilder sb = new StringBuilder();
        for (byte hashedPasswordByte : hashedPasswordBytes) {
            sb.append(Integer.toString((hashedPasswordByte & 0xff) + 0x100, 16).substring(1));
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

    private Date timestampToDate(long timestamp) {
        return new Date(timestamp);
    }

    public void updateLocation(double latitude, double longitude, Date timestamp) {
        setLocation(new MyLocation(latitude, longitude, timestamp));
        Call<ResponseBody> call = APIService.updatePersonLocation(getId(), getLocation());
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
                    friends.clear();
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

    public void fetchPersons() {
        Call<List<Personne>> call = APIService.getPersons();
        call.enqueue(new Callback<List<Personne>>() {
            @Override
            public void onResponse(Call<List<Personne>> call, Response<List<Personne>> response) {
                persons = response.body();
            }

            @Override
            public void onFailure(Call<List<Personne>> call, Throwable throwable) {
                Log.e(TAG, "All persons REST resource call failure");
                Log.e(TAG, throwable.toString());
            }
        });
    }

    public List<Personne> searchPersons(String keyword) {
        Call<List<Personne>> call = APIService.searchPerson(keyword);
        call.enqueue(new Callback<List<Personne>>() {
            @Override
            public void onResponse(Call<List<Personne>> call, Response<List<Personne>> response) {
                searchList = response.body();
            }

            @Override
            public void onFailure(Call<List<Personne>> call, Throwable throwable) {
                Log.e(TAG, "Search person REST resource call failure");
                Log.e(TAG, throwable.toString());
            }
        });
        return searchList;
    }
}
