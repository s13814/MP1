package com.example.mp1.DB;

import com.google.android.gms.maps.model.LatLng;

public class Shop {

    private String shopId;
    private String name;
    private String description;
    private Double radius;
    private LatLng location;

    public Shop(String shopId, String name, String description, Double radius, LatLng location) {
        this.shopId = shopId;
        this.name = name;
        this.description = description;
        this.radius = radius;
        this.location = location;
    }

    public String getShopId() {
        return shopId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Double getRadius() {
        return radius;
    }

    public LatLng getLocation() {
        return location;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setRadius(Double radius) {
        this.radius = radius;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }
}
