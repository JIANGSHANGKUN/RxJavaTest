package com.apps.android.news.rxjava20test;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Completable;
import io.reactivex.CompletableEmitter;
import io.reactivex.CompletableObserver;
import io.reactivex.CompletableOnSubscribe;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Maybe;
import io.reactivex.MaybeEmitter;
import io.reactivex.MaybeObserver;
import io.reactivex.MaybeOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleObserver;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "--";
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView) findViewById(R.id.tv);

        Button test1_btn = (Button) findViewById(R.id.test1_btn);
        Button test2_btn = (Button) findViewById(R.id.test2_btn);
        test1_btn.setOnClickListener(this);
        test2_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.test1_btn:
                ObservableTest();
                break;
            case R.id.test2_btn:
//                FlowableTest();
                MaybeTest();
                break;
        }
    }

    private void ObservableTest() {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                e.onNext("ObservableTest");
                e.onComplete();
            }
        }).subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.i(TAG, "Observer onSubscribe: " + d.isDisposed());
            }

            @Override
            public void onNext(String value) {
                Log.i(TAG, "Observer onNext: " + value);
            }

            @Override
            public void onError(Throwable e) {
                Log.i(TAG, "Observer onNext: " + e.getMessage().toString());
            }

            @Override
            public void onComplete() {
                Log.i(TAG, "Observer ----onComplete----");
            }
        });
    }

    private void FlowableTest() {
        Flowable.create(new FlowableOnSubscribe<String>() {
            @Override
            public void subscribe(FlowableEmitter<String> e) throws Exception {
                e.onNext("FlowableTest");
            }
        }, BackpressureStrategy.MISSING)
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        Log.i(TAG, "Subscriber onSubscribe: " + s.toString());

                    }

                    @Override
                    public void onNext(String s) {
                        Log.i(TAG, "Subscriber onNext: " + s);
                    }

                    @Override
                    public void onError(Throwable t) {
                        Log.i(TAG, "Subscriber onError: " + t.getMessage().toString());
                    }

                    @Override
                    public void onComplete() {
                        Log.i(TAG, "Subscriber onComplete: ");
                    }
                });
    }

    private void MaybeTest() {
        Maybe.create(new MaybeOnSubscribe<String>() {
            @Override
            public void subscribe(MaybeEmitter<String> e) throws Exception {
                Thread.sleep(5000);
                e.onSuccess("");
            }
        })
                .subscribeOn(Schedulers.newThread())
                .subscribe(new MaybeObserver<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.i(TAG, "MaybeObserver onSubscribe: ");
                    }

                    @Override
                    public void onSuccess(String value) {
                        Log.i(TAG, "MaybeObserver onSuccess: " + value);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i(TAG, "MaybeObserver onError: " + e.getMessage().toString());
                    }

                    @Override
                    public void onComplete() {
                        Log.i(TAG, "MaybeObserver onComplete: ");
                    }
                });
    }

    private void SingleTest() {
        Single.create(new SingleOnSubscribe<String>() {
            @Override
            public void subscribe(SingleEmitter<String> e) throws Exception {

            }
        }).subscribe(new SingleObserver<String>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onSuccess(String value) {

            }

            @Override
            public void onError(Throwable e) {

            }
        });
    }

    private void CompletedTest() {
        Completable.create(new CompletableOnSubscribe() {
            @Override
            public void subscribe(CompletableEmitter e) throws Exception {

            }
        }).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onComplete() {

            }

            @Override
            public void onError(Throwable e) {

            }
        });
    }

    private void MayBeJust(){
        Maybe.just(getBoolean()).subscribe(new MaybeObserver<Boolean>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onSuccess(Boolean value) {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }
    
    private void test3(){
        Maybe.create(new MaybeOnSubscribe<String>() {
            @Override
            public void subscribe(MaybeEmitter<String> e) throws Exception {

            }
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {

            }
        });
    }

    private boolean getBoolean(){
        return false;
    }

}
