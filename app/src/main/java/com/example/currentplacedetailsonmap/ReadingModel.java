package com.example.currentplacedetailsonmap;

import com.google.android.gms.maps.model.LatLng;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class ReadingModel {

    public ReadingModel()
    {
        this.setTimestamp(new Date());
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public LatLng getLatlng() {
        return latlng;
    }

    public void setLatlng(LatLng latlng) {
        this.latlng = latlng;
    }

    public String getPlaceAddress() {
        return placeAddress;
    }

    public void setPlaceAddress(String placeAddress) {
        this.placeAddress = placeAddress;
    }

    public boolean isCoughDetected() {
        return coughDetected;
    }

    public void setCoughDetected(boolean coughDetected) {
        this.coughDetected = coughDetected;
    }

    public int getNumDevicesDetected() {
        return numDevicesDetected;
    }

    public void setNumDevicesDetected(int numDevicesDetected) {
        this.numDevicesDetected = numDevicesDetected;
    }
    private String id;
    private String placeName;
    private LatLng latlng;
    private String placeAddress;
    private boolean coughDetected;
    private int numDevicesDetected;

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    private Date timestamp;

    public static String serialize(ReadingModel m)
    {
        StringBuilder sb = new StringBuilder();
        sb.append(m.getId());
        sb.append("#");
        sb.append(m.getPlaceName());
        sb.append("#");
        sb.append(m.getPlaceAddress());
        sb.append("#");
        LatLng l = m.getLatlng();
        sb.append(l.latitude +","+ l.longitude);
        sb.append("#");
        sb.append(m.isCoughDetected());
        sb.append("#");
        sb.append(m.getNumDevicesDetected());
        sb.append("#");
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZa");
        sb.append(formatter.format(m.getTimestamp()));
        sb.append("#");
        return sb.toString();
    }
    public static ReadingModel deserialize(String s)
    {
        ReadingModel m = new ReadingModel();
        Scanner sc = new Scanner(s);
        sc.useDelimiter("#");
        m.setId(sc.next());
        m.setPlaceName(sc.next());
        m.setPlaceAddress(sc.next());
        String[] latlngString = sc.next().split(",");
        double latitude = Double.parseDouble(latlngString[0]);
        double longitude = Double.parseDouble(latlngString[1]);
        LatLng latlng = new LatLng(latitude, longitude);
        m.setLatlng(latlng);
        m.setCoughDetected(sc.nextBoolean());
        m.setNumDevicesDetected(sc.nextInt());
        String ts = sc.next();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZa");
        try {
            m.setTimestamp(formatter.parse(ts));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return m;
    }
}
