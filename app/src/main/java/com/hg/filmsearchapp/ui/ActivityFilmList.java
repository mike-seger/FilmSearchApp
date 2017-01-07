package com.hg.filmsearchapp.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.hg.filmsearchapp.MyApplication;
import com.hg.filmsearchapp.R;
import com.hg.filmsearchapp.model.FilmList;
import com.hg.filmsearchapp.webservice.FilmService;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.jakewharton.rxbinding.widget.TextViewTextChangeEvent;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ActivityFilmList extends AppCompatActivity {

    @Inject
    FilmService mService;

    public final String TAG = getClass().getSimpleName();
    private ProgressDialog progressDialog;
    private EditText mFieldMovieName;
    private RecyclerView mRecyclerView;
    private FilmListAdapter mFilmListAdapter;
    private Observable<FilmList> mFilmListObservable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ((MyApplication) getApplication()).getmApiComponent().inject(this);

        mFieldMovieName = (EditText) findViewById(R.id.field_movie_name);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_film_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        showProgressDialog("", "Loading...");

        RxTextView.textChangeEvents(mFieldMovieName)
                .debounce(400, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<TextViewTextChangeEvent>() {
                    @Override
                    public void onCompleted() {
                        Log.i(TAG, "onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i(TAG, "onError " + e.getMessage());
                    }

                    @Override
                    public void onNext(TextViewTextChangeEvent textViewTextChangeEvent) {
                        Log.i(TAG, "onNext " + textViewTextChangeEvent.text().toString());

                        mFilmListObservable = mService.getFilmList(textViewTextChangeEvent.text().toString(), "json", "movie");
                        mFilmListObservable.subscribeOn(Schedulers.newThread())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Subscriber<FilmList>() {
                                    @Override
                                    public void onCompleted() {}

                                    @Override
                                    public void onError(Throwable e) {
                                        Log.i(TAG, e.getMessage());
                                        e.getStackTrace();
                                    }

                                    @Override
                                    public void onNext(final FilmList filmList) {

                                        hideProgressDialog();
                                        Log.i(TAG, "size of list: " + filmList.getSearch().size());

                                        mFilmListAdapter = new FilmListAdapter(ActivityFilmList.this, filmList, R.layout.row_film_list);
                                        mRecyclerView.setAdapter(mFilmListAdapter);

                                        mFilmListAdapter.setOnItemClickListener(new FilmListAdapter.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(View view, int position) {

                                                Intent intent = new Intent(ActivityFilmList.this, ActivityFilmDetail.class);
                                                intent.putExtra("filmId", filmList.getSearch().get(position).getImdbID());
                                                startActivity(intent);
                                            }
                                        });

                                    }
                                });
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
