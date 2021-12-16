package com.example.sportsballistics.utils

import android.R
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.widget.ImageViewCompat
import com.example.sportsballistics.AppSystem


class
AppConstant {
    /*User Roles*/
    companion object {
        const val ROLE_SUPER_PORTAL = "3"
        const val ROLE_CLUB_PORTAL = "4"
        const val ROLE_TRAINER_PORTAL = "1"
        const val ROLE_ATHLETES_PORTAL = "2"
        const val INTENT_EXTRA_1 = "extra_1"
        const val INTENT_EXTRA_2 = "extra_2"
        const val SLUG_EDIT = "SLUG_EDIT"
        const val INTENT_SCREEN_TYPE = "intent_screen_type"
        const val INTENT_SCREEN_TYPE_EDIT = 1;
        const val INTENT_SCREEN_TYPE_VIEW = 2;
        const val INTENT_SCREEN_TYPE_ADD = 3;
        const val VOLLEYBALL = "VOLLEYBALL";
        const val BASEBALL = "BASEBALL";
        const val TODDLER = "TODDLER";
        const val QB = "QB";
        const val STATE_CONTENT =
            "[{ \"name\": \"Alabama\", \"abbreviation\": \"AL\" }, { \"name\": \"Alaska\", \"abbreviation\": \"AK\" }, { \"name\": \"American Samoa\", \"abbreviation\": \"AS\" }, { \"name\": \"Arizona\", \"abbreviation\": \"AZ\" }, { \"name\": \"Arkansas\", \"abbreviation\": \"AR\" }, { \"name\": \"California\", \"abbreviation\": \"CA\" }, { \"name\": \"Colorado\", \"abbreviation\": \"CO\" }, { \"name\": \"Connecticut\", \"abbreviation\": \"CT\" }, { \"name\": \"Delaware\", \"abbreviation\": \"DE\" }, { \"name\": \"District Of Columbia\", \"abbreviation\": \"DC\" }, { \"name\": \"Federated States Of Micronesia\", \"abbreviation\": \"FM\" }, { \"name\": \"Florida\", \"abbreviation\": \"FL\" }, { \"name\": \"Georgia\", \"abbreviation\": \"GA\" }, { \"name\": \"Guam\", \"abbreviation\": \"GU\" }, { \"name\": \"Hawaii\", \"abbreviation\": \"HI\" }, { \"name\": \"Idaho\", \"abbreviation\": \"ID\" }, { \"name\": \"Illinois\", \"abbreviation\": \"IL\" }, { \"name\": \"Indiana\", \"abbreviation\": \"IN\" }, { \"name\": \"Iowa\", \"abbreviation\": \"IA\" }, { \"name\": \"Kansas\", \"abbreviation\": \"KS\" }, { \"name\": \"Kentucky\", \"abbreviation\": \"KY\" }, { \"name\": \"Louisiana\", \"abbreviation\": \"LA\" }, { \"name\": \"Maine\", \"abbreviation\": \"ME\" }, { \"name\": \"Marshall Islands\", \"abbreviation\": \"MH\" }, { \"name\": \"Maryland\", \"abbreviation\": \"MD\" }, { \"name\": \"Massachusetts\", \"abbreviation\": \"MA\" }, { \"name\": \"Michigan\", \"abbreviation\": \"MI\" }, { \"name\": \"Minnesota\", \"abbreviation\": \"MN\" }, { \"name\": \"Mississippi\", \"abbreviation\": \"MS\" }, { \"name\": \"Missouri\", \"abbreviation\": \"MO\" }, { \"name\": \"Montana\", \"abbreviation\": \"MT\" }, { \"name\": \"Nebraska\", \"abbreviation\": \"NE\" }, { \"name\": \"Nevada\", \"abbreviation\": \"NV\" }, { \"name\": \"New Hampshire\", \"abbreviation\": \"NH\" }, { \"name\": \"New Jersey\", \"abbreviation\": \"NJ\" }, { \"name\": \"New Mexico\", \"abbreviation\": \"NM\" }, { \"name\": \"New York\", \"abbreviation\": \"NY\" }, { \"name\": \"North Carolina\", \"abbreviation\": \"NC\" }, { \"name\": \"North Dakota\", \"abbreviation\": \"ND\" }, { \"name\": \"Northern Mariana Islands\", \"abbreviation\": \"MP\" }, { \"name\": \"Ohio\", \"abbreviation\": \"OH\" }, { \"name\": \"Oklahoma\", \"abbreviation\": \"OK\" }, { \"name\": \"Oregon\", \"abbreviation\": \"OR\" }, { \"name\": \"Palau\", \"abbreviation\": \"PW\" }, { \"name\": \"Pennsylvania\", \"abbreviation\": \"PA\" }, { \"name\": \"Puerto Rico\", \"abbreviation\": \"PR\" }, { \"name\": \"Rhode Island\", \"abbreviation\": \"RI\" }, { \"name\": \"South Carolina\", \"abbreviation\": \"SC\" }, { \"name\": \"South Dakota\", \"abbreviation\": \"SD\" }, { \"name\": \"Tennessee\", \"abbreviation\": \"TN\" }, { \"name\": \"Texas\", \"abbreviation\": \"TX\" }, { \"name\": \"Utah\", \"abbreviation\": \"UT\" }, { \"name\": \"Vermont\", \"abbreviation\": \"VT\" }, { \"name\": \"Virgin Islands\", \"abbreviation\": \"VI\" }, { \"name\": \"Virginia\", \"abbreviation\": \"VA\" }, { \"name\": \"Washington\", \"abbreviation\": \"WA\" }, { \"name\": \"West Virginia\", \"abbreviation\": \"WV\" }, { \"name\": \"Wisconsin\", \"abbreviation\": \"WI\" }, { \"name\": \"Wyoming\", \"abbreviation\": \"WY\" } ]";

        fun getDrawableTintedColor(
            @DrawableRes drawableId: Int,
            @ColorRes colorId: Int?,
            editText: EditText
        ) {
            var drawable: Drawable? = ContextCompat.getDrawable(AppSystem.context, drawableId)
            drawable = DrawableCompat.wrap(drawable!!)
            DrawableCompat.setTint(drawable, ContextCompat.getColor(AppSystem.context, colorId!!))
            DrawableCompat.setTintMode(drawable, PorterDuff.Mode.SRC_IN)
            editText.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null)
        }

        fun changeColor(imageView: ImageView) {
            ImageViewCompat.setImageTintList(
                imageView,
                ColorStateList.valueOf(AppSystem.getInstance().getColor())
            );

//            imageView.setColorFilter(
//                ContextCompat.getColor(
//                    imageView.context,
//                    AppSystem.getInstance().getColor()
//                )
//            )
        }

        fun changeDrawableColor(imageView: ImageView, context: Context) {
            if (imageView != null) {
                DrawableCompat.setTint(
                    DrawableCompat.wrap(imageView.getDrawable()),
                    ContextCompat.getColor(context, AppSystem.getInstance().getColor())
                );
            }
        }

//        fun changeColor(textView: TextView) {
//            textView.setTextColor(AppSystem.getInstance().getColor())
//        }

        fun changeColor(textView: TextView) {
            textView.setTextColor(
                ContextCompat.getColor(
                    textView.context,
                    AppSystem.getInstance().getColor()
                )
            );
        }

        fun changeColor(button: Button) {
            button.setTextColor(
                ContextCompat.getColor(
                    button.context,
                    AppSystem.getInstance().getColor()
                )
            );
        }
    }
}