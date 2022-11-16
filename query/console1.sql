drop database controller;
create database controller;
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
       (1, 'directory', 'C:/ProgramData/MySQL/MySQL Server 8.0/Uploads/test'),
       (1, 'natural_key', 'selector_col_1'),
       (1, 'created_date', 'true'),
       (1, 'selector_container', '.table.table-hover.table-striped.table-heatmap'),
       (1, 'selector_row', 'tbody > tr'),
       (1, 'selector_col_1', 'td:nth-child(1) > a > b'),
       (1, 'selector_col_2', 'td:nth-child(2)'),
       (1, 'selector_col_3', 'td:nth-child(4)'),
       (1, 'selector_col_4', 'thead tr th:nth-child(1)'),
       (1, 'selector_col_5', 'td:nth-child(1) > div');
INSERT INTO controller.configuration_key (config_id, config_name, config_value)
VALUES (2, 'method', 'jsoup'),
       (2, 'name', 'businessinsider'),
       (2, 'directory', 'C:/ProgramData/MySQL/MySQL Server 8.0/Uploads/test'),
       (2, 'natural_key', 'selector_col_1'),
       (2, 'created_date', 'true'),
       (2, 'selector_container', '.table:not(:first-child)'),
       (2, 'selector_row', 'tbody > tr'),
       (2, 'selector_col_1', 'td:nth-child(1) a'),
       (2, 'selector_col_2', 'td:nth-child(2) span'),
       (2, 'selector_col_3', 'td:nth-child(3) span'),
       (2, 'selector_col_4', 'thead tr th:nth-child(1)'),
       (2, 'selector_col_5', 'td:nth-child(5)');
# Insert into author
INSERT INTO controller.author(author_id, author_name, phone, email)
VALUES (1, 'Trần Thanh Nhân', '0354536457', '19130159@st.hcmuaf.edu.vn'),
       (2, 'Nguyễn Hữu Đạo', '0534545645', '19130029@st.hcmuaf.edu.vn'),
       (3, 'Lê Quốc Sơn Giang', '0394820944', '19130060@st.hcmuaf.edu.vn');
# Find file configuration
DELIMITER $
CREATE PROCEDURE controller.find_file_configuration(IN id int)
BEGIN
    select ck.config_id, config_name, config_value
    from file_configuration
             inner join configuration_key ck on file_configuration.config_id = ck.config_id
    where file_configuration.config_id = id;
END $
# Insert file_log
CREATE PROCEDURE controller.insert_file_log(IN c_id long, IN path varchar(255), IN status varchar(10), IN author_id int, IN created_at DATE, OUT id int)
BEGIN
    INSERT INTO controller.file_log (config_id, file_path, file_status, author, created_date)
    VALUES (c_id, path, status, author_id, created_at);
    SET id = LAST_INSERT_ID();
END $
# Update file_log
CREATE PROCEDURE controller.update_status_file_log(IN id int, IN status varchar(10))
BEGIN
    UPDATE controller.file_log
    SET file_status = status
    WHERE file_id = id;
END $
DELIMITER ;

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




