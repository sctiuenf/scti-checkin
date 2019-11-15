package com.scti.scti2019checkin;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.scti.scti2019checkin.adapters.PresenceAdapter;
import com.scti.scti2019checkin.database.DatabaseController;
import com.scti.scti2019checkin.models.Event;
import com.scti.scti2019checkin.models.Participant;
import com.scti.scti2019checkin.models.Presence;
import com.scti.scti2019checkin.utils.DateConverter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class EventActivity extends AppCompatActivity {
    private static final int CAMERA_PERMISSION_CODE = 1;
    private static final int PICK_PARTICIPANT_CODE = 123;
    List<Presence> presences;
    private PresenceAdapter adapter;
    private DatabaseController databaseController;
    private int eventId;
    private Event event;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        databaseController = new DatabaseController(this);

        Intent intent = getIntent();
        eventId = intent.getExtras().getInt("idEvento");

        event = databaseController.getEventById(eventId);
        setTitle(event.getTitle());

        //Setando listener do botão de nova presença
        FloatingActionButton newPresenceButton = findViewById(R.id.new_presence_button);
        newPresenceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    //Verificando permissao
                    if (checkSelfPermission(Manifest.permission.CAMERA)
                            != PackageManager.PERMISSION_GRANTED) {
                        //Requisitando permissao
                        requestPermissions(new String[]{Manifest.permission.CAMERA},
                                CAMERA_PERMISSION_CODE);
                    } else {
                        //Abre o leitor caso o usuário conceda a permissão
                        openScanner();
                    }
                } else {
                    //Abre o sensor caso a permissão já tenha sido dada anteriormente
                    openScanner();
                }
            }
        });

        FloatingActionButton newManualPresenceButton = findViewById(R.id.new_manual_presence_button);
        newManualPresenceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EventActivity.this, PickParticipantActivity.class);
                startActivityForResult(intent, PICK_PARTICIPANT_CODE);
            }
        });

        presences = databaseController.getPresencesInEvent(eventId);

        //Configurando o recycler view e inserindo os eventos nele
        RecyclerView recyclerView = findViewById(R.id.presence_list);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new PresenceAdapter(presences, this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_PARTICIPANT_CODE) {
            if (resultCode == RESULT_OK) {
                int participantId = Integer.valueOf(data.getDataString());
                addPresence(participantId);
            }
        } else {
            IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            if (result != null) {
                if (result.getContents() != null) {
                    try {
                        //Converte o conteúdo do QRCode em JSON
                        JSONObject participanteJSON = new JSONObject(result.getContents());
                        try {
                            int participantId = participanteJSON.getInt("id");
                            addPresence(participantId);
                        } catch (Exception e) {
                            //QRCode inválido
                            Toast.makeText(this, getResources().getString(R.string.scanner_invalid), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        //Falha na leitura
                        Toast.makeText(this, getResources().getString(R.string.scanner_fail), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    //Falha na leitura
                    Toast.makeText(this, getResources().getString(R.string.scanner_fail), Toast.LENGTH_SHORT).show();
                }
            } else {
                super.onActivityResult(requestCode, resultCode, data);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //Pedido de permissão respondido
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Permissão concedida
                openScanner();
            }
        }
    }

    private void openScanner() {
        IntentIntegrator integrator = new IntentIntegrator(EventActivity.this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
        integrator.setCameraId(0);
        integrator.setPrompt(getResources().getString(R.string.scanner_message));
        integrator.setOrientationLocked(false);
        integrator.initiateScan();
    }

    private void updateRecyclerView() {
        presences.clear();
        List<Presence> presencesAux = databaseController.getPresencesInEvent(eventId);
        presences.addAll(presencesAux);
        adapter.notifyDataSetChanged();
    }

    private void addPresence(final int id) {
        if (databaseController.presenceIsDuplicate(eventId, id)) {
            Toast.makeText(this, getString(R.string.events_presence_repeated), Toast.LENGTH_SHORT).show();
            return;
        }

        if (event.getType().equals(Event.TYPE_MINICOURSE) && !databaseController.checkSubscription(eventId, id)) {
            Participant participant = databaseController.getParticipantById(id);
            AlertDialog.Builder builder = new AlertDialog.Builder(EventActivity.this);
            builder.setCancelable(true);
            builder.setTitle(getResources().getString(R.string.events_forced_dialog_title));
            builder.setMessage(participant.getName() + " " + getResources().getString(R.string.events_forced_dialog_message));
            builder.setPositiveButton(getResources().getString(R.string.events_forced_confirm),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            databaseController.insertPresence(id, eventId, DateConverter.getFormatedNow(), true);
                            updateRecyclerView();
                        }
                    });
            builder.setNegativeButton(getResources().getString(R.string.events_forced_decline),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
            AlertDialog dialog = builder.create();
            dialog.show();
        } else {
            databaseController.insertPresence(id, eventId, DateConverter.getFormatedNow(), false);
            updateRecyclerView();
        }
    }

}
