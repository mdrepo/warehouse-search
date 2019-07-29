package nz.co.warehouseandroidtest.features

import androidx.lifecycle.Observer

class LoggingObserver<T> : Observer<T> {
    var value: T? = null
    override fun onChanged(t: T?) {
        this.value = t
    }
}