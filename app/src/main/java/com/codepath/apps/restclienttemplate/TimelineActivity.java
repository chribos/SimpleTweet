package com.codepath.apps.restclienttemplate;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.parceler.Parcels;

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
    private final int REQUEST_CODE = 20;
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Inflate the menu to add items to the action bar
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    //make action for when option item is selected
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.compose) {
            //Compose icon has been selected
            Toast.makeText(this, "compose", Toast.LENGTH_SHORT).show();

            //navigate to compose activity with intent (compose tweets after setting up tweet button and compose box
            Intent intent = new Intent(this, ComposeActivity.class);
            //change start activity to reflect your new tweet on the homepage
            //this launches child activity (compose) and will retunr a tweet if it was submitted successfuly
            //to notify we need to modify onActivityResult
            startActivityForResult(intent, REQUEST_CODE);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        if(  requestCode == REQUEST_CODE && resultCode == RESULT_OK ) {
            //GET DATA (tweet object) FROM INTENT
          Tweet tweet = Parcels.unwrap(data.getParcelableExtra("tweet"));
            //update recycler view from new tweet
            //modify data source
            tweets.add(0, tweet);
            adapter.notifyItemInserted(0);
            //update adapter

            //scroll
            rvTweets.smoothScrollToPosition(0);
        }
        super.onActivityResult(requestCode, resultCode, data);
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