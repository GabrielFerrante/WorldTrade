package br.edu.ifsp.appexchangerates.Template;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import br.edu.ifsp.appexchangerates.R;

public class AprenderActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aprender);
    }
    public void onClickVoltar(View v){
        finish();
    }
}
