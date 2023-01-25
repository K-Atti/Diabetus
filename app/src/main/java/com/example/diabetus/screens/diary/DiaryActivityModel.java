package com.example.diabetus.screens.diary;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.diabetus.database.DbHelper;
import com.example.diabetus.database.diary.DiaryEntry;

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
import java.util.Locale;
import java.util.Set;

public class DiaryActivityModel {

    MutableLiveData<List<DiaryListObject>> diaryList;
    DbHelper dbh;


    public DiaryActivityModel(Context context) {
        diaryList = new MutableLiveData<>(new ArrayList<>());
        dbh = new DbHelper(context, null, 1);
        refreshDiaryList();
    }

    public void refreshDiaryList() {
        new Thread(() -> {
            List<DiaryEntry> list = dbh.getAllDiaryEntry();
            Collections.sort(list, Collections.reverseOrder(Comparator.comparing(DiaryEntry::getDate)));
            diaryList.postValue(groupDataByDate(list));
        }).start();
    }

    public LiveData<List<DiaryListObject>> getDiaryList() {
        return diaryList;
    }

    private List<DiaryListObject> groupDataByDate(List<DiaryEntry> entryList) {
        LinkedHashMap<String, Set<DiaryEntry>> groupedHashMap = new LinkedHashMap<>();
        Set<DiaryEntry> list;
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
            DateFormat df = new SimpleDateFormat("yyyy.MM.dd", Locale.getDefault());
            Date date = new Date();
            try {
                date =  df.parse(key);
            }
            catch (ParseException e) {
                e.printStackTrace();
            }
            DiaryListObject dateItem = new DiaryListObject(new DiaryEntry(date), DiaryListObject.DATE_TYPE);
            consolidatedList.add(dateItem);
            for (DiaryEntry entry : groupedHashMap.get(key)) {
                consolidatedList.add(new DiaryListObject(entry, DiaryListObject.ENTRY_TYPE));
            }
        }

        return consolidatedList;
    }
}
