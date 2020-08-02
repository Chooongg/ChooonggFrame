package chooongg.frame.manager

import android.app.Activity
import android.app.Application
import android.os.Bundle
import java.util.*

object ActivityTaskManager : Application.ActivityLifecycleCallbacks {

    val activityTasks = LinkedList<Activity>()

    val activityTop: Activity? get() = activityTasks.last

    override fun onActivityCreated(activity: Activity, saveInstanceState: Bundle?) {
        activityTasks.addLast(activity)
    }

    override fun onActivityStarted(activity: Activity) {
    }

    override fun onActivityResumed(activity: Activity) {
    }

    override fun onActivityPaused(activity: Activity) {
    }

    override fun onActivityStopped(activity: Activity) {
    }

    override fun onActivityDestroyed(activity: Activity) {
        activityTasks.remove(activity)
    }

    override fun onActivitySaveInstanceState(activity: Activity, saveInstanceState: Bundle) {
    }
}