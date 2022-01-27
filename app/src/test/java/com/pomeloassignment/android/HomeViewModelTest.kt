package com.pomeloassignment.android

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth
import com.pomeloassignment.android.feature.home.viewmodel.HomeViewModel
import com.pomeloassignment.android.feature.home.viewmodel.HomeViewState
import com.pomeloassignment.android.db.ArticleEntity
import com.pomeloassignment.android.feature.home.repository.INewsRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.flow.flow
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HomeViewModelTest {
    @MockK
    lateinit var newsRepo: INewsRepository

    lateinit var model: HomeViewModel

    @get:Rule
    private val rule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxed = true)
    }

    @Test
    fun `HomeViewModel - fetchNews() - validation of data getting received on live data subscribed to UI`() {
        model = HomeViewModel(newsRepo)
        val initialState: HomeViewState.FetchArticlesState =
            model.state.value as HomeViewState.FetchArticlesState //mandatory failure
        println("asserting initial state to be loading")
        Truth.assertThat(initialState.isLoading == true)
        coEvery {
            newsRepo.fetchNews()
        } returns flow {
            emit(dummyArticles)
        }

        model.fetchNews()

        val newState = model.state.value as HomeViewState.FetchArticlesState
        println("new state to contain data")
        Truth.assertThat(!newState.data.isNullOrEmpty())
        coVerify(exactly = 1) {
            newsRepo.fetchNews()
        }
        confirmVerified(newsRepo)
    }

    @Test
    fun `HomeViewModel - fetchNews() - functional test with repositories included`() {

    }

    private val dummyArticles = listOf<ArticleEntity>(
        ArticleEntity(
            id = 1,
            url = "example.com",
            uri = "example.com",
            source = "",
            publishedDate = "",
            section = "",
            imageUrl = "example.com",
            title = "Article Title",
            abstract = "Dummy artcle"
        ),
        ArticleEntity(
            id = 2,
            url = "example.com",
            uri = "example.com",
            source = "",
            publishedDate = "",
            section = "",
            imageUrl = "example.com",
            title = "Article Title",
            abstract = "Dummy artcle"
        ),
        ArticleEntity(
            id = 3,
            url = "example.com",
            uri = "example.com",
            source = "",
            publishedDate = "",
            section = "",
            imageUrl = "example.com",
            title = "Article Title",
            abstract = "Dummy artcle"
        ),
        ArticleEntity(
            id = 4,
            url = "example.com",
            uri = "example.com",
            source = "",
            publishedDate = "",
            section = "",
            imageUrl = "example.com",
            title = "Article Title",
            abstract = "Dummy artcle"
        ),
        ArticleEntity(
            id = 5,
            url = "example.com",
            uri = "example.com",
            source = "",
            publishedDate = "",
            section = "",
            imageUrl = "example.com",
            title = "Article Title",
            abstract = "Dummy artcle"
        )
    )
}
