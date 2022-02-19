drop database if exists springdb;
create database springdb;
use springdb;

create table qna(
	qnaNo bigint not null primary key auto_increment,
    productNo int,
    type varchar(200) not null,
    originalNo bigint,
    reply boolean default false,
    content varchar(2000) not null,
    id varchar(50) not null,
    regdate date not null,
    secret boolean default false,
    image varchar(500) not null);

commit;

select * from qna;
