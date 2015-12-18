package com.example.raj.pro1;

import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Config;
import android.util.Log;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AllProductsActivity extends ListActivity {

    //Declaring the progress Dialog
    private ProgressDialog pDialog;



    //Declaring the json string myJSON is the string where we will store the JSON string from server
    String myJSON;
    JSONObject jObj;
    //Declaring the final json string tags
    private static final String TAG_RESULT = "result";
    private static final String TAG_PRODUCTS = "products";
    private static final String TAG_PID = "pid";
    private static final String TAG_NAME = "name";

    //Declaring the array list
    ArrayList<HashMap<String, String>> productsList;

    //Declaring the json array products
    JSONArray products = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_products);

        //We have initialized our list object and personList.
        ListView lv=getListView();
        productsList = new ArrayList<HashMap<String, String>>();
        new GetDataJSON().execute();

    }
    String url = "http://192.168.1.3/project/get_all_products.php";
    class GetDataJSON extends AsyncTask<String, Void, String> {
        InputStream is = null;
            @Override
            protected String doInBackground(String... strings) {

        /*        try{
                    URL url = new URL("http://192.168.1.3/project/get_all_products.php");
                    HttpURLConnection con = (HttpURLConnection)url.openConnection();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }*/

             List<NameValuePair> params = new ArrayList<NameValuePair>();
                try{
                    DefaultHttpClient httpClient = new DefaultHttpClient();
                    String paramString = URLEncodedUtils.format(params, "utf-8");
                    url += "?" + paramString;
                    HttpGet httpGet = new HttpGet(url);

                    HttpResponse httpResponse = httpClient.execute(httpGet);
                    HttpEntity httpEntity = httpResponse.getEntity();
                    is = httpEntity.getContent();

            } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

                try {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(
                            is, "iso-8859-1"), 8);
                    StringBuilder sb = new StringBuilder();
                    String line = null;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    is.close();
                    myJSON = sb.toString();
                    Log.d("buffer",myJSON);

                } catch (Exception e) {
                    Log.e("Buffer Error", "Error converting result " + e.toString());
                }


                // try parse the string to a JSON obj

            //    DefaultHttpClient httpclient = new DefaultHttpClient(new BasicHttpParams());
             /*   HttpPost httppost = new HttpPost("http://192.168.1.3/project/get_all_products.php"); //Doubt

                InputStream inputStream = null;
                String result = null;
                try {
                    HttpResponse response = httpclient.execute(httppost);
                    HttpEntity entity = response.getEntity();

                    inputStream = entity.getContent();
                    // json is UTF-8 by default
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
                    StringBuilder sb = new StringBuilder();

                    String line = null;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    result = sb.toString();
                    myJSON=result;
                    Log.d("sb",result);
                } catch (Exception e) {
                    // Oops
                    Log.d("sdas","catch");
                } finally {
                    try {
                        if (inputStream != null) inputStream.close();
                        Log.d("finally","final");
                    } catch (Exception squish) {
                    }
                }*/
                // try parse the string to a JSON object
            /*    try {
                    jObj = new JSONObject(myJSON);
                } catch (JSONException e) {
                    Log.e("JSON Parser", "Error parsing data " + e.toString());
                }*/



                return myJSON;

            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                pDialog = new ProgressDialog(AllProductsActivity.this);
                pDialog.setMessage("Loading products. Please wait...");
                pDialog.setIndeterminate(false);
                pDialog.setCancelable(false);
                pDialog.show();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                // dismiss the dialog after getting all products
                Log.d("inpost", "inpost ");
                pDialog.dismiss();
                myJSON = s;

                try {
                    JSONObject jo = new JSONObject(myJSON);
                    products=jo.getJSONArray(TAG_PRODUCTS);

                    for(int i = 0; i<products.length(); i++){
                        JSONObject c = products.getJSONObject(i);
                        String id = c.getString(TAG_PID);
                        String name = c.getString(TAG_NAME);

                        HashMap<String,String> employees = new HashMap<>();
                        employees.put(TAG_PID, id);
                        employees.put(TAG_NAME, name);
                        productsList.add(employees);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                runOnUiThread(new Runnable() {
                    public void run() {
                        /**
                         * Updating parsed JSON data into ListView
                         * */
                        ListAdapter adapter = new SimpleAdapter(
                                AllProductsActivity.this, productsList,
                                R.layout.list_item, new String[]{TAG_PID,
                                TAG_NAME},
                                new int[]{R.id.id, R.id.name});
                        Log.d("list", adapter.toString());
                        setListAdapter(adapter);


                    }


                });
            }
        }
    }
