package com.apps.android.news.rxjavatest;

import rx.Subscriber;

/**
 * Created by android on 2017/3/9.
 */

public class MySubscribe<T> extends Subscriber<T>{

    @Override
    public void onStart() {
        super.onStart();
        request(1);
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void onNext(Object o) {

    }
}
