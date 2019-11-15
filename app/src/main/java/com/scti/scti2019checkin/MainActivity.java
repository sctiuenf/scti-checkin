package com.scti.scti2019checkin;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.recyclerview.extensions.ListAdapter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.scti.scti2019checkin.adapters.EventAdapter;
import com.scti.scti2019checkin.converters.PresenceConverter;
import com.scti.scti2019checkin.database.DatabaseController;
import com.scti.scti2019checkin.interfaces.OnAsyncTaskResult;
import com.scti.scti2019checkin.models.Event;
import com.scti.scti2019checkin.models.Participant;
import com.scti.scti2019checkin.tasks.GetEventsTask;
import com.scti.scti2019checkin.tasks.SendPresencesTask;
import com.scti.scti2019checkin.utils.DateConverter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private SharedPreferences sharedPref;
    private List<Event> events;
    private String token;
    private String selector;
    private DatabaseController databaseController;
    private EventAdapter adapter;
    private TextView statusView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        statusView = findViewById(R.id.sync_status);

        sharedPref = getApplication().getSharedPreferences(getString(R.string.app_sharedPref_file), Context.MODE_PRIVATE);
        token = sharedPref.getString(getString(R.string.app_sharedPref_token), "");
        selector = sharedPref.getString(getString(R.string.app_sharedPref_selector), "");

        databaseController = new DatabaseController(getBaseContext());
        events = databaseController.getEvents();

        //Configurando o recycler view e inserindo os eventos nele
        RecyclerView recyclerView = findViewById(R.id.event_list);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new EventAdapter(events, this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateSyncStatus();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.get_data:
                getData();
                break;
            case R.id.post_data:
                postData();
                break;
            case R.id.logoff:
                logoff();
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void logoff() {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(getString(R.string.app_sharedPref_token), "");
        editor.putString(getString(R.string.app_sharedPref_selector), "");
        editor.putString(getString(R.string.app_sharedPref_mail), "");
        editor.apply();
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public void getData() {
        String[] data = new String[]{token, selector};

        new GetEventsTask(MainActivity.this, new OnAsyncTaskResult() {
            @Override
            public void onResult(String result) {
                if (result != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        boolean success = jsonObject.getBoolean("success");
                        if (success) {
                            JSONObject dataArray = jsonObject.getJSONObject("data_array");
                            JSONArray eventsArray = dataArray.getJSONArray("events");
                            for (int i = 0; i < eventsArray.length(); i++) {
                                JSONObject eventInfo = eventsArray.getJSONObject(i);
                                int eventId = Integer.valueOf(eventInfo.getString("idEvento"));
                                String eventTitle = eventInfo.getString("tituloEvento");
                                String eventType = eventInfo.getString("tipo");
                                String eventBegin = eventInfo.getString("inicioEvento");
                                String eventEnd = eventInfo.getString("fimEvento");
                                databaseController.insertEvent(eventId, eventTitle, eventType, eventBegin, eventEnd);

                                if (eventInfo.has("inscricoes")) {
                                    JSONArray subscriptions = eventInfo.getJSONArray("inscricoes");
                                    for (int j = 0; j < subscriptions.length(); j++) {
                                        JSONObject subscriptionJson = subscriptions.getJSONObject(j);
                                        int participantId = Integer.valueOf(subscriptionJson.getString("idParticipante"));
                                        databaseController.insertSubscription(eventId, participantId);
                                    }
                                }
                            }

                            JSONArray participantsArray = dataArray.getJSONArray("attendees");
                            for (int i = 0; i < participantsArray.length(); i++) {
                                JSONObject participantInfo = participantsArray.getJSONObject(i);
                                int particiantId = Integer.valueOf(participantInfo.getString("idParticipante"));
                                String name = participantInfo.getString("nomeParticipante");
                                String lastname = participantInfo.getString("sobrenomeParticipante");
                                databaseController.insertParticipant(particiantId, Participant.capitalize(name), Participant.capitalize(lastname));
                            }

                            updateRecyclerView();
                            Toast.makeText(MainActivity.this, getString(R.string.events_get_success), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MainActivity.this, getString(R.string.events_get_fail), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(MainActivity.this, getString(R.string.events_get_fail), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }).execute(data);
    }

    public void postData() {
        PresenceConverter converter = new PresenceConverter(this);
        final String[] data = new String[]{token, selector, converter.getDataJSON()};

        new SendPresencesTask(MainActivity.this, new OnAsyncTaskResult() {
            @Override
            public void onResult(String result) {
                if (result != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(result);

                        boolean success = jsonObject.getBoolean("success");
                        if (success) {
                            JSONArray dataArray = jsonObject.getJSONArray("data_array");

                            boolean fullSuccess = true;

                            for (int i = 0; i < dataArray.length(); i++) {
                                JSONObject eventInfo = dataArray.getJSONObject(i);
                                String status = eventInfo.getString("status");
                                if (status.equals("not_enrolled")) {
                                    fullSuccess = false;
                                    break;
                                }
                            }

                            updateRecyclerView();
                            if (fullSuccess) {
                                changeSyncStatus(true);
                            } else {
                                changeSyncStatus(false);
                            }

                            Toast.makeText(MainActivity.this, getString(R.string.events_post_success), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MainActivity.this, getString(R.string.events_post_fail), Toast.LENGTH_SHORT).show();
                            changeSyncStatus(false);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(MainActivity.this, getString(R.string.events_post_fail), Toast.LENGTH_SHORT).show();
                        changeSyncStatus(false);
                    }
                }
            }
        }).execute(data);
    }

    public void updateSyncStatus() {
        boolean status = sharedPref.getBoolean(getString(R.string.app_sharedPref_syncstatus), true);
        if (status) {
            statusView.setVisibility(View.GONE);
        } else {
            statusView.setVisibility(View.VISIBLE);
        }
    }

    public void changeSyncStatus(boolean state) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(getString(R.string.app_sharedPref_syncstatus), state);
        editor.apply();
        updateSyncStatus();
    }

    private void updateRecyclerView() {
        events.clear();
        List<Event> eventsAux = databaseController.getEvents();
        events.addAll(eventsAux);
        adapter.notifyDataSetChanged();
    }
}
