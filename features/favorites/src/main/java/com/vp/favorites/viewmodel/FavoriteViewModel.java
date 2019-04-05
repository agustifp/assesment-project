package com.vp.favorites.viewmodel;

import com.vp.database.dao.MoviesDAO;
import com.vp.database.model.entity.ListItem;

import java.util.List;

import javax.inject.Inject;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class FavoriteViewModel extends ViewModel {
    private MutableLiveData<DataBaseResult> liveData = new MutableLiveData<>();

    private MoviesDAO moviesDAO = new MoviesDAO();

    @Inject
    FavoriteViewModel() {

    }

    public LiveData<DataBaseResult> observeMovies() {
        return liveData;
    }

    public void loadFavorites() {

        liveData.setValue(DataBaseResult.inProgress());

        List<ListItem> results = moviesDAO.getFavoritesMovies();
        if (results != null && results.size() > 0) {
            liveData.setValue(DataBaseResult.success(results, results.size()));
        } else {
            liveData.setValue(DataBaseResult.error());
        }
    }
}
