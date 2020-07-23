package com.vinuthana.vinvidya.activities.otheractivities;

import android.app.Activity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.vinuthana.vinvidya.R;


import java.util.List;

/**
 * Created by KISHAN on 08-15-2017.
 */

public class GalleryAlbumsAdapter extends RecyclerView.Adapter<GalleryAlbumsAdapter.MyViewHolder> {
    private Activity mContext;
    private List<GalleryAlbum> albumList;

    public GalleryAlbumsAdapter(Activity mContext, List<GalleryAlbum> albumList) {
        this.mContext = mContext;
        this.albumList = albumList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, count;
        public ImageView thumbnail, overflow;

        public MyViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.txtvGalleryCard);
            count = (TextView) itemView.findViewById(R.id.txtvGalleryCardCount);
            thumbnail = (ImageView) itemView.findViewById(R.id.imageViewGalleryCard);
            overflow = (ImageView) itemView.findViewById(R.id.imgviewGalleryCardOverFlow);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.gallery_card, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        GalleryAlbum galleryAlbum = albumList.get(position);
        holder.title.setText(galleryAlbum.getName());
        holder.count.setText(galleryAlbum.getNumOfSongs() + "songs");

        Glide.with(mContext).load(galleryAlbum.getThumbnail()).into(holder.thumbnail);

        holder.overflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopUpMenu(holder.overflow);
            }
        });
    }

    private void showPopUpMenu(View overflow) {
        PopupMenu popup = new PopupMenu(mContext, overflow);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_album, popup.getMenu());
        popup.setOnMenuItemClickListener(new MyMenuItemClickListener());
        popup.show();
    }


    private class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {
        public MyMenuItemClickListener() {
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_add_favourite:
                    Toast.makeText(mContext, "Add to favoirite", Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.action_play_next:
                    Toast.makeText(mContext, "Play Next", Toast.LENGTH_SHORT).show();
                    return true;
                default:
            }
            return false;
        }
    }

    @Override
    public int getItemCount() {
        return albumList.size();
    }

}
