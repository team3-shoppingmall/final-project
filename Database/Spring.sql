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
DROP procedure IF EXISTS `autoQuestion`;
DROP procedure IF EXISTS `autoReply`;
DROP procedure IF EXISTS `orderChangeSchedule`;
DROP EVENT IF EXISTS `event_AutoScheduler`;

-- create table and trigger
CREATE TABLE membertable (
	ID VARCHAR(50) PRIMARY KEY,
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
if(new.ORDERMETHOD = 'cash')
then
	set new.state = '입금전';
else
	set new.state = '결제완료';
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
	IMAGE VARCHAR(400),
    CONSTRAINT qna_fk_id FOREIGN KEY (ID) REFERENCES membertable (ID)
        on delete cascade
        on update cascade
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
	NUM INT PRIMARY KEY AUTO_INCREMENT,
	IMAGE VARCHAR(100) NOT NULL,
	LINK VARCHAR(100) NOT NULL
);
-- insert data
-- 회원
insert into membertable values('spring','Asdqwe123','Spring','0212345678','spring@gmail.com','12345','서울 강남구 테헤란로 212 (멀티캠퍼스)','2층 201호',false,null,'ROLE_ADMIN');
insert into membertable values('tester','Asdqwe123','유저','01098765432','user@gmail.com','54321','부산 남구 문현로 56-1 (네이버코리아)','5층 502호',false,null,'ROLE_USER');
insert into membertable values('tester1','Asdqwe123','유저2','01045614561','user2@gmail.com','24241','부산 문현로 56-1 (네이버코리아)','4층 405호',false,null,'ROLE_USER');
insert into membertable values('tester2','Asdqwe123','유저2','01045614561','user2@gmail.com','24241','부산 문현로 56-1 (네이버코리아)','4층 405호',false,null,'ROLE_USER');
insert into membertable values('tester3','Asdqwe123','유저2','01045614561','user2@gmail.com','24241','부산 문현로 56-1 (네이버코리아)','4층 405호',false,null,'ROLE_USER');
insert into membertable values('tester4','Asdqwe123','유저2','01045614561','user2@gmail.com','24241','부산 문현로 56-1 (네이버코리아)','4층 405호',false,null,'ROLE_USER');
insert into membertable values('tester5','Asdqwe123','유저2','01045614561','user2@gmail.com','24241','부산 문현로 56-1 (네이버코리아)','4층 405호',false,null,'ROLE_USER');
insert into membertable values('tester6','Asdqwe123','유저2','01045614561','user2@gmail.com','24241','부산 문현로 56-1 (네이버코리아)','4층 405호',false,null,'ROLE_USER');
insert into membertable values('tester7','Asdqwe123','유저2','01045614561','user2@gmail.com','24241','부산 문현로 56-1 (네이버코리아)','4층 405호',false,null,'ROLE_USER');
insert into membertable values('tester8','Asdqwe123','유저2','01045614561','user2@gmail.com','24241','부산 문현로 56-1 (네이버코리아)','4층 405호',false,null,'ROLE_USER');
insert into membertable values('tester9','Asdqwe123','유저2','01045614561','user2@gmail.com','24241','부산 문현로 56-1 (네이버코리아)','4층 405호',false,null,'ROLE_USER');
insert into membertable values('tester10','Asdqwe123','유저2','01045614561','user2@gmail.com','24241','부산 문현로 56-1 (네이버코리아)','4층 405호',false,null,'ROLE_USER');
insert into membertable values('tester11','Asdqwe123','유저2','01045614561','user2@gmail.com','24241','부산 문현로 56-1 (네이버코리아)','4층 405호',false,null,'ROLE_USER');
-- 상품
insert into producttable(productname, type1, type2, imagename, price, color, size, amount, regDate, detailimagename, onSale) 
values('스노우 버튼 모직스커트', 'skirt','mini','nature-3082832__480.jpg;photo-1433086966358-54859d0ed716.jfif',38000,'그레이지;소프트민트','S;M;L', 100, '2022-01-01 09:00:00', 'photo-1447752875215-b2761acb3c5d.jfif;photo-1469474968028-56623f02e42e.jfif', true);
insert into producttable(productname, type1, type2, imagename, price, discount, color, size, amount, regDate, detailimagename, onSale)
values('실키 여리핏 히든블라우스', 'top','blouse','photo-1465146344425-f00d5f5c8f07.jfif;photo-1426604966848-d7adac402bff.jfif',34900,5000,'아이보리;피치베이지;워터리블루;블랙',null, 100,'2022-01-02 10:00:00', 'photo-1475924156734-496f6cac6ec1.jfif;photo-1586348943529-beaae6c28db9.jfif', true);
insert into producttable(productname, type1, type2, imagename, price, discount, color, size, amount, regDate,  detailimagename, onSale)
values('비프리 자켓', 'outer','jacket','tourist-attraction-7037967__340.jpg',92000,0,'아이보리;블랙',null, 100,'2022-01-05 13:24:51','tree-6792528__340.jpg', true);

