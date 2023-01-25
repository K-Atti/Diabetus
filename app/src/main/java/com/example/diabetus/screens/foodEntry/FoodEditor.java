package com.example.diabetus.screens.foodEntry;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.view.MenuHost;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.diabetus.R;
import com.example.diabetus.common.EatableListListener;
import com.example.diabetus.database.DbHelper;
import com.example.diabetus.database.food.Eatable;
import com.example.diabetus.database.food.EatableListAdapter;
import com.example.diabetus.database.food.FoodEntry;
import com.example.diabetus.database.food.FoodListAdapter;
import com.example.diabetus.database.food.Meal;
import com.example.diabetus.screens.mealEditor.MealFragment;

import java.util.ArrayList;
import java.util.List;

public class FoodEditor extends Fragment implements EatableListListener {

    EatableListAdapter eatableListAdapter;
    DbHelper dbh;

    private List<? extends Eatable> full_eatable_list;

    public FoodEditor() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        dbh = new DbHelper(getActivity(), null, 1);
        View view = inflater.inflate(R.layout.fragment_foodeditor, container, false);

        RecyclerView eatable_list = view.findViewById(R.id.list_Eatable);
        {
            full_eatable_list = dbh.getAllEatableEntry();
            eatableListAdapter = new EatableListAdapter(full_eatable_list, this);
            eatable_list.setAdapter(eatableListAdapter);
            eatable_list.setLayoutManager(new LinearLayoutManager(getActivity()));
        }

        EditText et_FoodEditorSearch = view.findViewById(R.id.et_FoodEditorSearch);
        et_FoodEditorSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                List<Object> filteredList = new ArrayList<>();

                if (!s.toString().isEmpty()) {
                    for (Eatable eatable : full_eatable_list) {
                        if (eatable.getPresentableName().toLowerCase().contains(s.toString().toLowerCase())) {
                            filteredList.add(eatable);
                        }
                    }
                    eatableListAdapter.Update((List<? extends Eatable>) (Object) filteredList);
                }
            }
        });

        MenuHost menuHost = requireActivity();
        menuHost.addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.foodeditor, menu);
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.menu_addMeal:
                        NavHostFragment.findNavController(FoodEditor.this).navigate(R.id.action_foodeditor_to_mealFragment);
                        break;

                    case R.id.menu_addFood:
                        NavHostFragment.findNavController(FoodEditor.this).navigate(R.id.action_foodeditor_to_foodentry);
                        break;
                }
                return false;
            }
        }, getViewLifecycleOwner());

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onInspectClicked(int position) {
        onRowClicked(position);
    }

    @Override
    public void onRowClicked(int position) {
        if (eatableListAdapter.getItem(position).getType() == Eatable.EATABLE_FOODENTRY) {
            Bundle bundle = new Bundle();
            bundle.putParcelable(FoodEntry_Fragment.ARG_FOOD_ENTRY, (FoodEntry)eatableListAdapter.getItem(position));
            NavHostFragment.findNavController(FoodEditor.this).navigate(R.id.action_foodeditor_to_foodentry, bundle);
        }
        else if (eatableListAdapter.getItem(position).getType() == Eatable.EATABLE_MEAL) {
            Bundle bundle = new Bundle();
            bundle.putParcelable(MealFragment.ARG_MEAL_ENTRY, (Meal)eatableListAdapter.getItem(position));
            NavHostFragment.findNavController(FoodEditor.this).navigate(R.id.action_foodeditor_to_mealFragment, bundle);
        }
    }

    @Override
    public void onAddClicked(int position) {
        if (eatableListAdapter.getItem(position).getType() == Eatable.EATABLE_FOODENTRY) {
            FoodEntry entry = (FoodEntry)eatableListAdapter.getItem(position);
            dbh.addFoodEntry(entry);
            Toast.makeText(getContext(), "Food \"" + entry.getPresentableName() + "\" added",Toast.LENGTH_SHORT).show();
        }
    }
}