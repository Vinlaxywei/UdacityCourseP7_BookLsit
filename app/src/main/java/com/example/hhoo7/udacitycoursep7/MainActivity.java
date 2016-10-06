package com.example.hhoo7.udacitycoursep7;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private final String LOG_TAG = MainActivity.class.getSimpleName();
    private ListView mListView;
    private BookListAdapter mAdapter;
    private EditText mQueryView;
    private ImageButton btn_Search;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mQueryView = (EditText) findViewById(R.id.query_edit_view);
        btn_Search = (ImageButton) findViewById(R.id.btn_seatch_view);

        mAdapter = new BookListAdapter(this, new ArrayList<BookClass>());
        mListView = (ListView) findViewById(R.id.book_list_view);
        mListView.setAdapter(mAdapter);

        btn_Search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String param = mQueryView.getText().toString();
                if (param.length() == 0) {
                    Toast.makeText(getApplicationContext(),
                            getString(R.string.search_view_hint), Toast.LENGTH_SHORT).show();
                } else {
                    DataTask dataTask = new DataTask();
                    dataTask.execute(param);
                    Log.d(LOG_TAG, "onClick: " + param);
                }
            }
        });

    }

    public class DataTask extends AsyncTask<String, Void, List<BookClass>> {

        @Override
        protected List<BookClass> doInBackground(String... param) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String queryParam = param[0];
            String maxresult = "10";

            try {
                // baseUrl = https://www.googleapis.com/books/v1/volumes?q=Android
                final String FORECAST_BASE_URL = "https://www.googleapis.com/books/v1/volumes?";
                final String QUERY_PARAM = "q";
                final String MAXRESULTS_PARAN = "maxResults";

                Uri builtUri = Uri.parse(FORECAST_BASE_URL).buildUpon()
                        .appendQueryParameter(QUERY_PARAM, queryParam)
                        .appendQueryParameter(MAXRESULTS_PARAN, maxresult)
                        .build();
                URL url = new URL(builtUri.toString());

                // 打开Http连接
                urlConnection = (HttpURLConnection) url.openConnection();
                // 设定请求参数
                urlConnection.setRequestMethod("GET");
                urlConnection.setReadTimeout(10000);
                urlConnection.setConnectTimeout(15000);
                // 发送请求
                urlConnection.connect();

                if (urlConnection.getResponseCode() == 200) {
                    // 获取请求
                    InputStream inputStream = urlConnection.getInputStream();
                    // 将流数据转化
                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                    reader = new BufferedReader(inputStreamReader);

                    // 读取流数据
                    StringBuilder builder = new StringBuilder();
                    String line = reader.readLine();
                    while (line != null) {
                        builder.append(line);
                        line = reader.readLine();
                    }

                    // 将读取到的最终数据转化成字符串
                    return getDataFromJson(builder.toString());
                } else {
                    Log.d(LOG_TAG, "doInBackground: ResponseCode incorrect"
                            + urlConnection.getResponseCode());
                    return null;
                }
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error,IOException", e);
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(List<BookClass> results) {
            mAdapter.clear();
            if (results.size() != 0) {
                 mAdapter.addAll(results);
                Toast.makeText(getApplicationContext(), R.string.loading_done, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), getString(R.string.display_no_result), Toast.LENGTH_SHORT).show();
            }
        }

        private List<BookClass> getDataFromJson(String jsonStr) {
            List<BookClass> results = new ArrayList<BookClass>();

            try {
                JSONObject primaryJson = new JSONObject(jsonStr);
                if (primaryJson.getInt("totalItems") > 0) {
                    JSONArray itemArray = primaryJson.getJSONArray("items");

                    for (int i = 0; i < itemArray.length(); i++) {
                        JSONObject item = itemArray.getJSONObject(i);
                        JSONObject volumeInfo = item.optJSONObject("volumeInfo");
                        String title = volumeInfo.optString("title");

                        JSONArray authors = volumeInfo.optJSONArray("authors");
                        String author = " ";
                        if (authors != null) {
                            for (int j = 0; j < authors.length() && j < 3; j++) {
                                author += authors.getString(j) + "  ";
                            }
                        } else {
                            author = getString(R.string.incorrect_author);
                        }

                        String publisher = volumeInfo.optString("publisher");
                        String publishedDate = volumeInfo.optString("publishedDate");

                        results.add(new BookClass(title, author, publisher, publishedDate));
                    }
                }
            } catch (JSONException e) {
                Log.d(LOG_TAG, "getDataFromJson: Error,JSONException");
                e.printStackTrace();
            }

            return results;
        }

    }


}
