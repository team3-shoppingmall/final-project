create database springdb;
use springdb;

create table ReviewTable(
	ReviewNO bigint not null auto_increment,	-- 후기번호(자동증가)
	-- productNO int not null,				-- 상품번호 외래키
    content varchar(600) not null,     -- 내용
	id varchar(50) not null,            -- 등록아이디
    regDate date default (current_date),
	-- regDate date not null default 'sysdate',			    -- 등록날짜
	image varchar(100),                 -- 사진이름
	star int not null,                  -- 별점

	primary key(reviewNO)
    -- foreign key(productno) references producttable(productno)
);
drop table ReviewTable

insert into reviewtable(content, id, image, star)
values('후기내용1','user1', 'image1','5'),('후기내용2','user2', 'image1','5'),('후기내용3','user3', 'image1','5');

select * from reviewtable

alter table reviewtable drop foreign key reviewtable_ibfk_1
select * from information_schema.table_constraints where table_name = 'reviewtable';

create table ProductTable(
	ProductNO int not null auto_increment,	
	productName varchar(200) null,		
    type1 varchar(50) not null,
    type2 varchar(50) not null,
    imageName varchar(50) not null,
    price int not null,
    discount int not null,
    color varchar(1000),
    size varchar(1000),
    amount int,
	regDate date,			    -- 등록날짜
	detailImageName varchar(2000) not null,
    onsale boolean,
	primary key(productNO)                  
);
insert into producttable(productname, type1, type2, imagename, price, discount, color, size, amount, regdate, detailimagename, onsale)
values('티셔츠','타입1', '타입2', '이미지', '4000', '1000', '블랙', 'm','11', '1111-01-01', '1','가나다','true');

create table ProductTable(
	ProductNO int not null auto_increment,	
	productName varchar(200) null,	
    primary key(productNO)  
    );

insert into producttable(productname) values("바지");
select * from producttable