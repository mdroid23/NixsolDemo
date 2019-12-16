package com.nixsol.mahesh.ui.home;

import android.support.annotation.NonNull;

import com.nixsol.mahesh.common.api.ApiInterface;
import com.nixsol.mahesh.model.response.FactResponse;

import rx.Observer;
import rx.Scheduler;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

public class HomePresenter implements HomeContract.Presenter {

    @NonNull
    private ApiInterface charactersDataSource;
    @NonNull
    private Scheduler backgroundScheduler;
    @NonNull
    private Scheduler mainScheduler;
    @NonNull
    private CompositeSubscription subscriptions;

    private HomeContract.View view;
    private ApiInterface apiInterface;


    public HomePresenter(HomeContract.View view, Scheduler mainScheduler,
                         Scheduler backgroundScheduler, @NonNull ApiInterface apiInterface) {

        this.view = view;
        this.mainScheduler = mainScheduler;
        this.backgroundScheduler = backgroundScheduler;
        this.apiInterface = apiInterface;
        subscriptions = new CompositeSubscription();
    }

    @Override
    public void loadDataFromApi() {
        subscriptions.clear();
        Subscription subscription = apiInterface
                .getFacts()
                .subscribeOn(backgroundScheduler)
                .observeOn(mainScheduler)
                .subscribe(new Observer<FactResponse>() {
                    @Override
                    public void onCompleted() {
                        view.onGetDataCompleted();
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.onGetDataError(e);
                    }

                    @Override
                    public void onNext(FactResponse response) {
                        view.onGetDataSuccess(response);
                    }
                });

        subscriptions.add(subscription);
    }

    @Override
    public void subscribe() {
        loadDataFromApi();
    }

    @Override
    public void unsubscribe() {
        subscriptions.clear();
    }

    @Override
    public void onDestroy() {
        this.view = null;
    }
}
