package com.bowoon.android.android_http_spi;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.util.Log;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;
import com.twitter.sdk.android.core.models.Media;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.services.MediaService;
import com.twitter.sdk.android.core.services.StatusesService;

import java.io.ByteArrayOutputStream;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;

public class TwitterPostUpload extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Twitter.initialize(getApplicationContext());

        TwitterAuthClient twitterAuthClient = new TwitterAuthClient();
        twitterAuthClient.authorize(this, new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                TwitterSession session = TwitterCore.getInstance().getSessionManager().getActiveSession();
                TwitterAuthToken authToken = session.getAuthToken();
                String token = authToken.token;
                String secret = authToken.secret;
                Log.i("token", token);
                Log.i("secret", secret);
                Log.i("Twitter", "Success");
            }

            @Override
            public void failure(TwitterException exception) {
                Log.i("Twitter", "failure");
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        TwitterApiClient twitterApiClient = TwitterCore.getInstance().getApiClient();
        StatusesService statusesService = twitterApiClient.getStatusesService();

        MediaService mediaService = twitterApiClient.getMediaService();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        String encodedImage = Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT);
        Log.i("encodedImage", encodedImage);

        RequestBody media = RequestBody.create(MediaType.parse("image/jpeg"), outputStream.toByteArray());
        RequestBody mediaData = RequestBody.create(MediaType.parse("text/plain"), encodedImage);

        Call<Media> call = mediaService.upload(media, mediaData, null);
        call.enqueue(new Callback<Media>() {
            @Override
            public void success(Result<Media> result) {
                Log.i("success", String.valueOf(result.response.raw()));
                Call<Tweet> call = statusesService.update("Test", null, null, null, null, null, null, null, result.data.mediaIdString);
                call.enqueue(new Callback<Tweet>() {
                    @Override
                    public void success(Result<Tweet> result) {
                        //Do something with result
                        Log.i("twitter", String.valueOf(result.response.raw()));
                    }

                    public void failure(TwitterException exception) {
                        //Do something on failure
                    }
                });
            }

            @Override
            public void failure(TwitterException exception) {
                Log.i("failure", "failure");
            }
        });

        finish();

        // Pass the activity result to the login button.
//        loginButton.onActivityResult(requestCode, resultCode, data);
    }
}
