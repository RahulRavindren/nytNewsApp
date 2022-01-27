package com.pomeloassignment.android

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth
import com.pomeloassignment.android.model.Article
import com.pomeloassignment.android.model.ArticleResponse
import com.pomeloassignment.android.model.ArticleSearchDoc
import com.pomeloassignment.android.model.Result
import com.pomeloassignment.android.model.SearchResponse
import com.pomeloassignment.android.feature.home.data.NewsNetworkSource
import com.pomeloassignment.android.feature.home.repository.NewsService
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody
import retrofit2.HttpException
import retrofit2.Response

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class NewsNetworkSourceTest {
    @MockK
    lateinit var newsService: NewsService

    lateinit var newsNetworkSource: NewsNetworkSource

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxed = true)
        newsNetworkSource = NewsNetworkSource(newsService)
    }

    @Test
    fun `NewsNetworkSource||fetchRecentByPopularity||ideal condition of results returned`() {
        coEvery {
            newsService.fetchByPopular(any())
        } returns ArticleResponse(status = "OK", copyright = "", results = dummyArticleData)
        runTest {
            val results = newsNetworkSource.fetchRecentByPopularity("7")
            if (results is Result.Success) {
                println("results received count : ${results.value.size}")
                Truth.assertThat(results.value == dummyArticleData)
            }
        }
        coVerify {
            newsService.fetchByPopular(any())
        }
        confirmVerified(newsService)
    }


    @Test
    fun `NewsNetworkSource||fetchRecentByPopularity|| network error condition ioexception case`() {
        coEvery {
            newsService.fetchByPopular(any())
        } throws IOException("exception")

        runTest {
            val result = newsNetworkSource.fetchRecentByPopularity("7")
            Truth.assertThat(result is Result.ApiError)
            val error = result as? Result.ApiError ?: return@runTest
            println("error received ApiError")
            Truth.assertThat(error is Result.ApiError)
        }
        coVerify {
            newsService.fetchByPopular(any())
        }
        confirmVerified(newsService)
    }

    @Test
    fun `NewsNetworkSource||fetchRecentByPopularity|| network error condition httpexception 500 case`() {
        coEvery {
            newsService.fetchByPopular(any())
        } throws HttpException(
            Response.error<Any>(
                500,
                ResponseBody.create("application/json".toMediaType(), "")
            )
        )

        runTest {
            val result = newsNetworkSource.fetchRecentByPopularity("7")
            Truth.assertThat(result is Result.GenericError)
            val error = result as? Result.GenericError ?: return@runTest
            println("error code: ${error.code}")
            println("error :${error.error}")
            Truth.assertThat(error.code == 500)
        }
        coVerify {
            newsService.fetchByPopular(any())
        }
        confirmVerified(newsService)
    }

    @Test
    fun `NewsNetworkSource||fetchRecentByPopularity|| no results condition`() {
        coEvery {
            newsService.fetchByPopular(any())
        } returns ArticleResponse(status = "OK", copyright = "", results = emptyList())
        runTest {
            val results = newsNetworkSource.fetchRecentByPopularity("7")
            if (results is Result.Success) {
                println("results received count : ${results.value.size}")
                Truth.assertThat(results.value.isEmpty())
            }
        }

        coVerify {
            newsService.fetchByPopular(any())
        }
        confirmVerified(newsService)
    }

    @Test
    fun `NewsNetworkSource||fetchSearchResults||ideal condition of results returned`() {
        coEvery {
            newsService.searchArticle(any())
        } returns SearchResponse(status = "OK", copyright = "", response = searchResultsDummy)
        runTest {
            val results = newsNetworkSource.fetchSearchResults("donaldtrump")
            if (results is Result.Success) {
                println("results received count : ${results.value.size}")
                Truth.assertThat(results.value == searchResultsDummy.docs)
            }
        }
        coVerify {
            newsService.searchArticle(any())
        }
        confirmVerified(newsService)
    }

    @Test
    fun `NewsNetworkSource||fetchSearchResults|| network error condition ioexception case`() {
        coEvery {
            newsService.searchArticle(any())
        } throws IOException("exception")

        runTest {
            val result = newsNetworkSource.fetchSearchResults("7")
            Truth.assertThat(result is Result.ApiError)
            val error = result as? Result.ApiError ?: return@runTest
            println("error received ApiError")
            Truth.assertThat(error is Result.ApiError)
        }
        coVerify {
            newsService.searchArticle(any())
        }
        confirmVerified(newsService)
    }

    @Test
    fun `NewsNetworkSource||fetchSearchResults|| network error condition httpexception 500 case`() {
        coEvery {
            newsService.searchArticle(any())
        } throws HttpException(
            Response.error<Any>(
                500,
                ResponseBody.create("application/json".toMediaType(), "")
            )
        )

        runTest {
            val result = newsNetworkSource.fetchSearchResults("7")
            Truth.assertThat(result is Result.GenericError)
            val error = result as? Result.GenericError ?: return@runTest
            println("error code: ${error.code}")
            println("error :${error.error}")
            Truth.assertThat(error.code == 500)
        }
        coVerify {
            newsService.searchArticle(any())
        }
        confirmVerified(newsService)
    }


    @Test
    fun `NewsNetworkSource||fetchSearchResults|| no results condition`() {
        coEvery {
            newsService.searchArticle(any())
        } returns SearchResponse(
            status = "OK",
            copyright = "",
            response = ArticleSearchDoc(docs = emptyList())
        )
        runTest {
            val results = newsNetworkSource.fetchSearchResults("7")
            if (results is Result.Success) {
                println("results received count : ${results.value.size}")
                Truth.assertThat(results.value.isEmpty())
            }
        }

        coVerify {
            newsService.searchArticle(any())
        }
        confirmVerified(newsService)
    }


    private val searchResultsDummy = ArticleSearchDoc(
        docs = listOf(
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