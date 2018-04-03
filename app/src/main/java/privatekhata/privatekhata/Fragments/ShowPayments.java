package privatekhata.privatekhata.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import privatekhata.privatekhata.DataBaseHelper.DataBaseHelper;
import privatekhata.privatekhata.GoogleSheetNewCode.AsyncResult;
import privatekhata.privatekhata.GoogleSheetNewCode.ClientData;
import privatekhata.privatekhata.GoogleSheetNewCode.DownloadWebpageTask;
import privatekhata.privatekhata.GoogleSheetNewCode.TeamsAdapter;
import privatekhata.privatekhata.R;
import privatekhata.privatekhata.SimpleDividerItemDecoration;
import privatekhata.privatekhata.User;


public class ShowPayments extends Fragment {


    public View view;
    public ProgressBar progressBar;
    Context mContext;
    public RecyclerView recyclerView;
    public HomeAdapter homeAdapter;
    public DataBaseHelper dataBaseHelper;
    ArrayList<ClientData> clientDataArrayList = new ArrayList<ClientData>();
    ListView listview;

    public static final String PREFS_NAME = "FirstLaunch";


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.home_fragment_layout, container, false);

        listview =  view.findViewById(R.id.listview);

        progressBar = view.findViewById(R.id.progressBar1);
        //dataBaseHelper = new DataBaseHelper(getActivity());

        checkCurrentDate();


       // recyclerView =  view.findViewById(R.id.recycler_view);

        mContext = container.getContext();

        //usersList = new ArrayList<ClientData>();

        ConnectivityManager connMgr = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {

            new DownloadWebpageTask(new AsyncResult() {
                @Override
                public void onResult(JSONObject object) {
                    processJson(object);
                }
            }).execute("https://spreadsheets.google.com/tq?key=1NQ9nUULayEaA3TFqm8WcSOHUJW9jQdxANP7QJ5xUsFc");


        } else {

        }



        return view;

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
                ClientData clientData = new ClientData( date, name, AmtReceived, AmtPending);
                clientDataArrayList.add(clientData);
            }

            final TeamsAdapter adapter = new TeamsAdapter(getActivity(), R.layout.team, clientDataArrayList);
            listview.setAdapter(adapter);
            progressBar.setVisibility(View.INVISIBLE);



        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void checkCurrentDate() {

        SharedPreferences sharedPref = getActivity().getSharedPreferences(PREFS_NAME, 0);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String currentDate = sdf.format(new Date());


        if (sharedPref.getString("LAST_LAUNCH_DATE", "nodate").contains(currentDate)) {
            // Date matches. User has already Launched the app once today. So do nothing.
            // chronometerStartFromPrevious();
            return;
        } else {
            // Display dialog text here......
            // Do all other actions for first time launch in the day...
            // Set the last Launched date to today.
//            dataBaseHelper.deleteAllTableData();


            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("LAST_LAUNCH_DATE", currentDate);
            editor.commit();

        }
    }

    public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder>{

        Context context;
        private List<ClientData> clientData;


        public HomeAdapter(Context context, List<ClientData> clientData) {
            this.context = context;
            this.clientData = clientData;
        }

        @Override
        public HomeAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_row_layout, parent, false);

            return new MyViewHolder(itemView);

        }

        @Override
        public void onBindViewHolder(HomeAdapter.MyViewHolder holder, int position) {


            holder.txtDate.setText(clientData.get(position).getDate());
            holder.txtClientName.setText(clientData.get(position).getName());
            holder.txtAmtRec.setText(clientData.get(position).getAmtReceived());
            holder.txtAmtPending.setText(clientData.get(position).getAmtPending());
        }

        @Override
        public int getItemCount() {

            return clientData.size();
        }



        public class MyViewHolder extends RecyclerView.ViewHolder{

            public TextView txtDate,txtClientName,txtAmtRec,txtAmtPending;

            public MyViewHolder(View itemView) {
                super(itemView);


                txtDate = (TextView) itemView.findViewById(R.id.txtDate);
                txtClientName = (TextView) itemView.findViewById(R.id.txtClientName);
                txtAmtRec = (TextView) itemView.findViewById(R.id.txtAmtRec);
                txtAmtPending = (TextView) itemView.findViewById(R.id.txtAmtPending);
            }
        }
    }
}
