package com.example.donorapp.DonationListing;

import java.io.Serializable;
import java.util.List;

public class    Donation implements Serializable {
    public final String id;
    public final String donorId;
    public String title;
    public String description;
    public String status;

    public Donation(String id, String donorId, String title, String description, String status) {
        this.id = id;
        this.donorId = donorId;
        this.title = title;
        this.description = description;
        this.status = status;
    }

    @Override
    public String toString() {
        return String.format("Donation {}: {}", id, title);
    }
}