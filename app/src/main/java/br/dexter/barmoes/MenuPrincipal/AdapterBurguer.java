package br.dexter.barmoes.MenuPrincipal;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import br.dexter.barmoes.R;

public class AdapterBurguer extends RecyclerView.Adapter<AdapterBurguer.ViewHolder>
{
    private Context context;
    private ArrayList<String> burguer, comentario;

    AdapterBurguer(Context context, ArrayList<String> burguer, ArrayList<String> comentario)
    {
        this.context = context;
        this.burguer = burguer;
        this.comentario = comentario;
        notifyDataSetChanged();
    }

    @NonNull @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i)
    {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_menu_principal_burg, parent, false);

        return new ViewHolder(view);
    }

    @Override @SuppressLint("SetTextI18n")
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i)
    {
        viewHolder.nameOfBurguer.setText(burguer.get(i));

        if(!viewHolder.nameOfText.getText().toString().isEmpty()) {
            viewHolder.nameOfText.setVisibility(View.VISIBLE);
            viewHolder.nameOfText.setText(comentario.get(i));
        }
    }

    @Override
    public int getItemCount() {
        return burguer.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView nameOfBurguer, nameOfText;

        ViewHolder(final View itemView)
        {
            super(itemView);

            nameOfBurguer = itemView.findViewById(R.id.nameOfHamburguer);
            nameOfText = itemView.findViewById(R.id.nameOfText);
        }
    }
}