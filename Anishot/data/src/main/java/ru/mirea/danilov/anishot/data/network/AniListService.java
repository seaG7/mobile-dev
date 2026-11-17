package ru.mirea.danilov.anishot.data.network;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface AniListService {
    @Headers({
            "Content-Type: application/json",
            "Accept: application/json"
    })
    @POST(".")
    Call<AniListResponse> mediaById(@Body Request body);

    class Request {
        final String query;
        final Variables variables;

        public Request(int id) {
            this.query = "query($id:Int){Media(id:$id,type:ANIME){id title{romaji english native} averageScore seasonYear episodes format coverImage{large medium} description(asHtml:false)}}";
            this.variables = new Variables(id);
        }
    }

    class Variables {
        final int id;

        Variables(int id) {
            this.id = id;
        }
    }
}
