package uz.umarxon.uzendictionary.DB.Entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
class Category :Serializable{

    @PrimaryKey
    var id:Int? = null
    var name:String? = null

    constructor(id: Int?, name: String?) {
        this.id = id
        this.name = name
    }

    constructor(name: String?) {
        this.name = name
    }

    constructor()

}