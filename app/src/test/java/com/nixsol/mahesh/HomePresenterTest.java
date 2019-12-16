package com.nixsol.mahesh;

import com.nixsol.mahesh.common.api.ApiInterface;
import com.nixsol.mahesh.model.response.Fact;
import com.nixsol.mahesh.model.response.FactResponse;
import com.nixsol.mahesh.ui.home.HomeContract;
import com.nixsol.mahesh.ui.home.HomePresenter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;

import rx.Observable;
import rx.schedulers.Schedulers;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class HomePresenterTest {

    @Mock
    ApiInterface apiInterface;
    @Mock
    HomeContract.View view;

    @Before
    public void create() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void loadValidDataIntoAdapter() {

        ArrayList<Fact> factArrayList = new ArrayList<>();
        factArrayList.add(new Fact("Transportation","Better Transportation",null));

        FactResponse factResponse = new FactResponse("Canada", factArrayList);

        when(apiInterface.getFacts())
                .thenReturn(Observable.just(factResponse));

        HomePresenter presenter = new HomePresenter(this.view, Schedulers.immediate(),
                Schedulers.immediate(), apiInterface);

        presenter.loadDataFromApi();

        InOrder inOrder = Mockito.inOrder(view);
        inOrder.verify(view, times(1)).onGetDataSuccess(factResponse);
        inOrder.verify(view, times(1)).onGetDataCompleted();
    }

    @Test
    public void checkErrorShouldReturnErrorToView() {

        Exception exception = new Exception();

        when(apiInterface.getFacts())
                .thenReturn(Observable.<FactResponse>error(exception));

        HomePresenter presenter = new HomePresenter(this.view, Schedulers.immediate(),
                Schedulers.immediate(), apiInterface);

        presenter.loadDataFromApi();

        InOrder inOrder = Mockito.inOrder(view);
        inOrder.verify(view, times(1)).onGetDataError(exception);
        verify(view, never()).onGetDataCompleted();
    }

}
