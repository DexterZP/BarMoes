package br.dexter.barmoes.MenuPrincipal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;

import br.dexter.barmoes.Notification.BaseNotification;
import br.dexter.barmoes.R;

public class menuPrincipal extends AppCompatActivity
{
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);

        setTitle("Bar do Moe's");

        recyclerView = findViewById(R.id.recyclerview);

        BaseNotification.startBaseAlarmManager(this);
        BaseNotification.enableBootReceiver(this);

        AdicionarMesas();
    }

    public void AdicionarMesas()
    {
        ArrayList<String> mesas = new ArrayList<>();

        mesas.add("Mesa 01");
        mesas.add("Mesa 02");
        mesas.add("Mesa 03");
        mesas.add("Mesa 04");
        mesas.add("Mesa 05");
        mesas.add("Mesa 06");
        mesas.add("Mesa 07");
        mesas.add("Mesa 08");
        mesas.add("Mesa 09");
        mesas.add("Mesa 10");
        mesas.add("Mesa 11");
        mesas.add("Mesa 12");

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(mLayoutManager);
        AdapterMesas adapter = new AdapterMesas(this, mesas);
        recyclerView.setAdapter(adapter);
    }
}
