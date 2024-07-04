--发帖表--
create table post (
    id serial primary key,
    user_id int,
    foreign key (user_id) references Users(user_id),
    title varchar not null,
    content varchar not null,
    post_time timestamp,
    reply_num int not null
);

--一级回复--
create table reply (
    id serial primary key,
    post_id int not null,
    foreign key (post_id) references post(id),
    content varchar not null,
    star int,
    user_id int,
    foreign key (user_id) references Users(user_id),
    post_time timestamp
);

--二级回复--
create table second_reply(
    id serial primary key,
    reply_id int not null,
    foreign key (reply_id) references reply(id),
    second_reply_id int,
    foreign key (second_reply_id) references second_reply(id),
    content varchar not null,
    star int,
    user_id int,
    foreign key (user_id) references Users(user_id),
    post_time timestamp
);

--关注--
create table User_follow (
    id serial primary key,
    user_id int not null,
    foreign key (user_id) references Users(user_id),
    follow_id int not null,
    foreign key (follow_id) references Users(user_id)
);

--收藏--
create table User_favour_post (
    id serial primary key,
    post_id int not null ,
    foreign key (post_id) references post(id),
    user_id int,
    foreign key (user_id) references Users(user_id)
);

--拉黑--
create table User_ban (
    id serial primary key,
    user_id int not null,
    foreign key (user_id) references Users(user_id),
    ban_id int not null,
    foreign key (ban_id) references Users(user_id)
);

--历史浏览--
create table history (
    id serial primary key,
    user_id int not null,
    foreign key (user_id) references Users(user_id),
    post_id int not null,
    foreign key (post_id) references post(id)
);
