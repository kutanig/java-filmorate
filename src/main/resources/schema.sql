CREATE TABLE IF NOT EXISTS genres (
    id INTEGER NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL UNIQUE,
    CONSTRAINT genre_pk PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS mpa (
    id INTEGER NOT NULL AUTO_INCREMENT,
    name VARCHAR(10) NOT NULL UNIQUE,
    description VARCHAR(200),
    CONSTRAINT mpa_pk PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS users (
    id INTEGER NOT NULL AUTO_INCREMENT,
    name VARCHAR(60) NOT NULL,
    login VARCHAR(60) NOT NULL UNIQUE,
    email VARCHAR(200) NOT NULL UNIQUE,
    birthday DATE NOT NULL,
    CONSTRAINT user_pk PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS films (
    id INTEGER NOT NULL AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    description VARCHAR(200),
    release_date DATE NOT NULL,
    duration INTEGER NOT NULL CHECK (duration > 0),
    mpa_id INTEGER NOT NULL,
    CONSTRAINT film_pk PRIMARY KEY (id),
    CONSTRAINT film_mpa_fk FOREIGN KEY (mpa_id) REFERENCES mpa(id) ON DELETE RESTRICT
);

CREATE TABLE IF NOT EXISTS film_genres (
    film_id INTEGER NOT NULL,
    genre_id INTEGER NOT NULL,
    CONSTRAINT film_genre_pk PRIMARY KEY (film_id, genre_id),
    CONSTRAINT film_fk FOREIGN KEY (film_id) REFERENCES films(id) ON DELETE CASCADE,
    CONSTRAINT genre_fk FOREIGN KEY (genre_id) REFERENCES genres(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS film_likes (
    film_id INTEGER NOT NULL,
    user_id INTEGER NOT NULL,
    CONSTRAINT film_like_pk PRIMARY KEY (film_id, user_id),
    CONSTRAINT like_film_fk FOREIGN KEY (film_id) REFERENCES films(id) ON DELETE CASCADE,
    CONSTRAINT like_user_fk FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS friendships (
    user_id INTEGER NOT NULL,
    friend_id INTEGER NOT NULL,
    status BOOLEAN NOT NULL DEFAULT FALSE,
    CONSTRAINT friendship_pk PRIMARY KEY (user_id, friend_id),
    CONSTRAINT friendship_user_fk FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT friendship_friend_fk FOREIGN KEY (friend_id) REFERENCES users(id) ON DELETE CASCADE
);