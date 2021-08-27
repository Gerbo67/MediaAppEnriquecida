package utng.edu.mx.mediaappenriquecida;

import androidx.leanback.widget.AbstractDetailsDescriptionPresenter;

public class DetailsDescriptionPresenter extends AbstractDetailsDescriptionPresenter {
    @Override
    protected void onBindDescription(ViewHolder viewHolder, Object item) {
        utng.edu.mx.mediaappenriquecida.Video video = (utng.edu.mx.mediaappenriquecida.Video) item;

        if (video != null) {
            viewHolder.getTitle().setText(video.getTitle());
            viewHolder.getSubtitle().setText(video.getCategory());
            viewHolder.getBody().setText(video.getDescription());
        }
    }
}
