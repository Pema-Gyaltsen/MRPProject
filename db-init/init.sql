-- start clean if you re-run this:
DROP TABLE IF EXISTS rating_likes CASCADE;
DROP TABLE IF EXISTS favorites CASCADE;
DROP TABLE IF EXISTS ratings CASCADE;
DROP TABLE IF EXISTS media CASCADE;
DROP TABLE IF EXISTS users CASCADE;

-- users
CREATE TABLE users (
  id             BIGSERIAL PRIMARY KEY,
  username       TEXT UNIQUE NOT NULL,
  password_hash  TEXT NOT NULL,
  email          TEXT,
  favorite_genre TEXT,
  created_at     TIMESTAMPTZ DEFAULT NOW()
);

-- media
CREATE TABLE media (
  id              BIGSERIAL PRIMARY KEY,
  title           TEXT NOT NULL,
  description     TEXT,
  media_type      TEXT NOT NULL,      -- e.g. 'movie', 'series', 'book'
  release_year    INT,
  genres          TEXT[] DEFAULT '{}',-- array of strings
  age_restriction INT,
  created_by      BIGINT REFERENCES users(id) ON DELETE SET NULL,
  created_at      TIMESTAMPTZ DEFAULT NOW()
);

-- ratings (each user may rate a media once)
CREATE TABLE ratings (
  id         BIGSERIAL PRIMARY KEY,
  user_id    BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
  media_id   BIGINT NOT NULL REFERENCES media(id) ON DELETE CASCADE,
  stars      INT NOT NULL CHECK (stars BETWEEN 1 AND 5),
  comment    TEXT,
  confirmed  BOOLEAN DEFAULT FALSE,   -- for "Confirm Rating Comment"
  created_at TIMESTAMPTZ DEFAULT NOW(),
  updated_at TIMESTAMPTZ DEFAULT NOW(),
  CONSTRAINT uq_user_media UNIQUE (user_id, media_id)
);

-- who liked which rating (for "Like Rating")
CREATE TABLE rating_likes (
  rating_id BIGINT NOT NULL REFERENCES ratings(id) ON DELETE CASCADE,
  user_id   BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
  PRIMARY KEY (rating_id, user_id)
);

-- favorites (for "Mark/Unmark Favorite")
CREATE TABLE favorites (
  user_id  BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
  media_id BIGINT NOT NULL REFERENCES media(id) ON DELETE CASCADE,
  PRIMARY KEY (user_id, media_id)
);

-- helpful indexes
CREATE INDEX idx_media_title        ON media (title);
CREATE INDEX idx_media_release_year ON media (release_year);
CREATE INDEX idx_ratings_media      ON ratings (media_id);
CREATE INDEX idx_ratings_user       ON ratings (user_id);
