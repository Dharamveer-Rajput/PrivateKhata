package privatekhata.privatekhata.SharedPreferences;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by dharamveer on 5/12/17.
 */

public class UserDataUtility {


    public static boolean getLogin(Context context)
    {

        SharedPreferences sharedPreferences = context.getSharedPreferences("lll",Context.MODE_PRIVATE);
        return  sharedPreferences.getBoolean("first",false);
    }


    public static void setLogin(boolean login, Context context)
    {

        SharedPreferences sharedPreferences = context.getSharedPreferences("lll",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("first",login);
        editor.clear();
        editor.apply();
        editor.commit();

    }

}
