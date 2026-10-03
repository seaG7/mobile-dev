package ru.mirea.danilov.anishot.data.network;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface KitsuService {
    @Headers("Accept: application/vnd.api+json")
    @GET("anime")
    Call<KitsuPage> popular(@Query("page[limit]") int limit, @Query("sort") String sort);
}
