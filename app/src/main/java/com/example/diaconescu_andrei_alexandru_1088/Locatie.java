package com.example.diaconescu_andrei_alexandru_1088;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class Locatie implements Serializable {
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private String nume;
    private float rating; // 0 - 5
    private int puncte;
    private String tip = "urban";

    public JSONObject toJson() throws JSONException, JSONException {
        JSONObject json = new JSONObject();
        json.put("nume", nume != null ? nume : "");
        json.put("rating", rating);
        json.put("puncte", puncte);
        json.put("tip", tip != null ? tip : "");
        json.put("adresa", adresa != null ? adresa : "");
        return json;
    }
    public static Locatie fromJson(JSONObject json) {
        String nume = json.optString("nume", "");
        float rating = (float) json.optDouble("rating", 0.0);
        int puncte = json.optInt("puncte", 0);
        String tip = json.optString("tip", "urban");
        String adresa = json.optString("adresa", "");
        return new Locatie(adresa, tip, puncte, rating, nume);
    }

    public Locatie(String adresa, String tip, int puncte, float rating, String nume) {
        this.adresa = adresa;
        this.tip = tip;
        this.puncte = puncte;
        this.rating = rating;
        this.nume = nume;
    }

    @Override
    public String toString() {
        return "Locatie{" +
                "nume='" + nume + '\'' +
                ", rating=" + rating +
                ", puncte=" + puncte +
                ", tip='" + tip + '\'' +
                ", adresa='" + adresa + '\'' +
                '}';
    }

    private String adresa;
    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public int getPuncte() {
        return puncte;
    }

    public void setPuncte(int puncte) {
        this.puncte = puncte;
    }

    public String getAdresa() {
        return adresa;
    }

    public void setAdresa(String adresa) {
        this.adresa = adresa;
    }

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }
}
