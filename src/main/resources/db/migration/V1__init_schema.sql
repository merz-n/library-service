CREATE EXTENSION IF NOT EXISTS pgcrypto;

-- AUTHORS
CREATE TABLE IF NOT EXISTS authors (
  id         UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  name       VARCHAR(255) NOT NULL,
  birth_date DATE
);
CREATE INDEX IF NOT EXISTS idx_authors_name ON authors(name);

-- BOOKS
CREATE TABLE IF NOT EXISTS books (
  id         UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  title      VARCHAR(255) NOT NULL,
  isbn       VARCHAR(20)  NOT NULL,
  author_id  UUID         NOT NULL,
  available  BOOLEAN      NOT NULL DEFAULT TRUE,
  CONSTRAINT fk_books_author FOREIGN KEY (author_id)
    REFERENCES authors(id) ON UPDATE CASCADE ON DELETE RESTRICT
);
ALTER TABLE books ADD CONSTRAINT uq_books_isbn UNIQUE (isbn);
CREATE INDEX IF NOT EXISTS idx_books_title     ON books(title);
CREATE INDEX IF NOT EXISTS idx_books_author_id ON books(author_id);
CREATE INDEX IF NOT EXISTS idx_books_available ON books(available);
