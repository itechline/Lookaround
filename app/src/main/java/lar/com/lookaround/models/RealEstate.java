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
        estates.add(new RealEstate(0001,"Nyírbátor", "Zrínyi 72", "Úgybizonydááá", "69 Huf", true, "http://interiii.com/wp-content/uploads/2013/03/Best-R-House-a-Minimalist-Villa-in-Hungary-House-Design-Pictures.jpg"));
        estates.add(new RealEstate(0002,"Nyírpócs", "Street", "Igen", "6969 Huf", false, "http://o.homedsgn.com/wp-content/uploads/2012/02/House-in-a-Hungarian-Town-01-800x800.jpg"));
        estates.add(new RealEstate(0003,"Asdf", "Street2", "Nem", "100 Huf", true, "http://cdn.designrulz.com/wp-content/uploads/2015/01/Holiday-Cottage_designrulz-1.jpg"));
        estates.add(new RealEstate(0004,"Asdf2", "Stree3", "Lofasz", "6969 Huf", false, "https://s-media-cache-ak0.pinimg.com/736x/58/69/cb/5869cb4d71b26273e2baa31502d084c9.jpg"));
        estates.add(new RealEstate(0005,"Asdf3", "Street4", "Hogyishijjak", "100 Huf", false, "http://assets.inhabitat.com/wp-content/blogs.dir/1/files/2012/09/ODOO-by-Team-Hungary-SDE-2012-lead.jpg"));
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
