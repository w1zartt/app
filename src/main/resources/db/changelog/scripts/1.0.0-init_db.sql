CREATE TABLE publisher (
    id          bigserial       PRIMARY KEY NOT NULL,
    name        varchar(255),
    tg_link     varchar(255),
    role        varchar(30)
);

CREATE TABLE image (
    id              bigserial PRIMARY KEY NOT NULL,
    name            varchar,
    link            text
);

CREATE TABLE book (
    id                      bigserial           PRIMARY KEY NOT NULL,
    name_russian            varchar(255),
    name_english            varchar(255),
    author_name_english     varchar(255),
    author_name_russian     varchar(255),
    image_id                bigserial,
    storage_url             text,
    description             text,
    publisher_id            bigserial,
    FOREIGN KEY (publisher_id) REFERENCES publisher(id),
    FOREIGN KEY (image_id) REFERENCES image(id)
);


