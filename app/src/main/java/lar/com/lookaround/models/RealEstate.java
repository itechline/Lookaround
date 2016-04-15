package lar.com.lookaround.models;

import android.graphics.Picture;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Attila_Dan on 16. 04. 08..
 */
public class RealEstate {
    int id;
    private String adress;
    private String street;
    private String description;
    private String price;
    private boolean isFavourite;



    private String urls;
    //private Picture estateMainPic;

    public RealEstate(int id, String adress, String street, String description, String price, boolean isFavourite, String urls) {
        this.id = id;
        this.adress = adress;
        this.street = street;
        this.description = description;
        this.price = price;
        this.isFavourite = isFavourite;
        this.urls = urls;
    }


    public static ArrayList<RealEstate> getUsers() {
        ArrayList<RealEstate> estates = new ArrayList<RealEstate>();
        estates.add(new RealEstate(0001,"Nyírbátor", "Zrínyi 72", "Úgybizonydááá", "69 Huf", true, "http://completehome.com.au/wp-content/uploads/2013/07/real-home-interior-house-rejuvenated8.jpg"));
        estates.add(new RealEstate(0002,"Nyírpócs", "Street", "Igen", "6969 Huf", false, "https://www.withfriendship.com/images/i/40443/shotgun-house.jpg"));
        estates.add(new RealEstate(0003,"Asdf", "Street2", "Nem", "100 Huf", true, "http://images.locanto.ph/1155062564/House-and-Lot-for-SALE-in-a-SUBDIVISION-near-METRO-MANILA-Cavite_1.jpg"));
        estates.add(new RealEstate(0004,"Asdf2", "Stree3", "Lofasz", "6969 Huf", false, "http://materia.nl/wp-content/uploads/2015/11/beautifully-textured-house-innovates-with-hemp-based-material-01-300x400.jpg"));
        estates.add(new RealEstate(0005,"Asdf3", "Street4", "Hogyishijjak", "100 Huf", false, "https://psyc2301.wikispaces.com/file/view/dr-house.jpg/132240505/dr-house.jpg"));
        return estates;
    }

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

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

    public String getUrls() {
        return urls;
    }

    public void setUrls(String urls) {
        this.urls = urls;
    }



    /*public Picture getEstateMainPic() {
        return estateMainPic;
    }

    public void setEstateMainPic(Picture estateMainPic) {
        this.estateMainPic = estateMainPic;
    }*/
}
