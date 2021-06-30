package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.Tweet;

import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;


//after defining viewholder, extend rv adapter and paramertize with viewholder we just defined
public class TweetsAdapter extends RecyclerView.Adapter<TweetsAdapter.ViewHolder> {
    //pass in context and list of tweet
    Context context;
    List<Tweet> tweets;

    public TweetsAdapter(Context context, List<Tweet> tweets) {
        this.context = context;
        this.tweets = tweets;
    }
    public void clear() {
        tweets.clear();
        notifyDataSetChanged();
    }
    public void addAll(List<Tweet> list) {
        tweets.addAll(list);
        notifyDataSetChanged();
    }


    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        //for each row inflate data
        View view = LayoutInflater.from(context).inflate(R.layout.item_tweet, parent, false);
        return new ViewHolder(view);
    }
//bind view data for each position
    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
    //get data
        Tweet tweet = tweets.get(position);

        //bind tweet with view holder (create bind function at bottom)
        holder.bind(tweet);
    }

    @Override
    public int getItemCount() {
        return tweets.size();
    }
    //pass in context and list of tweets

    //for each row inflate a layout for a tweet

    //Bind values based on the position of the element

    //define view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivProfileImage;
        TextView tvBody;
        TextView tvScreenName;
        TextView tvTime;
        ImageView tvImage;
        //this itemView that is passed in is a representation of one row of the recycler view aka a tweet
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProfileImage = itemView.findViewById(R.id.ivProfileImage);
            tvBody = itemView.findViewById(R.id.tvBody);
            tvScreenName = itemView.findViewById(R.id.tvScreenName);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvImage = itemView.findViewById(R.id.tvImage);
        }

        public void bind(Tweet tweet) {
            //fill out tweet attributes
            tvBody.setText(tweet.body);
            tvScreenName.setText(tweet.user.screenName);
            tvTime.setText(tweet.relTime);
            //load imageurl with Glide (included in build gradle already)
            Glide.with(context).load(tweet.user.profileImageUrl).into(ivProfileImage);
            Glide.with(context).load(tweet.mediaUrl).centerCrop().transform(new RoundedCornersTransformation(30, 10)).override(1000,500).into(tvImage);


        }
    }

}

