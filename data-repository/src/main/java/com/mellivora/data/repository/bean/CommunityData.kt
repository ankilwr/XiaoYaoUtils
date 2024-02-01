package com.mellivora.data.repository.bean
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 * 朋友圈列表数据
 */
data class CommunityData(
    @SerializedName("id")
    val id: String?,
    @SerializedName("avatar")
    val avatar: String?,
    @SerializedName("nick_name")
    val nickName: String?,
    @SerializedName("date_format")
    val dateFormat: String?,
    @SerializedName("content")
    val content: String?,
    @SerializedName("location")
    val location: Location?,
    @SerializedName("up")
    val up: MutableList<Up>?,
    @SerializedName("discuss")
    var discuss: MutableList<Discuss>?,
    @SerializedName("link")
    val link: Link?,
    @SerializedName("music")
    val music: Any?,
    @SerializedName("video")
    val video: Video?,
    @SerializedName("images")
    val images: List<Image>?,
    @SerializedName("type")
    val type: String?
) {

    data class Location(
        @SerializedName("place")
        val place: String?,
        @SerializedName("longitude")
        val longitude: String?,
        @SerializedName("latitude")
        val latitude: String?
    )

    data class Link(
        @SerializedName("url")
        val url: String?,
        @SerializedName("icon")
        val icon: String?,
        @SerializedName("description")
        val description: String?
    )

    data class Image(
        @SerializedName("url")
        val url: String?,
        @SerializedName("width")
        val width: Int = 0,
        @SerializedName("height")
        val height: Int = 0
    )

    data class Video(
        @SerializedName("cover")
        val cover: String?,
        @SerializedName("width")
        val width: Int = 0,
        @SerializedName("height")
        val height: Int = 0,
        @SerializedName("url")
        val url: String?
    ){
        fun getVideoDimensionRatio(): String{
            if(width <= 0 || height <= 0){
                return "3:4"
            }
            return "$width:$height"
        }
    }

    data class Up(
        @SerializedName("user_id")
        val userId: String?,
        @SerializedName("nickname")
        val nickname: String?,
    )

    @Parcelize
    data class Discuss(
        @SerializedName("sender")
        val sender: String?,
        @SerializedName("sender_id")
        val senderId: String?,
        @SerializedName("recipient")
        val recipient: String?,
        @SerializedName("recipient_id")
        val recipientId: String?,
        @SerializedName("content")
        val content: String?,
    ): Parcelable

}


