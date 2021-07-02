package com.codepath.apps.restclienttemplate;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.Tweet;

import org.parceler.Parcels;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class DetailsActivity extends AppCompatActivity {
    Tweet tweet;
    RelativeLayout tvMedia;
    ImageView ivProfileImage;
    TextView tvBody;
    TextView tvScreenName;
    TextView tvTime;
    ImageView tvImage;
    ImageButton retweet;
    ImageButton reply;
    ImageButton heart;
    TwitterClient client;

    public static final String TAG = "DetailsActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //set content view only needs to be called once at the top
        setContentView(R.layout.activity_details);

        tweet = Parcels.unwrap(getIntent().getParcelableExtra("tweet"));
        client = TwitterApp.getRestClient(this);
        tvMedia = findViewById(R.id.tvMedia);
        ivProfileImage = findViewById(R.id.ivProfileImage);
        tvBody = findViewById(R.id.tvBody);
        tvScreenName = findViewById(R.id.tvScreenName);
        tvTime = findViewById(R.id.tvTime);
        tvImage = findViewById(R.id.tvImage);
        retweet = findViewById(R.id.retweet);
        reply = findViewById(R.id.reply);
        heart = findViewById(R.id.heart);
        tvBody.setText(tweet.body);
        tvScreenName.setText(tweet.user.screenName);
        tvTime.setText(tweet.relTime);
        //load imageurl with Glide (included in build gradle already)
        Glide.with(this).load(tweet.user.profileImageUrl).into(ivProfileImage);
        Glide.with(this).load(tweet.mediaUrl).centerCrop().transform(new RoundedCornersTransformation(30, 10)).override(1000,500).into(tvImage);



    }

}
