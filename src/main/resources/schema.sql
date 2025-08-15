-- Script ejemplo para creación de BD (H2 in-memory)
CREATE TABLE IF NOT EXISTS users (
  id VARCHAR(36) PRIMARY KEY,
  name VARCHAR(255),
  email VARCHAR(255) UNIQUE NOT NULL,
  password VARCHAR(255) NOT NULL,
  created TIMESTAMP,
  modified TIMESTAMP,
  last_login TIMESTAMP,
  token VARCHAR(512),
  isactive BOOLEAN
);

CREATE TABLE IF NOT EXISTS phones (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  number number,
  citycode number,
  countrycode number,
  user_id VARCHAR(36),
  CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users(id)
);
