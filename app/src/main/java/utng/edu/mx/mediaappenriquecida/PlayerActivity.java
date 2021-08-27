package utng.edu.mx.mediaappenriquecida;

import android.app.Activity;
import android.os.Bundle;
import android.widget.VideoView;

public class PlayerActivity extends Activity implements utng.edu.mx.mediaappenriquecida.PlayerControlsFragment.PlayerControlsListener {
    private VideoView mVideoView;
    private utng.edu.mx.mediaappenriquecida.Video mVideo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        initViews();
        initVideoPlayer();
    }

    private void initViews() {
        mVideoView = (VideoView) findViewById( R.id.video_view );
    }

    private void initVideoPlayer() {
        mVideo = (utng.edu.mx.mediaappenriquecida.Video) getIntent().getSerializableExtra( VideoDetailsFragment.EXTRA_VIDEO );
        mVideoView.setVideoPath( mVideo.getVideoUrl() );
    }

    @Override
    public void play() {
        mVideoView.start();
    }

    @Override
    public void pause() {
        mVideoView.pause();
    }
}
