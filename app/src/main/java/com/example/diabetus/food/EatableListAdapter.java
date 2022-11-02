package com.example.diabetus.food;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.diabetus.R;
import com.example.diabetus.RecyclerViewClickListener;

import java.util.List;

public class EatableListAdapter extends RecyclerView.Adapter<EatableListAdapter.ViewHolder> {

    private List<? extends Eatable> mValues;
    private static RecyclerViewClickListener itemListener;


    public EatableListAdapter(List<? extends Eatable> items, RecyclerViewClickListener listener) {
        mValues = items;
        itemListener = listener;
    }

    @NonNull
    @Override
    public EatableListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.eatable_row, parent, false);
        return new EatableListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final EatableListAdapter.ViewHolder holder, int position) {
        holder.eatable_Name.setText(mValues.get(position).getPresentableName());
        holder.eatable_Calorie.setText(String.format("%.1f", mValues.get(position).getCalorie()));
        holder.eatable_Carb.setText(String.format("%.1f", mValues.get(position).getCarb()));
        holder.eatable_Protein.setText(String.format("%.1f", mValues.get(position).getProtein()));
        holder.eatable_Fat.setText(String.format("%.1f", mValues.get(position).getFat()));

        if (mValues.get(position).getType() == Eatable.EATABLE_FOOD) {
            String text = String.format("%.1f", ((Food)mValues.get(position)).getQuantity());
            if (((Food)mValues.get(position)).getQuantityType() == Food.QUANTITY_100G) {
                text += " g";
            }
            else {
                text += " piece";
            }
            holder.eatable_Quantity.setText(text);
            holder.eatable_Icon.setImageResource(R.drawable.ic_meat_svgrepo_com);
        }
        else if (mValues.get(position).getType() == Eatable.EATABLE_MEAL) {
            holder.eatable_Quantity.setVisibility(View.GONE);
            holder.eatable_Icon.setImageResource(R.drawable.ic_meal);
        } else {
            holder.eatable_Quantity.setVisibility(View.GONE);
            holder.eatable_Icon.setImageResource(R.drawable.ic_meat_svgrepo_com);
        }

        holder.ibtn_DeleteFood.setVisibility(View.GONE);
    }

    public List<? extends Eatable> getAllItem() {
        return mValues;
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public Eatable getItem(int position) {
        return mValues.get(position);
    }

    public void Update(List<? extends Eatable> filteredList) {
        mValues = filteredList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView eatable_Name, eatable_Calorie, eatable_Carb, eatable_Protein,
                eatable_Fat, eatable_Quantity;
        private final ImageButton ibtn_DeleteFood;
        private final ImageView eatable_Icon;

        public ViewHolder(View itemView) {
            super(itemView);
            eatable_Name = itemView.findViewById(R.id.eatable_Name);
            eatable_Calorie = itemView.findViewById(R.id.eatable_Calorie);
            eatable_Carb = itemView.findViewById(R.id.eatable_Carb);
            eatable_Protein = itemView.findViewById(R.id.eatable_Protein);
            eatable_Fat = itemView.findViewById(R.id.eatable_Fat);
            ibtn_DeleteFood = itemView.findViewById(R.id.ibtn_DeleteFood);
            eatable_Quantity = itemView.findViewById(R.id.eatable_Quantity);
            eatable_Icon = itemView.findViewById(R.id.eatable_Icon);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            itemListener.recyclerViewListClicked(v, this.getLayoutPosition());
        }

        @Override
        public String toString() {
            return super.toString() + mValues.toString();
        }
    }
}
