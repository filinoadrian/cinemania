package com.far_sstrwnt.cinemania

import androidx.lifecycle.LiveData
import com.far_sstrwnt.cinemania.shared.result.Event
import org.junit.Assert.assertEquals

fun assertLiveDataEventTriggered(
    liveData: LiveData<Event<Pair<String, String>>>,
    navigationArgs: Pair<String, String>
) {
    val value = LiveDataTestUtil.getValue(liveData)
    assertEquals(value.getContentIfNotHandled(), navigationArgs)
}

fun assertSnackbarMessage(snackbarLiveData: LiveData<Event<Int>>, messageId: Int) {
    val value: Event<Int> = LiveDataTestUtil.getValue(snackbarLiveData)
    assertEquals(value.getContentIfNotHandled(), messageId)
}
