package com.example.fratnav.models;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import com.example.fratnav.R;

public class HouseCardView {

    /**
     *
     *  <androidx.cardview.widget.CardView
     *                     android:id="@+id/cardview1"
     *                     android:layout_width="0dp"
     *                     android:layout_height="0dp"
     *                     android:layout_rowWeight="1"
     *                     android:layout_columnWeight="1"
     *                     android:layout_marginLeft="16dp"
     *                     android:layout_marginRight="16dp"
     *                     android:layout_marginBottom="16dp"
     *                     card_view:cardCornerRadius="8dp"
     *                     card_view:cardElevation="8dp">
     *
     *                     <LinearLayout
     *                         android:layout_width="wrap_content"
     *                         android:layout_height="wrap_content"
     *                         android:layout_gravity="center_horizontal|center_vertical"
     *                         android:layout_margin="16dp"
     *                         android:orientation="vertical">
     *
     *                         <TextView
     *                             android:id="@+id/house1"
     *                             android:layout_width="wrap_content"
     *                             android:layout_height="wrap_content"
     *                             android:text="APhi"
     *                             android:textAlignment="center"
     *                             android:textColor="@color/black"
     *                             android:textSize="18sp"
     *                             android:textStyle="bold" />
     *
     *                         <ImageView
     *                             android:layout_width="wrap_content"
     *                             android:layout_height="wrap_content"
     *                             android:layout_gravity="center_horizontal"
     *                             android:src="@drawable/aphi" />
     *
     *                     </LinearLayout>
     *
     *         </androidx.cardview.widget.CardView>
     */

    public String name;
    public Context context;

    public HouseCardView(String name, Context context){
        this.name = name;
        this.context = context;
    }

    public CardView makeCardView(){ // need to put drawable in there as well
        CardView cd = new CardView(context);
        CardView.LayoutParams params = new CardView.LayoutParams(300, 300);
        params.setMargins(16, 0, 16, 16);
        cd.setCardElevation(8);
        cd.setRadius(8);
        cd.setLayoutParams(params);




        LinearLayout l = new LinearLayout(context);
        LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        l.setHorizontalGravity(Gravity.CENTER_HORIZONTAL);
        l.setOrientation(LinearLayout.VERTICAL);
        l.setVerticalGravity(Gravity.CENTER_VERTICAL);
        lparams.setMargins(16, 16, 16, 16);
        l.setLayoutParams(lparams);

        TextView tv = new TextView(context);
        ViewGroup.LayoutParams p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        tv.setTextColor(context.getColor(R.color.black));
        tv.setTextSize(18);
        tv.setTypeface(null, Typeface.BOLD);
        tv.setText(name);
        tv.setLayoutParams(p);

        ImageView iv = new ImageView(context);
        LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        imageParams.gravity = Gravity.CENTER_HORIZONTAL;
        iv.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.aphi));

        l.addView(tv);
        l.addView(iv);

        cd.addView(l);

        return cd;
    }
}
