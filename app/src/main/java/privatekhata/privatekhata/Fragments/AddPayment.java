package privatekhata.privatekhata.Fragments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveApi;
import com.google.android.gms.drive.DriveContents;
import com.google.android.gms.drive.DriveFolder;
import com.google.android.gms.drive.DriveId;
import com.google.android.gms.drive.MetadataChangeSet;
import com.google.android.gms.drive.OpenFileActivityBuilder;
import com.google.api.services.sheets.v4.model.UpdateValuesResponse;
import com.google.api.services.sheets.v4.model.ValueRange;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import privatekhata.privatekhata.AddClientDialog;
import privatekhata.privatekhata.DataBaseHelper.DataBaseHelper;
import privatekhata.privatekhata.R;
import privatekhata.privatekhata.User;
import privatekhata.privatekhata.Utility;

import static android.app.Activity.RESULT_OK;


public class AddPayment extends Fragment implements View.OnClickListener, DatePickerDialog.OnDateSetListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {


    public EditText edDatePicker,edAmtReceived,edAmtPending;
    public Button btnAddPayment;
    public Spinner spinClientName;
    private int _day;
    private int _month;
    private int _birthYear;
    public View view;
    private DataBaseHelper databaseHelper;
    private User user;

    public String spinnerItem;

    public static final String DATE_KEY="entry.751299440";
    public static final String CLIENT_NAME_KEY="entry.2062510420";
    public static final String AMT_REC_KEY="entry.2038969937";
    public static final String AMT_PEND_KEY="entry.1445752620";

    public static final String URL="https://docs.google.com/forms/d/e/1FAIpQLSfVsQIHnlmyG229Oq0PMo7r-f6GvpcTWDGNLcP0Uqeak6gbsA/formResponse";
    // Define the URL to request.  This should never change.
    public static final String  SPREADSHEET_FEED_URL = "https://spreadsheets.google.com/feeds/spreadsheets/private/full";
    public static final MediaType FORM_DATA_TYPE = MediaType.parse("application/x-www-form-urlencoded; charset=utf-8");

    private Context context;
    //public String newClient;
    private List<String> list;
    AddClientDialog.MyOnClickListener myListener;
    private GoogleApiClient googleApiClient;
    private boolean fileOperation = false;
    private static final int REQUEST_CODE_RESOLUTION = 1;

    private static final  int REQUEST_CODE_OPENER = 2;
    private DriveId mFileId;


    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.add_items_frag,container,false);

        context = getActivity();

        initViews();
        initListeners();
        initObjects();

        list=new ArrayList<String>();
        list.add("Client 1");
        list.add("Client 2");
        list.add("Client 3");
        list.add("Client 4");
        list.add("Client 5");

        add_spinner();

        spinClientName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                spinnerItem = parent.getItemAtPosition(position).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        createGoogleFileClient();

        return view;
    }





    @Override
    public void onStop() {
        super.onStop();

        if (googleApiClient != null) {

            // disconnect Google API client connection
            googleApiClient.disconnect();
        }
        super.onPause();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);



        switch (requestCode) {

            case REQUEST_CODE_OPENER:

                if (resultCode == RESULT_OK) {

                    mFileId = (DriveId) data.getParcelableExtra(
                            OpenFileActivityBuilder.EXTRA_RESPONSE_DRIVE_ID);

                    Log.e("file id", mFileId.getResourceId() + "");

                    String url = "https://drive.google.com/open?id="+ mFileId.getResourceId();
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                }

                break;

            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }

    public void createGoogleFileClient(){

        if (googleApiClient == null) {

            /**
             * Create the API client and bind it to an instance variable.
             * We use this instance as the callback for connection and connection failures.
             * Since no account name is passed, the user is prompted to choose.
             */
            googleApiClient = new GoogleApiClient.Builder(getActivity())
                    .addApi(Drive.API)
                    .addScope(Drive.SCOPE_FILE)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build();
        }

        googleApiClient.connect();



    }


    final ResultCallback<DriveApi.DriveContentsResult> driveContentsCallback =
            new ResultCallback<DriveApi.DriveContentsResult>() {
                @Override
                public void onResult(DriveApi.DriveContentsResult result) {

                    if (result.getStatus().isSuccess()) {

                        if (fileOperation == true) {

                            CreateFileOnGoogleDrive(result);

                        } else {

                           // OpenFileFromGoogleDrive();

                        }
                    }


                }
            };


    /**
     *  Open list of folder and file of the Google Drive
     */
