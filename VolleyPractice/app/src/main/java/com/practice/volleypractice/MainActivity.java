package com.practice.volleypractice;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    private TextView text;
    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        text = (TextView) findViewById(R.id.main_text);
        btn = (Button) findViewById(R.id.request_btn);
        final RequestQueue rq = VolleyPractice.getInstance(getApplicationContext()).getRequestQueue();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JsonObjectRequest jsonRQ = new JsonObjectRequest(Request.Method.GET,
                        "https://randomuser.me/api/?result=10",
                        new JSONObject(),
                        networkSuccessListener(),
                        networkErrorListener());
                rq.add(jsonRQ);
            }
        });
    }
    private Response.Listener<JSONObject> networkSuccessListener() {
        return new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                String from_server = null;
                try {
                    Log.i("Volley Request Success", response.getString("results"));
                    from_server = response.getString("results");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                text.setText(from_server);
            }
        };
    }

    private Response.ErrorListener networkErrorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Network Error", Toast.LENGTH_SHORT).show();
            }
        };
    }
}