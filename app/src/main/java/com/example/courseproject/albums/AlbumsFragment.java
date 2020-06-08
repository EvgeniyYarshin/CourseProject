package com.example.courseproject.albums;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.courseproject.ApiUtils;
import com.example.courseproject.App;
import com.example.courseproject.R;
import com.example.courseproject.album.DetailAlbumFragment;
import com.example.courseproject.db.MusicDao;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class AlbumsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private View errorView;

    public static AlbumsFragment newInstance() {
        return new AlbumsFragment();
    }

    @NonNull
    private final AlbumsAdapter mAlbumsAdapter = new AlbumsAdapter(album -> {
        DetailAlbumFragment detailAlbumFragment = DetailAlbumFragment.newInstance(album);
        getFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, detailAlbumFragment)
                .addToBackStack(DetailAlbumFragment.class.getSimpleName())
                .commit();

    });

    //------------------------------------FRAGMENT LIFE CYCLE---------------------------------------//
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fr_albums, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        recyclerView = view.findViewById(R.id.recycler);
        swipeRefreshLayout = view.findViewById(R.id.refresh);
        errorView = view.findViewById(R.id.error_view);
        swipeRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().setTitle(R.string.albums);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(mAlbumsAdapter);
        onRefresh();
    }

    //----------------------------------------------------------------------------------------------//

    @Override
    public void onRefresh() {
        swipeRefreshLayout.post(this::getAlbums);
    }

    private void getAlbums() {

        ApiUtils.getApiService(false).getAlbums()
                .subscribeOn(Schedulers.io())
                .doOnSuccess(albums -> getMusicDao().insertAlbums(albums))
                .onErrorReturn(throwable -> {
                    if(ApiUtils.NETWORK_EXCEPTIONS.contains(throwable.getClass())) {
                        return getMusicDao().getAlbums();
                    } else return null;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> swipeRefreshLayout.setRefreshing(true))
                .doFinally(() -> swipeRefreshLayout.setRefreshing(false))
                .subscribe(
                        albums -> {
                            errorView.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                            mAlbumsAdapter.addData(albums, true);
                        },
                        throwable -> {
                            errorView.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                        });
    }

    private MusicDao getMusicDao() {
        return ((App) getActivity().getApplication()).getDatabase().getMusicDao();
    }
}
