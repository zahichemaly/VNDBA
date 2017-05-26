package com.booboot.vndbandroid.model.vndb;

import com.booboot.vndbandroid.R;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class StaffSummary {
    private int id;
    private int sid;
    private int aid;
    private int vid;
    private String name;
    private String original;
    private String role;
    private String note;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getVid() {
        return vid;
    }

    public void setVid(int vid) {
        this.vid = vid;
    }

    public int getSid() {
        return sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    public int getAid() {
        return aid;
    }

    public void setAid(int aid) {
        this.aid = aid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOriginal() {
        return original;
    }

    public void setOriginal(String original) {
        this.original = original;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Integer getIcon() {
        switch (role) {
            case "songs":
                return R.drawable.ic_mic_white_48dp;
            case "staff":
                return R.drawable.ic_assignment_ind_white_48dp;
            case "scenario":
                return R.drawable.ic_text_format_white_48dp;
            case "music":
                return R.drawable.ic_audiotrack_white_48dp;
            case "art":
                return R.drawable.ic_brush_white_48dp;
            case "director":
                return R.drawable.ic_theaters_white_48dp;
            default:
                return R.drawable.ic_assignment_ind_white_48dp;
        }
    }

    @Override
    public String toString() {
        return "StaffSummary{" +
                "id=" + id +
                ", sid=" + sid +
                ", aid=" + aid +
                ", vid=" + vid +
                ", name='" + name + '\'' +
                ", original='" + original + '\'' +
                ", role='" + role + '\'' +
                ", note='" + note + '\'' +
                '}';
    }
}
