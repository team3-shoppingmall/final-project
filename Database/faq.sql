drop database if exists springdb;
create database springdb;
use springdb;
drop table if exists faqtable;
create table faqtable(
   faqNo INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
   type VARCHAR(200) NOT NULL,
   title VARCHAR(2000) NOT NULL,
   content VARCHAR(2000) NOT NULL);
   select * from faqtable;
   commit;