package com.example.courseproject.comments;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.courseproject.ApiUtils;
import com.example.courseproject.App;
import com.example.courseproject.R;
import com.example.courseproject.db.MusicDao;
import com.example.courseproject.model.Album;
import com.example.courseproject.model.Comment;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.HttpException;

import static android.view.KeyEvent.KEYCODE_ENTER;
import static com.example.courseproject.album.DetailAlbumFragment.ALBUM_KEY;

public class CommentsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mRefresher;
    private View mErrorView;
    private Album mAlbum;
    private View mAddComment;
    private ImageButton mButton;
    private EditText mEditText;
    private int mCommentsCount;
    private boolean mIsFirstLoadComments = true;

    private final CommentsAdapter mCommentsAdapter = new CommentsAdapter();

    public static CommentsFragment newInstance(Bundle args) {
        CommentsFragment fragment = new CommentsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    //------------------------------------FRAGMENT LIFE CYCLE---------------------------------------//
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fr_comments, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mRecyclerView = view.findViewById(R.id.recycler);
        mRefresher = view.findViewById(R.id.refresh);
        mErrorView = view.findViewById(R.id.error_view);
        mAddComment = view.findViewById(R.id.add_comment_view);
        mButton = view.findViewById(R.id.bt_add_comment);
        mEditText = view.findViewById(R.id.et_add_comment);

        /*mEditText.setOnKeyListener((view1, i, keyEvent) -> {
            if (keyEvent.getKeyCode() == KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_UP) {
                sendComment();
            }
            return true;
        });*/

        mRefresher.setOnRefreshListener(this);
        mAddComment.setVisibility(View.VISIBLE);

        mButton.setOnClickListener(view12 -> sendComment());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        assert getArguments() != null;
        mAlbum = (Album) getArguments().getSerializable(ALBUM_KEY);

        getActivity().setTitle(mAlbum.getName());

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mCommentsAdapter);

        onRefresh();
    }

    //----------------------------------------------------------------------------------------------//

    //------------------------------------SWIPE REFRESH LAYOUT--------------------------------------//
    @Override
    public void onRefresh() {
        mCommentsCount = mCommentsAdapter.getItemCount();
        mRefresher.post(() -> {
            mRefresher.setRefreshing(true);
            getComments(false);
        });
    }
    //----------------------------------------------------------------------------------------------//

    //---------------------------------INTERACTION WITH COMMENTS------------------------------------//
    private void sendComment() {
        if(mEditText.length() == 0) {
            Toast.makeText(getActivity(), "Нет текста для отправки", Toast.LENGTH_SHORT).show();
            return;
        }
        Comment comment_post = new Comment(mAlbum.getId(), mEditText.getText().toString());
        ApiUtils.getApiService(false).setComment(comment_post)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    getComments(true);
                    mEditText.setText("");
                }, throwable -> {
                    Toast.makeText(getActivity(), "Ошибка", Toast.LENGTH_SHORT).show();
                    if(throwable instanceof HttpException) {
                        HttpException httpException = (HttpException) throwable;

                        if (httpException.code() == 400)
                            showMessage(R.string.response_code_400);
                        else if (httpException.code() == 500)
                            showMessage(R.string.response_code_500);
                    }
                });
    }

    private void getComments(boolean isSendComment) {
        ApiUtils.getApiService(false).getComments(mAlbum.getId())
                .subscribeOn(Schedulers.io())
                .doOnSuccess(comments -> getMusicDao().insertComments(comments))
                .onErrorReturn(throwable -> {
                    if(ApiUtils.NETWORK_EXCEPTIONS.contains(throwable.getClass())) {
                        return getMusicDao().getCommentsByAlbumId(mAlbum.getId());
                    } else return null;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> mRefresher.setRefreshing(true))
                .doFinally(() -> mRefresher.setRefreshing(false))

                .subscribe(
                        comments -> {

                            mErrorView.setVisibility(View.GONE);
                            mRecyclerView.setVisibility(View.VISIBLE);
                            mCommentsAdapter.addData(comments, true);

                            if(mIsFirstLoadComments)
                                mIsFirstLoadComments = false;
                            else if(mCommentsCount == mCommentsAdapter.getItemCount())
                                showMessage(R.string.no_new_comments);
                            else
                                showMessage(R.string.comments_update);

                            if(isSendComment)
                                mRecyclerView.smoothScrollToPosition(mRecyclerView.getAdapter().getItemCount() - 1);
                        },
                        throwable -> {
                            mErrorView.setVisibility(View.VISIBLE);
                            mRecyclerView.setVisibility(View.GONE);
                        });

    }

    private MusicDao getMusicDao() {
        return ((App) getActivity().getApplication()).getDatabase().getMusicDao();
    }

    private void showMessage(@StringRes int string) {
        Toast.makeText(getActivity(), string, Toast.LENGTH_LONG).show();
    }
}
