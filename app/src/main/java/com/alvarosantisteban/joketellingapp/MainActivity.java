package com.alvarosantisteban.joketellingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.alvarosantisteban.jokedisplayer.JokeDisplayingActivity;

/**
 * Retrieves jokes from the JokeProvider lib and passes them to the JokeDisplayer lib.
 */
public class MainActivity extends AppCompatActivity implements EndpointsAsyncTask.OnAsyncTaskCommunication {

    private final static String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void tellJoke(View view) {
        new EndpointsAsyncTask(this).execute();
    }

    @Override
    public void onAsyncTaskFinished(@NonNull String joke) {
        if(!joke.equals(EndpointsAsyncTask.CONNECTION_ERROR)) {
            Intent intent = new Intent(this, JokeDisplayingActivity.class);
            intent.putExtra(JokeDisplayingActivity.JOKE_EXTRA, joke);
            startActivity(intent);
        } else {
            Snackbar.make(findViewById(R.id.fragment),
                    R.string.connection_error, Snackbar.LENGTH_LONG).show();
        }
    }
}
