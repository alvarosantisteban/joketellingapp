package com.alvarosantisteban.joketellingapp.free;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import com.alvarosantisteban.jokedisplayer.JokeDisplayingActivity;
import com.alvarosantisteban.joketellingapp.EndpointsAsyncTask;
import com.alvarosantisteban.joketellingapp.R;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

/**
 * Displays a button to get a joke after displaying an ad interstitial. Also displays a banner ad.
 */
public class MainActivityFragment extends Fragment implements EndpointsAsyncTask.OnAsyncTaskCommunication {

    private final static String TAG = MainActivityFragment.class.getSimpleName();

    private ProgressBar progressBar;
    private InterstitialAd interstitialAd;

    private String joke;

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

        createInterstitialAd();

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

    @Override
    public void onResume() {
        super.onResume();

        // Load the interstitial in onResume instead of onCreateView, so it is ready even if the
        // user returns from another activity
        interstitialAd.loadAd(new AdRequest.Builder().build());
    }

    @Override
    public void onAsyncTaskFinished(@NonNull String joke) {
        hideProgressBar();

        this.joke = joke;
        if(!joke.equals(EndpointsAsyncTask.CONNECTION_ERROR)) {
            if (interstitialAd.isLoaded()) {
                interstitialAd.show();
            } else {
                Log.w(TAG, "Interstitial was not loaded");

                goToJokeDisplayingActivity();
            }
        } else {
            Snackbar.make(getActivity().findViewById(R.id.fragment),
                    R.string.connection_error, Snackbar.LENGTH_LONG).show();
        }
    }

    private void createInterstitialAd() {
        interstitialAd = new InterstitialAd(getActivity());
        interstitialAd.setAdUnitId(getResources().getString(R.string.interstitial_ad_unit_id));
        interstitialAd.loadAd(new AdRequest.Builder().build());
        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                Log.v("Ads", "onAdLoaded");
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
                Log.v("Ads", "onAdFailedToLoad");
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when the ad is displayed.
                Log.v("Ads", "onAdOpened");
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
                Log.v("Ads", "onAdLeftApplication");
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when when the interstitial ad is closed.
                Log.v("Ads", "onAdClosed");
                goToJokeDisplayingActivity();
            }
        });
    }

    private void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
    }

    private void goToJokeDisplayingActivity() {
        Intent intent = new Intent(getActivity(), JokeDisplayingActivity.class);
        intent.putExtra(JokeDisplayingActivity.JOKE_EXTRA, this.joke);
        startActivity(intent);
    }
}
