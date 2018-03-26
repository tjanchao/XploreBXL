package be.ehb.xplorebxl.Utils.Adapter;

import android.Manifest;
import android.app.Activity;
import android.content.ContextWrapper;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

import be.ehb.xplorebxl.Database.LandMarksDatabase;
import be.ehb.xplorebxl.Model.Comic;
import be.ehb.xplorebxl.Model.StreetArt;
import be.ehb.xplorebxl.R;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Q on 21-3-2018.
 */

public class ComicListAdapter extends BaseAdapter {

    private class Viewholder {
        TextView tvArtistName, tvPersonnage, tvDistance;
        ImageView ivComicMuralPhoto;
    }

    private Activity context;
    private List<Comic> items;
    private Location location;



    public ComicListAdapter(Activity context, LocationManager lm) {
        this.context = context;
        if (lm != null && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            if(location == null){
                location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }

        }
        items = LandMarksDatabase.getInstance(context).getSortedCommic(location);
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int i) {
        return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        Viewholder mViewHolder;

        if (view == null){
            view = context.getLayoutInflater().inflate(R.layout.fragment_street_art_detail,viewGroup,false);
            mViewHolder = new Viewholder();

            mViewHolder.tvArtistName = view.findViewById(R.id.tv_detail_streetart_artistname);
            mViewHolder.tvPersonnage = view.findViewById(R.id.tv_detail_streetart_explanation);
            mViewHolder.ivComicMuralPhoto = view.findViewById(R.id.iv_detail_streetart);
            mViewHolder.tvDistance = view.findViewById(R.id.tv_detail_streetart_distance);

            view.setTag(mViewHolder);

        }else {
            mViewHolder = (Viewholder) view.getTag();
        }

        Comic currentComic = items.get(i);

        mViewHolder.tvArtistName.setText("Illustrator: " + currentComic.getNameOfIllustrator());
        mViewHolder.tvPersonnage.setText("Feat. " + currentComic.getPersonnage());

        if (location != null){
            Location loc_comic = new Location("location");
            loc_comic.setLatitude(currentComic.getCoordX());
            loc_comic.setLongitude(currentComic.getCoordY());
            float distance_comic = location.distanceTo(loc_comic);

            distance_comic = distance_comic/1000;
            mViewHolder.tvDistance.setText(String.format("%.2f km", distance_comic));

        }else {
            mViewHolder.tvDistance.setVisibility(View.GONE);
        }


        if(currentComic.isHasIMG()) {

            String imgId = currentComic.getImgUrl()
                    .split("files/")[1]
                    .split("[/]")[0];

            ContextWrapper cw = new ContextWrapper(context);
            File directory = cw.getDir("images", MODE_PRIVATE);
            File file = new File(directory, imgId +".jpeg");
            Picasso.with(context)
                    .load(file)
                    .into(mViewHolder.ivComicMuralPhoto);
        }else {
            mViewHolder.ivComicMuralPhoto.setVisibility(View.INVISIBLE);
        }

        return view;
    }
}
