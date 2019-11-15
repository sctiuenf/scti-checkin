package com.scti.scti2019checkin;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.scti.scti2019checkin.adapters.ParticipantAdapter;
import com.scti.scti2019checkin.database.DatabaseController;
import com.scti.scti2019checkin.interfaces.OnRecyclerViewClickListener;
import com.scti.scti2019checkin.models.Participant;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

public class PickParticipantActivity extends AppCompatActivity implements OnRecyclerViewClickListener {
    private List<Participant> participants, auxParticipants;
    private DatabaseController databaseController;
    private ParticipantAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_participant);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        databaseController = new DatabaseController(this);
        participants = databaseController.getParticipants();
        auxParticipants = new ArrayList<>(participants);

        //Configurando o recycler view e inserindo os participantes nele
        RecyclerView recyclerView = findViewById(R.id.participant_list);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ParticipantAdapter(auxParticipants, this, this);
        recyclerView.setAdapter(adapter);

        final EditText searchView = findViewById(R.id.participant_search);
        searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                searchParticipants(searchView.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    public void onClick(View view, int position) {
        Intent intent = new Intent();
        String result = "" + auxParticipants.get(position).getId();
        intent.setData(Uri.parse(result));
        setResult(RESULT_OK, intent);
        finish();
    }

    private void searchParticipants(String searchText) {
        auxParticipants.clear();
        if (searchText.length() == 0) {
            for (int i = 0; i < participants.size(); i++) {
                auxParticipants.addAll(participants);
            }

        } else {
            for (int i = 0; i < participants.size(); i++) {
                String participantName = participants.get(i).getName().toLowerCase();
                participantName = unaccent(participantName);

                searchText = searchText.toLowerCase();
                searchText = unaccent(searchText);
                Log.i("name1123", searchText);

                if (participantName.contains(searchText)) {
                    auxParticipants.add(participants.get(i));
                }
            }
        }
        adapter.notifyDataSetChanged();
    }

    public String unaccent(String text) {
        String s = text;
        s = s.replaceAll("[éèê]", "e");
        s = s.replaceAll("[ùúû]", "u");
        s = s.replaceAll("[ìíî]", "i");
        s = s.replaceAll("[àáâã]", "a");
        s = s.replaceAll("[òóoô]", "o");
        s = s.replaceAll("[ç]", "c");
        return s;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) finish();
        return super.onOptionsItemSelected(item);
    }
}
