package uz.umarxon.uzendictionary.Utils

import android.annotation.SuppressLint
import android.widget.ImageView

@SuppressLint("StaticFieldLeak")
object Data {
    var isHome = false
    var isSettingFragmentActive = false
    lateinit var settingBackImage :ImageView
    var homeSelected = true
    var likeSelected = false
    var settingTabPos = 0
    var alreadyRecorded = false
    var isApplicationIsFirstRunning = true
    var path=""
    var isFirstPressed = true
    var isSecondPressed = false
    var isThirdPressed = false
}