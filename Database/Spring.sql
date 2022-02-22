drop database if exists springdb;
create database springdb;
use springdb;

-- table drop
drop table if exists bannertable;
drop table if exists qnatable;
drop table if exists reviewtable;
drop table if exists faqtable;
drop table if exists noticetable;
drop table if exists pointtable;
drop table if exists ordertable;
drop table if exists wishlisttable;
drop table if exists baskettable;
drop table if exists producttable;
drop table if exists membertable;

-- drop trigger
DROP TRIGGER IF EXISTS `springdb`.`membertable_BEFORE_INSERT`;
DROP TRIGGER IF EXISTS `springdb`.`pointtable_BEFORE_INSERT`;

-- create table and trigger
CREATE TABLE membertable (
    ID VARCHAR(50) PRIMARY KEY,
    PASSWORD VARCHAR(256) NOT NULL,
    NAME VARCHAR(50) NOT NULL,
    TEL VARCHAR(11) NOT NULL,
    EMAIL VARCHAR(50) NOT NULL,
    ZIPCODE VARCHAR(10) NOT NULL,
    ADDRESS VARCHAR(80) NOT NULL,
    DETAILADDR VARCHAR(50) NOT NULL,
    TERMS BOOLEAN NOT NULL,
    POINT INT,
    AUTHORITY VARCHAR(20) NOT NULL
);

DELIMITER $$
USE `springdb`$$
CREATE DEFINER = CURRENT_USER TRIGGER `springdb`.`membertable_BEFORE_INSERT` BEFORE INSERT ON `membertable` FOR EACH ROW
BEGIN
set new.point = 2000;
END$$
DELIMITER ;

CREATE TABLE producttable(
	PRODUCTNO INT PRIMARY KEY AUTO_INCREMENT,
	PRODUCTNAME VARCHAR(200) NOT NULL,
	TYPE1 VARCHAR(50) NOT NULL,
	TYPE2 VARCHAR(50) NOT NULL,
	IMAGENAME VARCHAR(100) NOT NULL,
	PRICE INT NOT NULL,
	DISCOUNT INT NOT NULL,
	COLOR VARCHAR(1000),
	SIZE VARCHAR(1000),
	AMOUNT INT NOT NULL,
    REGDATE TIMESTAMP DEFAULT (current_timestamp),
	DETAILIMAGENAME VARCHAR(2000) NOT NULL,
	ONSALE BOOLEAN DEFAULT FALSE
);

CREATE TABLE baskettable (
    BASKETIDX BIGINT PRIMARY KEY AUTO_INCREMENT,
    ID VARCHAR(50) NOT NULL,
    PRODUCTNO INT NOT NULL,
    SELECTEDCOLOR VARCHAR(100),
    SELECTEDSIZE VARCHAR(100),
    AMOUNT INT NOT NULL,
    CONSTRAINT basket_fk_id FOREIGN KEY (ID)
        REFERENCES membertable (ID),
    CONSTRAINT basket_fk_productno FOREIGN KEY (PRODUCTNO)
        REFERENCES producttable (PRODUCTNO)
);

CREATE TABLE wishlisttable (
    WishListIDX BIGINT PRIMARY KEY AUTO_INCREMENT,
    ID VARCHAR(50) NOT NULL,
    PRODUCTNO INT NOT NULL
);

CREATE TABLE ordertable (
	ORDERIDX BIGINT PRIMARY KEY AUTO_INCREMENT,
	ID VARCHAR(50) NOT NULL,
	PRODUCTNO INT NOT NULL,
	ORDERNO BIGINT,
	SELECTEDCOLOR VARCHAR(100),
	SELECTEDSIZE VARCHAR(100),
	AMOUNT INT NOT NULL,
	TOTALPRICE INT NOT NULL,
	ORDERDATE TIMESTAMP DEFAULT (current_timestamp),
	STATE VARCHAR(20) DEFAULT '결제 완료',
	ORDERMETHOD VARCHAR(40) NOT NULL,
	NAME VARCHAR(50) NOT NULL,
	TEL VARCHAR(11) NOT NULL,
	ZIPCODE VARCHAR(5) NOT NULL,
	ADDRESS VARCHAR(200) NOT NULL,
	DETAILADDR VARCHAR(50) NOT NULL,
    CONSTRAINT order_fk_id FOREIGN KEY (ID) REFERENCES membertable (ID),
    CONSTRAINT order_fk_productno FOREIGN KEY (PRODUCTNO) REFERENCES producttable (PRODUCTNO)
);

