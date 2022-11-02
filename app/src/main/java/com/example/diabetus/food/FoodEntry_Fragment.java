package com.example.diabetus.food;

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
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.view.MenuHost;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.diabetus.R;
import com.example.diabetus.database.DbHelper;

public class FoodEntry_Fragment extends Fragment {

    public static final String ARG_FOOD_ENTRY = "entry";

    FoodEntry foodEntry;
    public FoodEntry_Fragment() {
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
        View view = inflater.inflate(R.layout.fragment_foodentry, container, false);

        EditText et_Name = view.findViewById(R.id.et_Name);
        EditText et_Brand = view.findViewById(R.id.et_Brand);
        EditText et_Calorie = view.findViewById(R.id.et_Calorie);
        EditText et_SaturatedFat = view.findViewById(R.id.et_SaturatedFat);
        EditText et_UnsaturatedFat = view.findViewById(R.id.et_UnsaturatedFat);
        EditText et_TransFat = view.findViewById(R.id.et_TransFat);
        EditText et_Sodium = view.findViewById(R.id.et_Sodium);
        EditText et_Carb = view.findViewById(R.id.et_Carb);
        EditText et_Sugar = view.findViewById(R.id.et_Sugar);
        EditText et_Fiber = view.findViewById(R.id.et_Fiber);
        EditText et_Protein = view.findViewById(R.id.et_Protein);
        EditText et_Weight = view.findViewById(R.id.et_Weight);

        Bundle bundle = this.getArguments();
        if (bundle == null) {
            foodEntry = new FoodEntry();
        }
        else {
            foodEntry = bundle.getParcelable(ARG_FOOD_ENTRY);
            et_Name.setHint(foodEntry.getName());
            et_Brand.setHint(foodEntry.getBrand());
            et_Calorie.setHint(String.format("%.2f",foodEntry.getCalorie()));
            et_SaturatedFat.setHint(String.format("%.2f",foodEntry.getSaturated_fat()));
            et_UnsaturatedFat.setHint(String.format("%.2f",foodEntry.getUnsaturated_fat()));
            et_TransFat.setHint(String.format("%.2f",foodEntry.getTrans_fat()));
            et_Sodium.setHint(String.format("%.2f",foodEntry.getSodium()));
            et_Carb.setHint(String.format("%.2f",foodEntry.getCarb()));
            et_Sugar.setHint(String.format("%.2f",foodEntry.getSugar()));
            et_Fiber.setHint(String.format("%.2f",foodEntry.getFiber()));
            et_Protein.setHint(String.format("%.2f",foodEntry.getProtein()));
            et_Weight.setHint(String.format("%.2f",foodEntry.getWeight()));
        }

        et_Name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().isEmpty()) {
                    foodEntry.setName(s.toString());
                }
            }
        });

        et_Brand.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().isEmpty()) {
                    foodEntry.setBrand(s.toString());
                }
            }
        });

        et_Calorie.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().isEmpty()) {
                    foodEntry.setCalorie(Double.parseDouble(s.toString()));
                }
            }
        });

        et_SaturatedFat.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().isEmpty()) {
                    foodEntry.setSaturated_fat(Double.parseDouble(s.toString()));
                }
            }
        });

        et_UnsaturatedFat.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().isEmpty()) {
                    foodEntry.setUnsaturated_fat(Double.parseDouble(s.toString()));
                }
            }
        });

        et_TransFat.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().isEmpty()) {
                    foodEntry.setTrans_fat(Double.parseDouble(s.toString()));
                }
            }
        });

        et_Sodium.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().isEmpty()) {
                    foodEntry.setSodium(Double.parseDouble(s.toString()));
                }
            }
        });

        et_Carb.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().isEmpty()) {
                    foodEntry.setCarb(Double.parseDouble(s.toString()));
                }
            }
        });

        et_Sugar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().isEmpty()) {
                    foodEntry.setSugar(Double.parseDouble(s.toString()));
                }
            }
        });

        et_Fiber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().isEmpty()) {
                    foodEntry.setFiber(Double.parseDouble(s.toString()));
                }
            }
        });

        et_Protein.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().isEmpty()) {
                    foodEntry.setProtein(Double.parseDouble(s.toString()));
                }
            }
        });

        et_Weight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().isEmpty()) {
                    foodEntry.setWeight(Double.parseDouble(s.toString()));
                }
            }
        });


        MenuHost menuHost = requireActivity();
        menuHost.addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.foodentry, menu);
            }

            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.menu_delete_entry:
                        AlertDialog.Builder dialogBuilder;
                        DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
                            switch (which){
                                case DialogInterface.BUTTON_POSITIVE:
                                    //Yes button clicked
                                    dbh.deleteFoodEntry(foodEntry);
                                    NavHostFragment.findNavController(FoodEntry_Fragment.this).navigate(R.id.action_foodentry_to_foodeditor);
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

                    case R.id.menu_Save:
                        boolean success = dbh.addFoodEntry(foodEntry);
                        String text = (!success) ? "Database error" : "Food saved as " + foodEntry.getPresentableName();
                        Toast.makeText(getActivity(), text, Toast.LENGTH_LONG).show();
                        if (success) {
                            NavHostFragment.findNavController(FoodEntry_Fragment.this).navigate(R.id.action_foodentry_to_foodeditor);
                        }
                        break;
                }
                return false;
            }
        }, getViewLifecycleOwner());

        // Inflate the layout for this fragment
        return view;
    }
}
