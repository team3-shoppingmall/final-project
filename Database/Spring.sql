drop database if exists springdb;
create database springdb;
use springdb;

-- key컬럼이 아닌 컬럼으로 update하기 위함
set sql_safe_updates=0;

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

-- drop trigger, procedure, event
DROP TRIGGER IF EXISTS `springdb`.`membertable_BEFORE_INSERT`;
DROP TRIGGER IF EXISTS `springdb`.`ordertable_BEFORE_INSERT`;
DROP TRIGGER IF EXISTS `springdb`.`pointtable_BEFORE_INSERT`;
DROP TRIGGER IF EXISTS `springdb`.`membertable_AFTER_DELETE`;
DROP procedure IF EXISTS `autoQuestion`;
DROP procedure IF EXISTS `autoReply`;
DROP procedure IF EXISTS `orderChangeSchedule`;
DROP procedure IF EXISTS `bannerInsert`;
DROP procedure IF EXISTS `bannerUpdate`;
DROP procedure IF EXISTS `bannerDelete`;
DROP EVENT IF EXISTS `event_AutoScheduler`;

-- create table and trigger
CREATE TABLE membertable (
	ID VARCHAR(70) PRIMARY KEY,
	PASSWORD VARCHAR(256) NOT NULL,
	NAME VARCHAR(50) NOT NULL,
	TEL VARCHAR(11) NOT NULL,
	EMAIL VARCHAR(50) NOT NULL,
	ZIPCODE VARCHAR(10) NOT NULL,
	ADDR1 VARCHAR(80) NOT NULL,
	ADDR2 VARCHAR(50) NOT NULL,
	TERMS BOOLEAN NOT NULL,
	POINT INT,
	AUTHORITY VARCHAR(20) NOT NULL
);

DELIMITER $$
USE `springdb`$$
CREATE DEFINER = CURRENT_USER TRIGGER `springdb`.`membertable_BEFORE_INSERT` BEFORE INSERT ON `membertable` FOR EACH ROW
BEGIN
set new.point = 2000;
insert into pointtable(id, point, content) values (new.id, 2000, '회원 가입 축하 포인트');
END$$
DELIMITER ;

DELIMITER $$
USE `springdb`$$
CREATE DEFINER = CURRENT_USER TRIGGER `springdb`.`membertable_AFTER_DELETE` AFTER DELETE ON `membertable` FOR EACH ROW
BEGIN
delete from qnatable where originalNo in (select * from (select qnaNo from qnatable where id = old.id) temp);
delete from pointtable where id = old.id;
END$$
DELIMITER ;

CREATE TABLE producttable(
	PRODUCTNO INT PRIMARY KEY AUTO_INCREMENT,
	PRODUCTNAME VARCHAR(100) NOT NULL,
	TYPE1 VARCHAR(50) NOT NULL,
	TYPE2 VARCHAR(50) NOT NULL,
	IMAGENAME VARCHAR(400) NOT NULL,
	PRICE INT NOT NULL,
	DISCOUNT INT DEFAULT 0,
	COLOR VARCHAR(1000),
	SIZE VARCHAR(1000),
	AMOUNT INT NOT NULL,
	REGDATE TIMESTAMP DEFAULT current_timestamp,
	DETAILIMAGENAME VARCHAR(2000) NOT NULL,
	ONSALE BOOLEAN DEFAULT FALSE
);

CREATE TABLE baskettable (
	BASKETIDX BIGINT PRIMARY KEY AUTO_INCREMENT,
	ID VARCHAR(50) NOT NULL,
	PRODUCTNO INT NOT NULL,
	SELECTEDCOLOR VARCHAR(50),
	SELECTEDSIZE VARCHAR(50),
	BASKETAMOUNT INT NOT NULL,
    CONSTRAINT basket_fk_id FOREIGN KEY (ID)
        REFERENCES membertable (ID)
        on delete cascade
        on update cascade,
    CONSTRAINT basket_fk_productno FOREIGN KEY (PRODUCTNO)
        REFERENCES producttable (PRODUCTNO)
        on delete cascade
        on update cascade
);

CREATE TABLE wishlisttable (
	ID VARCHAR(50) NOT NULL,
	PRODUCTNO INT NOT NULL,
    CONSTRAINT primary_wishlist PRIMARY KEY (ID, PRODUCTNO),
    CONSTRAINT wishList_fk_id FOREIGN KEY (ID)
        REFERENCES membertable (ID)
        on delete cascade
        on update cascade,
    CONSTRAINT wishList_fk_productno FOREIGN KEY (PRODUCTNO)
        REFERENCES producttable (PRODUCTNO)
        on delete cascade
        on update cascade
);

CREATE TABLE ordertable (
	ORDERIDX BIGINT PRIMARY KEY AUTO_INCREMENT,
	ID VARCHAR(50) NOT NULL,
	PRODUCTNO INT NOT NULL,
	ORDERNO BIGINT,
	SELECTEDCOLOR VARCHAR(100),
	SELECTEDSIZE VARCHAR(100),
	ORDERAMOUNT INT NOT NULL,
	TOTALPRICE INT NOT NULL,
	ORDERDATE TIMESTAMP DEFAULT (current_timestamp),
	STATE VARCHAR(20),
	ORDERMETHOD VARCHAR(100) NOT NULL,
	NAME VARCHAR(50) NOT NULL,
	TEL VARCHAR(11) NOT NULL,
	ZIPCODE VARCHAR(5) NOT NULL,
	ADDRESS VARCHAR(200) NOT NULL,
	DETAILADDR VARCHAR(50) NOT NULL,
    MESSAGE VARCHAR(50),
    CONSTRAINT order_fk_productno FOREIGN KEY (PRODUCTNO) REFERENCES producttable (PRODUCTNO)
);

DELIMITER $$
USE `springdb`$$
CREATE DEFINER = CURRENT_USER TRIGGER `springdb`.`ordertable_BEFORE_INSERT` BEFORE INSERT ON `ordertable` FOR EACH ROW
BEGIN
if(new.state = null)
then
if(new.ORDERMETHOD = 'cash')
then
	set new.state = '입금전';
else
	set new.state = '결제완료';
END if;
END if;
END$$
DELIMITER ;

DELIMITER $$
USE `springdb`$$
CREATE PROCEDURE `orderChangeSchedule` ()
BEGIN
IF( DAYOFWEEK(curdate()) between 2 and 6)
THEN
UPDATE ordertable set state = '배송준비중' where state = '결제완료';
END IF;
END$$
DELIMITER ;

create EVENT event_AutoScheduler
ON schedule 
EVERY 1 day starts '2022-03-06 15:00:00'
COMMENT '매일 15:00 결제완료 -> 배송준비중으로 변경'
DO Call orderChangeSchedule();

CREATE TABLE pointtable (
	NUM BIGINT PRIMARY KEY AUTO_INCREMENT,
	ID VARCHAR(50) NOT NULL,
	POINT INT NOT NULL,
	POINTDATE TIMESTAMP DEFAULT (current_timestamp),
    CONTENT VARCHAR(50)
);

DELIMITER $$
USE `springdb`$$
CREATE DEFINER = CURRENT_USER TRIGGER `springdb`.`pointtable_BEFORE_INSERT` BEFORE INSERT ON `pointtable` FOR EACH ROW
BEGIN
if(new.content != '회원 가입 축하 포인트')
Then
update membertable
set point = point + New.point
where id = New.id;
End if;
END$$
DELIMITER ;

CREATE TABLE noticetable (
	NOTICENO INT PRIMARY KEY AUTO_INCREMENT,
	TITLE VARCHAR(100) NOT NULL,
	CONTENT VARCHAR(10000) NOT NULL,
	ID VARCHAR(50) NOT NULL,
	IMAGE VARCHAR(400),
    CONSTRAINT notice_fk_id FOREIGN KEY (ID) REFERENCES membertable (ID)
        on delete cascade
        on update cascade
);

CREATE TABLE faqtable (
	FAQNO INT PRIMARY KEY AUTO_INCREMENT,
	TYPE VARCHAR(50) NOT NULL,
	TITLE VARCHAR(100) NOT NULL,
	CONTENT VARCHAR(2000) NOT NULL
);

CREATE TABLE reviewTable(
	REVIEWNO BIGINT PRIMARY KEY AUTO_INCREMENT,
	PRODUCTNO INT not NULL,
	CONTENT VARCHAR(600) NOT NULL,
	id VARCHAR(50) NOT NULL,
	REGDATE TIMESTAMP DEFAULT (current_timestamp),
	IMAGE VARCHAR(400),
	STAR INT NOT NULL,
    CONSTRAINT review_fk_id FOREIGN KEY (ID) REFERENCES membertable (ID)
        on delete cascade
        on update cascade,
	CONSTRAINT review_fk_productno FOREIGN KEY (PRODUCTNO) REFERENCES producttable (PRODUCTNO)
        on delete cascade
        on update cascade
);

CREATE TABLE qnatable(
	QNANO BIGINT PRIMARY KEY auto_increment,
	PRODUCTNO INT,
	TYPE VARCHAR(200) NOT NULL,
	ORIGINALNO BIGINT,
	REPLY BOOLEAN DEFAULT FALSE,
	CONTENT VARCHAR(2000) NOT NULL,
	ID VARCHAR(50) NOT NULL,
	REGDATE TIMESTAMP DEFAULT (current_timestamp),
	SECRET BOOLEAN NOT NULL,
	IMAGE VARCHAR(400)
);

DELIMITER $$
USE `springdb`$$
CREATE PROCEDURE `autoQuestion` (qna_productNo INT, qna_type VARCHAR(200), qna_reply BOOLEAN, qna_content VARCHAR(2000), qna_id VARCHAR(50), qna_secret BOOLEAN, qna_image VARCHAR(400))
BEGIN
DECLARE getMaxQnaNo BIGINT;
SET getMaxQnaNo = (SELECT max(qnaNo) FROM qnatable) + 1;
insert into qnatable(QNANO, PRODUCTNO, type, originalNo, reply, content, id, secret, image) values(getMaxQnaNo, qna_productNo, qna_type, getMaxQnaNo, qna_reply, qna_content, qna_id, qna_secret, qna_image);
END$$
DELIMITER ;

DELIMITER $$
USE `springdb`$$
CREATE PROCEDURE `autoReply` (qna_productNo INT, qna_type VARCHAR(200), qna_originalNo BIGINT, qna_content VARCHAR(2000), qna_id VARCHAR(50), qna_secret BOOLEAN, qna_image VARCHAR(400))
BEGIN
DECLARE getMaxQnaNo BIGINT;
SET getMaxQnaNo = (SELECT max(qnaNo) FROM qnatable) + 1;
insert into qnatable(QNANO, PRODUCTNO, type, originalNo, content, id, secret, image) values(getMaxQnaNo, qna_productNo, qna_type, qna_originalNo, qna_content, qna_id, qna_secret, qna_image);
END$$
DELIMITER ;

CREATE TABLE bannertable (
	IMAGE VARCHAR(100) PRIMARY KEY,
	LINK VARCHAR(100) NOT NULL,
	NUM INT
);

DELIMITER $$
USE `springdb`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `bannerInsert`(imageTemp varchar(100), linkTemp varchar(100), numTemp int)
BEGIN
DECLARE getMaxNum INT;
if(numTemp is null)
then

SET getMaxNum = (SELECT max(num) FROM bannertable) + 1;
insert into bannertable values(imageTemp, linkTemp, getMaxNum);

else

if((select image from bannertable where num = numTemp) is not null)
then
update bannertable set num = num+1 where num in (select * from (select num from bannertable where num >= numTemp) temp);
end if;
insert into bannertable values(imageTemp, linkTemp, numTemp);

end if;
END$$
DELIMITER ;

DELIMITER $$
USE `springdb`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `bannerUpdate`(beforeImage varchar(100), imageTemp varchar(100), linkTemp varchar(100), numTemp int)
BEGIN
DECLARE endTemp INT;
set endTemp = (select num from bannertable where image = beforeImage);
if(endTemp = numTemp)
then
update bannertable set image = imageTemp, link = linkTemp where num = numTemp;
else

update bannertable set num = 0 where image = beforeImage;

if((select image from bannertable where num = numTemp) is not null)
then
update bannertable set num = num+1 where num in (select * from (select num from bannertable where num >= numTemp and num <= endTemp) temp);
end if;

update bannertable set image = imageTemp, link = linkTemp, num = numTemp where num = 0;
end if;
END$$
DELIMITER ;

DELIMITER $$
USE `springdb`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `bannerDelete`(imageTemp varchar(100))
BEGIN
DECLARE numTemp INT;
set numTemp = (select num from bannertable where image = imageTemp);

if((select image from bannertable where num = numTemp) is not null)
then
update bannertable set num = num-1 where num in (select * from (select num from bannertable where num >= numTemp) temp);
end if;

delete from bannertable where image = imageTemp;
END$$
DELIMITER ;

