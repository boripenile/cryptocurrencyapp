package com.youngprime.cryptocurrency;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.youngprime.cryptocurrency.model.CountryCurrency;

import java.util.List;

/**
 * Created by bd_dev_murtala on 20/10/2017.
 */

public class CurrencyListAdapter extends ArrayAdapter<CountryCurrency> {

    private static final String TAG = "CurrencyListAdapter";

    private Context mContext;

    private int mResource;

    private int lastPosition = -1;
    /**
     * Holds variables in a View
     */
    private static class ViewHolder {
        TextView currencyName;
        TextView currencySymbol;
        ImageView flag;
    }

    public CurrencyListAdapter(Context context, int resource, List<CountryCurrency> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.mResource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //sets up the image loader library
        setupImageLoader();

        String currencyName = getItem(position).getCurrencyName();
        String currencySymbol = getItem(position).getSymbol();
        String flagUrl = getItem(position).getFlagUrl();

        //create the view result for showing the animation
        final View result;

        //ViewHolder object
        ViewHolder holder;

        if(convertView == null){
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(mResource, parent, false);
            holder= new ViewHolder();
            holder.currencyName = (TextView) convertView.findViewById(R.id.txtCurrencyName);
            holder.currencySymbol = (TextView) convertView.findViewById(R.id.txtCurrencySymbol);
            holder.flag = (ImageView) convertView.findViewById(R.id.countryImage);

            result = convertView;

            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder) convertView.getTag();
            result = convertView;
        }


        Animation animation = AnimationUtils.loadAnimation(mContext,
                (position > lastPosition) ? R.anim.load_down_anim : R.anim.load_up_anim);
        result.startAnimation(animation);
        lastPosition = position;

        holder.currencyName.setText(currencyName);
        holder.currencySymbol.setText(currencySymbol);

        //create the imageloader object
        ImageLoader imageLoader = ImageLoader.getInstance();

        int defaultImage = mContext.getResources()
                .getIdentifier("@drawable/ic_image_failed",null,mContext.getPackageName());

        //create display options
        DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true)
                .cacheOnDisc(true).resetViewBeforeLoading(true)
                .showImageForEmptyUri(defaultImage)
                .showImageOnFail(defaultImage)
                .showImageOnLoading(defaultImage).build();

        Log.d(TAG, "getView: FlagUrl = " + currencyName + " Url = " + flagUrl);
        //download and display image from url
        imageLoader.displayImage(flagUrl, holder.flag, options);

        Log.d(TAG, "Second getView: FlagUrl = " + currencyName + " Url = " + flagUrl);
        return convertView;
    }

    /**
     * Required for setting up the Universal Image loader Library
     */
    private void setupImageLoader(){
        // UNIVERSAL IMAGE LOADER SETUP
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheOnDisc(true).cacheInMemory(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .displayer(new FadeInBitmapDisplayer(300)).build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                mContext)
                .defaultDisplayImageOptions(defaultOptions)
                .memoryCache(new WeakMemoryCache())
                .discCacheSize(100 * 1024 * 1024).build();

        ImageLoader.getInstance().init(config);
        // END - UNIVERSAL IMAGE LOADER SETUP
    }
}
