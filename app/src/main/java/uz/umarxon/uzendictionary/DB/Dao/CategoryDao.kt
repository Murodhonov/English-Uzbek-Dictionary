package uz.umarxon.uzendictionary.DB.Dao

import androidx.room.*
import uz.umarxon.uzendictionary.DB.Entity.Category

@Dao
interface CategoryDao {

    @Insert
    fun addCategory(category: Category)

    @Update
    fun updateCategory(category: Category)

    @Delete
    fun deleteCategory(category: Category)

    @Query("select * from Category")
    fun getAll():List<Category>

}