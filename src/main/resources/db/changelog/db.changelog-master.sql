-- Расширяем таблицу md_files
CREATE EXTENSION pgcrypto;

CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(32) NOT NULL
);

CREATE TABLE md_files (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    file_name VARCHAR(255) NOT NULL,
    file_data TEXT NOT NULL,
    file_hash CHAR(40) NOT NULL,   -- SHA-1 текущего файла
    prev_hash CHAR(40),             -- SHA-1 предыдущего файла
    insert_date TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    delete_date TIMESTAMPTZ DEFAULT NULL
);

-- Функция для вычисления SHA-1 хэшей
CREATE OR REPLACE FUNCTION set_file_hashes()
RETURNS TRIGGER AS $$
DECLARE
    last_hash CHAR(40);
BEGIN
    -- Вычисляем SHA-1 текущего файла
    NEW.file_hash := encode(digest(NEW.file_data, 'sha1'), 'hex');

    -- Получаем hash последнего файла этого пользователя
    SELECT file_hash
    INTO last_hash
    FROM md_files
    WHERE user_id = NEW.user_id
    ORDER BY insert_date DESC
    LIMIT 1;

    -- Записываем prev_hash
    NEW.prev_hash := last_hash;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Триггер перед вставкой
CREATE TRIGGER trg_set_file_hashes
BEFORE INSERT ON md_files
FOR EACH ROW
EXECUTE FUNCTION set_file_hashes();
