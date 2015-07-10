package org.cryse.lkong.ui;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import org.cryse.lkong.R;
import org.cryse.lkong.application.LKongApplication;
import org.cryse.lkong.model.ThreadModel;
import org.cryse.lkong.presenter.UserProfilePresenter;
import org.cryse.lkong.presenter.UserProfileThreadsPresenter;
import org.cryse.lkong.ui.adapter.ThreadListAdapter;
import org.cryse.lkong.utils.LKAuthObject;
import org.cryse.lkong.utils.UIUtils;

import java.util.List;

import javax.inject.Inject;

public class UserProfileThreadsFragment extends SimpleCollectionFragment<
        ThreadModel,
        ThreadListAdapter,
        UserProfileThreadsPresenter> {
    private static final String LOG_TAG = UserProfileThreadsFragment.class.getName();
    private static final String KEY_UID = "key_args_uid";
    private static final String KEY_IS_DIGEST = "key_args_is_digest";
    private static final String LOAD_IMAGE_TASK_TAG = "timeline_load_image_tag";
    private long mUid;
    private boolean mIsDigest;

    @Inject
    UserProfileThreadsPresenter mPresenter;

    public static UserProfileThreadsFragment newInstance(long uid, boolean isDigest) {
        Bundle args = new Bundle();
        args.putLong(KEY_UID, uid);
        args.putBoolean(KEY_IS_DIGEST, isDigest);
        UserProfileThreadsFragment fragment = new UserProfileThreadsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        injectThis();
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if(args != null && args.containsKey(KEY_UID) && args.containsKey(KEY_IS_DIGEST)) {
            mUid = args.getLong(KEY_UID);
            mIsDigest = args.getBoolean(KEY_IS_DIGEST);
        }
        setHasOptionsMenu(false);
    }

    @Override
    protected void injectThis() {
        LKongApplication.get(getActivity()).lKongPresenterComponent().inject(this);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected String getLogTag() {
        return LOG_TAG;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_simple_collection;
    }

    @Override
    protected UserProfileThreadsPresenter getPresenter() {
        return mPresenter;
    }

    @Override
    protected ThreadListAdapter createAdapter(List<ThreadModel> itemList) {
        ThreadListAdapter adapter = new ThreadListAdapter(getActivity(), getPicasso(), mItemList, LOAD_IMAGE_TASK_TAG);
        adapter.setOnThreadItemClickListener(new ThreadListAdapter.OnThreadItemClickListener() {
            @Override
            public void onItemThreadClick(View view, int adapterPosition) {
                int itemIndex = adapterPosition - mCollectionAdapter.getHeaderViewCount();
                if (itemIndex >= 0 && itemIndex < mCollectionAdapter.getItemList().size()) {
                    ThreadModel model = mCollectionAdapter.getItem(itemIndex);
                    String idString = model.getId().substring(7);
                    long tid = Long.parseLong(idString);
                    mAndroidNavigation.openActivityForPostListByThreadId(getActivity(), tid);
                }
            }

            @Override
            public void onProfileAreaClick(View view, int position, long uid) {
                int itemIndex = position - mCollectionAdapter.getHeaderViewCount();
                if (itemIndex >= 0 && itemIndex < mCollectionAdapter.getItemList().size()) {
                    ThreadModel model = mCollectionAdapter.getItem(itemIndex);
                    int[] startingLocation = new int[2];
                    view.getLocationOnScreen(startingLocation);
                    startingLocation[0] += view.getWidth() / 2;
                    mAndroidNavigation.openActivityForUserProfile(getActivity(), startingLocation, model.getUid());
                }
            }
        });
        return adapter;
    }

    @Override
    protected void loadData(LKAuthObject authObject, long start, boolean isLoadingMore, Object... extraArgs) {
        getPresenter().loadUserThreads(authObject, mUid, start, mIsDigest, isLoadingMore);
    }

    @Override
    protected void onItemClick(View view, int position, long id) {
    }

    @Override
    protected SwipeRefreshLayout.OnRefreshListener getRefreshListener() {
        return null;
    }

    @Override
    protected UIUtils.InsetsValue getRecyclerViewInsets() {
        return null;
    }


    @Override
    protected void onCollectionViewInitComplete() {
        super.onCollectionViewInitComplete();
        mCollectionView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    getPicasso().resumeTag(LOAD_IMAGE_TASK_TAG);
                } else {
                    getPicasso().pauseTag(LOAD_IMAGE_TASK_TAG);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }
}