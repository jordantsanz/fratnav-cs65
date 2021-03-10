package com.example.fratnav.models;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import com.example.fratnav.R;

public class HouseCardView implements Parcelable {

    public String name;
    public Context context;
    public int drawable;

    public HouseCardView(String name, Context context, int drawable){
        this.name = name;
        this.context = context;
        this.drawable = drawable;
    }

    protected HouseCardView(Parcel in) {
        name = in.readString();
        drawable = in.readInt();
    }

    public static final Creator<HouseCardView> CREATOR = new Creator<HouseCardView>() {
        @Override
        public HouseCardView createFromParcel(Parcel in) {
            return new HouseCardView(in);
        }

        @Override
        public HouseCardView[] newArray(int size) {
            return new HouseCardView[size];
        }
    };

    // makes a new cardview object
    public CardView makeCardView(){
        CardView cd = new CardView(context);
        CardView.LayoutParams params = new CardView.LayoutParams(300, 300);
        params.setMargins(16, 0, 16, 16);
        cd.setCardElevation(8);
        cd.setRadius(8);
        cd.setLayoutParams(params);

        // makes linear layout
        LinearLayout l = new LinearLayout(context);
        LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        l.setHorizontalGravity(Gravity.CENTER_HORIZONTAL);
        l.setOrientation(LinearLayout.VERTICAL);
        l.setVerticalGravity(Gravity.CENTER_VERTICAL);
        lparams.setMargins(16, 16, 16, 16);
        l.setLayoutParams(lparams);

        // makes textview
        TextView tv = new TextView(context);
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        tv.setGravity(Gravity.BOTTOM);
        tv.setTextColor(context.getColor(R.color.white));
        tv.setTextSize(18);
        Typeface typeface = ResourcesCompat.getFont(context, R.font.montserrat);
        tv.setTypeface(typeface);
        tv.setText(name);


        tv.setLayoutParams(p);


        // makes imageview
        ImageView iv = new ImageView(context);
        LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        imageParams.gravity = Gravity.CENTER_HORIZONTAL;

        boolean bool = false;
        if (drawable == R.drawable.signu1){
            bool = true;
        }

        int image = 0;
        switch(name){
            case "Sig Nu":
                image = R.drawable.signu1;
                break;
            case "TDX":
                image = R.drawable.tdx;
                break;
            case "Zete":
                image = R.drawable.zetapsi;
                break;
            case "Sigma Delt":
                image = R.drawable.sigdelt;
                break;
            case "Tabard":
                image = R.drawable.tabard;
                break;
            case "Tri-Kap":
                image = R.drawable.trikap;
                break;
            case "Kappa":
                image = R.drawable.kkg;
                break;
            case "Hereot":
                image = R.drawable.heorot;
                break;
            case "Phi Delt":
                image = R.drawable.phidelta;
                break;
            case "KD":
                image = R.drawable.kd;
                break;
            case "KDE":
                image = R.drawable.kde;
                break;
            case "Phi Tau":
                image = R.drawable.phitau;
                break;
            case "Alpha Chi":
                image = R.drawable.ic_axa;
                break;
            case "GDX":
                image = R.drawable.gdx;
                break;
            case "Psi U":
                image = R.drawable.psiu;
                break;
            case "Chi Delt":
                image = R.drawable.chidelt;
                break;
            case "Chi Gam":
                image = R.drawable.chigam;
                break;
            case "EKT":
                image = R.drawable.ekt;
                break;
            case "Deltas":
                image = R.drawable.deltas;
                break;
            case "AXiD":
                image = R.drawable.axid;
                break;
            case "Beta":
                image = R.drawable.beta;
                break;
            case "BG":
                image = R.drawable.bg;
                break;
            case "APhi":
                image = R.drawable.aphi1;
                break;
            case "Alpha Theta":
                image = R.drawable.alphatheta;
                break;
            case "Alphas":
                image = R.drawable.alphas;
                break;
            case "AKA":
                image = R.drawable.aka;
                break;
            default:
                image = drawable;
                break;
        }
        iv.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(), image, null));

        imageParams.setMargins(0, 0, 0, 35);


        // adds views together
        l.addView(iv);
        l.addView(tv);

        cd.addView(l);

        // return cardview
        return cd;
    }

    // makes a small cardview
    public CardView makeSmallCardView(){
        CardView cd = new CardView(context);
        CardView.LayoutParams params = new CardView.LayoutParams(150, 150);
        params.setMargins(16, 0, 16, 16);
        cd.setCardElevation(8);
        cd.setRadius(8);
        cd.setLayoutParams(params);

        // makes layout
        LinearLayout l = new LinearLayout(context);
        LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        l.setHorizontalGravity(Gravity.CENTER_HORIZONTAL);
        l.setOrientation(LinearLayout.VERTICAL);
        l.setVerticalGravity(Gravity.CENTER_VERTICAL);
        lparams.setMargins(16, 16, 16, 16);
        l.setLayoutParams(lparams);

        // makes textview
        TextView tv = new TextView(context);
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        tv.setGravity(Gravity.BOTTOM);
        tv.setTextColor(context.getColor(R.color.white));
        tv.setTextSize(12);
        Typeface typeface = ResourcesCompat.getFont(context, R.font.montserrat);
        tv.setTypeface(typeface);
        tv.setText(name);


        tv.setLayoutParams(p);


        // make imageview
        ImageView iv = new ImageView(context);
        LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        imageParams.gravity = Gravity.CENTER_HORIZONTAL;

        boolean bool = false;
        if (drawable == R.drawable.signu1){
            bool = true;
        }

        int image = 0;
        switch(name){
            case "Sig Nu":
                image = R.drawable.signu1;
                break;
            case "TDX":
                image = R.drawable.tdx;
                break;
            case "Zete":
                image = R.drawable.zetapsi;
                break;
            case "Sigma Delt":
                image = R.drawable.sigdelt;
                break;
            case "Tabard":
                image = R.drawable.tabard;
                break;
            case "Tri-Kap":
                image = R.drawable.trikap;
                break;
            case "Kappa":
                image = R.drawable.kkg;
                break;
            case "Hereot":
                image = R.drawable.heorot;
                break;
            case "Phi Delt":
                image = R.drawable.phidelta;
                break;
            case "KD":
                image = R.drawable.kd;
                break;
            case "KDE":
                image = R.drawable.kde;
                break;
            case "Phi Tau":
                image = R.drawable.phitau;
                break;
            case "Alpha Chi":
                image = R.drawable.ic_axa;
                break;
            case "GDX":
                image = R.drawable.gdx;
                break;
            case "Psi U":
                image = R.drawable.psiu;
                break;
            case "Chi Delt":
                image = R.drawable.chidelt;
                break;
            case "Chi Gam":
                image = R.drawable.chigam;
                break;
            case "EKT":
                image = R.drawable.ekt;
                break;
            case "Deltas":
                image = R.drawable.deltas;
                break;
            case "AXiD":
                image = R.drawable.axid;
                break;
            case "Beta":
                image = R.drawable.beta;
                break;
            case "BG":
                image = R.drawable.bg;
                break;
            case "APhi":
                image = R.drawable.aphi1;
                break;
            case "Alpha Theta":
                image = R.drawable.alphatheta;
                break;
            case "Alphas":
                image = R.drawable.alphas;
                break;
            case "AKA":
                image = R.drawable.aka;
                break;
            default:
                image = drawable;
                break;
        }
        iv.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(), image, null));

        imageParams.setMargins(0, 0, 0, 35);


        // add views together
        l.addView(iv);
        l.addView(tv);

        cd.addView(l);

        // return cardview
        return cd;
    }

    // parcel writing
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeInt(drawable);
    }
}
