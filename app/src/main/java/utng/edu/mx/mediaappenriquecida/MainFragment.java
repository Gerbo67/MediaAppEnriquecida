package utng.edu.mx.mediaappenriquecida;

import android.content.Intent;
import android.os.Bundle;

import androidx.leanback.app.BrowseFragment;
import androidx.leanback.widget.ArrayObjectAdapter;
import androidx.leanback.widget.HeaderItem;
import androidx.leanback.widget.ListRow;
import androidx.leanback.widget.ListRowPresenter;
import androidx.leanback.widget.OnItemViewClickedListener;
import androidx.leanback.widget.Presenter;
import androidx.leanback.widget.Row;
import androidx.leanback.widget.RowPresenter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MainFragment extends BrowseFragment implements OnItemViewClickedListener {
    private List<utng.edu.mx.mediaappenriquecida.Video> mVideos = new ArrayList<>();

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loadData();
        loadRows();
        setOnItemViewClickedListener( this );
    }

    private void loadData() {
        String json = utng.edu.mx.mediaappenriquecida.Utils.loadJSONFromResource( getActivity(), R.raw.videos );
        Type collection = new TypeToken<ArrayList<utng.edu.mx.mediaappenriquecida.Video>>(){}.getType();
        Gson gson = new Gson();
        mVideos = gson.fromJson( json, collection );
        setTitle("UTNG Movies Player");
        setHeadersState( HEADERS_ENABLED );
        setHeadersTransitionOnBackEnabled( true );
    }

    private void loadRows() {
        ArrayObjectAdapter adapter = new ArrayObjectAdapter( new ListRowPresenter() );
        CardPresenter presenter = new CardPresenter();

        List<String> categories = getCategories();

        if( categories == null || categories.isEmpty() )
            return;

        for( String category : categories ) {
            ArrayObjectAdapter listRowAdapter = new ArrayObjectAdapter( presenter );
            for( utng.edu.mx.mediaappenriquecida.Video movie : mVideos ) {
                if( category.equalsIgnoreCase( movie.getCategory() ) )
                    listRowAdapter.add( movie );
            }
            if( listRowAdapter.size() > 0 ) {
                HeaderItem header = new HeaderItem( adapter.size() - 1, category );
                adapter.add( new ListRow( header, listRowAdapter ) );
            }
        }
        setAdapter(adapter);
    }

    private List<String> getCategories() {
        if( mVideos == null )
            return null;

        List<String> categories = new ArrayList<String>();
        for( utng.edu.mx.mediaappenriquecida.Video movie : mVideos ) {
            if( !categories.contains( movie.getCategory() ) ) {
                categories.add( movie.getCategory() );
            }
        }

        return categories;
    }

    @Override
    public void onItemClicked(Presenter.ViewHolder itemViewHolder, Object item, RowPresenter.ViewHolder rowViewHolder, Row row) {
        if (item instanceof utng.edu.mx.mediaappenriquecida.Video) {
            utng.edu.mx.mediaappenriquecida.Video video = (utng.edu.mx.mediaappenriquecida.Video) item;
            Intent intent = new Intent(getActivity(), VideoDetailsActivity.class);
            intent.putExtra(utng.edu.mx.mediaappenriquecida.VideoDetailsFragment.EXTRA_VIDEO, video);
            startActivity(intent);
        }
    }
}
