package com.aliu.myapplication;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.ikould.frame.activity.BaseActivity;
import com.ikould.frame.fragment.BaseFragment;

/**
 * describe
 *
 * @author ikould on 2017/10/31.
 */

public class FragmentTest1 extends BaseFragment {
    @Override
    protected void onBaseCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setBaseContentView(R.layout.fragment_test1);
    }
}
