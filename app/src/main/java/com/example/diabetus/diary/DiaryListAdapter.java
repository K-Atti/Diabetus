package com.example.diabetus.diary;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.diabetus.R;
import com.example.diabetus.RecyclerViewClickListener;

import java.util.List;

public class DiaryListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<DiaryListObject> mValues;
    private static RecyclerViewClickListener itemListener;
    private Context context;

    public DiaryListAdapter(List<DiaryListObject> items, RecyclerViewClickListener listener) {
        mValues = items;
        itemListener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        RecyclerView.ViewHolder viewHolder;

        View view;
        if (viewType == DiaryListObject.ENTRY_TYPE) {
            view = inflater.inflate(R.layout.diary_row, parent, false);
            viewHolder = new ViewHolder(view);
        }
        else {
            view = inflater.inflate(R.layout.diary_date_row, parent, false);
            viewHolder = new ViewDateHolder(view);
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case DiaryListObject.ENTRY_TYPE:
                ViewHolder viewHolder = (ViewHolder) holder;
                viewHolder.bind(mValues.get(position).getEntry());
                break;

            case DiaryListObject.DATE_TYPE:
                ViewDateHolder viewDateHolder = (ViewDateHolder) holder;
                viewDateHolder.bind(mValues.get(position).getEntry());
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mValues.get(position).getObjectType();
    }

    public DiaryEntry getItem(int position) {
        return mValues.get(position).getEntry();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView diary_calText, diary_fatText, diary_carbText, diary_protText,
                diary_basalText, diary_rapidText, diary_weightText;
        private final TextView diary_header, diary_bs, diary_cal, diary_carb, diary_protein, diary_fat,
                diary_rapidInsulin, diary_basalInsulin, diary_meal, diary_weight;
        private final ImageView diary_MealImage, diary_RapidImage, diary_BasalImage,
                diary_MealTextImage, diary_WeightImage;

        public ViewHolder(View itemView) {
            super(itemView);
            diary_header = itemView.findViewById(R.id.diary_header);
            diary_bs = itemView.findViewById(R.id.diary_bs);
            diary_cal = itemView.findViewById(R.id.diary_Calorie);
            diary_carb = itemView.findViewById(R.id.diary_Carb);
            diary_protein = itemView.findViewById(R.id.diary_Protein);
            diary_fat = itemView.findViewById(R.id.diary_Fat);
            diary_rapidInsulin = itemView.findViewById(R.id.diary_RapidInsulin);
            diary_basalInsulin = itemView.findViewById(R.id.diary_BasalInsulin);
            diary_meal = itemView.findViewById(R.id.diary_Meal);
            diary_MealImage = itemView.findViewById(R.id.diary_MealImage);
            diary_RapidImage = itemView.findViewById(R.id.diary_RapidImage);
            diary_BasalImage = itemView.findViewById(R.id.diary_BasalImage);
            diary_MealTextImage = itemView.findViewById(R.id.diary_MealTextImage);
            diary_calText = itemView.findViewById(R.id.diary_CalorieText);
            diary_fatText = itemView.findViewById(R.id.diary_FaText);
            diary_carbText = itemView.findViewById(R.id.diary_CarbText);
            diary_protText = itemView.findViewById(R.id.diary_ProteinText);
            diary_basalText = itemView.findViewById(R.id.diary_BasalInsulinText);
            diary_rapidText = itemView.findViewById(R.id.diary_RapidInsulinText);
            diary_weight = itemView.findViewById(R.id.diary_Weight);
            diary_weightText = itemView.findViewById(R.id.diary_WeightText);
            diary_WeightImage = itemView.findViewById(R.id.diary_WeightImage);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            itemListener.recyclerViewListClicked(v, this.getLayoutPosition());
        }

        @NonNull
        @Override
        public String toString() {
            return super.toString() + mValues.toString() ;
        }

        public void bind(final DiaryEntry entry) {
            diary_header.setText(entry.getStringTime() + ", " + entry.getCategory());
            diary_bs.setText(String.format("%.1f", entry.getBs()));
            diary_cal.setText(String.format("%.1f", entry.getMeal().getCalorie()));
            diary_carb.setText(String.format("%.1f", entry.getMeal().getCarb()));
            diary_protein.setText(String.format("%.1f", entry.getMeal().getProtein()));
            diary_fat.setText(String.format("%.1f", entry.getMeal().getFat()));
            diary_rapidInsulin.setText(String.valueOf(entry.getRapid_insulin()));
            diary_basalInsulin.setText(String.valueOf(entry.getBasal_insulin()));
            diary_meal.setText(entry.getMeal().getContent());
            diary_weight.setText(String.format("%.1f", entry.getWeight()));

            if (entry.getBs() == 0) {
                diary_bs.setVisibility(View.GONE);
            }
            else {
                diary_bs.setVisibility(View.VISIBLE);

                if (entry.getBs() > 10.0) {
                    diary_bs.setBackgroundColor(context.getResources().getColor(R.color.red));
                } else if (entry.getBs() > 6.0) {
                    diary_bs.setBackgroundColor(context.getResources().getColor(R.color.orange));
                } else if (entry.getBs() < 3.0) {
                    diary_bs.setBackgroundColor(context.getResources().getColor(R.color.purple_500));
                } else if (entry.getBs() < 4.0) {
                    diary_bs.setBackgroundColor(context.getResources().getColor(R.color.purple_200));
                } else {
                    diary_bs.setBackgroundColor(context.getResources().getColor(R.color.green));
                }
            }

            if (entry.getMeal().getFoodList().size() == 0) {
                diary_cal.setVisibility(View.GONE);
                diary_calText.setVisibility(View.GONE);
                diary_carb.setVisibility(View.GONE);
                diary_carbText.setVisibility(View.GONE);
                diary_protein.setVisibility(View.GONE);
                diary_protText.setVisibility(View.GONE);
                diary_fat.setVisibility(View.GONE);
                diary_fatText.setVisibility(View.GONE);
                diary_MealImage.setVisibility(View.GONE);
                diary_meal.setVisibility(View.GONE);
                diary_MealTextImage.setVisibility(View.GONE);
            }
            else {

                diary_cal.setVisibility(View.VISIBLE);
                diary_calText.setVisibility(View.VISIBLE);
                diary_carb.setVisibility(View.VISIBLE);
                diary_carbText.setVisibility(View.VISIBLE);
                diary_protein.setVisibility(View.VISIBLE);
                diary_protText.setVisibility(View.VISIBLE);
                diary_fat.setVisibility(View.VISIBLE);
                diary_fatText.setVisibility(View.VISIBLE);
                diary_MealImage.setVisibility(View.VISIBLE);
                diary_meal.setVisibility(View.VISIBLE);
                diary_MealTextImage.setVisibility(View.VISIBLE);
            }

            if (entry.getRapid_insulin() == 0) {
                diary_rapidInsulin.setVisibility(View.GONE);
                diary_rapidText.setVisibility(View.GONE);
                diary_RapidImage.setVisibility(View.GONE);
            }
            else {
                diary_rapidInsulin.setVisibility(View.VISIBLE);
                diary_rapidText.setVisibility(View.VISIBLE);
                diary_RapidImage.setVisibility(View.VISIBLE);
            }

            if (entry.getBasal_insulin() == 0) {
                diary_basalInsulin.setVisibility(View.GONE);
                diary_basalText.setVisibility(View.GONE);
                diary_BasalImage.setVisibility(View.GONE);
            }
            else {
                diary_basalInsulin.setVisibility(View.VISIBLE);
                diary_basalText.setVisibility(View.VISIBLE);
                diary_BasalImage.setVisibility(View.VISIBLE);
            }

            if (entry.getWeight() == 0) {
                diary_weight.setVisibility(View.GONE);
                diary_weightText.setVisibility(View.GONE);
                diary_WeightImage.setVisibility(View.GONE);
            }
            else {
                diary_weight.setVisibility(View.VISIBLE);
                diary_weightText.setVisibility(View.VISIBLE);
                diary_WeightImage.setVisibility(View.VISIBLE);
            }
        }
    }

    public class ViewDateHolder extends RecyclerView.ViewHolder {
        private final TextView diary_date;

        public ViewDateHolder(View itemView) {
            super(itemView);
            diary_date = itemView.findViewById(R.id.diary_date);
        }

        @NonNull
        @Override
        public String toString() {
            return super.toString() + mValues.toString() ;
        }

        public void bind(DiaryEntry entry) {
            diary_date.setText(entry.getStringDate());
        }
    }
}