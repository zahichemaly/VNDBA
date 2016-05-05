package com.booboot.vndbandroid.factory;

/**
 * Created by od on 05/05/2016.
 */
public class PlaceholderPictureFactory {
    public final static boolean USE_PLACEHOLDER = false;
    public final static String[] PLACEHOLDER_PICTURES = new String[]{
            "http://image.noelshack.com/fichiers/2016/18/1462434856-mb-bg-fb-26.png",
            "http://image.noelshack.com/fichiers/2016/18/1462434797-mb-bg-fb-08.png",
            "http://image.noelshack.com/fichiers/2016/18/1462434817-mb-bg-fb-16.png",
            "http://image.noelshack.com/fichiers/2016/18/1462434801-mb-bg-fb-12.png",
            "http://image.noelshack.com/fichiers/2016/18/1462434778-mb-bg-fb-04.png",
            "http://image.noelshack.com/fichiers/2016/18/1462434777-mb-bg-fb-06.png",
            "http://image.noelshack.com/fichiers/2016/18/1462434797-mb-bg-fb-07.png",
            "http://image.noelshack.com/fichiers/2016/18/1462434840-mb-bg-fb-22.png",
            "http://image.noelshack.com/fichiers/2016/18/1462434839-mb-bg-fb-20.png",
            "http://image.noelshack.com/fichiers/2016/18/1462434778-mb-bg-fb-01.png",
            "http://image.noelshack.com/fichiers/2016/18/1462434778-mb-bg-fb-02.png",
            "http://image.noelshack.com/fichiers/2016/18/1462434777-mb-bg-fb-05.png",
            "http://image.noelshack.com/fichiers/2016/18/1462434799-mb-bg-fb-10.png",
            "http://image.noelshack.com/fichiers/2016/18/1462434798-mb-bg-fb-09.png",
            "http://image.noelshack.com/fichiers/2016/18/1462434832-mb-bg-fb-19.png",
            "http://image.noelshack.com/fichiers/2016/18/1462434839-mb-bg-fb-21.png",
            "http://image.noelshack.com/fichiers/2016/18/1462434817-mb-bg-fb-15.png",
            "http://image.noelshack.com/fichiers/2016/18/1462434813-mb-bg-fb-13.png",
            "http://image.noelshack.com/fichiers/2016/18/1462434858-mb-bg-fb-27.png",
            "http://image.noelshack.com/fichiers/2016/18/1462434858-mb-bg-fb-30.png",
            "http://image.noelshack.com/fichiers/2016/18/1462434841-mb-bg-fb-24.png",
            "http://image.noelshack.com/fichiers/2016/18/1462434841-mb-bg-fb-23.png",
            "http://image.noelshack.com/fichiers/2016/18/1462434822-mb-bg-fb-18.png",
            "http://image.noelshack.com/fichiers/2016/18/1462434821-mb-bg-fb-17.png",
            "http://image.noelshack.com/fichiers/2016/18/1462434856-mb-bg-fb-28.png",
            "http://image.noelshack.com/fichiers/2016/18/1462434819-mb-bg-fb-14.png",
            "http://image.noelshack.com/fichiers/2016/18/1462434777-mb-bg-fb-03.png",
            "http://image.noelshack.com/fichiers/2016/18/1462434799-mb-bg-fb-11.png",
            "http://image.noelshack.com/fichiers/2016/18/1462434852-mb-bg-fb-25.png",
            "http://image.noelshack.com/fichiers/2016/18/1462434859-mb-bg-fb-29.png"
    };
    public static int placeholderIndex = 0;

    public static String getPlaceholderPicture() {
        String res = PLACEHOLDER_PICTURES[placeholderIndex];
        placeholderIndex = (placeholderIndex + 1) % PLACEHOLDER_PICTURES.length;
        return res;
    }
}