-- insert data
-- 회원
insert into membertable values('spring','$2a$10$V63Xuxy9M9oOOMFwQ03L5uA2yaaFoOXMe54bJmBLul0JdeMR4lm/S','Spring','0212345678','spring@gmail.com','12345','서울 강남구 테헤란로 212 (멀티캠퍼스)','2층 201호',false,null,'ROLE_ADMIN');
insert into membertable values('tester','$2a$10$V63Xuxy9M9oOOMFwQ03L5uA2yaaFoOXMe54bJmBLul0JdeMR4lm/S','유저','01098765432','tester@gmail.com','54321','부산 남구 문현로 56-1 (네이버코리아)','5층 502호',false,null,'ROLE_USER');
insert into membertable values('portal','$2a$10$V63Xuxy9M9oOOMFwQ03L5uA2yaaFoOXMe54bJmBLul0JdeMR4lm/S','강기룡','01077777777','portal@gmail.com','02000','부산 문현로 56-1 (네이버코리아)','4층 405호',false,null,'ROLE_USER');
insert into membertable values('aodremm','$2a$10$V63Xuxy9M9oOOMFwQ03L5uA2yaaFoOXMe54bJmBLul0JdeMR4lm/S','안동근','01011111111','aodremm@gmail.com','11111','부산 문현로 56-1 (네이버코리아)','4층 405호',false,null,'ROLE_USER');
insert into membertable values('grimhink','$2a$10$V63Xuxy9M9oOOMFwQ03L5uA2yaaFoOXMe54bJmBLul0JdeMR4lm/S','정호재','01022222222','grimhink@gmail.com','22222','부산 문현로 56-1 (네이버코리아)','4층 405호',true,null,'ROLE_USER');
insert into membertable values('madana','$2a$10$V63Xuxy9M9oOOMFwQ03L5uA2yaaFoOXMe54bJmBLul0JdeMR4lm/S','서기준','01033333333','madana@gmail.com','33333','부산 문현로 56-1 (네이버코리아)','4층 405호',false,null,'ROLE_USER');
insert into membertable values('prose','$2a$10$V63Xuxy9M9oOOMFwQ03L5uA2yaaFoOXMe54bJmBLul0JdeMR4lm/S','남범석','01044444444','prose@gmail.com','44444','부산 문현로 56-1 (네이버코리아)','4층 405호',false,null,'ROLE_USER');
insert into membertable values('rhonia','$2a$10$V63Xuxy9M9oOOMFwQ03L5uA2yaaFoOXMe54bJmBLul0JdeMR4lm/S','조철준','01055555555','rhonia@gmail.com','55555','부산 문현로 56-1 (네이버코리아)','4층 405호',false,null,'ROLE_USER');
insert into membertable values('rhonnyn','$2a$10$V63Xuxy9M9oOOMFwQ03L5uA2yaaFoOXMe54bJmBLul0JdeMR4lm/S','박기하','01066666666','rhonnyn@gmail.com','66666','부산 문현로 56-1 (네이버코리아)','4층 405호',false,null,'ROLE_USER');
insert into membertable values('seaxwell','$2a$10$V63Xuxy9M9oOOMFwQ03L5uA2yaaFoOXMe54bJmBLul0JdeMR4lm/S','정진숙','01077777777','seaxwell@gmail.com','77777','부산 문현로 56-1 (네이버코리아)','4층 405호',true,null,'ROLE_USER');
insert into membertable values('talwyn','$2a$10$V63Xuxy9M9oOOMFwQ03L5uA2yaaFoOXMe54bJmBLul0JdeMR4lm/S','박동현','01088888888','talwyn@gmail.com','88888','부산 문현로 56-1 (네이버코리아)','4층 405호',false,null,'ROLE_USER');
insert into membertable values('trice','$2a$10$V63Xuxy9M9oOOMFwQ03L5uA2yaaFoOXMe54bJmBLul0JdeMR4lm/S','성재용','01099999999','trice@gmail.com','99999','부산 문현로 56-1 (네이버코리아)','4층 405호',false,null,'ROLE_USER');
insert into membertable values('wantin','$2a$10$V63Xuxy9M9oOOMFwQ03L5uA2yaaFoOXMe54bJmBLul0JdeMR4lm/S','한명옥','01000000000','wantin@gmail.com','00000','부산 문현로 56-1 (네이버코리아)','4층 405호',false,null,'ROLE_USER');
-- 상품
insert into producttable(productname, type1, type2, imagename, price, color, size, amount, regDate, detailimagename, onSale) 
values('스노우 버튼 모직스커트', 'skirt','mini','image3.jpg;lake-6641880__340.webp;lake-6701636__340.jpg;water-6706894__340.webp',38000,'그레이지;소프트민트','S;M;L', 100, '2022-01-01 09:00:00', 'photo-1433086966358-54859d0ed716.jfif;milky-way-2695569__340.jpg;photo-1447752875215-b2761acb3c5d.jfif;photo-1469474968028-56623f02e42e.jfif', true);
insert into producttable(productname, type1, type2, imagename, price, discount, color, size, amount, regDate, detailimagename, onSale)
values('실키 여리핏 히든블라우스', 'top','blouse','photo-1465146344425-f00d5f5c8f07.jfif;photo-1426604966848-d7adac402bff.jfif',34900,5000,'아이보리;피치베이지;워터리블루;블랙',null, 100,'2022-01-02 10:00:00', 'photo-1475924156734-496f6cac6ec1.jfif;photo-1586348943529-beaae6c28db9.jfif', true);
insert into producttable(productname, type1, type2, imagename, price, discount, color, size, amount, regDate,  detailimagename, onSale)
values('비프리 자켓', 'outer','jacket','tourist-attraction-7037967__340.jpg',92000,0,'아이보리;블랙',null, 100,'2022-01-03 13:24:51','tree-6792528__340.jpg', true);
insert into producttable(productname, type1, type2, imagename, price, discount, color, size, amount, regDate,  detailimagename, onSale)
values('루린 코튼 핀턱 숏 팬츠', 'pants','shorts','field-7025238__340.webp',31000,0,'아이보리;크림;베이지;블랙','S;M;L', 100,'2022-01-04 13:24:51','storm-7018311__340.webp', true);
insert into producttable(productname, type1, type2, imagename, price, discount, color, size, amount, regDate,  detailimagename, onSale)
values('크롭 투웨이 후드 집업', 'outer','jumper','forest-7023487__340.jpg',32000,3200,'아이보리;오트밀;메란지;그레이;블랙',null, 100,'2022-01-05 13:24:51','bird-7016926__340.jpg', true);
insert into producttable(productname, type1, type2, imagename, price, discount, color, size, amount, regDate,  detailimagename, onSale)
values('더윈도우 티', 'top','tshirts','feather-7009025__340.jpg',31000,0,'아이보리;베이지;네이비',null, 100,'2022-01-06 13:24:51','school-6982073__340.jpg', true);
insert into producttable(productname, type1, type2, imagename, price, discount, color, size, amount, regDate,  detailimagename, onSale)
values('하우드 코튼 팬츠', 'pants','cotton','tree-6835828__340.jpg',41000,0,'아이보리','S;M;L', 100,'2022-01-07 13:24:51','roys-peak-7008528__340.jpg', true);
insert into producttable(productname, type1, type2, imagename, price, discount, color, size, amount, regDate,  detailimagename, onSale)
values('포메른 싱글 트렌치 코트', 'outer','coat','landscape-7043420__340.jpg',115000,0,'크림;베이지;소라',null, 100,'2022-01-08 13:24:51','animal-7027635__340.webp', true);
insert into producttable(productname, type1, type2, imagename, price, discount, color, size, amount, regDate,  detailimagename, onSale)
values('슬리너 롱 스커트', 'skirt','midi-long','peace-7041597__340.webp',44000,0,'아이보리;블랙',null, 100,'2022-01-09 13:24:51','northern-lights-6862969__340.webp', true);
insert into producttable(productname, type1, type2, imagename, price, discount, color, size, amount, regDate,  detailimagename, onSale)
values('아델르 핀턱 퍼프 원피스', 'dress','ops','bird-6983434__340.webp',49000,0,'블랙','S;M;L', 100,'2022-01-10 13:24:51','tree-blossoms-7022041__340.webp', true);
insert into producttable(productname, type1, type2, imagename, price, discount, color, size, amount, regDate,  detailimagename, onSale)
values('코디스 골지 가디건', 'outer','cardigan','altai-6943982__340.jpg',17000,0,'아이보리;옐로우;소라;퍼플;메란지;블랙',null, 100,'2022-01-11 13:24:51','passiflora-7027917__340.webp', true);
insert into producttable(productname, type1, type2, imagename, price, discount, color, size, amount, regDate,  detailimagename, onSale)
values('데저트 크롭 맨투맨', 'top','mtm','travel-7014427__340.jpg',26000,2000,'블루',null, 100,'2022-01-12 13:24:51','chicago-6921297__340.webp', true);
insert into producttable(productname, type1, type2, imagename, price, discount, color, size, amount, regDate,  detailimagename, onSale)
values('라이트 롤업 점프수트', 'dress','jumpSuit','road-6881040__340.webp',75000,0,'아이보리;핑크;카키','숏;롱', 100,'2022-01-13 13:24:51','sunset-6911736__340.webp', true);
insert into producttable(productname, type1, type2, imagename, price, discount, color, size, amount, regDate,  detailimagename, onSale)
values('디셈버 핀턱 와이드 슬랙스', 'pants','slacks','monk-7010969__340.webp',37000,3700,'아이보리;핑크;챠콜;블랙','XS;S;M', 100,'2022-01-14 13:24:51','wine-6688901__340.webp', true);
insert into producttable(productname, type1, type2, imagename, price, discount, color, size, amount, regDate,  detailimagename, onSale)
values('카엘 스트라이프 셔츠', 'top','shirt','lantern-6826687__340.webp',43000,3400,'아이보리;베이지;네이비',null, 100,'2022-01-15 13:24:51','window-7001026__340.webp', true);
insert into producttable(productname, type1, type2, imagename, price, discount, color, size, amount, regDate,  detailimagename, onSale)
values('릴리브 골지 슬리브리스', 'top','sleeveless','nature-6842159__340.jpg',13000,0,'아이보리;베이지;핑크;카키;브라운;블랙',null, 100,'2022-01-16 13:24:51','year-6786741__340.webp', true);
insert into producttable(productname, type1, type2, imagename, price, discount, color, size, amount, regDate,  detailimagename, onSale)
values('비파인 꽈배기 브이 크롭 니트', 'top','knit','house-6967908__340.jpg',36000,2800,'베이지;소라;블랙',null, 100,'2022-01-17 13:24:51','sea-6948569__340.webp', true);
insert into producttable(productname, type1, type2, imagename, price, discount, color, size, amount, regDate,  detailimagename, onSale)
values('조이쉬 피그먼트 와이드 데님', 'pants','denim','port-6587129__340.webp',46000,0,'베이지;머스타드;레드;퍼플;그레이','S;M;L', 100,'2022-01-18 13:24:51','road-6935773__340.jpg', true);
insert into producttable(productname, type1, type2, imagename, price, discount, color, size, amount, regDate,  detailimagename, onSale)
values('블렛 레더 스커트', 'skirt','mini','water-6706894__340.webp',31000,0,'아이보리;모카브라운;블랙','S;M', 100,'2022-01-19 13:24:51','photo-1433086966358-54859d0ed716.jfif', true);
insert into producttable(productname, type1, type2, imagename, price, discount, color, size, amount, regDate,  detailimagename, onSale)
values('워린 카라 퍼프 블라우스', 'top','blouse','photo-1465146344425-f00d5f5c8f07.jfif',44000,0,'아이보리;크림;네이비',null, 100,'2022-01-20 13:24:51','photo-1475924156734-496f6cac6ec1.jfif', true);
insert into producttable(productname, type1, type2, imagename, price, discount, color, size, amount, regDate,  detailimagename, onSale)
values('리턴드 자켓', 'outer','jacket','tourist-attraction-7037967__340.jpg',79000,0,'베이지;소라;카키;블랙',null, 100,'2022-01-21 13:24:51','tree-6792528__340.jpg', true);
insert into producttable(productname, type1, type2, imagename, price, discount, color, size, amount, regDate,  detailimagename, onSale)
values('프리미엄 바이커 숏츠', 'pants','shorts','field-7025238__340.webp',22000,0,'블랙',null, 100,'2022-01-22 13:24:51','storm-7018311__340.webp', true);
insert into producttable(productname, type1, type2, imagename, price, discount, color, size, amount, regDate,  detailimagename, onSale)
values('카라 투웨이 집업', 'outer','jumper','forest-7023487__340.jpg',32000,0,'아이보리;오트밀;메란지;그레이;블랙',null, 100,'2022-01-23 13:24:51','bird-7016926__340.jpg', true);
insert into producttable(productname, type1, type2, imagename, price, discount, color, size, amount, regDate,  detailimagename, onSale)
values('쉘리 유넥 티', 'top','tshirts','feather-7009025__340.jpg',15000,0,'아이보리;크림;메란지;네이비;블랙',null, 100,'2022-01-24 13:24:51','school-6982073__340.jpg', true);
insert into producttable(productname, type1, type2, imagename, price, discount, color, size, amount, regDate,  detailimagename, onSale)
values('멀드 코튼 팬츠', 'pants','cotton','tree-6835828__340.jpg',43000,0,'아이보리;그린;블랙',null, 100,'2022-01-25 13:24:51','roys-peak-7008528__340.jpg', true);
insert into producttable(productname, type1, type2, imagename, price, discount, color, size, amount, regDate,  detailimagename, onSale)
values('너트번 레더 롱 코트', 'outer','coat','landscape-7043420__340.jpg',129000,0,'베이지;블랙',null, 100,'2022-01-26 13:24:51','animal-7027635__340.webp', true);
insert into producttable(productname, type1, type2, imagename, price, discount, color, size, amount, regDate,  detailimagename, onSale)
values('라몬드 미디 스커트', 'skirt','midi-long','peace-7041597__340.webp',32000,0,'옐로우;네이비','S;M', 100,'2022-01-27 13:24:51','northern-lights-6862969__340.webp', true);
insert into producttable(productname, type1, type2, imagename, price, discount, color, size, amount, regDate,  detailimagename, onSale)
values('폴아웃 롱 원피스', 'dress','ops','bird-6983434__340.webp',66000,0,'크림;베이지',null, 100,'2022-01-28 13:24:51','tree-blossoms-7022041__340.webp', true);
insert into producttable(productname, type1, type2, imagename, price, discount, color, size, amount, regDate,  detailimagename, onSale)
values('노우즈 라운드 가디건', 'outer','cardigan','altai-6943982__340.jpg',48000,0,'크림;레드;블루;네이비',null, 100,'2022-01-29 13:24:51','passiflora-7027917__340.webp', true);
insert into producttable(productname, type1, type2, imagename, price, discount, color, size, amount, regDate,  detailimagename, onSale)
values('트리플캣 맨투맨', 'top','mtm','travel-7014427__340.jpg',27000,2100,'옐로우;메란지;챠콜',null, 100,'2022-01-30 13:24:51','chicago-6921297__340.webp', true);
insert into producttable(productname, type1, type2, imagename, price, discount, color, size, amount, regDate,  detailimagename, onSale)
values('튤립 점프수트', 'dress','jumpSuit','road-6881040__340.webp',59000,0,'베이지;네이비',null, 100,'2022-01-31 13:24:51','sunset-6911736__340.webp', true);
insert into producttable(productname, type1, type2, imagename, price, discount, color, size, amount, regDate,  detailimagename, onSale)
values('포드너 핀턱 하프 슬랙스', 'pants','slacks','monk-7010969__340.webp',46000,0,'베이지;네이비','S;M', 100,'2022-02-01 13:24:51','wine-6688901__340.webp', true);
insert into producttable(productname, type1, type2, imagename, price, discount, color, size, amount, regDate,  detailimagename, onSale)
values('로밍 카라 셔츠', 'top','shirt','lantern-6826687__340.webp',36000,2800,'아이보리;베이지;그린;소라',null, 100,'2022-02-02 13:24:51','window-7001026__340.webp', true);
insert into producttable(productname, type1, type2, imagename, price, discount, color, size, amount, regDate,  detailimagename, onSale)
values('페어링 크롭 슬리브리스', 'top','sleeveless','nature-6842159__340.jpg',16000,0,'아이보리;블랙',null, 100,'2022-02-03 13:24:51','year-6786741__340.webp', true);
insert into producttable(productname, type1, type2, imagename, price, discount, color, size, amount, regDate,  detailimagename, onSale)
values('로빌 울 백 스트랩 니트', 'top','knit','house-6967908__340.jpg',54000,4300,'베이지;카키;블랙',null, 100,'2022-02-04 13:24:51','sea-6948569__340.webp', true);
insert into producttable(productname, type1, type2, imagename, price, discount, color, size, amount, regDate,  detailimagename, onSale)
values('파코 데님', 'pants','denim','port-6587129__340.webp',31000,0,'연청;중청;진청','S;M;L', 100,'2022-02-05 13:24:51','road-6935773__340.jpg', true);
insert into producttable(productname, type1, type2, imagename, price, discount, color, size, amount, regDate,  detailimagename, onSale)
values('오에스트 핀턱 미니 스커트', 'skirt','mini','water-6706894__340.webp',43000,3400,'베이지;차콜',null, 100,'2022-02-06 13:24:51','photo-1433086966358-54859d0ed716.jfif', true);
insert into producttable(productname, type1, type2, imagename, price, discount, color, size, amount, regDate,  detailimagename, onSale)
values('비마인 레이스 블라우스', 'top','blouse','photo-1465146344425-f00d5f5c8f07.jfif',42000,0,'아이보리;퍼플',null, 100,'2022-02-07 13:24:51','photo-1475924156734-496f6cac6ec1.jfif', true);
insert into producttable(productname, type1, type2, imagename, price, discount, color, size, amount, regDate,  detailimagename, onSale)
values('뉘앙스 카라 트위드 자켓', 'outer','jacket','tourist-attraction-7037967__340.jpg',125000,0,'아이보리;블랙',null, 100,'2022-02-08 13:24:51','tree-6792528__340.jpg', true);
insert into producttable(productname, type1, type2, imagename, price, discount, color, size, amount, regDate,  detailimagename, onSale)
values('에이라인 데님 숏츠', 'pants','shorts','field-7025238__340.webp',29000,0,'중청','XS;S;M;L', 100,'2022-02-09 13:24:51','storm-7018311__340.webp', true);
insert into producttable(productname, type1, type2, imagename, price, discount, color, size, amount, regDate,  detailimagename, onSale)
values('루니드 야상 점퍼', 'outer','jumper','forest-7023487__340.jpg',66000,0,'베이지',null, 100,'2022-02-10 13:24:51','bird-7016926__340.jpg', true);
insert into producttable(productname, type1, type2, imagename, price, discount, color, size, amount, regDate,  detailimagename, onSale)
values('티유 퍼프 티', 'top','tshirts','feather-7009025__340.jpg',19000,0,'아이보리;핑크;블랙',null, 100,'2022-02-11 13:24:51','school-6982073__340.jpg', true);
insert into producttable(productname, type1, type2, imagename, price, discount, color, size, amount, regDate,  detailimagename, onSale)
values('크롬 코튼 와이드 팬츠', 'pants','cotton','tree-6835828__340.jpg',28000,0,'아이보리','S;M;L', 100,'2022-02-12 13:24:51','roys-peak-7008528__340.jpg', true);
insert into producttable(productname, type1, type2, imagename, price, discount, color, size, amount, regDate,  detailimagename, onSale)
values('데아르 울 더블 롱 코트', 'outer','coat','landscape-7043420__340.jpg',184000,0,'카멜;네이비',null, 100,'2022-02-13 13:24:51','animal-7027635__340.webp', true);
insert into producttable(productname, type1, type2, imagename, price, discount, color, size, amount, regDate,  detailimagename, onSale)
values('더블 벨트 롱 플레어 스커트', 'skirt','midi-long','peace-7041597__340.webp',46000,0,'크림',null, 100,'2022-02-14 13:24:51','northern-lights-6862969__340.webp', true);
insert into producttable(productname, type1, type2, imagename, price, discount, color, size, amount, regDate,  detailimagename, onSale)
values('그룸 슬립 롱 원피스', 'dress','ops','bird-6983434__340.webp',42000,0,'옐로우;핑크;블루;블랙',null, 100,'2022-02-15 13:24:51','tree-blossoms-7022041__340.webp', true);
insert into producttable(productname, type1, type2, imagename, price, discount, color, size, amount, regDate,  detailimagename, onSale)
values('밀키 골지 브이 가디건', 'outer','cardigan','altai-6943982__340.jpg',25000,0,'베이지;옐로우;핑크;소라;퍼플',null, 100,'2022-02-16 13:24:51','passiflora-7027917__340.webp', true);
insert into producttable(productname, type1, type2, imagename, price, discount, color, size, amount, regDate,  detailimagename, onSale)
values('위아드 카라 롱 맨투맨', 'top','mtm','travel-7014427__340.jpg',49000,0,'아이보리;핑크;블랙',null, 100,'2022-02-17 13:24:51','chicago-6921297__340.webp', true);
insert into producttable(productname, type1, type2, imagename, price, discount, color, size, amount, regDate,  detailimagename, onSale)
values('블톤 데님 점프수트', 'dress','jumpSuit','road-6881040__340.webp',46000,0,'진청',null, 100,'2022-02-18 13:24:51','sunset-6911736__340.webp', true);
insert into producttable(productname, type1, type2, imagename, price, discount, color, size, amount, regDate,  detailimagename, onSale)
values('엔츠 슬림핏 슬랙스', 'pants','slacks','monk-7010969__340.webp',33000,3300,null,'숏;롱', 100,'2022-02-19 13:24:51','wine-6688901__340.webp', true);
insert into producttable(productname, type1, type2, imagename, price, discount, color, size, amount, regDate,  detailimagename, onSale)
values('로말 셔츠', 'top','shirt','lantern-6826687__340.webp',29000,0,'아이보리;베이지;피치;소라;퍼플',null, 100,'2022-02-20 13:24:51','window-7001026__340.webp', true);
insert into producttable(productname, type1, type2, imagename, price, discount, color, size, amount, regDate,  detailimagename, onSale)
values('팬트리 터틀 슬리브리스', 'top','sleeveless','nature-6842159__340.jpg',16000,0,'아이보리;베이지',null, 100,'2022-02-21 13:24:51','year-6786741__340.webp', true);
insert into producttable(productname, type1, type2, imagename, price, discount, color, size, amount, regDate,  detailimagename, onSale)
values('뮤지엄 캐시미어 하프 니트', 'top','knit','house-6967908__340.jpg',39000,0,'크림;머스타드;레드;블루;메란지',null, 100,'2022-02-22 13:24:51','sea-6948569__340.webp', true);
insert into producttable(productname, type1, type2, imagename, price, discount, color, size, amount, regDate,  detailimagename, onSale)
values('테이스트 부츠컷 데님', 'pants','denim','port-6587129__340.webp',39000,0,'연청;진청','XS;S;M;L', 100,'2022-02-23 13:24:51','road-6935773__340.jpg', true);

