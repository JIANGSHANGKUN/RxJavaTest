package com.apps.android.news.rxjavatest;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.ActionN;
import rx.functions.Func1;
import rx.schedulers.Schedulers;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "-Rx-";

    private Observer observer;
    private Subscriber subscriber;
    private Observable observableCreate;
    private Observable observableJust;
    private Observable observableFrom;
    private TextView textView;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;

        Button subscribeBtn = (Button) findViewById(R.id.subscribe_btn);
        Button customBtn = (Button) findViewById(R.id.custom_btn);
        Button example1_btn = (Button) findViewById(R.id.example1_btn);
        Button schedules_btn = (Button) findViewById(R.id.schedules_btn);
        Button transfer2_btn = (Button) findViewById(R.id.transfer2_btn);
        Button schedules2_btn = (Button) findViewById(R.id.schedules2_btn);
        Button test1_btn = (Button) findViewById(R.id.test1_btn);
        subscribeBtn.setOnClickListener(this);
        customBtn.setOnClickListener(this);
        example1_btn.setOnClickListener(this);
        schedules_btn.setOnClickListener(this);
        transfer2_btn.setOnClickListener(this);
        schedules2_btn.setOnClickListener(this);
        test1_btn.setOnClickListener(this);

        textView = (TextView) findViewById(R.id.tv);

        createObserver();
        createObservable();

        custom();

    }

    /**
     * 自定义，查看源码:
     * <p>
     * return subscribe(new Subscriber<T>() {
     *
     * @Override public final void onCompleted() {
     * onComplete.call();
     * }
     */
    private void custom() {
        //因此 Action0 可以被当成一个包装对象，将 onCompleted() 的内容打包起来将自己作为一个参数传入 subscribe() 以实现不完整定义的回调。
        //这样其实也可以看做将 onCompleted() 方法作为参数传进了 subscribe()，相当于其他某些语言中的『闭包』。
        Action0 actionComplete = new Action0() {
            @Override
            public void call() {
                Log.i(TAG, "call: actionComplete");
            }
        };

        Action1<String> action1 = new Action1<String>() {
            @Override
            public void call(String s) {
                Log.i(TAG, "call: action1  " + s);
            }
        };

        Action1<Throwable> actionError = new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                Log.i(TAG, "call: actionError:" + throwable.getMessage().toString());
            }
        };


        ActionN actionN = new ActionN() {
            @Override
            public void call(Object... args) {
                Log.i(TAG, "call: actionN  " + args[0].toString() + "  " + args[1].toString());
            }
        };

        observableJust.subscribe(action1, actionError, actionComplete);
    }

    /**
     * 2.将创建Observable
     */
    private void createObservable() {
        //2.创建Observable
        //被观察者
        //在这之中可以调用观察者的方法，一旦observable被订阅，call将会立即被调用，里面的语句将会一次被执行
        observableCreate = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                //在这之中可以调用观察者的方法，一旦observable被订阅，call将会立即被调用，里面的语句将会一次被执行
                subscriber.onNext("observableCreate");
                subscriber.onCompleted();

            }
        });
        //create为RxJava最基本的创建事件序列的方法

        //还有其他方法
        //just将传入的参数一次传递出去
        observableJust = Observable.just("observableJust");

        //Observable将传入的数组或者Iterable拆分成具体的对象，然后一次发送出去
        String[] strings = new String[]{"observableFrom"};
        observableFrom = Observable.from(strings);
    }

    /**
     * 1.创建Observer
     */
    private void createObserver() {
        //1.创建Observer
        observer = new Observer<String>() {
            @Override
            public void onCompleted() {
                Log.i(TAG, "Observer- onCompleted: ");
            }

            @Override
            public void onError(Throwable e) {
                Log.i(TAG, "Observer- onError: " + e.getMessage().toString());
            }

            @Override
            public void onNext(String s) {
                Log.i(TAG, "Observer- onNext: " + s);
            }
        };

        //Subscriber为创建Observer的扩展，基本用法相同,实际上RxJava在subscribe时，创建Observer会被先转化为Subscriber
        subscriber = new Subscriber<String>() {
            @Override
            public void onCompleted() {
                Log.i(TAG, "Subscriber- onCompleted: ");
            }

            @Override
            public void onError(Throwable e) {
                Log.i(TAG, "Subscriber- onError: " + e.getMessage().toString());
            }

            @Override
            public void onNext(String s) {
                Log.i(TAG, "Subscriber- onNext: " + s);
            }
        };
    }

    /**
     * 订阅,将观察者observer和被观察者Observable进行绑定
     */
    private void subscribe() {
//        observableCreate.subscribe(subscriber);
//        observableCreate.subscribe(observer);
//
        observableJust.subscribe(subscriber);
        observableJust.subscribe(observer);

//        observableFrom.subscribe(subscriber);
//        observableFrom.subscribe(observer);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.subscribe_btn:
                subscribe();
                break;
            case R.id.custom_btn:
                custom();
                break;
            case R.id.example1_btn:
                example1();
                break;
            case R.id.schedules_btn:
                schedulerTest();
                break;
            case R.id.transfer2_btn:
                transferTest2();
                break;
            case R.id.schedules2_btn:

                break;
            case R.id.test1_btn:
                test1();
                break;
        }
    }


    /**
     * 简单例子，将创建Observable，Observer以及将其串联起来
     * 但是RxJava默认事件的发出和消费都是在同一线程中的，
     * 所以这里只是一个同步的观察者模式
     */
    private void example1() {
        String[] strings = {"111", "222", "333", "444"};

        Observable.from(strings).subscribe(new Observer<String>() {
            @Override
            public void onCompleted() {
                Log.i(TAG, "example1 onCompleted: ");
            }

            @Override
            public void onError(Throwable e) {
                Log.i(TAG, "example1 onError: " + e.getMessage().toString());
            }

            @Override
            public void onNext(String s) {
                Log.i(TAG, "example1 onNext: " + s);
            }
        });
    }

    /**
     * Rxjava在不指定线程的情况下， RxJava 遵循的是线程不变的原则，
     * 即：在哪个线程调用 subscribe()，就在哪个线程生产事件；在哪个线程生产事件，就在哪个线程消费事件。
     * 如果需要切换线程，就需要用到 Scheduler （调度器）。
     * subscribeOn只能调用一次位置随意，observerOn可以多次调用改变其后执行的线程
     * 有多个subscribeOn时，只有第一个有作用
     * Subscriber的onStart在subscribe时，发送事件前调用，可用于流程开始前的初始化。因此不能能指定线程
     * Observable.doOnSubscribe 与onStart相似，不同的是doOnSubscribe可已指定线程，其受后面最近的subscribeOn的影响
     */
    private void schedulerTest() {
        Log.i(TAG, "------schedulerTest: -------------");
        //直接在当前线程运行，相当于不指定线程，默认
//        Schedulers.immediate();
        //总是启用新线程，并在新线程执行操作。
//        Schedulers.newThread();
        //I/O 操作（读写文件、读写数据库、网络信息交互等）所使用的 Scheduler。
        //行为模式和 newThread() 差不多，区别在于 io() 的内部实现是是用一个无数量上限的线程池，可以重用空闲的线程，因此多数情况下 io() 比 newThread() 更有效率。
        //不要把计算工作放在 io() 中，可以避免创建不必要的线程。
//        Schedulers.io();
        //计算所使用的 Scheduler。
        //这个计算指的是 CPU 密集型计算，即不会被 I/O 等操作限制性能的操作，例如图形的计算。这个 Scheduler 使用的固定的线程池，大小为 CPU 核数。
        //不要把 I/O 操作放在 computation() 中，否则 I/O 操作的等待时间会浪费 CPU。
//        Schedulers.computation();
        //在 Android 主线程运行
//        AndroidSchedulers.mainThread();

        Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {

                subscriber.onNext("Observable");
//                                textView.setText("Subscriber");
                Log.i(TAG, "call: ");
            }
        })
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {

                    }
                })
                .subscribeOn(Schedulers.immediate())//指定事件产生的线程，事件在该线程发出
                .observeOn(AndroidSchedulers.mainThread())//指定事件消费的线程
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i(TAG, "onError: " + e.getMessage().toString());
                    }

                    @Override
                    public void onNext(String s) {
                        textView.setText(s);
                    }
                });
    }

    /**
     * map
     */
    private void transferTest1() {

        //这里的变换，通过just中的参数触发Func1,根据Integer型的参数获得相应的Bitmap类型，
        //new Func1<>中左侧为原型，右侧为目标类型，调用的call传入的为原型，return的为目标类型
        // 然后将该类型的结果发送到事件处理进行
        //
        Observable.just(1).map(new Func1<Integer, Bitmap>() {
            @Override
            public Bitmap call(Integer s) {

                return null;
            }
        }).subscribe(new Action1<Bitmap>() {
            @Override
            public void call(Bitmap s) {

            }
        });
    }

    private void transferTest2() {

        //打印学生名字
//        Student[] students = {new Student("小明"),new Student("小刚"),new Student("小红")};
//        Observable.from(students).map(new Func1<Student, String>() {
//            @Override
//            public String call(Student student) {
//                return student.getName();
//            }
//        }).subscribe(new Action1<String>() {
//            @Override
//            public void call(String studentName) {
//                Log.i(TAG, "StudentName: "+studentName);
//            }
//        });


        //打印课程名字
        Course course1 = new Course("语文");
        Course course2 = new Course("数学");
        Course course3 = new Course("英语");
        Course course4 = new Course("化学");
        Course course5 = new Course("物理");
        Course course6 = new Course("政治");
        List<Course> courses1 = new ArrayList<>();
        courses1.add(course1);
        courses1.add(course2);
        List<Course> courses2 = new ArrayList<>();
        courses2.add(course3);
        courses2.add(course4);
        List<Course> courses3 = new ArrayList<>();
        courses3.add(course5);
        courses3.add(course6);

        Student[] students = {new Student("小明", courses1), new Student("小刚", courses2), new Student("小红", courses3)};

//        Subscriber<Student> subscriber = new Subscriber<Student>() {
//            @Override
//            public void onCompleted() {
//
//            }
//
//            @Override
//            public void onError(Throwable e) {
//
//            }
//
//            @Override
//            public void onNext(Student student) {
//                List<Course> courses = student.getCourses();
//                for (int i = 0; i < courses.size(); i++) {
//                    Log.i(TAG, student.getName()+" courses: "+courses.get(i).getName());
//                }
//
//            }
//        };
//        Observable.from(students).subscribe(subscriber);

        Subscriber<Course> subscriber = new Subscriber<Course>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Course course) {
                Log.i(TAG, "onNext: " + course.getName());
            }
        };


