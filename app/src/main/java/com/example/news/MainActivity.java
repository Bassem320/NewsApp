package com.example.news;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;

import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity  {



    private ProgressBar loadingPB;
    private ArrayList<Articles>articlesArrayList;

    private NewsRVAdapter newsRVAdapter;




    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView newsRV = findViewById(R.id.idRVNews);
        loadingPB = findViewById(R.id.idPBLoading);
        articlesArrayList = new ArrayList<>();
        newsRVAdapter = new NewsRVAdapter(articlesArrayList,this);
        newsRV.setLayoutManager(new LinearLayoutManager(this));
        newsRV.setAdapter(newsRVAdapter);
        getNews();
        newsRVAdapter.notifyDataSetChanged();


    }

         @SuppressLint("NotifyDataSetChanged")

         private void getNews(){
        loadingPB.setVisibility(View.VISIBLE);
        articlesArrayList.clear();
        String url ="https://newsapi.org/v2/top-headlines?country=us&excludeDomains=stackoverflow.com&sortBy=pubishedAt&language=en&apikey=b3c590d6fbd54abbbf479769da1a185a";
        String BASE_URL ="https://newsapi.org/";

        Retrofit retrofit =new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);
             Call<NewsModel> call;

                 call = retrofitAPI.getAllNews(url);

             call.enqueue(new Callback<NewsModel>() {
                 @SuppressLint("NotifyDataSetChanged")
                 @Override
                 public void onResponse(@NonNull Call<NewsModel> call, @NonNull Response<NewsModel> response) {
                     NewsModel newsModel = response.body();
                     loadingPB.setVisibility(View.GONE);
                     ArrayList<Articles> articles = null;
                     if (newsModel != null) {
                         articles = newsModel.getArticles();
                     }
                     if (articles != null) {
                         for (int i=0 ; i<articles.size(); i++){
                             articlesArrayList.add(new Articles(articles.get(i).getTitle(),articles.get(i).getDescription(),articles.get(i).getUrlToImage(),articles.get(i).getUrl(),articles.get(i).getContent()));

                         }
                     }
                     newsRVAdapter.notifyDataSetChanged();
                 }



                 @Override
                 public void onFailure(@NonNull Call<NewsModel> call, @NonNull Throwable t) {
                     Toast.makeText(MainActivity.this,"Fail to get news",Toast.LENGTH_SHORT).show();
                 }
             });


    }


}