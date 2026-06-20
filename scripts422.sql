-- Создание таблицы машин
CREATE TABLE car (
                     id BIGSERIAL PRIMARY KEY,
                     brand VARCHAR(100) NOT NULL,
                     model VARCHAR(100) NOT NULL,
                     price NUMERIC(12, 2) NOT NULL
);

-- Создание таблицы людей с внешним ключом на таблицу машин
CREATE TABLE person (
                        id BIGSERIAL PRIMARY KEY,
                        name VARCHAR(150) NOT NULL,
                        age INT NOT NULL,
                        has_driver_license BOOLEAN NOT NULL DEFAULT FALSE,
                        car_id BIGINT REFERENCES car(id) ON DELETE SET NULL
);