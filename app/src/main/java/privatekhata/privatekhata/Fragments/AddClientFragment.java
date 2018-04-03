package privatekhata.privatekhata.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import privatekhata.privatekhata.R;
import privatekhata.privatekhata.Utility;

/**
 * Created by dharamveer on 5/12/17.
 */

public class AddClientFragment extends Fragment {


    EditText edClientName,edPhone,edAadhaarNo,edEmailAddress,edBussiAddress;
    Button btnSubmitData;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.add_client_frag,container,false);


        edClientName = view.findViewById(R.id.edClientName);

        edPhone = view.findViewById(R.id.edPhone);
        edAadhaarNo = view.findViewById(R.id.edAadhaarNo);
        edEmailAddress = view.findViewById(R.id.edEmailAddress);
        edBussiAddress = view.findViewById(R.id.edBussiAddress);

        btnSubmitData = view.findViewById(R.id.btnSubmitData);





        btnSubmitData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                if(edClientName.getText().toString().length()==0){
                    edClientName.setError("Enter client name");
                }


                else if(edPhone.getText().toString().length()==0){
                    edPhone.setError("Enter phone no");
                }



                else if(edAadhaarNo.getText().toString().length()==0){
                    edAadhaarNo.setError("Enter aadhaar no");
                }


                else if(edEmailAddress.getText().toString().length()==0){
                    edEmailAddress.setError("Enter email address ");
                }

                else if(edBussiAddress.getText().toString().length()==0){
                    edBussiAddress.setError("Enter bussiness address");
                }

                else {

                    Utility utility = new Utility(getActivity());

                    utility.setNewClient(edClientName.getText().toString());
                }
            }
        });

        return view;

    }
}
