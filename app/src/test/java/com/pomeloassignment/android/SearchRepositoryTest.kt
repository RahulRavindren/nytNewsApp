package com.pomeloassignment.android

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth
import com.pomeloassignment.android.feature.home.data.INewsDataSource
import com.pomeloassignment.android.feature.home.repository.ISearchRepository
import com.pomeloassignment.android.feature.home.repository.SearchRepository
import com.pomeloassignment.android.model.ArticleSearchDoc
import com.pomeloassignment.android.model.Result
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.impl.annotations.MockK
import io.mockk.slot
import java.io.IOException
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SearchRepositoryTest {

    @MockK
    private lateinit var remoteStore: INewsDataSource

    private lateinit var repository: ISearchRepository


    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        repository = SearchRepository(remoteStore)
    }


    @Test
    fun `NewsRepository-fetchSearchResults() - failure ntwk case`() {
        coEvery {
            remoteStore.fetchSearchResults(any())
        } throws IOException("api exception")

        runBlocking {
            repository.searchArticle("donald trump")
                .catch { error ->
                    println("failure case")
                    println("exception $error")
                    Truth.assertThat(error is IOException)
                }.collect()
        }

        coVerify(exactly = 1) {
            remoteStore.fetchSearchResults(any())
        }
        confirmVerified(remoteStore)
    }

    @Test
    fun `NewsRepository-fetchSearchResults() - exception thrown for empty results`() {
        coEvery {
            remoteStore.fetchSearchResults(any())
        } returns Result.Success(emptyList())

        runBlocking {
            repository.searchArticle("donald trump")
                .catch { error ->
                    println("failure case")
                    println("exception $error")
                    Truth.assertThat(error is NoResultsException)
                }.collect()
        }

        coVerify(exactly = 1) {
            remoteStore.fetchSearchResults(any())
        }

        confirmVerified(remoteStore)
    }

    @Test
    fun `NewsRepostiory-fetchSearchResults() - ideal case of results`() {
        val search = slot<String>()
        coEvery {
            remoteStore.fetchSearchResults(capture(search))
        } returns Result.Success(dummySearchResponseList)

        runBlocking {
            repository.searchArticle("donaldTrump")
                .collect {
                    println("asserting results received is not empty")
                    Truth.assertThat(it.isNotEmpty())
                    println("received mock list size : ${it.size}")
                    Truth.assertThat(it == dummySearchResponseList)
                }
        }

        coVerify(exactly = 1) {
            remoteStore.fetchSearchResults(capture(search))
        }
        println("assseting if passed param is DONALD_TRUMP")
        println("printing captured value ${search.captured}")
        Truth.assertThat(search.captured == "donaldTrump")
        confirmVerified(remoteStore)
    }


}

private val dummySearchResponseList = listOf(
    ArticleSearchDoc.ArticleSearch(
        webUrl = "example.com",
        abstract = "Search1",
        source = "q1",
        Id = "1",
        uri = "example.com"
    ),
    ArticleSearchDoc.ArticleSearch(
        webUrl = "example.com",
        abstract = "Search2",
        source = "q2",
        Id = "2",
        uri = "example.com"
    ),
    ArticleSearchDoc.ArticleSearch(
        webUrl = "example.com",
        abstract = "Search3",
        source = "q3",
        Id = "3",
        uri = "example.com"
    ),
    ArticleSearchDoc.ArticleSearch(
        webUrl = "example.com",
        abstract = "Search4",
        source = "q4",
        Id = "4",
        uri = "example.com"
    )
)