-- 장바구니
insert into baskettable(id, productno, selectedcolor, selectedsize, basketAmount) values('tester',1,'소프트민트','S',2);
insert into baskettable(id, productno, selectedcolor, selectedsize, basketAmount) values('tester',2,'아이보리',null,5);
-- 관심상품
insert into wishlisttable(id, productno) values('tester', 1);
insert into wishlisttable(id, productno) values('tester', 2);
-- 주문
insert into ordertable(id, productno, orderno, selectedcolor, selectedsize, orderAmount, totalprice, ordermethod, name, tel, zipcode, address, detailaddr)
values('tester',1,1,'그레이지','S',1,38000,'cash','유저','01098765432','54321','부산 남구 문현로 56-1 (네이버코리아)','5층 502호');
insert into ordertable(id, productno, orderno, selectedcolor, selectedsize, orderAmount, totalprice, ordermethod, name, tel, zipcode, address, detailaddr)
values('tester2',1,2,'소프트민트','M',1,38000,'credit','유저2','01045614561','54321','부산 남구 문현로 56-1 (네이버코리아)','5층 502호');
insert into ordertable(id, productno, orderno, selectedcolor, selectedsize, orderAmount, totalprice, ordermethod, name, tel, zipcode, address, detailaddr)
values('tester2',2,2,'피치베이지',null,4,119600,'credit','유저2','01045614561','24241','부산 문현로 56-1 (네이버코리아)','4층 405호');
-- 포인트 내역
insert into pointtable(id, point, content) values ('tester',-2000, '상품 구매');
insert into pointtable(id, point, content) values ('tester',500, '구매 확정');
-- 공지사항
insert into noticetable(title, content, id, image) values("test1", "content1", "spring", "test1.png");
insert into noticetable(title, content, id, image) values("test2", "content2", "spring", "test2.png");
insert into noticetable(title, content, id, image) values("test3", "content3", "spring", "test3.png");
insert into noticetable(title, content, id, image) values("test1", "content1", "spring", "test1.png");
insert into noticetable(title, content, id, image) values("test2", "content2", "spring", "test2.png");
insert into noticetable(title, content, id, image) values("test3", "content3", "spring", "test3.png");
insert into noticetable(title, content, id, image) values("test1", "content1", "spring", "test1.png");
insert into noticetable(title, content, id, image) values("test2", "content2", "spring", "test2.png");
insert into noticetable(title, content, id, image) values("test3", "content3", "spring", "test3.png");
insert into noticetable(title, content, id, image) values("test1", "content1", "spring", "test1.png");
insert into noticetable(title, content, id, image) values("test2", "content2", "spring", "test2.png");
insert into noticetable(title, content, id, image) values("test3", "content3", "spring", "test3.png");
insert into noticetable(title, content, id, image) values("test1", "content1", "spring", "test1.png");
insert into noticetable(title, content, id, image) values("test2", "content2", "spring", "test2.png");
insert into noticetable(title, content, id, image) values("test3", "content3", "spring", "test3.png");
insert into noticetable(title, content, id, image) values("test1", "content1", "spring", "test1.png");
insert into noticetable(title, content, id, image) values("test2", "content2", "spring", "test2.png");
insert into noticetable(title, content, id, image) values("test3", "content3", "spring", "test3.png");
insert into noticetable(title, content, id, image) values("test1", "content1", "spring", "test1.png");
insert into noticetable(title, content, id, image) values("test2", "content2", "spring", "test2.png");
insert into noticetable(title, content, id, image) values("test3", "content3", "spring", "test3.png");
insert into noticetable(title, content, id, image) values("test1", "content1", "spring", "test1.png");
insert into noticetable(title, content, id, image) values("test2", "content2", "spring", "test2.png");

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
insert into faqtable(type, title, content) values('delivery', '무료배송 조건은 어떻게 되나요?', '<p><strong>7만원 이상</strong> 구매시 무료배송에 적용됩니다.</p>
                                                                                          <p>교환/반품 시 최종 결제하시는 상품이 7만원 미만이 될 경우 혜택에 해당되지 않아 초기 배송비가 함께 차감되어 환불 처리됩니다.</p>');
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
                                                                                            <p>7만원 이하 구매 → 반품상품 금액에서 -2,500원 차감</p><p>7만원 이상 구매 → 반품상품 금액에서 -5,000원 차감 (초기 무료배송 혜택 적용된 경우)</p><p>–반품 후에도 나머지 구입하신 금액이 7만원 이상일 경우 -2,500원만 차감됩니다.</p>'); 
insert into faqtable(type, title, content) values('return', '상품을 받아봤는데 오배송/불량상품이 왔어요.', '<p>“배송 후 교환/반품” 게시판에 <strong>사진을 필수 첨부하여 문의해 주세요.</strong></p><p>정확한 처리를 위하여 불량/오배송 확인이 가능한 사진이 필요합니다.</p><p>불량/오배송 상품의 경우 스프링 왕복택배비(5,000원) 부담으로</p><p>신속하게 교환 처리 도와드리고 있습니다!</p><p>&nbsp;</p>
                                                                                                 <p><strong>교환</strong> - 스프링 왕복 배송비 부담으로 교환 처리.</p><p><strong>반품</strong> - 반품 배송비 스프링 부담으로 처리.</p><p>&nbsp;</p>
																								 <p>교환이 아닌 환불 원하실 경우, &nbsp;7만원이상 무료배송 혜택을 받아보시고(무료배송쿠폰 포함) 환불로 인해 혜택이 취소되는 경우, 초기 배송비(2,500원) 차감되어 환불될 수 있습니다.</p><p>&nbsp;</p>
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
insert into faqtable(type, title, content) values('product', '신상품 할인 기간을 알고 싶어요.', '<p>신상품 업데이트 후 <strong>3일 동안 5% 할인된 </strong>가격으로 판매됩니다.</p>'); 
insert into faqtable(type, title, content) values('delivery', '구매한 상품 입고 날짜를 알고싶어요.', '<p>스프링은 재고를 미리 확보해 두려고 노력하고 있습니다.</p><p>결제 완료된 상품들은 순차적으로 출고되며 기본 영업일 기준 1 - 2일에서 길어지면 3 - 4일 소요됩니다.</p><p>&nbsp;</p><p>사전에 상품명/상세페이지 내<strong> [주문폭주/입고지연/제작지연]</strong>으로 안내되어 있는 경우, 상품에 따라 영업일 기준 <strong>5일에서 최대 14일 이상 준비 기간이 필요할 수 있습니다.</strong></p><p>&nbsp;</p><p>매일 오전 <strong>[DELAY] 입고지연 게시판에</strong> 당일 입고 내역을 공지하고 있으니 참고해주세요. 구매하신 상품이 당일 입고되었어도 결제한 순번대로 출고되어 출고 불가할 수 있는 점 양해 부탁드립니다.</p>'); 
insert into faqtable(type, title, content) values('etc', '주문 후 언제까지 입급해야 하나요?', '<p>무통장입금 등 현금으로 결제해 주시는 경우 7일 내 입금확인이 되지 않을 때 주문 건은 자동 취소 처리됩니다.</p>'); 
insert into faqtable(type, title, content) values('etc', '콜센터 전화연결이 안돼요.', '<p>업무가 시작되는 월요일, 긴 공휴일 이후 또는 이벤트 직후, 문의 폭주로 인해 콜센터 연결이 지연될 수 있습니다.</p><p>불편하시더라도 이점 너그럽게 양해 부탁드리며 성격에 맞는 게시판에 문의 주시면 최대한 빠른 처리 도와드릴 수 있도록 하겠습니다.</p><p>&nbsp;</p><p><strong>[고객센터 운영시간 / 070-1234-1234]</strong></p><p>am 10:00 - pm 17:00</p><p>BREAK TIME : pm 12:00 - pm 13:00(sat / sun / holiday OFF)</p>'); 
insert into faqtable(type, title, content) values('delivery', '주문한 상품이 배송 시 누락됐어요.', '<p>부분 배송으로 일부 상품만 받아보신 경우가 아닌, 구매하신 상품이 누락으로 판단되는 경우<strong> “배송 후 교환/반품” 게시판</strong> 또는<strong> 고객센터로 [070-1234-1234]</strong>로 연락 부탁드립니다!</p><p>물류센터에서 &nbsp;상품 출고 시 녹화되는 CCTV 확인 후 연락드리고 있으며 누락으로 확인되는 경우 재배송 또는 환불이 가능합니다.</p>'); 
insert into faqtable(type, title, content) values('delivery', '부분 배송 가능한가요?', '<p>상품 일부를 부분 배송 원하실 경우에, <strong>배송비 2,500원을</strong> 입금 후 <strong>“배송 전 변경/취소” 게시판</strong>으로 당일 <strong>15:00시 까지</strong> 문의 주시면 확인되는 대로 처리 가능합니다.</p><p>&nbsp;</p><p>-간혹 문의 폭주로 게시글 확인이 늦어질 수 있으며 <strong>배송 작업이 마무리되었을 경우 익일 출고될 수 있는 점 </strong>양해 부탁드립니다.</p><p>-부분 배송 가능한 상품이 없을 경우 입금하신 배송비는 환불 처리될 수 있으며 계좌 정보 요청드리고 있습니다.</p><p>&nbsp;</p><p>[기업 029-000000-00-000 / 예금주:(주)스프링]</p>'); 
insert into faqtable(type, title, content) values('delivery', '도서산간지역은 배송비가 얼마인가요?', '<p>스프링은 7만원 이상 구매 시 무료배송 혜택이 적용됩니다.</p><p>7만원 미만 구매 시<strong> 2,500원 배송비가</strong> 자동 결제됩니다. (도서산간지역 동일)</p>'); 
																						
-- 후기
insert into reviewtable(productno, content, id, image, star) values(1,'처음 입고 됐을 땐 네이비, 색상 추가로 재입고 됐을 땐 다크네이비로 사서 잘 입고 다녔는데 민트브라운이 계속 아른거리더라구요...재입고 될 때마다 풀렸나 싶어 몇번을 기웃거렸던지요...그래서 이번에 추가로 재입고 해주셔서 너무 좋아요!!ㅎㅎ','tester', 'image1.jpg','5');
insert into reviewtable(productno, content, id, image, star) values(1,'이전에 다른 색상으로 구매했었는데 재질도 좋고 착용감도 좋아서 새로 또 구매했어요! 그레이 색상이라 전체적으로 색감이 다운됐지만, 그 만의 매력이 있어서 마음에 들어요!','tester2', 'image1.jpg','4');
insert into reviewtable(productno, content, id, image, star) values(2,'가을에 휘뚜루마뚜루 입으려고 샀어요 검정+보라 스트라이프 사고 싶었는데 라벤더네이비도 충분히 예쁘네요 너무 오버핏도 아니고 너무 베이직한 핏도 아니라서 딱 맘에 들어요 다른 색으로 더 살 것 같아요 쌩유','tester2', 'image2.jpg','4');
insert into reviewtable(productno, content, id, image, star) values(1,'완전 딱 진짜 기본이에요!! 탄탄한 재질은 아니지만 봄에 가볍게 입기 좋을것 같아요!! 그리고 흰색배경이 아니라 색감 진짜 너무 예쁜것같아요 : )','tester', 'image1.jpg','4');
insert into reviewtable(productno, content, id, image, star) values(2,'챠콜이랑 아이보리 고민했었는데 아이보리가 하의를 뭘입어도 다 잘어울릴것같아서 선택했어요! 핏도 낙낙하고 안에 목티입으니까 완전 제스타일이에요=) 다른색깔도 구매하고 싶네용','tester', 'image2.jpg','5');
insert into reviewtable(productno, content, id, image, star) values(1,'개인적으로 색깔은 솔직히 실망했어요. 예쁜 베이지가 아니라 그냥 황토색? 상세샷보다도 좀 어두워요.다른 분들 후기처럼 냄새는 많이 나고 꺼끌거려요. 그리고 무엇보다 털이 진짜.. 어마무시하게 빠집니다. 안에 검정색 히트텍 입었다가 깜짝 놀랐어요..','tester1', 'image1.jpg','1');
insert into reviewtable(productno, content, id, image, star) values(2,'너무 이뻐요 사길 후회 안할정도로 ...팔뚝잇는66인데 이거 입고 55로 보인데요..(엄마 눈에만) 배송은 좀 느리게 왓더라고요! 제가 사진후기 잘 안올리는데 품절 풀리면 바로사세오...','tester2', 'image2.jpg','4');
insert into reviewtable(productno, content, id, image, star) values(1,'바스락거리는 소재에요! 약간 단작셔츠같은 느낌..! 제가 좋아하는 느낌이에요 히히 근데 159인 제 키에는 뒷쪽 언발 부분이 엉덩이를 다 가리고도 살짝 더 내려와서 안그래도 작은 키가 더 작아보이긴 하네요,, 그래도 색감이나 핏 다 너무 예쁘고 소매쪽 핀턱때문에 포인트도 되고! 마음에 들어요 ´ㅅ`','tester3', 'image1.jpg','3');
insert into reviewtable(productno, content, id, image, star) values(2,'편하게 입으려고 샀는데, 후기를 미처 꼼꼼히 못 읽은 것 같아요. 피부 좀 예민하신 분들은 엄청 따갑습니다. 특히 가운데 지퍼 부분이 까끌해서 피부 엄청 빨갛게 돼요ㅠ 그리고 털이 너무 많이 빠져서 코트에 다 묻습니다. 예쁘지만 산 것 중에 유일하게 좀 실망한 니트예요ㅠㅠ','tester4', 'image2.jpg','2');
insert into reviewtable(productno, content, id, image, star) values(1,'이옷은 제가 산것중 제일 실망입니다,,물론 디자인은 나쁘진 않은데 원단이 좀,,싼티나요 보풀도 많이나고,,진짜 믿고 구매하는데 오래기다려서 받았는데 반품이 귀찮아입어요;;','tester5', 'image1.jpg','1');
insert into reviewtable(productno, content, id, image, star) values(1,'인기가 워낙 많았던 ㅔ품이라서 오래 기다려서 받았어요!엄마도 이때까지 산 셔츠중에 이게 가장 예쁘다고 하시네요 ㅎㅎ 언발 기장으로 떨어지는 게 너무 예쁩니당 얼른 날 풀려서 단독으로 입고 다니고 싶어요ㅠㅠ','tester1', 'image1.jpg','5');
insert into reviewtable(productno, content, id, image, star) values(2,'색이 정말 이뻐요! 색상은 화면과 같아요! 다만 구김 옴청 잘 가는 소재 ㅠ 알고 샀지만 생각보다 더 바스락 거리는 구김 가는 소재에요 ㅎㅎ입을때마다 열심히 다리미해야겠어요!','tester2', 'image2.jpg','4');
insert into reviewtable(productno, content, id, image, star) values(1,'거울이 너무 더럽지만 리뷰 써봐용 ㅠㅠ 교복 같을까봐 걱정했는데 블랙이라 그런 느낌도 덜 해서 다행이고 길이도 너무 짧지 않고 적당해서 자주 입을 것 같아요','tester3', 'image1.jpg','4');
insert into reviewtable(productno, content, id, image, star) values(2,'바스락바스락 얇은 옷이에요. 근데 무엇보다.. 색감이 미쳤어요ㅠㅠㅠㅠ 진짜 너무 예쁩니다. 유치하고 가벼운 느낌 아니고 차분해요. 핏은 오버핏인데 언발란스한 기장 덕분에 남의 옷 입은 느낌 1도 없고 여리여리해보여요. 빨리 따뜻해져서 단독으로 입고싶은 옷이에요.ㅠㅠㅠ','tester4', 'image2.jpg','4');
-- 문의
-- insert into qnatable(qnaNo, productno, type, originalNo, reply, content, id, secret, image) values(1, 1,'product', 1, true, '질문 내용', 'tester', true, 'image1.jpg');
-- call autoQuestion(2,'product', false, '질문 내용', 'tester',true, 'image1.jpg');
-- call autoReply(1,'productReply', 1, '답변 내용', 'spring',true, 'image1.jpg');
-- call autoQuestion(null, 'general', false, '질문 내용', 'tester',true, 'image1.jpg');
-- call autoQuestion(1,'product', true, '<p>이곳은 상품문의를 위한 게시판입니다.<br><br>※게시판 성격에 맞지 않는 내용을 문의주실 경우 처리가 불가할 수 있습니다.<br><br>---------------------------------------------<br>&nbsp;</p><p>160/55 인데 스노우 버튼 모직스커트 m사이즈 맞을까요?</p><p>160이 m입으면 길이는 어디까지 오나요?</p>', 'tester',true, null);
-- call autoQuestion(2,'product', true, '질문 내용', 'tester2',true, 'image1.jpg');
-- call autoReply(1,'productReply', 5, '답변 내용', 'spring',true, null);
-- call autoReply(2,'productReply', 6, '답변 내용', 'spring',true, null);
-- call autoQuestion(null, 'general', false, '질문 내용', 'tester',true, 'image1.jpg');
-- call autoQuestion(2,'product', false, '질문 내용', 'tester2',true, 'image1.jpg');
-- call autoQuestion(null, 'delivery', false, '질문 내용', 'tester2',true, 'image1.jpg');
-- call autoQuestion(null, 'cancel', false, '질문 내용', 'tester2',true, 'image1.jpg');
-- call autoQuestion(null, 'exchange', false, '질문 내용', 'tester2',true, 'image1.jpg');

insert into qnatable(qnaNo, productno, type, originalNo, reply, content, id, secret, image) values(1, 1,'product', 1, true, '<p>이곳은 상품문의를 위한 게시판입니다.<br><br>※게시판 성격에 맞지 않는 내용을 문의주실 경우 처리가 불가할 수 있습니다.<br><br>---------------------------------------------</p><p><br>이 셔츠랑 소프트 민트색이랑 잘어울릴까요?</p>', 'tester1', true, 'wardrobe-gf579a32fc_640.jpg');
call autoQuestion(2,'product', false, '<p>이곳은 상품문의를 위한 게시판입니다.<br><br>※게시판 성격에 맞지 않는 내용을 문의주실 경우 처리가 불가할 수 있습니다.<br><br>---------------------------------------------<br>&nbsp;</p><p>실키 블라우스 워터리블루 색상이랑 이 색상이랑 비슷할까요?</p><p>이 색이랑 비슷해보이는데 실제 색상이 이거랑 비슷한지 알고싶어요!!</p>', 'tester',true, 'blue-g19cc294a9_1280.jpg');
call autoReply(1,'productReply', 1, '<p>안녕하세요 고객님!</p><p>네~ 화이트 셔츠라서 어떤 색상이든 잘 어울릴것으로 보입니다~^^</p><p>문의 감사드립니다~!&nbsp;</p><p>spring에서 즐거운 쇼핑되시길 바랍니다 ♥</p><p>&nbsp;</p>', 'spring',true, null);
call autoQuestion(null, 'general', false, '<p>이곳은 일반문의를 위한 게시판입니다.<br>상품과 관련된 문의는 제목을 상품문의로 선택해주세요!<br><br>※게시판 성격에 맞지 않는 내용을 문의주실 경우 처리가 불가할 수 있습니다.<br><br>---------------------------------------------<br>&nbsp;</p><p>모델분 스펙이 어떻게 되나요 ?!&nbsp;</p><p>키랑 몸무게 알고싶어요~~</p>', 'tester2',true, null);
call autoQuestion(1,'product', true, '<p>이곳은 상품문의를 위한 게시판입니다.<br><br>※게시판 성격에 맞지 않는 내용을 문의주실 경우 처리가 불가할 수 있습니다.<br><br>---------------------------------------------<br>&nbsp;</p><p>160/55 인데 스노우 버튼 모직스커트 m사이즈 맞을까요?</p><p>160이 m입으면 길이는 어디까지 오나요?</p>', 'tester5',true, null);
call autoQuestion(2,'product', true, '<p>이곳은 상품문의를 위한 게시판입니다.<br><br>※게시판 성격에 맞지 않는 내용을 문의주실 경우 처리가 불가할 수 있습니다.<br><br>---------------------------------------------<br>&nbsp;</p><p>실키 블라우스랑 스노우 모직 스커트랑 잘어울릴까요?</p>', 'tester2',true, null);
call autoReply(1,'productReply', 5, '<p>안녕하세요 고객님!</p><p>165/50정도면 m사이즈 무난하게 잘 맞으실것으로 예상됩니다~^^♥</p><p>길이는 체형에 따라 조금씩 다르기 때문에 정확한 답변은 어려울 것 같습니다ㅜㅜ</p><p>문의 감사드립니다~!&nbsp;</p><p>spring에서 즐거운 쇼핑되시길 바랍니다 ♥</p><p>&nbsp;</p>', 'spring',true, null);
call autoReply(2,'productReply', 6, '<p>안녕하세요 고객님!</p><p> 네!! 실키 여리핏 히든블라우스랑 모직 스노우 스커트는 세트로 잘 팔리는 상품입니다!♥</p><p>문의 감사드립니다~!&nbsp;</p><p>spring에서 즐거운 쇼핑되시길 바랍니다 ♥</p><p>&nbsp;</p>', 'spring',true, null);
call autoQuestion(null, 'general', false, '<p>이곳은 일반문의를 위한 게시판입니다.<br>상품과 관련된 문의는 제목을 상품문의로 선택해주세요!<br><br>※게시판 성격에 맞지 않는 내용을 문의주실 경우 처리가 불가할 수 있습니다.<br><br>---------------------------------------------<br>&nbsp;</p><p> 혹시 spring 쇼룸 있나요? 직접 가서 입어보고 구매하고싶은데 ㅠㅠ</p>', 'tester3',true, null);
call autoQuestion(null, 'delivery', false, '<p>★배송전 상품 변경/취소/환불/주소지변경 등 처리는<br>꼭!<strong> &#39;배송전 변경/취소&#39; </strong>게시판에 남겨주세요!★<br><br>---------------------------------------------<br>주문번호: 20292835</p><p>방금 주문했는데 제가 금요일에 꼭 입어야하는데 목요일까지 배송되나요??</p><p>꼭 목요일까지 배송 부탁드려요 ㅜㅜ</p>', 'tester4',true, null);
call autoQuestion(null, 'delivery', false, '<p>★배송전 상품 변경/취소/환불/주소지변경 등 처리는<br>꼭!<strong> &#39;배송전 변경/취소&#39; </strong>게시판에 남겨주세요!★<br><br>---------------------------------------------<br>주문번호: 20254327</p><p>제가 3주 전에 주문했는데 아직까지 안와서요..;; 언제쯤 받아볼 수 있을까요??</p>', 'tester6',true, null);
call autoQuestion(null, 'delivery', false, '<p>★배송전 상품 변경/취소/환불/주소지변경 등 처리는<br>꼭!<strong> &#39;배송전 변경/취소&#39; </strong>게시판에 남겨주세요!★<br><br>---------------------------------------------<br>주문번호: 20321124</p><p>배송 출발했다고 3일 전에 문자 받았는데 아직까지도 출고중이라고 되어있는데 누락된거 아닌가요? 확인부탁드립니다.</p>', 'tester7',true, null);
call autoQuestion(null, 'cancel', false, '<p><br>예치금 환불 시 철회 불가능하며<br>쿠폰/적립금과 중복 사용 불가능합니다.<br>쿠폰은 일회성으로 변심 취소 시 자동삭제/재지급이 불가능합니다.<br>이점 꼭 참고하여 예치금 환불 요청 부탁드리겠습니다:)<br><br>-----------------------------------------------<br>*주문취소*(전체취소시 상품명에  &#39;전체취소&#39; 꼭! 기재해주세요!)<br><br><strong>주문번호 : 12121360</strong><br><strong>상품명(옵션포함기재): 스노우 버튼 모직 스커트 (그레이지, s)</strong><br><strong>※결제시 입금한 이름의 계좌번호※</strong><br><strong>환불계좌번호: 1002-010-0023459</strong><br><strong>은행사 : 우리은행</strong><br><strong>예금주명 : 박나나</strong><br><br>♥해당 양식에 정확한 상품명 남겨주셔야 처리가 가능합니다♥</p>', 'tester2',true, null);
call autoReply(null, 'cancelReply', 14, '<p>안녕하세요 고객님!</p><p> 주문 취소 완료되었습니다~! </p><p>문의 감사드립니다~!&nbsp;</p><p>spring에서 즐거운 쇼핑되시길 바랍니다 ♥</p><p>&nbsp;</p>', 'spring',true, null);
call autoQuestion(null, 'cancel', false, '<p><br>예치금 환불 시 철회 불가능하며<br>쿠폰/적립금과 중복 사용 불가능합니다.<br>쿠폰은 일회성으로 변심 취소 시 자동삭제/재지급이 불가능합니다.<br>이점 꼭 참고하여 예치금 환불 요청 부탁드리겠습니다:)<br><br>-----------------------------------------------<br>*주문취소*(전체취소시 상품명에  &#39;전체취소&#39; 꼭! 기재해주세요!)<br><br><strong>주문번호 : 23456323</strong><br><strong>상품명(옵션포함기재): 전체 취소</strong><br><strong>※결제시 입금한 이름의 계좌번호※</strong><br><strong>환불계좌번호: 235-021-087159</strong><br><strong>은행사 : 국민은행</strong><br><strong>예금주명 : 김주영</strong><br><br>♥해당 양식에 정확한 상품명 남겨주셔야 처리가 가능합니다♥</p>', 'tester7',true, null);
call autoQuestion(null, 'change', false, '<p>♥해당 양식에 정확한 상품명을 기재해주셔야 처리가 가능합니다♥<br><br>예치금 환불 시 철회 불가능하며<br>쿠폰/적립금과 중복 사용 불가능합니다.<br>쿠폰은 일회성으로 변심 취소 시 자동삭제/재지급이 불가능합니다.<br>이점 꼭 참고하여 예치금 환불 요청 부탁드리겠습니다:)<br><br>-----------------------------------------------<br>*상품변경*<br><br>주문번호 :<br>변경전 상품명 (사이즈,컬러) : 스노우 버튼 모직 스커트(s, 그레이지)<br>변경후 상품명 (사이즈,컬러) : 스노우 버트 모직 스커트(m, 그레이지)</p>', 'tester10',true, null);
call autoQuestion(null, 'changeAddress', false, '<p>♥해당 양식에 정확한 주문번호를 기재해주셔야 처리가 가능합니다♥<br><br><br>-----------------------------------------------<br>*주소지 변경*<br><br>주문번호 : 101284623<br>변경 주소지(번지수포함) : 서울 강남구 언주로 508 서울상록빌딩</p>', 'tester3', true, null);
call autoQuestion(null, 'exchange', false, '<p>♥해당 양식에 정확한 정보를 기재해주셔야 처리가 가능합니다♥<br><br><br>예치금 환불 시 철회 불가능하며<br>쿠폰/적립금과 중복 사용 불가능합니다.<br>쿠폰은 일회성으로 변심 취소 시 자동삭제/재지급이 불가능합니다.<br>이점 꼭 참고하여 예치금 환불 요청 부탁드리겠습니다:)<br><br>--------------------------------------<br>*교환*<br><br><strong>주문번호 : 120984323</strong><br><strong>교환전 상품명(사이즈,컬러) : 비프리 자켓(아이보리)</strong><br><strong>교환후 상품명(사이즈,컬러) : 비프리 자켓(블랙)</strong><br><strong>왕복 배송비 입금자명/입금날짜 : 주현영/ 2022-03-07</strong><br><br>(수령주소지로 자동 회수접수)<br>※회수/교환상품수령지 변경 원하실 경우에만 새주소지 함께 기재해 주세요.<br><br>회수주소: 서울 강남구 대치동 889-41<br>교환상품 수령 주소: 서울 강남구 대치동 889-41<br>&nbsp;</p>', 'tester', true, null);
call autoQuestion(null, 'return', false, '<p>♥해당 양식에 정확한 정보를 기재해주셔야 처리가 가능합니다♥<br><br><br>예치금 환불 시 철회 불가능하며<br>쿠폰/적립금과 중복 사용 불가능합니다.<br>쿠폰은 일회성으로 변심 취소 시 자동삭제/재지급이 불가능합니다.<br>이점 꼭 참고하여 예치금 환불 요청 부탁드리겠습니다:)<br><br>--------------------------------------<br>*반품*<br><br><strong>주문번호 : 546787654</strong><br><strong>상품명(사이즈,컬러): 스노우 버튼 모직 스커트(m, 소프트민트)</strong><br><strong>반품 사유 : 사이즈가 안맞고 색상도 화면이랑 많이 다르네요</strong><br><strong>※결제시 입금한 이름의 계좌번호※</strong><br><strong>환불계좌번호(은행사포함) : 123-36777-33-562</strong><br><strong>예금주명 : 이나라</strong><br><br>(수령주소지로 자동 회수접수)<br>※회수주소 변경 원하실 경우에만 새주소지와 함께 기재해 주세요.<br><br>회수주소: 서울 강남구 테헤란로 212<br><br>--------------------------------------<br>&nbsp;</p>', 'tester5', true,null);
call autoQuestion(null, 'error', false, '<p>♥해당 양식에 정확한 정보를 기재해주셔야 처리가 가능합니다♥<br><br>--------------------------------------<br>*불량/오배송*<br><br><strong>▷바코드(검수완료)사진</strong><br><strong>▷불량사진</strong><br><strong>(필수첨부 부탁드립니다!)</strong><br><br>주문번호 : 102319532<br>교환/반품 (원하시는 처리 선택해주세요!) : 반품<br>상품 수령일자 : (3/2)<br>반품 접수일자 : (3/7)<br>상품명(사이즈,컬러) : 스노우 버튼 모직 스커트(m, 그레이지)<br>불량/오배송 사유 : 스커트 단추가 불량이네요<br>검수번호(숫자나 알파벳) : 123ew2<br><br>(상품바코드옆 검수자 숫자한자리/두자리를 기재합니다.)</p>','tester2', true, 'bar-code-g042121347_640.png;jeans-ga6ae8c0ef_640.jpg;');

-- 배너
-- insert into bannertable(image, link) values('test1.jpg','testlink1'); 
-- insert into bannertable(image, link) values('test2.jpg','testlink2');

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