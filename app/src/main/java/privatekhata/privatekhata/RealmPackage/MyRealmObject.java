package privatekhata.privatekhata.RealmPackage;

import io.realm.RealmObject;

/**
 * Created by dharamveer on 13/11/17.
 */

public class MyRealmObject extends RealmObject {


    private String Name;
    private String Password;



    public MyRealmObject() {
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }
}
