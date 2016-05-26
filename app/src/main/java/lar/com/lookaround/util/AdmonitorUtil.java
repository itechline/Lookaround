package lar.com.lookaround.util;

import java.util.ArrayList;

/**
 * Created by Attila_Dan on 16. 05. 25..
 */
public class AdmonitorUtil {
    private int id;
    private String name;
    private String search;
    private String priceMin;
    private String priceMax;
    private int type;
    private int floorsMin;
    private int floorsMax;
    private int roomsMin;
    private int roomsMax;
    private int elevator;
    private int balcony;
    private int size;
    private int view;
    private int hasFurniture;
    private int parking;
    private int condition;
    private int etype;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public String getPriceMin() {
        return priceMin;
    }

    public void setPriceMin(String priceMin) {
        this.priceMin = priceMin;
    }

    public String getPriceMax() {
        return priceMax;
    }

    public void setPriceMax(String priceMax) {
        this.priceMax = priceMax;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getFloorsMin() {
        return floorsMin;
    }

    public void setFloorsMin(int floorsMin) {
        this.floorsMin = floorsMin;
    }

    public int getFloorsMax() {
        return floorsMax;
    }

    public void setFloorsMax(int floorsMax) {
        this.floorsMax = floorsMax;
    }

    public int getRoomsMin() {
        return roomsMin;
    }

    public void setRoomsMin(int roomsMin) {
        this.roomsMin = roomsMin;
    }

    public int getRoomsMax() {
        return roomsMax;
    }

    public void setRoomsMax(int roomsMax) {
        this.roomsMax = roomsMax;
    }

    public int getElevator() {
        return elevator;
    }

    public void setElevator(int elevator) {
        this.elevator = elevator;
    }

    public int getBalcony() {
        return balcony;
    }

    public void setBalcony(int balcony) {
        this.balcony = balcony;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getView() {
        return view;
    }

    public void setView(int view) {
        this.view = view;
    }

    public int getHasFurniture() {
        return hasFurniture;
    }

    public void setHasFurniture(int hasFurniture) {
        this.hasFurniture = hasFurniture;
    }

    public int getParking() {
        return parking;
    }

    public void setParking(int parking) {
        this.parking = parking;
    }

    public int getCondition() {
        return condition;
    }

    public void setCondition(int condition) {
        this.condition = condition;
    }

    public int getEtype() {
        return etype;
    }

    public void setEtype(int etype) {
        this.etype = etype;
    }

    public AdmonitorUtil (int id, String name, String search, String priceMin, String priceMax, int type, int floorsMin, int floorsMax,
            int roomsMin, int roomsMax, int elevator, int balcony, int size, int view, int hasFurniture, int parking, int condition, int etype) {
        this.id = id;
        this.name = name;
        this.search = search;
        this.priceMin = priceMin;
        this.priceMax = priceMax;
        this.type = type;
        this.floorsMin = floorsMin;
        this.floorsMax = floorsMax;
        this.roomsMin = roomsMin;
        this.roomsMax = roomsMax;
        this.elevator = elevator;
        this.balcony = balcony;
        this.size = size;
        this.view = view;
        this.hasFurniture = hasFurniture;
        this.parking = parking;
        this.condition = condition;
        this.etype = etype;
    }
    static ArrayList<AdmonitorUtil> admonitorUtils = new ArrayList<AdmonitorUtil>();
    public static void addAdmonitor(String name, String search, String priceMin, String priceMax, int type, int floorsMin, int floorsMax,
                             int roomsMin, int roomsMax, int elevator, int balcony, int size, int view, int hasFurniture, int parking, int condition, int etype) {

        admonitorUtils.add(new AdmonitorUtil(admonitorUtils.size(),name, search, priceMin, priceMax, type, floorsMin, floorsMax, roomsMin, roomsMax, elevator, balcony, size, view, hasFurniture, parking, condition, etype));
    }

    public static ArrayList<AdmonitorUtil> get_list_admonitors() {
        return admonitorUtils;
    }

    public static void removeAdmonitor(int id) {
        admonitorUtils.remove(id);
    }
}