-- 장바구니
insert into baskettable(id, productno, selectedcolor, selectedsize, basketAmount) values('portal',1,'소프트민트','S',2);
insert into baskettable(id, productno, selectedcolor, selectedsize, basketAmount) values('tester',2,'아이보리',null,1);
insert into baskettable(id, productno, selectedcolor, selectedsize, basketAmount) values('tester',3,'블랙',null,2);
insert into baskettable(id, productno, selectedcolor, selectedsize, basketAmount) values('tester',4,'아이보리','L',3);
insert into baskettable(id, productno, selectedcolor, selectedsize, basketAmount) values('aodremm',5,'소프트민트',null,1);
insert into baskettable(id, productno, selectedcolor, selectedsize, basketAmount) values('portal',6,'아이보리',null,2);
insert into baskettable(id, productno, selectedcolor, selectedsize, basketAmount) values('madana',7,'소프트민트','S',2);
insert into baskettable(id, productno, selectedcolor, selectedsize, basketAmount) values('tester',8,'아이보리',null,1);
insert into baskettable(id, productno, selectedcolor, selectedsize, basketAmount) values('grimhink',9,'소프트민트',null,1);
insert into baskettable(id, productno, selectedcolor, selectedsize, basketAmount) values('portal',10,'아이보리','M',3);
insert into baskettable(id, productno, selectedcolor, selectedsize, basketAmount) values('prose',11,'소프트민트',null,2);
insert into baskettable(id, productno, selectedcolor, selectedsize, basketAmount) values('rhonia',12,'아이보리',null,1);
insert into baskettable(id, productno, selectedcolor, selectedsize, basketAmount) values('tester',13,'소프트민트','숏',2);
insert into baskettable(id, productno, selectedcolor, selectedsize, basketAmount) values('rhonnyn',14,'아이보리','XS',1);
-- 관심상품
insert into wishlisttable(id, productno) values('tester', 1);
insert into wishlisttable(id, productno) values('tester', 2);
insert into wishlisttable(id, productno) values('tester', 3);
insert into wishlisttable(id, productno) values('tester', 4);
insert into wishlisttable(id, productno) values('portal', 1);
insert into wishlisttable(id, productno) values('portal', 2);
insert into wishlisttable(id, productno) values('portal', 7);
insert into wishlisttable(id, productno) values('portal', 10);
insert into wishlisttable(id, productno) values('aodremm', 1);
insert into wishlisttable(id, productno) values('grimhink', 2);
-- 주문
insert into ordertable(id, productno, orderno, selectedcolor, selectedsize, orderAmount, totalprice, orderDate, state, ordermethod, name, tel, zipcode, address, detailaddr)
values('tester',1,1,'그레이지','S',1,38000,'2018-10-27 13:24:51','배송완료','cash','유저','01098765432','54321','부산 남구 문현로 56-1 (네이버코리아)','5층 502호');
insert into ordertable(id, productno, orderno, selectedcolor, selectedsize, orderAmount, totalprice, orderDate, state, ordermethod, name, tel, zipcode, address, detailaddr)
values('portal',1,2,'소프트민트','M',1,38000,'2019-08-03 13:24:51','교환완료','credit','유저2','01045614561','54321','부산 남구 문현로 56-1 (네이버코리아)','5층 502호');
insert into ordertable(id, productno, orderno, selectedcolor, selectedsize, orderAmount, totalprice, orderDate, state, ordermethod, name, tel, zipcode, address, detailaddr)
values('portal',2,2,'피치베이지',null,4,119600,'2019-05-13 13:24:51','배송완료','credit','유저2','01045614561','24241','부산 문현로 56-1 (네이버코리아)','4층 405호');
insert into ordertable(id, productno, orderno, selectedcolor, selectedsize, orderAmount, totalprice, orderDate, state, ordermethod, name, tel, zipcode, address, detailaddr)
values('tester',4,3,'블랙','S',1,31000,'2020-09-15 13:24:51','환불완료','cash','유저','01098765432','54321','부산 남구 문현로 56-1 (네이버코리아)','5층 502호');
insert into ordertable(id, productno, orderno, selectedcolor, selectedsize, orderAmount, totalprice, orderDate, state, ordermethod, name, tel, zipcode, address, detailaddr)
values('tester',5,4,'메란지',null,1,28800,'2021-02-02 13:24:51','배송완료','credit','유저2','01045614561','54321','부산 남구 문현로 56-1 (네이버코리아)','5층 502호');
insert into ordertable(id, productno, orderno, selectedcolor, selectedsize, orderAmount, totalprice, orderDate, state, ordermethod, name, tel, zipcode, address, detailaddr)
values('portal',6,5,'베이지',null,2,62000,'2021-09-25 13:24:51','배송완료','credit','유저2','01045614561','24241','부산 문현로 56-1 (네이버코리아)','4층 405호');
insert into ordertable(id, productno, orderno, selectedcolor, selectedsize, orderAmount, totalprice, orderDate, state, ordermethod, name, tel, zipcode, address, detailaddr)
values('tester',7,6,'아이보리','S',1,41000,'2021-12-22 13:24:51','배송완료','cash','유저','01098765432','54321','부산 남구 문현로 56-1 (네이버코리아)','5층 502호');
insert into ordertable(id, productno, orderno, selectedcolor, selectedsize, orderAmount, totalprice, orderDate, state, ordermethod, name, tel, zipcode, address, detailaddr)
values('portal',8,6,'소라',null,1,115000,'2022-02-14 13:24:51','취소완료','credit','유저2','01045614561','54321','부산 남구 문현로 56-1 (네이버코리아)','5층 502호');
insert into ordertable(id, productno, orderno, selectedcolor, selectedsize, orderAmount, totalprice, orderDate, state, ordermethod, name, tel, zipcode, address, detailaddr)
values('tester',5,6,'오트밀',null,2,87600,'2022-03-04 13:24:51','배송완료','credit','유저2','01045614561','24241','부산 문현로 56-1 (네이버코리아)','4층 405호');
insert into ordertable(id, productno, orderno, selectedcolor, selectedsize, orderAmount, totalprice, orderDate, state, ordermethod, name, tel, zipcode, address, detailaddr)
values('tester',1,7,'소프트민트','L',1,38000,'2022-03-04 15:24:51','배송완료','credit','유저2','01045614561','24241','부산 문현로 56-1 (네이버코리아)','4층 405호');
insert into ordertable(id, productno, orderno, selectedcolor, selectedsize, orderAmount, totalprice, orderDate, state, ordermethod, name, tel, zipcode, address, detailaddr)
values('tester',3,8,'아이보리',null,1,92000,'2022-03-04 20:24:51','배송완료','credit','유저2','01045614561','24241','부산 문현로 56-1 (네이버코리아)','4층 405호');
insert into ordertable(id, productno, orderno, selectedcolor, selectedsize, orderAmount, totalprice, orderDate, state, ordermethod, name, tel, zipcode, address, detailaddr)
values('tester',4,9,'크림','M',1,31000,'2022-03-05 00:56:31','배송완료','credit','유저2','01045614561','24241','부산 문현로 56-1 (네이버코리아)','4층 405호');
insert into ordertable(id, productno, orderno, selectedcolor, selectedsize, orderAmount, totalprice, orderDate, state, ordermethod, name, tel, zipcode, address, detailaddr)
values('tester',15,9,'네이비',null,1,39600,'2022-03-05 01:56:31','배송완료','credit','유저2','01045614561','24241','부산 문현로 56-1 (네이버코리아)','4층 405호');
insert into ordertable(id, productno, orderno, selectedcolor, selectedsize, orderAmount, totalprice, orderDate, state, ordermethod, name, tel, zipcode, address, detailaddr)
values('portal',10,10,'블랙','M',1,49000,'2022-03-05 13:24:51','배송중','credit','유저2','01045614561','54321','부산 남구 문현로 56-1 (네이버코리아)','5층 502호');
insert into ordertable(id, productno, orderno, selectedcolor, selectedsize, orderAmount, totalprice, orderDate, ordermethod, name, tel, zipcode, address, detailaddr)
values('tester',20,10,'아이보리',null,1,44000,'2022-03-06 13:24:51','cash','유저','01098765432','54321','부산 남구 문현로 56-1 (네이버코리아)','5층 502호');
insert into ordertable(id, productno, orderno, selectedcolor, selectedsize, orderAmount, totalprice, ordermethod, name, tel, zipcode, address, detailaddr)
values('portal',11,11,'퍼플',null,1,17000,'credit','유저2','01045614561','24241','부산 문현로 56-1 (네이버코리아)','4층 405호');
-- 포인트 내역
insert into pointtable(id, point, content) values ('tester',-2000, '상품 구매');
insert into pointtable(id, point, content) values ('tester',500, '구매 확정');
insert into pointtable(id, point, content) values ('portal',-2000, '상품 구매');
insert into pointtable(id, point, content) values ('portal',500, '구매 확정');
insert into pointtable(id, point, content) values ('portal',100, '구매 확정');
insert into pointtable(id, point, content) values ('tester',2000, '리뷰 이벤트 당첨');
insert into pointtable(id, point, content) values ('tester',210, '구매 확정');
insert into pointtable(id, point, content) values ('tester',500, '구매 확정');
-- 공지사항
insert into noticetable(title, content, id, image) values('Grand Open!', '<p><br>2022년 1월 1일 Spring Mall Grand Open!', 'spring', '001.png');
insert into noticetable(title, content, id, image) values('1월 리뷰 이벤트', '<p><br>스프링몰 오픈 기념 리뷰 2배 이벤트르 진행합니다.', 'spring', '001.png');
insert into noticetable(title, content, id, image) values('해외배송 안내', '<p><br>많은 문의 주셨던 해외배송을 시작합니다!<br>이제 스프링의 옷을 해외에서도 받아봐요! ❤', 'spring', '002.png');
insert into noticetable(title, content, id, image) values('적립금 소멸 안내', '<p><br>적립금 소멸 유효기간 안내드립니다. 소멸 되기 전에 기간 내에 꼭 사용해주세요!', 'spring', '003.png');
insert into noticetable(title, content, id, image) values('2022년 02월 07일 이전 개인정보 처리방침', '<p>스프링는 (이하 "회사"는) 고객님의 개인정보를 중요시하며, "정보통신망 이용촉진 및 정보보호"에 관한 법률을 준수하고 있습니다.<br>회사는 개인정보취급방침을 통하여 고객님께서 제공하시는 개인정보가 어떠한 용도와 방식으로 이용되고 있으며, 개인정보보호를 위해 어떠한 조치가 취해지고 있는지 알려드립니다.</p><p>&nbsp;</p><p>■&nbsp;<strong>수집하는 개인정보 항목 및 수집방법</strong></p><p>가. 수집하는 개인정보의 항목<br>o 회사는 회원가입, 상담, 서비스 신청 등을 위해 아래와 같은 개인정보를 수집하고 있습니다.<br>- 회원가입시 : 이름 , 생년월일 , 성별 , 로그인ID , 비밀번호 , 자택 전화번호 , 휴대전화번호 , 이메일 , 14세미만 가입자의 경우 법정대리인의 정보<br>- 서비스 신청시 : 주소, 결제 정보</p><p>o 서비스 이용 과정이나 사업 처리 과정에서 서비스이용기록, 접속로그, 쿠키, 접속 IP, 결제 기록, 불량이용 기록이 생성되어 수집될 수 있습니다.</p><p>나. 수집방법<br>- 홈페이지, 서면양식, 게시판, 이메일, 이벤트 응모, 배송요청, 전화, 팩스, 생성 정보 수집 툴을 통한 수집</p><p>&nbsp;</p><p>■&nbsp;<strong>개인정보의 수집 및 이용목적</strong><br>회사는 수집한 개인정보를 다음의 목적을 위해 활용합니다.<br>o 서비스 제공에 관한 계약 이행 및 서비스 제공에 따른 요금정산 콘텐츠 제공 , 구매 및 요금 결제 , 물품배송 또는 청구지 등 발송 , 금융거래 본인 인증 및 금융 서비스<br>o 회원 관리 회원제 서비스 이용에 따른 본인확인 , 개인 식별 , 불량회원의 부정 이용 방지와 비인가 사용 방지 , 가입 의사 확인 , 연령확인 , 만14세 미만 아동 개인정보 수집 시 법정 대리인 동의여부 확인, 불만처리 등 민원처리 , 고지사항 전달<br>o 마케팅 및 광고에 활용 이벤트 등 광고성 정보 전달 , 접속 빈도 파악 또는 회원의 서비스 이용에 대한 통계</p><p>&nbsp;</p><p>■&nbsp;<strong>개인정보의 보유 및 이용기간</strong><br>원칙적으로, 개인정보 수집 및 이용목적이 달성된 후에는 해당 정보를 지체 없이 파기합니다. 단, 다음의 정보에 대해서는 아래의 이유로 명시한 기간 동안 보존합니다.</p><p>가. 회사 내부방침에 의한 정보보유 사유<br>&nbsp;회원이 탈퇴한 경우에도 불량회원의 부정한 이용의 재발을 방지, 분쟁해결 및 수사기관의 요청에 따른 협조를 위하여, 이용계약 해지일로부터 해당 수사, 조사 종료 시 까지 회원의 정보를 보유할 수 있습니다.</p><p>&nbsp;</p><p>나. 관련 법령에 의한 정보 보유 사유<br>전자상거래등에서의소비자보호에관한법률 등 관계법령의 규정에 의하여 보존할 필요가 있는 경우 회사는 아래와 같이 관계법령에서 정한 일정한 기간 동안 회원정보를 보관합니다.<br>o 계약 또는 청약철회 등에 관한 기록<br>- 보존이유 : 전자상거래등에서의소비자보호에관한법률<br>- 보존기간 : 5년<br>o 대금 결제 및 재화 등의 공급에 관한 기록<br>- 보존이유: 전자상거래등에서의소비자보호에관한법률<br>- 보존기간 : 5년<br>o 소비자 불만 또는 분쟁처리에 관한 기록<br>- 보존이유 : 전자상거래등에서의소비자보호에관한법률<br>- 보존기간 : 3년<br>o 로그 기록<br>- 보존이유: 통신비밀보호법<br>- 보존기간 : 3개월</p><p>&nbsp;</p><p>■&nbsp;<strong>개인정보의 파기절차 및 방법</strong><br>회사는 원칙적으로 개인정보 수집 및 이용목적이 달성된 후에는 해당 정보를 지체없이 파기합니다. 파기절차 및 방법은 다음과 같습니다.<br>o 파기절차<br>- 회원님이 회원가입 등을 위해 입력하신 정보는 목적이 달성된 후 별도의 DB로 옮겨져(종이의 경우 별도의 서류함) 내부 방침 및 기타 관련 법령에 의한 정보보호 사유에 따라(보유 및 이용기간 참조) 일정 기간 저장된 후 파기되어집니다.<br>별도 DB로 옮겨진 개인정보는 법률에 의한 경우가 아니고서는 보유되어지는 이외의 다른 목적으로 이용되지 않습니다.<br>o 파기방법<br>- 전자적 파일형태로 저장된 개인정보는 기록을 재생할 수 없는 기술적 방법을 사용하여 삭제합니다.</p><p>&nbsp;</p><p>■&nbsp;<strong>개인정보 제공</strong><br>회사는 이용자의 개인정보를 원칙적으로 외부에 제공하지 않습니다. 다만, 아래의 경우에는 예외로 합니다.<br>o 이용자들이 사전에 동의한 경우<br>o 법령의 규정에 의거하거나, 수사 목적으로 법령에 정해진 절차와 방법에 따라 수사기관의 요구가 있는 경우</p><p>&nbsp;</p><p>■&nbsp;<strong>수집한 개인정보의 위탁</strong><br>회사는 서비스 이행을 위해 아래와 같이 외부 전문업체에 위탁하여 운영하고 있습니다.<br>o 위탁 대상자 : 우체국택배<br>o 위탁업무 내용 : 해당상품 상하자 및 배송 대행업무</p><p>o 위탁 대상자 : 나이스페이먼츠 - 올더게이트<br>o 위탁업무 내용 : 카드결제 대행업무</p><p>o 위탁 대상자 : ㈜스냅컴퍼니(알림전송수탁업체 : ㈜엠티에스컴퍼니)</p><p>o 위탁업무 내용 : 고객 개인정보(이름, 아이디, 휴대전화번호)를 통해 신규 서비스(제품) 개발 및 맞춤 서비스 제공, 이벤트 및 광고성 정보 및 참여기회 제공 (2020-09-28~)</p><p><br>-&nbsp;수탁자&nbsp;: (주)루나소프트</p><p>-&nbsp;위탁&nbsp;업무&nbsp;내용&nbsp;:&nbsp;카카오톡&nbsp;알림톡(정보성메세지)&nbsp;발송&nbsp;업무</p><p>&nbsp;</p><p>&nbsp;</p><p>&nbsp;#네이버 알림 시행에 관한 내용<br>&nbsp;</p><p>&nbsp; &nbsp; &nbsp; &nbsp; -&nbsp;수탁자&nbsp;:&nbsp;㈜루나소프트</p><p>&nbsp; &nbsp; &nbsp; &nbsp; -&nbsp;개인정보제공처&nbsp;:&nbsp;네이버㈜</p><p>&nbsp; &nbsp; &nbsp; &nbsp; -&nbsp;위탁 업무 내용&nbsp;:&nbsp;네이버 알림 서비스 제공</p><p>&nbsp;</p><p><br>■&nbsp;<strong>이용자 및 법정대리인의 권리와 그 행사방법</strong><br>o 이용자는 언제든지 등록되어 있는 자신의 개인정보를 조회하거나 수정할 수 있으며 가입해지를 요청할 수도 있습니다.<br>o 이용자들의 개인정보 조회,수정을 위해서는 "개인정보변경"(또는 "회원정보수정" 등)을 가입해지(동의철회)를 위해서는 "회원탈퇴"를 클릭하여 본인 확인 절차를 거치신 후 직접 열람, 정정 또는 탈퇴가 가능합니다.<br>o 혹은 개인정보관리책임자에게 서면, 전화 또는 이메일로 연락하시면 지체없이 조치하겠습니다.<br>o 귀하가 개인정보의 오류에 대한 정정을 요청하신 경우에는 정정을 완료하기 전까지 당해 개인정보를 이용 또는 제공하지 않습니다. 또한 잘못된 개인정보를 제3자에게 이미 제공한 경우에는 정정 처리결과를 제3자에게 지체없이 통지하여 정정이 이루어지도록 하겠습니다.<br>o 회사는 이용자의 요청에 의해 해지 또는 삭제된 개인정보는 "회사가 수집하는 개인정보의 보유 및 이용기간"에 명시된 바에 따라 처리하고 그 외의 용도로 열람 또는 이용할 수 없도록 처리하고 있습니다.</p><p>o 이용자의 권리 행사는 정보주체의 법정대리인이나 위임을 받은자 등 대리인을 통하여 처리할 수 있으며, 이 경우 개인정보 보호법 시행규칙 서식에 따른 위임장을 제출하셔야 합니다.</p><p>o 이용자는 개인정보보호법 등 관계법령을 위반하여 회사가 처리하고 있는 정보주체 본인이나 타인의 개인정보 및 사생활을 침해하여서는</p><p>&nbsp;</p><p>■&nbsp;<strong>개인정보 자동수집 장치의 설치, 운영 및 그 거부에 관한 사항</strong><br>회사는 귀하의 정보를 수시로 저장하고 찾아내는 "쿠키(cookie)" 등을 운용합니다. 쿠키란 웹사이트를 운영하는데 이용되는 서버가 귀하의 브라우저에 보내는 아주 작은 텍스트 파일로서 귀하의 컴퓨터 하드디스크에 저장됩니다.<br>회사은(는) 다음과 같은 목적을 위해 쿠키를 사용합니다.<br>o 쿠키 등 사용 목적<br>1. 회원과 비회원의 접속 빈도나 방문 시간 등을 분석, 이용자의 취향과 관심분야를 파악 및 자취 추적, 각종 이벤트 참여 정도 및 방문 회수 파악 등을 통한 타겟 마케팅 및 개인 맞춤 서비스 제공<br>2. 귀하는 쿠키 설치에 대한 선택권을 가지고 있습니다. 따라서, 귀하는 웹브라우저에서 옵션을 설정함으로써 모든 쿠키를 허용하거나, 쿠키가 저장될 때마다 확인을 거치거나, 아니면 모든 쿠키의 저장을 거부할 수도 있습니다.<br>o 쿠키 설정 거부 방법<br>1. 쿠키 설정을 거부하는 방법으로는 회원님이 사용하시는 웹 브라우저의 옵션을 선택함으로써 모든 쿠키를 허용하거나 쿠키를 저장할 때마다 확인을 거치거나, 모든 쿠키의 저장을 거부할 수 있습니다.<br>2. 설정방법 예(인터넷 익스플로어의 경우) : 웹 브라우저 상단의 도구 &gt; 인터넷 옵션 &gt; 개인정보<br>3. 단, 귀하께서 쿠키 설치를 거부하였을 경우 서비스 제공에 어려움이 있을 수 있습니다.</p><p>&nbsp;</p><p>■&nbsp;<strong>개인정보에 관한 민원서비스</strong><br>회사는 고객의 개인정보를 보호하고 개인정보와 관련한 불만을 처리하기 위하여 아래와 같이 관련 부서 및 개인정보관리책임자를 지정하고 있습니다.<br>o 개인정보관리담당자<br>성명 : 김재형<br>소속 : 스프링<br>전화번호 : 070-7705-5595<br>이메일 : slowandmore@naver.com</p><p>&nbsp;</p><p>&nbsp;</p><p>■&nbsp;<strong>개인정보의&nbsp;위탁&nbsp;처리</strong></p><p>스프링는&nbsp;서비스&nbsp;향상을&nbsp;위해&nbsp;관계법령에&nbsp;따라&nbsp;회원의&nbsp;동의를&nbsp;얻거나&nbsp;관련&nbsp;사항을&nbsp;공개&nbsp;또는&nbsp;고지&nbsp;후&nbsp;회원의&nbsp;개인정보를&nbsp;외부에&nbsp;위탁하여&nbsp;처리하고&nbsp;있습니다. 보유 및 이용기간은 서비스 제공기간 까지이며, 스프링의&nbsp;개인정보처리&nbsp;수탁자와&nbsp;그&nbsp;업무의&nbsp;내용은&nbsp;다음과&nbsp;같습니다.</p><p><br>&nbsp;</p>',
															 'spring', '정보위탁.png');
