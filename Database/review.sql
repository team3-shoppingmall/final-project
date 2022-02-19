use springdb;

drop table if exists reviewTable;
create table reviewTable(
	ReviewNO bigint not null auto_increment,	-- 후기번호(자동증가)
	-- productNO int not null,				-- 상품번호 외래키
    content varchar(600) not null,          -- 내용
	id varchar(50) not null,                -- 등록아이디
    regDate date default (current_date),    -- 등록날짜
	image varchar(100),                     -- 사진이름
	star int not null,                      -- 별점

	primary key(reviewNO)
    -- foreign key(productno) references producttable(productno)
);

insert into reviewtable(content, id, image, star) values('후기내용1','user1', 'image1','3');
insert into reviewtable(content, id, image, star) values('후기내용2','user2', 'image2','4');
insert into reviewtable(content, id, image, star) values('후기내용3','user3', 'image3','5');
insert into reviewtable(content, id, image, star) values('후기내용4','user4', 'image4','5');
insert into reviewtable(content, id, image, star) values('후기내용5','user5', 'image5','5');
insert into reviewtable(content, id, image, star) values('후기내용1','user1', 'image1','5');
insert into reviewtable(content, id, image, star) values('후기내용2','user2', 'image2','5');
insert into reviewtable(content, id, image, star) values('후기내용3','user3', 'image3','5');
insert into reviewtable(content, id, image, star) values('후기내용4','user4', 'image4','5');
insert into reviewtable(content, id, image, star) values('후기내용5','user5', 'image5','5');

commit;

select * from reviewtable order by reviewNo desc;

-- select * from reviewtable order by reviewno desc limit 3,10;
-- select * from reviewtable order by reviewno desc;
-- select * from reviewtable where id like '%%' order by reviewno desc limit 1,5;