/*
    public void OpenFileFromGoogleDrive(){

        IntentSender intentSender = Drive.DriveApi
                .newOpenFileActivityBuilder()
                .setMimeType(new String[] { "text/plain", "text/html" })
                .build(googleApiClient);
        try {
            startIntentSenderForResult(intentSender, REQUEST_CODE_OPENER, null, 0, 0, 0);

        } catch (IntentSender.SendIntentException e) {

            Log.w("", "Unable to send intent", e);
        }

    }
*/

    /**
     * Handle result of Created fi
     */
    final private ResultCallback<DriveFolder.DriveFileResult> fileCallback = new
            ResultCallback<DriveFolder.DriveFileResult>() {
                @Override
                public void onResult(DriveFolder.DriveFileResult result) {
                    if (result.getStatus().isSuccess()) {

                        Toast.makeText(getActivity(), "file created: "+""+
                                result.getDriveFile().getDriveId(), Toast.LENGTH_LONG).show();

                    }

                    return;

                }
            };



    public void add_spinner()
    {

        final ArrayAdapter arrayAdapter = new ArrayAdapter(getContext(),R.layout.spinner_item,list);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinClientName.setPrompt("Select you Client");

        Utility utility = new Utility(getActivity());

        list.add(utility.getNewClient());

        spinClientName.setAdapter(arrayAdapter);

        arrayAdapter.notifyDataSetChanged();
    }


    private void initViews() {
        edDatePicker = view.findViewById(R.id.edDatePicker);
        edAmtReceived = view.findViewById(R.id.edAmtReceived);
        edAmtPending = view.findViewById(R.id.edAmtPending);


        btnAddPayment = view.findViewById(R.id.btnAddPayment);
        spinClientName = view.findViewById(R.id.spinner);


    }



    private void initObjects() {

        databaseHelper = new DataBaseHelper(getActivity());
        user = new User();

    }

    private void initListeners() {
        edDatePicker.setOnClickListener(this);

        btnAddPayment.setOnClickListener(this);
    }


    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        _birthYear = year;
        _month = monthOfYear;
        _day = dayOfMonth;
        updateDisplay();

    }


    public String date,clientname,amtReceived,amtPending;
    @Override
    public void onClick(View v) {

        if(v==edDatePicker) {

            Calendar calendar = Calendar.getInstance(TimeZone.getDefault());

            DatePickerDialog dialog = new DatePickerDialog(getActivity(), this,
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH));
            dialog.show();

        }
        if(v==btnAddPayment){


            date = edDatePicker.getText().toString().trim();
            clientname = spinnerItem;
            amtReceived = edAmtReceived.getText().toString().trim();
            amtPending = edAmtPending.getText().toString().trim();


            if(edDatePicker.getText().toString().length()==0)
            {
                edDatePicker.setError("Enter Date First");
                return;
            }
            else if(edAmtReceived.getText().toString().length()==0)
            {
                edAmtReceived.setError("Enter Amount Received");
                return;
            }
            else if(edAmtPending.getText().toString().length()==0)
            {
                edAmtPending.setError("Enter Amount Received");
                return;
            }

            else {

                fileOperation = true;

                // create new contents resource
                Drive.DriveApi.newDriveContents(googleApiClient)
                        .setResultCallback(driveContentsCallback);

                //Create an object for PostDataTask AsyncTask
                PostDataTask postDataTask = new PostDataTask();
                postDataTask.execute(URL, date, clientname, amtReceived, amtPending);
                postDataToSQLite();

            }

        }

    }


    public void CreateFileOnGoogleDrive(DriveApi.DriveContentsResult result){


        final DriveContents driveContents = result.getDriveContents();

        // Perform I/O off the UI thread.
        new Thread() {
            @Override
            public void run() {
                // write content to DriveContents
                OutputStream outputStream = driveContents.getOutputStream();
                Writer writer = new OutputStreamWriter(outputStream);
                try {
                    writer.write("Date");
                    writer.close();
                } catch (IOException e) {
                    Log.e("Add payment", e.getMessage());
                }

                MetadataChangeSet changeSet = new MetadataChangeSet.Builder()
                        .setTitle("Clients Data Sheet")
                        .setMimeType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
                        .setStarred(true).build();

                // create a file in root folder
                Drive.DriveApi.getRootFolder(googleApiClient)
                        .createFile(googleApiClient, changeSet, driveContents)
                        .setResultCallback(fileCallback);
            }
        }.start();
    }



    private void postDataToSQLite() {


        user.setDate(edDatePicker.getText().toString().trim());
        user.setClientName(spinnerItem);
        user.setAmtReceived(edAmtReceived.getText().toString().trim());
        user.setAmtPending(edAmtPending.getText().toString().trim());

        databaseHelper.insertData(user);

        Fragment fragment = new ShowPayments();
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame,fragment).commit();



    }

    // updates the date in the birth date EditText
    private void updateDisplay() {

        edDatePicker.setText(new StringBuilder()
                // Month is 0 based so add 1
                .append(_day).append("/").append(_month + 1).append("/").append(_birthYear).append(" "));
    }



    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // Called whenever the API client fails to connect.
        Log.i("", "GoogleApiClient connection failed: " + connectionResult.toString());

        if (!connectionResult.hasResolution()) {

            // show the localized error dialog.
            GoogleApiAvailability.getInstance().getErrorDialog(getActivity(), connectionResult.getErrorCode(), 0).show();
            return;
        }

        /**
         *  The failure has a resolution. Resolve it.
         *  Called typically when the app is not yet authorized, and an  authorization
         *  dialog is displayed to the user.
         */

        try {

            connectionResult.startResolutionForResult(getActivity(), REQUEST_CODE_RESOLUTION);

        } catch (IntentSender.SendIntentException e) {

            Log.e("", "Exception while starting resolution activity", e);
        }
    }


    private class PostDataTask extends AsyncTask<String,Void,Boolean>{


        @Override
        protected Boolean doInBackground(String... user) {


            Boolean result = true;
            String url = user[0];

            String date = user[1];
            String clientName = user[2];
            String amtReceived = user[3];
            String amtPending = user[4];

            String postBody="";

            try {
                //all values must be URL encoded to make sure that special characters like & | ",etc.
                //do not cause problems
                postBody = DATE_KEY+"=" + URLEncoder.encode(date,"UTF-8") +
                        "&" + CLIENT_NAME_KEY + "=" + URLEncoder.encode(clientName,"UTF-8") +
                        "&" + AMT_REC_KEY + "=" + URLEncoder.encode(amtReceived,"UTF-8") +
                        "&" + AMT_PEND_KEY + "=" + URLEncoder.encode(amtPending,"UTF-8");
            } catch (UnsupportedEncodingException ex) {
                result=false;
            }



            try{
                //Create OkHttpClient for sending request
                OkHttpClient client = new OkHttpClient();
                //Create the request body with the help of Media Type
                RequestBody body = RequestBody.create(FORM_DATA_TYPE, postBody);
                Request request = new Request.Builder()
                        .url(url)
                        .post(body)
                        .build();
                //Send the request
                Response response = client.newCall(request).execute();
            }catch (IOException exception){
                result=false;
            }


            return result;


        }


        @Override
        protected void onPostExecute(Boolean result){
            //Print Success or failure message accordingly
        }



    }
}
