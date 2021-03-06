package com.test.twitch.topgames.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.squareup.picasso.Picasso;
import com.test.twitch.topgames.R;
import com.test.twitch.topgames.api.API;
import com.test.twitch.topgames.model.TopGame;
import com.test.twitch.topgames.model.TopGamesResponse;
import com.test.twitch.topgames.ui.ImageByDensityUtil;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by nathalielima on 8/6/15.
 */
public class ItemRecyclerViewAdapter extends RecyclerView.Adapter<ItemRecyclerViewAdapter.ItemViewHolder> {

    private static final int TOP_LIMIT = 50;
    private static final int VIEW_TYPE_LIST = 0;
    private static final int VIEW_TYPE_GRID = 1;
    private List<TopGame> topGamesList = new ArrayList<>();
    private Context context;
    private boolean isListMode;
    private TopGamesResponse mTopGamesResponse;
    private boolean isLoadingMoreItems;

    public ItemRecyclerViewAdapter(Context context, boolean isListMode) {
        this.context = context;
        this.isListMode = isListMode;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int resLayout = viewType == VIEW_TYPE_LIST ? R.layout.card_list_item : R.layout.card_grid_item;
        View rootView = LayoutInflater.from(parent.getContext()).inflate(resLayout, parent, false);
        return new ItemViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(final ItemViewHolder holder, int position) {
        TopGame item = topGamesList.get(position);
        holder.tvGameName.setText(item.getGame().getName());
        holder.tvViewers.setText(context.getResources().getString(R.string.viewers_total, item.getViewers()));

        Picasso.with(context)
                .load(ImageByDensityUtil.bindImage(context, item.getGame().getLogo()))
                .placeholder(R.drawable.game_placeholder)
                .into(holder.imgGame);
    }

    @Override
    public int getItemCount() {
        return topGamesList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return this.isListMode ? VIEW_TYPE_LIST : VIEW_TYPE_GRID;
    }

    public void setResponseAndResetData(TopGamesResponse response) {
        mTopGamesResponse = response;
        if (response == null) {
            return;
        }

        if (response.getTop() == null || response.getTop().isEmpty()) {
            return;
        }

        this.topGamesList.clear();
        this.topGamesList.addAll(response.getTop());
        notifyDataSetChanged();
    }

    public void notifyLayoutViewChange(boolean isListMode) {
        this.isListMode = isListMode;
        notifyDataSetChanged();
    }

    public void nextPage() {
        final Callback callback = new Callback<TopGamesResponse>() {

            @Override
            public void success(TopGamesResponse topGamesResponse, Response response) {
                if (response != null && response.getStatus() >= 200 && response.getStatus() < 300) {
                    mTopGamesResponse = topGamesResponse;
                    List<TopGame> list = topGamesResponse.getTop();

                    if (list != null) {
                        addAll(list);
                    }

                    setLoadingMoreItems(false);

                }
            }

            @Override
            public void failure(RetrofitError retrofitError) {
            }
        };

        if (!this.isLoadingMoreItems) {
            if (topGamesList.size() >= TOP_LIMIT) {
                return;
            }
        }

        this.setLoadingMoreItems(true);
        new Thread(new Runnable() {
            @Override
            public void run() {
                API.getTopGamesNextPage(mTopGamesResponse.get_links().getNext(), callback);
            }
        }).start();
    }

    public void setLoadingMoreItems(boolean loadingMoreItems) {
        this.isLoadingMoreItems = loadingMoreItems;
    }

    public void addAll(List<TopGame> topGames) {
        topGamesList.addAll(topGames);
        notifyDataSetChanged();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imgGame;
        private final TextView tvGameName;
        private final TextView tvViewers;

        public ItemViewHolder(View itemView) {
            super(itemView);
            tvGameName = (TextView) itemView.findViewById(R.id.tv_game_name);
            tvViewers = (TextView) itemView.findViewById(R.id.tv_viewers);
            imgGame = (ImageView) itemView.findViewById(R.id.img_game);
        }
    }
}
