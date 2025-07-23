CREATE TABLE IF NOT EXISTS naics_description_history (
    id BIGSERIAL PRIMARY KEY,
    code NUMERIC(6) NOT NULL REFERENCES naics_code (code),
    description CHARACTER VARYING(200) NOT NULL,
    description_year INTEGER -- year description began
);

INSERT INTO naics_description_history (code, description, description_year) VALUES (311221,'Wet Corn Milling',2017);
INSERT INTO naics_description_history (code, description, description_year) VALUES (311221,'Wet Corn Milling and Starch Manufacturing',2022);
INSERT INTO naics_description_history (code, description, description_year) VALUES (325992,'Photographic Film, Paper, Plate, and Chemical Manufacturing',2017);
INSERT INTO naics_description_history (code, description, description_year) VALUES (325992,'Photographic Film, Paper, Plate, Chemical, and Copy Toner Manufacturing',2022);
INSERT INTO naics_description_history (code, description, description_year) VALUES (424940,'Tobacco and Tobacco Product Merchant Wholesalers',2017);
INSERT INTO naics_description_history (code, description, description_year) VALUES (424940,'Tobacco Product and Electronic Cigarette Merchant Wholesalers',2022);
INSERT INTO naics_description_history (code, description, description_year) VALUES (444120,'Paint and Wallpaper Stores',2017);
INSERT INTO naics_description_history (code, description, description_year) VALUES (444120,'Paint and Wallpaper Retailers',2022);
INSERT INTO naics_description_history (code, description, description_year) VALUES (445110,'Supermarkets and Other Grocery (except Convenience) Stores',2017);
INSERT INTO naics_description_history (code, description, description_year) VALUES (445110,'Supermarkets and Other Grocery Retailers (except Convenience Retailers)',2022);
INSERT INTO naics_description_history (code, description, description_year) VALUES (445230,'Fruit and Vegetable Markets',2017);
INSERT INTO naics_description_history (code, description, description_year) VALUES (445230,'Fruit and Vegetable Retailers',2022);
INSERT INTO naics_description_history (code, description, description_year) VALUES (445291,'Baked Goods Stores',2017);
INSERT INTO naics_description_history (code, description, description_year) VALUES (445291,'Baked Goods Retailers',2022);
INSERT INTO naics_description_history (code, description, description_year) VALUES (445292,'Confectionery and Nut Stores',2017);
INSERT INTO naics_description_history (code, description, description_year) VALUES (445292,'Confectionery and Nut Retailers',2022);
INSERT INTO naics_description_history (code, description, description_year) VALUES (485310,'Taxi Service',2017);
INSERT INTO naics_description_history (code, description, description_year) VALUES (485310,'Taxi and Ridesharing Services',2022);
INSERT INTO naics_description_history (code, description, description_year) VALUES (518210,'Data Processing, Hosting, and Related Services',2017);
INSERT INTO naics_description_history (code, description, description_year) VALUES (518210,'Computing Infrastructure Providers, Data Processing, Web Hosting, and Related Services',2022);
INSERT INTO naics_description_history (code, description, description_year) VALUES (524292,'Third Party Administration of Insurance and Pension Funds',2017);
INSERT INTO naics_description_history (code, description, description_year) VALUES (524292,'Pharmacy Benefit Management and Other Third Party Administration of Insurance and Pension Funds',2022);
INSERT INTO naics_description_history (code, description, description_year) VALUES (541380,'Testing Laboratories',2017);
INSERT INTO naics_description_history (code, description, description_year) VALUES (541380,'Testing Laboratories and Services',2022);
INSERT INTO naics_description_history (code, description, description_year) VALUES (541850,'Outdoor Advertising',2017);
INSERT INTO naics_description_history (code, description, description_year) VALUES (541850,'Indoor and Outdoor Display Advertising',2022);
INSERT INTO naics_description_history (code, description, description_year) VALUES (561611,'Investigation Services',2017);
INSERT INTO naics_description_history (code, description, description_year) VALUES (561611,'Investigation and Personal Background Check Services',2022);
INSERT INTO naics_description_history (code, description, description_year) VALUES (624410,'Child Day Care Services',2017);
INSERT INTO naics_description_history (code, description, description_year) VALUES (624410,'Child Care Services',2022);