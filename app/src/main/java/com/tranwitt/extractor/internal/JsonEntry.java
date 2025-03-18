package com.tranwitt.extractor.internal;

import androidx.annotation.NonNull;

public class JsonEntry {

    private final String mName;
    private final String mListId;
    private final String mId;

    /**
     * A class that stores data found within the specified json file
     * @param mName - String name of the entry
     * @param mListId - String list id of the entry
     * @param mId - String id of the entry
     */
    public JsonEntry(@NonNull String mName, @NonNull String mListId, @NonNull String mId) {
        this.mName = mName;
        this.mListId = mListId;
        this.mId = mId;
    }

    /**
     * Gets the name of the entry
     * @return String name
     */
    public String getName() {
        return mName;
    }

    /**
     * Gets the list id of the entry
     * @return String list id
     */
    public String getListId() {
        return mListId;
    }

    /**
     * Gets the id of the entry
     * @return String id
     */
    public String getId() {
        return mId;
    }
}
