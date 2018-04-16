package com.bakingapp;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.support.v4.app.TaskStackBuilder;

import com.bakingapp.models.Ingredient;
import com.bakingapp.models.Recipe;


public class IngredientsWidgetProvider extends AppWidgetProvider {

    public static Recipe latestOpenedRecipe;

    private static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                        int appWidgetId) {

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.ingredients_widget_provider);

        if(latestOpenedRecipe != null) {

            StringBuilder sIngredients = new StringBuilder();
            for (Ingredient ing : latestOpenedRecipe.getIngredients()) {
                sIngredients.append(ing.getIngredient() + " (" + ing.getQuantity() + " " +
                        ing.getMeasure() + ")");
                sIngredients.append("\n");
            }


            views.setTextViewText(R.id.recipe_name, latestOpenedRecipe.getName());
            views.setTextViewText(R.id.recipe_ingredients, sIngredients);

            Intent intent = new Intent(context, RecipeDetailsActivity.class);
            intent.putExtra(RecipeDetailsActivity.EXTRA_RECIPE, latestOpenedRecipe);
            PendingIntent pendingIntent = TaskStackBuilder.create(context)
                    .addNextIntentWithParentStack(intent)
                    .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

            views.setOnClickPendingIntent(R.id.root_layout, pendingIntent);
        }

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    public static void updateIngredientsWidget(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
    }

    @Override
    public void onDisabled(Context context) {
    }
}
