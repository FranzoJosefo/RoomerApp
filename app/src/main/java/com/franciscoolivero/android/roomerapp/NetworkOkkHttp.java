//package com.franciscoolivero.android.roomerapp;
//
//import android.os.Bundle;
//import android.util.Log;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.io.IOException;
//
//import androidx.appcompat.app.AppCompatActivity;
//import okhttp3.Call;
//import okhttp3.Callback;
//import okhttp3.MediaType;
//import okhttp3.OkHttpClient;
//import okhttp3.Request;
//import okhttp3.RequestBody;
//import okhttp3.Response;
//
//public class Network extends AppCompatActivity {
//
//    public String postUrl= "https://reqres.in/api/users/";
//    public String postBody="{\n" +
//            "    \"name\": \"morpheus\",\n" +
//            "    \"job\": \"leader\"\n" +
//            "}";
//
//    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        try {
//            postRequest(postUrl,postBody);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void postRequest(String postUrl,String postBody) throws IOException {
//
//        OkHttpClient client = new OkHttpClient();
//
//        RequestBody body = RequestBody.create(JSON, postBody);
//
//        Request request = new Request.Builder()
//                .url(postUrl)
//                .post(body)
//                .build();
//
//        client.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                call.cancel();
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                Log.d("TAG",response.body().string());
//                final String myResponse = response.body().string();
//
//                MainActivity.this.runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        try {
//
//                            JSONObject json = new JSONObject(myResponse);
//                            txtString.setText(json.getJSONObject("data").getString("first_name") + " " + json.getJSONObject("data").getString("last_name"));
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                });
//            }
//        });
//    }
//}
