package com.bakingapp.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bakingapp.R;
import com.bakingapp.models.Step;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;


public class RecipeStepsAdapter extends RecyclerView.Adapter<RecipeStepsAdapter.RecipeStepViewHolder> {

    private final Context mContext;
    private final Step[] mStepList;

    private OnItemClickListener mListener;
    private int mSelectedItemPosition = -1;

    public RecipeStepsAdapter(Context context, Step[] stepList) {

        mContext = context;
        mStepList = stepList;
    }

    public void setOnStepClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    @Override
    public RecipeStepViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.recipe_step_list_item, parent, false);
        return new RecipeStepViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecipeStepViewHolder holder, int position) {
        holder.textViewStepShortDescription.setText(mStepList[position].getShortDescription());
        if(mStepList[position].getThumbnailURL() != null && !mStepList[position].getThumbnailURL().isEmpty()) {
            Picasso.with(mContext)
                    .load(mStepList[position].getThumbnailURL())
                    .error(R.drawable.ic_done_black_24dp)
                    .into(holder.imgThumb);
        }

        if(position == mSelectedItemPosition) {
            holder.StepCard.setCardBackgroundColor(mContext.getResources().getColor(R.color.colorPrimaryLight));
        } else {
            holder.StepCard.setCardBackgroundColor(mContext.getResources().getColor(R.color.colorCard));
        }
    }

    @Override
    public int getItemCount() {
        return mStepList.length;
    }

    public class RecipeStepViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.step_card)
        CardView StepCard;
        @BindView(R.id.step_short_description)
        TextView textViewStepShortDescription;
        @BindView(R.id.step_thumb)
        ImageView imgThumb;

        public RecipeStepViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            StepCard.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if(mListener != null) {
                mListener.onRecipeStepClicked(getAdapterPosition());
                mSelectedItemPosition = getAdapterPosition();
                Log.d("mSelectedItemPosition", String.valueOf(mSelectedItemPosition));
                notifyDataSetChanged();
            }
        }
    }

    public interface OnItemClickListener {
        void onRecipeStepClicked(int position);
    }
}