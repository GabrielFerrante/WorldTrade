package br.edu.ifsp.appexchangerates;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.TextView;

public class ResultadoActivity extends AppCompatActivity {
    TextView valor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultado);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        valor = findViewById(R.id.lblValor) ;
        valor.setText("R$: "+String.valueOf(intent.getDoubleExtra(WSClientConvert.KEY_INTENT,0)));
      
    }

}
