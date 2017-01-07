package com.hg.filmsearchapp.webservice;

import com.hg.filmsearchapp.model.FilmList;
import com.hg.filmsearchapp.model.FilmUnique;
import com.hg.filmsearchapp.utils.Utils;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Hurman on 17/09/2016.
 */
public interface FilmService {

    @GET(Utils.BASE_URL)
    Observable<FilmUnique> getFilmDetails(@Query("i") String filmId, @Query("r") String responseType);

    @GET(Utils.BASE_URL)
    Observable<FilmList> getFilmList(@Query("s") String movieTitle, @Query("r") String responseType, @Query("type") String type);

}
