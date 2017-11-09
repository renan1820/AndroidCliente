package com.example.renan.cliente.Util;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Hermanos 04 on 26/05/2017.
 */

public class JSONfunctions {
    public static String getJSONfromURL(String entrada) {
        String result = null;
        JSONObject jArray = null;
        try {
            URL myurl = new URL(entrada);
            HttpURLConnection urlConnection = (HttpURLConnection) myurl.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoInput(true);
            urlConnection.connect();
            InputStream is=urlConnection.getInputStream();
            if (is != null) {
                StringBuilder sb = new StringBuilder();
                String line;
                try {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                    }
                    reader.close();
                } finally {
                    is.close();
                }
                result = sb.toString();
            }
        }catch (Exception e){
            result = e.getMessage();
        }
        return result;
    }
}