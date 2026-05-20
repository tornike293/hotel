CREATE TABLE IF NOT EXISTS apartments
(
    id
    INTEGER
    PRIMARY
    KEY,
    price
    DECIMAL
(
    15,
    2
) NOT NULL,
    reserved BOOLEAN NOT NULL DEFAULT FALSE,
    client_name VARCHAR
(
    255
)
    );