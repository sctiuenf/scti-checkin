package com.scti.scti2019checkin.tasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.scti.scti2019checkin.R;
import com.scti.scti2019checkin.interfaces.OnAsyncTaskResult;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class SendPresencesTask extends AsyncTask<String, Void, String> {
    private final WeakReference<Context> context;
    private ProgressDialog dialog;
    private OnAsyncTaskResult mResultCallback = null;

    public SendPresencesTask(Context context, OnAsyncTaskResult resultCallback) {
        this.context = new WeakReference<>(context);
        this.dialog = new ProgressDialog(context);
        this.mResultCallback = resultCallback;
    }

    @Override
    protected void onPreExecute() {
        //Mostra o dialog
        dialog = ProgressDialog.show(context.get(), context.get().getResources().getString(R.string.events_post_dialog_title),
                context.get().getResources().getString(R.string.events_post_dialog_message), true, false);
    }

    @Override
    protected String doInBackground(String... data) {
        try {
            URL url = new URL("https://scti.uenf.br/events/checkin");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("x-auth-token", data[0]);
            connection.setRequestProperty("x-auth-selector", data[1]);
            OutputStream os = connection.getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8");

            osw.write(data[2]);
            osw.flush();
            osw.close();
            os.close();

            connection.connect();

            String result;
            BufferedInputStream bis = new BufferedInputStream(connection.getInputStream());
            ByteArrayOutputStream buf = new ByteArrayOutputStream();
            int result2 = bis.read();
            while (result2 != -1) {
                buf.write((byte) result2);
                result2 = bis.read();
            }
            result = buf.toString();

            return result;

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        //Esconde o dialog
        dialog.dismiss();
        mResultCallback.onResult(result);
    }
}
