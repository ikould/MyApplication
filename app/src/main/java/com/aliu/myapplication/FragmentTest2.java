package com.aliu.myapplication;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.ikould.frame.fragment.BaseFragment;

/**
 * describe
 *
 * @author ikould on 2017/10/31.
 */

public class FragmentTest2 extends BaseFragment {

    @Override
    protected void onBaseCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setBaseContentView(R.layout.fragment_test2);
    }
}
