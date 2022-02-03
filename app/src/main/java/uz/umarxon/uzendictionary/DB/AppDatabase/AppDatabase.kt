package uz.umarxon.uzendictionary.DB.AppDatabase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import uz.umarxon.uzendictionary.DB.Dao.CategoryDao
import uz.umarxon.uzendictionary.DB.Dao.WordsDao
import uz.umarxon.uzendictionary.DB.Entity.Category
import uz.umarxon.uzendictionary.DB.Entity.Word

@Database(entities = [Category::class, Word::class], version = 2)
abstract class AppDatabase : RoomDatabase() {

    abstract fun categoryDao():CategoryDao
    abstract fun wordsDao():WordsDao

    companion object{
        private var instanse:AppDatabase?= null

        @Synchronized
        fun getInstance(context: Context):AppDatabase{

            when(instanse){
                null->{
                    instanse = Room.databaseBuilder(context,AppDatabase::class.java,"db_order")
                        .fallbackToDestructiveMigration()
                        .allowMainThreadQueries()
                        .build()
                }
            }

            return instanse!!
        }

    }
}