package com.far_sstrwnt.cinemania

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.far_sstrwnt.cinemania.model.MediaEntity
import com.far_sstrwnt.cinemania.shared.domain.*
import com.far_sstrwnt.cinemania.ui.favorites.FavoriteViewModel
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class FavoriteViewModelTest {

    // Subject under test
    private lateinit var favoriteViewModel: FavoriteViewModel

    // User a fake repository to be injected into the viewModel
    private lateinit var mediaRepository: FakeRepository

    // Set the main coroutines dispatcher for unit testing
    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    // Executes each task synchronously using Architecture Components
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setupViewModel() {
        mediaRepository = FakeRepository()
        val mediaFavorite1 = MediaEntity(
            id = "464052",
            posterPath = "/8UlWHLMpgZm9bx6QYh0NFoq67TZ.jpg",
            name = "Wonder Woman 1984",
            originalTitle = "Wonder Woman 1984",
            voteAverage = 7.3f,
            genreList = null,
            backdropPath = "/srYya1ZlI97Au4jUYAktDe3avyA.jpg",
            releaseDate = null,
            overview = "Wonder Woman comes into conflict with the Soviet Union during the Cold War in the 1980s and finds a formidable foe by the name of the Cheetah.",
            numberOfSeasons = null
        )
        mediaRepository.addMediaFavorite(mediaFavorite1)

        favoriteViewModel = FavoriteViewModel(
            GetMediaFavoriteUseCase(mediaRepository),
            DeleteMediaFavoriteByIdUseCase(mediaRepository)
        )
    }

    @Test
    fun fetchMediaFavorite_loadingTogglesAndDataLoaded() {
        // Pause dispatcher so we can verify initial values
        mainCoroutineRule.pauseDispatcher()

        // Given an initialized HomeViewModel with initialized trending media
        // When loading of media is requested
        // Trigger loading of trending media
        favoriteViewModel.fetchMediaFavorite()

        // Then progress indicator is shown
        assertThat(LiveDataTestUtil.getValue(favoriteViewModel.dataLoading)).isTrue()

        // Execute pending coroutines actions
        mainCoroutineRule.resumeDispatcher()

        // Then progress indicator is hidden
        assertThat(LiveDataTestUtil.getValue(favoriteViewModel.dataLoading)).isFalse()

        // And data correctly loaded
        assertThat(LiveDataTestUtil.getValue(favoriteViewModel.mediaFavorite)).hasSize(1)
    }
}