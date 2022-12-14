package com.example.diabetus.diary;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.view.MenuHost;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.diabetus.R;
import com.example.diabetus.database.DbHelper;
import com.example.diabetus.database.DiaryDbHelper;
import com.example.diabetus.food.Meal;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

enum MyCategories {
    ENUM1("Other"),
    ENUM2("Empty stomach"),
    ENUM3("Before breakfast"),
    ENUM4("Breakfast"),
    ENUM5("After breakfast"),
    ENUM6("Before lunch"),
    ENUM7("Lunch"),
    ENUM8("After lunch"),
    ENUM9("Before dinner"),
    ENUM10("Dinner"),
    ENUM11("After dinner"),
    ENUM12("Snack");

    private final String friendlyName;

    MyCategories(String friendlyName){
        this.friendlyName = friendlyName;
    }
    @NonNull
    @Override
    public String toString() {
        return friendlyName;
    }

    public static int getIndex(String friendlyName) {
        for(MyCategories i : MyCategories.values()) {
            if (friendlyName.equals(i.toString())) {
                return i.ordinal();
            }
        }
        return 0;
    }
}

public class EntryFragment extends Fragment {
    private EditText et_Date, et_Time, et_BloodSugar, et_RapidInsulin, et_BasalInsulin, et_Weight;
    private DiaryEntry entry;

    public EntryFragment() {
        super(R.layout.fragment_entry);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        entry = new DiaryEntry();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_entry, container, false);

        et_Date = view.findViewById(R.id.et_Date);
        et_Time = view.findViewById(R.id.et_Time);
        et_BloodSugar = view.findViewById(R.id.et_BloodSugar);
        et_RapidInsulin = view.findViewById(R.id.et_RI);
        et_BasalInsulin = view.findViewById(R.id.et_BI);
        et_Weight = view.findViewById(R.id.et_Weight);
        Spinner spinner_Category = view.findViewById(R.id.spinner_Category);
        Button btn_SetMeal = view.findViewById(R.id.btn_SetMeal);
        TextView text_CurrentMeal = view.findViewById(R.id.textCurrentMeal);
        TextView text_CurrentMacros = view.findViewById(R.id.textCurrentMacros);

        super.onCreate(savedInstanceState);

        btn_SetMeal.setOnClickListener(view1 -> {
            Bundle bundle = new Bundle();
            bundle.putParcelable(MealFragment.ARG_DIARY_ENTRY, entry);
            NavHostFragment.findNavController(EntryFragment.this).navigate(R.id.action_entryFragment_to_mealFragment, bundle);
        });

        et_BloodSugar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().isEmpty()) {
                    entry.setBs(Double.parseDouble(et_BloodSugar.getText().toString()));
                }
            }
        });

        et_RapidInsulin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().isEmpty()) {
                    entry.setRapid_insulin(Integer.parseInt(et_RapidInsulin.getText().toString()));
                }
            }
        });

        et_BasalInsulin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().isEmpty()) {
                    entry.setBasal_insulin(Integer.parseInt(et_BasalInsulin.getText().toString()));
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
                    entry.setWeight(Double.parseDouble(et_Weight.getText().toString()));
                }
            }
        });

        spinner_Category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                entry.setCategory(parent.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            entry = bundle.getParcelable("entry");
        }
        else
        {
            entry.setId(-1);
            entry.setBs(0.0);
            entry.setDate(Calendar.getInstance().getTime());
            entry.setRapid_insulin(0);
            entry.setBasal_insulin(0);
            entry.setWeight(0.0);
            entry.setCategory(MyCategories.ENUM1.toString());
            entry.setMeal(new Meal());
        }

        et_BloodSugar.setHint(String.valueOf(entry.getBs()));

        SimpleDateFormat dateF = new SimpleDateFormat("yyyy.MM.dd", Locale.getDefault());
        String date = dateF.format(entry.getDate());
        et_Date.setText(date);
        et_Date.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                et_Date.setTextColor(Color.WHITE);
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().isEmpty()) {
                    try {
                        Date date;
                        date = new SimpleDateFormat("yyyy.MM.dd HH:mm").parse(et_Date.getText().toString() + " " + et_Time.getText().toString());
                        entry.setDate(date);
                    }
                    catch (ParseException e) {
                    }
                }
            }
        });

        dateF = new SimpleDateFormat("HH:mm", Locale.getDefault());
        date = dateF.format(entry.getDate());
        et_Time.setText(date);
        et_Time.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                et_Time.setTextColor(Color.WHITE);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!editable.toString().isEmpty()) {
                    try {
                        Date date;
                        date = new SimpleDateFormat("yyyy.MM.dd HH:mm").parse(et_Date.getText().toString() + " " + et_Time.getText().toString());
                        entry.setDate(date);
                    }
                    catch (ParseException e) {
                    }
                }
            }
        });

        et_RapidInsulin.setHint(String.valueOf(entry.getRapid_insulin()));
        et_BasalInsulin.setHint(String.valueOf(entry.getBasal_insulin()));
        et_Weight.setHint(String.valueOf(entry.getWeight()));

        ArrayAdapter adapter = new ArrayAdapter(getContext(), R.layout.spinner_category_item, MyCategories.values());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_Category.setAdapter(adapter);
        spinner_Category.setSelection(MyCategories.getIndex(entry.getCategory()));
        text_CurrentMeal.setText(entry.getMeal().getContent());
        text_CurrentMacros.setText(entry.getMeal().getMacros());

        MenuHost menuHost = requireActivity();
        menuHost.addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.diaryentry, menu);
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.menu_delete_entry:
                        DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
                            switch (which){
                                case DialogInterface.BUTTON_POSITIVE:
                                    DiaryDbHelper dbh = new DiaryDbHelper(getActivity(),"Diary", null, 1);
                                    dbh.deleteEntry(entry);
                                    //Switch to diary fragment
                                    NavHostFragment.findNavController(EntryFragment.this).navigate(R.id.action_entryFragment_to_diaryList_Fragment);

                                    break;

                                case DialogInterface.BUTTON_NEGATIVE:
                                    //"No" button clicked
                                    break;
                            }
                        };

                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
                                .setNegativeButton("No", dialogClickListener).show();
                        break;

                    case R.id.menu_Copy:
                        entry.setId(-1);
                        /* Intentional fallthrough */

                    case R.id.menu_Save:
                        DbHelper dbh = new DbHelper(getActivity(), null, 1);

                        if(dbh.addDiaryEntry(entry))
                        {
                            Toast.makeText(getActivity(), "Saved", Toast.LENGTH_LONG).show();
                            //Switch to diary fragment
                            NavHostFragment.findNavController(EntryFragment.this).navigate(R.id.action_entryFragment_to_diaryList_Fragment);
                        }
                        else {
                            Toast.makeText(getActivity(), "Database error", Toast.LENGTH_LONG).show();
                        }
                        break;
                }
                return false;
            }
        }, getViewLifecycleOwner());

        return view;
    }
}