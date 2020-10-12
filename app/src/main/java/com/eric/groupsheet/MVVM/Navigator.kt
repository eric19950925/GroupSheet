package com.eric.groupsheet.MVVM

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.eric.groupsheet.*
import com.eric.groupsheet.EditSheet.EditRule.EditRule
import com.eric.groupsheet.EditSheet.EditRule.PlanSearch
import com.eric.groupsheet.EditSheet.EditRule.TextRecogCam
import com.eric.groupsheet.EditSheet.EditSheet.EditSheet
import com.eric.groupsheet.EditSheet.SheetAnalysis.SheetAnalysis
import com.eric.groupsheet.Life.*
import com.eric.groupsheet.MainHome.MainHome
import com.eric.groupsheet.NameList.NameList
import com.eric.groupsheet.Sheet.Sheet
import org.koin.ext.getFullName

class Navigator {
    lateinit var activity: FragmentActivity
    var lastAddTime: Long = 0
}
fun Navigator.toMainHomePage(){
    addPage(MainHome.newInstance())
}
fun Navigator.toLoginPage(
    userName: String,
    userEmail: String,
    userPhotoUrl: String,
    userID: String,
    newsVersion: Int
) {
    addPage(Login.newInstance(
        userName,
        userEmail,
        userPhotoUrl,
        userID,
        newsVersion
    ))
}
fun Navigator.toNameListPage(){
    addPage(NameList.newInstance())
}
fun Navigator.toAboutPage(){
    addPage(About.newInstance())
}
fun Navigator.toNewSheetPage(){
    addPage(Sheet.newInstance())
}
fun Navigator.toEditRulePage(id:String){
    addPage(EditRule.newInstance(id))
}

fun Navigator.toAnalysisPage(id:String){
    addPage(SheetAnalysis.newInstance(id))
}

fun Navigator.toEditSheetPage(id:String){
    addPage(EditSheet.newInstance(id))
}
fun Navigator.toSettingPage(){
    addPage(Setting.newInstance())
}
fun Navigator.toLifePage(){
    addPage(Life.newInstance())
}
fun Navigator.toAccountPage(){
    addPage(UserAccount.newInstance())
}

fun Navigator.toKey1Page(){
    addPage(Key1.newInstance())
}

fun Navigator.toSearchPage(){
    addPage(PlanSearch.newInstance())
}

fun Navigator.toCameraPage(){
    addPage(TextRecogCam.newInstance())
}

fun Navigator.addPage(fragment: Fragment){
    //unknow
    if (System.currentTimeMillis() - lastAddTime < 500) {
        return
    }
    lastAddTime = System.currentTimeMillis()
    if (activity.supportFragmentManager.fragments.size > 0) {
        var needToHideFragment: Fragment? = null
        for (topFragment in activity.supportFragmentManager.fragments) {
            if (topFragment.isVisible) {
                needToHideFragment = topFragment
            }
        }

        if (needToHideFragment == null) {
            activity.supportFragmentManager.beginTransaction()
                .setCustomAnimations(
                    R.anim.anim_in,
                    R.anim.anim_out,
                    R.anim.pop_in,
                    R.anim.pop_out
                )
                .add(R.id.container, fragment, fragment::class.getFullName())
                .addToBackStack(fragment::class.getFullName())
                .commit()
        } else {
            activity.supportFragmentManager.beginTransaction()
                .setCustomAnimations(
                    R.anim.anim_in,
                    R.anim.anim_out,
                    R.anim.pop_in,
                    R.anim.pop_out
                )
                .add(R.id.container, fragment, fragment::class.getFullName())
                .hide(needToHideFragment)
                .addToBackStack(fragment::class.getFullName())
                .commit()
        }
    } else {
        activity.supportFragmentManager.beginTransaction()
            .setCustomAnimations(
                R.anim.anim_in,
                R.anim.anim_out,
                R.anim.pop_in,
                R.anim.pop_out
            )
            .add(R.id.container, fragment, fragment::class.getFullName())
            .addToBackStack(fragment::class.getFullName())
            .commit()
    }
}
