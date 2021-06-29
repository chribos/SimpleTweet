package com.codepath.apps.restclienttemplate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

public class TimelineActivity extends AppCompatActivity {
    TwitterClient client;
    RecyclerView rvTweets;
    List<Tweet> tweets;
    TweetsAdapter adapter;
    Button logout;

    public static final String TAG = "TimelineActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

         client = TwitterApp.getRestClient(this);
         //find recycler vew
        rvTweets= findViewById(R.id.rvTweets);
        logout = findViewById(R.id.logout);
        //initialize list of tweets and adapter
        tweets = new ArrayList<>();
        adapter= new TweetsAdapter(this, tweets);

        //configure recycler view with layout manager and adapter
        rvTweets.setLayoutManager(new LinearLayoutManager(this));
        rvTweets.setAdapter(adapter);

         populateHomeTimeline();

        logout.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {
                client.clearAccessToken(); // forget who's logged in
                finish(); // navigate backwards to Login screen

            }
        });
    }
    private void populateHomeTimeline() {
        client.getHomeTimeline(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.i(TAG, "onSuccess!" + json.toString());

                //add list of tweets
                JSONArray jsonArray = json.jsonArray;
                try {
                    tweets.addAll(Tweet.fromJsonArray(jsonArray));
                    //notify adapter that data has changed
                    adapter.notifyDataSetChanged();
                }
                catch (JSONException e) {
                    Log.e(TAG, "fail: json exception");
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.e(TAG, "onFailure:(" + response, throwable);
            }
        });
    }
}