package com.far_sstrwnt.cinemania

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import com.far_sstrwnt.cinemania.model.GenreEntity
import com.far_sstrwnt.cinemania.model.MediaEntity
import com.far_sstrwnt.cinemania.model.MediaType
import com.far_sstrwnt.cinemania.shared.domain.GetMediaByCategoryUseCase
import com.far_sstrwnt.cinemania.shared.domain.GetMediaGenreUseCase
import com.far_sstrwnt.cinemania.shared.domain.GetMediaTrendingUseCase
import com.far_sstrwnt.cinemania.ui.home.HomeViewModel
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class HomeViewModelTest {

    // Subject under test
    private lateinit var homeViewModel: HomeViewModel

    // Use a fake repository to be injected into the viewModel
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
        val mediaTrending1 = MediaEntity(
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
        mediaRepository.addMedia(mediaTrending1)

        homeViewModel = HomeViewModel(
            GetMediaTrendingUseCase(mediaRepository),
            GetMediaByCategoryUseCase(mediaRepository)
        )
    }

    @Test
    fun fetchMediaTrending_loadingTogglesAndDataLoaded() {
        // Pause dispatcher so we can verify initial values
        mainCoroutineRule.pauseDispatcher()

        // Given an initialized HomeViewModel with initialized trending media
        // When loading of media is requested
        // Trigger loading of trending media
        homeViewModel.fetchMediaTrending(MediaType.MOVIE.value)

        // Then progress indicator is shown
        assertThat(LiveDataTestUtil.getValue(homeViewModel.dataLoading)).isTrue()

        // Execute pending coroutines actions
        mainCoroutineRule.resumeDispatcher()

        // Then progress indicator is hidden
        assertThat(LiveDataTestUtil.getValue(homeViewModel.dataLoading)).isFalse()

        // And data correctly loaded
        assertThat(LiveDataTestUtil.getValue(homeViewModel.mediaTrending)).hasSize(1)
    }

    @Test
    fun fetchMediaTrending_error() {
        // Make the repository return errors
        mediaRepository.setReturnError(true)

        // Fetch media trending
        homeViewModel.fetchMediaTrending(MediaType.MOVIE.value)

        // Then progress indicator is hidden
        assertThat(LiveDataTestUtil.getValue(homeViewModel.dataLoading)).isFalse()

        // And the list of items is empty
        assertThat(LiveDataTestUtil.getValue(homeViewModel.mediaTrending)).isEmpty()

        // And the snackbar updated
        assertSnackbarMessage(homeViewModel.snackbarMessage, R.string.loading_trending_error)
    }

    @Test
    fun clickOnOpenMediaDetail_setsEvent() {
        // When opening a new task
        val mediaType = MediaType.MOVIE.value
        val mediaId = "464052"
        homeViewModel.openDetail(mediaType, mediaId)

        // Then the event is triggered
        assertLiveDataEventTriggered(homeViewModel.navigateToMediaDetailAction, Pair(mediaType, mediaId))
    }
}