CREATE TABLE pointtable (
    NUM BIGINT PRIMARY KEY AUTO_INCREMENT,
    ID VARCHAR(50) NOT NULL,
    POINT INT NOT NULL,
	POINTDATE TIMESTAMP DEFAULT (current_timestamp),
    CONSTRAINT point_fk_id FOREIGN KEY (ID) REFERENCES membertable (ID)
);

DELIMITER $$
USE `springdb`$$
CREATE DEFINER = CURRENT_USER TRIGGER `springdb`.`pointtable_BEFORE_INSERT` BEFORE INSERT ON `pointtable` FOR EACH ROW
BEGIN
update membertable
set point = point + New.point
where id = New.id;
END$$
DELIMITER ;

CREATE TABLE noticetable (
    NOTICENO INT PRIMARY KEY AUTO_INCREMENT,
    TITLE VARCHAR(200) NOT NULL,
    CONTENT VARCHAR(2000) NOT NULL,
    ID VARCHAR(50) NOT NULL,
    IMAGE VARCHAR(500)
);

CREATE TABLE faqtable (
    FAQNO INT PRIMARY KEY AUTO_INCREMENT,
    TYPE VARCHAR(200) NOT NULL,
    TITLE VARCHAR(2000) NOT NULL,
    CONTENT VARCHAR(2000) NOT NULL
);

CREATE TABLE reviewTable(
	REVIEWNO BIGINT PRIMARY KEY AUTO_INCREMENT,
	PRODUCTNO INT NOT NULL,
    CONTENT VARCHAR(600) NOT NULL,
	id VARCHAR(50) NOT NULL,
    REGDATE TIMESTAMP DEFAULT (current_timestamp),
	IMAGE VARCHAR(100),
	STAR INT NOT NULL,
    CONSTRAINT review_fk_productno FOREIGN KEY (PRODUCTNO) REFERENCES producttable (PRODUCTNO)
);

CREATE TABLE qnatable(
	QNANO BIGINT PRIMARY KEY AUTO_INCREMENT,
	PRODUCTNO INT,
    TYPE VARCHAR(200) NOT NULL,
    ORIGINALNO BIGINT,
    REPLY BOOLEAN DEFAULT FALSE,
    CONTENT VARCHAR(2000) NOT NULL,
    ID VARCHAR(50) NOT NULL,
    REGDATE TIMESTAMP DEFAULT (current_timestamp),
    SECRET BOOLEAN NOT NULL,
    IMAGE VARCHAR(500) NOT NULL
);

CREATE TABLE bannertable (
    NUM INT PRIMARY KEY AUTO_INCREMENT,
    IMAGE VARCHAR(100) NOT NULL,
    LINK VARCHAR(100) NOT NULL
);

