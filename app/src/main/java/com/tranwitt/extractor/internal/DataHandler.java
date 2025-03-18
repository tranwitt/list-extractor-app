package com.tranwitt.extractor.internal;

import static java.util.Objects.requireNonNull;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class DataHandler {

    private final String mUrl;

    private HashMap<String, ArrayList<JsonEntry>> mData = new HashMap<>();

    /**
     * A class to handle extracting json data from the web
     * @param mUrl - Url to extract json data from the web
     */
    public DataHandler(String mUrl) {
        this.mUrl = mUrl;
        getJsonData(mUrl);
        SortData();
    }

    /**
     * Initializer for extracting json data
     * @param url - Url string to extract data from
     */
    private void getJsonData(String url){
        URL u = createUrl(url);
        String jsonRes = null;
        try {
            jsonRes = makeHttpRequest(u);
        } catch (IOException e) {
            e.printStackTrace();
        }
        extractFromHttp(jsonRes);
    }

    /**
     * Extracts data taken from the web and builds a map of JsonEntry objects based on listId
     * @param jsonRes - Raw json data from the web
     * @return
     */
    private boolean extractFromHttp(String jsonRes) {
        if (TextUtils.isEmpty(jsonRes)){
            return false;
        }
        try {
            JSONArray root = new JSONArray(jsonRes);
            for (int i = 0 ;i<root.length();i++){
                JSONObject currentObj = root.getJSONObject(i);

                String name = currentObj.optString("name");
                if (name.isEmpty() || name.equals("null")){
                    continue;
                }
                String listId = currentObj.optString("listId");
                String id = currentObj.optString("id");
                if (listId.isEmpty() || id.isEmpty()) {
                    continue;
                }
                JsonEntry entry = new JsonEntry(name, listId, id);
                if (mData.containsKey(entry.getListId())) {
                    requireNonNull(mData.get(entry.getListId())).add(entry);
                }
                else {
                    mData.put(entry.getListId(), new ArrayList<>());
                    requireNonNull(mData.get(entry.getListId())).add(entry);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * Sorts the data based on name
     */
    private void SortData() {
        for (String key : mData.keySet()) {
            mData.get(key).sort((item1, item2) -> item1.getName()
                    .compareToIgnoreCase(item2.getName()));
        }
    }

    private static String makeHttpRequest(URL u) throws IOException {
        HttpURLConnection uc = null;
        InputStream is =null;
        String jsRes = "";
        if (u==null){
            return  jsRes;
        }
        try {
            uc = (HttpURLConnection) u.openConnection();
            uc.setRequestMethod("GET");
            uc.setReadTimeout(1000);
            uc.setConnectTimeout(1500);
            uc.connect();

            if (uc.getResponseCode()==200){
                is = uc.getInputStream();
                jsRes = readFromStream(is);
            }else{
                Log.e("Respond Code","Error respond code :"+uc.getResponseCode());
            }
        }catch (IOException e){
            e.printStackTrace();
            Log.e("Problem JSON ","Error retrieving Json results");
        }finally {
            if (uc !=null){
                uc.disconnect();
            }if (is!=null){
                is.close();
            }
        }
        return jsRes;
    }

    private static String readFromStream(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        if (is!=null){
            InputStreamReader isr =new InputStreamReader(is);
            BufferedReader bfr = new BufferedReader(isr);
            String line = bfr.readLine();
            while (line!=null){
                sb.append(line);
                line=bfr.readLine();
            }
        }
        return sb.toString();
    }

    private static URL createUrl(String url) {
        URL u1 = null;
        try {
            u1 = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return u1;
    }

    public HashMap<String, ArrayList<JsonEntry>> getData() {
        return mData;
    }
}
