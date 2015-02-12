package org.cryse.lkong.presenter;

import org.cryse.lkong.logic.LKongForumService;
import org.cryse.lkong.model.DataItemLocationModel;
import org.cryse.lkong.model.PostModel;
import org.cryse.lkong.model.ThreadInfoModel;
import org.cryse.lkong.utils.LKAuthObject;
import org.cryse.lkong.utils.SubscriptionUtils;
import org.cryse.lkong.view.PostListView;

import java.util.List;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

public class PostListPresenter implements BasePresenter<PostListView> {
    public static final String LOG_TAG = PostListPresenter.class.getName();
    LKongForumService mLKongForumService;
    PostListView mView;
    Subscription mLoadPostListSubscription;
    Subscription mLoadThreadInfoSubscription;
    Subscription mAddOrRemoveFavoriteSubscription;
    Subscription mDataItemLocationSubscription;

    @Inject
    public PostListPresenter(LKongForumService forumService) {
        this.mLKongForumService = forumService;
    }

    public void getPostLocation(LKAuthObject authObject, long pid) {
        SubscriptionUtils.checkAndUnsubscribe(mDataItemLocationSubscription);
        mView.setLoading(true);
        mDataItemLocationSubscription = mLKongForumService.getPostIdLocation(authObject, pid)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        result -> {
                            Timber.d("PostListPresenter::loadThreadInfo() onNext().", LOG_TAG);
                            mView.onGetPostLocationComplete(result);
                        },
                        error -> {
                            Timber.e(error, "PostListPresenter::loadThreadInfo() onError().", LOG_TAG);
                            mView.setLoading(false);
                        },
                        () -> {
                            Timber.d("PostListPresenter::loadThreadInfo() onComplete().", LOG_TAG);
                        }
                );
    }

    public void loadThreadInfo(LKAuthObject authObject, long tid) {
        SubscriptionUtils.checkAndUnsubscribe(mLoadThreadInfoSubscription);
        mView.setLoading(true);
        mLoadThreadInfoSubscription = mLKongForumService.getThreadInfo(authObject, tid)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        result -> {
                            Timber.d("PostListPresenter::loadThreadInfo() onNext().", LOG_TAG);
                            mView.onLoadThreadInfoComplete(result);
                        },
                        error -> {
                            Timber.e(error, "PostListPresenter::loadThreadInfo() onError().", LOG_TAG);
                            mView.setLoading(false);
                        },
                        () -> {
                            Timber.d("PostListPresenter::loadThreadInfo() onComplete().", LOG_TAG);
                        }
                );
    }

    public void loadPostList(LKAuthObject authObject, long tid, int page, boolean refreshPosition) {
        SubscriptionUtils.checkAndUnsubscribe(mLoadPostListSubscription);
        mView.setLoading(true);
        mLoadPostListSubscription = mLKongForumService.getPostList(authObject, tid, page)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        result -> {
                            Timber.d("PostListPresenter::loadPostList() onNext().", LOG_TAG);
                            mView.showPostList(page, result, refreshPosition);
                        },
                        error -> {
                            Timber.e(error, "PostListPresenter::loadPostList() onError().", LOG_TAG);
                            mView.setLoading(false);
                        },
                        () -> {
                            Timber.d("PostListPresenter::loadPostList() onComplete().", LOG_TAG);
                            mView.setLoading(false);
                        }
                );
    }

    public void addOrRemoveFavorite(LKAuthObject authObject, long tid, boolean remove) {
        SubscriptionUtils.checkAndUnsubscribe(mAddOrRemoveFavoriteSubscription);
        mAddOrRemoveFavoriteSubscription = mLKongForumService.addOrRemoveFavorite(authObject, tid, remove)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        result -> {
                            Timber.d("PostListPresenter::addOrRemoveFavorite() onNext().", LOG_TAG);
                            mView.onAddOrRemoveFavoriteComplete(result);
                        },
                        error -> {
                            Timber.e(error, "PostListPresenter::addOrRemoveFavorite() onError().", LOG_TAG);
                        },
                        () -> {
                            Timber.d("PostListPresenter::addOrRemoveFavorite() onComplete().", LOG_TAG);
                        }
                );
    }

    @Override
    public void bindView(PostListView view) {
        this.mView = view;
    }

    @Override
    public void unbindView() {
        this.mView = new EmptyPostListView();
    }

    @Override
    public void destroy() {
        SubscriptionUtils.checkAndUnsubscribe(mLoadPostListSubscription);
        SubscriptionUtils.checkAndUnsubscribe(mLoadThreadInfoSubscription);
        SubscriptionUtils.checkAndUnsubscribe(mAddOrRemoveFavoriteSubscription);
        SubscriptionUtils.checkAndUnsubscribe(mDataItemLocationSubscription);
    }

    private class EmptyPostListView implements PostListView {
        @Override
        public void showPostList(int page, List<PostModel> posts, boolean refreshPosition) {

        }

        @Override
        public void onGetPostLocationComplete(DataItemLocationModel locationModel) {

        }

        @Override
        public void onLoadThreadInfoComplete(ThreadInfoModel threadInfoModel) {

        }

        @Override
        public void onAddOrRemoveFavoriteComplete(boolean isFavorite) {

        }

        @Override
        public void setLoading(Boolean value) {

        }

        @Override
        public Boolean isLoading() {
            return null;
        }

        @Override
        public void showToast(int text_value, int toastType) {

        }
    }
}
