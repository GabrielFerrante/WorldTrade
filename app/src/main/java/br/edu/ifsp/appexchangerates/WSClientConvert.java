package br.edu.ifsp.appexchangerates;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.renderscript.Double3;
import android.util.JsonReader;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class WSClientConvert extends AsyncTask <String,Void,Double> {
    private final static String BASE = "https://openexchangerates.org/api/latest.json";
    private final static String KEY = "d6c0fcc6818e49338537ca30c3a2d2a8";
    public final static String KEY_INTENT = "resultado";
    private Context context;


    public WSClientConvert(Context context){
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
    protected Double doInBackground(String... strings) {
        ArrayList<String> conversaoDolar = new ArrayList<String>();
        Double result=null;
        URL url ;
        try{
            if(strings[1].split(" ")[0].equals(strings[2].split(" ")[0])){
                return 1 * Double.parseDouble(strings[0]);
            }
            Thread.sleep(1000);
            url = new URL(BASE+"?app_id="+KEY+"&show_alternative=1");
            HttpURLConnection conn;
            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(10000); //10 segundos de timeout
            conn.setReadTimeout(10000); //10 segundos de timeout
            conn.setRequestMethod("GET");
            conn.connect();
            InputStream myInputStream = new BufferedInputStream(conn.getInputStream());
            if(myInputStream != null) {

                conversaoDolar = executeJsonParser(myInputStream);
                Double de = 0.0;
                Double para =0.0;

                for (String item:conversaoDolar) {

                    item = item.replace(":"," ");

                    if(item.split(" ")[0].equals(strings[1].split(" ")[0])){
                        de = Double.parseDouble(item.split(" ")[1]);

                    }else if(item.split(" ")[0].equals(strings[2].split(" ")[0])){
                        para = Double.parseDouble(item.split(" ")[1]);

                    }

                }

                 if(strings[1].split(" ")[0].equals("USD")){
                    result = para * Double.parseDouble(strings[0]);
                }else{
                    double base = (1/de);
                    double intermediario = Double.parseDouble(strings[0]) * base;
                    result = (intermediario * para);

                }

            }

        }catch (IOException | InterruptedException e){
            e.printStackTrace();
        }
        return result;
    }
    @Override
    protected void onPostExecute(Double resultado) {
        super.onPostExecute(resultado);
        Intent intent = new Intent(context,ResultadoActivity.class);
        intent.putExtra(KEY_INTENT, resultado);
        context.startActivity(intent);

    }

    private ArrayList<String> executeJsonParser(InputStream myInputStream) throws IOException{
        ArrayList<String> conversoes = new ArrayList<String>();
        Reader reader = new InputStreamReader(myInputStream,  "UTF-8");
        JsonReader jsonReader = new JsonReader(reader);
        jsonReader.beginObject();
        while (jsonReader.hasNext()) {
            String name = jsonReader.nextName();
            if(name.equals("rates")){
                    conversoes = readValue(jsonReader);
            }else{
                jsonReader.skipValue();
            }

        }

        jsonReader.endObject();
        jsonReader.close();


        return conversoes;

    }
    public ArrayList<String> readValue(JsonReader reader) throws IOException {
        ArrayList<String> valores = new ArrayList<String>();
        reader.beginObject();
        while (reader.hasNext()) {
            valores.add(reader.nextName() + ":" + String.valueOf(reader.nextDouble()));
        }
        reader.endObject();
        return valores;
    }

}
