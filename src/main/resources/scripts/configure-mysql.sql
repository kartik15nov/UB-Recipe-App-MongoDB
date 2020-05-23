## Use to run mysql db docker image, optional if you're not using a local mysqldb
# docker run --name mysqldb -p 3306:3306 -e MYSQL_ALLOW_EMPTY_PASSWORD=yes -d mysql

# connect to mysql and run as root user

#Create Databases
CREATE DATABASE ub_dev;
CREATE DATABASE ub_prod;

#Create database service accounts
CREATE USER 'ub_dev_user'@'localhost' IDENTIFIED BY 'ub123';
CREATE USER 'ub_prod_user'@'localhost' IDENTIFIED BY 'ub123';
CREATE USER 'ub_dev_user'@'%' IDENTIFIED BY 'ub123';
CREATE USER 'ub_prod_user'@'%' IDENTIFIED BY 'ub123';

#Database grants
GRANT SELECT ON ub_dev.* to 'ub_dev_user'@'localhost';
GRANT INSERT ON ub_dev.* to 'ub_dev_user'@'localhost';
GRANT DELETE ON ub_dev.* to 'ub_dev_user'@'localhost';
GRANT UPDATE ON ub_dev.* to 'ub_dev_user'@'localhost';
GRANT SELECT ON ub_prod.* to 'ub_prod_user'@'localhost';
GRANT INSERT ON ub_prod.* to 'ub_prod_user'@'localhost';
GRANT DELETE ON ub_prod.* to 'ub_prod_user'@'localhost';
GRANT UPDATE ON ub_prod.* to 'ub_prod_user'@'localhost';
GRANT SELECT ON ub_dev.* to 'ub_dev_user'@'%';
GRANT INSERT ON ub_dev.* to 'ub_dev_user'@'%';
GRANT DELETE ON ub_dev.* to 'ub_dev_user'@'%';
GRANT UPDATE ON ub_dev.* to 'ub_dev_user'@'%';
GRANT SELECT ON ub_prod.* to 'ub_prod_user'@'%';
GRANT INSERT ON ub_prod.* to 'ub_prod_user'@'%';
GRANT DELETE ON ub_prod.* to 'ub_prod_user'@'%';
GRANT UPDATE ON ub_prod.* to 'ub_prod_user'@'%';