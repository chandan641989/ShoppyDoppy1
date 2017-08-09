package com.kartnap.chandan.shoppydoppy;

/**
 * Created by Chandan on 6/30/2017.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Chandan on 5/10/2017.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.CustomViewHolder> {
    private List<FeedItems> feedItemList;
    private Context context;
    private OnItemClickLIstener onItemClickLIstener;
    public RecyclerViewAdapter(Context context, List<FeedItems> feedItemList){
        this.context = context;
        this.feedItemList = feedItemList;

    }
    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_card_view,parent,false);
        CustomViewHolder customViewHolder = new CustomViewHolder(v);

        return customViewHolder;
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        final FeedItems feedItem = feedItemList.get(position);
        if (!TextUtils.isEmpty(feedItem.getImage())){
            Picasso.with(context).load((feedItem.getImage()))
                    .error(R.drawable.images)
                    .placeholder(R.drawable.images)
                    .into(holder.imageView);

        }
        holder.title.setText(feedItem.getTitle());

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickLIstener.OnItemClick(feedItem);

            }
        };
        holder.imageView.setOnClickListener(listener);
        holder.title.setOnClickListener(listener);
//        holder.price.setOnClickListener(listener);

    }

    @Override
    public int getItemCount() {
        return (null != feedItemList ? feedItemList.size() : 0);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        protected ImageView imageView;
        protected TextView title;

        public CustomViewHolder(View itemView) {
            super(itemView);
            this.imageView = (ImageView) itemView.findViewById(R.id.thumbnail);
            this.title = (TextView)itemView.findViewById(R.id.title_view);
        }
    }
//    public void setOnItemClickLIstener(OnItemClickLIstener onItemClickLIstener){
//        this.onItemClickLIstener=onItemClickLIstener;
//
//    }
public void setOnItemClickLIstener(OnItemClickLIstener onItemClickLIstener){
    this.onItemClickLIstener=onItemClickLIstener;

}
}

