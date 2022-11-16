drop database data_warehouse;
create database data_warehouse;
use data_warehouse;

# CREATE TABLE data_warehouse.currency_dim
# (
#     currency_key  int         not null auto_increment,
#     currency_name varchar(50) not null,
#     currency_code varchar(50) not null,
#     primary key (currency_key)
# );
# CREATE TABLE data_warehouse.measure_dim
# (
#     measure_key    int         not null auto_increment,
#     measure_name   varchar(50) not null,
#     measure_symbol varchar(50),
#     primary key (measure_key)
# );
# CREATE TABLE data_warehouse.unit_dim
# (
#     unit_key     int not null auto_increment,
#     currency_key int not null,
#     measure_key  int not null,
#     primary key (unit_key),
#     foreign key (currency_key) references currency_dim (currency_key),
#     foreign key (measure_key) references measure_dim (measure_key)
# );
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
    price          decimal(13, 2) not null,
    percent        decimal(13, 2) not null,
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
    into table data_warehouse.date_dim
    FIELDS TERMINATED BY ',';


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

create procedure data_warehouse.load_staging_to_dw()
    begin
        insert into data_warehouse.data_warehouse (natural_key, commodity_name, price, percent, category_key, unit_key, created_date)
        (select natural_key, commodity_name, price, percent, cate_key, unit, date_key
         from staging.staging
                  left join data_warehouse.cate_dim on staging.category = data_warehouse.cate_dim.cate_name
                  left join data_warehouse.date_dim on staging.created_date = full_date);
    end;
create procedure data_warehouse.delete_staging_no_change()
begin
    delete s from staging.staging s
        left join data_warehouse.data_warehouse on s.natural_key = data_warehouse.natural_key
    where s.price = data_warehouse.price and s.percent = data_warehouse.percent;
end;
create procedure data_warehouse.update_expired_date(IN expiredDate DATE)
begin
    update data_warehouse.data_warehouse
        inner join staging.staging on staging.natural_key = data_warehouse.natural_key
    set data_warehouse.expired_date = expiredDate
    where (staging.price != data_warehouse.price or staging.percent != data_warehouse.percent)
        and data_warehouse.expired_date = '9999-12-31';
end;
create procedure data_warehouse.insert_data()
    begin
        set @expiredDate = (select distinct created_date from staging.staging);
        call data_warehouse.update_expired_date(@expiredDate);
        call data_warehouse.delete_staging_no_change();
        call data_warehouse.load_staging_to_dw();
    end;
drop  procedure data_warehouse.find_unexpired_data;
create procedure data_warehouse.find_unexpired_data()
    begin
        select s_key, natural_key, commodity_name, price, percent, cate_name, unit_key, expired_date, full_date as created_date
        from data_warehouse.data_warehouse
          inner join data_warehouse.cate_dim on data_warehouse.category_key = data_warehouse.cate_dim.cate_key
          inner join data_warehouse.date_dim on data_warehouse.created_date = date_dim.date_key
        where data_warehouse.expired_date = '9999-12-31';
    end;

# select * from controller.file_log;
# select * from staging.staging;
# select * from data_warehouse.data_warehouse;
# select count(distinct natural_key) from data_warehouse.data_warehouse
# where expired_date = '9999-12-31';
# truncate controller.file_log;
# truncate staging.staging;
# truncate data_warehouse.data_warehouse;
