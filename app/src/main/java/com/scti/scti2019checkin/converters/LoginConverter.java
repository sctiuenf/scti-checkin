package com.scti.scti2019checkin.converters;

import org.json.JSONException;
import org.json.JSONStringer;

public class LoginConverter {
    public String getKeyJSON(String email, String password) {
        JSONStringer js = new JSONStringer();

        try {
            js.object();
            js.key("email");
            js.value(email);
            js.key("password");
            js.value(password);
            js.endObject();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return js.toString();
    }
}
