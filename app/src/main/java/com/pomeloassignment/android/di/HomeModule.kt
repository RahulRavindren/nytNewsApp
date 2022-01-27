package com.pomeloassignment.android.di

import androidx.lifecycle.ViewModelProvider
import com.pomeloassignment.android.base.viewModelFactory
import com.pomeloassignment.android.feature.home.viewmodel.HomeViewModel
import com.pomeloassignment.android.db.ArticleDao
import com.pomeloassignment.android.feature.home.data.INewsDataSource
import com.pomeloassignment.android.feature.home.data.INewsLocalSource
import com.pomeloassignment.android.feature.home.repository.INewsRepository
import com.pomeloassignment.android.feature.home.data.NewsDbSource
import com.pomeloassignment.android.feature.home.data.NewsNetworkSource
import com.pomeloassignment.android.feature.home.repository.ISearchRepository
import com.pomeloassignment.android.feature.home.repository.NewsRepository
import com.pomeloassignment.android.feature.home.repository.NewsService
import com.pomeloassignment.android.feature.home.repository.SearchRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import retrofit2.Retrofit

@Module
@InstallIn(FragmentComponent::class)
object HomeModule {

	@Provides
	fun homeVMFactory(
		repository: INewsRepository,
		searchRepository: ISearchRepository
	): ViewModelProvider.Factory {
		return viewModelFactory { HomeViewModel(repository, searchRepository) }
	}

	@Provides
	fun articleRemoteDataSource(newsService: NewsService): INewsDataSource =
		NewsNetworkSource(newsService)

	@Provides
	fun articleLocalDataSource(articleDao: ArticleDao): INewsLocalSource = NewsDbSource(articleDao)

	@Provides
	fun articleRepository(
		localSource: INewsLocalSource,
		remoteStore: INewsDataSource
	): INewsRepository {
		return NewsRepository(localSource, remoteStore)
	}

	@Provides
	fun searchRepository(remoteStore: INewsDataSource): ISearchRepository {
		return SearchRepository(remoteStore)
	}

	@Provides
	fun articleService(retrofit: Retrofit): NewsService = retrofit.create(NewsService::class.java)

}