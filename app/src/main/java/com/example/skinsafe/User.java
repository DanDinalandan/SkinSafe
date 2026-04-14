package com.example.skinsafe;

import java.util.ArrayList;
import java.util.List;

public class User {
    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private String passwordHash;
    private List<String> skinTypes;
    private List<String> skinConcerns;
    private int totalScans;
    private int flaggedCount;
    private int savedCount;

    public User() {
        skinTypes = new ArrayList<>();
        skinConcerns = new ArrayList<>();
    }

    public User(String firstName, String lastName, String email, String passwordHash) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.passwordHash = passwordHash;
        this.skinTypes = new ArrayList<>();
        this.skinConcerns = new ArrayList<>();
        this.totalScans = 0;
        this.flaggedCount = 0;
        this.savedCount = 0;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getFullName() { return firstName + " " + lastName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public List<String> getSkinTypes() { return skinTypes; }
    public void setSkinTypes(List<String> skinTypes) { this.skinTypes = skinTypes; }
    public void addSkinType(String type) { skinTypes.add(type); }

    public List<String> getSkinConcerns() { return skinConcerns; }
    public void setSkinConcerns(List<String> concerns) { this.skinConcerns = concerns; }
    public void addSkinConcern(String concern) { skinConcerns.add(concern); }

    public int getTotalScans() { return totalScans; }
    public void setTotalScans(int totalScans) { this.totalScans = totalScans; }

    public int getFlaggedCount() { return flaggedCount; }
    public void setFlaggedCount(int flaggedCount) { this.flaggedCount = flaggedCount; }

    public int getSavedCount() { return savedCount; }
    public void setSavedCount(int savedCount) { this.savedCount = savedCount; }

    public String getInitials() {
        String f = (firstName != null && !firstName.isEmpty()) ? String.valueOf(firstName.charAt(0)) : "";
        String l = (lastName != null && !lastName.isEmpty()) ? String.valueOf(lastName.charAt(0)) : "";
        return (f + l).toUpperCase();
    }

    public String getSkinTypesDisplay() {
        return String.join(", ", skinTypes);
    }
}
