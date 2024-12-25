package com.hope.igb.savvy.util

import com.hope.igb.savvy.R
import com.hope.igb.savvy.common.Constants


 class DefaultProfilePicturesList {


    companion object {


        private val profilePicturesList = listOf(
            R.drawable.z_user_animalsye,
            R.drawable.z_user_beautyfull,
            R.drawable.z_user_captaintired,
            R.drawable.z_user_capturecoat,
            R.drawable.z_user_cottonmottled,
            R.drawable.z_user_cute_meow,
            R.drawable.z_user_dropfalcon,
            R.drawable.z_user_footballtime,
            R.drawable.z_user_fromfax,
            R.drawable.z_user_goodforme,
            R.drawable.z_user_kittenfell,
            R.drawable.z_user_kittenlost,
            R.drawable.z_user_manageclip,
            R.drawable.z_user_pureheart,
            R.drawable.z_user_roughlypath,
            R.drawable.z_user_royalbonehead,
            R.drawable.z_user_secretivechase,
            R.drawable.z_user_servicediscuss,
            R.drawable.z_user_showcolorful,
            R.drawable.z_user_slowlydanger,
            R.drawable.z_user_snowglass,
            R.drawable.z_user_sportshortly,
            R.drawable.z_user_sporttag,
            R.drawable.z_user_unlikelyspecial,
            R.drawable.z_user_wantcolorless
        )

        fun getProfilePictureFrom(posterPicture: String): Int {
            return profilePicturesList[posterPicture.split(Constants.DEFAULT_PROFILE_PICTURE_SIGNATURE)[1].toInt()]
        }

    }

}