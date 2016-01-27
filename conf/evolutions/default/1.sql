# Messages schema

# --- !Ups

create table "MESSAGES" ("id" bigint(20) NOT NULL PRIMARY KEY,"message" VARCHAR NOT NULL);

INSERT INTO messages values(1, 'Hi!');
INSERT INTO messages values(2, 'What''s up?');
INSERT INTO messages values(3, 'Your new application is ready.');

# --- !Downs

DROP TABLE "MESSAGES";