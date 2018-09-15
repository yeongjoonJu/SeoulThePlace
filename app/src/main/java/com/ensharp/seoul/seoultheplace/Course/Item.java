package com.ensharp.seoul.seoultheplace.Course;

public class Item {
    int image;
    String address;

    int getImage() {
        return this.image;
    }
    String getaddress() {
        return this.address;
    }

    Item(int image, String address) {
        this.image = image;
        this.address = address;
    }
}