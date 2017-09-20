package com.alvarosantisteban.jokedisplayer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Displays a joke.
 */
public class JokeDisplayingActivity extends AppCompatActivity {

    public static final String JOKE_EXTRA = "jokeExtra";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joke_displaying);

        TextView textView = (TextView) findViewById(R.id.joke_text);

        Bundle extras = getIntent().getExtras();
        if(extras != null && extras.containsKey(JOKE_EXTRA)) {
            String joke = extras.getString(JOKE_EXTRA);
            textView.setText(joke);
        }
    }
}
