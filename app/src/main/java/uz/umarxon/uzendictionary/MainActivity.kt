package uz.umarxon.uzendictionary

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.wuyr.rippleanimation.RippleAnimation
import kotlinx.android.synthetic.main.activity_main.*
import uz.umarxon.uzendictionary.Utils.Data

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


    }

    override fun onBackPressed() = when{
        Data.isHome-> finishAffinity()
        Data.isSettingFragmentActive-> {
            RippleAnimation.create(Data.settingBackImage).setDuration(1000).start()
            animation_ripple.setBackgroundColor(Color.parseColor("#59595959"))
            super.onBackPressed()
        }
        else-> super.onBackPressed()
    }
    fun showBottomMenu(i:Boolean){
        if(i){
            bottom_view.visibility = View.VISIBLE
            my_indicator.visibility = View.VISIBLE
        }else{
            bottom_view.visibility = View.GONE
            my_indicator.visibility = View.GONE
        }
    }
}