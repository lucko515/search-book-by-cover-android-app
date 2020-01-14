package com.laimhe.bookworm.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.laimhe.bookworm.R;
import com.laimhe.bookworm.adapters.CatalogueViewAdapter;
import com.laimhe.bookworm.helpers.ImageUtils;
import com.laimhe.bookworm.helpers.MyConstants;
import com.laimhe.bookworm.models.Book;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;


/**
 * Activity that holds all retrieved book information
 */
public class CatalogueActivity extends AppCompatActivity {

    //Define UI elements
    private ProgressBar pgsBar;
    private RecyclerView searchResults;

    //Define Recycler View custom adapter
    CatalogueViewAdapter adapter = null;
    //Define holder for all products
    Map<String, Book> resultBooks;

    LinearLayoutManager linearLayoutManager = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalogue);


        //Setup RecyclerView that holds all of our results
        linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        searchResults = findViewById(R.id.result_list);
        searchResults.setLayoutManager(linearLayoutManager);
        searchResults.setHasFixedSize(true);


        pgsBar = findViewById(R.id.pBar);

        //Call the AI book scanner
        coverAIScanner();
    }

    /**
     * Call this function to perform the inference
     */
    private void coverAIScanner(){

        //Create Request body that holds image in the Byte format
        RequestBody postBodyImage = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("image", "resulting_image.jpg", RequestBody.create(MediaType.parse("image/*jpg"), ImageUtils.imageToBytes(MyConstants.selectedImageBitmap)))
                .build();


        //Create the OkHttpClient and open it for 60 seconds, because we are uploading image and internet might be slow
        OkHttpClient client;
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(60, TimeUnit.SECONDS);
        builder.readTimeout(60, TimeUnit.SECONDS);
        builder.writeTimeout(60, TimeUnit.SECONDS);
        client = builder.build();

        //Create the POST Request
        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(getString(R.string.server_ip) + getString(R.string.cover_search_endpoint))
                .post(postBodyImage)
                .build();

        //Handle Response from the server
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                call.cancel();

                // In order to access the TextView inside the UI thread, the code is executed inside runOnUiThread()
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Failed to Connect to Server", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, final okhttp3.Response response) {

                Thread thread = new Thread(new Runnable() {

                    @Override
                    public void run() {
                        try {

                            JSONObject jsonResponse = new JSONObject(response.body().string().replaceAll("NaN", "-1"));

                            resultBooks = new HashMap<>();

                            //Iterate through all books retrieved from the server and populate the RecyclerView Adapter with that information
                            for (Iterator<String> it = jsonResponse.keys(); it.hasNext(); ) {

                                String key = it.next();
                                JSONObject snapshot = jsonResponse.getJSONObject(key);
                                Book product = new Book();

                                String isbn = snapshot.getString("isbn");

                                if (!resultBooks.containsKey(isbn)) {
                                    //Create product object from the FireBase by extracting each element from the data snapshot
                                    product.setAuthor(snapshot.getString("author"));
                                    product.setCategory(snapshot.getString("category"));
                                    product.setFormat(snapshot.getString("format"));
                                    product.setName(snapshot.getString("name"));
                                    product.setIsbn(snapshot.getString("isbn"));
                                    product.setImage(snapshot.getString("image"));

                                    resultBooks.put(isbn, product);
                                }else{
                                    Book p = resultBooks.get(isbn);

                                    assert p != null;
                                    //ArrayList<Store> stores = p.getStores();
                                    resultBooks.put(isbn, p);
                                }
                            }

                            ArrayList<Book> books = new ArrayList<>(resultBooks.values());
                            adapter = new CatalogueViewAdapter(CatalogueActivity.this, books);

                            //NOTE: Make sure to set adapter to the RecyclerView on the UI Thread!
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    searchResults.setAdapter(adapter);
                                    pgsBar.setVisibility(View.GONE);
                                }
                            });

                        } catch (JSONException | IOException e) {
                            e.printStackTrace();
                        }

                    }
                });
                thread.start();
            }
        });
    }
}