//        Observable.from(students)
//                .flatMap(new Func1<Student, Observable<Course>>() {
//                    @Override
//                    public Observable<Course> call(Student student) {
//                        return Observable.from(student.getCourses());
//                    }
//                })

//        从上面的代码可以看出， flatMap() 和 map() 有一个相同点：它也是把传入的参数转化之后返回另一个对象。
//        但需要注意，和 map() 不同的是， flatMap() 中返回的是个 Observable 对象，
//        并且这个 Observable 对象并不是被直接发送到了 Subscriber 的回调方法中。
//        flatMap() 的原理是这样的：
//        1. 使用传入的事件对象创建一个 Observable 对象；
//        2. 并不发送这个 Observable, 而是将它激活，于是它开始发送事件；
//        3. 每一个创建出来的 Observable 发送的事件，都被汇入同一个 Observable ，而这个 Observable 负责将这些事件统一交给 Subscriber 的回调方法。
//        这三个步骤，把事件拆成了两级，通过一组新创建的 Observable 将初始的对象『铺平』之后通过统一路径分发了下去。而这个『铺平』就是 flatMap() 所谓的 flat。

        Observable.from(students).flatMap(new Func1<Student, Observable<Course>>() {
            @Override
            public Observable<Course> call(Student student) {
                Log.i(TAG, "call: " + student.getName() + ":");
                return Observable.from(student.getCourses());
            }
        }).subscribe(subscriber);
    }

