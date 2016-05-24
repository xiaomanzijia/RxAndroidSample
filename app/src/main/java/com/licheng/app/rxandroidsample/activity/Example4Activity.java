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
import com.licheng.app.rxandroidsample.data.Page;
import com.licheng.app.rxandroidsample.R;
import com.licheng.app.rxandroidsample.common.ServerHelper;

import java.util.ArrayList;
import java.util.List;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;

/**
 * RxAndroid异步加载示例
 */
public class Example4Activity extends Activity {

    private RecyclerView recyclerview;
    private BeautyAdapter adapter;
    private ProgressBar progressbar;

    private PublishSubject<Page> mPublishSubject;
    private Subscription mSubscription;

    private Page page;


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
        createObserver();

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
                        page.setPageIndex(pageIndex);
                        mPublishSubject.onNext(page);
                    }
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mSubscription != null && !mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }
    }

    private void createObserver() {
        page = new Page(pageIndex,pageSize);
        mPublishSubject = PublishSubject.create();
        mSubscription = mPublishSubject
                .observeOn(Schedulers.io())
                .map(new Func1<Page, List<Beauty>>() {
                    @Override
                    public List<Beauty> call(Page page) {
                        List<Beauty> list = ServerHelper.getBeautyList(page.getPageIndex(),page.getPageSize());
                        if(list.size() == pageSize){
                            pageIndex ++;
                            isLastPage = false;
                        }else if(list.size() < pageSize){
                            isLastPage = true;
                        }
                        return list;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Beauty>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<Beauty> beautyList) {
                        displayRecyclerView(beautyList);
                    }
                });
        mPublishSubject.onNext(page);
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
        linearLayoutManager = new LinearLayoutManager(Example4Activity.this, LinearLayoutManager.VERTICAL,false);
        recyclerview.setLayoutManager(linearLayoutManager);
        adapter = new BeautyAdapter(beautyList,Example4Activity.this);
        recyclerview.setAdapter(adapter);

        recyclerview.addOnItemTouchListener(new OnRecyclerViewClickListener(recyclerview) {
            @Override
            protected void onItemClick(RecyclerView.ViewHolder viewHolder) {
                Toast.makeText(Example4Activity.this,viewHolder.getLayoutPosition()+"",Toast.LENGTH_SHORT).show();
            }
        });

    }

}
