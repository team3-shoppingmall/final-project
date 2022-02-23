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
    AMOUNT INT NOT NULL
--     CONSTRAINT basket_fk_id FOREIGN KEY (ID)
--         REFERENCES membertable (ID),
--     CONSTRAINT basket_fk_productno FOREIGN KEY (PRODUCTNO)
--         REFERENCES producttable (PRODUCTNO)
);

CREATE TABLE wishlisttable (
    ID VARCHAR(50) NOT NULL,
    PRODUCTNO INT NOT NULL,
    CONSTRAINT primary_wishlist PRIMARY KEY (ID, PRODUCTNO)
--     CONSTRAINT wishList_fk_id FOREIGN KEY (ID)
--         REFERENCES membertable (ID),
--     CONSTRAINT wishList_fk_productno FOREIGN KEY (PRODUCTNO)
--         REFERENCES producttable (PRODUCTNO)
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
   DETAILADDR VARCHAR(50) NOT NULL
--     CONSTRAINT order_fk_id FOREIGN KEY (ID) REFERENCES membertable (ID),
--     CONSTRAINT order_fk_productno FOREIGN KEY (PRODUCTNO) REFERENCES producttable (PRODUCTNO)
);

CREATE TABLE pointtable (
    NUM BIGINT PRIMARY KEY AUTO_INCREMENT,
    ID VARCHAR(50) NOT NULL,
    POINT INT NOT NULL,
   POINTDATE TIMESTAMP DEFAULT (current_timestamp)
--     CONSTRAINT point_fk_id FOREIGN KEY (ID) REFERENCES membertable (ID)
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
   STAR INT NOT NULL
--     CONSTRAINT review_fk_productno FOREIGN KEY (PRODUCTNO) REFERENCES producttable (PRODUCTNO)
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
insert into membertable values('admin','admin','관리자','0212345678','spring@gmail.com','12345','서울 강남구 테헤란로 212 (멀티캠퍼스)','2층 201호',false,null,'ROLE_ADMIN');
insert into membertable values('tester','Asdqwe123','유저','01098765432','user@gmail.com','54321','부산 남구 문현로 56-1 (네이버코리아)','5층 502호',false,null,'ROLE_USER');
insert into membertable values('tester2','Asdqwe123','유저2','01045614561','user2@gmail.com','24241','부산 문현로 56-1 (네이버코리아)','4층 405호',false,null,'ROLE_USER');
-- 상품
insert into producttable(productname, type1, type2, imagename, price, discount, color, size, amount, detailimagename) 
values('스노우 버튼 모직스커트', 'skirt','mini','test.jpg',38000,0,'그레이지;소프트민트','S;M;L', 100,'test.jpg');
insert into producttable(productname, type1, type2, imagename, price, discount, color, size, amount, detailimagename)
values('실키 여리핏 히든블라우스', 'shirt','blouse','test.jpg',34900,5000,'아이보리;피치베이지;워터리블루;블랙',null, 100,'test.jpg');
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
values('tester2',2,2,'피치베이지',null,4,59800,'credit','유저2','01045614561','24241','부산 문현로 56-1 (네이버코리아)','4층 405호');
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
insert into faqtable(type, title, content) values('product', '상품 관련 자주 묻는 질문 제목입니다', '상품 관련 자주 묻는 질문 내용입니다');
insert into faqtable(type, title, content) values('delivery', '배송 관련 자주 묻는 질문 제목입니다', '배송 관련 자주 묻는 질문 내용입니다');
insert into faqtable(type, title, content) values('return', '교환/반품 관련 자주 묻는 질문 제목입니다', '교환/반품 관련 자주 묻는 질문 내용입니다');
insert into faqtable(type, title, content) values('etc', '기타 관련 자주 묻는 질문 제목입니다', '기타 관련 자주 묻는 질문 내용입니다');
-- 후기
insert into reviewtable(productno, content, id, image, star) values(1,'처음 입고 됐을 땐 네이비, 색상 추가로 재입고 됐을 땐 다크네이비로 사서 잘 입고 다녔는데 민트브라운이 계속 아른거리더라구요...재입고 될 때마다 풀렸나 싶어 몇번을 기웃거렸던지요...그래서 이번에 추가로 재입고 해주셔서 너무 좋아요!!ㅎㅎ','tester', 'image1.jpg','5');
insert into reviewtable(productno, content, id, image, star) values(1,'이전에 다른 색상으로 구매했었는데 재질도 좋고 착용감도 좋아서 새로 또 구매했어요! 그레이 색상이라 전체적으로 색감이 다운됐지만, 그 만의 매력이 있어서 마음에 들어요!','tester2', 'image2.jpg','4');
insert into reviewtable(productno, content, id, image, star) values(2,'가을에 휘뚜루마뚜루 입으려고 샀어요 검정+보라 스트라이프 사고 싶었는데 라벤더네이비도 충분히 예쁘네요 너무 오버핏도 아니고 너무 베이직한 핏도 아니라서 딱 맘에 들어요 다른 색으로 더 살 것 같아요 쌩유','tester2', 'image3.jpg','4');
insert into reviewtable(productno, content, id, image, star) values(1,'완전 딱 진짜 기본이에요!! 탄탄한 재질은 아니지만 봄에 가볍게 입기 좋을것 같아요!! 그리고 흰색배경이 아니라 색감 진짜 너무 예쁜것같아요 : )','tester', 'image4.jpg','4');
insert into reviewtable(productno, content, id, image, star) values(2,'챠콜이랑 아이보리 고민했었는데 아이보리가 하의를 뭘입어도 다 잘어울릴것같아서 선택했어요! 핏도 낙낙하고 안에 목티입으니까 완전 제스타일이에요=) 다른색깔도 구매하고 싶네용','tester', 'image5.jpg','5');
insert into reviewtable(productno, content, id, image, star) values(1,'개인적으로 색깔은 솔직히 실망했어요. 예쁜 베이지가 아니라 그냥 황토색? 상세샷보다도 좀 어두워요.다른 분들 후기처럼 냄새는 많이 나고 꺼끌거려요. 그리고 무엇보다 털이 진짜.. 어마무시하게 빠집니다. 안에 검정색 히트텍 입었다가 깜짝 놀랐어요..','tester1', 'image1.jpg','1');
insert into reviewtable(productno, content, id, image, star) values(2,'너무 이뻐요 사길 후회 안할정도로 ...팔뚝잇는66인데 이거 입고 55로 보인데요..(엄마 눈에만) 배송은 좀 느리게 왓더라고요! 제가 사진후기 잘 안올리는데 품절 풀리면 바로사세오...','tester2', 'image2.jpg','4');
insert into reviewtable(productno, content, id, image, star) values(1,'바스락거리는 소재에요! 약간 단작셔츠같은 느낌..! 제가 좋아하는 느낌이에요 히히 근데 159인 제 키에는 뒷쪽 언발 부분이 엉덩이를 다 가리고도 살짝 더 내려와서 안그래도 작은 키가 더 작아보이긴 하네요,, 그래도 색감이나 핏 다 너무 예쁘고 소매쪽 핀턱때문에 포인트도 되고! 마음에 들어요 ´ㅅ`','tester3', 'image3.jpg','3');
insert into reviewtable(productno, content, id, image, star) values(2,'편하게 입으려고 샀는데, 후기를 미처 꼼꼼히 못 읽은 것 같아요. 피부 좀 예민하신 분들은 엄청 따갑습니다. 특히 가운데 지퍼 부분이 까끌해서 피부 엄청 빨갛게 돼요ㅠ 그리고 털이 너무 많이 빠져서 코트에 다 묻습니다. 예쁘지만 산 것 중에 유일하게 좀 실망한 니트예요ㅠㅠ','tester4', 'image4.jpg','2');
insert into reviewtable(productno, content, id, image, star) values(1,'이옷은 제가 산것중 제일 실망입니다,,물론 디자인은 나쁘진 않은데 원단이 좀,,싼티나요 보풀도 많이나고,,진짜 믿고 구매하는데 오래기다려서 받았는데 반품이 귀찮아입어요;;','tester5', 'image5.jpg','1');
insert into reviewtable(productno, content, id, image, star) values(1,'인기가 워낙 많았던 ㅔ품이라서 오래 기다려서 받았어요!엄마도 이때까지 산 셔츠중에 이게 가장 예쁘다고 하시네요 ㅎㅎ 언발 기장으로 떨어지는 게 너무 예쁩니당 얼른 날 풀려서 단독으로 입고 다니고 싶어요ㅠㅠ','tester1', 'image1.jpg','5');
insert into reviewtable(productno, content, id, image, star) values(2,'색이 정말 이뻐요! 색상은 화면과 같아요! 다만 구김 옴청 잘 가는 소재 ㅠ 알고 샀지만 생각보다 더 바스락 거리는 구김 가는 소재에요 ㅎㅎ입을때마다 열심히 다리미해야겠어요!','tester2', 'image2.jpg','4');
insert into reviewtable(productno, content, id, image, star) values(1,'거울이 너무 더럽지만 리뷰 써봐용 ㅠㅠ 교복 같을까봐 걱정했는데 블랙이라 그런 느낌도 덜 해서 다행이고 길이도 너무 짧지 않고 적당해서 자주 입을 것 같아요','tester3', 'image3.jpg','4');
insert into reviewtable(productno, content, id, image, star) values(2,'바스락바스락 얇은 옷이에요. 근데 무엇보다.. 색감이 미쳤어요ㅠㅠㅠㅠ 진짜 너무 예쁩니다. 유치하고 가벼운 느낌 아니고 차분해요. 핏은 오버핏인데 언발란스한 기장 덕분에 남의 옷 입은 느낌 1도 없고 여리여리해보여요. 빨리 따뜻해져서 단독으로 입고싶은 옷이에요.ㅠㅠㅠ','tester4', 'image4.jpg','4');
insert into reviewtable(productno, content, id, image, star) values(1,'일시품절되기전에 구매했어요!너무너무 만족스럽스럽니다 ㅎㅎ클리어블루 컬러 실제로 보면 더 예뻐요 ㅠㅠ 꼭 사세요!','tester5', 'image5.jpg','5');
insert into reviewtable(productno, content, id, image, star) values(1,'이번에 산 옷들끼리 코디해봤는데 이 스커트 인스타로 봤을 때 부터 이건 사야한다 싶었는데 진짜 너무 이쁘네요ㅜㅜ이번 할인구매 물품 중에 1등입니다ㅜㅜ저는 사실 쬐끔만 더 짧았으면 햇는데 딱 안정적으로 이쁜 길이긴 해요ㅎㅎ','tester5', 'image5.jpg','4');
insert into reviewtable(productno, content, id, image, star) values(1,'요즘 옷들이 작아서 안 맞을까 걱정했는데(ㅠㅠ) 불편하지 않게 딱 맞아요! 핏도 맘에 들고 만족스러워용^.^','tester5', 'image5.jpg','5');
insert into reviewtable(productno, content, id, image, star) values(1,'딱 봄 가을에 입기 좋을 얇은 두께입니다 겨울에는 너무 추울 것 같아요 에스 사이즈로 샀는데 조금 크게 나온 것 같아요 그래서인지 핏하게 예쁘게 떨어지지는 않아 조금 아쉽습니다 ㅠㅠ','tester5', 'image5.jpg','4');
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
insert into bannertable(image, link) values('test1.jpg','testlink1');
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

-- 많이 팔린 순으로 정렬
-- select productno, sum(amount) from ordertable group by productno order by sum(amount) desc;
-- select * from producttable left join ordertable on producttable.productno = ordertable.productno where type1 = 'skirt' group by ordertable.productno order by sum(ordertable.amount) desc limit 0,8;