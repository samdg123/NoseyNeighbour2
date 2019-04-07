package com.example.noseyneighbour;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import com.example.noseyneighbour.Classes.Crime;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.google.maps.android.ui.IconGenerator;

public class CrimeClusterRenderer extends DefaultClusterRenderer<Crime> {

    private Context context;

    public CrimeClusterRenderer(Context context, GoogleMap map, ClusterManager<Crime> clusterManager) {
        super(context, map, clusterManager);
        this.context = context;
    }

    //sets marker icon based on crime category
    @Override
    protected void onBeforeClusterItemRendered(Crime item, MarkerOptions markerOptions) {
        super.onBeforeClusterItemRendered(item, markerOptions);

        Drawable icon = context.getDrawable(R.drawable.ic_pin_red);

        switch (item.getCategory()) {
            //case "anti-social-behaviour":
                //icon = context.getDrawable(R.drawable.ic_pin_red);
                //break;
            case "bicycle-theft":
                icon = context.getDrawable(R.drawable.ic_pin_blue);
                break;
            case "burglary":
                icon = context.getDrawable(R.drawable.ic_pin_cyan);
                break;
            case "criminal-damage-arson":
                icon = context.getDrawable(R.drawable.ic_pin_dk_blue);
                break;
            case "drugs":
                icon = context.getDrawable(R.drawable.ic_pin_dk_green);
                break;
            case "other-theft":
                icon = context.getDrawable(R.drawable.ic_pin_dk_orange);
                break;
            case "possession-of-weapons":
                icon = context.getDrawable(R.drawable.ic_pin_green);
                break;
            case "public-order":
                icon = context.getDrawable(R.drawable.ic_pin_orange);
                break;
            case "robbery":
                icon = context.getDrawable(R.drawable.ic_pin_violet);
                break;
            case "violent-crime":
                icon = context.getDrawable(R.drawable.ic_pin_yellow);
                break;
        }

        //sets drawable to bitmap icon
        IconGenerator iconGenerator = new IconGenerator(context);
        iconGenerator.setBackground(icon);
        Bitmap bitmap = iconGenerator.makeIcon();

        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(bitmap));
    }
}
