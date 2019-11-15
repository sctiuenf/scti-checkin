package com.scti.scti2019checkin;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.scti.scti2019checkin.converters.LoginConverter;
import com.scti.scti2019checkin.interfaces.OnAsyncTaskResult;
import com.scti.scti2019checkin.tasks.LoginRequestTask;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {
    private SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Pega o token e seletor armazenados
        sharedPref = getApplication().getSharedPreferences(getString(R.string.app_sharedPref_file), Context.MODE_PRIVATE);
        String token = sharedPref.getString(getString(R.string.app_sharedPref_token), "");
        String selector = sharedPref.getString(getString(R.string.app_sharedPref_selector), "");

        //Se existir um token e um seletor, não precisa logar
        if (!token.equals("") && !selector.equals("")) {
            executeLogin();
        }

        //Localiza o botão de login e cadastra um listener de click
        findViewById(R.id.login_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Captura as views de email e senha
                TextView emailView = findViewById(R.id.login_email);
                TextView passwordView = findViewById(R.id.login_password);
                String email = emailView.getText().toString();
                if (!email.equals("")) {

                    String password = passwordView.getText().toString();
                    if (!password.equals("")) {

                        //Se email e password não estiverem em branco inicia a requisição de login
                        LoginConverter converter = new LoginConverter();
                        String loginJSON = converter.getKeyJSON(email, password);
                        login(loginJSON);

                    } else {
                        //Mensagem de campo obrigatório
                        passwordView.setError(getString(R.string.auth_required));
                    }

                } else {
                    //Mensagem de campo obrigatório
                    emailView.setError(getString(R.string.auth_required));
                }
            }
        });


    }

    private void login(String json) {
        //Inicia uma nova requisição HTTP
        new LoginRequestTask(LoginActivity.this, new OnAsyncTaskResult() {
            @Override
            public void onResult(String result) {
                if (result != null) {
                    try {
                        //Converte o resultado para JSON
                        JSONObject loginResult = new JSONObject(result);
                        processLoginResult(loginResult);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(LoginActivity.this, getString(R.string.auth_connection_error), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, getString(R.string.auth_connection_error), Toast.LENGTH_SHORT).show();
                }

            }
        }).execute(json);
    }

    void processLoginResult(JSONObject loginResult) {
        try {
            boolean success = loginResult.getBoolean("success");
            Toast.makeText(this, loginResult.getString("message"), Toast.LENGTH_SHORT).show();
            if (success) {
                JSONObject dataArray = new JSONObject(loginResult.getString("data_array"));
                String token = dataArray.getString("token");
                String selector = dataArray.getString("selector");

                JSONObject organizerData = new JSONObject(dataArray.getString("organizer"));
                String mail = organizerData.getString("email");

                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString(getString(R.string.app_sharedPref_token), token);
                editor.putString(getString(R.string.app_sharedPref_selector), selector);
                editor.putString(getString(R.string.app_sharedPref_mail), mail);
                editor.apply();
                executeLogin();
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(LoginActivity.this, getString(R.string.auth_connection_error), Toast.LENGTH_SHORT).show();
        }
    }

    void executeLogin() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
