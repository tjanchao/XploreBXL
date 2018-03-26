package be.ehb.xplorebxl.Utils.Adapter;

import android.Manifest;
import android.app.Activity;
import android.content.ContextWrapper;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

import be.ehb.xplorebxl.Database.LandMarksDatabase;
import be.ehb.xplorebxl.Model.StreetArt;
import be.ehb.xplorebxl.R;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Q on 19-3-2018.
 */

public class StreetArtListAdapter extends BaseAdapter {

    private class ViewHolder{
        TextView tvStreetartArtistName;
        TextView tvStreetartAddress;
        TextView tvDistance;
        ImageView ivStreetartPhoto;
    }
    private Activity context;
    private List<StreetArt> items;
    private Location location;


    public StreetArtListAdapter(Activity context, LocationManager lm) {
        this.context = context;
        if (lm != null && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            if(location == null){
                location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }

        }
        items = LandMarksDatabase.getInstance(context).getSortedStreetArt(location);
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

        ViewHolder mViewHolder;

        if (view == null){
            view = context.getLayoutInflater().inflate(R.layout.fragment_street_art_detail,viewGroup,false);
            mViewHolder = new ViewHolder();

            mViewHolder.tvStreetartArtistName = view.findViewById(R.id.tv_detail_streetart_artistname);
            mViewHolder.tvStreetartAddress = view.findViewById(R.id.tv_detail_streetart_explanation);
            mViewHolder.ivStreetartPhoto = view.findViewById(R.id.iv_detail_streetart);
            mViewHolder.tvDistance = view.findViewById(R.id.tv_detail_streetart_distance);


            view.setTag(mViewHolder);

        }else {
            mViewHolder = (ViewHolder) view.getTag();
        }

        StreetArt currentStreetArt = items.get(i);

        mViewHolder.tvStreetartArtistName.setText(currentStreetArt.getNameOfArtist());
        mViewHolder.tvStreetartAddress.setText(currentStreetArt.getAddress());

        if (location != null){
            Location loc_streetart = new Location("location");
            loc_streetart.setLatitude(currentStreetArt.getCoordX());
            loc_streetart.setLongitude(currentStreetArt.getCoordY());
            float distance_streetart = location.distanceTo(loc_streetart);

            distance_streetart = distance_streetart/1000;
            mViewHolder.tvDistance.setText(String.format("%.2f km", distance_streetart));

        }else {
            mViewHolder.tvDistance.setVisibility(View.GONE);
        }

        if(currentStreetArt.isHasIMG()) {

            String imgId = currentStreetArt.getImgUrl()
                    .split("files/")[1]
                    .split("[/]")[0];

            ContextWrapper cw = new ContextWrapper(context);
            File directory = cw.getDir("images", MODE_PRIVATE);
            File file = new File(directory, imgId +".jpeg");
            Picasso.with(context)
                    .load(file)
                    .into(mViewHolder.ivStreetartPhoto);
        }else {
            //placeholder
          //  mViewHolder.ivStreetartPhoto.setVisibility(View.INVISIBLE);
        }


        return view;
    }


}

