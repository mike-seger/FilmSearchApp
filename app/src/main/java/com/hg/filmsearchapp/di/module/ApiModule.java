package com.hg.filmsearchapp.di.module;

import com.hg.filmsearchapp.di.scope.CustomScope;
import com.hg.filmsearchapp.webservice.FilmService;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * Created by Hurman on 17/09/2016.
 */
@Module
public class ApiModule {

    @Provides
    @CustomScope
    FilmService provideFilmService(Retrofit retrofit) {
        return retrofit.create(FilmService.class);
    }

}
