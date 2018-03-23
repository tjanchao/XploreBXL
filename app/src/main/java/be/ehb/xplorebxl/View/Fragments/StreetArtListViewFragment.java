package be.ehb.xplorebxl.View.Fragments;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import be.ehb.xplorebxl.Model.StreetArt;
import be.ehb.xplorebxl.R;
import be.ehb.xplorebxl.Utils.StreetArtListAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class StreetArtListViewFragment extends Fragment {

    private ListView lvStreetart;

    private ListviewItemListener callback;


    public StreetArtListViewFragment() {
        // Required empty public constructor
    }

    public static StreetArtListViewFragment newInstance (){
        StreetArtListViewFragment fragment = new StreetArtListViewFragment();
        return fragment;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        callback = (ListviewItemListener) context;
    }

    //voor oudere android versies
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        callback = (ListviewItemListener) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_street_art_list_view, container, false);

        lvStreetart = rootView.findViewById(R.id.lv_streetart);

        final StreetArtListAdapter streetArtListAdapter = new StreetArtListAdapter(getActivity());

        lvStreetart.setAdapter(streetArtListAdapter);

        lvStreetart.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                StreetArt selectedStreetart = (StreetArt) streetArtListAdapter.getItem(i);
                callback.itemSelected(selectedStreetart);
                getActivity().onBackPressed();

            }
        });


        return rootView ;
    }

}
