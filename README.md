# Cinemania

A simple Android app that allow user to discover movies and tv series. This app fetch data using The Movie Database API.

## Getting Started

1. Clone this repository in your local machine

```
git clone https://github.com/filinoadrian/cinemania.git
```

2. Get a developer API key from [The Movie Database](https://www.themoviedb.org/documentation/api) website

3. Put your API key into gradle.properties file substitute "\<API-KEY>\" with your actual API key. 

```
TMDB_API_TOKEN="<API-KEY>"
```

## Features

- Discover movies and tv series by category (trending, popular, top rated, etc.)
- Search movies and tv series by genre or title
- Get detail about movies and tv series (overview, cast, episodes, similar)
- Watch movie and tv series trailers and teasers
- Mark movies and tv series as favorites

## Screenshots

![Home Fragment](https://user-images.githubusercontent.com/18563489/103913146-d2b59280-513a-11eb-8cf2-d77d46bd2be9.jpg) ![Home Fragment 2](https://user-images.githubusercontent.com/18563489/103913649-5cfdf680-513b-11eb-80b0-b6007d78188f.jpg) ![Favorites](https://user-images.githubusercontent.com/18563489/103914074-df86b600-513b-11eb-9909-d195c09b1fe9.jpg)

![Search Fragment](https://user-images.githubusercontent.com/18563489/103913837-97679380-513b-11eb-90e0-45d9ab8b1780.jpg) ![Search Result](https://user-images.githubusercontent.com/18563489/103913850-9a628400-513b-11eb-9107-576e22043d76.jpg) ![Search by Genre](https://user-images.githubusercontent.com/18563489/103913841-9898c080-513b-11eb-966d-e19d3590c4f8.jpg)

![Detail Overview](https://user-images.githubusercontent.com/18563489/103913996-c4b44180-513b-11eb-8c25-2fe5032e1c71.jpg) ![Detail Cast](https://user-images.githubusercontent.com/18563489/103914000-c5e56e80-513b-11eb-9036-ba7c237ffc31.jpg) ![Detail Episode](https://user-images.githubusercontent.com/18563489/103914004-c7169b80-513b-11eb-89e9-996a277112c5.jpg)

## Libraries

- [Android Architecture Components](https://developer.android.com/topic/libraries/architecture/)
  - [Data Binding](https://developer.android.com/topic/libraries/data-binding)
  - [LiveData](https://developer.android.com/topic/libraries/architecture/livedata)
  - [Paging](https://developer.android.com/topic/libraries/architecture/paging/v3-overview)
  - [Room](https://developer.android.com/jetpack/androidx/releases/room)
- [Android Navigation Components](https://developer.android.com/guide/navigation)
- [Material Design](https://material.io/design)
- [Kotlin Coroutines](https://developer.android.com/kotlin/coroutines)
- [Retrofit](https://square.github.io/retrofit/)
- [Dagger](https://dagger.dev/dev-guide/)
- [Glide](https://github.com/bumptech/glide)
