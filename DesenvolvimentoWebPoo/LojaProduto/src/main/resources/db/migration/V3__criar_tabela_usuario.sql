create table usuario(
    codigo BIGSERIAL PRIMARY KEY,
    username VARCHAR(100),
    senha VARCHAR(200),
    email VARCHAR(200),
    token VARCHAR(256)
)