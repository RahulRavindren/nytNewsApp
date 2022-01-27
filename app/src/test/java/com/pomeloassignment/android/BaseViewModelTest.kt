package com.pomeloassignment.android

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth
import com.pomeloassignment.android.base.BaseViewModel
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class BaseViewModelTest(val inputState: DummyState, val expectedStat: DummyState) {
    private lateinit var dummyVM: DummyViewModel

    @get:Rule
    val taskRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        dummyVM = DummyViewModel()
    }

    @Test
    fun `check state change is reflected when invoked from VM`() {
        println("input state $inputState")
        dummyVM.changeState(inputState)
        val receiveValue = dummyVM.state.value!!
        println("received state $receiveValue")
        Truth.assertThat(receiveValue.id == expectedStat.id)
    }

    companion object {
        @JvmStatic
        @Parameterized.Parameters
        fun data(): Collection<Array<Any>> {
            return listOf(
                arrayOf(DummyState("5"), DummyState("5")),
                arrayOf(DummyState("4"), DummyState("4")),
                arrayOf(DummyState("10"), DummyState("10")),
                arrayOf(DummyState("24"), DummyState("24")),
                arrayOf(DummyState("45"), DummyState("5")),
                arrayOf(DummyState("50"), DummyState("50"))
            )
        }
    }
}

data class DummyState(val id: String)

class DummyViewModel : BaseViewModel<DummyState>(DummyState("1")) {

    fun changeState(state: DummyState) {
        setState { state }
    }
}