insert into noticetable(title, content, id, image) values('스프링 멤버쉽 가입 혜택', '<p><br>스프링몰에 오신것을 환영합니다. <br>멤버가 되어 다양한 혜택을 누리세요!', 'spring', '007.png');
insert into noticetable(title, content, id, image) values('1월 리뷰 이벤트 당첨자 발표', '<p><br>1월의 리뷰이벤트 당첨자를 발표합니다. 당첨되신 분들 모두 축하드립니다!', 'spring', '006.png');
insert into noticetable(title, content, id, image) values('2월 리뷰 이벤트', '<p><br>2월 리뷰이벤트가 시작됩니다. <br>기간 내에 상품을 구매하시고 후기를 남겨주시는 분들께 추첨을 통해 상품을 드려요!',
															 'spring', '008.png');
insert into noticetable(title, content, id, image) values('White day Edition Week', '<p><br>White Day를 위한 세일을 진행합니다. 최대 40%할인 놓치지 마세요!', 'spring', '009.png');
insert into noticetable(title, content, id, image) values('2월 리뷰 이벤트 당첨자 발표', '<p><br>2월의 리뷰이벤트 당첨자를 발표합니다. 당첨되신 분들 모두 축하드립니다!', 'spring', '006.png');
insert into noticetable(title, content, id, image) values('3월 리뷰 이벤트', '<p><br>3월 리뷰이벤트가 시작됩니다. <br>기간 내에 상품을 구매하시고 후기를 남겨주시는 분들께 추첨을 통해 상품을 드려요!',
															 'spring', '008.png');
