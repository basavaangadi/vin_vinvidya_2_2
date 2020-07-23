package com.vinuthana.vinvidya.activities.otheractivities;

import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.vinuthana.vinvidya.R;

import java.util.ArrayList;
import java.util.List;

public class GalleryActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private GalleryAlbumsAdapter adapter;
    private List<GalleryAlbum> albumList;
    String strStudentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        Toolbar toolbar = (Toolbar) findViewById(R.id.gallaryAlbumtoolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Gallery");
        try {
            strStudentId = getIntent().getExtras().getString("studentId");

            //Toast.makeText(this, strStudId, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        initCollapsingToolbar();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerGallery);
        albumList = new ArrayList<>();
        adapter = new GalleryAlbumsAdapter(GalleryActivity.this, albumList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        prepareAlbums();

        try {
            Glide.with(this).load(R.drawable.cover).into((ImageView) findViewById(R.id.backdrop));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void prepareAlbums() {
        int[] cover = new int[] {
                R.drawable.album1,
                R.drawable.album2,
                R.drawable.album3,
                R.drawable.album4,
                R.drawable.album5,
                R.drawable.album6,
                R.drawable.album7,
                R.drawable.album8,
                R.drawable.album9,
                R.drawable.album10,
                R.drawable.album11,
        };
        GalleryAlbum galleryAlbum = new GalleryAlbum("Ture Romance", 13,cover[0]);
        albumList.add(galleryAlbum);

        galleryAlbum = new GalleryAlbum("Xscape",8,cover[1]);
        albumList.add(galleryAlbum);

        galleryAlbum = new GalleryAlbum("Maroon 5", 11,cover[2]);
        albumList.add(galleryAlbum);

        galleryAlbum = new GalleryAlbum("Born to Die", 12,cover[3]);
        albumList.add(galleryAlbum);

        galleryAlbum = new GalleryAlbum("Honeymoon", 14,cover[4]);
        albumList.add(galleryAlbum);

        galleryAlbum = new GalleryAlbum("I Need a Doctor", 1,cover[5]);
        albumList.add(galleryAlbum);

        galleryAlbum = new GalleryAlbum("Loud", 11,cover[6]);
        albumList.add(galleryAlbum);

        galleryAlbum = new GalleryAlbum("Legend", 14,cover[7]);
        albumList.add(galleryAlbum);

        galleryAlbum = new GalleryAlbum("Hello", 11,cover[8]);
        albumList.add(galleryAlbum);

        galleryAlbum = new GalleryAlbum("Greatest Hits", 17,cover[9]);
        albumList.add(galleryAlbum);

        adapter.notifyDataSetChanged();
    }

    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {
        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount,int spacing,boolean includeEdge) {
            this.spacing = spacing;
            this.spanCount = spanCount;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int postion = parent.getChildAdapterPosition(view);
            int column = postion % spanCount;

            if(includeEdge) {
                outRect.left = spacing - column * spacing / spanCount;

                if(postion < spanCount) {
                    outRect.top = spacing;
                }
                outRect.bottom = spacing;
            } else {
                outRect.left = column * spacing / spanCount;
                outRect.right =spacing - (column +1) * spacing / spanCount;
                if (postion >= spanCount) {
                    outRect.top = spacing;
                }
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private void initCollapsingToolbar() {
        final CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_gallery);
        collapsingToolbar.setTitle(" ");
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbarGallery);
        appBarLayout.setExpanded(true);

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbar.setTitle("Gallery");
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }
}
