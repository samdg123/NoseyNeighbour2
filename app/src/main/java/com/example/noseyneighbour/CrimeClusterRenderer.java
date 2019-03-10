package com.example.noseyneighbour;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.support.annotation.ColorInt;

import com.example.noseyneighbour.Classes.Crime;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
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

    @Override
    protected void onBeforeClusterItemRendered(Crime item, MarkerOptions markerOptions) {
        super.onBeforeClusterItemRendered(item, markerOptions);
        Drawable icon = context.getResources().getDrawable(R.drawable.ic_outline_person_pin_circle_24px, context.getTheme());
        //icon.setColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
        icon.setTint(Color.GREEN);

        IconGenerator iconGenerator = new IconGenerator(context);
        iconGenerator.setBackground(icon);
        Bitmap bitmap = iconGenerator.makeIcon();

        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(bitmap));
    }
}
