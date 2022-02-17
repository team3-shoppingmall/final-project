drop database if exists noticeTable;

drop database if exists springdb;
create database springdb;

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

commit;

select * from noticetable;