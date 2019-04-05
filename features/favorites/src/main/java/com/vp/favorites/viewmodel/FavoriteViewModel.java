package com.vp.favorites.viewmodel;

import com.vp.database.model.entity.ListItem;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class FavoriteViewModel extends ViewModel {
    private MutableLiveData<DataBaseResult> liveData = new MutableLiveData<>();

    private String currentTitle = "";
    private List<ListItem> aggregatedItems = new ArrayList<>();



    public LiveData<DataBaseResult> observeMovies() {
        return liveData;
    }

    public void loadFavorites() {

//        if (page == 1 && !title.equals(currentTitle)) {
//            aggregatedItems.clear();
//            currentTitle = title;
//            liveData.setValue(DataBaseResult.inProgress());
//        }
//        searchService.search(title, page).enqueue(new Callback<SearchResponse>() {
//            @Override
//            public void onResponse(@NonNull Call<SearchResponse> call, @NonNull Response<SearchResponse> response) {
//
//                SearchResponse result = response.body();
//
//                if (result != null) {
//                    aggregatedItems.addAll(result.getSearch());
//                    liveData.setValue(SearchResult.success(aggregatedItems, aggregatedItems.size()));
//                }
//            }
//
//            @Override
//            public void onFailure(@NonNull Call<SearchResponse> call, @NonNull Throwable t) {
//                liveData.setValue(SearchResult.error());
//            }
//        });
    }
}
