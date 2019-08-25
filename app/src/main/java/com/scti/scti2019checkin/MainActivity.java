package com.scti.scti2019checkin;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.recyclerview.extensions.ListAdapter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.scti.scti2019checkin.adapters.EventAdapter;
import com.scti.scti2019checkin.models.Event;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Preenchendo a lista de eventos
        ArrayList<Event> events = new ArrayList<Event>();
        events.add(new Event(1, 1, "Desenvolvendo apps nativos para android e ios com Flutter", "14:00 - 16:00"));
        events.add(new Event(2, 2, "Criando uma API REST em Java com Spring Boot", "16:00 - 18:00"));
        events.add(new Event(2, 3, "Introdução ao Desenvolvimento de Jogos com Unity", "18:00 - 20:00"));
        events.add(new Event(2, 3, "Infraestrutura como Código (IaC) com o Ansible", "18:00 - 20:00"));
        events.add(new Event(2, 3, "IoT - Da coleta ao armazenamento", "18:00 - 20:00"));
        events.add(new Event(2, 3, "Design Thinking de serviços - Princípios, Métodos e Ferramentas", "18:00 - 20:00"));
        events.add(new Event(2, 3, "Aplicações Serverless", "18:00 - 20:00"));

        //Configurando o recycler view e inserindo os eventos nele
        RecyclerView recyclerView = findViewById(R.id.event_list);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        EventAdapter adapter = new EventAdapter(events, this);
        recyclerView.setAdapter(adapter);
    }
}
