package com.bowoon.android.android_http_practice;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.bowoon.android.android_http_practice.common.HttpCallback;
import com.bowoon.android.android_http_practice.common.HttpService;
import com.bowoon.android.android_http_practice.common.HttpServiceFactory;
import com.bowoon.android.android_http_practice.common.ItemClickListener;
import com.bowoon.android.android_http_practice.common.RecyclerAdapter;
import com.bowoon.android.android_http_practice.model.Person;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        // retrofit은 사용하기가 간단하고 OkHttp와 함께 사용하면 활용 범위가 더욱 커짐
        // 어노테이션을 사용해 헤더, 파라메터 및 멀티파트데이터를 쉽게 전송가능
        // 이미지와 영상과 같은 데이터를 전송하려면 retrofit을 사용하는게 편할거 같다
        HttpService retrofit = HttpServiceFactory.createClass("retrofit");
        retrofit.getPerson(new HttpCallback() {
            @Override
            public void onSuccess(final Object o) {
                if (o instanceof Person) {
                    ItemClickListener listener = new ItemClickListener() {
                        @Override
                        public void onItemClick(int position) {
                            Toast.makeText(getApplicationContext(), ((Person) o).getItems().get(position).getName().getFirst(), Toast.LENGTH_SHORT).show();
                        }
                    };
                    RecyclerAdapter adapter = new RecyclerAdapter(((Person) o).getItems(), listener);
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onFail() {

            }
        });

        // 간단한 텍스트 데이터를 전송하기에 적합한 라이브러리
        // 헤더와 파라메터를 전송하려면 따로 오버라이드해서 추가해야함
        // 멀티파트데이터도 따로 오버라이딩해야 하는데 쉽지 않음
        // volley를 사용하기 위해서는 Context가 필요
//        HttpService volley = HttpServiceFactory.createClass("volley", getApplicationContext());
//        volley.getPerson(new HttpCallback() {
//            @Override
//            public void onSuccess(final Object o) {
//                if (o instanceof Person) {
//                    ItemClickListener listener = new ItemClickListener() {
//                        @Override
//                        public void onItemClick(int position) {
//                            Toast.makeText(getApplicationContext(), ((Person) o).getItems().get(position).getName().getFirst(), Toast.LENGTH_SHORT).show();
//                        }
//                    };
//                    RecyclerAdapter adapter = new RecyclerAdapter(((Person) o).getItems(), listener);
//                    recyclerView.setAdapter(adapter);
//                }
//            }
//
//            @Override
//            public void onFail() {
//
//            }
//        });

        // OkHttp를 단독으로 사용하려면 따로 스레드 처리를 하여 UI업데이트를 해야함
        // retrofit과 함께 사용하는게 더욱 편하다
//        HttpService okHttp = HttpServiceFactory.createClass("okhttp");
//        okHttp.getPerson(new HttpCallback() {
//            @Override
//            public void onSuccess(final Object o) {
//                if (o instanceof Person) {
//                    ItemClickListener listener = new ItemClickListener() {
//                        @Override
//                        public void onItemClick(int position) {
//                            Toast.makeText(getApplicationContext(), ((Person) o).getItems().get(position).getName().getFirst(), Toast.LENGTH_SHORT).show();
//                        }
//                    };
//                    final RecyclerAdapter adapter = new RecyclerAdapter(((Person) o).getItems(), listener);
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            recyclerView.setAdapter(adapter);
//                        }
//                    });
//                }
//            }
//
//            @Override
//            public void onFail() {
//                Log.d("error", "OkHttp");
//            }
//        });
    }
}
