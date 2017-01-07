package com.hg.filmsearchapp.ui;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.hg.filmsearchapp.MyApplication;
import com.hg.filmsearchapp.R;
import com.hg.filmsearchapp.model.FilmUnique;
import com.hg.filmsearchapp.webservice.FilmService;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ActivityFilmDetail extends AppCompatActivity {

    @Inject
    FilmService mService;

    public final String TAG = getClass().getSimpleName();
    private ProgressDialog progressDialog;
    private TextView mFilmTitle, mFilmYear, mFilmReleaseDate, mFilmRuntime, mFilmActors, mFilmGenre,
    mFilmPlot, mFilmDirector, mFilmLanguage, mFilmRating, mFilmWriter, mFilmContentRating;
    private ImageView mImgFilmPoster;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_film_detail);

        ((MyApplication) getApplication()).getmApiComponent().inject(this);

        String filmId = getIntent().getExtras().getString("filmId");
        Log.i(TAG, "film: " + filmId);

        mFilmTitle = (TextView) findViewById(R.id.tv_title);
        mFilmYear = (TextView) findViewById(R.id.tv_year);
        mFilmReleaseDate = (TextView) findViewById(R.id.tv_release_date);
        mFilmRuntime = (TextView) findViewById(R.id.tv_runtime);
        mFilmActors = (TextView) findViewById(R.id.tv_actors);
        mFilmGenre = (TextView) findViewById(R.id.tv_genre);
        mFilmPlot = (TextView) findViewById(R.id.tv_plot);
        mFilmDirector = (TextView) findViewById(R.id.tv_director);
        mFilmLanguage = (TextView) findViewById(R.id.tv_language);
        mFilmRating = (TextView) findViewById(R.id.tv_imdb_rating);
        mFilmWriter = (TextView) findViewById(R.id.tv_writer);
        mFilmContentRating = (TextView) findViewById(R.id.tv_rated);

        mImgFilmPoster = (ImageView) findViewById(R.id.img_film);

        showProgressDialog("", "Loading...");
        Observable<FilmUnique> mFilmObservable = mService.getFilmDetails(filmId, "json");
        mFilmObservable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<FilmUnique>() {
                    @Override
                    public void onCompleted() {}

                    @Override
                    public void onError(Throwable e) {
                        Log.i(TAG, e.getMessage());
                        e.getStackTrace();
                    }

                    @Override
                    public void onNext(FilmUnique filmUnique) {

                        Log.i(TAG, "film title: " + filmUnique.getTitle());
                        hideProgressDialog();

                        Picasso.with(ActivityFilmDetail.this)
                                .load(filmUnique.getPoster())
                                .error(R.drawable.ic_error)
                                .into(mImgFilmPoster);

                        mFilmTitle.setText(filmUnique.getTitle());
                        mFilmYear.setText(filmUnique.getYear());
                        mFilmReleaseDate.setText(filmUnique.getReleased());
                        mFilmRuntime.setText(filmUnique.getRuntime());
                        mFilmActors.setText(filmUnique.getActors());
                        mFilmGenre.setText(filmUnique.getGenre());
                        mFilmPlot.setText(filmUnique.getPlot());
                        mFilmDirector.setText(filmUnique.getDirector());
                        mFilmLanguage.setText(filmUnique.getLanguage());
                        mFilmRating.setText(filmUnique.getImdbRating());
                        mFilmWriter.setText(filmUnique.getWriter());
                        mFilmContentRating.setText(filmUnique.getRated());

                    }
                });

    }

    protected void showProgressDialog(String title, String description) {
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(true);
        progressDialog.setTitle(title);
        progressDialog.setMessage(description);
        progressDialog.show();
    }

    protected void hideProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

}