package com.ensharp.seoul.seoultheplace.Course;

public class LocationDistance {
    public static void main(String[] args) {
    }
    /**
     *  * 두 지점간의 거리 계산 * *
     * @param lat1 지점 1 위도 *
     * @param lon1 지점 1 경도 *
     * @param lat2 지점 2 위도 *
     * @param lon2 지점 2 경도 *
     * @param unit 거리 표출단위 *
     * @return
     * */
    public static int distance(String lat1, String lon1, String lat2, String lon2, String unit) {
        double theta = Double.parseDouble(lon1) - Double.parseDouble(lon2);
        double dist = Math.sin(deg2rad(Double.parseDouble(lat1)))
                * Math.sin(deg2rad(Double.parseDouble(lat2)))
                + Math.cos(deg2rad(Double.parseDouble(lat1)))
                * Math.cos(deg2rad(Double.parseDouble(lat2)))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        if (unit == "kilometer") {
            dist = dist * 1.609344;
        } else if(unit == "meter"){
            dist = dist * 1609.344;
        }
        return (int)dist;
    }
        // This function converts decimal degrees to radians
    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }
    // This function converts radians to decimal degrees
    private static double rad2deg(double rad) {
        return (rad * 180 / Math.PI);
    }
}
