package com.hope.igb.savvy.networking.models

class PostModelHelper {

    companion object {

        fun postModelToString(postUrl: String, posterPicture: String): String {
            return "$postUrl,,,$posterPicture"
        }

        fun getDownloadUrl(postModelToString: String): String {
            return postModelToString.split(",,,")[0]
        }

        fun getPosterPicture(postModelToString: String): String {
            return postModelToString.split(",,,")[1]
        }

//
//
//
//        fun modelUsagesToString(postModel: PostModel): String {
//            return wallpaper.id+",,,"+wallpaper.usageTimes
//        }
//

//
//        fun getUsages(usagesInString: String) : Int{
//            return usagesInString.split(",,,")[1].toInt()
//        }


    }


}