package com.hope.igb.savvy.networking.models


data class PostModel(
    val id: String,
    val posterName: String,
    val posterPicture: String,
    val category: String,
    val downloadUrl: String,
    var likes: Int,
    var downloads: Int,
    val impressions: Int, //will using for evaluate trending posts by equation  (likes+downloads)-lastPeriodImpressions
    val description: String,
    val postType: String
) {
    constructor() : this("","","","","", 0, 0, 0,"",
        "")
}