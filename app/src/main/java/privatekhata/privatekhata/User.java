package privatekhata.privatekhata;

/**
 * Created by dharamveer on 13/11/17.
 */

public class User  {


    public int id;

    public String date,clientName,amtReceived,amtPending;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getAmtReceived() {
        return amtReceived;
    }

    public void setAmtReceived(String amtReceived) {
        this.amtReceived = amtReceived;
    }

    public String getAmtPending() {
        return amtPending;
    }

    public void setAmtPending(String amtPending) {
        this.amtPending = amtPending;
    }
}
