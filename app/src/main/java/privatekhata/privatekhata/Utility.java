package privatekhata.privatekhata;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by dharamveer on 6/12/17.
 */

public class Utility {



    Context context;
    static SharedPreferences sharedPreferences;

    String newClient;
    String nameOfClient;
    String userEmail;
    String imageUrl;



    public Utility(Context context) {
        this.context = context;
        this.sharedPreferences = context.getSharedPreferences("userinfo", Context.MODE_PRIVATE);;

    }


    public String getImageUrl() {
        imageUrl = sharedPreferences.getString("imageUrl", "");

        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        sharedPreferences.edit().putString("imageUrl", imageUrl).commit();

    }

    public String getNameOfClient() {
        nameOfClient = sharedPreferences.getString("nameOfClient", "");

        return nameOfClient;
    }

    public void setNameOfClient(String nameOfClient) {
        this.nameOfClient = nameOfClient;
        sharedPreferences.edit().putString("nameOfClient", nameOfClient).commit();

    }

    public String getUserEmail() {
        userEmail = sharedPreferences.getString("userEmail", "");

        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
        sharedPreferences.edit().putString("userEmail", userEmail).commit();

    }

    public String getNewClient() {
        newClient = sharedPreferences.getString("newClient", "");

        return newClient;
    }

    public void setNewClient(String newClient) {
        this.newClient = newClient;
        sharedPreferences.edit().putString("newClient", newClient).commit();

    }
}
