package com.ensharp.seoul.seoultheplace;

public class DetailInformationVO {
    private String imageSource;
    private String category;
    private String detail;

    public DetailInformationVO(String imageSource, String category, String detail) {
        this.imageSource = imageSource;
        this.category = category;
        this.detail = detail;
    }

    public String getImageSource() { return imageSource; }

    public String getCategory() { return category; }

    public String getDetail() { return detail; }
}
