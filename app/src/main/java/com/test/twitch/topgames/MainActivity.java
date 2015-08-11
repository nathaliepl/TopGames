package com.test.twitch.topgames;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.test.twitch.topgames.adapter.ItemRecyclerViewAdapter;
import com.test.twitch.topgames.api.API;
import com.test.twitch.topgames.model.TopGamesResponse;
import com.test.twitch.topgames.ui.DividerItemDecoration;
import com.test.twitch.topgames.ui.RecyclerEndlessScrollListener;
import com.test.twitch.topgames.ui.OnBottomListener;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainActivity extends AppCompatActivity {

    private static final String SAVED_INSTANCE_STATE_SHOWING_LIST = "LIST_MODE";
    private static final String SAVED_INSTANCE_STATE_TOP_GAMES_RESPONSE = "SAVED_INSTANCE_STATE_TOP_GAMES_RESPONSE";

    private CharSequence mMenuItemViewModeTitle;
    private TopGamesResponse mTopGamesResponse;
    private ItemRecyclerViewAdapter mAdapter;

    private boolean isListMode = true;

    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        if (mAdapter == null) {
            mAdapter = new ItemRecyclerViewAdapter(this, isListMode);
            mRecyclerView.setAdapter(mAdapter);

            mRecyclerView.addOnScrollListener(new RecyclerEndlessScrollListener(new OnBottomListener() {
                @Override
                public void onBottom() {
                    mAdapter.nextPage();
                }
            }));
        }

        if (savedInstanceState != null) {
            isListMode = savedInstanceState.getBoolean(SAVED_INSTANCE_STATE_SHOWING_LIST, true);
            mAdapter.setResponseAndResetData((TopGamesResponse) savedInstanceState.getSerializable(SAVED_INSTANCE_STATE_TOP_GAMES_RESPONSE));
        } else {
            mAdapter.setLoadingMoreItems(true);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    API.getTopGames(new Callback<TopGamesResponse>() {
                        @Override
                        public void success(TopGamesResponse topGamesResponse, Response response) {
                            if (topGamesResponse != null) {
                                mTopGamesResponse = topGamesResponse;
                                mAdapter.setResponseAndResetData(mTopGamesResponse);
                                mAdapter.setLoadingMoreItems(false);
                            }
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            Toast.makeText(MainActivity.this, "ops!", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }).start();
        }

        defineLayoutViewMode(isListMode, savedInstanceState == null);


    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(SAVED_INSTANCE_STATE_SHOWING_LIST, isListMode);
        outState.putSerializable(SAVED_INSTANCE_STATE_TOP_GAMES_RESPONSE, mTopGamesResponse);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        defineLayoutViewMode(isListMode, false);
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        if (!TextUtils.isEmpty(mMenuItemViewModeTitle)) {
            menu.getItem(0).setTitle(mMenuItemViewModeTitle);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_view_mode) {
            defineLayoutViewMode(!isListMode, false);
            mAdapter.notifyLayoutViewChange(!isListMode);
            item.setTitle(isListMode ? R.string.list_mode : R.string.grid_mode);
            item.setIcon(isListMode ? R.drawable.ic_list_white : R.drawable.ic_grid_on_white);
            mMenuItemViewModeTitle = item.getTitle();
            isListMode = !isListMode;
        }

        return super.onOptionsItemSelected(item);
    }

    private void defineLayoutViewMode(boolean isListMode, boolean addDivider) {
        if (isListMode) {
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

            if (addDivider) {
                mRecyclerView.setItemAnimator(new DefaultItemAnimator());
                mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
            }
        } else {
            mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(getResources().getInteger(R.integer.grid_columns), StaggeredGridLayoutManager.VERTICAL));
        }
    }
}
