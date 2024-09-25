DROP TABLE IF EXISTS users_friendship, film_likes, "user", film_genre, genre, films, mpa_rating;

CREATE TABLE IF NOT EXISTS mpa_rating (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name varchar
);

CREATE TABLE IF NOT EXISTS films (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    title varchar NOT NULL,
    description varchar(200),
    release_date date,
    duration integer,
    rating_id bigint REFERENCES mpa_rating(id)
);

CREATE TABLE IF NOT EXISTS genre (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name varchar NOT NULL
);

CREATE TABLE IF NOT EXISTS film_genre (
    film_id BIGINT REFERENCES films(id) ON DELETE CASCADE,
    genre_id BIGINT REFERENCES genre(id) ON DELETE CASCADE,
    PRIMARY KEY (film_id, genre_id)
);

CREATE TABLE IF NOT EXISTS "user" (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    email varchar UNIQUE NOT NULL,
    login varchar UNIQUE NOT NULL,
    name varchar,
    birthday date
);

CREATE TABLE IF NOT EXISTS film_likes (
    film_id BIGINT REFERENCES films(id) ON DELETE CASCADE,
    user_id BIGINT REFERENCES "user"(id) ON DELETE CASCADE,
    PRIMARY KEY (film_id, user_id)
);

CREATE TABLE IF NOT EXISTS users_friendship (
    initiator_id BIGINT REFERENCES "user"(id) ON DELETE CASCADE,
    friend_id BIGINT REFERENCES "user"(id) ON DELETE CASCADE,
    status varchar NOT NULL,
    PRIMARY KEY (initiator_id, friend_id)
);

CREATE TABLE IF NOT EXISTS reviews (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    content varchar NOT NULL,
    is_positive boolean NOT NULL,
    user_id BIGINT NOT NULL REFERENCES "user"(id) ON DELETE CASCADE,
    film_id BIGINT NOT NULL REFERENCES films(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS review_likes (
    review_id BIGINT REFERENCES reviews(id) ON DELETE CASCADE,
    user_id BIGINT REFERENCES "user"(id) ON DELETE CASCADE,
    like_type varchar NOT NULL,
    PRIMARY KEY (review_id, user_id)
);