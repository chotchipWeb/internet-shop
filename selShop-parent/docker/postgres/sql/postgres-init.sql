CREATE USER catalogueUser WITH PASSWORD 'user';
CREATE DATABASE products;
GRANT ALL PRIVILEGES ON DATABASE products TO catalogueUser;