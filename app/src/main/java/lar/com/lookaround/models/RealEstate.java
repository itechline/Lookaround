package lar.com.lookaround.models;

import android.graphics.Picture;

import java.util.ArrayList;

/**
 * Created by Attila_Dan on 16. 04. 08..
 */
public class RealEstate {
    private String adress;
    private String street;
    private String description;
    private String price;
    private boolean isFavourite;
    //private Picture estateMainPic;

    public RealEstate(int id, String adress, String street, String description, String price, boolean isFavourite) {
        this.adress = adress;
        this.street = street;
        this.description = description;
        this.price = price;
        this.isFavourite = isFavourite;
    }


    public static ArrayList<RealEstate> getUsers() {
        ArrayList<RealEstate> estates = new ArrayList<RealEstate>();
        estates.add(new RealEstate(0001,"Nyírbátor", "Zrínyi 72", "Úgybizonydááá", "69 Huf", true));
        estates.add(new RealEstate(0002,"Nyírpócs", "Street", "Igen", "6969 Huf", false));
        estates.add(new RealEstate(0003,"Asdf", "Street2", "Nem", "100 Huf", true));
        estates.add(new RealEstate(0004,"Asdf2", "Stree3", "Lofasz", "6969 Huf", false));
        estates.add(new RealEstate(0005,"Asdf3", "Street4", "Hogyishijjak", "100 Huf", false));
        return estates;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public boolean isFavourite() {
        return isFavourite;
    }

    public void setIsFavourite(boolean isFavourite) {
        this.isFavourite = isFavourite;
    }



    /*public Picture getEstateMainPic() {
        return estateMainPic;
    }

    public void setEstateMainPic(Picture estateMainPic) {
        this.estateMainPic = estateMainPic;
    }*/
}
