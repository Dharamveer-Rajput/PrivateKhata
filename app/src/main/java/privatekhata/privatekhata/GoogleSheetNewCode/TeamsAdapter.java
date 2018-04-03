package privatekhata.privatekhata.GoogleSheetNewCode;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import privatekhata.privatekhata.R;


public class TeamsAdapter extends ArrayAdapter<ClientData> {

    Context context;
    private ArrayList<ClientData> clientData;

    public TeamsAdapter(Context context, int textViewResourceId, ArrayList<ClientData> clientData) {
        super(context, textViewResourceId, clientData);
        this.context = context;
        this.clientData = clientData;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.team, null);
        }
        ClientData o = clientData.get(position);
        if (o != null) {
            TextView date = (TextView) v.findViewById(R.id.date);
            TextView name1 = (TextView) v.findViewById(R.id.name);
            TextView amtreceived = (TextView) v.findViewById(R.id.amtreceived);
            TextView amtpending = (TextView) v.findViewById(R.id.amtpending);

            date.setText(String.valueOf(o.getDate()));
            name1.setText(String.valueOf(o.getName()));
            amtreceived.setText(String.valueOf(o.getAmtReceived()));
            amtpending.setText(String.valueOf(o.getAmtPending()));
        }
        return v;
    }
}