-- USERS
CREATE TABLE IF NOT EXISTS users (
  id    UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  name  VARCHAR(255) NOT NULL,
  email VARCHAR(255) NOT NULL UNIQUE
);

-- текущий держатель книги (может быть NULL)
ALTER TABLE books
  ADD COLUMN IF NOT EXISTS borrowed_by_id UUID NULL,
  ADD CONSTRAINT fk_books_borrowed_by FOREIGN KEY (borrowed_by_id)
    REFERENCES users(id) ON UPDATE CASCADE ON DELETE SET NULL;

CREATE INDEX IF NOT EXISTS idx_books_borrowed_by ON books(borrowed_by_id);
