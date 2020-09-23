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
    public static final String valorKEY = "Valor";
    public static final String paraKEY = "Para";
    public static final String[] keyListForStrings= {valorKEY,paraKEY};
    private SharedPreferences preferences;

    private String para,de,quantia;

    TextView valor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultado);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SharedPreferencesMethods spm = new SharedPreferencesMethods(this);
        this.preferences = spm.sharedPreferencesLoading();
        Intent intent = getIntent();
        de = intent.getStringExtra(WSClientConvert.KEY_INTENT_DE);
        para = intent.getStringExtra(WSClientConvert.KEY_INTENT_PARA);
        quantia = intent.getStringExtra(WSClientConvert.KEY_INTENT_QUANTIA);

        valor = findViewById(R.id.lblValor) ;
        Locale brLocale = Locale.forLanguageTag("pt-BR");
        NumberFormat brnf = NumberFormat.getInstance(brLocale);
        valor.setText("$: "+brnf.format(intent.getDoubleExtra(WSClientConvert.KEY_INTENT_RESULTADO,0))+ "\n"+para);

        savingSharedPreferences(valor.getText().toString(), para);
      
    }
    @Override
    protected void onResume() {

        super.onResume();
        SharedPreferencesMethods spm = new SharedPreferencesMethods(this);
        this.preferences = spm.sharedPreferencesLoading();
        this.loadingSharedPreferences();

    }

    private boolean savingSharedPreferences(String valor, String para){
        SharedPreferencesMethods msp = new SharedPreferencesMethods(this);

        msp.setStringsKeys(keyListForStrings);// Keys for strings datas

        msp.setStringsValues(new String[]{valor,para});//Strings datas

        if(msp.sharedPreferencesSaving()){
            this.preferences = msp.sharedPreferencesLoading();
            return true;
        }else{
            return false;
        }
    }
    private boolean loadingSharedPreferences(){
        try{
            valor.setText(preferences.getString(valorKEY,""));
            para = preferences.getString(paraKEY,"");
            return true;
        }catch (Exception e){
            return false;
        }

    }
    public void onClickCompartilharResultado(View v){
        enviarMensagem("Olá, \na conversão da moeda: " + de
                + "\nPara a moeda: "+ para
                +"\nDe valor: "+quantia
                +"\nTeve como resultado: "+valor.getText());
    }
    public void onClickNovamente(View v){
        this.savingSharedPreferences("","");
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
