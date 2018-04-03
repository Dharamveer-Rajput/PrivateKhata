package privatekhata.privatekhata.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import privatekhata.privatekhata.CircleImageView.CircleImageView;
import privatekhata.privatekhata.R;
import privatekhata.privatekhata.SquareImageView.SquareImageView;
import privatekhata.privatekhata.Utility;




public class AccountDetails extends Fragment {


    private CircleImageView profileImage;
    private TextView txtNameClient,txtEmail;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.account_frag_lay,container,false);


        profileImage = view.findViewById(R.id.profileImage);
        txtNameClient = view.findViewById(R.id.txtNameClient);
        txtEmail = view.findViewById(R.id.txtEmail);


        Utility utility = new Utility(getActivity());

        txtNameClient.setText(utility.getNameOfClient());
        txtEmail.setText(utility.getUserEmail());



        Glide.with(getActivity()).load(utility.getImageUrl())
                .thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(profileImage);


        return view;

    }
}
