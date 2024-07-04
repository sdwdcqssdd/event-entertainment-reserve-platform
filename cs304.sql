-- 创建事件表
CREATE TABLE Events (
    event_id SERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    date DATE NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    venue_id INT NOT NULL,
    category_id INT NOT NULL,
    type varchar(20) constraint check_isValidType check(type in('approved','pending','rejected')),
    organizer_id INT NOT NULL,
    capacity_limit INT,
    remaining INT,
    FOREIGN KEY (venue_id) REFERENCES Venues(venue_id),
    FOREIGN KEY (category_id) REFERENCES Categories(category_id),
    FOREIGN KEY (organizer_id) REFERENCES Users(user_id)
);


-- 创建用户表
CREATE TABLE Users (
    user_id SERIAL PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    identity varchar(20) constraint check_isValidUser check(identity in('USER','SUPERUSER'))
);

-- 创建类别表
CREATE TABLE Categories (
    category_id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

-- 创建场馆表
CREATE TABLE Venues (
    venue_id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    capacity INT NOT NULL
);

-- 创建用户预约事件表
CREATE TABLE UserEvent (
    user_event_id SERIAL PRIMARY KEY,
    user_id INT NOT NULL,
    event_id INT NOT NULL,
    status VARCHAR(20) NOT NULL,
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES Users(user_id),
    CONSTRAINT fk_event FOREIGN KEY (event_id) REFERENCES Events(event_id),
    CONSTRAINT check_isValidStatus CHECK (status IN ('approved', 'pending', 'rejected'))
);

CREATE TABLE Emails (
    id serial primary key,
    UserID INT NOT NULL,
    Subject VARCHAR(255) NOT NULL,
    Body TEXT NOT NULL,
    Time timestamp DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (UserID) REFERENCES Users(user_id)
);