-- insert data
-- 회원
insert into membertable values('admin','Asdqwe123','관리자','0212345678','spring@gmail.com','12345','서울 강남구 테헤란로 212 (멀티캠퍼스)','2층 201호',false,null,'ROLE_ADMIN');
insert into membertable values('tester','Asdqwe123','유저','01098765432','user@gmail.com','54321','부산 남구 문현로 56-1 (네이버코리아)','5층 502호',false,null,'ROLE_USER');
insert into membertable values('tester2','Asdqwe123','유저2','01045614561','user2@gmail.com','24241','부산 문현로 56-1 (네이버코리아)','4층 405호',false,null,'ROLE_USER');
-- 상품
insert into producttable(productname, type1, type2, imagename, price, discount, color, size, amount, detailimagename) 
values('스노우 버튼 모직스커트', 'skirt','mini','test.jpg',38000,0,'그레이지;소프트민트;','S;M;', 100,'test.jpg');
insert into producttable(productname, type1, type2, imagename, price, discount, color, size, amount, detailimagename)
values('실키 여리핏 히든블라우스', 'shirt','blouse','test.jpg',34900,5000,'아이보리;피치베이지;워터리블루;블랙;',null, 100,'test.jpg');
-- 장바구니
insert into baskettable(id, productno, selectedcolor, selectedsize, amount) values('tester',1,'소프트민트','S',2);
insert into baskettable(id, productno, selectedcolor, selectedsize, amount) values('tester',2,'아이보리',null,5);
-- 관심상품
insert into wishlisttable(id, productno) values('tester', 1);
insert into wishlisttable(id, productno) values('tester', 2);
-- 주문
insert into ordertable(id, productno, orderno, selectedcolor, selectedsize, amount, totalprice, ordermethod, name, tel, zipcode, address, detailaddr)
values('tester',1,1,'그레이지','S',1,38000,'피치베이지','유저','01098765432','54321','부산 남구 문현로 56-1 (네이버코리아)','5층 502호');
insert into ordertable(id, productno, orderno, selectedcolor, selectedsize, amount, totalprice, ordermethod, name, tel, zipcode, address, detailaddr)
values('tester2',1,2,'소프트민트','M',1,38000,'credit','유저2','01098765432','54321','부산 남구 문현로 56-1 (네이버코리아)','5층 502호');
insert into ordertable(id, productno, orderno, selectedcolor, selectedsize, amount, totalprice, ordermethod, name, tel, zipcode, address, detailaddr)
values('tester2',2,2,'피치베이지',null,2,59800,'credit','유저2','01045614561','24241','부산 문현로 56-1 (네이버코리아)','4층 405호');
-- 포인트 내역
insert into pointtable(id, point) values ('tester',-2000);
insert into pointtable(id, point) values ('tester',500);
-- 공지사항
insert into noticetable(title, content, id, image) values("test1", "content1", "admin1", "test1.png");
insert into noticetable(title, content, id, image) values("test2", "content2", "admin2", "test2.png");
insert into noticetable(title, content, id, image) values("test3", "content3", "admin3", "test3.png");
insert into noticetable(title, content, id, image) values("test1", "content1", "admin4", "test1.png");
insert into noticetable(title, content, id, image) values("test2", "content2", "admin5", "test2.png");
insert into noticetable(title, content, id, image) values("test3", "content3", "admin6", "test3.png");
insert into noticetable(title, content, id, image) values("test1", "content1", "admin7", "test1.png");
insert into noticetable(title, content, id, image) values("test2", "content2", "admin8", "test2.png");
insert into noticetable(title, content, id, image) values("test3", "content3", "admin9", "test3.png");
insert into noticetable(title, content, id, image) values("test1", "content1", "admin10", "test1.png");
insert into noticetable(title, content, id, image) values("test2", "content2", "admin11", "test2.png");
insert into noticetable(title, content, id, image) values("test3", "content3", "admin12", "test3.png");
insert into noticetable(title, content, id, image) values("test1", "content1", "admin13", "test1.png");
insert into noticetable(title, content, id, image) values("test2", "content2", "admin14", "test2.png");
insert into noticetable(title, content, id, image) values("test3", "content3", "admin15", "test3.png");
insert into noticetable(title, content, id, image) values("test1", "content1", "admin16", "test1.png");
insert into noticetable(title, content, id, image) values("test2", "content2", "admin17", "test2.png");
insert into noticetable(title, content, id, image) values("test3", "content3", "admin18", "test3.png");
insert into noticetable(title, content, id, image) values("test1", "content1", "admin19", "test1.png");
insert into noticetable(title, content, id, image) values("test2", "content2", "admin20", "test2.png");
insert into noticetable(title, content, id, image) values("test3", "content3", "admin21", "test3.png");
insert into noticetable(title, content, id, image) values("test1", "content1", "admin22", "test1.png");
insert into noticetable(title, content, id, image) values("test2", "content2", "admin23", "test2.png");
insert into noticetable(title, content, id, image) values("test3", "content3", "admin24", "test3.png");
insert into noticetable(title, content, id, image) values("test1", "content1", "admin25", "test1.png");
insert into noticetable(title, content, id, image) values("test2", "content2", "admin26", "test2.png");
insert into noticetable(title, content, id, image) values("test3", "content3", "admin27", "test3.png");
insert into noticetable(title, content, id, image) values("test1", "content1", "admin28", "test1.png");
insert into noticetable(title, content, id, image) values("test2", "content2", "admin29", "test2.png");
insert into noticetable(title, content, id, image) values("test3", "content3", "admin30", "test3.png");
insert into noticetable(title, content, id, image) values("test3", "content3", "admin31", "test3.png");
insert into noticetable(title, content, id, image) values("test3", "content3", "admin31", "test3.png");
-- 자주 묻는 질문
insert into faqtable(type, title, content) values('product', '자주 묻는 질문 제목입니다', '자주 묻는 질문 내용입니다');
insert into faqtable(type, title, content) values('delivery', '자주 묻는 질문 제목입니다2', '자주 묻는 질문 내용입니다2');
insert into faqtable(type, title, content) values('return', '자주 묻는 질문 제목입니다3', '자주 묻는 질문 내용입니다3');
insert into faqtable(type, title, content) values('etc', '자주 묻는 질문 제목입니다4', '자주 묻는 질문 내용입니다4');
-- 후기
insert into reviewtable(productno, content, id, image, star) values(1,'후기내용1','tester', 'image1.jpg','5');
insert into reviewtable(productno, content, id, image, star) values(1,'후기내용2','tester2', 'image2.jpg','4');
insert into reviewtable(productno, content, id, image, star) values(2,'후기내용3','tester2', 'image3.jpg','3');
insert into reviewtable(productno, content, id, image, star) values(1,'후기내용4','tester', 'image4.jpg','2');
insert into reviewtable(productno, content, id, image, star) values(2,'후기내용5','tester', 'image5.jpg','1');
-- 문의
insert into qnatable(productno,type, originalNo, reply, content, id, secret, image) values(1,'product', 1, true, '질문 내용', 'tester', true, 'image1.jpg');
insert into qnatable(productno,type, originalNo, reply, content, id, secret, image) values(2,'product', last_insert_id()+1, false, '질문 내용', 'tester',true, 'image1.jpg');
insert into qnatable(type, originalNo, content, id, secret, image) values('productReply', 1, '답변 내용', 'admin',true, 'image1.jpg');
insert into qnatable(type, originalNo, reply, content, id, secret, image) values('general', last_insert_id()+1, false, '질문 내용', 'tester',true, 'image1.jpg');
insert into qnatable(productno,type, originalNo, reply, content, id, secret, image) values(1,'product', last_insert_id()+1, true, '질문 내용', 'tester',true, 'image1.jpg');
insert into qnatable(productno,type, originalNo, reply, content, id, secret, image) values(2,'product', last_insert_id()+1, true, '질문 내용', 'tester2',true, 'image1.jpg');
insert into qnatable(type, originalNo, content, id, secret, image) values('productReply', 5, '답변 내용', 'admin',true, 'image1.jpg');
insert into qnatable(type, originalNo, content, id, secret, image) values('productReply', 6, '답변 내용', 'admin',true, 'image1.jpg');
insert into qnatable(type, originalNo, reply, content, id, secret, image) values('general', last_insert_id()+1, false, '질문 내용', 'tester',true, 'image1.jpg');
insert into qnatable(productno,type, originalNo, reply, content, id, secret, image) values(2,'product', last_insert_id()+1, false, '질문 내용', 'tester2',true, 'image1.jpg');
insert into qnatable(type, originalNo, reply, content, id, secret, image) values('delivery', last_insert_id()+1, false, '질문 내용', 'tester2',true, 'image1.jpg');
insert into qnatable(type, originalNo, reply, content, id, secret, image) values('cancel', last_insert_id()+1, false, '질문 내용', 'tester2',true, 'image1.jpg');
insert into qnatable(type, originalNo, reply, content, id, secret, image) values('exchange', last_insert_id()+1, false, '질문 내용', 'tester2',true, 'image1.jpg');
-- 배너
insert into bannertable(image, link) values('test1.jpg','testlin1k');
insert into bannertable(image, link) values('test2.jpg','testlink2');

commit;

-- select data
select * from membertable;
select * from producttable;
select * from baskettable;
select * from wishlisttable;
select * from ordertable;
select * from pointtable;
select * from noticetable;
select * from faqtable;
select * from reviewtable;
select * from qnatable;
select * from bannertable;

-- select문 실험 및 용도

-- 글 번호는 최신순이지만 답글이 원글 밑에 오도록 함
-- select * from qnatable order by originalno desc, qnano asc;