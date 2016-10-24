package com.kimbowoon.android_practice;

import org.junit.After;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by BoWoon on 2016-10-24.
 */
public class MainPresenterTest {
    private MainPresenter mMainPresenter;
    private MainModel mainModel;

    @Mock
    private MainPresenter mainPresenter;

    @Mock
    private MainModel mMainModel;

    @Mock
    private MainContract.View view;

    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        mMainPresenter = new MainPresenter(view);
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void sumButtonClick() throws Exception {
        when(mMainModel.getSumData(1, 2)).thenReturn(3);

        mMainPresenter.sumButtonClick(1, 2);

        verify(mainPresenter).sumButtonClick(1, 2);
    }

}