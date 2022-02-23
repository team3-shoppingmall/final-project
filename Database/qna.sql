use springdb;

drop table if exists qnatable;
create table qnatable(
	qnaNo bigint not null primary key auto_increment,
    -- productNo int, 
    type varchar(200) not null,
    originalNo bigint,
    reply boolean default false,
    content varchar(2000) not null,
    id varchar(50) not null,
    regDate timestamp default (current_timestamp),
    secret boolean default false,
    image varchar(500) not null);

insert into qnatable(type, originalNo, reply, content, id, image) values('product', 1, true, 'content1', 'user1','image1.jpg');
insert into qnatable(type, originalNo, reply, content, id, image) values('product', last_insert_id()+1, true, 'content1', 'user1','image1.jpg');
insert into qnatable(type, originalNo, content, id, image) values('productReply', 1, 'content1', 'user1','image1.jpg');
insert into qnatable(type, originalNo, reply, content, id, image) values('product', last_insert_id()+1, true, 'content1', 'user1','image1.jpg');
insert into qnatable(type, originalNo, reply, content, id, image) values('product', last_insert_id()+1, true, 'content1', 'user1','image1.jpg');
insert into qnatable(type, originalNo, reply, content, id, image) values('product', last_insert_id()+1, true, 'content1', 'user1','image1.jpg');
insert into qnatable(type, originalNo, content, id, image) values('productReply', 3, 'content1', 'user1','image1.jpg');
insert into qnatable(type, originalNo, content, id, image) values('productReply', 4, 'content1', 'user1','image1.jpg');
insert into qnatable(type, originalNo, reply, content, id, image) values('product', lnoticetableast_insert_id()+1, true, 'content1', 'user1','image1.jpg');
insert into qnatable(type, originalNo, reply, content, id, image) values('product', last_insert_id()+1, true, 'content1', 'user1','image1.jpg');

commit;

select * from qnatable order by qnaNo desc;

