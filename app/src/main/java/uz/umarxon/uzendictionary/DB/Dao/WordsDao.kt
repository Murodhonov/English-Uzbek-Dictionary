package uz.umarxon.uzendictionary.DB.Dao

import androidx.room.*
import uz.umarxon.uzendictionary.DB.Entity.Word

@Dao
interface WordsDao {
    @Insert
        fun addWord(word: Word)

        @Update
        fun updateWord(word: Word)

        @Delete
        fun deleteWord(word: Word)

        @Query("select * from Word")
        fun getAll():List<Word>
}