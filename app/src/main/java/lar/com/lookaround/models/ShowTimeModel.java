package lar.com.lookaround.models;

/**
 * Created by laci on 16. 07. 01..
 */
public class ShowTimeModel {

    private int hetfo;
    private int kedd;
    private int szerda;
    private int csutortok;
    private int pentek;
    private int szombat;
    private int vasarnap;
    private String fromtime;
    private String totime;

    public ShowTimeModel() {
        hetfo = 0;
        kedd = 0;
        szerda = 0;
        csutortok = 0;
        pentek = 0;
        szombat = 0;
        vasarnap = 0;
        fromtime = "";
        totime = "";
    }

    public int getKedd() {
        return kedd;
    }

    public void setKedd(int kedd) {
        this.kedd = kedd;
    }

    public int getSzerda() {
        return szerda;
    }

    public void setSzerda(int szerda) {
        this.szerda = szerda;
    }

    public int getCsutortok() {
        return csutortok;
    }

    public void setCsutortok(int csutortok) {
        this.csutortok = csutortok;
    }

    public int getPentek() {
        return pentek;
    }

    public void setPentek(int pentek) {
        this.pentek = pentek;
    }

    public int getSzombat() {
        return szombat;
    }

    public void setSzombat(int szombat) {
        this.szombat = szombat;
    }

    public int getVasarnap() {
        return vasarnap;
    }

    public void setVasarnap(int vasarnap) {
        this.vasarnap = vasarnap;
    }

    public String getFromtime() {
        return fromtime;
    }

    public void setFromtime(String fromtime) {
        this.fromtime = fromtime;
    }

    public String getTotime() {
        return totime;
    }

    public void setTotime(String totime) {
        this.totime = totime;
    }

    public int getHetfo() {

        return hetfo;
    }

    public void setHetfo(int hetfo) {
        this.hetfo = hetfo;
    }
}
