package com.hg.filmsearchapp.di.component;

import com.hg.filmsearchapp.di.module.ApiModule;
import com.hg.filmsearchapp.di.scope.CustomScope;
import com.hg.filmsearchapp.ui.ActivityFilmDetail;
import com.hg.filmsearchapp.ui.ActivityFilmList;

import dagger.Component;

/**
 * Created by Hurman on 17/09/2016.
 */
@CustomScope
@Component(modules = ApiModule.class, dependencies = NetworkComponent.class)
public interface ApiComponent {

    void inject(ActivityFilmList activity);
    void inject(ActivityFilmDetail activityFilmDetail);
}
