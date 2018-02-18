package com.rasmusdev.cameraroll;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements OnTaskCompleted, GalleryRecyclerViewAdapter.OnGalleryItemClickListener {

    private RecyclerView mRecyclerView;
    private GalleryRecyclerViewAdapter mAdapter;
    private AndroidMediaStore mediaStore;

    private ContentObserver observer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mediaStore = new AndroidMediaStore(this);
        setContentView(R.layout.activity_main);
        mRecyclerView = findViewById(R.id.gallery_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        LayoutManager mLayoutManager = new GridLayoutManager(this, 3);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new GalleryRecyclerViewAdapter(this, this);
        mRecyclerView.setAdapter(mAdapter);

        /// Animation
        int resId = R.anim.layout_animation_fall_down;
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(this, resId);
        mRecyclerView.setLayoutAnimation(animation);

        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                // Explain to the user why we need to read the contacts
            }

            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);

            // MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE is an
            // app-defined int constant that should be quite unique

            return;
        }
        initDataset();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    initDataset();

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    private void initDataset() {
        this.mediaStore.getImages(this);
    }

    @Override
    public void onTaskCompleted(final ArrayList<GalleryItem> items) {
//        mDataset = paths.toArray(new String[paths.size()]);
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mAdapter.setDataSet(items);
                mAdapter.notifyDataSetChanged();
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();

        observer = new ContentObserver(new Handler());
        observer.setListener(new ContentObserver.Listener() {
            @Override
            public void onChange(boolean selfChange, Uri uri) {
                Log.d("MainActivity", "onChange()");
                mediaStore.dataChanged = true;
                //observer.unregister(MainActivity.this);
                //observer = null;
            }
        });
        observer.register(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(mediaStore.dataChanged) {
            mediaStore.dataChanged = false;
            initDataset();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (observer != null) {
            observer.unregister(this);
        }
    }

    @Override
    public void onItemClick(int position, GalleryItem item, ImageView sharedImageView) {
        Intent intent = new Intent(this, ImageDetails.class);

        intent.putExtra("IMAGE_CLICKED", item.getPath());
        ActivityOptionsCompat options = ActivityOptionsCompat.
                makeSceneTransitionAnimation(this, sharedImageView, "details");

        startActivity(intent, options.toBundle());
    }
}
