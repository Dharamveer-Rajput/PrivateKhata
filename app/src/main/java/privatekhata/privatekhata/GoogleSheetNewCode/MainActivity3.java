package privatekhata.privatekhata.GoogleSheetNewCode;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.MetadataChangeSet;
import com.google.api.services.sheets.v4.model.ValueRange;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import privatekhata.privatekhata.R;

public class MainActivity3 extends AppCompatActivity {
    private static final String DEBUG_TAG = "HttpExample";
    ArrayList<ClientData> clientDataArrayList = new ArrayList<ClientData>();
    ListView listview;
    //Button btnDownload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        listview =  findViewById(R.id.listview);
       // btnDownload =  findViewById(R.id.btnDownload);
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {

            new DownloadWebpageTask(new AsyncResult() {
                @Override
                public void onResult(JSONObject object) {
                    processJson(object);
                }
            }).execute("https://spreadsheets.google.com/tq?key=1NQ9nUULayEaA3TFqm8WcSOHUJW9jQdxANP7QJ5xUsFc");


        } else {
            new DownloadWebpageTask(new AsyncResult() {
                @Override
                public void onResult(JSONObject object) {
                    processJson(object);
                }
            }).execute("https://spreadsheets.google.com/tq?key=1NQ9nUULayEaA3TFqm8WcSOHUJW9jQdxANP7QJ5xUsFc");

        }
    }




    private void processJson(JSONObject object) {

        try {
            JSONArray rows = object.getJSONArray("rows");

            for (int r = 0; r < rows.length(); ++r) {
                JSONObject row = rows.getJSONObject(r);
                JSONArray columns = row.getJSONArray("c");

                String date = columns.getJSONObject(1).getString("v");
                String name = columns.getJSONObject(2).getString("v");
                int AmtReceived = columns.getJSONObject(3).getInt("v");
                int AmtPending = columns.getJSONObject(4).getInt("v");
                ClientData clientData = new ClientData(date, name, AmtReceived, AmtPending);
                clientDataArrayList.add(clientData);
            }

            final TeamsAdapter adapter = new TeamsAdapter(this, R.layout.team, clientDataArrayList);
            listview.setAdapter(adapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
