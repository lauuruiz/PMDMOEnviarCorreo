package com.example.contentproviderroot;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.Toast;

import com.example.contentproviderroot.adapter.ContactoAdapter;
import com.example.contentproviderroot.model.Contacto;

import java.util.ArrayList;

public class ContactoActivity extends AppCompatActivity {

    private ContactoAdapter contactoAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView recyclerContactos;

    private String nombre, telefono;

    protected final int SOLICITUD_PERMISO_CONTACTOS=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacto);
        initComponents();

    }

    private void initComponents() {
        nombre = getIntent().getStringExtra("nombre");
        telefono = getIntent().getStringExtra("telefono");

        recyclerContactos = findViewById(R.id.rvContactos);
        layoutManager = new GridLayoutManager(this, 1);
        contactoAdapter = new ContactoAdapter(this);

        recyclerContactos.setLayoutManager(layoutManager);
        recyclerContactos.setAdapter(contactoAdapter);

        pedirPermisos(Manifest.permission.READ_CONTACTS,
                R.string.necesito, R.string.necesito);
    }


    private void pedirPermisos(String permiso, int titulo, int mensaje) {
        if (ContextCompat.checkSelfPermission(this, permiso)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permiso)) {
                explicacion(R.string.tituloExplicacion, R.string.mensajeExplicacion, permiso);
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{permiso},
                        SOLICITUD_PERMISO_CONTACTOS);
            }
        } else {
            contactoAdapter.setData(obtenerAgendaContactos(ContactoActivity.this));
        }
    }

    private void explicacion(int title, int message, final String permissions) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                ActivityCompat.requestPermissions(ContactoActivity.this, new String[]{permissions}, SOLICITUD_PERMISO_CONTACTOS);
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case SOLICITUD_PERMISO_CONTACTOS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    //Permiso concedido
                    contactoAdapter.setData(obtenerAgendaContactos(ContactoActivity.this));

                } else {
                    Toast.makeText(this,"Permiso no concedido", Toast.LENGTH_LONG);
                    finish();
                }

                return;
            }
        }
    }

    public ArrayList<Contacto> obtenerAgendaContactos(Context context){
        ArrayList<Contacto> listaContactos = new ArrayList<>();
        ContentResolver cr = context.getContentResolver();
        String[] PROJECTION = new String[] {
                ContactsContract.RawContacts._ID,
                ContactsContract.Contacts.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Email.DATA,
                ContactsContract.CommonDataKinds.Phone.NUMBER };
        String order = "CASE WHEN "
                + ContactsContract.Contacts.DISPLAY_NAME
                + " NOT LIKE '%@%' THEN 1 ELSE 2 END, "
                + ContactsContract.Contacts.DISPLAY_NAME
                + ", "
                + ContactsContract.CommonDataKinds.Email.DATA
                +", "
                + ContactsContract.CommonDataKinds.Phone.DATA1
                + " COLLATE NOCASE";
        String filter = ContactsContract.CommonDataKinds.Email.DATA + " NOT LIKE '' AND "+ ContactsContract.CommonDataKinds.Phone.NUMBER+ " NOT LIKE ''";
        Cursor cur = cr.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, PROJECTION, filter, null, order);
        Cursor cur2 = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, PROJECTION, filter, null, order);
        while (cur.moveToNext() && cur2.moveToNext()) {
            String nombreContacto = cur.getString(1);
            String emailContacto = cur.getString(2);
            String telefonoContacto = cur2.getString(3);
            if (nombreContacto.contains(nombre) || telefonoContacto.contains(telefono) ){
                Contacto contacto = new Contacto();
                contacto.setNombre(nombreContacto);
                contacto.setEmail(emailContacto);
                contacto.setTelefono(telefonoContacto);
                listaContactos.add(contacto);
            }
        }
        cur.close();
        return listaContactos;
    }


}
