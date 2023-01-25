package com.example.diabetus.screens.diary;

import com.example.diabetus.database.diary.DiaryEntry;

public class DiaryListObject {
    public static final int ENTRY_TYPE = 0;
    public static final int DATE_TYPE = 1;

    private DiaryEntry Entry;
    private final int ObjectType;

    public DiaryListObject(DiaryEntry entry, int type) {
        Entry = entry;
        ObjectType = type;
    }

    public DiaryEntry getEntry() {
        return Entry;
    }

    public void setEntry(DiaryEntry entry) {
        Entry = entry;
    }

    public int getObjectType() {
        return ObjectType;
    }
}
