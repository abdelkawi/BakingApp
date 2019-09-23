package com.example.bakingapp;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;

import com.example.bakingapp.model.StepsItem;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.Player.EventListener;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.offline.Download;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a {@link StepListActivity}
 * in two-pane mode (on tablets) or a {@link RecipeDetailActivity}
 * on handsets.
 */
public class StepDetailFragment extends Fragment implements EventListener, View.OnClickListener {

    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_RECIPE_ID = "recipe_id";
    public static final String ARG_STEP_ID = "step_id";
    private RecipeStepsViewModel recipeStepsViewModel;
    private ExoPlayer mExoplayer;
    @BindView(R.id.ep_video_view)
    PlayerView mPlayerView;

    @BindView(R.id.tv_next_step)
    TextView mNextStepTV;

    @BindView(R.id.tv_prev_step)
    TextView mPrevStepTV;
    CollapsingToolbarLayout appBarLayout;

    @BindView(R.id.tv_recipe_step_detail)
    TextView mStepDetails;

    /**
     * The dummy content this fragment is presenting.
     */
    private StepsItem mItem;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public StepDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments().containsKey(ARG_RECIPE_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            recipeStepsViewModel = ViewModelProviders.of(this).get(RecipeStepsViewModel.class);
            recipeStepsViewModel.setStepsItemList(RecipesViewModel.getInstance(getActivity().getApplication()).
                    getRecipe(getArguments().getInt(ARG_RECIPE_ID)).getSteps());
            mItem = recipeStepsViewModel.getStep(getArguments().getInt(ARG_STEP_ID));


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
        View rootView = inflater.inflate(R.layout.step_detail, container, false);
        appBarLayout = getActivity().findViewById(R.id.toolbar_layout);
        // Show the dummy content as text in a TextView.
        ButterKnife.bind(this, rootView);
        setupView();
        mNextStepTV.setOnClickListener(this);
        mPrevStepTV.setOnClickListener(this);

        return rootView;
    }

    void setupView() {
        if (mItem != null) {
            if (appBarLayout != null) {
                appBarLayout.setTitle(mItem.getShortDescription());
            }
            setupPlayer(Uri.parse(mItem.getVideoURL()));
            mStepDetails.setText(mItem.getDescription());
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mExoplayer.release();
    }

    @Override
    public void onStop() {
        super.onStop();

        if (mExoplayer != null) {
            mExoplayer.setPlayWhenReady(false);
            mExoplayer.getPlaybackState();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mExoplayer != null) {
            mExoplayer
                    .setPlayWhenReady(true);
            mExoplayer.getPlaybackState();
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_next_step:
                if (recipeStepsViewModel.getNextItem(mItem.getId()) != null) {
                    mItem = recipeStepsViewModel.getNextItem(mItem.getId());
                    setupView();
                }
                break;
            case R.id.tv_prev_step:
                if (recipeStepsViewModel.getPreviousItem(mItem.getId()) != null) {
                    mItem = recipeStepsViewModel.getPreviousItem(mItem.getId());
                    setupView();
                }
                break;
        }
    }
}
