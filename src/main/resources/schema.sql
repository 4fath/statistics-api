-- CREATE TABLE transactions
-- (
--   timestamp BIGINT NOT NULL,
--   amount    DOUBLE NOT NULL,
--   PRIMARY KEY (timestamp)
-- );

CREATE TABLE summary
(
  timestamp BIGINT NOT NULL,
  sum       DOUBLE NOT NULL,
  average   DOUBLE NOT NULL,
  max       DOUBLE NOT NULL,
  min       DOUBLE NOT NULL,
  count     INT    NOT NULL,
  PRIMARY KEY (timestamp)
);
