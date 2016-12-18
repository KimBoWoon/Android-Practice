package com.bowoon.unit_test;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Created by 보운 on 2016-12-18.
 */
public class MainPresenterTest {
    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void sumButtonClick() throws Exception {
        MainPresenter mp = mock(MainPresenter.class);
        mp.sumButtonClick(any(Integer.class), any(Integer.class));
        verify(mp).sumButtonClick(any(Integer.class), any(Integer.class));
    }
}