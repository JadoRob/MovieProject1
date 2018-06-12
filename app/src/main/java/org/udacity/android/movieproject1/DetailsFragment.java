package org.udacity.android.movieproject1;


import android.support.v4.app.Fragment;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class DetailsFragment extends Fragment {

    private static final String TAG = DetailsFragment.class.getSimpleName();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_details, container, false);
        final MovieViewModel movieViewModel = ViewModelProviders.of(getActivity())
                .get(MovieViewModel.class);
        Log.i(TAG, movieViewModel.getTitle()); // app crashes when clicked on a grid item. If I
        //comment this Log out then it works fine.

        return rootView;
    }



}
