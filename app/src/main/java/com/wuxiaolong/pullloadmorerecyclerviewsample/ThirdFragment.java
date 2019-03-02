package com.wuxiaolong.pullloadmorerecyclerviewsample;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.GsonBuilder;
import com.wuxiaolong.pullloadmorerecyclerviewsample.http.HomeService;
import com.wuxiaolong.pullloadmorerecyclerviewsample.http.PicBean;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


public class ThirdFragment extends Fragment {

    private static final String TAG = "ThirdFragment";
    private StaggeredRecycleViewAdapter mRecyclerViewAdapter;
    private Retrofit retrofit;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_test, container, false);
    }


    private List<String> list = new ArrayList<>();

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView mPullLoadMoreRecyclerView = (RecyclerView) view.findViewById(R.id.rv_one);
        StaggeredGridLayoutManager staggeredGridLayoutManager =
                new StaggeredGridLayoutManager(2,
                        StaggeredGridLayoutManager.VERTICAL);
        mPullLoadMoreRecyclerView.setLayoutManager(staggeredGridLayoutManager);
        mRecyclerViewAdapter = new StaggeredRecycleViewAdapter(getActivity(), list);
        mPullLoadMoreRecyclerView.setAdapter(mRecyclerViewAdapter);
        requestData();
    }

    private void requestData() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(" http://limming.site")
                    .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().create()))
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
        }
        HomeService homeService = retrofit.create(HomeService.class);
        homeService.getPic().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<PicBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.i(TAG, "onSubscribe");

                    }

                    @Override
                    public void onNext(PicBean picBean) {
                        if (picBean != null) {
                            List<String> pics = picBean.pics;
                            list.addAll(pics);
                            mRecyclerViewAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {

                        Log.i(TAG, "complete");
                    }
                });
    }

}
