package lar.com.lookaround.models;

/**
 * Created by laci on 16. 06. 22..
 */
public class Idopont {

    private int id;
    private int iid;
    private int status;
    private String datum;
    private int felid;
    private String fel;

    public String getFel() {
        return fel;
    }

    public void setFel(String fel) {
        this.fel = fel;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIid() {
        return iid;
    }

    public void setIid(int iid) {
        this.iid = iid;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getDatum() {
        return datum;
    }

    public void setDatum(String datum) {
        this.datum = datum;
    }

    public int getFelid() {
        return felid;
    }

    public void setFelid(int felid) {
        this.felid = felid;
    }
}