insert into noticetable(title, content, id, image) values('[필독]배송지연 안내', '<p><br>현재 택배 파업으로 인해 배송이 지연되고 있습니다.<br> 저희 스프링몰에서는 고객님께서 빨리 제품을 받으실 수 있도록 최선을 다하겠습니다. <br>아래 이미지를 통해 배송 지연 지역을 확인해보세요.',
															 'spring', '004.png');

-- 자주 묻는 질문
insert into faqtable(type, title, content) values('product', '품절된 상품은 재입고 안되나요?', '<p>제작처 사정으로 인해 품절된 상품은 바로 재입고 여부 판단이 어려우며 재입고 가능할 시 해당 상품페이지에 안내해 드리고 있습니다.</p>
                                                                                         <p>정확한 일정은 제작처 상황에 따라 변경될 수 있는 점 양해 부탁드립니다.</p>');
insert into faqtable(type, title, content) values('delivery', '배송은 얼마나 걸리나요?', '<p>-한 주문건에 상품들은 모두 함께 준비되어 발송되며 기본 배송준비일은 결제완료 기준 다음날부터 빠르면 <strong>영업일 기준 1~2일에서 길어지면 3~4일 소요 됩니다.</strong>(지연상품 제외)</p>
																					<P>-사전에 지연되는 상품은 상품명에서 소요되는 시일 확인 가능하며 구매하시는 상품이 <strong>[입고지연/주문폭주/예약주문]</strong> 상품이 아닌지 확인해주세요.</p>
                                                                                    <p>-외 공급처 사정으로 인해 <strong>갑작스러운 지연 및 품절이 발생 될 수 있습니다.</strong> 사전에 안내드린 일정보다 지연되는 경우 개별적으로 빠른 안내 드리겠습니다.</p>');
insert into faqtable(type, title, content) values('return', '교환/반품 접수는 어떻게 하나요?', '<p>Q&amp;A “배송 후 교환/반품” 게시판 또는 고객센터 [070-1234-1234]로 접수해주셔야 정상적인 처리가 가능합니다.</p>
                                                                                         <p>&nbsp;</p><p>-상품을 받는 날로부터 꼭<strong> “7일”(주말제외/영업일기준) </strong>안으로 요청해 주세요. 스프링에서 우체국 택배사로 회수 신청해드리고 있습니다.</p><p>&nbsp;</p>
                                                                                         <p>[반품주소지]</p><p>서울시 동대문구 천호대로16 (주)스프링 물류창고</p>');
insert into faqtable(type, title, content) values('product', '옷을 받았는데 세탁은 어떻게 하나요?', '<p>모든 의류 상품 첫 세탁방법은 원단의 오랜 유지를 위하여 <strong>드라이클리닝을 추천 드립니다.</strong> 자세한 세탁 방법은 상품 상세 페이지 하단에 기재되어 있으니 <strong>소재별로 확인해 주세요.</strong></p>');
insert into faqtable(type, title, content) values('product', '기획특가모음 / 라스트찬스', '<p>해당 카테고리 외 이벤트 카테고리 상품들은 원가 미만에 판매되는 상품으로 교환/반품이 어렵습니다.</p><p>&nbsp;</p>
                                                                                    <p>모든 주문건은 입금하신 순으로 순차적 출고됩니다.</p><p>카드결제 → 휴대폰결제 → 무통장입금(외 현금결제) 순으로 결제완료 처리되며 무통장입금 시 주문건이 생성되었어도 입금확인 전산 처리 전 해당 상품 재고소진으로 품절될 수 있으니 이점 양해 부탁드립니다.</p>
																					<p>(상품품절 시 최대한 빠르게 개별 연락드려 환불 처리 도와드리겠습니다.</p>');
insert into faqtable(type, title, content) values('delivery', '무료배송 조건은 어떻게 되나요?', '<p><strong>5만원 이상</strong> 구매시 무료배송에 적용됩니다.</p>
                                                                                          <p>교환/반품 시 최종 결제하시는 상품이 5만원 미만이 될 경우 혜택에 해당되지 않아 초기 배송비가 함께 차감되어 환불 처리됩니다.</p>');
insert into faqtable(type, title, content) values('delivery', '당일발송 구매했는데 언제 배송되나요?', '<p>당일 낮<strong> 12시 이전까지 결제 및 입금확인이 완료된</strong> 주문건 한해 발송해 드리고 있습니다.</p><p>&nbsp;</p>
                                                                                              <p>-무통장 입금의 경우 입금확인까지 시간이 소요될 수 있어 간혹 당일출고가 어려울 수 있는 점 양해 부탁드립니다.</p>
																							  <p>-주문 후 옵션이나 정보를 변경하실 경우 당일발송이 어려울 수 있습니다.</p>
																							  <p>-당일발송 상품이 아닌 일반 상품과 함께 구매하실 경우 당일 출고가 불가하니 꼭 단독 주문해주세요.</p>
																							  <p>-갑작스러운 주문폭주 및 검수 시 발생하는 불량상품으로 인해 당일발송이 불가할 수 있습니다.</p>
																							  <p>-물류센터에서 당일 출고가 완료된 후 택배사 사정에 따라 원하시는 날짜에 수령이 어려울 수 있으니 출고 후 택배사측으로 문의 부탁드립니다.</p>');
insert into faqtable(type, title, content) values('delivery', '따로 주문했는데 합배송으로 받을 수 있나요?', '<p><strong>모든 주문 건 배송 전 상태에 요청 시</strong> 합배송 처리와 &nbsp;합배송비 환불이 가능합니다. 고객센터 [070-1234-1234] 또는 Q&amp;A “배송 전 변경/취소” 게시판으로 요청 주셔야 가능하며 혹여나 빠른 출고 처리로 인해 이미 합배송 요청하신 <strong>주문 건이 일부 발송 된다면 처리가 불가능한 점 참고 부탁드립니다.</strong></p><p>&nbsp;</p>
                                                                                                   <p>-실제 상품은 발송되었으나 마이페이지 상에는 배송 전으로 조회될 수 있으며 전산 처리되는데 시간이 소요됩니다! 본사에서는 실제 출고 상태로 체크하며 합배송비 환불이 불가능할 수 있는 점 양해 부탁드립니다.</p>');
insert into faqtable(type, title, content) values('delivery', '배송 전 주소지를 변경하고 싶어요.', '<p>당일 낮 10시 이전 Q&amp;A “배송 전 변경/취소” 게시판으로 원하시는 주소지 확인 후 문의주세요. 이미 배송작업이 시작되었을 경우 배송지 변경이 불가합니다.</p>'); 
insert into faqtable(type, title, content) values('delivery', '주문을 취소하고 싶어요.', '<p>Q&amp;A <strong>“배송 전 변경/취소” 게시판</strong> 또는 <strong>고객센터 [070-1234-1234]</strong>로 접수해 주셔야 정상적인 처리가 가능합니다.</p><p>&nbsp;</p>
                                                                                    <p><strong>낮 10시 이전(출고 전) 요청 건은 최대한 처리해 드리고 있으나, 배송작업이 시작됐을 경우 처리가 거부 될 수 있습니다.</strong></p>
																					<p>-결제가 완료된 주문 건은 재고가 할당되는 대로 실시간 출고가 진행되고 있습니다. 최대한 빠르게 수령하실 수 있기 위한 방침입니다.</p>
                                                                                    <p>-이미 출고된 상품 취소를 원하실 경우 상품을 받아보신 후 반품 신청해 주셔야 &nbsp;하며 배송비는 고객님 부담입니다.</p><p>&nbsp;</p>
                                                                                    <p>-카드취소 후 카드사 전산처리 기간 최대 3 - 7일</p>
                                                                                    <p>-현금결제 후 취소 입금되는 시간 최대 1 - 3일</p>
                                                                                    <p>영업일 기준으로 처리 가능하며, 현금결제 경우에 초기 입금해주신 동일계좌로만 환불처리 가능합니다.</p>');
insert into faqtable(type, title, content) values('delivery', '배송 전에 상품을 변경/추가 하고 싶어요.', '<p>[자사몰 주문 건 기준 / 지그재그 제트결제건은 추가/변경 불가합니다.]</p><p>&nbsp;</p>
                                                                                                 <p>Q&amp;A <strong>“배송 전 변경/취소” </strong>게시판 또는 <strong>고객센터 [070-1234-1234]</strong>로 접수해 주셔야 정상적인 처리가 가능합니다.</p><p>&nbsp;</p>
																					             <p><strong>변경</strong> - 같은 상품 옵션 변경일 경우 원하시는<strong> 상품명과 옵션을 정확하게 기재하여</strong> 남겨주세요.</p><p>(다른 상품 변경일 경우 추가 금액까지 한 번에 입금 후 요청해주세요.)</p><p><strong>취소</strong> - 추가 원하시는 <strong>상품 금액 입금 후 정확한 상품명과 입금자명/금액을</strong> 남겨주세요.</p><p>&nbsp;</p>
																								 <p>변경/추가 상품들은 시스템상 <strong>구매적립금 자동지급, 현금영수증 자동신청이 어렵습니다.</strong> 원하실 경우에는 기존 주문 건 취소 후 모두 재 결제 해주시면 원활한 처리가 가능합니다.</p><p>&nbsp;</p>
                                                                                                 <p><strong>낮 10시 이전(출고 전) 요청 건은 최대한 처리해 드리고 있으나, 배송작업이 시작됐을 경우 처리가 거부 될 수 있습니다.</strong></p><p>-결제가 완료된 주문 건은 재고가 할당되는 대로 실시간 출고가 진행되고 있습니다. 최대한 빠르게 수령하실 수 있기 위한 방침입니다.</p><p>-이미 출고된 상품 취소/변경 원하실 경우 상품을 받아보신 후 반품/교환 신청해 주셔야 하며 배송비는 고객님 부담입니다.</p><p>&nbsp;</p>
                                                                                                 <p><strong>성격에 맞지 않는 타 게시판에 취소/변경/추가 요청해 주실 경우 게시글 확인 전 상품이 출고 될 수 있습니다.</strong></p><p>-이미 출고된 상품 취소/변경 원하실 경우 상품을 받아보신 후 반품/교환 신청해 주셔야 하며 배송비는 고객님 부담입니다.</p><p>&nbsp;</p>
                                                                                                 <p><strong>주문취소/상품변경/상품추가/주소지변경</strong> 요청으로 인한 게시글 작성 시 <strong>자동으로 양식이 구분돼</strong> 등록되어 있으니<strong> 꼼꼼하게 확인 후 누락없이 작성 부탁드립니다.</strong></p><p>-상품 변경/추가로 추가 차액금이 발생할 경우 <strong>차액금을 먼저 입금, 게시글 양식에 맞게 작성 후 [입금자명/입금액]</strong>을 함께 남겨주세요.</p><p>-상품 추가는 현금 결제만 가능합니다. 카드결제 및 다른 결제 수단을 원하실 경우 기존 주문 건 취소 후 모두 함께 재결제 해주시면 됩니다.</p>'); 
insert into faqtable(type, title, content) values('return', '교환/반품 시 배송비는 어떻게 하나요?', '<p><strong>반품</strong> - 환불 금액에서 배송비 <strong>차감 후</strong> 환불해 드립니다.</p><p><strong>교환</strong> - 왕복 배송비를 <strong>입금</strong>해주시면 됩니다.</p><p>&nbsp;</p>
                                                                                            <p>반품용지에 [임급자명/입금날짜] 기재 필수</p><p>[기업은행/000-000000-00-000/(주)스프링]</p><p>&nbsp;</p>
                                                                                            <p>5만원 이하 구매 → 반품상품 금액에서 -2,500원 차감</p><p>5만원 이상 구매 → 반품상품 금액에서 -5,000원 차감 (초기 무료배송 혜택 적용된 경우)</p><p>–반품 후에도 나머지 구입하신 금액이 5만원 이상일 경우 -2,500원만 차감됩니다.</p>'); 
insert into faqtable(type, title, content) values('return', '상품을 받아봤는데 오배송/불량상품이 왔어요.', '<p>“배송 후 교환/반품” 게시판에 <strong>사진을 필수 첨부하여 문의해 주세요.</strong></p><p>정확한 처리를 위하여 불량/오배송 확인이 가능한 사진이 필요합니다.</p><p>불량/오배송 상품의 경우 스프링 왕복택배비(5,000원) 부담으로</p><p>신속하게 교환 처리 도와드리고 있습니다!</p><p>&nbsp;</p>
                                                                                                 <p><strong>교환</strong> - 스프링 왕복 배송비 부담으로 교환 처리.</p><p><strong>반품</strong> - 반품 배송비 스프링 부담으로 처리.</p><p>&nbsp;</p>
																								 <p>교환이 아닌 환불 원하실 경우, &nbsp;5만원이상 무료배송 혜택을 받아보시고(무료배송쿠폰 포함) 환불로 인해 혜택이 취소되는 경우, 초기 배송비(2,500원) 차감되어 환불될 수 있습니다.</p><p>&nbsp;</p>
                                                                                                 <p>하자가 없는 정상 제품일 경우 그 상품에 대한 변심으로 간주되어 한 번의 편도 배송비(2,500원)를 부담해주셔야 합니다.</p>'); 
insert into faqtable(type, title, content) values('return', '교환/반품처리가 어려운 경우가 있나요?', '<p>-상품 수령일로부터 7일 이상 (주말제외/영업일기준) 경과된 경우</p><p>-상품정보에 교환/환불 불가능이 명시되어 있는 경우</p><p>&nbsp;(라스트찬스, 세일 상품, 이벤트 카테고리 상품 등)</p><p>-택 제거 및 바코드 훼손, 오염이 확인된 상품</p><p>-사용 흔적(집냄새, 향수냄새, 체취)가 발견된 상품</p><p>-고객 부주의 또는 임의 세탁으로 인해 훼손된 상품</p><p>-배송 시 생긴 단순 구김과 제작 과정에서 발생하는 섬유 냄새,</p><p>&nbsp;마감 박음질, 단순 실밥, 세탁 시 지워지는 초크 자국은 불량상품으로 볼 수 없습니다.</p><p>&nbsp;</p>
                                                                                             <p>위와 같은 경우에는 교환/반품 처리가 어려울 수 있습니다.</p>'); 
insert into faqtable(type, title, content) values('return', '상품을 보냈는데 환불처리는 언제 되나요?', '<p>보내주신 상품은 택배사 중간 사업소를 거쳐오기에 신청을 해주신 날로부터 대략 <strong>일주일 정도 소요될 수 있습니다.</strong></p><p>어플 또는 우체국 사이트 배송조회에서 확인되는 도착일자와 실제 스프링 사무실 도착일자가 상이하니 참고해 주세요. 본사로 반입 후 최대한 빠르게 처리해드리겠습니다.</p><p>&nbsp;</p>
                                                                                              <p>-카드취소 후 카드사 전산처리 기간 최대 3 - 7일</p><p>-현금결제 취소 후 입금된느 시간 최대 1 - 3일</p><p>영업일 기준으로 처리 가능하며, 현금결제 경우에 초기 입금해주신 동일계좌로만 환불처리 가능합니다.</p>'); 
insert into faqtable(type, title, content) values('return', '다른 택배사로 교환/반품 택배를 보내도 되나요?', '<p>스프링에서 신청해드린 기사님 통해 발송하시지 않을 경우, 추가 배송비 부과될 수 있으니 <strong>신청해드린 방문 기사님 통해 보내주시기 바랍니다.&nbsp;</strong></p><p>부득이하게 타 택배사 통해 보내주신다면 <strong>꼭 “선불” 발송 부탁드립니다.</strong></p>
                                                                                                   <p>착불 발송 시 택배 비용 전체가 환불 금액에서 제외되어 환불 처리됩니다.</p>'); 
insert into faqtable(type, title, content) values('return', '휴대폰결제로 주문했는데 취소/반품이 가능한가요?', '<p>휴대폰 결제는 통신 요금에 해당되어 청구되는 결제 방식으로 통신사 정책상 결제하신 달의 1일 ~ 31일까지만 전체취소가 가능합니다.</p>
                                                                                                    <p>또한 통신사 정책상, 휴대폰 결제는 부분취소가 불가하며 휴대폰 결제 취소 시 통신사 측으로 이체되는 휴대폰 결제 수수료 3.5%를 제외한 후 환불처리 되는 점 양해부탁드립니다.</p>'); 
insert into faqtable(type, title, content) values('etc', '현금영수증 신청은 어떻게 하나요?', '<p>고객님께서 주문서 작성 시 현금영수증 신청 직접 가능합니다.</p><p>간혹 직접 신청을 못해주셨을 경우 문의게시판에 남겨주시면 확인 후 신청 도와드리고 있습니다.</p><p>&nbsp;</p>
                                                                                      <p>초기 주문 시 현금영수증을 신청해 주시고 <strong>일부 금액 환불 받아보실 경우 초기 신청해 주신 현금영수증 신청이 자동으로 취소됩니다. 환불 이후 꼭 고객센터/게시판 통해 재신청을 부탁드립니다.</strong></p><p>&nbsp;</p>
                                                                                      <p>ex) 현금영수증을 신청합니다.</p><p>주문번호 : 20220223-00000000</p><p>휴대폰 번호(영수증 신청) : 010-0000-0000</p><p>&nbsp;</p><p>휴대폰 번호는 현금영수증을 신청 원하는 번호로 기재해 주십시오.</p>'); 
insert into faqtable(type, title, content) values('etc', '적립금 안내', '<p><strong>적립금</strong> - 스프링에서 혜택으로 드리고 있는 전자화폐</p><p>(회원가입적립금/리뷰적립금/구매상품적립금 등) 1만원 이상의 상품을 구매할 때, 누적적립금이 최소 2,000원 이상일 때 사용가능</p><p>&nbsp;</p>
                                                                      <p>리뷰적립금 - 작성 후 7일 이내로 순차 지급</p><p>구매상품적립금 - 배송완료 후 14일 이후 자동적립</p>'); 
insert into faqtable(type, title, content) values('etc', '입금했는데 입금확인이 안돼요.', '<p>주문서의 입금자명과 금액이 <strong>오차 없이 동일하면</strong> 문제 없이 자동입금 확인 됩니다. (전산 처리에 따라 1 - 3시간 소요될 수 있습니다.)</p><p>주문자명/입금자명 다르게 기재한 경우에는 꼭 입금자명으로 입금해주셔야 합니다.</p><p>&nbsp;</p>
                                                                                    <p><strong>자동입금 확인이 어려운 경우</strong> (자동입금 누락될 시 배송이 늦어질 수 있는 점 참고 부탁드립니다.)</p><p>&nbsp;</p>
                                                                                    <p>-주문 건 작성 시 기재한 입금자명과 실제 입금자명이 조금이라도 다른 경우&nbsp;</p><p>-입금자명 제한수가 초과 되었거나 띄어쓰기가 조금이라도 다른 경우</p><p>-결제 예정 금액과 실제 입금액이 조금이라도 다른 경우</p><p>-각 다른 주문건에 입금자명이 동명인, 결제예정금액 &nbsp;또한 동일할 경우 (입금자명과 결제 예정 금액이 동일한 주문건이 중복으로 있을 경우)</p><p>&nbsp;</p>
                                                                                    <p>1 - 3시간 내에도 입금확인이 안되는 경우 “배송 전 변경/취소” 게시판에<strong> [입금은행/입금자명/날짜/시간]</strong> 확인 후 남겨 주시면 수동입금 처리 도와 드리겠습니다.</p>'); 
insert into faqtable(type, title, content) values('etc', '환불 관련 자주 하시는 질문', '<p>주문 건에 대한 모든 환불 처리는<strong> [배송 후 교환/반품], [배송 전 변경/취소]</strong> 게시판 또는 <strong>고객센터 [070-1234-1234]</strong>로 접수해 주셔야 처리가 가능합니다.</p><p>&nbsp;</p>
                                                                                  <p>-카드취소 후 카드사 전산처리 기간 최대 3 - 7일</p><p>-현금결제 취소 후 입금되는 시간 최대 1 - 3일</p><p>영업일 기준으로 처리 가능하며 현금결제 경우에 초기 입금해주신 동일 계좌로만 환불처리 가능합니다.</p><p>&nbsp;</p><p>-모든 환불 처리는 초기 <strong>결제해 주신 수단</strong>으로 환불 처리 가능합니다.</p><p>-현금결제(무통장입금, 계좌이체 등) 결제건은<strong> [계좌번호/예금주명/은행사]</strong> 정보가 꼭 필요합니다.</p>'); 
insert into faqtable(type, title, content) values('etc', '해외 배송 가능한가요?', '<p>-원화 결제만 가능합니다.</p><p><strong>-"해외배송" 상품을 검색하여</strong> 구매 원하시는 상품과 <strong>함께 결제해</strong> 주세요.</p><p>-자연재해, 천재지변으로 인한 상품 훼손 및 배송지연이 불가피하게 일어날 수 있는 점 동의하시는 경우에만 해외배송 결제를 부탁드립니다.</p><p>-출고 후 수령까지 상황에 따라 최대 한 달까지 소요될 수 있습니다.</p><p>-해외배송 통관 시 발생하는 관세(추가배송비)에 관련해서는 스프링에서 예측하기 어렵습니다. &nbsp;<strong>추가적으로 발생하는 금액은 모두 고객님께서 부담해 주셔야 합니다.</strong></p><p>-결제 완료 직후 해외배송 게시판에 자동 등록된 <strong>양식에 맞게 작성해주세요.</strong> 상품 준비 후 메일로 추가 배송비를 안내드리고 있으며, <strong>일주일 내 배송비 입금이 확인되지 않을 경우</strong> 주문 건은 변심 취소로 간주하며 <strong>환불 처리될 수 있습니다.</strong></p>'); 
insert into faqtable(type, title, content) values('etc', '리뷰글을 작성했는데 적립금 지급이 안되었어요.', '<p><strong>영업일 기준 7일 안으로</strong> 순차 지급해드리고 있습니다.</p><p>판매/교환/반품예정인 상품의 후기글 등 후기 게시판 성격에 맞지 않는 글을 무통보 삭제될 수 있습니다.</p><p>&nbsp;</p><p>아래 조건이 맞지 않을 경우 적립금 지급이 어려울 수 있습니다.</p><p>조건을 충족하였으나 지급이 안되었을 경우 “ 상품문의” 게시판으로<strong> [아이디/상품명,옵션] </strong>남겨주시면 안내 도와드리겠습니다.</p><p>&nbsp;</p><p><strong>적립금 지급 기준</strong></p><p>-글 후기 (30자 이상) &nbsp;&amp; 상품 미착용 사진 : 500원</p><p>-착용 사진 + 글 후기 (30자 이상) : 1,000원</p><p>&nbsp;</p><p><strong>적립금 지급이 어려운 경우</strong></p><p>-지그재그 제트결제 주문 건</p><p>-30자 미만 글 후기 (반복문구/띄어쓰기 제외)</p><p>-수령한지 2주가 지난 상품 (주말,공휴일 포함 14일이내)</p><p>-단품 상품 금액이 9,900원 미만인 상품</p><p>-반품/교환 에정인 상품</p><p>-세일 상품 (라스트찬스, 이벤트 카테고리 상품)</p><p>-한 가지 상품을 중복으로 작성해 준 리뷰글</p><p>-주문 건에 추가 금액 입금 후 상품 추가/변경했을 경우</p>'); 
insert into faqtable(type, title, content) values('product', '신상품은 언제 업데이트 되나요?', '<p>평균 <strong>월 수 금 11:00 am</strong> 새로운 상품들이 업데이트 됩니다.</p><p>간혹 상황에 따라 업데이트 일정이 변동될 수 있으며<strong> @spring 인스타그램 계정을</strong> 통해 업데이트 일정을 공유 드리고 있습니다!</p>'); 
insert into faqtable(type, title, content) values('etc', '주문 후 언제까지 입급해야 하나요?', '<p>무통장입금 등 현금으로 결제해 주시는 경우 7일 내 입금확인이 되지 않을 때 주문 건은 자동 취소 처리됩니다.</p>'); 
insert into faqtable(type, title, content) values('etc', '콜센터 전화연결이 안돼요.', '<p>업무가 시작되는 월요일, 긴 공휴일 이후 또는 이벤트 직후, 문의 폭주로 인해 콜센터 연결이 지연될 수 있습니다.</p><p>불편하시더라도 이점 너그럽게 양해 부탁드리며 성격에 맞는 게시판에 문의 주시면 최대한 빠른 처리 도와드릴 수 있도록 하겠습니다.</p><p>&nbsp;</p><p><strong>[고객센터 운영시간 / 070-1234-1234]</strong></p><p>am 10:00 - pm 17:00</p><p>BREAK TIME : pm 12:00 - pm 13:00(sat / sun / holiday OFF)</p>'); 
insert into faqtable(type, title, content) values('delivery', '주문한 상품이 배송 시 누락됐어요.', '<p>부분 배송으로 일부 상품만 받아보신 경우가 아닌, 구매하신 상품이 누락으로 판단되는 경우<strong> “배송 후 교환/반품” 게시판</strong> 또는<strong> 고객센터로 [070-1234-1234]</strong>로 연락 부탁드립니다!</p><p>물류센터에서 &nbsp;상품 출고 시 녹화되는 CCTV 확인 후 연락드리고 있으며 누락으로 확인되는 경우 재배송 또는 환불이 가능합니다.</p>'); 
insert into faqtable(type, title, content) values('delivery', '부분 배송 가능한가요?', '<p>상품 일부를 부분 배송 원하실 경우에, <strong>배송비 2,500원을</strong> 입금 후 <strong>“배송 전 변경/취소” 게시판</strong>으로 당일 <strong>16:00시 까지</strong> 문의 주시면 확인되는 대로 처리 가능합니다.</p><p>&nbsp;</p><p>-간혹 문의 폭주로 게시글 확인이 늦어질 수 있으며 <strong>배송 작업이 마무리되었을 경우 익일 출고될 수 있는 점 </strong>양해 부탁드립니다.</p><p>-부분 배송 가능한 상품이 없을 경우 입금하신 배송비는 환불 처리될 수 있으며 계좌 정보 요청드리고 있습니다.</p><p>&nbsp;</p><p>[기업 029-000000-00-000 / 예금주:(주)스프링]</p>'); 
insert into faqtable(type, title, content) values('delivery', '도서산간지역은 배송비가 얼마인가요?', '<p>스프링은 5만원 이상 구매 시 무료배송 혜택이 적용됩니다.</p><p>5만원 미만 구매 시<strong> 2,500원 배송비가</strong> 자동 결제됩니다. (도서산간지역 동일)</p>'); 
																						
-- 후기
insert into reviewtable(productno, content, id, image, star) values(1,'<p>처음 입고 됐을 땐 네이비, 색상 추가로 재입고 됐을 땐 다크네이비로 사서 잘 입고 다녔는데&nbsp;</p><p>민트브라운이 계속 아른거리더라구요...재입고 될 때마다 풀렸나 싶어 몇번을 기웃거렸던지요...</p><p>그래서 이번에 추가로 재입고 해주셔서 너무 좋아요!!ㅎㅎ</p>','tester', 'image1.jpg','5');
insert into reviewtable(productno, content, id, image, star) values(1,'<p>이전에 다른 색상으로 구매했었는데 재질도 좋고 착용감도 좋아서 새로 또 구매했어요!&nbsp;</p><p>그레이 색상이라 전체적으로 색감이 다운됐지만, 그 만의 매력이 있어서 마음에 들어요!</p>','portal', 'image1.jpg','4');
insert into reviewtable(productno, content, id, image, star) values(2,'<p>가을에 휘뚜루마뚜루 입으려고 샀어요 검정+보라 스트라이프 사고 싶었는데 라벤더네이비도 충분히 예쁘네요&nbsp;</p><p>너무 오버핏도 아니고 너무 베이직한 핏도 아니라서 딱 맘에 들어요 다른 색으로 더 살 것 같아요 쌩유</p>','portal', 'image2.jpg','4');
insert into reviewtable(productno, content, id, image, star) values(1,'완전 딱 진짜 기본이에요!! 탄탄한 재질은 아니지만 봄에 가볍게 입기 좋을것 같아요!! 그리고 흰색배경이 아니라 색감 진짜 너무 예쁜것같아요 : )','tester', 'image1.jpg','4');
insert into reviewtable(productno, content, id, image, star) values(2,'<p>챠콜이랑 아이보리 고민했었는데 아이보리가 하의를 뭘입어도 다 잘어울릴것같아서 선택했어요!&nbsp;</p><p>핏도 낙낙하고 안에 목티입으니까 완전 제스타일이에요=) 다른색깔도 구매하고 싶네용</p>','tester', 'image2.jpg','5');
insert into reviewtable(productno, content, id, image, star) values(1,'<p>개인적으로 색깔은 솔직히 실망했어요. 예쁜 베이지가 아니라 그냥 황토색? 상세샷보다도 좀 어두워요.</p><p>다른 분들 후기처럼 냄새는 많이 나고 꺼끌거려요. 그리고 무엇보다 털이 진짜.. 어마무시하게 빠집니다.&nbsp;</p><p>안에 검정색 히트텍 입었다가 깜짝 놀랐어요..</p>','portal', 'image1.jpg','1');
insert into reviewtable(productno, content, id, image, star) values(2,'<p>너무 이뻐요 사길 후회 안할정도로 ...팔뚝잇는66인데 이거 입고 55로 보인데요..(엄마 눈에만)&nbsp;</p><p>배송은 좀 느리게 왓더라고요! 제가 사진후기 잘 안올리는데 품절 풀리면 바로사세오...</p>','rhonnyn', 'image2.jpg','4');
insert into reviewtable(productno, content, id, image, star) values(1,'<p>바스락거리는 소재에요! 약간 단작셔츠같은 느낌..! 제가 좋아하는 느낌이에요 히히&nbsp;</p><p>근데 159인 제 키에는 뒷쪽 언발 부분이 엉덩이를 다 가리고도 살짝 더 내려와서&nbsp;</p><p>안그래도 작은 키가 더 작아보이긴 하네요,, 그래도 색감이나 핏 다 너무 예쁘고&nbsp;</p><p>소매쪽 핀턱때문에 포인트도 되고! 마음에 들어요 ´ㅅ`</p>','prose', 'image1.jpg','3');
insert into reviewtable(productno, content, id, image, star) values(2,'<p>편하게 입으려고 샀는데, 후기를 미처 꼼꼼히 못 읽은 것 같아요. 피부 좀 예민하신 분들은 엄청 따갑습니다.&nbsp;</p><p>특히 가운데 지퍼 부분이 까끌해서 피부 엄청 빨갛게 돼요ㅠ 그리고 털이 너무 많이 빠져서 코트에 다 묻습니다.&nbsp;</p><p>예쁘지만 산 것 중에 유일하게 좀 실망한 니트예요ㅠㅠ</p>','madana', 'image2.jpg','2');
insert into reviewtable(productno, content, id, image, star) values(1,'<p>이옷은 제가 산것중 제일 실망입니다,,물론 디자인은 나쁘진 않은데 원단이 좀,,싼티나요 보풀도 많이나고,,</p><p>진짜 믿고 구매하는데 오래기다려서 받았는데 반품이 귀찮아입어요;;</p>','aodremm', 'image1.jpg','1');
insert into reviewtable(productno, content, id, image, star) values(1,'<p>인기가 워낙 많았던 제품이라서 오래 기다려서 받았어요!엄마도 이때까지 산 셔츠중에 이게 가장 예쁘다고 하시네요 ㅎㅎ&nbsp;</p><p>언발 기장으로 떨어지는 게 너무 예쁩니당 얼른 날 풀려서 단독으로 입고 다니고 싶어요ㅠㅠ</p>','portal', 'image1.jpg','5');
insert into reviewtable(productno, content, id, image, star) values(2,'<p>색이 정말 이뻐요! 색상은 화면과 같아요! 다만 구김 옴청 잘 가는 소재 ㅠ&nbsp;</p><p>알고 샀지만 생각보다 더 바스락 거리는 구김 가는 소재에요 ㅎㅎ</p><p>입을때마다 열심히 다리미해야겠어요!</p>','rhonnyn', 'image2.jpg','4');
insert into reviewtable(productno, content, id, image, star) values(1,'<p>거울이 너무 더럽지만 리뷰 써봐용 ㅠㅠ&nbsp;</p><p>교복 같을까봐 걱정했는데 블랙이라 그런 느낌도 덜 해서 다행이고&nbsp;</p><p>길이도 너무 짧지 않고 적당해서 자주 입을 것 같아요</p>','prose', 'image1.jpg','4');
insert into reviewtable(productno, content, id, image, star) values(2,'<p>바스락바스락 얇은 옷이에요. 근데 무엇보다.. 색감이 미쳤어요ㅠㅠㅠㅠ 진짜 너무 예쁩니다.&nbsp;</p><p>유치하고 가벼운 느낌 아니고 차분해요. 핏은 오버핏인데 언발란스한 기장 덕분에 남의 옷 입은 느낌 1도 없고 여리여리해보여요.&nbsp;</p><p>빨리 따뜻해져서 단독으로 입고싶은 옷이에요.ㅠㅠㅠ</p>','madana', 'image2.jpg','4');
insert into reviewtable(productno, content, id, image, star) values(2,'<p>첨에 입을 땐 스판이 아예 없어서 놀랐는데 그만큼 세탁만 잘 하면 오래 입을 수 있을 것 같아 마음에 들어욤 ㅎ-ㅎ&nbsp;</p><p>길이감도 적당합니당 제작 아닌 상품 오랜만에 구매해보는데 만족해요!&nbsp;</p>','tester', 'image1.jpg','4');
insert into reviewtable(productno, content, id, image, star) values(1,'<p>이번에 산 옷들끼리 코디해봤는데 이 스커트 인스타로 봤을 때 부터 이건 사야한다 싶었는데 진짜 너무 이쁘네요ㅜㅜ</p><p>이번 할인구매 물품 중에 1등입니다ㅜㅜ저는 사실 쬐끔만 더 짧았으면 햇는데 딱 안정적으로 이쁜 길이긴 해요ㅎㅎ</p>','aodremm', 'image1.jpg','4');
insert into reviewtable(productno, content, id, image, star) values(2,'<p>요즘 옷들이 작아서 안 맞을까 걱정했는데(ㅠㅠ)&nbsp;</p><p>불편하지 않게 딱 맞아요! 핏도 맘에 들고 만족스러워용^.^</p>','aodremm', 'image2.jpg','5');
insert into reviewtable(productno, content, id, image, star) values(1,'<p>딱 봄 가을에 입기 좋을 얇은 두께입니다 겨울에는 너무 추울 것 같아요&nbsp;</p><p>에스 사이즈로 샀는데 조금 크게 나온 것 같아요</p><p>그래서인지 핏하게 예쁘게 떨어지지는 않아 조금 아쉽습니다 ㅠㅠ</p>','aodremm', 'image1.jpg','4');
insert into reviewtable(productno, content, id, image, star) values(3,'<p>첨에 입을 땐 스판이 아예 없어서 놀랐는데 그만큼 세탁만 잘 하면 오래 입을 수 있을 것 같아 마음에 들어욤 ㅎ-ㅎ&nbsp;</p><p>길이감도 적당합니당 제작 아닌 상품 오랜만에 구매해보는데 만족해요!&nbsp;</p>','tester', 'winter-7046920__340.webp','4');
insert into reviewtable(productno, content, id, image, star) values(4,'<p>이번에 산 옷들끼리 코디해봤는데 이 스커트 인스타로 봤을 때 부터 이건 사야한다 싶었는데 진짜 너무 이쁘네요ㅜㅜ</p><p>이번 할인구매 물품 중에 1등입니다ㅜㅜ저는 사실 쬐끔만 더 짧았으면 햇는데 딱 안정적으로 이쁜 길이긴 해요ㅎㅎ</p>','aodremm', 'flowers-6574079__340.webp','4');
insert into reviewtable(productno, content, id, image, star) values(5,'<p>요즘 옷들이 작아서 안 맞을까 걱정했는데(ㅠㅠ)&nbsp;</p><p>불편하지 않게 딱 맞아요! 핏도 맘에 들고 만족스러워용^.^</p>','aodremm', 'heart-6997703__340.jpg','5');
insert into reviewtable(productno, content, id, image, star) values(6,'<p>딱 봄 가을에 입기 좋을 얇은 두께입니다 겨울에는 너무 추울 것 같아요&nbsp;</p><p>에스 사이즈로 샀는데 조금 크게 나온 것 같아요</p><p>그래서인지 핏하게 예쁘게 떨어지지는 않아 조금 아쉽습니다 ㅠㅠ</p>','aodremm', 'tulip-7045491__340.webp','4');
-- 문의
insert into qnatable(qnaNo, productno, type, originalNo, reply, content, id, secret, image) values(1, 1,'product', 1, true, '<p>이곳은 상품문의를 위한 게시판입니다.<br><br>※게시판 성격에 맞지 않는 내용을 문의주실 경우 처리가 불가할 수 있습니다.<br><br>---------------------------------------------</p><p><br>이 셔츠랑 소프트 민트색이랑 잘어울릴까요?</p>', 'trice', true, 'wardrobe-gf579a32fc_640.jpg');
call autoQuestion(2,'product', false, '<p>이곳은 상품문의를 위한 게시판입니다.<br><br>※게시판 성격에 맞지 않는 내용을 문의주실 경우 처리가 불가할 수 있습니다.<br><br>---------------------------------------------<br>&nbsp;</p><p>실키 블라우스 워터리블루 색상이랑 이 색상이랑 비슷할까요?</p><p>이 색이랑 비슷해보이는데 실제 색상이 이거랑 비슷한지 알고싶어요!!</p>', 'tester',true, 'blue-g19cc294a9_1280.jpg');
call autoReply(1,'productReply', 1, '<p>안녕하세요 고객님!</p><p>네~ 화이트 셔츠라서 어떤 색상이든 잘 어울릴것으로 보입니다~^^</p><p>문의 감사드립니다~!&nbsp;</p><p>spring에서 즐거운 쇼핑되시길 바랍니다 ♥</p><p>&nbsp;</p>', 'spring',true, null);
call autoQuestion(null, 'general', false, '<p>이곳은 일반문의를 위한 게시판입니다.<br>상품과 관련된 문의는 제목을 상품문의로 선택해주세요!<br><br>※게시판 성격에 맞지 않는 내용을 문의주실 경우 처리가 불가할 수 있습니다.<br><br>---------------------------------------------<br>&nbsp;</p><p>모델분 스펙이 어떻게 되나요 ?!&nbsp;</p><p>키랑 몸무게 알고싶어요~~</p>', 'portal',true, null);
call autoQuestion(1,'product', true, '<p>이곳은 상품문의를 위한 게시판입니다.<br><br>※게시판 성격에 맞지 않는 내용을 문의주실 경우 처리가 불가할 수 있습니다.<br><br>---------------------------------------------<br>&nbsp;</p><p>160/55 인데 스노우 버튼 모직스커트 m사이즈 맞을까요?</p><p>160이 m입으면 길이는 어디까지 오나요?</p>', 'rhonia',true, null);
call autoQuestion(2,'product', true, '<p>이곳은 상품문의를 위한 게시판입니다.<br><br>※게시판 성격에 맞지 않는 내용을 문의주실 경우 처리가 불가할 수 있습니다.<br><br>---------------------------------------------<br>&nbsp;</p><p>실키 블라우스랑 스노우 모직 스커트랑 잘어울릴까요?</p>', 'portal',true, null);
call autoReply(1,'productReply', 5, '<p>안녕하세요 고객님!</p><p>165/50정도면 m사이즈 무난하게 잘 맞으실것으로 예상됩니다~^^♥</p><p>길이는 체형에 따라 조금씩 다르기 때문에 정확한 답변은 어려울 것 같습니다ㅜㅜ</p><p>문의 감사드립니다~!&nbsp;</p><p>spring에서 즐거운 쇼핑되시길 바랍니다 ♥</p><p>&nbsp;</p>', 'spring',true, null);
call autoReply(2,'productReply', 6, '<p>안녕하세요 고객님!</p><p> 네!! 실키 여리핏 히든블라우스랑 모직 스노우 스커트는 세트로 잘 팔리는 상품입니다!♥</p><p>문의 감사드립니다~!&nbsp;</p><p>spring에서 즐거운 쇼핑되시길 바랍니다 ♥</p><p>&nbsp;</p>', 'spring',true, null);
call autoQuestion(null, 'general', false, '<p>이곳은 일반문의를 위한 게시판입니다.<br>상품과 관련된 문의는 제목을 상품문의로 선택해주세요!<br><br>※게시판 성격에 맞지 않는 내용을 문의주실 경우 처리가 불가할 수 있습니다.<br><br>---------------------------------------------<br>&nbsp;</p><p> 혹시 spring 쇼룸 있나요? 직접 가서 입어보고 구매하고싶은데 ㅠㅠ</p>', 'madana',true, null);
call autoQuestion(null, 'delivery', false, '<p>★배송전 상품 변경/취소/환불/주소지변경 등 처리는<br>꼭!<strong> &#39;배송전 변경/취소&#39; </strong>게시판에 남겨주세요!★<br><br>---------------------------------------------<br>주문번호: 20292835</p><p>방금 주문했는데 제가 금요일에 꼭 입어야하는데 목요일까지 배송되나요??</p><p>꼭 목요일까지 배송 부탁드려요 ㅜㅜ</p>', 'prose',true, null);
call autoQuestion(null, 'delivery', false, '<p>★배송전 상품 변경/취소/환불/주소지변경 등 처리는<br>꼭!<strong> &#39;배송전 변경/취소&#39; </strong>게시판에 남겨주세요!★<br><br>---------------------------------------------<br>주문번호: 20254327</p><p>제가 3주 전에 주문했는데 아직까지 안와서요..;; 언제쯤 받아볼 수 있을까요??</p>', 'rhonnyn',true, null);
call autoQuestion(null, 'delivery', false, '<p>★배송전 상품 변경/취소/환불/주소지변경 등 처리는<br>꼭!<strong> &#39;배송전 변경/취소&#39; </strong>게시판에 남겨주세요!★<br><br>---------------------------------------------<br>주문번호: 20321124</p><p>배송 출발했다고 3일 전에 문자 받았는데 아직까지도 출고중이라고 되어있는데 누락된거 아닌가요? 확인부탁드립니다.</p>', 'talwyn',true, null);
call autoQuestion(null, 'cancel', false, '<p><br>예치금 환불 시 철회 불가능하며<br>쿠폰/적립금과 중복 사용 불가능합니다.<br>쿠폰은 일회성으로 변심 취소 시 자동삭제/재지급이 불가능합니다.<br>이점 꼭 참고하여 예치금 환불 요청 부탁드리겠습니다:)<br><br>-----------------------------------------------<br>*주문취소*(전체취소시 상품명에  &#39;전체취소&#39; 꼭! 기재해주세요!)<br><br><strong>주문번호 : 12121360</strong><br><strong>상품명(옵션포함기재): 스노우 버튼 모직 스커트 (그레이지, s)</strong><br><strong>※결제시 입금한 이름의 계좌번호※</strong><br><strong>환불계좌번호: 1002-010-0023459</strong><br><strong>은행사 : 우리은행</strong><br><strong>예금주명 : 박나나</strong><br><br>♥해당 양식에 정확한 상품명 남겨주셔야 처리가 가능합니다♥</p>', 'portal',true, null);
call autoReply(null, 'cancelReply', 14, '<p>안녕하세요 고객님!</p><p> 주문 취소 완료되었습니다~! </p><p>문의 감사드립니다~!&nbsp;</p><p>spring에서 즐거운 쇼핑되시길 바랍니다 ♥</p><p>&nbsp;</p>', 'spring',true, null);
call autoQuestion(null, 'cancel', false, '<p><br>예치금 환불 시 철회 불가능하며<br>쿠폰/적립금과 중복 사용 불가능합니다.<br>쿠폰은 일회성으로 변심 취소 시 자동삭제/재지급이 불가능합니다.<br>이점 꼭 참고하여 예치금 환불 요청 부탁드리겠습니다:)<br><br>-----------------------------------------------<br>*주문취소*(전체취소시 상품명에  &#39;전체취소&#39; 꼭! 기재해주세요!)<br><br><strong>주문번호 : 23456323</strong><br><strong>상품명(옵션포함기재): 전체 취소</strong><br><strong>※결제시 입금한 이름의 계좌번호※</strong><br><strong>환불계좌번호: 235-021-087159</strong><br><strong>은행사 : 국민은행</strong><br><strong>예금주명 : 김주영</strong><br><br>♥해당 양식에 정확한 상품명 남겨주셔야 처리가 가능합니다♥</p>', 'talwyn',true, null);
call autoQuestion(null, 'change', false, '<p>♥해당 양식에 정확한 상품명을 기재해주셔야 처리가 가능합니다♥<br><br>예치금 환불 시 철회 불가능하며<br>쿠폰/적립금과 중복 사용 불가능합니다.<br>쿠폰은 일회성으로 변심 취소 시 자동삭제/재지급이 불가능합니다.<br>이점 꼭 참고하여 예치금 환불 요청 부탁드리겠습니다:)<br><br>-----------------------------------------------<br>*상품변경*<br><br>주문번호 :<br>변경전 상품명 (사이즈,컬러) : 스노우 버튼 모직 스커트(s, 그레이지)<br>변경후 상품명 (사이즈,컬러) : 스노우 버트 모직 스커트(m, 그레이지)</p>', 'wantin',true, null);
call autoQuestion(null, 'changeAddress', false, '<p>♥해당 양식에 정확한 주문번호를 기재해주셔야 처리가 가능합니다♥<br><br><br>-----------------------------------------------<br>*주소지 변경*<br><br>주문번호 : 101284623<br>변경 주소지(번지수포함) : 서울 강남구 언주로 508 서울상록빌딩</p>', 'madana', true, null);
call autoQuestion(null, 'exchange', false, '<p>♥해당 양식에 정확한 정보를 기재해주셔야 처리가 가능합니다♥<br><br><br>예치금 환불 시 철회 불가능하며<br>쿠폰/적립금과 중복 사용 불가능합니다.<br>쿠폰은 일회성으로 변심 취소 시 자동삭제/재지급이 불가능합니다.<br>이점 꼭 참고하여 예치금 환불 요청 부탁드리겠습니다:)<br><br>--------------------------------------<br>*교환*<br><br><strong>주문번호 : 120984323</strong><br><strong>교환전 상품명(사이즈,컬러) : 비프리 자켓(아이보리)</strong><br><strong>교환후 상품명(사이즈,컬러) : 비프리 자켓(블랙)</strong><br><strong>왕복 배송비 입금자명/입금날짜 : 주현영/ 2022-03-07</strong><br><br>(수령주소지로 자동 회수접수)<br>※회수/교환상품수령지 변경 원하실 경우에만 새주소지 함께 기재해 주세요.<br><br>회수주소: 서울 강남구 대치동 889-41<br>교환상품 수령 주소: 서울 강남구 대치동 889-41<br>&nbsp;</p>', 'tester', true, null);
call autoQuestion(null, 'return', false, '<p>♥해당 양식에 정확한 정보를 기재해주셔야 처리가 가능합니다♥<br><br><br>예치금 환불 시 철회 불가능하며<br>쿠폰/적립금과 중복 사용 불가능합니다.<br>쿠폰은 일회성으로 변심 취소 시 자동삭제/재지급이 불가능합니다.<br>이점 꼭 참고하여 예치금 환불 요청 부탁드리겠습니다:)<br><br>--------------------------------------<br>*반품*<br><br><strong>주문번호 : 546787654</strong><br><strong>상품명(사이즈,컬러): 스노우 버튼 모직 스커트(m, 소프트민트)</strong><br><strong>반품 사유 : 사이즈가 안맞고 색상도 화면이랑 많이 다르네요</strong><br><strong>※결제시 입금한 이름의 계좌번호※</strong><br><strong>환불계좌번호(은행사포함) : 123-36777-33-562</strong><br><strong>예금주명 : 이나라</strong><br><br>(수령주소지로 자동 회수접수)<br>※회수주소 변경 원하실 경우에만 새주소지와 함께 기재해 주세요.<br><br>회수주소: 서울 강남구 테헤란로 212<br><br>--------------------------------------<br>&nbsp;</p>', 'rhonia', true,null);
call autoQuestion(null, 'error', false, '<p>♥해당 양식에 정확한 정보를 기재해주셔야 처리가 가능합니다♥<br><br>--------------------------------------<br>*불량/오배송*<br><br><strong>▷바코드(검수완료)사진</strong><br><strong>▷불량사진</strong><br><strong>(필수첨부 부탁드립니다!)</strong><br><br>주문번호 : 102319532<br>교환/반품 (원하시는 처리 선택해주세요!) : 반품<br>상품 수령일자 : (3/2)<br>반품 접수일자 : (3/7)<br>상품명(사이즈,컬러) : 스노우 버튼 모직 스커트(m, 그레이지)<br>불량/오배송 사유 : 스커트 단추가 불량이네요<br>검수번호(숫자나 알파벳) : 123ew2<br><br>(상품바코드옆 검수자 숫자한자리/두자리를 기재합니다.)</p>','portal', true, 'bar-code-g042121347_640.png;jeans-ga6ae8c0ef_640.jpg;');

-- 배너
 insert into bannertable(image, link, num) values('b001.png','', 1); 
 insert into bannertable(image, link, num) values('b002.png','/productList/pants/denim',2); 
insert into bannertable(image, link, num) values('b003.png','',3); 
insert into bannertable(image, link, num) values('b004.png','',4); 

insert into bannertable(image, link, num) values('b006.png','',5); 
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
select * from information_schema.events;
-- select문 실험 및 용도

-- 글 번호는 최신순이지만 답글이 원글 밑에 오도록 함
-- select * from qnatable order by originalno desc, qnano asc;

-- 많이 팔린 순으로 정렬
-- select productno, sum(amount) from ordertable group by productno order by sum(amount) desc;
-- select * from producttable left join ordertable on producttable.productno = ordertable.productno where type1 = 'skirt' group by ordertable.productno order by sum(ordertable.amount) desc limit 0,8;

-- select * from producttable p left join ordertable o on p.productNo = o.productNo where p.onSale = true and p.amount > 0 and (orderDate BETWEEN DATE_ADD(NOW(), INTERVAL -1 week) AND NOW()) group by o.productNo order by sum(o.orderAmount) desc limit 8
-- select sum(o.totalPrice) as priceSum, o.productNo, p.productName from ordertable o left join producttable p on p.productNo = o.productNo group by o.productNo having sum(o.totalPrice) >= 76000;
