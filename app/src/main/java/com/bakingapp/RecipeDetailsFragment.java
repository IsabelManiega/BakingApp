package com.bakingapp;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bakingapp.adapters.RecipeStepsAdapter;
import com.bakingapp.models.Ingredient;
import com.bakingapp.models.Recipe;

import butterknife.BindView;
import butterknife.ButterKnife;


public class RecipeDetailsFragment extends Fragment implements RecipeStepsAdapter.OnItemClickListener {

    @BindView(R.id.ingredients)
    TextView textViewIngredients;
    @BindView(R.id.steps_recyclerview)
    RecyclerView Steps;

    private static final String RECIPE = "recipe";

    private Recipe mRecipe;

    private OnRecipeDetailInteractionListener mListener;

    public RecipeDetailsFragment() {
    }

    public static RecipeDetailsFragment newInstance(Recipe recipe) {
        RecipeDetailsFragment fragment = new RecipeDetailsFragment();
        Bundle args = new Bundle();
        args.putParcelable(RECIPE, recipe);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mRecipe = getArguments().getParcelable(RECIPE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_recipe_details, container, false);
        ButterKnife.bind(this, rootView);

        // Ingredients
        StringBuilder sIngredients = new StringBuilder();
        for (Ingredient ing : mRecipe.getIngredients()) {
            sIngredients.append(ing.getIngredient() + " (" + ing.getQuantity() + " " +
                    ing.getMeasure() + ")");
            sIngredients.append("\n");
        }
        textViewIngredients.setText(sIngredients);

        // Steps
        RecipeStepsAdapter recipeStepsAdapter = new RecipeStepsAdapter(getActivity(), mRecipe.getSteps());
        recipeStepsAdapter.setOnStepClickListener(this);
        Steps.setAdapter(recipeStepsAdapter);
        Steps.setLayoutManager(new LinearLayoutManager(getActivity()));
        Steps.setNestedScrollingEnabled(false);

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getActivity());
        int[] appWidgetIDs = appWidgetManager.getAppWidgetIds(new ComponentName(getActivity(), IngredientsWidgetProvider.class));
        IngredientsWidgetProvider.latestOpenedRecipe = mRecipe;
        IngredientsWidgetProvider.updateIngredientsWidget(getActivity(), appWidgetManager, appWidgetIDs);

        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnRecipeDetailInteractionListener) {
            mListener = (OnRecipeDetailInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnRecipeDetailInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onRecipeStepClicked(int position) {
        if (mListener != null) {
            mListener.onStepClicked(position);
        }
    }

    public interface OnRecipeDetailInteractionListener {
        void onStepClicked(int pos);
    }
}