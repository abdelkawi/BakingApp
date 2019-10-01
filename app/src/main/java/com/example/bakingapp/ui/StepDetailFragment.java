package com.example.bakingapp.ui;

import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;

import com.example.bakingapp.R;
import com.example.bakingapp.viewmodel.RecipeStepsViewModel;
import com.example.bakingapp.viewmodel.RecipesViewModel;
import com.example.bakingapp.model.StepsItem;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;

import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.Player.EventListener;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
    private SimpleExoPlayer mExoplayer;
    @BindView(R.id.ep_video_view)
    PlayerView mPlayerView;

    @BindView(R.id.tv_next_step)
    TextView mNextStepTV;

    @BindView(R.id.tv_prev_step)
    TextView mPrevStepTV;
    CollapsingToolbarLayout appBarLayout;

    @BindView(R.id.tv_recipe_step_detail)
    TextView mStepDetails;
    private MediaSessionCompat mediaSession;
    private PlaybackStateCompat.Builder stateBuilder;
    private int resumeWindow;
    private long resumePosition;
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
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) mPlayerView.getLayoutParams();
        if (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            params.width = params.MATCH_PARENT;
            params.height = params.MATCH_PARENT;
            mPlayerView.setLayoutParams(params);
        }
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments().containsKey(ARG_RECIPE_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            clearResumePosition();
            setRetainInstance(true);
            recipeStepsViewModel = ViewModelProviders.of(this).get(RecipeStepsViewModel.class);
            recipeStepsViewModel.setStepsItemList(RecipesViewModel.getInstance(getActivity().getApplication()).
                    getRecipe(getArguments().getInt(ARG_RECIPE_ID)).getSteps());
            mItem = recipeStepsViewModel.getStep(getArguments().getInt(ARG_STEP_ID));


        }
    }
    private void updateResumePosition() {
        resumeWindow = mExoplayer.getCurrentWindowIndex();
        resumePosition = mExoplayer.isCurrentWindowSeekable() ? Math.max(0, mExoplayer.getCurrentPosition())
                : C.TIME_UNSET;
    }

    private void clearResumePosition() {
        resumeWindow = C.INDEX_UNSET;
        resumePosition = C.TIME_UNSET;
    }

    private void releasePlayer() {
        if (mExoplayer != null) {
            updateResumePosition();
            mExoplayer.stop();
            mExoplayer.release();
            mExoplayer = null;
        }
    }
    @Override
    public void onPause() {
        super.onPause();

        releasePlayer();

    }

    @Override
    public void onResume() {
        super.onResume();
        if (mExoplayer == null) {
            initializePlayer(mItem.getVideoURL());
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }




    private void initializePlayer(String videoUrl) {
        if (mExoplayer == null) {
            // 1. Create a default TrackSelector
            LoadControl loadControl = new DefaultLoadControl();
//            LoadControl loadControl = new DefaultLoadControl(
//                    new DefaultAllocator(true, 16),
//                    VideoPlayerConfig.MIN_BUFFER_DURATION,
//                    VideoPlayerConfig.MAX_BUFFER_DURATION,
//                    VideoPlayerConfig.MIN_PLAYBACK_START_BUFFER,
//                    VideoPlayerConfig.MIN_PLAYBACK_RESUME_BUFFER, -1, true);
            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            TrackSelection.Factory videoTrackSelectionFactory =
                    new AdaptiveTrackSelection.Factory(bandwidthMeter);
            TrackSelector trackSelector =
                    new DefaultTrackSelector(videoTrackSelectionFactory);
            // 2. Create the player
            mExoplayer = ExoPlayerFactory.newSimpleInstance(getContext(),new DefaultRenderersFactory(getContext())
                    , trackSelector, loadControl);
            mPlayerView.setPlayer(mExoplayer);
            buildMediaSource(Uri.parse(videoUrl));
            mExoplayer.setPlayWhenReady(true);

            boolean haveResumePosition = resumeWindow != C.INDEX_UNSET;
            if (haveResumePosition) {
                mExoplayer.seekTo(resumeWindow, resumePosition);

            }

        }
    }
    private void buildMediaSource( Uri mUri) {

        if(mUri!=null){
            mPlayerView.setVisibility(View.VISIBLE);
            // Measures bandwidth during playback. Can be null if not required.
            DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            // Produces DataSource instances through which media data is loaded.
            DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(getContext(),
                    Util.getUserAgent(getActivity(), getString(R.string.app_name)), bandwidthMeter);
            // This is the MediaSource representing the media to be played.
            MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(mUri);
            // Prepare the player with the source.
            mExoplayer.prepare(videoSource);


        }

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.step_detail, container, false);
        appBarLayout = getActivity().findViewById(R.id.toolbar_layout);
        // Show the dummy content as text in a TextView.
        ButterKnife.bind(this, rootView);

        mNextStepTV.setOnClickListener(this);
        mPrevStepTV.setOnClickListener(this);

        return rootView;
    }


    void setupView() {
        if (mItem != null) {
            if (appBarLayout != null) {
                appBarLayout.setTitle(mItem.getShortDescription());
            }
            initializePlayer(mItem.getVideoURL());

            mStepDetails.setText(mItem.getDescription());
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupView();

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

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if ((playbackState == ExoPlayer.STATE_READY) && playWhenReady&&stateBuilder!=null) {
            stateBuilder.setState(PlaybackStateCompat.STATE_PLAYING, mExoplayer.getCurrentPosition(), 1f);
        } else if (stateBuilder!=null && (playbackState == ExoPlayer.STATE_READY)) {
            stateBuilder.setState(PlaybackStateCompat.STATE_PAUSED, mExoplayer.getCurrentPosition(), 1f);
        }
        if (mediaSession != null)
            mediaSession.setPlaybackState(stateBuilder.build());
    }
}
