package com.nixsol.mahesh.ui.home;

import com.nixsol.mahesh.common.ui.BasePresenter;
import com.nixsol.mahesh.common.ui.BaseView;
import com.nixsol.mahesh.model.response.FactResponse;

public class HomeContract {

    public interface View extends BaseView {
        void onGetDataCompleted();

        void onGetDataSuccess(FactResponse factResponse);

        void onGetDataError(Throwable e);

    }

    public interface Presenter extends BasePresenter {

        void loadDataFromApi();

        void subscribe();

        void unsubscribe();
    }
}
