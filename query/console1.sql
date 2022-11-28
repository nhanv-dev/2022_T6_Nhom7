drop database controller;
drop database staging;
drop database data_warehouse;
create database controller;
create database data_warehouse;
create database staging;

use controller;

CREATE TABLE controller.author
(
    author_id   int           NOT NULL AUTO_INCREMENT,
    author_name nvarchar(255) NOT NULL,
    phone       nvarchar(255) NOT NULL,
    email       nvarchar(255) NOT NULL,
    is_deleted  tinyint default 1,
    PRIMARY KEY (author_id)
);
CREATE TABLE controller.file_configuration
(
    config_id int NOT NULL AUTO_INCREMENT,
    source    varchar(255),
    PRIMARY KEY (config_id)
);
CREATE TABLE controller.configuration_key
(
    config_id    int          NOT NULL,
    config_name  varchar(255) NOT NULL,
    config_value varchar(255) NOT NULL,
    PRIMARY KEY (config_id, config_name)
);
CREATE TABLE controller.file_log
(
    file_id      int          NOT NULL AUTO_INCREMENT,
    config_id    int          NOT NULL,
    file_path    varchar(255) NOT NULL,
    file_status  varchar(10)  NOT NULL,
    author       int          NOT NULL,
    created_date datetime DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (file_id),
    FOREIGN KEY (author) REFERENCES author (author_id)
);
# Checked Sources:
# https://tradingeconomics.com/commodities
# https://markets.businessinsider.com/commodities
INSERT INTO controller.file_configuration (config_id, source)
VALUES (1, 'https://tradingeconomics.com/commodities'),
       (2, 'https://markets.businessinsider.com/commodities');
INSERT INTO controller.configuration_key (config_id, config_name, config_value)
VALUES (1, 'method', 'jsoup'),
       (1, 'name', 'tradingeconomics'),
       (1, 'directory', 'C:/ProgramData/MySQL/MySQL Server 8.0/Uploads/data'),
       (1, 'natural_key', 'selector_col_1'),
       (1, 'created_date', 'true'),
       (1, 'selector_container', '.table.table-hover.table-striped.table-heatmap'),
       (1, 'selector_row', 'tbody > tr'),
       (1, 'selector_col_name', 'td:nth-child(1) > a > b'),
       (1, 'selector_col_price', 'td:nth-child(2)'),
       (1, 'selector_col_percent', 'td:nth-child(4)'),
       (1, 'selector_col_category', 'thead tr th:nth-child(1)'),
       (1, 'selector_col_unit', 'td:nth-child(1) > div');
INSERT INTO controller.configuration_key (config_id, config_name, config_value)
VALUES (2, 'method', 'jsoup'),
       (2, 'name', 'businessinsider'),
       (2, 'directory', 'C:/ProgramData/MySQL/MySQL Server 8.0/Uploads/data'),
       (2, 'natural_key', 'selector_col_1'),
       (2, 'created_date', 'true'),
       (2, 'selector_container', '.table:not(:first-child)'),
       (2, 'selector_row', 'tbody > tr'),
       (2, 'selector_col_name', 'td:nth-child(1) a'),
       (2, 'selector_col_price', 'td:nth-child(2) span'),
       (2, 'selector_col_percent', 'td:nth-child(3) span'),
       (2, 'selector_col_category', 'thead tr th:nth-child(1)'),
       (2, 'selector_col_unit', 'td:nth-child(5)');
# Insert into author
INSERT INTO controller.author(author_id, author_name, phone, email)
VALUES (1, 'Trần Thanh Nhân', '0354536457', '19130159@st.hcmuaf.edu.vn'),
       (2, 'Nguyễn Hữu Đạo', '0534545645', '19130029@st.hcmuaf.edu.vn'),
       (3, 'Lê Quốc Sơn Giang', '0394820944', '19130060@st.hcmuaf.edu.vn');
# Find file configuration
CREATE PROCEDURE controller.find_file_configuration(IN id int)
BEGIN
    select ck.config_id, config_name, config_value
    from file_configuration
             inner join configuration_key ck on file_configuration.config_id = ck.config_id
    where file_configuration.config_id = id;
END;
# Insert file_log
CREATE PROCEDURE controller.insert_file_log(IN c_id long, IN path varchar(255), IN status varchar(10), IN author_id int,
                                            IN created_at DATE, OUT id int)
BEGIN
    INSERT INTO controller.file_log (config_id, file_path, file_status, author, created_date)
    VALUES (c_id, path, status, author_id, created_at);
    SET id = LAST_INSERT_ID();
END;

use data_warehouse;

