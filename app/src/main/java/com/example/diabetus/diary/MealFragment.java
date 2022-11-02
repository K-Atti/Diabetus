package com.example.diabetus.diary;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.view.MenuHost;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.diabetus.R;
import com.example.diabetus.RecyclerViewClickListener;
import com.example.diabetus.database.DbHelper;
import com.example.diabetus.food.Eatable;
import com.example.diabetus.food.EatableListAdapter;
import com.example.diabetus.food.Food;
import com.example.diabetus.food.FoodEntry;
import com.example.diabetus.food.FoodListAdapter;
import com.example.diabetus.food.Meal;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class MealFragment extends Fragment implements RecyclerViewClickListener {

    public static final String ARG_DIARY_ENTRY = "entry";
    public static final String ARG_MEAL_ENTRY = "meal";

    private String Caller;

    DiaryEntry Entry;

    private EatableListAdapter EatableListAdapter;
    private FoodListAdapter FoodListAdapter;

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private Button btn_SelectedFood;

    final NumberFormat numberFormat = NumberFormat.getInstance(Locale.getDefault());

    private List<? extends Eatable> full_eatable_list;

    public MealFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        DbHelper dbh = new DbHelper(getActivity(), null, 1);
        View view = inflater.inflate(R.layout.fragment_meal, container, false);
        EditText et_MealName = view.findViewById(R.id.et_MealName);
        Bundle bundle = this.getArguments();

        if (bundle == null) {
            Entry = new DiaryEntry();
            Entry.setMeal(new Meal());
            Caller = ARG_MEAL_ENTRY;
        }
        else {
            Entry = bundle.getParcelable(ARG_DIARY_ENTRY);

            if (Entry == null) {
                Entry = new DiaryEntry();
                Entry.setMeal(bundle.getParcelable(ARG_MEAL_ENTRY));
                et_MealName.setText(Entry.getMeal().getName());
                Caller = ARG_MEAL_ENTRY;
            } else {
                Caller = ARG_DIARY_ENTRY;
            }
        }

        // Initialize layout elements
        RecyclerView eatable_list = view.findViewById(R.id.list_Eatable);
        {
            full_eatable_list = dbh.getAllEatableEntry();
            EatableListAdapter  = new EatableListAdapter(dbh.getAllEatableEntry(), this);
            eatable_list.setAdapter(EatableListAdapter);
            eatable_list.setLayoutManager(new LinearLayoutManager(getActivity()));
        }

        EditText et_search = view.findViewById(R.id.et_Search);
        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //Not needed
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //Not needed
            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });

        btn_SelectedFood = view.findViewById(R.id.btn_SelectedFood);
        btn_SelectedFood.setText(Entry.getMeal().getMacros());
        btn_SelectedFood.setOnClickListener(v -> createFoodListPopup());

        MenuHost menuHost = requireActivity();
        menuHost.addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.meal, menu);
                MenuItem item;
                if (Caller.equals(ARG_MEAL_ENTRY)) {
                    item = menu.findItem(R.id.menu_SaveMeal);
                }
                else {
                    item = menu.findItem(R.id.menu_DeleteMeal);
                }
                item.setVisible(false);
            }

            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.menu_DeleteMeal:
                        DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
                            switch (which) {
                                case DialogInterface.BUTTON_POSITIVE:
                                    //Yes button clicked
                                    dbh.deleteMealEntry(Entry.getMeal());
                                    NavHostFragment.findNavController(MealFragment.this).navigate(R.id.action_mealFragment_to_foodeditor);
                                    break;

                                case DialogInterface.BUTTON_NEGATIVE:
                                    //No button clicked
                                    break;
                            }
                            dialog.dismiss();
                        };
                        dialogBuilder = new AlertDialog.Builder(getActivity());
                        dialogBuilder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
                                .setNegativeButton("No", dialogClickListener).show();
                        break;

                    case R.id.menu_SaveAsMeal:
                        //Save meal into database and make a toast
                        String name = et_MealName.getText().toString();
                        if (!name.isEmpty()) {
                            Entry.getMeal().setName(name);
                            boolean success = dbh.addMealEntry(Entry.getMeal());
                            String text = (!success) ? "Database error" : "Meal saved as " + et_MealName.getText().toString();
                            Toast.makeText(getActivity(), text, Toast.LENGTH_LONG).show();
                            if (Caller.equals(ARG_MEAL_ENTRY)) {
                                NavHostFragment.findNavController(MealFragment.this).navigate(R.id.action_mealFragment_to_foodeditor);
                            }
                        }
                        else {
                            Toast.makeText(getActivity(), "No name is given", Toast.LENGTH_SHORT).show();
                        }
                        break;

                    case R.id.menu_SaveMeal:
                        Bundle bundle = new Bundle();
                        bundle.putParcelable(ARG_DIARY_ENTRY, Entry);
                        NavHostFragment.findNavController(MealFragment.this).navigate(R.id.action_mealFragment_to_entryFragment, bundle);
                        break;
                }
                return false;
            }
        }, getViewLifecycleOwner());

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void recyclerViewListClicked(View v, int position) {
        if (EatableListAdapter.getItem(position).getType() == Eatable.EATABLE_FOODENTRY) {
            // create a popup window
            createQuantityPopup((FoodEntry)EatableListAdapter.getItem(position), Integer.MAX_VALUE);
        }
        else if (EatableListAdapter.getItem(position).getType() == Eatable.EATABLE_MEAL) {
            Entry.addMeal((Meal)EatableListAdapter.getItem(position));
            btn_SelectedFood.setText(Entry.getMeal().getMacros());
        }
    }

    public void createFoodListPopup() {
        RecyclerView foodList;
        RecyclerViewClickListener foodListListener = (v, position) -> {
            Food food = FoodListAdapter.getItem(position);
            //change dialog to quantity
            dialog.dismiss();
            createQuantityPopup(food.getFood(), position);
        };

        dialogBuilder = new AlertDialog.Builder(this.getActivity());
        final View foodListPopUpView = getLayoutInflater().inflate(R.layout.foodlist_popup, null);
        FoodListAdapter  = new FoodListAdapter(Entry.getMeal().getFoodList(), foodListListener);

        foodList = foodListPopUpView.findViewById(R.id.list_popup_foodList);
        foodList.setAdapter(FoodListAdapter);
        foodList.setLayoutManager(new LinearLayoutManager(getActivity()));

        Button btn_saveFoodlist = foodListPopUpView.findViewById(R.id.btn_popup_foodList);
        btn_saveFoodlist.setOnClickListener(v -> {
            Entry.getMeal().setFoodList(FoodListAdapter.getAllItem());
            btn_SelectedFood.setText(Entry.getMeal().getMacros());
            dialog.dismiss();
        });

        dialogBuilder.setView(foodListPopUpView);
        dialog = dialogBuilder.create();
        dialog.setOnDismissListener(dialog -> btn_SelectedFood.setText(Entry.getMeal().getMacros()));
        dialog.show();
    }

    @SuppressLint("SetTextI18n")
    public void createQuantityPopup(FoodEntry foodEntry, int position) {
        Button btn_Quantity0;
        Button btn_Quantity1;
        Button btn_Quantity2;
        Button btn_Quantity3;
        Button btn_Quantity4;
        Button btn_Quantity5;
        Button btn_Quantity6;
        Button btn_Quantity7;
        EditText et_Quantity;
        SwitchCompat switch_Quantity;
        TextView text_QuantityName;


        dialogBuilder = new AlertDialog.Builder(this.getActivity());
        final View quantityPopUpView = getLayoutInflater().inflate(R.layout.quantity_popup, null);

        btn_Quantity0 = quantityPopUpView.findViewById(R.id.btn_Quantity0);
        btn_Quantity1 = quantityPopUpView.findViewById(R.id.btn_Quantity1);
        btn_Quantity2 = quantityPopUpView.findViewById(R.id.btn_Quantity2);
        btn_Quantity3 = quantityPopUpView.findViewById(R.id.btn_Quantity3);
        btn_Quantity4 = quantityPopUpView.findViewById(R.id.btn_Quantity4);
        btn_Quantity5 = quantityPopUpView.findViewById(R.id.btn_Quantity5);
        btn_Quantity6 = quantityPopUpView.findViewById(R.id.btn_Quantity6);
        btn_Quantity7 = quantityPopUpView.findViewById(R.id.btn_Quantity7);
        Button btn_Ok = quantityPopUpView.findViewById(R.id.btn_Ok);
        et_Quantity = quantityPopUpView.findViewById(R.id.et_Quantity);
        switch_Quantity = quantityPopUpView.findViewById(R.id.switch_Quantity);
        text_QuantityName = quantityPopUpView.findViewById(R.id.textQuantityName);

        et_Quantity.setText("0");
        btn_Quantity0.setText("1");
        btn_Quantity1.setText("5");
        btn_Quantity2.setText("10");
        btn_Quantity3.setText("15");
        btn_Quantity4.setText("25");
        btn_Quantity5.setText("50");
        btn_Quantity6.setText("100");
        btn_Quantity7.setText("200");
        text_QuantityName.setText(foodEntry.getPresentableName());

        btn_Ok.setOnClickListener(view1 -> {
            int type = switch_Quantity.isChecked() ? Food.QUANTITY_PIECE : Food.QUANTITY_100G;
            if (position == Integer.MAX_VALUE) {
                try {
                    Entry.getMeal().addFood(foodEntry, Objects.requireNonNull(numberFormat.parse(et_Quantity.getText().toString())).doubleValue(),
                            type);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                dialog.dismiss();
            } else {
                try {
                    Entry.getMeal().getFoodList().get(position).setQuantity(
                            Objects.requireNonNull(numberFormat.parse(et_Quantity.getText().toString())).doubleValue());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Entry.getMeal().getFoodList().get(position).setQuantityType(type);
                //change dialog to food list
                dialog.dismiss();
                createFoodListPopup();
            }
            btn_SelectedFood.setText(Entry.getMeal().getMacros());
        });

        btn_Quantity0.setOnClickListener(view1 -> {
            double quantity = 0;
            try {
                quantity = Objects.requireNonNull(numberFormat.parse(et_Quantity.getText().toString())).doubleValue();
                quantity += Objects.requireNonNull(numberFormat.parse(btn_Quantity0.getText().toString())).doubleValue();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            et_Quantity.setText(String.format("%.2f", quantity));
        });

        btn_Quantity1.setOnClickListener(view1 -> {
            double quantity = 0;
            try {
                quantity = Objects.requireNonNull(numberFormat.parse(et_Quantity.getText().toString())).doubleValue();
                quantity += Objects.requireNonNull(numberFormat.parse(btn_Quantity1.getText().toString())).doubleValue();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            et_Quantity.setText(String.format("%.2f", quantity));
        });

        btn_Quantity2.setOnClickListener(view1 -> {
            double quantity = 0;
            try {
                quantity = Objects.requireNonNull(numberFormat.parse(et_Quantity.getText().toString())).doubleValue();
                quantity += Objects.requireNonNull(numberFormat.parse(btn_Quantity2.getText().toString())).doubleValue();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            et_Quantity.setText(String.format("%.2f", quantity));
        });

        btn_Quantity3.setOnClickListener(view1 -> {
            double quantity = 0;
            try {
                quantity = Objects.requireNonNull(numberFormat.parse(et_Quantity.getText().toString())).doubleValue();
                quantity += Objects.requireNonNull(numberFormat.parse(btn_Quantity3.getText().toString())).doubleValue();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            et_Quantity.setText(String.format("%.2f", quantity));
        });

        btn_Quantity4.setOnClickListener(view1 -> {
            double quantity = 0;
            try {
                quantity = Objects.requireNonNull(numberFormat.parse(et_Quantity.getText().toString())).doubleValue();
                quantity += Objects.requireNonNull(numberFormat.parse(btn_Quantity4.getText().toString())).doubleValue();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            et_Quantity.setText(String.format("%.2f", quantity));
        });

        btn_Quantity5.setOnClickListener(view1 -> {
            double quantity = 0;
            try {
                quantity = Objects.requireNonNull(numberFormat.parse(et_Quantity.getText().toString())).doubleValue();
                quantity += Objects.requireNonNull(numberFormat.parse(btn_Quantity5.getText().toString())).doubleValue();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            et_Quantity.setText(String.format("%.2f", quantity));
        });

        btn_Quantity6.setOnClickListener(view1 -> {
            double quantity = 0;
            try {
                quantity = Objects.requireNonNull(numberFormat.parse(et_Quantity.getText().toString())).doubleValue();
                quantity += Objects.requireNonNull(numberFormat.parse(btn_Quantity6.getText().toString())).doubleValue();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            et_Quantity.setText(String.format("%.2f", quantity));
        });

        btn_Quantity7.setOnClickListener(view1 -> {
            double quantity = 0;
            try {
                quantity = Objects.requireNonNull(numberFormat.parse(et_Quantity.getText().toString())).doubleValue();
                quantity += Objects.requireNonNull(numberFormat.parse(btn_Quantity7.getText().toString())).doubleValue();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            et_Quantity.setText(String.format("%.2f", quantity));
        });

        switch_Quantity.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                et_Quantity.setText("0");
                btn_Quantity0.setText("1");
                btn_Quantity1.setText("5");
                btn_Quantity2.setText("10");
                btn_Quantity3.setText("15");
                btn_Quantity4.setText("0.25");
                btn_Quantity5.setText("0.33");
                btn_Quantity6.setText("0.5");
                btn_Quantity7.setText("0.66");
            } else {
                et_Quantity.setText("0");
                btn_Quantity0.setText("1");
                btn_Quantity1.setText("5");
                btn_Quantity2.setText("10");
                btn_Quantity3.setText("15");
                btn_Quantity4.setText("25");
                btn_Quantity5.setText("50");
                btn_Quantity6.setText("100");
                btn_Quantity7.setText("200");
            }
        });

        dialogBuilder.setView(quantityPopUpView);
        dialog = dialogBuilder.create();
        dialog.show();
    }

    private void filter(String text) {
        List<Object> filteredList = new ArrayList<>();

        for (Eatable eatable : full_eatable_list) {
            if (eatable.getPresentableName().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(eatable);
            }
        }
        EatableListAdapter.Update((List<? extends Eatable>)(Object)filteredList);
    }
}