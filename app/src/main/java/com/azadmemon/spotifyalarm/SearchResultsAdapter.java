package com.azadmemon.spotifyalarm;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.common.base.Joiner;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import kaaes.spotify.webapi.android.models.ArtistSimple;
import kaaes.spotify.webapi.android.models.Image;
import kaaes.spotify.webapi.android.models.PlaylistSimple;
import kaaes.spotify.webapi.android.models.Track;

public class SearchResultsAdapter extends RecyclerView.Adapter<SearchResultsAdapter.ViewHolder> {

    private final List<Track> mSongs = new ArrayList<>();
    private final List<PlaylistSimple> mPlaylists = new ArrayList<>();
    private final Context mContext;
    private final ItemSelectedListener mListener;
    private Boolean isSongsLastAdded = true;

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final TextView title;
        public final TextView subtitle;
        public final ImageView image;

        public ViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.entity_title);
            subtitle = (TextView) itemView.findViewById(R.id.entity_subtitle);
            image = (ImageView) itemView.findViewById(R.id.entity_image);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            notifyItemChanged(getLayoutPosition());
            String uri = mSongs.size() > 0 ? mSongs.get(getAdapterPosition()).uri : mPlaylists.get(getAdapterPosition()).uri;
            String name = mSongs.size() > 0 ? mSongs.get(getAdapterPosition()).name : mPlaylists.get(getAdapterPosition()).name;
            mListener.onItemSelected(v, uri, name);
        }
    }

    public interface ItemSelectedListener {
        void onItemSelected(View itemView, String uri, String name);
    }

    public SearchResultsAdapter(Context context, ItemSelectedListener listener) {
        mContext = context;
        mListener = listener;
    }

    public void clearData() {
        isSongsLastAdded = false;
        mSongs.clear();
        mPlaylists.clear();
        notifyDataSetChanged();
    }

    public void addSongs(List<Track> items) {
        isSongsLastAdded = true;
        mSongs.addAll(items);
        notifyDataSetChanged();
    }

    public void addPlaylists(List<PlaylistSimple> items) {
        isSongsLastAdded = false;
        mPlaylists.addAll(items);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_result, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (isSongsLastAdded) {
            Track item = mSongs.get(position);

            holder.title.setText(item.name);

            List<String> names = new ArrayList<>();
            for (ArtistSimple i : item.artists) {
                names.add(i.name);
            }
            Joiner joiner = Joiner.on(", ");
            holder.subtitle.setText(joiner.join(names));

            Image image = item.album.images.get(0);
            if (image != null) {
                Picasso.with(mContext).load(image.url).into(holder.image);
            }
        } else {
            PlaylistSimple item = mPlaylists.get(position);

            holder.title.setText(item.name);
            holder.subtitle.setText(item.owner.id != null ? item.owner.id : item.owner.display_name);

            Image image = item.images.get(0);
            if (image != null) {
                Picasso.with(mContext).load(image.url).into(holder.image);
            }
        }
    }

    @Override
    public int getItemCount() {
        return isSongsLastAdded ? mSongs.size() : mPlaylists.size();
    }
}
