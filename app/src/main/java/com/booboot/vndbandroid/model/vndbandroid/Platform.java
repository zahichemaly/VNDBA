package com.booboot.vndbandroid.model.vndbandroid;

import com.booboot.vndbandroid.R;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by od on 12/03/2016.
 */
public class Platform {
    public final static Map<String, String> FULL_TEXT = new HashMap<>();
    public final static Map<String, Integer> IMAGES = new HashMap<>();

    static {
        FULL_TEXT.put("win", "Windows");
        FULL_TEXT.put("dos", "DOS");
        FULL_TEXT.put("lin", "Linux");
        FULL_TEXT.put("mac", "Mac OS");
        FULL_TEXT.put("ios", "Apple iProduct");
        FULL_TEXT.put("and", "Android");
        FULL_TEXT.put("dvd", "DVD Player");
        FULL_TEXT.put("bdp", "Blu-ray Player");
        FULL_TEXT.put("fmt", "FM Towns");
        FULL_TEXT.put("gba", "Game Boy Advance");
        FULL_TEXT.put("gbc", "Game Boy Color");
        FULL_TEXT.put("msx", "MSX");
        FULL_TEXT.put("nds", "Nintendo DS");
        FULL_TEXT.put("nes", "Famicom");
        FULL_TEXT.put("p88", "PC-88");
        FULL_TEXT.put("p98", "PC-98");
        FULL_TEXT.put("pce", "PC Engine");
        FULL_TEXT.put("pcf", "PC-FX");
        FULL_TEXT.put("psp", "PlayStation Portable");
        FULL_TEXT.put("ps1", "PlayStation 1");
        FULL_TEXT.put("ps2", "PlayStation 2");
        FULL_TEXT.put("ps3", "PlayStation 3");
        FULL_TEXT.put("ps4", "PlayStation 4");
        FULL_TEXT.put("psv", "PlayStation Vita");
        FULL_TEXT.put("drc", "Dreamcast");
        FULL_TEXT.put("sat", "Sega Saturn");
        FULL_TEXT.put("sfc", "Super Nintendo");
        FULL_TEXT.put("wii", "Nintendo Wii");
        FULL_TEXT.put("n3d", "Nintendo 3DS");
        FULL_TEXT.put("x68", "X68000");
        FULL_TEXT.put("xb1", "Xbox");
        FULL_TEXT.put("xb3", "Xbox 360");
        FULL_TEXT.put("xbo", "Xbox One");
        FULL_TEXT.put("web", "Website");
        FULL_TEXT.put("oth", "Other");

        IMAGES.put("win", R.drawable.win);
        IMAGES.put("dos", R.drawable.dos);
        IMAGES.put("lin", R.drawable.lin);
        IMAGES.put("mac", R.drawable.mac);
        IMAGES.put("ios", R.drawable.ios);
        IMAGES.put("and", R.drawable.and);
        IMAGES.put("dvd", R.drawable.dvd);
        IMAGES.put("bdp", R.drawable.bdp);
        IMAGES.put("fmt", R.drawable.fmt);
        IMAGES.put("gba", R.drawable.gba);
        IMAGES.put("gbc", R.drawable.gbc);
        IMAGES.put("msx", R.drawable.msx);
        IMAGES.put("nds", R.drawable.nds);
        IMAGES.put("nes", R.drawable.nes);
        IMAGES.put("p88", R.drawable.p88);
        IMAGES.put("p98", R.drawable.p98);
        IMAGES.put("pce", R.drawable.pce);
        IMAGES.put("pcf", R.drawable.pcf);
        IMAGES.put("psp", R.drawable.psp);
        IMAGES.put("ps1", R.drawable.ps1);
        IMAGES.put("ps2", R.drawable.ps2);
        IMAGES.put("ps3", R.drawable.ps3);
        IMAGES.put("ps4", R.drawable.ps4);
        IMAGES.put("psv", R.drawable.psv);
        IMAGES.put("drc", R.drawable.drc);
        IMAGES.put("sat", R.drawable.sat);
        IMAGES.put("sfc", R.drawable.sfc);
        IMAGES.put("wii", R.drawable.wii);
        IMAGES.put("n3d", R.drawable.n3d);
        IMAGES.put("x68", R.drawable.x68);
        IMAGES.put("xb1", R.drawable.xb1);
        IMAGES.put("xb3", R.drawable.xb3);
        IMAGES.put("xbo", R.drawable.xbo);
        IMAGES.put("web", R.drawable.web);
        IMAGES.put("oth", R.drawable.oth);
    }
}
