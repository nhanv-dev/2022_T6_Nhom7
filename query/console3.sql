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

load data infile 'C:/ProgramData/MySQL/MySQL Server 8.0/Uploads/test/tradingeconomics-20221116121242.csv'
into table staging
FIELDS TERMINATED BY ','
(natural_key, commodity_name, price, percent, category, unit, created_date)