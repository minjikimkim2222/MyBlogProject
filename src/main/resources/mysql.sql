show tables;
-- drop table 모음
drop table users;
drop table blogs;
drop table series;
drop table posts;
drop table tags;
drop table follows;
drop table post_tags;
drop table likes;
drop table comments;

-- select 결과..
select * from users;
select * from blogs;
select * from series;
select * from posts;
select * from likes;
select * from post_tags;
select * from tags;
select * from follows;
select * from comments;

-- user 1개 시험용 넣기
insert into users (username, password, name, email, image, role) VALUES
    ('id1', 'pw1', 'name1', 'email1@exam.com', 'image1.jpg', 'ROLE_USER');