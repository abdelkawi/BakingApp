package com.example.bakingapp.ui;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.bakingapp.R;
import com.example.bakingapp.data.RecipeItem;
import com.example.bakingapp.data.StepsItem;
import com.example.bakingapp.ui.activity.RecipeDetailsActivity;
import com.example.bakingapp.viewmodel.RecipeViewModel;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.Player.EventListener;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.gson.Gson;


public class RecipeDetailFragment extends Fragment implements EventListener, View.OnClickListener {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_RECIPE = "recipePos";
    public static final String ARG_STEP = "stepPos";

    /**
     * The dummy content this fragment is presenting.
     */
    private StepsItem mCurrentStepItem;

    int recipePos = 0, stepPos = 0, stepSize = 0;

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */


    // exoplayer
    private ExoPlayer mExoplayer;
    private PlayerView mPlayerView;
    private TextView mStepDescriptionTv, mNextStepTv, mPrevStepTv;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments().containsKey(ARG_STEP)) {
            recipePos = getArguments().getInt(ARG_RECIPE);
            stepPos = getArguments().getInt(ARG_STEP);
            mCurrentStepItem = RecipeViewModel.getInstance(getActivity().getApplication())
                    .getCurrentStep(recipePos, stepPos);
            stepSize = RecipeViewModel.getInstance(getActivity().getApplication()).getCurrentRecipe(recipePos).getSteps().size();
        }
    }

    void setupPlayer(Uri uri) {
        if (mExoplayer == null) {
            // Create an instance of the ExoPlayer.
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mExoplayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);
            mPlayerView.setPlayer(mExoplayer);

            // Set the ExoPlayer.EventListener to this activity.
            mExoplayer.addListener(this);

        }
            // Prepare the MediaSource.
            String userAgent = Util.getUserAgent(getContext(), "BakingApp");
            MediaSource mediaSource = new ExtractorMediaSource(uri, new DefaultDataSourceFactory(
                    getContext(), userAgent), new DefaultExtractorsFactory(), null, null);
            mExoplayer.prepare(mediaSource);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recipe_detail, container, false);
        mPlayerView = rootView.findViewById(R.id.ep_video_view);
        mStepDescriptionTv = rootView.findViewById(R.id.recipestep_detail);
        mNextStepTv = rootView.findViewById(R.id.tv_next_step);
        mPrevStepTv = rootView.findViewById(R.id.tv_prev_step);
        mNextStepTv.setOnClickListener(this);
        mPrevStepTv.setOnClickListener(this);
        // Show the dummy content as text in a TextView.
        if (mCurrentStepItem != null) {
            updateView();
        }
        return rootView;
    }

    void updateView() {
        mStepDescriptionTv.setText(mCurrentStepItem.getDescription());
        if(getResources().getBoolean(R.bool.isTablet))
        {
            mStepDescriptionTv.setVisibility(View.VISIBLE);
        }
        ((RecipeDetailsActivity) getActivity()).getSupportActionBar().setTitle(
                RecipeViewModel.getInstance(getActivity().getApplication()).getCurrentRecipe(recipePos).getName()  +
                       getString(R.string.step)+ (stepPos+1));

        setupPlayer(Uri.parse(mCurrentStepItem.getVideoURL()));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_prev_step:
                if (stepPos > 0) {
                    stepPos -= 1;
                    mCurrentStepItem = RecipeViewModel.getInstance(getActivity().getApplication())
                            .getCurrentStep(recipePos, stepPos);
                    updateView();
                }
                break;
            case R.id.tv_next_step:
                if (stepPos < stepSize - 1) {
                    stepPos += 1;
                    mCurrentStepItem = RecipeViewModel.getInstance(getActivity().getApplication())
                            .getCurrentStep(recipePos, stepPos);
                    updateView();
                }
                break;
        }
    }
}
