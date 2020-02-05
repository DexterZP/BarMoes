package br.dexter.barmoes.MenuPrincipal;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rey.material.app.Dialog;
import com.rey.material.widget.Button;
import com.rey.material.widget.Spinner;

import java.util.ArrayList;

import br.dexter.barmoes.R;

public class AdapterMesas extends RecyclerView.Adapter<AdapterMesas.ViewHolder>
{
    private Context context;
    private ArrayList<String> mesa;
    private DatabaseReference databaseReference;

    AdapterMesas(Context context, ArrayList<String> mesa)
    {
        this.context = context;
        this.mesa = mesa;
        notifyDataSetChanged();
    }

    @NonNull @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i)
    {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_menu_principal_mesa, parent, false);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        return new ViewHolder(view);
    }

    @Override @SuppressLint("SetTextI18n")
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i)
    {
        viewHolder.nameOfMesa.setText(mesa.get(i));

        Dialog dialog = new Dialog(context);

        if(!isOnline()) {
            dialog.setContentView(R.layout.conexion);
            dialog.setCancelable(false);
            dialog.show();
        }
        else dialog.dismiss();

        databaseReference.child(mesa.get(i)).addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                ArrayList<String> burguer = new ArrayList<>();
                ArrayList<String> coment = new ArrayList<>();

                for(int j = 0; j < 20; j++)
                {
                    String hamburguer = dataSnapshot.child("Hamburguer " + j).getValue(String.class);
                    String text = dataSnapshot.child("HamburguerTXT " + j).getValue(String.class);

                    if(hamburguer == null) {
                        viewHolder.nameOfPedido.setVisibility(View.VISIBLE);
                        break;
                    }
                    else
                    {
                        burguer.add(hamburguer);
                        coment.add(text);
                        viewHolder.nameOfPedido.setVisibility(View.GONE);
                    }
                }

                RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(context, 3);
                viewHolder.recyclerView.setLayoutManager(mLayoutManager);
                AdapterBurguer adapter = new AdapterBurguer(context, burguer, coment);
                viewHolder.recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });

        viewHolder.cardView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);

                final ViewGroup dialog = (ViewGroup) View.inflate(context, R.layout.activity_menu_principal_add, null);
                dialog.startAnimation(AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left));

                viewHolder.nameOfMesaAdd = dialog.findViewById(R.id.nameOfMesa);
                viewHolder.Adicionar = dialog.findViewById(R.id.Adicionar);
                viewHolder.Encerrar = dialog.findViewById(R.id.Encerrar);
                viewHolder.InputText = dialog.findViewById(R.id.InputText);
                viewHolder.Text = dialog.findViewById(R.id.Text);
                final Spinner spinner = dialog.findViewById(R.id.spinner_Select);

                viewHolder.nameOfMesaAdd.setText(mesa.get(i));

                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context, R.array.Hamburgueres, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);

                builder.setView(dialog);
                final AlertDialog alert = builder.create();
                alert.show();

                viewHolder.Adicionar.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        final Dialog alertSync = new Dialog(context);
                        alertSync.setContentView(R.layout.dialog);
                        alertSync.setCancelable(false);
                        alertSync.show();

                        assert viewHolder.Text.getText() != null;

                        databaseReference.addListenerForSingleValueEvent(new ValueEventListener()
                        {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                            {
                                for(int j = 0; j < 20; j++)
                                {
                                    String hamburguer = dataSnapshot.child(mesa.get(i)).child("Hamburguer " + j).getValue(String.class);

                                    if(hamburguer == null)
                                    {
                                        databaseReference.child(mesa.get(i)).child("Hamburguer " + j).setValue(spinner.getSelectedItem().toString());
                                        databaseReference.child(mesa.get(i)).child("HamburguerTXT " + j).setValue(viewHolder.Text.getText().toString());
                                        alertSync.dismiss();
                                        alert.dismiss();
                                        break;
                                    }
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) { }
                        });
                    }
                });

                viewHolder.Encerrar.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        final Dialog alertSync = new Dialog(context);
                        alertSync.setContentView(R.layout.dialog);
                        alertSync.setCancelable(false);
                        alertSync.show();

                        databaseReference.addListenerForSingleValueEvent(new ValueEventListener()
                        {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                            {
                                for(int j = 0; j < 20; j++)
                                {
                                    String hamburguer = dataSnapshot.child(mesa.get(i)).child("Hamburguer " + j).getValue(String.class);

                                    if(hamburguer != null)
                                    {
                                        databaseReference.child(mesa.get(i)).removeValue().addOnSuccessListener(new OnSuccessListener<Void>()
                                        {
                                            @Override
                                            public void onSuccess(Void aVoid)
                                            {
                                                Toast.makeText(context, "Mesa encerrada com sucesso", Toast.LENGTH_SHORT).show();
                                                alertSync.dismiss();
                                                alert.dismiss();
                                            }
                                        });

                                        break;
                                    }
                                    else
                                    {
                                        Toast.makeText(context, "Você não pode encerrar uma mesa vazia", Toast.LENGTH_SHORT).show();
                                        alertSync.dismiss();
                                        alert.dismiss();
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) { }
                        });
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return mesa.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder
    {
        CardView cardView;
        TextView nameOfMesa, nameOfMesaAdd, nameOfPedido;
        RecyclerView recyclerView;
        Button Adicionar, Encerrar;
        TextInputLayout InputText;
        TextInputEditText Text;

        ViewHolder(final View itemView)
        {
            super(itemView);

            nameOfPedido = itemView.findViewById(R.id.nameOfPedidos);
            cardView = itemView.findViewById(R.id.CardClick);
            nameOfMesa = itemView.findViewById(R.id.nameOfMesa);
            recyclerView = itemView.findViewById(R.id.recyclerview);
        }
    }

    private boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(cm != null) {
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            return netInfo != null && netInfo.isConnectedOrConnecting();
        }
        return false;
    }
}