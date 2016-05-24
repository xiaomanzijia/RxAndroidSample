package com.licheng.app.rxandroidsample.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;


import com.licheng.app.rxandroidsample.data.Beauty;
import com.licheng.app.rxandroidsample.adapter.BeautyAdapter;
import com.licheng.app.rxandroidsample.common.OnRecyclerViewClickListener;
import com.licheng.app.rxandroidsample.R;
import com.licheng.app.rxandroidsample.common.ServerHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * RxAndroid异步加载示例
 */
public class Example2Activity extends Activity {

    private RecyclerView recyclerview;
    private BeautyAdapter adapter;
    private ProgressBar progressbar;

    private Subscription mSubcription;
    private Observable<List<Beauty>> observerable;


    private List<Beauty> beautyList = new ArrayList<>();
    private int pageIndex = 1;
    private int pageSize = 10;
    private boolean isLastPage = false;
    private int lastVisibleItem = 0;
    private LinearLayoutManager linearLayoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        configureLayout();
        createObserver(pageIndex,pageSize);

        recyclerview.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE
                        && lastVisibleItem + 1 == adapter.getItemCount()) {
                    //根据类目网络请求数据
                    if(!isLastPage){
                        createObserver(pageIndex,pageSize);
                    }
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mSubcription != null && !mSubcription.isUnsubscribed()) {
            mSubcription.unsubscribe();
        }
    }

    private void createObserver(final int index, final int size) {
        //请求网络数据
        observerable = Observable.fromCallable(new Callable<List<Beauty>>() {
            @Override
            public List<Beauty> call() throws Exception {
                List<Beauty> list = ServerHelper.getBeautyList(index,size);
                if(list.size() == pageSize){
                    pageIndex ++;
                    isLastPage = false;
                }else if(list.size() < pageSize){
                    isLastPage = true;
                }
                return list;
            }
        });

        mSubcription = observerable
                //配置线程运行
                .subscribeOn(Schedulers.io())
                //配置在UI主线程观察
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Beauty>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<Beauty> list) {
                        displayRecyclerView(list);
                    }
                });

    }

    private void displayRecyclerView(List<Beauty> list) {
        beautyList.addAll(list);
        progressbar.setVisibility(View.GONE);
        adapter.notifyDataSetChanged();
    }

    private void configureLayout() {
        setContentView(R.layout.activity1_rxandroid);
        progressbar = (ProgressBar) findViewById(R.id.loader);
        recyclerview = (RecyclerView) findViewById(R.id.recyleview);
        linearLayoutManager = new LinearLayoutManager(Example2Activity.this, LinearLayoutManager.VERTICAL,false);
        recyclerview.setLayoutManager(linearLayoutManager);
        adapter = new BeautyAdapter(beautyList,Example2Activity.this);
        recyclerview.setAdapter(adapter);

        recyclerview.addOnItemTouchListener(new OnRecyclerViewClickListener(recyclerview) {
            @Override
            protected void onItemClick(RecyclerView.ViewHolder viewHolder) {
                Toast.makeText(Example2Activity.this,viewHolder.getLayoutPosition()+"",Toast.LENGTH_SHORT).show();
            }
        });

    }

}
