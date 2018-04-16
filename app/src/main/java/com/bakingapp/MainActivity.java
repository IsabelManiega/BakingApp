package com.bakingapp;


import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.bakingapp.adapters.RecipesAdapter;
import com.bakingapp.loaders.RecipeListLoader;
import com.bakingapp.models.Recipe;
import com.bakingapp.utils.JSONUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<String>,
        RecipesAdapter.ItemClickListener {

    @BindView(R.id.root_layout)
    ConstraintLayout rootLayout;
    @BindView(R.id.loading)
    TextView textviewLoading;
    @BindView(R.id.recipes_recyclerview)
    RecyclerView Recipes;
    @BindView(R.id.my_toolbar)
    Toolbar toolbar;

    private ActionBar bar;
    private final String TAG = "MainActivity";
    private static final int RECIPES_LOADER_ID = 1;
    private List<Recipe> mRecipes;
    public static final String RECIPES_URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        bar = getSupportActionBar();
    }


    @Override
    protected void onResume() {
        super.onResume();
        loadRecipes();
    }


    private void loadRecipes() {
        getSupportLoaderManager().restartLoader(RECIPES_LOADER_ID, null, this).forceLoad();
    }

    @Override
    public Loader<String> onCreateLoader(int id, Bundle args) {
        return new RecipeListLoader(this, RECIPES_URL);
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {
        Log.d(TAG, "RESULT: " + data);

        if(data.isEmpty()) {
            Snackbar.make(rootLayout, R.string.no_internet, Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.retry, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            loadRecipes();
                        }
                    }).show();
            return;
        }

        textviewLoading.setVisibility(View.GONE);

        mRecipes = JSONUtils.parseRecipesJson(data);
        RecipesAdapter adapter = new RecipesAdapter(this, mRecipes);
        adapter.setRecipeClickListener(this);
        Recipes.setAdapter(adapter);

        if(getResources().getBoolean(R.bool.isTablet))
            Recipes.setLayoutManager(new GridLayoutManager(this, 3));
        else
            Recipes.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {

    }

    @Override
    public void onRecipeClick(View view, int pos) {
        Log.d(TAG, "recipe clicked: " + pos);

        Intent intent = new Intent(this, RecipeDetailsActivity.class);
        intent.putExtra(RecipeDetailsActivity.EXTRA_RECIPE, mRecipes.get(pos));
        startActivity(intent);
    }
}