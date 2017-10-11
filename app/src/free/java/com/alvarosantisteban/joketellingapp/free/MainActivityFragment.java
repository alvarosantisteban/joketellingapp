package com.alvarosantisteban.joketellingapp.free;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import com.alvarosantisteban.jokedisplayer.JokeDisplayingActivity;
import com.alvarosantisteban.joketellingapp.EndpointsAsyncTask;
import com.alvarosantisteban.joketellingapp.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

/**
 * A placeholder fragment containing a simple view and an ad.
 */
public class MainActivityFragment extends Fragment implements EndpointsAsyncTask.OnAsyncTaskCommunication {

    private ProgressBar progressBar;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main, container, false);

        AdView mAdView = root.findViewById(R.id.adView);
        // Create an ad request. Check logcat output for the hashed device ID to
        // get test ads on a physical device. e.g.
        // "Use AdRequest.Builder.addTestDevice("ABCDEF012345") to get test ads on this device."
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        mAdView.loadAd(adRequest);

        progressBar = root.findViewById(R.id.progress_bar);
        Button button = root.findViewById(R.id.joke_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showProgressBar();

                new EndpointsAsyncTask(MainActivityFragment.this).execute();
            }
        });

        return root;
    }

    private void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onAsyncTaskFinished(@NonNull String joke) {
        hideProgressBar();

        if(!joke.equals(EndpointsAsyncTask.CONNECTION_ERROR)) {
            Intent intent = new Intent(getActivity(), JokeDisplayingActivity.class);
            intent.putExtra(JokeDisplayingActivity.JOKE_EXTRA, joke);
            startActivity(intent);
        } else {
            Snackbar.make(getActivity().findViewById(R.id.fragment),
                    R.string.connection_error, Snackbar.LENGTH_LONG).show();
        }
    }
}
