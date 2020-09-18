package br.edu.ifsp.appexchangerates;

import android.content.Context;
import android.os.AsyncTask;
import android.util.JsonReader;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class WSClientBuscaMoedas extends AsyncTask<String,Void,ArrayList<String>> {

    private Context context;


    public WSClientBuscaMoedas(Context context){
        this.context = context;

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();


    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected ArrayList<String> doInBackground(String... strings) {
        ArrayList<String> siglas = new ArrayList<String>();
        URL url ;
            try {
                Thread.sleep(2000);
                String sUrl = strings[0];
                url = new URL(sUrl);
                HttpURLConnection conn;
                conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(10000); //10 segundos de timeout
                conn.setReadTimeout(10000); //10 segundos de timeout
                conn.setRequestMethod("GET");
                conn.connect();

                InputStream myInputStream = new BufferedInputStream(conn.getInputStream());
                if(myInputStream != null) {
                    siglas = executeJsonParser(myInputStream);

                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        return siglas;

    }
    @Override
    protected void onPostExecute(ArrayList<String> string) {
        super.onPostExecute(string);
        ((MainActivity)context).popularSpinner(string);

    }

    private ArrayList<String> executeJsonParser(InputStream myInputStream) throws IOException {

        ArrayList<String> siglas = new ArrayList<String>();
        Reader reader = new InputStreamReader(myInputStream,  "UTF-8");
        JsonReader jsonReader = new JsonReader(reader);

        jsonReader.beginObject();

        while (jsonReader.hasNext()) {
            siglas.add(jsonReader.nextName() +" : "+jsonReader.nextString());
        }

        jsonReader.endObject();
        jsonReader.close();

        return siglas;
    }
}
