package com.booboot.vndbandroid.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Build;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.booboot.vndbandroid.BuildConfig;
import com.booboot.vndbandroid.R;
import com.booboot.vndbandroid.activity.EmptyActivity;
import com.booboot.vndbandroid.bean.vndbandroid.Mail;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Created by od on 03/04/2016.
 */
public class Utils {
    public final static int PORTRAIT = 1;
    public final static int LANDSCAPE = 2;

    public static String capitalize(String s) {
        if (s == null) return null;
        if (s.length() == 1) {
            return s.toUpperCase();
        }
        if (s.length() > 1) {
            return s.substring(0, 1).toUpperCase() + s.substring(1);
        }
        return "";
    }

    public static String getDate(String date, boolean showFullDate) {
        if (date == null) {
            return "Unknown";
        } else try {
            Date released = new SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(date);
            return new SimpleDateFormat(showFullDate ? "d MMMM yyyy" : "yyyy", Locale.US).format(released);
        } catch (ParseException e) {
            return date;
        }
    }

    public static void openURL(Activity context, String url) {
        if (SettingsManager.getInAppBrowser(context)) {
            CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
            builder.setToolbarColor(getThemeColor(context, R.attr.colorPrimary));
            builder.setCloseButtonIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_arrow_back));
            builder.setStartAnimations(context, R.anim.slide_in, R.anim.slide_out);
            builder.setExitAnimations(context, R.anim.slide_back_in, R.anim.slide_back_out);
            CustomTabsIntent customTabsIntent = builder.build();
            customTabsIntent.launchUrl(context, Uri.parse(url));
        } else {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            context.startActivity(browserIntent);
        }
    }

    /**
     * Seamlessly recreates an activity, keeping its state and avoiding the blinking effect.
     * The trick is simple: another fake activity is launched and immediately killed, keeping the main thread busy
     * and preventing it from displaying a black screen while it's reloading our activity. The effect is even more
     * transparent if the fake activity launches itself twice.
     *
     * @param activity activity to recreate
     */
    public static void recreate(Activity activity) {
        Intent intent = new Intent(activity, EmptyActivity.class);
        activity.startActivity(intent);
        activity.recreate();
    }

    public static boolean isDeviceWide(Activity activity, int orientation) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        int baseSize = activity.getResources().getConfiguration().orientation == orientation ? outMetrics.widthPixels : outMetrics.heightPixels;
        float density = activity.getResources().getDisplayMetrics().density;
        float widthAllocatedForCards = (baseSize - activity.getResources().getDimension(R.dimen.activity_horizontal_margin) * 2) / density;

        int threshold = orientation == PORTRAIT ? 750 : 1100;
        return widthAllocatedForCards > threshold;
    }

    public static void setTitle(Activity activity, String title) {
        ActionBar actionBar = ((AppCompatActivity) activity).getSupportActionBar();
        if (actionBar != null)
            actionBar.setTitle(title);
    }

    /**
     * Returns a pseudo-random number between min and max, inclusive.
     * The difference between min and max can be at most
     * <code>Integer.MAX_VALUE - 1</code>.
     *
     * @param min Minimum value
     * @param max Maximum value.  Must be greater than min.
     * @return Integer between min and max, inclusive.
     */
    public static int randInt(int min, int max) {
        return min + (int) (Math.random() * ((max - min) + 1));
    }

    public static void setButtonColor(Context context, Button button) {
        ColorStateList buttonBackgroundColor = ColorStateList.valueOf(getThemeColor(context, R.attr.colorPrimaryDark));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            button.setBackgroundTintList(buttonBackgroundColor);
        } else {
            ViewCompat.setBackgroundTintList(button, buttonBackgroundColor);
        }
    }

    public static int getThemeColor(Context context, int resid) {
        TypedValue colorAttribute = new TypedValue();
        context.getTheme().resolveAttribute(resid, colorAttribute, true);
        return colorAttribute.data;
    }

    public static void setTextViewLink(final Activity context, TextView textView, final String url, int start, int end) {
        SpannableString ss = new SpannableString(textView.getText());
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                Utils.openURL(context, url);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
            }
        };
        ss.setSpan(clickableSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        textView.setText(ss);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        textView.setHighlightColor(Color.TRANSPARENT);
    }

    public static String getBuildDateAsString(Context context, DateFormat dateFormat) {
        String buildDate;
        try {
            ApplicationInfo ai = context.getPackageManager().getApplicationInfo(context.getPackageName(), 0);
            ZipFile zf = new ZipFile(ai.sourceDir);
            ZipEntry ze = zf.getEntry("classes.dex");
            long time = ze.getTime();
            buildDate = dateFormat.format(new Date(time));
            zf.close();
        } catch (Exception e) {
            buildDate = "Unknown";
        }
        return buildDate;
    }

    public static String getDeviceModelName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        } else {
            return capitalize(manufacturer) + " " + model;
        }
    }

    public static String getDeviceInfo(Context context) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);

        String res = "Build version: " + BuildConfig.VERSION_NAME + " (" + BuildConfig.VERSION_CODE + ") \n";
        res += "Build date: " + Utils.getBuildDateAsString(context, dateFormat) + " \n";
        res += "Current date: " + dateFormat.format(new Date()) + " \n";
        res += "Device: " + Utils.getDeviceModelName() + " \n";
        res += "Android version: " + Build.VERSION.RELEASE + " \n";
        res += "VNDB username: " + SettingsManager.getUsername(context) + " \n \n";
        return res;
    }

    public static void sendEmail(final Context context, final String title, final String body) {
        new Thread() {
            @Override
            public void run() {
                MailService mailer = new MailService(Mail.getInfo(context).getUsername(), Mail.getInfo(context).getTo(), title, body, null);
                try {
                    mailer.sendAuthenticated();
                } catch (Exception e) {
                }
            }
        }.start();
    }

    public static void tintImage(Context context, ImageView imageView, int res, boolean attribute) {
        if (attribute) {
            imageView.setColorFilter(Utils.getThemeColor(context, res), PorterDuff.Mode.SRC_ATOP);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            imageView.setColorFilter(context.getResources().getColor(res, context.getTheme()), PorterDuff.Mode.SRC_ATOP);
        } else {
            imageView.setColorFilter(context.getResources().getColor(res), PorterDuff.Mode.SRC_ATOP);
        }
    }

    public static String convertLink(Context context, String bbcodeLink) {
        return bbcodeLink.replaceAll("\\[url=(.*?)\\](.*?)\\[/url\\]", "<a href=\"" + context.getPackageName() + "://$1\">$2</a>");
    }

    public static boolean isInMultiWindowMode(Activity activity) {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && activity.isInMultiWindowMode();
    }

    public static void loadImage(String url, final Callback callback) {
        ImageLoader.getInstance().loadImage(url, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                callback.loadedImage = loadedImage;
                callback.call();
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {
            }
        });
    }
}