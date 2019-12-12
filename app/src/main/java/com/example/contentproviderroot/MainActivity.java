package com.example.contentproviderroot;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText etNombre, etTelefono;
    private Button btBuscar;

    private String nombre = "nombre", telefono ="telefono";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        init();
    }

    private void init() {
        etNombre = findViewById(R.id.etNombre);
        etTelefono = findViewById(R.id.etTelefono);

        btBuscar = findViewById(R.id.btBuscar);
        btBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(comprobar()){
                    Intent i = new Intent(MainActivity.this, ContactoActivity.class);
                    i.putExtra("nombre", etNombre.getText().toString());
                    i.putExtra("telefono", etTelefono.getText().toString());
                    startActivity(i);
                }else{
                    Toast.makeText(MainActivity.this, "Algun campo está vacio", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean comprobar() {
        boolean comprobar = true;
        if (etNombre.getText().toString().isEmpty() || etTelefono.getText().toString().isEmpty()){
            comprobar = false;
        }
        return comprobar;
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
