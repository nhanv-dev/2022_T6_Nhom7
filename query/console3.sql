drop database staging;
create database staging;
use staging;

CREATE TABLE staging.staging
(
    s_key          int not null auto_increment,
    natural_key    varchar(50),
    commodity_name varchar(50),
    category       varchar(50),
    price          decimal(13, 2),
    percent        decimal(13, 2),
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
    price          decimal(13, 2),
    percent        decimal(13, 2),
    unit           varchar(50),
    created_date   int,
    primary key (s_key),
    foreign key (category) references data_warehouse.cate_dim(cate_key),
    foreign key (created_date) references data_warehouse.date_dim(date_key)
);


# insert into unit_dim(unit_name, unit_desc, unit_keyword)
# values ('USD/MMBtu', 'USD per MMBtu', 'USD/MMBtu,'),
#        ('USD/Gal', 'USD per Gallon', 'USD/Gal,USD per Gallone,USD per Gallon'),
#        ('USD/MMBtu', 'USD per 100 Liter', ''),
#        ('USD/Bu', 'USD per Bushel', ''),
#        ('USD/1000 board feet', 'USD per 1.000 board feet', 'USD per 1.000 board feet'),
#        ('USD/Lbs', 'USD per lb.', 'USD/Lbs'),
#        ('USD/T', 'USD per Ton', 'USD/T,USD per Ton,USD per Dry Metric Ton'),
#        ('USC/T', 'USC per Ton', ''),
#        ('EUR/T', 'EUR per Ton', ''),
#        ('GBP/T', 'GBP per Ton', 'GBP/T,GBP per Ton'),
#        ('MYR/T', 'Ringgit per Ton', 'MYR/T,Ringgit/T,Ringgit per Ton'),
#        ('CAD/T', 'CAD per Ton', ''),
#        ('EUR/100Kg', 'EUR per 100Kg', 'EUR/100KG,EUR per 100Kg'),
#        ('USD/cwt', 'USD per cwt.', 'USD/cwt,USD per cwt.'),
#        ('USD/Dozen', 'USD per Dozen', 'USD/Dozen'),
#        ('GBP/thm', 'GBP per thm', 'GBP/thm'),
#        ('USD/t.oz', 'USD per Troy Ounce', 'USD/t oz.'),
#        ('USD/Kg', 'USD per Kg', 'USD Cents / Kg,USD/Kgs,USD/Kg'),
#        ('AUD/100Kg', 'USD/Bbl', ''),
#        ('Index Points', 'Index Points', 'Index Points'),
#        ('CNY/Kg', 'Index Points', 'CNY/Kg'),
#        ('EUR/MT', 'Index Points', 'EUR/MT'),
#        ('BRL/Kg', 'Index Points', 'BRL/Kg,BRL/Kgs'),
#        ('NOK/Kg', 'Index Points', 'NOK/Kg'),
#        ('EUR', 'EURO', 'EUR'),
#        ('USD', 'USD', 'USD'),
#        ('GBP/MWh', 'GBP/MWh', 'GBP/MWh'),
#        ('EUR/MWh', 'EUR per MWh', 'EUR/MWh');

truncate staging.staging;
load data infile 'C:/ProgramData/MySQL/MySQL Server 8.0/Uploads/test/tradingeconomics-20221116041607.csv'
into table staging
FIELDS TERMINATED BY ','
(natural_key, commodity_name, price, percent, category, unit, created_date);

truncate staging.staging_transformed;

drop procedure staging.update_to_new_value;
create procedure staging.update_to_new_value(IN old_category varchar(50), IN new_category varchar(50))
begin
    UPDATE staging.staging
    SET staging.category = new_category
    WHERE staging.category = old_category;
end;

call update_to_new_value('Agricultural', 'Agriculture');

# -----------------------

drop procedure delete_rows_null_column;
create procedure staging.delete_rows_null_column()
begin
    DELETE FROM staging.staging
    WHERE staging.commodity_name = '' or staging.commodity_name is null or
            staging.category = '' or staging.category is null or
        staging.price is null or staging.percent is null or
            staging.unit = '' or staging.unit is null or
        staging.created_date is null;
end;

call delete_rows_null_column();

# -----------------------
drop procedure insert_into_staging_transformed;
create procedure staging.insert_into_staging_transformed()
begin
    INSERT INTO staging.staging_transformed
    SELECT staging.s_key, staging.natural_key, staging.commodity_name, cate_dim.cate_key, staging.price, staging.percent, staging.unit, date_dim.date_key
    FROM staging.staging LEFT JOIN data_warehouse.date_dim ON staging.created_date = date_dim.full_date
                         LEFT JOIN data_warehouse.cate_dim ON staging.category = cate_dim.cate_name;
end;

call insert_into_staging_transformed()