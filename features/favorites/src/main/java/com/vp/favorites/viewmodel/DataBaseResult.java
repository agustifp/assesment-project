package com.vp.favorites.viewmodel;

import com.vp.database.model.entity.ListItem;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class DataBaseResult {

    private List<ListItem> items;
    private int totalResult;
    private FavoriteState listState;

    private DataBaseResult(List<ListItem> items, int totalResult, FavoriteState listState) {
        this.items = items;
        this.listState = listState;
        this.totalResult = totalResult;
    }

    public List<ListItem> getItems() {
        return items;
    }

    public int getTotalResult() {
        return totalResult;
    }

    public FavoriteState getListState() {
        return listState;
    }

    public static DataBaseResult error() {
        return new DataBaseResult(Collections.emptyList(), 0, FavoriteState.ERROR);
    }

    public static DataBaseResult success(List<ListItem> items, int totalResult) {
        return new DataBaseResult(items, totalResult, FavoriteState.LOADED);
    }

    public static DataBaseResult inProgress() {
        return new DataBaseResult(Collections.emptyList(), 0, FavoriteState.IN_PROGRESS);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DataBaseResult that = (DataBaseResult) o;
        return totalResult == that.totalResult &&
                Objects.equals(items, that.items) &&
                listState == that.listState;
    }

    @Override
    public int hashCode() {
        return Objects.hash(items, totalResult, listState);
    }
}
