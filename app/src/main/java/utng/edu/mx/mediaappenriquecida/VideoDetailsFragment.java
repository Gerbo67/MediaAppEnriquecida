package utng.edu.mx.mediaappenriquecida;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.Toast;

import androidx.leanback.app.DetailsFragment;
import androidx.leanback.widget.AbstractDetailsDescriptionPresenter;
import androidx.leanback.widget.Action;
import androidx.leanback.widget.ArrayObjectAdapter;
import androidx.leanback.widget.ClassPresenterSelector;
import androidx.leanback.widget.DetailsOverviewRow;
import androidx.leanback.widget.FullWidthDetailsOverviewRowPresenter;
import androidx.leanback.widget.HeaderItem;
import androidx.leanback.widget.ListRow;
import androidx.leanback.widget.ListRowPresenter;
import androidx.leanback.widget.OnActionClickedListener;
import androidx.leanback.widget.OnItemViewClickedListener;
import androidx.leanback.widget.Presenter;
import androidx.leanback.widget.Row;
import androidx.leanback.widget.RowPresenter;
import androidx.leanback.widget.SparseArrayObjectAdapter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class VideoDetailsFragment extends DetailsFragment
        implements OnItemViewClickedListener, OnActionClickedListener {

    public static final String EXTRA_VIDEO = "extra_video";

    public static final long ACTION_WATCH = 1;

    private utng.edu.mx.mediaappenriquecida.Video mVideo;

    private DetailsOverviewRow mRow;

    private Target target = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            mRow.setImageBitmap(getActivity(), bitmap);
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {
        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mVideo = (utng.edu.mx.mediaappenriquecida.Video) getActivity().getIntent().getSerializableExtra( EXTRA_VIDEO );

        mRow = new DetailsOverviewRow( mVideo );

        initActions();

        ClassPresenterSelector presenterSelector = createDetailsPresenter();
        ArrayObjectAdapter adapter = new ArrayObjectAdapter( presenterSelector );
        adapter.add(mRow);

        loadRelatedMedia(adapter);
        setAdapter(adapter);

        Picasso.with(getActivity()).load(mVideo.getPoster()).resize(274, 274).into(target);
        setOnItemViewClickedListener(this);
    }

    private ClassPresenterSelector createDetailsPresenter() {
        ClassPresenterSelector presenterSelector = new ClassPresenterSelector();
        FullWidthDetailsOverviewRowPresenter presenter =
                new FullWidthDetailsOverviewRowPresenter(new DetailsDescriptionPresenter() {
                });

        presenter.setOnActionClickedListener( this );

        presenterSelector.addClassPresenter( DetailsOverviewRow.class, presenter );
        presenterSelector.addClassPresenter( ListRow.class, new ListRowPresenter() );

        return presenterSelector;
    }

    public class DetailsDescriptionPresenter extends
            AbstractDetailsDescriptionPresenter {
        @Override
        protected void onBindDescription(
                ViewHolder viewHolder, Object item) {
            utng.edu.mx.mediaappenriquecida.Video video = (utng.edu.mx.mediaappenriquecida.Video) item;
            if (video != null) {
                viewHolder.getTitle().setText(video.getTitle());
                viewHolder.getSubtitle().setText(video.getCategory());
                viewHolder.getBody().setText(video.getDescription());
            }
        }
    }

    private void initActions() {
        //addAction has been deprecated
        mRow.setActionsAdapter(new SparseArrayObjectAdapter() {
            @Override
            public int size() {
                return 3;
            }

            @Override
            public Object get(int position) {
                if(position == 0) {
                    return new Action(ACTION_WATCH, "Watch", "");
                } else if( position == 1 ) {
                    return new Action( 42, "Rent", "Line 2" );
                } else if( position == 2 ) {
                    return new Action( 42, "Preview", "" );
                }

                else return null;
            }
        });
    }

    private void loadRelatedMedia( ArrayObjectAdapter adapter ) {

        String json = utng.edu.mx.mediaappenriquecida.Utils.loadJSONFromResource( getActivity(), R.raw.videos );
        Gson gson = new Gson();
        Type collection = new TypeToken<ArrayList<utng.edu.mx.mediaappenriquecida.Video>>(){}.getType();
        List<utng.edu.mx.mediaappenriquecida.Video> videos = gson.fromJson( json, collection );
        if( videos == null )
            return;

        ArrayObjectAdapter listRowAdapter = new ArrayObjectAdapter( new CardPresenter() );

        for( utng.edu.mx.mediaappenriquecida.Video video : videos ) {
            if( video.getCategory().equals( mVideo.getCategory() ) && !video.getTitle().equals( mVideo.getTitle() ) ) {
                listRowAdapter.add( video );
            }
        }

        HeaderItem header = new HeaderItem( 0, "Related" );
        adapter.add(new ListRow(header, listRowAdapter));
    }

    @Override
    public void onItemClicked(Presenter.ViewHolder itemViewHolder, Object item, RowPresenter.ViewHolder mRowViewHolder, Row mRow) {
        if( item instanceof utng.edu.mx.mediaappenriquecida.Video) {
            utng.edu.mx.mediaappenriquecida.Video video = (utng.edu.mx.mediaappenriquecida.Video) item;
            Intent intent = new Intent( getActivity(), VideoDetailsActivity.class );
            intent.putExtra( utng.edu.mx.mediaappenriquecida.VideoDetailsFragment.EXTRA_VIDEO, video );
            startActivity( intent );
        }
    }

    @Override
    public void onActionClicked(Action action) {
        if( action.getId() == ACTION_WATCH ) {
            Intent intent = new Intent(getActivity(), utng.edu.mx.mediaappenriquecida.PlayerActivity.class);
            intent.putExtra(utng.edu.mx.mediaappenriquecida.VideoDetailsFragment.EXTRA_VIDEO, mVideo);
            startActivity(intent);
        } else {
            Toast.makeText(getActivity(), "Action", Toast.LENGTH_SHORT).show();
        }
    }
}