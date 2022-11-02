package com.example.diabetus.diary;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.view.MenuHost;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.diabetus.R;
import com.example.diabetus.RecyclerViewClickListener;
import com.example.diabetus.database.DbHelper;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class DiaryList_Fragment extends Fragment implements RecyclerViewClickListener {

    private DiaryListAdapter diaryListAdapter;

    public DiaryList_Fragment() {
        super(R.layout.fragment_diary_list_);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_diary_list_, container, false);

        RecyclerView diary_list = view.findViewById(R.id.diary_list);

        {
            DbHelper dbh = new DbHelper(getActivity(), null, 1);
            List<DiaryEntry> list = dbh.getAllDiaryEntry();
            Collections.sort(list, Collections.reverseOrder(Comparator.comparing(DiaryEntry::getDate)));


            diaryListAdapter = new DiaryListAdapter(groupDataByDate(list), this);
            diary_list.setAdapter(diaryListAdapter);
            diary_list.setLayoutManager(new LinearLayoutManager(getActivity()));
        }

        MenuHost menuHost = requireActivity();
        menuHost.addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.diarylist, menu);
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.menu_addDiaryEntry) {
                    NavHostFragment.findNavController(DiaryList_Fragment.this).navigate(R.id.action_diaryList_Fragment_to_entryFragment);
                }
                return false;
            }
        }, getViewLifecycleOwner());

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void recyclerViewListClicked(View v, int position){
        Bundle bundle = new Bundle();
        bundle.putParcelable("entry", diaryListAdapter.getItem(position));
        NavHostFragment.findNavController(DiaryList_Fragment.this).navigate(R.id.action_diaryList_Fragment_to_entryFragment, bundle);
    }

    private List<DiaryListObject> groupDataByDate(List<DiaryEntry> entryList) {
        LinkedHashMap<String, Set<DiaryEntry>> groupedHashMap = new LinkedHashMap<>();
        Set<DiaryEntry> list = null;
        for (DiaryEntry entry : entryList) {
            String hashMapKey = entry.getStringDate();
            if (groupedHashMap.containsKey(hashMapKey)) {
                // The key is already in the HashMap; add the object
                // against the existing key.
                groupedHashMap.get(hashMapKey).add(entry);
            }
            else {
                // The key is not there in the HashMap; create a new key-value pair
                list = new LinkedHashSet<>();
                list.add(entry);
                groupedHashMap.put(hashMapKey, list);
            }
        }

        //Generate list from map
        return generateListFromMap(groupedHashMap);
    }

    private List<DiaryListObject> generateListFromMap(LinkedHashMap<String, Set<DiaryEntry>> groupedHashMap) {
        // We linearly add every item into the consolidatedList.
        List<DiaryListObject> consolidatedList = new ArrayList<>();
        for (String key : groupedHashMap.keySet()) {
            DateFormat df = new SimpleDateFormat("yyyy.MM.dd");
            Date date = new Date();
            try {
                date =  df.parse(key);
            }
            catch (ParseException e) {}
            DiaryListObject dateItem = new DiaryListObject(new DiaryEntry(date), DiaryListObject.DATE_TYPE);
            consolidatedList.add(dateItem);
            for (DiaryEntry entry : groupedHashMap.get(key)) {
                consolidatedList.add(new DiaryListObject(entry, DiaryListObject.ENTRY_TYPE));
            }
        }

        return consolidatedList;
    }
}