package com.vp.favorites;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ViewAnimator;

import com.vp.detail.DetailActivity;
import com.vp.favorites.viewmodel.DataBaseResult;
import com.vp.favorites.viewmodel.FavoriteViewModel;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import dagger.android.support.AndroidSupportInjection;

public class FavoriteFragment extends Fragment implements FavoriteAdapter.OnItemClickListener {
    static final String TAG = "FavoriteFragment";

    @Inject
    ViewModelProvider.Factory factory;

    private FavoriteViewModel listViewModel;
    private FavoriteAdapter listAdapter;
    private ViewAnimator viewAnimator;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private TextView errorTextView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidSupportInjection.inject(this);
        listViewModel = ViewModelProviders.of(this, factory).get(FavoriteViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favorite, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setViews(view);

        initList();
        listViewModel.observeMovies().observe(this, searchResult -> {
            if (searchResult != null) {
                handleResult(listAdapter, searchResult);
            }
        });

        listViewModel.loadFavorites();
        showProgressBar();
    }

    private void setViews(@NonNull View view) {
        recyclerView = view.findViewById(R.id.recyclerView);
        viewAnimator = view.findViewById(R.id.viewAnimator);
        progressBar = view.findViewById(R.id.progressBar);
        errorTextView = view.findViewById(R.id.errorText);

    }


    private void initList() {
        listAdapter = new FavoriteAdapter();
        listAdapter.setOnItemClickListener(this);
        recyclerView.setAdapter(listAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
    }

    private void showProgressBar() {
        viewAnimator.setDisplayedChild(viewAnimator.indexOfChild(progressBar));
    }

    private void showList() {
        viewAnimator.setDisplayedChild(viewAnimator.indexOfChild(recyclerView));
    }

    private void showError() {
        viewAnimator.setDisplayedChild(viewAnimator.indexOfChild(errorTextView));
    }

    private void handleResult(@NonNull FavoriteAdapter listAdapter, @NonNull DataBaseResult searchResult) {
        switch (searchResult.getListState()) {
            case LOADED: {
                setItemsData(listAdapter, searchResult);
                showList();
                break;
            }
            case IN_PROGRESS: {
                showProgressBar();
                break;
            }
            default: {
                showError();
            }
        }
    }

    private void setItemsData(@NonNull FavoriteAdapter listAdapter, @NonNull DataBaseResult searchResult) {
        listAdapter.setItems(searchResult.getItems());


    }

    @Override
    public void onItemClick(String imdbID) {

        Intent detailIntent = new Intent(getActivity(), DetailActivity.class);
        //replaced the empty spaces to avoid api failure and bad movie ID generated.
        Uri uri = Uri.parse("").buildUpon()
                .appendQueryParameter("imdbID", imdbID.replace(" ", ""))
                .build();
        detailIntent.setData(uri);
        startActivity(detailIntent);
    }
}
