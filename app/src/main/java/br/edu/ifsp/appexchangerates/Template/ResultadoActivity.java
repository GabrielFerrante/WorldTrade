package br.edu.ifsp.appexchangerates.Template;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import br.edu.ifsp.appexchangerates.AsyncTasks.WSClientConvert;
import br.edu.ifsp.appexchangerates.Model.SharedPreferencesMethods;
import br.edu.ifsp.appexchangerates.R;

public class ResultadoActivity extends AppCompatActivity {


    private String para,de,quantia;

    TextView valor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultado);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        de = intent.getStringExtra(WSClientConvert.KEY_INTENT_DE);
        para = intent.getStringExtra(WSClientConvert.KEY_INTENT_PARA);
        quantia = intent.getStringExtra(WSClientConvert.KEY_INTENT_QUANTIA);

        valor = findViewById(R.id.lblValor) ;

        Locale locale;
        NumberFormat brnf;
        if(Locale.getDefault().toString().equals("pt_BR")){
            locale = Locale.forLanguageTag("pt-BR");
            brnf = NumberFormat.getInstance(locale);
        }else{
            locale = Locale.forLanguageTag("en-US");
            brnf = NumberFormat.getInstance(locale);
        }


        valor.setText("$: "+brnf.format(intent.getDoubleExtra(WSClientConvert.KEY_INTENT_RESULTADO,0))+ "\n"+para);


      
    }



    public void onClickCompartilharResultado(View v){
        if(Locale.getDefault().toString().equals("pt_BR")){
            enviarMensagem("Olá "
                    + "\nA conversão da moeda: " + de
                    + "\nPara a moeda: "+ para
                    +"\nDe valor: "+quantia
                    +"\nTeve como resultado: "+valor.getText());
        }else{
            enviarMensagem("Hi "
                    + "\nThe Coin conversion: " + de
                    + "\nFor currency: "+ para
                    +"\nValue: "+quantia
                    +"\nAs a result: "+valor.getText());
        }

    }
    public void onClickNovamente(View v){

        finish();
    }
    private void enviarMensagem(String texto){
        try {

            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, texto);
            sendIntent.setType("text/plain");
            // Verifica se há algum pacote com intentFilter para essa ação
            PackageManager packageManager = getPackageManager();
            List<ResolveInfo> activities = packageManager.queryIntentActivities(sendIntent, 0);
            boolean isIntentSafe = activities.size() > 0;
            if (isIntentSafe) {
                String title = getResources().getString(R.string.titulo);

                Intent chooser = Intent.createChooser(sendIntent, title);

                if (sendIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(chooser);
                }
            }

        }catch (Exception e){
            Toast.makeText(this,R.string.falhaEnvioMensagem,Toast.LENGTH_SHORT).show();
        }

    }
}
