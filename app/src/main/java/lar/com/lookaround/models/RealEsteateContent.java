package lar.com.lookaround.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Attila_Dan on 16. 04. 11..
 */
public class RealEsteateContent {
    private int id;
    private String adress;
    private String street;
    private String descriptionFull;
    private String price;
    private boolean isFavourite;
    private List<String> photoURLs = new ArrayList<String>();


    public RealEsteateContent(int id, String adress, String street, String description, String price, boolean isFavourite, List<String> photoURLs) {
        this.id = id;
        this.adress = adress;
        this.street = street;
        this.descriptionFull = description;
        this.price = price;
        this.isFavourite = isFavourite;
        this.photoURLs = photoURLs;
    }


    public static ArrayList<RealEsteateContent> getEstate(int id) {
        ArrayList<RealEsteateContent> estate = new ArrayList<RealEsteateContent>();
        /*estate.add(new RealEsteateContent(0001,"Nyírbátor", "Zrínyi 72", "Úgybizonydááá", "69 Huf", true));
        estate.add(new RealEsteateContent(0002,"Nyírpócs", "Street", "Igen", "6969 Huf", false));
        estate.add(new RealEsteateContent(0003,"Asdf", "Street2", "Nem", "100 Huf", true));
        estate.add(new RealEsteateContent(0004,"Asdf2", "Stree3", "Lofasz", "6969 Huf", false));
        estate.add(new RealEsteateContent(0005,"Asdf3", "Street4", "Hogyishijjak", "100 Huf", false));*/



        return estate;
    }

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public List<String> getPhotoURLs() {
        return photoURLs;
    }

    public void setPhotoURLs(List<String> photoURLs) {
        this.photoURLs = photoURLs;
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

    public String getDescriptionFull() {
        return descriptionFull;
    }

    public void setDescriptionFull(String descriptionFull) {
        this.descriptionFull = descriptionFull;
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

}
