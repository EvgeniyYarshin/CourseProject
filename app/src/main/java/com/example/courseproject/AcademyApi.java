package com.example.courseproject;

import com.example.courseproject.model.Album;
import com.example.courseproject.model.Comment;
import com.example.courseproject.model.User;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface AcademyApi {

    @POST("registration")
    Completable registration(@Body User user);

    @GET("user")
    Completable authorization();

    @GET("albums")
    Single<List<Album>> getAlbums();

    @GET("albums/{id}")
    Single<Album> getAlbum(@Path("id") int id);

    @GET("albums/{id}/comments")
    Single<List<Comment>> getComments(@Path("id") int id);

    @POST("comments")
    Completable setComment(@Body Comment comment);
}
