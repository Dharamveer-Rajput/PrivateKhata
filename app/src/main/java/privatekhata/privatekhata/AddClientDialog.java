package privatekhata.privatekhata;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by dharamveer on 5/12/17.
 */

public class AddClientDialog extends Dialog {

    EditText edClientName;
    Button btnAddClient;
    String clientName;
    public MyOnClickListener myListener;
    Context context;


    public AddClientDialog( Context context,MyOnClickListener myOnClickListener) {
        super(context);
        this.context = context;
        this.myListener = myOnClickListener;
    }

    public interface MyOnClickListener {
        void onButtonClick(String clientName);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        setContentView(R.layout.add_client);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        edClientName = findViewById(R.id.edClientName);
        btnAddClient = findViewById(R.id.btnAddClient);

        btnAddClient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                clientName = edClientName.getText().toString();
                myListener.onButtonClick(clientName);

                dismiss();


            }
        });

    }




}
