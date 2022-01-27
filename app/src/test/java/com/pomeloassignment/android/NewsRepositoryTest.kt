package com.pomeloassignment.android

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth
import com.pomeloassignment.android.model.Article
import com.pomeloassignment.android.db.ArticleEntity
import com.pomeloassignment.android.model.ArticleSearchDoc
import com.pomeloassignment.android.model.Result
import com.pomeloassignment.android.feature.home.data.INewsDataSource
import com.pomeloassignment.android.feature.home.data.INewsLocalSource
import com.pomeloassignment.android.feature.home.repository.INewsRepository
import com.pomeloassignment.android.feature.home.repository.NewsRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.coVerifyOrder
import io.mockk.confirmVerified
import io.mockk.impl.annotations.MockK
import io.mockk.slot
import java.io.IOException
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NewsRepositoryTest {
    @MockK
    private lateinit var dbLocalStore: INewsLocalSource

    @MockK
    private lateinit var remoteStore: INewsDataSource

    private lateinit var repository: INewsRepository


    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        repository = NewsRepository(dbLocalStore, remoteStore)
    }

    @Test
    fun `NewsRepository-fetchNews() - failure condition from ntwk and local cache present`() {
        coEvery {
            dbLocalStore.fetchArticles()
        } returns dummyArticles
        coEvery {
            remoteStore.fetchRecentByPopularity(any())
        } throws IOException("exception")

        runBlocking {
            repository.fetchNews()
                .onEach { result ->
                    println("asserting if result is not empty")
                    Truth.assertThat(result.isNotEmpty())
                    println("asserting results are the same mocked cache store")
                    Truth.assertThat(result == dummyArticles)
                }
                .catch { throwable ->
                    Truth.assertThat(throwable is IOException)
                    Truth.assertThat(throwable.message.equals("exception"))
                }.collect()
        }

        coVerify(exactly = 1) {
            dbLocalStore.fetchArticles()
            remoteStore.fetchRecentByPopularity(any())
        }
        confirmVerified(dbLocalStore, remoteStore)
    }


    //TODO: looking at evaluating based on slot capturing
    @Test
    fun `NewsRepository-fetchNews() - initial case of fetching data from ntwk and populating in cache`() {
        coEvery {
            dbLocalStore.fetchArticles()
        } returns emptyList() andThenAnswer { dummyArticles }


        coEvery {
            remoteStore.fetchRecentByPopularity(any())
        } returns Result.Success(dummyResponse)


        coEvery {
            dbLocalStore.insertArticles(*anyVararg())
        } returns Unit


        runBlocking {
            repository.fetchNews()
                .onEach { result ->
                    println("received result")
                    println("asserting received is same dummy data")
                    println("size of received result: ${result.size}")
                    Truth.assertThat(result == dummyArticles)
                }.first()
        }


        coVerifyOrder {
            dbLocalStore.fetchArticles()
            remoteStore.fetchRecentByPopularity(any())
            dbLocalStore.insertArticles(*anyVararg())
            dbLocalStore.fetchArticles()
        }

        coVerify(exactly = 2) {
            dbLocalStore.fetchArticles()
        }

        coVerify(exactly = 1) {
            dbLocalStore.insertArticles(*anyVararg())
        }

        confirmVerified(dbLocalStore, remoteStore)
    }

    //TODO: not write way to interpret error values
    @Test
    @Ignore
    fun `NewsRepository-fetchNews() - failure ntwk and cache is empty`() {
        coEvery {
            dbLocalStore.fetchArticles()
        } returns emptyList()

        coEvery {
            remoteStore.fetchRecentByPopularity(any())
        } throws IOException("exception")

        runBlocking {
            repository.fetchNews().collect {}
        }

        coVerify(exactly = 1) {
            dbLocalStore.fetchArticles()
            remoteStore.fetchRecentByPopularity(any())
        }

        coVerify(exactly = 0) {
            dbLocalStore.insertArticles(*anyVararg())
        }
        confirmVerified(dbLocalStore, remoteStore)
    }

    private val dummyResponse = listOf<Article>(
        Article(
            id = 1,
            url = "example.com",
            uri = "example.com",
            source = "",
            publishedDate = "",
            section = "",
            title = "Article Title",
            abstract = "Dummy artcle",
            assetId = 1,
            subsection = "",
            nytdsection = "",
            type = "",
            updated = "",
            media = emptyList(),
            etaId = 0
        ),
        Article(
            id = 2,
            url = "example.com",
            uri = "example.com",
            source = "",
            publishedDate = "",
            section = "",
            title = "Article Title2",
            abstract = "Dummy artcle",
            assetId = 1,
            subsection = "",
            nytdsection = "",
            type = "",
            updated = "",
            media = emptyList(),
            etaId = 0
        ),
        Article(
            id = 3,
            url = "example.com",
            uri = "example.com",
            source = "",
            publishedDate = "",
            section = "",
            title = "Article Title3",
            abstract = "Dummy artcle",
            assetId = 1,
            subsection = "",
            nytdsection = "",
            type = "",
            updated = "",
            media = emptyList(),
            etaId = 0
        ),
        Article(
            id = 4,
            url = "example.com",
            uri = "example.com",
            source = "",
            publishedDate = "",
            section = "",
            title = "Article Title4",
            abstract = "Dummy artcle",
            assetId = 1,
            subsection = "",
            nytdsection = "",
            type = "",
            updated = "",
            media = emptyList(),
            etaId = 0
        )
    )

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
