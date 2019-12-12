package com.example.contentproviderroot.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contentproviderroot.R;
import com.example.contentproviderroot.model.Contacto;

public class ContactoAdapter extends RecyclerView.Adapter <ContactoAdapter.ItemHolder> implements PopupMenu.OnMenuItemClickListener {

    private LayoutInflater inflater;
    private List<Contacto> contactosList;
    private Context context;
    private String correoElectronico;
    private PopupMenu popup;

    public ContactoAdapter(Context context) {
        inflater= LayoutInflater.from(context);
        this.context = context;
    }

    @NonNull
    @Override
    public ContactoAdapter.ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= inflater.inflate(R.layout.item_contacto,parent,false);
        return new ItemHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ContactoAdapter.ItemHolder holder, int position) {

        final Contacto contacto = contactosList.get(position);

        if (contacto != null){
            holder.tvNombre.setText(holder.tvNombre.getText()+" "+contacto.getNombre());
            holder.tvTelefono.setText(holder.tvTelefono.getText()+" "+contacto.getTelefono());
            holder.tvCorreo.setText(holder.tvCorreo.getText()+" "+contacto.getEmail());
        }

        holder.cl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                correoElectronico = contacto.getEmail();
                showPopup(holder.cl);
            }
        });
    }

    @Override
    public int getItemCount() {
        int elements=0;
        if(contactosList !=null){
            elements= contactosList.size();
        }
        return elements;
    }

    public void setData(List<Contacto> contactoList){
        this.contactosList = contactoList;
        notifyDataSetChanged();
    }

    public List<Contacto> getData(){
        return this.contactosList;
    }


    public void showPopup(View v) {
        popup = new PopupMenu(context, v);
        popup.setOnMenuItemClickListener(this);
        popup.inflate(R.menu.menu_contacto);
        popup.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.email:
                mandarCorreo(correoElectronico);
                return true;
            default:
                return true;
        }
    }

    private void mandarCorreo(String direccion) {

        String[] TO = {direccion};
        String[] CC = {""};


        //crear intent
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto: "));

        intent.putExtra(Intent.EXTRA_EMAIL, TO);
        intent.putExtra(Intent.EXTRA_CC, CC);

        intent.putExtra(Intent.EXTRA_SUBJECT, "ENVIANDO CORREO CON APLICACION");

        //crear chooset
        String title = "Mandar este mail con...";

        Intent chooser = Intent.createChooser(intent, title);
        if (intent.resolveActivity(context.getPackageManager()) != null){
            context.startActivity(chooser);
        }

    }

    public class ItemHolder extends RecyclerView.ViewHolder {
        private TextView tvNombre, tvTelefono, tvCorreo;
        private CardView cl;

        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre=itemView.findViewById(R.id.tvNombre);
            tvTelefono = itemView.findViewById(R.id.tvTelefono);
            tvCorreo = itemView.findViewById(R.id.tvCorreo);
            cl = itemView.findViewById(R.id.cvContacto);
        }
    }
}
