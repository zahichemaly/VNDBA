package com.booboot.vndbandroid.model.vndbandroid

import androidx.annotation.ColorRes
import com.booboot.vndbandroid.R

data class Platform(
    val text: String = "",
    @ColorRes val color: Int = 0
)

val PLATFORMS = mapOf(
    "web" to Platform("Web", R.color.web),
    "win" to Platform("PC", R.color.pc),
    "dos" to Platform("DOS", R.color.dos),
    "lin" to Platform("Linux", R.color.linux),
    "mac" to Platform("Mac", R.color.mac),
    "ios" to Platform("iOS", R.color.ios),
    "and" to Platform("Android", R.color.android),
    "dvd" to Platform("DVD", R.color.dvd),
    "bdp" to Platform("Blu-ray", R.color.bluray),
    "fmt" to Platform("FM Towns", R.color.fmtowns),
    "gba" to Platform("GBA", R.color.gba),
    "gbc" to Platform("GBC", R.color.gbc),
    "msx" to Platform("MSX", R.color.msx),
    "nds" to Platform("DS", R.color.ds),
    "nes" to Platform("NES", R.color.nes),
    "p88" to Platform("PC-88", R.color.pc88),
    "p98" to Platform("PC-98", R.color.pc98),
    "pce" to Platform("PC Engine", R.color.pcengine),
    "pcf" to Platform("PC-FX", R.color.pcfx),
    "psp" to Platform("PSP", R.color.psp),
    "ps1" to Platform("PS1", R.color.ps1),
    "ps2" to Platform("PS2", R.color.ps2),
    "ps3" to Platform("PS3", R.color.ps3),
    "ps4" to Platform("PS4", R.color.ps4),
    "psv" to Platform("PS Vita", R.color.psvita),
    "drc" to Platform("Dreamcast", R.color.dreamcast),
    "sat" to Platform("Saturn", R.color.saturn),
    "sfc" to Platform("SNES", R.color.snes),
    "wii" to Platform("Wii", R.color.wii),
    "wiu" to Platform("Wii U", R.color.wiiu),
    "swi" to Platform("Switch", R.color.nswitch),
    "n3d" to Platform("3DS", R.color._3ds),
    "x68" to Platform("X68000", R.color.x68),
    "xb1" to Platform("Xbox", R.color.xbox),
    "xb3" to Platform("X360", R.color.xbox360),
    "xbo" to Platform("XOne", R.color.xboxone),
    "oth" to Platform("Other", R.color.other)
)
