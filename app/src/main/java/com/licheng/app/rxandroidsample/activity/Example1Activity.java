package com.licheng.app.rxandroidsample.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.licheng.app.rxandroidsample.R;
import com.licheng.app.rxandroidsample.adapter.SimpleStringAdapter;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Observer;


/**
 * RxAndroid同步加载示例
 */
public class Example1Activity extends Activity {

    private RecyclerView recyclerview;
    private SimpleStringAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        configureLayout();
        createObservable();
    }

    private void createObservable() {
        Observable<List<String>> observer = Observable.just(getArrays());
        observer.subscribe(new Observer<List<String>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(List<String> strings) {
                adapter.setStrings(strings);
            }
        });
    }

    private void configureLayout() {
        setContentView(R.layout.activity1_rxandroid);
        recyclerview = (RecyclerView) findViewById(R.id.recyleview);
        recyclerview.setLayoutManager(new LinearLayoutManager(Example1Activity.this));
        adapter = new SimpleStringAdapter(Example1Activity.this);
        recyclerview.setAdapter(adapter);
    }

    private static List<String> getArrays(){
        List<String> list = new ArrayList<>();
        list.add("aaaa");
        list.add("bbbb");
        list.add("cccc");
        list.add("dddd");
        return list;
    }


}
