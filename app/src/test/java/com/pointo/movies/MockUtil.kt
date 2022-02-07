package com.pointo.movies

import com.pointo.movies.data.models.*
import com.pointo.movies.data.responses.DetailResponse
import com.pointo.movies.data.responses.SearchResponse

object MockUtil {

    fun mockMovie() = Movie(
        adult = false,
        backdrop_path = "/lXhgCODAbBXL5buk9yEmTpOoOgR.jpg",
        genre_ids = listOf(12, 14, 28),
        id = 122,
        original_language = "en",
        original_title = "The Lord of the Rings: The Return of the King",
        overview = "Aragorn is revealed as the heir to the ancient kings as he, Gandalf and the other members of the broken fellowship struggle to save Gondor from Sauron's forces. Meanwhile, Frodo and Sam take the ring closer to the heart of Mordor, the dark lord's realm.",
        popularity = 73.23,
        poster_path = "/rCzpDGLbOoPwLjy3OAm5NUPOTrC.jpg",
        release_date = "2003-12-01",
        title = "The Lord of the Rings: The Return of the King",
        video = false,
        vote_average = 8.5,
        vote_count = 17193
    )

    fun mockSimpleMovie() = SimpleMovieModel(
        id = 122,
        title = "The Lord of the Rings: The Return of the King",
        poster_path = "/rCzpDGLbOoPwLjy3OAm5NUPOTrC.jpg",
        isFavorite = true,
        isWatchlist = true
    )

    fun mockSearchResponse() = SearchResponse(
        page = 1,
        results = listOf(mockMovie()),
        total_pages = 1,
        total_results = 13,
        status_code = null,
        success = true,
        status_message = null
    )

    fun mockDetailResponse() = DetailResponse(
        adult = false,
        backdrop_path = "/lXhgCODAbBXL5buk9yEmTpOoOgR.jpg",
        belongs_to_collection = BelongsToCollection(
            id = 119,
            name = "The Lord of the Rings Collection",
            poster_path = "/p4UokEk2XnjjRTdXGe6DLYXlbI1.jpg",
            backdrop_path = "/bccR2CGTWVVSZAG0yqmy3DIvhTX.jpg"
        ),
        budget = 94000000,
        genres = listOf(
            Genre(id = 12, name = "Adventure"),
            Genre(id = 14, name = "Fantasy"),
            Genre(id = 28, name = "Action")
        ),
        homepage = "http://www.lordoftherings.net",
        id = 122,
        imdb_id = "tt0167260",
        original_language = "en",
        original_title = "The Lord of the Rings: The Return of the King",
        overview = "Aragorn is revealed as the heir to the ancient kings as he, Gandalf and the other members of the broken fellowship struggle to save Gondor from Sauron's forces. Meanwhile, Frodo and Sam take the ring closer to the heart of Mordor, the dark lord's realm.",
        popularity = 73.23,
        poster_path = "/rCzpDGLbOoPwLjy3OAm5NUPOTrC.jpg",
        production_companies = listOf(
            ProductionCompany(
                id = 12,
                logo_path = "/iaYpEp3LQmb8AfAtmTvpqd4149c.png",
                name = "New Line Cinema",
                origin_country = "US"
            ),
            ProductionCompany(
                id = 11,
                logo_path = "/6FAuASQHybRkZUk08p9PzSs9ezM.png",
                name = "WingNut Films",
                origin_country = "NZ"
            ),
            ProductionCompany(
                id = 5237,
                logo_path = null,
                name = "The Saul Zaentz Company",
                origin_country = "US"
            )
        ),
        production_countries = listOf(
            ProductionCountry(iso_3166_1 = "NZ", name = "New Zealand"),
            ProductionCountry(iso_3166_1 = "US", name = "United States of America")
        ),
        release_date = "2003-12-01",
        revenue = 1118888979,
        runtime = 201,
        spoken_languages = listOf(
            SpokenLanguage(english_name = "English", iso_639_1 = "en", name = "English")
        ),
        status = "Released",
        tagline = "The eye of the enemy is moving.",
        title = "The Lord of the Rings: The Return of the King",
        video = false,
        vote_average = 8.5,
        vote_count = 17193

    )
}