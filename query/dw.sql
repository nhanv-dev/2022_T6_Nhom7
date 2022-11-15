drop database data_warehouse;
create database data_warehouse;
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
CREATE TABLE data_warehouse.currency_dim
(
    currency_key  int         not null auto_increment,
    currency_name varchar(50) not null,
    currency_code varchar(50) not null,
    primary key (currency_key)
);
CREATE TABLE data_warehouse.measure_dim
(
    measure_key    int         not null auto_increment,
    measure_name   varchar(50) not null,
    measure_symbol varchar(50),
    primary key (measure_key)
);
CREATE TABLE data_warehouse.unit_dim
(
    unit_key     int not null auto_increment,
    currency_key int not null,
    measure_key  int not null,
    primary key (unit_key),
    foreign key (currency_key) references currency_dim (currency_key),
    foreign key (measure_key) references measure_dim (measure_key)
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
    unit_key       int            not null,
    expired_date   date default '9999/12/31',
    created_date   int,
    primary key (s_key),
    foreign key (created_date) references date_dim (date_key),
    foreign key (category_key) references cate_dim (cate_key),
    foreign key (unit_key) references unit_dim (unit_key)
);

load data infile 'C:/ProgramData/MySQL/MySQL Server 8.0/Uploads/date_dim_without_quarter.csv' into table date_dim
    FIELDS TERMINATED BY ',';

insert into cate_dim(cate_name)
values ('Agricultural'),
       ('Agriculture'),
       ('Energy'),
       ('Electricity'),
       ('Livestock'),
       ('Metals'),
       ('Industrial'),
       ('Industrial Metals'),
       ('Index');

insert into currency_dim(currency_name, currency_code)
values ('AUD', 'AUD'),
       ('BRL', 'BRL'),
       ('CAD', 'CAD'),
       ('CNY', 'CNY'),
       ('EUR', 'EUR'),
       ('MYR', 'MYR'),
       ('USD', 'USD'),
       ('USC', 'USC'),
       ('NOK', 'NOK'),
       ('GBP', 'GBP'),
       ('Ringgit', 'Ringgit');

select natural_key,
       commodity_name,
       price,
       percent,
       cate_key,
       unit,
       unit_key,
       date_key
from staging.staging
         left join data_warehouse.cate_dim on staging.staging.category = data_warehouse.cate_dim.cate_name
         left join data_warehouse.date_dim on staging.created_date = full_date
         left join data_warehouse.unit_dim on staging.unit in (SUBSTRING(data_warehouse.unit_dim.unit_keyword, ','));


# INSERT INTO data_warehouse.data_warehouse (natural_key, commodity_name, price, percent, category_key, unit_key,
#                                            created_date)
#     (
#
#      )

# UPDATE data_warehouse
# SET expired_date = CURRENT_DATE

