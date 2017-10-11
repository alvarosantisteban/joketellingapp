package com.alvarosantisteban.joketellingapp;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.alvarosantisteban.joketellingapp.backend.myApi.MyApi;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;

import java.io.IOException;

/**
 * AsyncTask to make requests to the backend server JokesBackend.
 */
public class EndpointsAsyncTask extends AsyncTask<Void, Void, String> {

    public interface OnAsyncTaskCommunication {
        void onAsyncTaskFinished(@NonNull String joke);
    }

    private final static String TAG = EndpointsAsyncTask.class.getSimpleName();

    private final static String IP_ADDRESS = "192.168.0.82";
    public final static String CONNECTION_ERROR = "connectionError";

    private static MyApi myApiService = null;
    private OnAsyncTaskCommunication onAsyncTaskCommunication;

    public EndpointsAsyncTask(OnAsyncTaskCommunication onAsyncTaskCommunication) {
        this.onAsyncTaskCommunication = onAsyncTaskCommunication;
    }

    @Override
    protected final String doInBackground(Void... params) {
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

        try {
            return myApiService.getJoke().execute().getData();
        } catch (IOException e) {
            return CONNECTION_ERROR;
        }
    }

    @Override
    protected void onPostExecute(@NonNull String joke) {
        if (onAsyncTaskCommunication != null) {
            onAsyncTaskCommunication.onAsyncTaskFinished(joke);
        } else {
            Log.e("Test", "There is no one to inform");
        }
    }
}