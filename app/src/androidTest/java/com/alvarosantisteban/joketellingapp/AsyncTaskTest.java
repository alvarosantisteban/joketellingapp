package com.alvarosantisteban.joketellingapp;

import android.support.annotation.NonNull;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.text.TextUtils;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.fail;

/**
 * Tests the asynchronous task used to obtain a joke from the JokesBackend.
 */
@RunWith(AndroidJUnit4.class)
public class AsyncTaskTest {

    private final static String TAG = AsyncTaskTest.class.getSimpleName();

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(
            MainActivity.class);

    /**
     * Test that the joke obtained from backend using a {@code EndpointsAsyncTask} is not null.
     */
    @Test
    public void testResultOfAsyncTask() {

        new EndpointsAsyncTask(new EndpointsAsyncTask.OnAsyncTaskCommunication() {

            @Override
            public void onAsyncTaskFinished(@NonNull String joke) {
                if(!joke.equals(EndpointsAsyncTask.CONNECTION_ERROR)) {
                    assertFalse("A joke was expected, but the string obtained from the AsyncTask is null or empty", TextUtils.isEmpty(joke));
                } else {
                    fail("There is no connection. Is the backend server running?");
                }
            }
        }).execute();
    }
}

