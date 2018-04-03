package privatekhata.privatekhata.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import privatekhata.privatekhata.DataBaseHelper.DataBaseHelper;
import privatekhata.privatekhata.R;


public class LoginActivity extends AppCompatActivity {


    private final AppCompatActivity activity = LoginActivity.this;

    public EditText edName,edPassword;
    public Button btnsignin;
    private DataBaseHelper databaseHelper;
    public String strUserName,strPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        initViews();
        initListeners();
        initObjects();

    }

    private void initObjects() {

        databaseHelper = new DataBaseHelper(activity);

    }

    private void initListeners() {

        btnsignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean errorOccurred = false;

                strUserName = edName.getText().toString().trim();
                strPassword = edPassword.getText().toString().trim();

                if (strUserName.equals("")) {
                    edName.setError("Username is required!");
                    errorOccurred = true;
                }

                if (strPassword.equals("")) {
                    edPassword.setError("Password is required!");
                    errorOccurred = true;
                }

                if (errorOccurred) {


                    return; // avoids executing the part of your code which queries the db
                }
                else if(!strUserName.equals("admin")&& !strPassword.equals("123")){


                    AlertDialog.Builder builder1 = new AlertDialog.Builder(LoginActivity.this);
                    builder1.setMessage("Username & Password is not valid");
                    builder1.setCancelable(true);

                    builder1.setPositiveButton(
                            "Yes",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

                    builder1.setNegativeButton(
                            "No",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                    return;


                }
                Intent intent = new Intent(LoginActivity.this,ProfileActivity.class);
                startActivity(intent);


            }
        });

    }

    private void initViews() {

        edName = findViewById(R.id.edName);
        edPassword = findViewById(R.id.edPassword);

        btnsignin = findViewById(R.id.btnsignin);


    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        LoginActivity.this.finish();
        moveTaskToBack(true);

    }
}