//    private void ChangeThreadTest() {
//        Observable.create(new Observable.OnSubscribe<String>() {
//            @Override
//            public void call(Subscriber<? super String> subscriber) {
//                subscriber.onNext("create call");
//                textView.setText("");
//                String threadName = getCurProcessName(context);
//                Log.i(TAG, "createc call: "+threadName);
//            }
//        })
//                .subscribeOn(AndroidSchedulers.mainThread())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Subscriber<String>() {
//                    @Override
//                    public void onCompleted() {
//                        Log.i(TAG, "ChangeThreadTest onCompleted: ");
//                        String threadName = getCurProcessName(context);
//                        Log.i(TAG, "subscribe call: "+threadName);
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        Log.i(TAG, "ChangeThreadTest onError: " + e.getMessage().toString());
//                        String threadName = getCurProcessName(context);
//                        Log.i(TAG, "subscribe call: "+threadName);
//                    }
//
//                    @Override
//                    public void onNext(String s) {
//                        Log.i(TAG, "ChangeThreadTest onNext: " + s);
//                        String threadName = getCurProcessName(context);
//                        Log.i(TAG, "subscribe call: "+threadName);
//                    }
//                });
//    }
//
//
//    private String getCurProcessName(Context context) {
//        int pid = android.os.Process.myPid();
//        ActivityManager mActivityManager = (ActivityManager) context
//                .getSystemService(Context.ACTIVITY_SERVICE);
//        for (ActivityManager.RunningAppProcessInfo appProcess : mActivityManager
//                .getRunningAppProcesses()) {
//            if (appProcess.pid == pid) {
//                return appProcess.processName;
//            }
//        }
//        return null;
//    }

    private void test() {
        Observable.just("On", "Off", "On", "On")
                //这就是在传递过程中对事件进行过滤操作
                .filter(new Func1<String, Boolean>() {
                    @Override
                    public Boolean call(String s) {
                        return s != null;
                    }
                })
                //实现订阅
                .onBackpressureBuffer()
                .subscribe(
                        //创建观察者，作为事件传递的终点处理事件
                        new Subscriber<String>() {
                            @Override
                            public void onStart() {
                                request(1);
                            }

                            @Override
                            public void onCompleted() {
                                Log.d("DDDDDD", "结束观察...\n");
                            }

                            @Override
                            public void onError(Throwable e) {
                                //出现错误会调用这个方法
                            }

                            @Override
                            public void onNext(String s) {
                                //处理事件
                                Log.d("DDDDD", "handle this---" + s);
                                request(1);
                            }
                        });
    }

    private void test1() {
//        Observable.interval(1, TimeUnit.MILLISECONDS)
//                .subscribeOn(Schedulers.newThread())
//                .observeOn(Schedulers.newThread())
//                .subscribe(new Subscriber<Long>() {
//                    @Override
//                    public void onCompleted() {
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//
//                    }
//
//                    @Override
//                    public void onNext(Long aLong) {
//                        try {
//                            Thread.sleep(1000);
//                            Log.i(TAG, "onNext: "+aLong);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                });

        Observable.interval(1, TimeUnit.MILLISECONDS)
                .onBackpressureDrop()
//                .onBackpressureBuffer()
                .observeOn(Schedulers.newThread())
                .subscribe(new Subscriber<Long>() {

                    @Override
                    public void onStart() {
                        Log.w("TAG", "start");
//                        request(1);
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("ERROR", e.toString());
                    }

                    @Override
                    public void onNext(Long aLong) {
                        Log.w("TAG", "---->" + aLong);
//                        try {
//                            Thread.sleep(100);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
                    }
                });
    }

}
