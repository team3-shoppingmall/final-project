use springdb;

drop table if exists membertable;
create table membertable(
	id varchar(50) not null,
    password varchar(256) not null,
    name varchar(50) not null,
    tel varchar(11) not null,
    email varchar(50) not null,
    zipcode varchar(10) not null,
    addr1 varchar(80) not null,
    addr2 varchar(50) not null,
    terms boolean default false,
    point int default 2000,
    authority varchar(20) not null,
    
    constraint membertable_pk primary key (id)
);

insert into membertable(id,password,name,tel,email,zipcode,addr1,addr2,authority) 
values('admin','admin','admin','01012345678','sunshine4437@gmail.com','zipcode','부산광역시 강서구 녹산산단382로14번가길 29번지(송정동)','addr2','ROLE_ADMIN');
commit;
select * from membertable;
select * from membertable where point like '%0%';
