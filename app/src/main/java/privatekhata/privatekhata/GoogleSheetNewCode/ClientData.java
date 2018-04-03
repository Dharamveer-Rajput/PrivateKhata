package privatekhata.privatekhata.GoogleSheetNewCode;

/**
 * Created by dharamveer on 6/12/17.
 */

public class ClientData {

    private String date;
    private String name;
    private int AmtReceived, AmtPending;

    public ClientData( String date, String name, int amtReceived, int amtPending) {
        this.date = date;
        this.name = name;
        AmtReceived = amtReceived;
        AmtPending = amtPending;
    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAmtReceived() {
        return AmtReceived;
    }

    public void setAmtReceived(int amtReceived) {
        AmtReceived = amtReceived;
    }

    public int getAmtPending() {
        return AmtPending;
    }

    public void setAmtPending(int amtPending) {
        AmtPending = amtPending;
    }
}
