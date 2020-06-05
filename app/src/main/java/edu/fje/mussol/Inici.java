package edu.fje.mussol;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;


public class Inici extends AppCompatActivity implements OnItemSelectedListener {

    private ArrayAdapter<String> tiposAdapter;
    private Spinner spinner;
    private String tipo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inici);

        spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 10);

    }

    public void nextPage(View v){
        Intent intent = new Intent(this, ClientXat.class);
        intent.putExtra("tipo",tipo);
        startActivity(intent);
    }

    public void Imatge(View v){
        Intent intent = new Intent(this, ImageActivity.class);
        intent.putExtra("tipo",tipo);
        startActivity(intent);
    }

    public void Geo(View v){
        Intent intent = new Intent(this, Geolocalitzacio.class);
        startActivity(intent);
    }


    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        tipo = parent.getItemAtPosition(pos).toString();
        System.out.println(tipo);
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }
}

