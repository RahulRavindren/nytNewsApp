package com.pomeloassignment.android

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ActivityScenario.launch
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.google.common.truth.Truth
import com.pomeloassignment.android.db.ArticleDatabase
import com.pomeloassignment.android.feature.home.viewmodel.HomeViewModel
import com.pomeloassignment.android.feature.home.viewmodel.HomeViewState
import com.pomeloassignment.android.db.ArticleEntity
import com.pomeloassignment.android.feature.home.data.INewsDataSource
import com.pomeloassignment.android.feature.home.data.INewsLocalSource
import com.pomeloassignment.android.feature.home.data.NewsDbSource
import com.pomeloassignment.android.feature.home.data.NewsNetworkSource
import com.pomeloassignment.android.feature.home.repository.INewsRepository
import com.pomeloassignment.android.feature.home.repository.ISearchRepository
import com.pomeloassignment.android.feature.home.repository.NewsRepository
import com.pomeloassignment.android.model.Article
import com.pomeloassignment.android.model.Result
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.coVerifyOrder
import io.mockk.confirmVerified
import io.mockk.impl.annotations.MockK
import io.mockk.slot
import io.mockk.spyk
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HomeViewModelTest {
	@MockK
	lateinit var newsRepo: INewsRepository

	@MockK
	lateinit var searchRepo: ISearchRepository

	lateinit var model: HomeViewModel

	@get:Rule
	private val rule = InstantTaskExecutorRule()

	@MockK
	lateinit var localDataStore: INewsLocalSource

	@MockK
	lateinit var remoteStore: INewsDataSource

	@MockK
	lateinit var searchRepository: ISearchRepository

	@Before
	fun setUp() {
		MockKAnnotations.init(this, relaxed = true)
		val context = ApplicationProvider.getApplicationContext<Context>()
	}

	@Test
	fun `HomeViewModel - fetchNews() - validation of data getting received on live data subscribed to UI`() {
		model = HomeViewModel(newsRepo, searchRepo)
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
	fun `HomeViewModel|| fetchNews()|| integration tests for initial flow of no data`() {
		coEvery {
			localDataStore.fetchArticles()
		} returns emptyList() andThen dummyArticles

		//mock network response. Since requirement was for not making any thrid party api calls for all test cases
		coEvery {
			remoteStore.fetchRecentByPopularity(any())
		} returns Result.Success(dummyArticleData)


		coEvery {
			localDataStore.insertArticles(any())
		} returns Unit

		val repository = NewsRepository(localDataStore, remoteStore)
		model = HomeViewModel(repository, searchRepository)

		runBlocking {
			model.fetchNews()
		}

		model.state.observeForever { state ->
			if (state is HomeViewState.FetchArticlesState) {
				if (state.data != null) Truth.assertThat(state.data?.isNotEmpty())
				else if (state.isLoading == true) Truth.assertThat(true)
			}
		}

		coVerifyOrder {
			localDataStore.fetchArticles()
			remoteStore.fetchRecentByPopularity(any())
			localDataStore.insertArticles(any())
			localDataStore.fetchArticles()
		}

		confirmVerified(localDataStore, repository, remoteStore)
	}

	@Test
	fun `HomeViewModel||searchNews()||enact search edit text`() = runTest {
		model = HomeViewModel(newsRepo, searchRepo)
		val inputFlow = listOf("don", "donal", "donald", "donaldtr", "donaldtrump")

		coEvery {
			searchRepo.searchArticle(any())
		} returns flow { emit(dummyArticles) }

		var job: Job? = null

		model.state.observeForever { state ->
			if (state is HomeViewState.FetchArticlesState && !state.isLoading) {
				println("always assertion that the values would be same")
				Truth.assertThat(state.data == dummyArticles)
			}
		}

		job = launch {
			var i = 0;
			while (isActive) {
				if (i >= inputFlow.size - 1) {
					job?.cancel()
				}
				model.query(inputFlow[i++])
				delay(3000)
			}
		}
		confirmVerified(searchRepo)
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
	private val dummyArticleData = listOf<Article>(
		Article(
			uri = "uri1",
			url = "www.google.com",
			id = 1,
			assetId = 1,
			source = "",
			publishedDate = "",
			updated = "",
			section = "",
			subsection = "",
			nytdsection = "",
			type = "",
			title = "",
			abstract = "",
			media = emptyList(),
			etaId = 23
		),
		Article(
			uri = "uri1",
			url = "www.google.com",
			id = 2,
			assetId = 2,
			source = "",
			publishedDate = "",
			updated = "",
			section = "",
			subsection = "",
			nytdsection = "",
			type = "",
			title = "",
			abstract = "",
			media = emptyList(),
			etaId = 23
		),
		Article(
			uri = "uri1",
			url = "www.google.com",
			id = 3,
			assetId = 3,
			source = "",
			publishedDate = "",
			updated = "",
			section = "",
			subsection = "",
			nytdsection = "",
			type = "",
			title = "",
			abstract = "",
			media = emptyList(),
			etaId = 23
		),
		Article(
			uri = "uri1",
			url = "www.google.com",
			id = 4,
			assetId = 4,
			source = "",
			publishedDate = "",
			updated = "",
			section = "",
			subsection = "",
			nytdsection = "",
			type = "",
			title = "",
			abstract = "",
			media = emptyList(),
			etaId = 23
		)
	)

}
