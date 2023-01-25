package com.example.diabetus.database.food;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.diabetus.R;
import com.example.diabetus.common.RecyclerViewClickListener;

import java.util.List;
import java.util.Locale;

public class FoodListAdapter extends RecyclerView.Adapter<FoodListAdapter.ViewHolder> {

    private final List<Food> mValues;
    private static RecyclerViewClickListener itemListener;


    public FoodListAdapter(List<Food> items, RecyclerViewClickListener listener) {
        mValues = items;
        itemListener = listener;
    }

    @NonNull
    @Override
    public FoodListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.eatable_row, parent, false);
        return new FoodListAdapter.ViewHolder(view);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onBindViewHolder(final FoodListAdapter.ViewHolder holder, int position) {
        holder.eatable_Name.setText(mValues.get(position).getPresentableName());
        holder.eatable_Calorie.setText(String.format(Locale.getDefault(), "%.1f", mValues.get(position).getCalorie()));
        holder.eatable_Carb.setText(String.format(Locale.getDefault(), "%.1f", mValues.get(position).getCarb()));
        holder.eatable_Protein.setText(String.format(Locale.getDefault(), "%.1f", mValues.get(position).getProtein()));
        holder.eatable_Fat.setText(String.format(Locale.getDefault(), "%.1f", mValues.get(position).getFat()));
        String text = String.format(Locale.getDefault(), "%.1f", mValues.get(position).getQuantity());

        if ((mValues.get(position)).getQuantityType() == Food.QUANTITY_100G) {
            text += " g";
        }
        else {
            text += " piece";
        }
        holder.eatable_Quantity.setText(text);
            holder.ibtn_DeleteFood.setOnClickListener(v -> {
                mValues.remove(position);
                notifyDataSetChanged();
            });
    }

    public List<Food> getAllItem() {
        return mValues;
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public Food getItem(int position) {
        return mValues.get(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView eatable_Name, eatable_Calorie, eatable_Carb, eatable_Protein,
                eatable_Fat, eatable_Quantity;
        private final ImageButton ibtn_DeleteFood;

        public ViewHolder(View itemView) {
            super(itemView);
            eatable_Name = itemView.findViewById(R.id.eatable_Name);
            eatable_Calorie = itemView.findViewById(R.id.eatable_Calorie);
            eatable_Carb = itemView.findViewById(R.id.eatable_Carb);
            eatable_Protein = itemView.findViewById(R.id.eatable_Protein);
            eatable_Fat = itemView.findViewById(R.id.eatable_Fat);
            ibtn_DeleteFood = itemView.findViewById(R.id.ibtn_DeleteFood);
            eatable_Quantity = itemView.findViewById(R.id.eatable_Quantity);
            ibtn_DeleteFood.setOnClickListener(this);
            itemView.setOnClickListener(this);

            ImageButton ibtn_InspectFood = itemView.findViewById(R.id.ibtn_InspectFood);
            ibtn_InspectFood.setVisibility(View.GONE);
            ImageButton ibtn_AddToDatabase = itemView.findViewById(R.id.ibtn_AddToDatabase);
            ibtn_AddToDatabase.setVisibility(View.GONE);
        }

        @Override
        public void onClick(View v) {
            itemListener.recyclerViewListClicked(v, this.getLayoutPosition());
        }

        @NonNull
        @Override
        public String toString() {
            return super.toString() + mValues.toString();
        }
    }
}
