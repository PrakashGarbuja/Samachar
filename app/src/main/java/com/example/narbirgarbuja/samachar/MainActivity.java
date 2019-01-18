package com.example.narbirgarbuja.samachar;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ArrayList<Samachar> newsList = new ArrayList<>();
    RecyclerView viewRecycle;
    Adapt adapt = new Adapt();
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewRecycle = (RecyclerView)findViewById(R.id.rviewNews);
        getData();
        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipetorefresh);
        swipeRefreshLayout.setColorSchemeColors(Color.parseColor("#00BCD4"),Color.parseColor("#FF9800"),Color.parseColor("#339a60"));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (newsList.size()>0){
                    newsList.clear();
                    adapt.notifyDataSetChanged();
                    getData();
                }
            }
        });
    }

    public void getData(){
        StringRequest stringRequest = new StringRequest("http://baatoo.com.np/php/request.php?newsdata=1",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                String title = jsonArray.getJSONObject(i).getString("blogtitle");
                                String blog = jsonArray.getJSONObject(i).getString("blog");
                                String publisher = jsonArray.getJSONObject(i).getString("publishedby");
                                String pubdate = jsonArray.getJSONObject(i).getString("date");
                                String imageurl = jsonArray.getJSONObject(i).getString("imglink");

                                newsList.add(new Samachar(title, blog, publisher, pubdate, imageurl));
                            }
                            viewRecycle.setAdapter(new Adapt());
                            viewRecycle.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                            swipeRefreshLayout.setRefreshing(false);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                    Log.e("--<<",""+error);
                Toast.makeText(MainActivity.this, "Connection Timeout.", Toast.LENGTH_SHORT).show();
            }
        });
        Volley.newRequestQueue(this).add(stringRequest);
    }


    public class Adapt extends RecyclerView.Adapter<Adapt.vholder>{
        @NonNull
        @Override
        public vholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View supportView = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_layout,parent,false);
            return new vholder(supportView);
        }

        @Override
        public void onBindViewHolder(@NonNull vholder holder, int position) {
            final String a=newsList.get(position).getBlogtitle1();
            if (a.length()>40) {
                holder.blogtitle.setText(a.substring(0,40)+".....");
            }
            else{
                holder.blogtitle.setText(a);
            }
            final String s = newsList.get(position).getBlog1();
            if (s.length()>200 ) {
                holder.blog.setText(s.substring(0,200)+".....");
            }
            else{
                holder.blog.setText(s);
            }
            final String d = newsList.get(position).getPublisheddate1();
            final String p = newsList.get(position).getPublishedby1();
            holder.publishedby.setText(p+", "+d);
            final String i = newsList.get(position).getImage();
            if (i.equals("null")) {
                holder.imamge.setVisibility(View.GONE);
            }
            else{
                Glide.with(MainActivity.this).load(newsList.get(position).getImage()).into(holder.imamge);
            }
            holder.toDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
                    intent.putExtra("title",a);
                    intent.putExtra("blog",s);
                    intent.putExtra("image",i);
                    intent.putExtra("publisher",p);
                    intent.putExtra("pdate",d);
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return newsList.size();
        }

        public class vholder extends RecyclerView.ViewHolder{
            TextView blogtitle;
            com.codesgood.views.JustifiedTextView blog;
            TextView publishedby;
            ImageView imamge;
            View toDetail;

            public vholder(View itemView) {
                super(itemView);
                blogtitle = (TextView)itemView.findViewById(R.id.txtBlogtitle);
                blog = (com.codesgood.views.JustifiedTextView)itemView.findViewById(R.id.txtBlog);
                publishedby = (TextView)itemView.findViewById(R.id.txtPublish);
                imamge = (ImageView)itemView.findViewById(R.id.imageShot);
                toDetail = (View)itemView.findViewById(R.id.itemtouch);
            }
        }
    }
    public class Samachar{
        String blogtitle1;
        String blog1;
        String publisheddate1;
        String publishedby1;
        String image;
        public Samachar(String blogtitle, String blog, String publishedby, String publisheddate, String imageurl){
            this.blogtitle1 = blogtitle;
            this.blog1 = blog;
            this.publishedby1 = publishedby;
            this.publisheddate1 = publisheddate;
            this.image = imageurl;
        }
        public String getBlogtitle1() {return blogtitle1;}
        public String getBlog1() {return blog1;}
        public String getPublisheddate1() { return publisheddate1;}
        public String getPublishedby1() {return  publishedby1;}
        public String getImage() {return  image;}
    }


}
