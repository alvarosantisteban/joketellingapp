package com.alvarosantisteban.joketellingapp;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import com.alvarosantisteban.jokedisplayer.JokeDisplayingActivity;
import com.alvarosantisteban.joketellingapp.backend.myApi.MyApi;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;

import java.io.IOException;

/**
 * AsyncTask to make requests to the backend server JokesBackend.
 */
class EndpointsAsyncTask extends AsyncTask<Context, Void, String> {

    private final static String IP_ADDRESS = "192.168.0.82";

    private static MyApi myApiService = null;
    private Context context;

    @Override
    protected final String doInBackground(Context... params) {
        if(myApiService == null) {  // Only do this once
            MyApi.Builder builder = new MyApi.Builder(AndroidHttp.newCompatibleTransport(),
                    new AndroidJsonFactory(), null)
                    // options for running against local devappserver
                    // - 10.0.2.2 is localhost's IP address in Android emulator
                    // - turn off compression when running against local devappserver
                    .setRootUrl("http://" +IP_ADDRESS +":8080/_ah/api/")
                    .setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
                        @Override
                        public void initialize(AbstractGoogleClientRequest<?> abstractGoogleClientRequest) throws IOException {
                            abstractGoogleClientRequest.setDisableGZipContent(true);
                        }
                    });
            // end options for devappserver

            myApiService = builder.build();
        }

        context = params[0];

        try {
            return myApiService.getJoke().execute().getData();
        } catch (IOException e) {
            return e.getMessage();
        }
    }

    @Override
    protected void onPostExecute(String joke) {
        Intent intent = new Intent(context, JokeDisplayingActivity.class);
        intent.putExtra(JokeDisplayingActivity.JOKE_EXTRA, joke);
        context.startActivity(intent);
    }
}