package nz.co.warehouseandroidtest.extensions

import android.view.View

/**
 * True if this View's visibility is [View.VISIBLE].
 *
 * If you set it to false, then this View's visibility will become [View.GONE].
 */
inline var View.visible: Boolean
    get() = visibility == View.VISIBLE
    set(value) {
        visibility = if (value) View.VISIBLE else View.GONE
    }