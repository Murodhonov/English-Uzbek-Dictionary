package uz.umarxon.uzendictionary.DB.Entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
class Word :Serializable{

    @PrimaryKey
    var id :Int? = null

    var uzbek:String? = null
    var english:String? = null
    var category_id:Int? = null
    var isLiked:Boolean? = null
    var image:String? = null
    var audio:String? = null

    constructor()
    constructor(
        id: Int?,
        uzbek: String?,
        english: String?,
        category_id: Int?,
        isLiked: Boolean?,
        image: String?,
        audio: String?,
    ) {
        this.id = id
        this.uzbek = uzbek
        this.english = english
        this.category_id = category_id
        this.isLiked = isLiked
        this.image = image
        this.audio = audio
    }

    constructor(
        uzbek: String?,
        english: String?,
        category_id: Int?,
        isLiked: Boolean?,
        image: String?,
        audio: String?,
    ) {
        this.uzbek = uzbek
        this.english = english
        this.category_id = category_id
        this.isLiked = isLiked
        this.image = image
        this.audio = audio
    }
}