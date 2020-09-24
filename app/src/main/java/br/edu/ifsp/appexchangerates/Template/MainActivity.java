package br.edu.ifsp.appexchangerates.Template;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

import br.edu.ifsp.appexchangerates.AsyncTasks.WSClientBuscaMoedas;
import br.edu.ifsp.appexchangerates.AsyncTasks.WSClientConvert;
import br.edu.ifsp.appexchangerates.R;

public class MainActivity extends AppCompatActivity {
    private Spinner sp1, sp2;
    String de, para;
    EditText valor;
    private static final String OpenExchangeRates_moedas = "https://openexchangerates.org/currencies.json?show_alternative=1";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        valor = findViewById(R.id.txtValor);
        this.sp1 = findViewById(R.id.cbSiglasDe);
        this.sp2 = findViewById(R.id.cbSiglasPara);

        //CHAMA O PROCESSO ASSINCRONO DE BUSCA DAS MOEDAS
        try {
            WSClientBuscaMoedas wsClientBuscaMoedas = new WSClientBuscaMoedas(this);

            wsClientBuscaMoedas.execute(OpenExchangeRates_moedas);
        }catch (Exception e){
            e.printStackTrace();
        }



    }


    public void popularSpinner(ArrayList<String> siglas){

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, siglas);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.sp1.setAdapter(adapter);
        this.sp2 .setAdapter(adapter);

        this.sp1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                de = (String )sp1.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        this.sp2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                para = (String )sp2.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        switch (id){
            case R.id.action_aprender:
                Intent intent = new Intent(MainActivity.this,AprenderActivity.class);
                startActivity(intent);
                break;
            case R.id.action_sair:
                finish();
                break;

            default:
                break;

        }

        return super.onOptionsItemSelected(item);
    }

    //CHAMA O PROCESSO ASSINCRONO DE CONVERSÃO
    public void onClickConverter(View view){
        try {
            if(!valor.getText().toString().equals("")){
                WSClientConvert ws = new WSClientConvert(this);
                ws.execute(valor.getText().toString(), de, para);
            }else{
                Snackbar.make(valor,R.string.valorNaoInserido,Snackbar.LENGTH_SHORT).show();
            }

        }catch (Exception e){
            e.printStackTrace();
            Snackbar.make(valor,R.string.erroConverter,Snackbar.LENGTH_LONG).show();
        }

    }
}