CREATE TABLE data_warehouse.date_dim
(
    date_key            int,
    full_date           date,
    day_since_2005      int,
    month_since_2005    int,
    day_of_week         varchar(30),
    calendar_month      varchar(30),
    calendar_year       year,
    calendar_year_month varchar(30),
    day_of_month        int,
    day_of_year         int,
    week_of_year_sunday int,
    year_week_sunday    varchar(30),
    week_sunday_start   date,
    week_of_year_monday int,
    year_week_monday    varchar(30),
    week_monday_start   date,
    holiday             varchar(30),
    day_type            varchar(30),
    primary key (date_key)
);
CREATE TABLE data_warehouse.cate_dim
(
    cate_key  int         not null auto_increment,
    cate_name varchar(50) not null,
    primary key (cate_key)
);
CREATE TABLE data_warehouse.data_warehouse
(
    s_key          int            not null auto_increment,
    natural_key    varchar(50)    not null,
    commodity_name varchar(50)    not null,
    price          float not null,
    percent        float not null,
    category_key   int            not null,
    unit_key       varchar(50)    not null,
    expired_date   date default '9999-12-31',
    created_date   int,
    primary key (s_key),
    foreign key (created_date) references date_dim (date_key),
    foreign key (category_key) references cate_dim (cate_key)
#     foreign key (unit_key) references unit_dim (unit_key)
);
load data infile 'C:/ProgramData/MySQL/MySQL Server 8.0/Uploads/date_dim_without_quarter.csv'
    into table data_warehouse.date_dim FIELDS TERMINATED BY ',';
insert into data_warehouse.cate_dim(cate_name)
values ('Agricultural'),
       ('Agriculture'),
       ('Energy'),
       ('Electricity'),
       ('Livestock'),
       ('Metals'),
       ('Industrial'),
       ('Industrial Metals'),
       ('Index');

use staging;

CREATE TABLE staging.staging
(
    s_key          int not null auto_increment,
    natural_key    varchar(50),
    commodity_name varchar(50),
    category       varchar(50),
    price          float,
    percent        float,
    unit           varchar(50),
    created_date   date default (CURRENT_DATE),
    primary key (s_key)
);
CREATE TABLE staging.staging_transformed
(
    s_key          int not null auto_increment,
    natural_key    varchar(50),
    commodity_name varchar(50),
    category       int,
    price          float,
    percent        float,
    unit           varchar(50),
    created_date   int,
    primary key (s_key),
    foreign key (category) references data_warehouse.cate_dim (cate_key),
    foreign key (created_date) references data_warehouse.date_dim (date_key)
);
create procedure staging.delete_rows_null_column()
begin
    DELETE
    FROM staging.staging
    WHERE staging.commodity_name = ''
       or staging.commodity_name is null
       or staging.category = ''
       or staging.category is null
       or staging.price is null
       or staging.percent is null
       or staging.unit = ''
       or staging.unit is null
       or staging.created_date is null;
end;
create procedure staging.insert_into_staging_transformed()
begin
    INSERT INTO staging.staging_transformed
    SELECT s.s_key,
           s.natural_key,
           s.commodity_name,
           cate_dim.cate_key,
           s.price,
           s.percent,
           s.unit,
           date_dim.date_key
    FROM staging.staging s
             LEFT JOIN data_warehouse.date_dim ON s.created_date = date_dim.full_date
             LEFT JOIN data_warehouse.cate_dim ON s.category = cate_dim.cate_name;
end;
create procedure data_warehouse.load_staging_to_dw()
begin
    insert into data_warehouse.data_warehouse (natural_key, commodity_name, price, percent, category_key, unit_key,
                                               created_date)
    select st.natural_key, st.commodity_name, st.price, st.percent, st.category, st.unit, st.created_date
    from staging.staging_transformed st
             left join data_warehouse.data_warehouse
                       on st.natural_key = data_warehouse.natural_key and data_warehouse.expired_date = '9999-12-31';
end;

create procedure data_warehouse.delete_staging_no_change()
begin
    delete s
    from staging.staging_transformed s
             left join data_warehouse.data_warehouse on s.natural_key = data_warehouse.natural_key
    where s.price = data_warehouse.price
      and s.percent = data_warehouse.percent;
end;

create procedure data_warehouse.update_expired_date()
begin
    set @expiredDate = (select distinct created_date from staging.staging);
    update data_warehouse.data_warehouse inner join staging.staging_transformed on staging_transformed.natural_key = data_warehouse.natural_key
    set data_warehouse.expired_date = @expiredDate
    where (staging_transformed.price != data_warehouse.price or staging_transformed.percent != data_warehouse.percent)
      and data_warehouse.expired_date = '9999-12-31';
end;
create procedure data_warehouse.insert_data()
begin
    call data_warehouse.update_expired_date();
    call data_warehouse.delete_staging_no_change();
    call data_warehouse.load_staging_to_dw();
end;
create procedure data_warehouse.find_unexpired_data()
begin
    select s_key,
           natural_key,
           commodity_name,
           price,
           percent,
           cate_name,
           unit_key,
           expired_date,
           full_date as created_date
    from data_warehouse.data_warehouse
             inner join data_warehouse.cate_dim on data_warehouse.category_key = data_warehouse.cate_dim.cate_key
             inner join data_warehouse.date_dim on data_warehouse.created_date = date_dim.date_key
    where data_warehouse.expired_date = '9999-12-31';
end;

CREATE PROCEDURE controller.find_log_by_day_and_id(IN d DATE, IN id int)
BEGIN
    SELECT * FROM controller.file_log
    WHERE file_log.created_date = d AND file_log.config_id = id;
END;

