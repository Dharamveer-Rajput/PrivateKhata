package privatekhata.privatekhata.DataBaseHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import privatekhata.privatekhata.GoogleSheetNewCode.ClientData;
import privatekhata.privatekhata.User;


public class DataBaseHelper extends SQLiteOpenHelper {


    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "PaymentsRecord";

    // User table name
    private static final String TABLE_NAME = "payments";

    // User Table Columns names

    private static final String COLUMN_ID = "id";
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_CLIENT_NAME = "client_name";
    private static final String COLUMN_AMOUNT_RECEIVED = "amt_received";
    private static final String COLUMN_AMOUNT_PENDING = "amt_pending";



    private String CREATE_USER_TABLE = " CREATE TABLE " + TABLE_NAME + " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_DATE + " TEXT NOT NULL, " +
            COLUMN_CLIENT_NAME + " TEXT NOT NULL, " +
            COLUMN_AMOUNT_RECEIVED + " INTEGER NOT NULL, " +
            COLUMN_AMOUNT_PENDING + " INTEGER NOT NULL);";


    // drop table sql query
    private String DROP_USER_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    public DataBaseHelper(Context context)

    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void deleteAllTableData(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+ TABLE_NAME);
        db.close();

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_USER_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Drop User Table if exist
        db.execSQL(DROP_USER_TABLE);

        // Create tables again
        onCreate(db);
    }


    public void insertData(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_DATE, user.getDate());
        values.put(COLUMN_CLIENT_NAME, user.getClientName());
        values.put(COLUMN_AMOUNT_RECEIVED, user.getAmtReceived());
        values.put(COLUMN_AMOUNT_PENDING, user.getAmtPending());

        // Inserting Row
        db.insert(TABLE_NAME, null, values);
        db.close();
    }


    public List<ClientData> getAllPayments() {

        List<ClientData> clientData = new ArrayList<ClientData>();

        SQLiteDatabase db = this.getWritableDatabase();


        Cursor cursor = db.rawQuery("select * from "+TABLE_NAME+" ;",null);
        StringBuffer stringBuffer = new StringBuffer();
        User user = null;

        // Traversing through all rows and adding to list
        while (cursor.moveToNext()) {

                // user = new ClientData();
                user.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_ID))));
                user.setDate(cursor.getString(cursor.getColumnIndex(COLUMN_DATE)));
                user.setClientName(cursor.getString(cursor.getColumnIndex(COLUMN_CLIENT_NAME)));
                user.setAmtReceived(cursor.getString(cursor.getColumnIndex(COLUMN_AMOUNT_RECEIVED)));
                user.setAmtPending(cursor.getString(cursor.getColumnIndex(COLUMN_AMOUNT_PENDING)));
                // Adding user record to list
         //   clientData.add(user);
            }

        cursor.close();
        db.close();

        // return user list
        return clientData;
    }




}
