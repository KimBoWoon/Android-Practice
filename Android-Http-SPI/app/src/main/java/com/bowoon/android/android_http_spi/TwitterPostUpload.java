package com.bowoon.android.android_http_spi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.util.Log;

import com.bowoon.android.android_http_spi.util.Utility;
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

import java.io.File;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;

public class TwitterPostUpload extends Activity {
    private TwitterAuthClient twitterAuthClient;
    private byte[] imageFile;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Twitter.initialize(getApplicationContext());

        File file = new File(getIntent().getStringExtra("image"));
        imageFile = Utility.fileToByte(file);

        twitterAuthClient = new TwitterAuthClient();
        twitterAuthClient.authorize(this, new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                Log.i("TwitterLogin", "Success");
                TwitterSession session = TwitterCore.getInstance().getSessionManager().getActiveSession();
                TwitterAuthToken authToken = session.getAuthToken();
                String token = authToken.token;
                String secret = authToken.secret;
                Log.i("token", token);
                Log.i("secret", secret);
            }

            @Override
            public void failure(TwitterException exception) {
                Log.i("TwitterLogin", "failure");
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == twitterAuthClient.getRequestCode()) {
            twitterAuthClient.onActivityResult(requestCode, resultCode, data);

            TwitterApiClient twitterApiClient = TwitterCore.getInstance().getApiClient();
            final StatusesService statusesService = twitterApiClient.getStatusesService();
            MediaService mediaService = twitterApiClient.getMediaService();

            String encodedImage = Base64.encodeToString(imageFile, Base64.DEFAULT);
//            Log.i("encodedImage", encodedImage);

            RequestBody media = RequestBody.create(MediaType.parse("image/gif"), imageFile);
            RequestBody mediaData = RequestBody.create(MediaType.parse("text/plain"), encodedImage);

            Call<Media> call = mediaService.upload(media, mediaData, null);
            call.enqueue(new Callback<Media>() {
                @Override
                public void success(Result<Media> result) {
                    Log.i("success", String.valueOf(result.response.raw()));
                    Log.i("success", result.data.mediaIdString);
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
                    Log.i("failure", exception.getMessage());
                }
            });

            twitterAuthClient.cancelAuthorize();
            finish();
        }
    }
}
