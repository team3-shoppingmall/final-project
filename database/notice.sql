use springdb;

drop table if exists noticetable;
create table noticetable(
	NOTICENO INT AUTO_INCREMENT PRIMARY KEY,
    TITLE VARCHAR(200) NOT NULL,
    CONTENT VARCHAR(2000) NOT NULL,
    ID VARCHAR(50) NOT NULL,
    IMAGE VARCHAR(500)
);

-- 증가값 조회
SELECT @@AUTO_INCREMENT_INCREMENT;
-- 증가값 변경
SET @@AUTO_INCREMENT_INCREMENT=1;

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

commit;

select * from noticetable order by noticeno desc limit 3,5;
select * from noticetable order by noticeno desc;
select * from noticetable where title like '%%' order by noticeno desc limit 1,5;