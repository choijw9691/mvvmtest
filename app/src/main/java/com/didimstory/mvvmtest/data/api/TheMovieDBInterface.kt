package com.didimstory.mvvmtest.data.api

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path

interface TheMovieDBInterface {
    // https://api.themoviedb.org/3/movie/299534?api_key=e51553e80a6693c33e9685e87f74b45a
    //https://api.themoviedb.org/3/movie/popular?api_key=e51553e80a6693c33e9685e87f74b45a&page=1
    // https://api.themoviedb.org/3/


@GET("movie/{movie_id}")
fun getMovieDetails(@Path("movie_id")id:Int) : Single<MovieDetails>



}