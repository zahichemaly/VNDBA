package com.booboot.vndbandroid.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by od on 25/04/2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Media extends VNDBCommand {
    public final static Map<String, String> FULL_TEXT = new HashMap<>();
    private String medium;
    private int qty;

    static {
        FULL_TEXT.put("cd", "CD");
        FULL_TEXT.put("dvd", "DVD");
        FULL_TEXT.put("gdr", "GD-ROM");
        FULL_TEXT.put("blr", "Blu-ray disc");
        FULL_TEXT.put("flp", "Floppy");
        FULL_TEXT.put("mrt", "Cartridge");
        FULL_TEXT.put("mem", "Memory card");
        FULL_TEXT.put("umd", "UMD");
        FULL_TEXT.put("nod", "Nintendo Optical Disc");
        FULL_TEXT.put("in", "Internet download");
        FULL_TEXT.put("otc", "Other");
    }

    public String getMedium() {
        return medium;
    }

    public void setMedium(String medium) {
        this.medium = medium;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }
}
