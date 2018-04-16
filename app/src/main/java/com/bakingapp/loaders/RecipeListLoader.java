package com.bakingapp.loaders;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.bakingapp.utils.NetworkUtils;



public class RecipeListLoader extends AsyncTaskLoader<String> {
    private final String mUrl;

    public RecipeListLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    public String loadInBackground() {
        return new NetworkUtils().sendHTTPRequest(mUrl);
    }

    @Override
    public void deliverResult(String data) {
        super.deliverResult(data);
    }
}