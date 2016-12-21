package com.bowoon.unit_test;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by 보운 on 2016-12-18.
 */
public class MainPresenterTest {
    @Mock
    MainPresenter mp;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void sumButtonClick() throws Exception {
        when(mp.getInteger(any(Integer.class), any(Integer.class))).thenReturn(any(Integer.class));
        mp.sumButtonClick(any(Integer.class), any(Integer.class));
        verify(mp).sumButtonClick(any(Integer.class), any(Integer.class));
    }

    @Test
    public void getInteger() throws Exception {
        when(mp.getInteger(any(Integer.class), any(Integer.class))).thenReturn(any(Integer.class));
    }
}