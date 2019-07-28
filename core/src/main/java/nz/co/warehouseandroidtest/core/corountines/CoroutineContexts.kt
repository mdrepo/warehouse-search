package nz.co.warehouseandroidtest.core.corountines

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelChildren
import kotlin.coroutines.CoroutineContext

class CoroutineContexts(
    uiDispatcher: CoroutineContext = Dispatchers.Main,
    backgroundDispatcher: CoroutineContext = Dispatchers.IO
) {
    private val job: Job = Job()

    val ui: CoroutineContext = job + uiDispatcher
    val background: CoroutineContext = job + backgroundDispatcher

    fun cancel() {
        job.apply {
            cancelChildren()
            cancel()
        }
    }
}