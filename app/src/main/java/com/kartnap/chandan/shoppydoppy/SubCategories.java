package com.kartnap.chandan.shoppydoppy;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class SubCategories extends AppCompatActivity {
    private String rootCategory;
    public String subCatUrl;
    private RecyclerView mRecyclerView;
    private RecyclerViewAdapter adapter;
    private List<FeedItems> feedsList;
    ProgressBar progressBar;
    private RecyclerView.LayoutManager mLayoutManager;
    //private GridLayoutManager gridLayoutManager;
    private StaggeredGridLayoutManager staggeredGridLayoutManager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_categories);
        // Toast.makeText(getApplicationContext(),"Hello Sub Cat",Toast.LENGTH_LONG).show();
        //SharedPreferences mPrefs = getSharedPreferences("cat",MODE_PRIVATE);
        //rootCategory = mPrefs.getString("title", "");
        rootCategory = getIntent().getStringExtra("title");
       rootCategory= rootCategory.replace(" ","%20");
        subCatUrl = "http://kart.webvalleytech.com/web.asmx/getSubCat?category=" + rootCategory;
        Toast.makeText(getApplicationContext(),subCatUrl,Toast.LENGTH_LONG).show();

        setTitle(rootCategory);
        mRecyclerView =(RecyclerView)findViewById(R.id.subCatRecyclerView);
        progressBar = (ProgressBar)findViewById(R.id.subCat_progress_bar);
        new DownloadSubTask().execute(subCatUrl);
        // mLayoutManager = new LinearLayoutManager(v.getContext());
        staggeredGridLayoutManager=new StaggeredGridLayoutManager(2,1);
        // gridLayoutManager=new GridLayoutManager(v.getContext(),2);
        mRecyclerView.setLayoutManager(staggeredGridLayoutManager);
    }

    private class DownloadSubTask extends AsyncTask<String, Void, Integer> {
        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Integer doInBackground(String... params) {
            Integer result = 0;
            HttpURLConnection urlConnection;
            try {
                URL url = new URL(params[0]);
                urlConnection = (HttpURLConnection)url.openConnection();
                int statusCode = urlConnection.getResponseCode();
                if (statusCode == 200){
                    BufferedReader r = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = r.readLine()) != null ){
                        response.append(line);
                    }

                    parseResult(response.toString());
                    result = 1;
                }else {
                    result = 0;
                }


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            progressBar.setVisibility(View.GONE);
            if (integer == 1){
                adapter = new RecyclerViewAdapter(getApplicationContext(),feedsList);


                mRecyclerView.setAdapter(adapter);
                adapter.setOnItemClickLIstener(new OnItemClickLIstener() {
                    @Override
                    public void OnItemClick(FeedItems feedItem) {
                        //Toast.makeText(getContext(),"Item clicked"+feedItem.getTitle(),Toast.LENGTH_LONG).show();
                        Intent in = new Intent(getApplicationContext(),ProductsList.class);
                        in.putExtra("title",feedItem.getTitle());
                        startActivity(in);

                    }
                });
                // Toast.makeText(getContext(),""+adapter.getItemCount(),Toast.LENGTH_LONG).show();

            }else {
                Toast.makeText(getApplicationContext(),"Data Fetching failed",Toast.LENGTH_LONG).show();
            }
        }
    }

    private void parseResult(String s) throws JSONException {
        JSONObject response=new JSONObject(s);
        JSONArray result=response.optJSONArray("Result");
        feedsList = new ArrayList<>();
        for (int i =0;i<result.length();i++){
            JSONObject product = result.optJSONObject(i);
            FeedItems item = new FeedItems();
            item.setTitle(product.optString("subcatName"));
            //item.setPrice(product.optInt("mrp"));
            //item.setImage(R.drawable.images /**"http://kart.webvalleytech.com/pics/"+product.getString("img")*/);
            feedsList.add(item);

        }


    }
}
