package nz.co.warehouseandroidtest.extensions

import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction

inline fun FragmentManager.transaction(
    now: Boolean = false,
    allowStateLoss: Boolean = false,
    body: FragmentTransaction.() -> Unit
) {
    val transaction = beginTransaction()
    transaction.body()
    if (now) {
        if (allowStateLoss) {
            transaction.commitNowAllowingStateLoss()
        } else {
            transaction.commitNow()
        }
    } else {
        if (allowStateLoss) {
            transaction.commitAllowingStateLoss()
        } else {
            transaction.commit()
        }
    }
}