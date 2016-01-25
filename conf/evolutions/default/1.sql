# Messages schema

# --- !Ups

CREATE TABLE messages (
    id SERIAL PRIMARY KEY,
    message varchar(255) NOT NULL
);

INSERT INTO messages (message) values('Hi!');
INSERT INTO messages (message) values('What''s up?');
INSERT INTO messages (message) values('Am I alive now?');

# --- !Downs

DROP TABLE messages;