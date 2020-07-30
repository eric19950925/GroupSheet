package com.eric.groupsheet.Life

import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter

class ViewPagerAdapter(var viewlist:ArrayList<View>): PagerAdapter(){
        override fun isViewFromObject(view: View, `object`: Any): Boolean {
            return view==`object`
        }

        override fun getCount(): Int = viewlist.size

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            container.addView(viewlist.get(position))
            return viewlist.get(position)
//            return super.instantiateItem(container, position)
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            container.removeView(`object` as View)
//        super.destroyItem(container, position, `object`)
        }

}
