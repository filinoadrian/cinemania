package com.far_sstrwnt.cinemania

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.far_sstrwnt.cinemania.model.*
import com.far_sstrwnt.cinemania.shared.domain.*
import com.far_sstrwnt.cinemania.ui.mediadetail.MediaDetailViewModel
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class MediaDetailViewModelTest {

    // Subject under test
    private lateinit var mediaDetailViewModel: MediaDetailViewModel

    // User a fake repository to be injected into the viewModel
    private lateinit var mediaRepository: FakeRepository

    // Set the main coroutines dispatcher for unit testing
    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    // Executes each task synchronously using Architecture Components
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private val media = MediaEntity(
        id = "44217",
        posterPath = "/8UlWHLMpgZm9bx6QYh0NFoq67TZ.jpg",
        name = "Vikings",
        originalTitle = "Vikings",
        voteAverage = 8f,
        genreList = null,
        backdropPath = "/aq2yEMgRQBPfRkrO0Repo2qhUAT.jpg",
        releaseDate = null,
        overview = "The adventures of Ragnar Lothbrok, the greatest hero of his age. The series tells the sagas of Ragnar's band of Viking brothers and his family, as he rises to become King of the Viking tribes. As well as being a fearless warrior, Ragnar embodies the Norse traditions of devotion to the gods. Legend has it that he was a direct descendant of Odin, the god of war and warriors.",
        numberOfSeasons = 6
    )

    @Before
    fun setupViewModel() {
        mediaRepository = FakeRepository()
        mediaRepository.addMedia(media)

        val cast1 = CastEntity(
            castId = null,
            character = "Bjorn Lothbrok",
            creditId = "543df5ee0e0a266f8e001347",
            gender = 2,
            id = "23498",
            name = "Alexander Ludwig",
            order = 1,
            profilePath = "/unP5YUgEdECL2gMLs0zCNya6is6.jpg"
        )
        mediaRepository.addMediaCast(cast1)

        val video1 = VideoEntity(
            id = "555f5723c3a36868e000301a",
            key = "1j2sXLbzm9U",
            name = "Vikings: Trailer",
            site = "YouTube",
            size = 480,
            type = "Trailer"
        )
        mediaRepository.addMediaVideo(video1)

        val episode1 = EpisodeEntity(
            id = "892432",
            episodeNumber = 1,
            name = "Rites of Passage",
            overview = "Ragnar Lothbrok is a warrior and a farmer who dreams of finding riches by bucking the tradition.",
            stillPath = "/7cuiE0fvDXnCyVbQ5EXHtIz2sAr.jpg"
        )
        mediaRepository.addMediaEpisode(episode1)

        mediaDetailViewModel = MediaDetailViewModel(
            GetMediaDetailUseCase(mediaRepository),
            GetMediaCastUseCase(mediaRepository),
            GetMediaVideosUseCase(mediaRepository),
            GetMediaSimilarUseCase(mediaRepository),
            GetTvSeasonUseCase(mediaRepository),
            GetMediaFavoriteByIdUseCase(mediaRepository),
            InsertMediaFavoriteUseCase(mediaRepository),
            DeleteMediaFavoriteByIdUseCase(mediaRepository)
        )
    }

    @Test
    fun fetchMediaDetail_loading() {
        // Pause dispatcher so we can verify initial values
        mainCoroutineRule.pauseDispatcher()

        // Fetch media detail in the viewModel
        mediaDetailViewModel.fetchMediaDetail(MediaType.MOVIE.value, media.id)

        // Then progress indicator is shown
        assertThat(LiveDataTestUtil.getValue(mediaDetailViewModel.dataLoading)).isTrue()

        // Execute pending coroutines actions
        mainCoroutineRule.resumeDispatcher()

        // Then progress indicator is hidden
        assertThat(LiveDataTestUtil.getValue(mediaDetailViewModel.dataLoading)).isFalse()
    }

    @Test
    fun fetchMediaDetail_dataLoaded() {
        mediaDetailViewModel.fetchMediaDetail(MediaType.MOVIE.value, media.id)

        // Then verify that the view was notified
        assertThat(LiveDataTestUtil.getValue(mediaDetailViewModel.mediaDetail).id).isEqualTo(media.id)
        assertThat(LiveDataTestUtil.getValue(mediaDetailViewModel.mediaDetail).posterPath).isEqualTo(media.posterPath)
        assertThat(LiveDataTestUtil.getValue(mediaDetailViewModel.mediaDetail).name).isEqualTo(media.name)
        assertThat(LiveDataTestUtil.getValue(mediaDetailViewModel.mediaDetail).originalTitle).isEqualTo(media.originalTitle)
        assertThat(LiveDataTestUtil.getValue(mediaDetailViewModel.mediaDetail).voteAverage).isEqualTo(media.voteAverage)
        assertThat(LiveDataTestUtil.getValue(mediaDetailViewModel.mediaDetail).genreList).isEqualTo(media.genreList)
        assertThat(LiveDataTestUtil.getValue(mediaDetailViewModel.mediaDetail).backdropPath).isEqualTo(media.backdropPath)
        assertThat(LiveDataTestUtil.getValue(mediaDetailViewModel.mediaDetail).releaseDate).isEqualTo(media.releaseDate)
        assertThat(LiveDataTestUtil.getValue(mediaDetailViewModel.mediaDetail).overview).isEqualTo(media.overview)
        assertThat(LiveDataTestUtil.getValue(mediaDetailViewModel.mediaDetail).numberOfSeasons).isEqualTo(media.numberOfSeasons)
    }

    @Test
    fun fetchMediaCast_dataLoaded() {
        mediaDetailViewModel.fetchMediaCast(MediaType.MOVIE.value, media.id)

        // Then verify that the view was notified
        assertThat(LiveDataTestUtil.getValue(mediaDetailViewModel.mediaCast)).hasSize(1)
    }

    @Test
    fun fetchMediaVideo_dataLoaded() {
        mediaDetailViewModel.fetchMediaVideos(MediaType.MOVIE.value, media.id)

        // Then verify that the view was notified
        assertThat(LiveDataTestUtil.getValue(mediaDetailViewModel.mediaVideos)).hasSize(1)
    }

    @Test
    fun fetchMediaEpisodes_dataLoaded() {
        mediaDetailViewModel.fetchMediaEpisodes(media.id, 1)

        // Then verify that the view was notified
        assertThat(LiveDataTestUtil.getValue(mediaDetailViewModel.mediaEpisodes)).hasSize(1)
    }
}