package com.hg.filmsearchapp.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hg.filmsearchapp.R;
import com.hg.filmsearchapp.model.FilmList;
import com.squareup.picasso.Picasso;

/**
 * Created by Hurman on 18/09/2016.
 */
public class FilmListAdapter extends RecyclerView.Adapter<FilmListAdapter.ViewHolder> {

    public OnItemClickListener mItemClickListener;
    private FilmList mFilmList;
    private int mLayout;
    public Context mContext;


    public FilmListAdapter(Context context, FilmList filmList, int layout){
        this.mContext = context;
        this.mFilmList = filmList;
        this.mLayout = layout;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(mLayout, parent, false));    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.tvTitle.setText(mFilmList.getSearch().get(position).getTitle());
        holder.tvYear.setText(mFilmList.getSearch().get(position).getYear());

        Picasso.with(mContext)
                .load(mFilmList.getSearch().get(position).getPoster())
                .error(R.drawable.ic_error)
                .into(holder.imgFilm);
    }

    @Override
    public int getItemCount() {
        return mFilmList.getSearch() != null ? mFilmList.getSearch().size() : 0;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public ImageView imgFilm;
        public TextView tvTitle, tvYear;

        public ViewHolder(View view) {
            super(view);

            imgFilm = (ImageView) view.findViewById(R.id.img_film);
            tvTitle = (TextView) view.findViewById(R.id.tv_title);
            tvYear = (TextView) view.findViewById(R.id.tv_year);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(view, getAdapterPosition());
            }
        }
    }

}
