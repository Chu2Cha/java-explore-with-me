DROP TABLE IF EXISTS endpointHits;

CREATE TABLE IF NOT EXISTS endpointHits
(
    id        BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    app       VARCHAR(127),
    uri       VARCHAR(127),
    ip        VARCHAR(127),
    timestamp TIMESTAMP WITHOUT TIME ZONE
);

