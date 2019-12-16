package com.nixsol.mahesh.home;


import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.app.FragmentTransaction;

import com.nixsol.mahesh.R;
import com.nixsol.mahesh.ui.home.HomeActivity;
import com.nixsol.mahesh.ui.home.fragments.HomeFragment;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class HomeScreenInitiatTest {

    @Rule
    public ActivityTestRule<HomeActivity> activityTestRule = new ActivityTestRule<HomeActivity>(HomeActivity.class);
    private String TAG = "HomeScreenTest";

    @Before
    public void init() {
        activityTestRule.getActivity()
                .getSupportFragmentManager().beginTransaction();
        activityTestRule.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                HomeFragment homeFragment = startHomeScreenFragment();
            }
        });
    }

    @Test
    public void homeFragmentCanBeInstantiated() {
        // instantiating home fragment object
        activityTestRule.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                HomeFragment homeFragment = startHomeScreenFragment();
            }
        });
        // then use click event after load the data into recycler view
        onView(withId(R.id.recylerView))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
    }


    private HomeFragment startHomeScreenFragment() {
        HomeActivity activity = (HomeActivity) activityTestRule.getActivity();
        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
        HomeFragment homeFragment = HomeFragment.newInstance();
        transaction.add(homeFragment, "HOME");
        transaction.commit();
        return homeFragment;
    }

}
