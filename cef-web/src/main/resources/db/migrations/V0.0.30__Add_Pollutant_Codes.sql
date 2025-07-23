CREATE TABLE pollutant
(
    pollutant_code character varying(12) NOT NULL,
    pollutant_name character varying(200) NOT NULL,
    pollutant_cas_id character varying(100),
    pollutant_srs_id character varying(40) NOT NULL, 
    pollutant_type character varying(12) NOT NULL,
    pollutant_standard_uom_code character varying(20),
    PRIMARY KEY (pollutant_code)
);

Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('OC','Organic Carbon portion of PM2.5-PRI','','OTH','TON');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('DIESEL-PM10','PM10-Primary from certain diesel engines','','OTH','TON');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('DIESEL-PM25','PM25-Primary from certain diesel engines','','OTH','TON');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('NO3','Nitrate portion of PM2.5-PRI','','OTH','TON');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('SO4','Sulfate Portion of PM2.5-PRI','','OTH','TON');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('PMFINE','Remaining PMFINE portion of PM2.5-PRI','','OTH','TON');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('10025737','Chromium (III) Chloride','','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('1002671','Diethylene Glycol Ethyl Methyl Ether','524671930','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('100414','Ethyl Benzene','19406','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('100425','Styrene','19414','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('10043660','Iodine 131','650515','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('100447','Benzyl Chloride','19430','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('10060125','Chromium Chloride, Hexahydrate','524671898','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('10099748','Lead Nitrate','167692','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('10101970','Nickel (II) Sulfate Hexahydrate','167858','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('10137969','Ethyleneglycol Mono-2-Methylpentyl Ether','524672003','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('10137981','Ethyleneglycolmono-2,6,8-Trimethyl-4-Nonyl Ether','','OTH','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('10143541','Diethylene Glycol Mono-2-Cyanoethyl Ether','','OTH','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('10143563','Diethyleneglycol-Mono-2-Methyl-Pentyl Ether','524671963','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('101688','4,4''-Methylenediphenyl Diisocyanate','20263','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('102','Benzo[b+k]Fluoranthene','','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('10215335','3-Butoxy-1-Propanol','','OTH','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('10294403','Barium Chromate','170001','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('10325947','Cadmium Nitrate','170217','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('10588019','Sodium Dichromate','172593','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('106423','p-Xylene','23580','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('106467','1,4-Dichlorobenzene','23622','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('106514','Quinone','23671','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('106887','1,2-Epoxybutane','23937','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('106898','Epichlorohydrin','23945','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('106990','1,3-Butadiene','24042','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('107028','Acrolein','24075','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('107051','Allyl Chloride','24109','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('107131','Acrylonitrile','24182','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('107211','Ethylene Glycol','24257','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('108054','Vinyl Acetate','24828','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('108101','Methyl Isobutyl Ketone','24851','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('108383','m-Xylene','25056','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('108883','Toluene','25452','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('108907','Chlorobenzene','25478','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('108952','Phenol','25510','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('109864','Ethylene Glycol Methyl Ether','26211','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('110805','Cellosolve Solvent','26989','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('11103869','Zinc Potassium Chromate','173146','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('111104','Methoxyethyl Oleate','27235','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('111422','Diethanolamine','27516','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('111762','Butyl Cellosolve','','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('111773','Diethylene Glycol Monomethyl Ether','27854','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('1120714','1,3-Propanesultone','79012','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('112072','2-Butoxyethyl Acetate','28100','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('112276','Triethylene glycol','','OTH','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('112345','Diethylene Glycol Monobutyl Ether','28340','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('112356','Methoxytriglycol','28357','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('112492','Triethylene Glycol Dimethyl Ether','28472','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('112594','N-Hexyl Carbitol','28563','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('118741','Hexachlorobenzene','30346','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('119937','3,3''-Dimethylbenzidine','30940','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('78002','Tetraethyl Lead','6825','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('7803512','Phosphine','156588','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('37871004','Total Heptachlorodibenzo-p-Dioxin','275438','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('383','Fine Mineral Fibers','649533','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('38998753','Total Heptachlorodibenzofuran','278200','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('40','16-PAH','','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('40321764','1,2,3,7,8-Pentachlorodibenzo-p-Dioxin','650911','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('41637905','Methylchrysene','524672110','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('4206615','Diethylene Glycol Diglycidyl Ether','','OTH','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('463581','Carbonyl Sulfide','46508','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('50000','Formaldehyde','1008','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('50922297','Zinc Chromite','290031','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('51796','Ethyl Carbamate','1495','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('534521','4,6-Dinitro-o-Cresol','51102','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('540885','Tert-butyl Acetate','','OTH','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('543908','Cadmium acetate','52654','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('544923','Copper Cyanide','52886','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('55684941','Total Hexachlorodibenzofuran','304824','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('56235','Carbon Tetrachloride','2071','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('56382','Parathion','2147','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('56553','Benz[a]Anthracene','2212','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('57147','1,1-Dimethyl Hydrazine','2469','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('57653857','1,2,3,6,7,8-Hexachlorodibenzo-p-Dioxin','694281','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('110496','Ethylene Glycol Monomethyl Ether Acetate','26708','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('111444','Dichloroethyl Ether','27532','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('111900','Diethylene Glycol Monoethyl Ether','27979','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('112254','2-(Hexyloxy)Ethanol','28266','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('112367','Diethylene Glycol Diethyl Ether','28365','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('119904','3,3''-Dimethoxybenzidine','30924','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('12018018','Chromium Dioxide','175208','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('121142','2,4-Dinitrotoluene','31575','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('121697','N,N-Dimethylaniline','31880','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('123386','Propionaldehyde','32953','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('125','Cadmium & Compounds','','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('126998','Chloroprene','34090','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('127184','Tetrachloroethylene','34157','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('1303282','Arsenic Pentoxide','82057','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('1308141','Chromium Hydroxide','82412','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('67562394','1,2,3,4,6,7,8-Heptachlorodibenzofuran','358382','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('684935','N-Nitroso-N-Methylurea','67462','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('70648269','1,2,3,4,7,8-Hexachlorodibenzofuran','525212','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('72918219','1,2,3,7,8,9-Hexachlorodibenzofuran','570150','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('7440020','Nickel','149674','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('7440360','Antimony','149963','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('7440417','Beryllium','150003','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('7440484','Cobalt','150078','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('7446084','Selenium Dioxide','150359','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('74839','Methyl Bromide','5074','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('74884','Methyl Iodide','5124','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('7496028','6-Nitrochrysene','524671872','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('75070','Acetaldehyde','5280','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('75150','Carbon Disulfide','5348','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('75343','Ethylidene Dichloride','5520','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('7550450','Titanium Tetrachloride','','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('764487','Ethylene Glycol Monovinyl Ether','','OTH','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('7664393','Hydrogen Fluoride','152371','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('7723140','Phosphorus','153049','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('7758976','Lead Chromate','153528','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('7778509','Potassium Dichromate','153916','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('7783791','Selenium Hexafluoride','689554','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('7789062','Strontium Chromate','155655','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('779022','9-Methyl Anthracene','70367','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('79005','1,1,2-Trichloroethane','7518','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('79118','Chloroacetic Acid','7625','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('79447','Dimethylcarbamoyl Chloride','7864','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('144','Cyanide & Compounds','','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('151508','Potassium Cyanide','40261','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('156627','Calcium Cyanamide','','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('1589497','3-Methoxy-1-Propanol','','OTH','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('189559','Dibenzo[a,i]Pyrene','40543','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('191300','Dibenzo[a,l]Pyrene','40584','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('192972','Benzo[e]Pyrene','40618','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('19408743','1,2,3,7,8,9-Hexachlorodibenzo-p-Dioxin','214783','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('199','Mercury & Compounds','','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('203123','Benzo(g,h,i)Fluoranthene','524671880','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('2051243','Decachlorobiphenyl (PCB-209)','','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('205992','Benzo[b]Fluoranthene','40683','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('207089','Benzo[k]Fluoranthene','40709','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('218019','Chrysene','40733','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('226','Nickel & Compounds','','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('23436193','1-Isobutoxy-2-Propanol','','OTH','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('2381217','1-Methylpyrene','961656','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('250','PAH/POM - Unspecified','','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('83329','Acenaphthene','9555','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('84742','Dibutyl Phthalate','10025','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('57749','Chlordane','2683','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('57976','7,12-Dimethylbenz[a]Anthracene','2733','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('58899','1,2,3,4,5,6-Hexachlorocyclohexane','2923','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('593602','Vinyl Bromide','57448','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('593748','Methyl Mercury','57471','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('600','Dioxins/Furans as 2,3,7,8-TCDD TEQs - TEQ scheme unspecified','17134024','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('602','Lead Compounds (Inorganic)','','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('602879','5-Nitroacenaphthene','','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('60344','Methylhydrazine','3376','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('60355','Acetamide','3384','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('604','Nickel Refinery Dust','','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('605','Radionuclides (Radioactivity - Including Radon)','','HAP','CURIE');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('607578','2-Nitrofluorene','59048','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('608','Ceramic Fibers (Man-Made Fibers)','17134149','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('613','Glasswool (Man-Made Fibers)','17132986','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('616','Slagwool (Man-Made Fibers)','649533','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('61789513','Cobalt Naphtha','321349','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('628','Dioxins/Furans as 2,3,7,8-TCDD TEQs - WHO2005','','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('624839','Methyl Isocyanate','63321','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('62533','Aniline','3632','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('627','Dioxins/Furans as 2,3,7,8-TCDD TEQs - WHO/98','17000407','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('62737','Dichlorvos','3681','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('62759','N-Nitrosodimethylamine','3707','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('629141','Ethylene Glycol Diethyl Ether','64634','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('63252','Carbaryl','3723','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('64675','Diethyl Sulfate','3889','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('67425','(Ethylenebis(Oxyethylenenitrilo)) Tetraacetic Acid','','OTH','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('67561','Methanol','4283','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('68122','N,N-Dimethylformamide','4416','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('693210','Diethylene Glycol Dinitrate','','OTH','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('7012375','2,4,4''-Trichlorobiphenyl (PCB-28)','','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('71432','Benzene','4754','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('72435','Methoxychlor','4903','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('72559','DDE (1,1-Dichloro-2,2-Bis(p-Chlorophenyl) Ethylene)','4945','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('7439921','Lead','','CAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('7439976','Mercury','149633','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('7440382','Arsenic','149989','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('7440439','Cadmium','150029','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('7446142','Lead Sulfate','150383','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('74873','Methyl Chloride','5116','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('74908','Hydrogen Cyanide','5140','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('75','7-PAH','17134057','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('75014','Vinyl Chloride','5231','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('75092','Methylene Chloride','5306','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('75252','Bromoform','5447','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('7529273','Ethylene Glycol Diallyl Ether','','OTH','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('75445','Phosgene','5587','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('75558','1,2-Propylenimine','5652','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('75569','Propylene Oxide','5660','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('764998','Diethylene Glycol Divinyl Ether','','OTH','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('7718549','Nickel Chloride','152868','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('7738945','Chromic Acid (VI)','153213','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('7775113','Sodium Chromate','153775','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('7778394','Arsenic Acid','153866','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('7782492','Selenium','154310','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('7784409','Lead Arsenate','155069','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('7786814','Nickel Sulfate','155366','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('7789006','Potassium Chromate','155630','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('7789095','Ammonium Dichromate','155663','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('7795917','Ethylene Glycol Mono-Sec-Butyl Ether','524671989','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('151564','Ethyleneimine','40279','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('1582098','Trifluralin','89060','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('16065831','Chromium III','203000','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('16672392','Di(Ethylene Glycol Monobutyl Ether) Phthalate','','OTH','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('16842038','Cobalt Hydrocarbonyl','524671922','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('171','Glycol Ethers','649996','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('18454121','Lead Chromate Oxide','212118','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('18540299','Chromium (VI)','212357','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('18912806','Diethylene Glycol Monoisobutyl Ether','213421','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('189640','Dibenzo[a,h]Pyrene','40550','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('192654','Dibenzo[a,e]Pyrene','40600','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('195','Lead & Compounds','','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('195197','Benzo(c)phenanthrene','524672417','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('200','Elemental Gaseous Mercury','149633','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('98077','Benzotrichloride','17756','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('98953','Nitrobenzene','18408','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('131113','Dimethyl Phthalate','35295','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('1319773','Cresol/Cresylic Acid (Mixed Isomers)','83550','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('51207319','2,3,7,8-Tetrachlorodibenzofuran','291005','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('532274','2-Chloroacetophenone','50740','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('540841','2,2,4-Trimethylpentane','51961','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('55673897','1,2,3,4,7,8,9-Heptachlorodibenzofuran','304782','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('56495','3-Methylcholanthrene','2188','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('57117314','2,3,4,7,8-Pentachlorodibenzofuran','308809','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('57835924','4-Nitropyrene','','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('584849','2,4-Toluene Diisocyanate','55939','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('60117','4-Dimethylaminoazobenzene','3269','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('60851345','2,3,4,6,7,8-Hexachlorodibenzofuran','317966','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('136677106','Polychlorinated Dibenzofurans, Total','629352','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('14307336','Chromic acid (H2Cr2O7), calcium salt (1:1)','194795','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('26914330','Tetrachlorobiphenyl','','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('39001020','Octachlorodibenzofuran','278218','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('10101538','Chromic Sulfate','167759','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('106445','p-Cresol','23606','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('106503','p-Phenylenediamine','23663','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('107302','Chloromethyl Methyl Ether','24299','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('CO','Carbon Monoxide','65052','CAP','TON');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('HFC','Hydrofluorocarbons','','GHG','TON');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('N2O','Nitrous Oxide','','GHG','TON');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('NOX','Nitrogen Oxides','17134099','CAP','TON');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('PM25-PRI','PM2.5 Primary (Filt + Cond)','524672300','CAP','TON');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('PM-FIL','PM Filterable','524672326','CAP','TON');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('PM-PRI','PM Primary (Filt + Cond)','524672334','CAP','TON');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('VOC','Volatile Organic Compounds','761346','CAP','TON');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('28655712','Heptachlorobiphenyl','','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('302012','Hydrazine','41541','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('30402154','Total Pentachlorodibenzofuran','256289','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('34465468','Hexachlorodibenzo-p-Dioxin','266965','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('12079651','Manganese, tricarbonyl (.eta.5-2,4-cyclopentadien-1-yl)-','179184','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('123319','Hydroquinone','32904','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('124174','Butyl Carbitol Acetate','33449','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('130498292','PAH, total','627059','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('1308061','Cobalt Oxide (II,III)','82396','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('1313139','Manganese Dioxide','82784','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('1317357','Manganese Tetroxide','83337','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('132649','Dibenzofuran','35709','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('133062','Captan','35790','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('1332214','Asbestos','85282','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('133904','Chloramben','35956','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('13463393','Nickel Carbonyl','186866','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('136527','Hexanoic acid, 2-ethyl-, cobalt(2+) salt','36699','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('136677093','Dioxins, Total, W/O Individ. Isomers Reported {PCDDS}','629345','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('140885','Ethyl Acrylate','37929','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('14255040','Lead-210','','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('14274829','Thorium-228','','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('78591','Isophorone','7187','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('78875','Propylene Dichloride','7393','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('78933','Methyl Ethyl Ketone','','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('79345','1,1,2,2-Tetrachloroethane','7773','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('79469','2-Nitropropane','7872','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('8007452','Coal Tar','157958','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('822060','Hexamethylene Diisocyanate','71209','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('832699','1-Methylphenanthrene','71852','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('85018','Phenanthrene','10199','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('85449','Phthalic Anhydride','10355','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('86737','Fluorene','10892','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('86748','Carbazole','','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('87683','Hexachlorobutadiene','11346','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('87865','Pentachlorophenol','11437','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('88062','2,4,6-Trichlorophenol','11536','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('90040','o-Anisidine','12815','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('90120','1-Methylnaphthalene','12849','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('91203','Naphthalene','13326','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('91225','Quinoline','13342','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('91576','2-Methylnaphthalene','13524','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('91587','2-Chloronaphthalene','13532','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('91941','3,3''-Dichlorobenzidine','13755','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('92','Antimony & Compounds','','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('92524','Biphenyl','14183','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('92671','4-Aminobiphenyl','14308','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('92875','Benzidine','14498','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('92933','4-Nitrobiphenyl','14548','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('929373','Diethylene Glycol Monovinyl Ether','','OTH','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('93','Arsenic & Compounds (Inorganic Including Arsine)','','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('94757','2,4-Dichlorophenoxy Acetic Acid','15651','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('95476','o-Xylene','16139','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('95487','o-Cresol','16147','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('95534','o-Toluidine','16196','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('95807','Toluene-2,4-Diamine','16410','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('95954','2,4,5-Trichlorophenol','16519','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('96093','Styrene Oxide','16584','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('96128','1,2-Dibromo-3-Chloropropane','16618','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('96457','Ethylene Thiourea','16857','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('98828','Cumene','18309','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('98862','Acetophenone','18333','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('CH4','Methane','','GHG','TON');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('CO2','Carbon Dioxide','','GHG','TON');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('NH3','Ammonia','152389','CAP','TON');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('PFC','Perfluorocarbons','','GHG','TON');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('PM10-FIL','PM10 Filterable','524672276','CAP','TON');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('PM10-PRI','PM10 Primary (Filt + Cond)','524672284','CAP','TON');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('PM25-FIL','PM2.5 Filterable','524672292','CAP','TON');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('PM-CON','PM Condensible','524672318','CAP','TON');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('SF6','Sulfur Hexafluoride','','GHG','TON');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('SO2','Sulfur Dioxide','150367','CAP','TON');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('1330207','Xylenes (Mixed Isomers)','84970','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('1336363','Polychlorinated Biphenyls','85878','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('50328','Benzo[a]Pyrene','1115','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('53703','Dibenzo[a,h]Anthracene','1685','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('542881','Bis(Chloromethyl)Ether','52498','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('57125','Cyanide','2444','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('57578','Beta-Propiolactone','2600','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('59892','N-Nitrosomorpholine','3194','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('603','Lead Compounds (Other Than Inorganic)','','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('606','Radon And Its Decay Products','','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('617','Rockwool (Man-Made Fibers)','649533','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('622082','Ethylene Glycol Monobenzyl Ether','62398','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('13530682','Chromic Sulfuric Acid','17000431','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('13966002','Potassium-40','','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('13982633','Radium-226','','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('142','Methylene Chloride Soluble Organics (MCSO)','17133984','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('14269637','Thorium-230','','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('143339','Sodium Cyanide','39305','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('30402143','Total Tetrachlorodibenzofuran','256271','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('3268879','Octachlorodibenzo-p-Dioxin','113837','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('36088229','Total Pentachlorodibenzo-p-Dioxin','270520','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('41903575','Total Tetrachlorodibenzo-p-Dioxin','284596','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('4439241','Isobutyl Cellosolve','124859','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('626','Dioxins/Furans as 2,3,7,8-TCDD TEQs -I/89','17000407','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('65357699','Methylbenzopyrene','524672102','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('100027','4-Nitrophenol','19117','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('101144','4,4''-Methylenebis(2-Chloraniline)','19943','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('10143530','Diethylene Glycol Ethylvinyl Ether','','OTH','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('101779','4,4''-Methylenedianiline','20347','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('106934','Ethylene Dibromide','23986','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('107062','Ethylene Dichloride','24117','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('108316','Maleic Anhydride','25015','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('108394','m-Cresol','25064','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('109','Beryllium & Compounds','','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('EC','Elemental Carbon portion of PM2.5-PRI','','OTH','TON');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('201','Gaseous Divalent Mercury','17016817','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('202','Particulate Divalent Mercury','17016817','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('203338','Benzo(a)Fluoranthene','40659','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('2050682','4,4''-Dichlorobiphenyl (PCB-15)','','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('2051607','2-Chlorobiphenyl (PCB-1)','','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('205823','Benzo[j]fluoranthene','40675','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('206440','Fluoranthene','40691','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('20706256','2-Propoxyethyl Acetate','650762','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('208968','Acenaphthylene','40717','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('224420','Dibenzo[a,j]Acridine','40741','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('226368','Dibenz[a,h]acridine','','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('22967926','Mercury (Organic)','222877','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('23495127','Ethyleneglycol Monophenyl Ether Propionate','224030','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('2422799','12-Methylbenz(a)Anthracene','17000381','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('246','Polycyclic Organic Matter','','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('249','15-PAH','17000415','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('253','Selenium & Compounds','','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('25429292','Pentachlorobiphenyl','','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('26601649','Hexachlorobiphenyl','','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('26914181','Methylanthracene','52450418','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('27310210','2-(2,4-Hexadienyloxy)Ethanol','','OTH','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('2807309','Propyl Cellosolve','107573','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('284','Extractable Organic Matter (EOM)','','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('301042','Lead Acetate','41483','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('3121617','Methyl Cellosolve Acrylate','111963','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('334883','Diazomethane','42762','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('35822469','1,2,3,4,6,7,8-Heptachlorodibenzo-p-Dioxin','270140','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('3697243','5-Methylchrysene','118240','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('373024','Nickel Acetate','44230','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('3775857','Ethylene Glycol Bis(2,3-Epoxy-2-Methylpropyl) Ether','','OTH','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('12035722','Nickel Subsulfide','176768','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('120558','Diethylene Glycol Dibenzoate','','OTH','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('120809','Catechol','31427','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('120821','1,2,4-Trichlorobenzene','31435','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('121448','Triethylamine','31708','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('122667','1,2-Diphenylhydrazine','32425','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('122996','Phenyl Cellosolve','32664','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('123911','p-Dioxane','33241','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('12710360','Nickel Carbide','524672060','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('129000','Pyrene','34843','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('1304569','Beryllium Oxide','82214','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('1306190','Cadmium Oxide','82313','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('1307966','Cobalt Oxide','82362','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('1308389','Chromic Oxide','82420','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('1309600','Lead Dioxide','82537','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('1309644','Antimony Trioxide','82545','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('13138459','Nickel Nitrate','184259','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('1313991','Nickel Oxide','82891','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('1317346','Manganese Trioxide','83329','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('1317368','Lead (II) Oxide','83345','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('1327339','Antimony Oxide','84681','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('1327533','Arsenic Trioxide','84749','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('1333820','Chromium Trioxide','85571','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('1335326','Lead Subacetate','85753','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('1345160','Cobalt Aluminate','86322','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('13530659','Zinc Chromate','188110','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('136','Chromium & Compounds','','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('13765190','Calcium Chromate','190694','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('13770893','Nickel Sulfamate','190868','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('139','Cobalt & Compounds','','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('13981527','Polonium-210','','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('140','Coke Oven Emissions','','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('140056','Methyl Cellosolve Acetylricinoleate','524672052','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('141','Benzene Soluble Organics (BSO)','17133927','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('143226','Triglycol Monobutyl Ether','39248','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('78820','2-Methyl-Propanenitrile','7344','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('79016','Trichloroethylene','7526','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('79061','Acrylamide','7575','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('79107','Acrylic Acid','7617','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('8001352','Toxaphene','156919','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('80626','Methyl Methacrylate','8458','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('82688','Pentachloronitrobenzene','9373','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('110543','Hexane','26740','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('110714','1,2-Dimethoxyethane','26906','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('111159','Cellosolve Acetate','27284','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('111966','Diethylene Glycol Dimethyl Ether','28027','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('112152','Carbitol Acetate','28167','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('112505','Ethoxytriglycol','28480','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('114261','Propoxur','28928','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('117817','Bis(2-Ethylhexyl)Phthalate','29934','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('120127','Anthracene','31013','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('12018198','Chromium Zinc Oxide','175257','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('39227286','1,2,3,4,7,8-Hexachlorodibenzo-p-Dioxin','711986','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('42397648','1,6-Dinitropyrene','524671849','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('42397659','1,8-Dinitropyrene','693218','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('510156','Chlorobenzilate','49320','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('51285','2,4-Dinitrophenol','1313','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('53742077','Nonachlorobiphenyl','','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('53963','2-Acetylaminofluorene','1719','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('542756','1,3-Dichloropropene','52449','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('5522430','1-Nitropyrene','133595','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('557211','Zinc Cyanide','54213','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('55722264','Octachlorobiphenyl','','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('56832736','Benzofluoranthenes','308023','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('57117416','1,2,3,7,8-Pentachlorodibenzofuran','308817','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('57117449','1,2,3,6,7,8-Hexachlorodibenzofuran','308825','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('67663','Chloroform','4317','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('67721','Hexachloroethane','4341','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('680319','Hexamethylphosphoramide','67314','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('71556','Methyl Chloroform','4796','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('7439965','Manganese','149625','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('7440291','Thorium-232','','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('7440473','Chromium','150060','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('7440611','Uranium-238','150185','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('7487947','Mercuric Chloride','150789','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('75003','Ethyl Chloride','5223','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('75058','Acetonitrile','5272','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('75218','Ethylene Oxide','5405','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('75354','Vinylidene Chloride','5538','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('76448','Heptachlor','6262','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('7647010','Hydrochloric Acid','152231','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('77474','Hexachlorocyclopentadiene','6494','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('77781','Dimethyl Sulfate','6684','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('7782505','Chlorine','154328','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('7784421','Arsine','155085','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('7785877','Manganese Sulfate','155259','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('7788989','Ammonium chromate','155622','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('14859677','Radon-222','','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('155','Dioxins','','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('1634044','Methyl Tert-Butyl Ether','89870','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('1746016','2,3,7,8-Tetrachlorodibenzo-p-Dioxin','91918','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('191242','Benzo[g,h,i,]Perylene','40576','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('193395','Indeno[1,2,3-c,d]Pyrene','40626','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('194592','7H-Dibenzo[c,g]carbazole','','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('198','Manganese & Compounds','','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('198550','Perylene','40642','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('7783064','Hydrogen Sulfide','154518','OTH','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('605A','Radionuclides (Mass - Including Radon)','','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('106945','1-Bromopropane','','OTH','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('2531842','2-Methylphenanthrene','','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('N590','Polycyclic aromatic compounds (includes 25 specific compounds)','','HAP','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('52663726','2,3'',4,4'',5,5''-Hexachlorobiphenyl (PCB-167)','','','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('31508006','2,3'',4,4'',5-Pentachlorobiphenyl (PCB118)','','','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('32598133','3,3'',4,4''-Tetrachlorobiphenyl (PCB-77)','','','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('32598144','2,3,3'',4,4''-Pentachlorobiphenyl (PCB-105)','','','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('74472370','2,3,4,4'',5-Pentachlorobiphenyl (PCB-114)','','','LB');
Insert into pollutant (pollutant_code, pollutant_name, pollutant_srs_id, pollutant_type, pollutant_standard_uom_code) values ('38380084','2,3,3'',4,4'',5/2,3,3'',4,4'',5-Hexachlorobiphenyl  (PCBs156/157)','','','LB');


Update pollutant set pollutant_cas_id = '112-15-2' where pollutant_srs_id = '28167';
Update pollutant set pollutant_cas_id = '198-55-0' where pollutant_srs_id = '40642';
Update pollutant set pollutant_cas_id = '10024-97-2' where pollutant_srs_id = '166389';
Update pollutant set pollutant_cas_id = '42397-64-8' where pollutant_srs_id = '524671849';
Update pollutant set pollutant_cas_id = '1307-96-6' where pollutant_srs_id = '82362';
Update pollutant set pollutant_cas_id = '61790-14-5' where pollutant_srs_id = '321760';
Update pollutant set pollutant_cas_id = '41637-90-5' where pollutant_srs_id = '524672110';
Update pollutant set pollutant_cas_id = '5522-43-0' where pollutant_srs_id = '133595';
Update pollutant set pollutant_cas_id = '75-21-8' where pollutant_srs_id = '5405';
Update pollutant set pollutant_cas_id = '121-69-7' where pollutant_srs_id = '31880';
Update pollutant set pollutant_cas_id = '101-02-0' where pollutant_srs_id = '19877';
Update pollutant set pollutant_cas_id = '126-99-8' where pollutant_srs_id = '34090';
Update pollutant set pollutant_cas_id = '92-93-3' where pollutant_srs_id = '14548';
Update pollutant set pollutant_cas_id = '6-4-7783' where pollutant_srs_id = '154518';
Update pollutant set pollutant_cas_id = '111-90-0' where pollutant_srs_id = '27979';
Update pollutant set pollutant_cas_id = '18912-80-6' where pollutant_srs_id = '213421';
Update pollutant set pollutant_cas_id = '12054-48-7' where pollutant_srs_id = '178129';
Update pollutant set pollutant_cas_id = '10101-50-5' where pollutant_srs_id = '167734';
Update pollutant set pollutant_cas_id = '12-6-3141' where pollutant_srs_id = '17005653';
Update pollutant set pollutant_cas_id = '67-72-1' where pollutant_srs_id = '4341';
Update pollutant set pollutant_cas_id = '7440-29-1' where pollutant_srs_id = '149906';
Update pollutant set pollutant_cas_id = '7439-97-6' where pollutant_srs_id = '149633';
Update pollutant set pollutant_cas_id = '7784-40-9' where pollutant_srs_id = '155069';
Update pollutant set pollutant_cas_id = '130498-29-2' where pollutant_srs_id = '627059';
Update pollutant set pollutant_cas_id = '26914-18-1' where pollutant_srs_id = '52450418';
Update pollutant set pollutant_cas_id = '12035-72-2' where pollutant_srs_id = '176768';
Update pollutant set pollutant_cas_id = '8007-45-2' where pollutant_srs_id = '157958';
Update pollutant set pollutant_cas_id = '7786-81-4' where pollutant_srs_id = '155366';
Update pollutant set pollutant_cas_id = '13530-65-9' where pollutant_srs_id = '188110';
Update pollutant set pollutant_cas_id = '79-01-6' where pollutant_srs_id = '7526';
Update pollutant set pollutant_cas_id = '50-00-0' where pollutant_srs_id = '1008';
Update pollutant set pollutant_cas_id = '10294-40-3' where pollutant_srs_id = '170001';
Update pollutant set pollutant_cas_id = '30402-14-3' where pollutant_srs_id = '256271';
Update pollutant set pollutant_cas_id = '2422-79-9' where pollutant_srs_id = '17000381';
Update pollutant set pollutant_cas_id = '108-90-7' where pollutant_srs_id = '25478';
Update pollutant set pollutant_cas_id = '2551-62-4' where pollutant_srs_id = '104364';
Update pollutant set pollutant_cas_id = '95-80-7' where pollutant_srs_id = '16410';
Update pollutant set pollutant_cas_id = '7439-97-6' where pollutant_srs_id = '149633';
Update pollutant set pollutant_cas_id = '7782-49-2' where pollutant_srs_id = '154310';
Update pollutant set pollutant_cas_id = '189-64-0' where pollutant_srs_id = '40550';
Update pollutant set pollutant_cas_id = '56832-73-6' where pollutant_srs_id = '308023';
Update pollutant set pollutant_cas_id = '51-79-6' where pollutant_srs_id = '1495';
Update pollutant set pollutant_cas_id = '680-31-9' where pollutant_srs_id = '67314';
Update pollutant set pollutant_cas_id = '6-2-7789' where pollutant_srs_id = '155655';
Update pollutant set pollutant_cas_id = '79-00-5' where pollutant_srs_id = '7518';
Update pollutant set pollutant_cas_id = '308066-92-4' where pollutant_srs_id = '17132986';
Update pollutant set pollutant_cas_id = '119-93-7' where pollutant_srs_id = '30940';
Update pollutant set pollutant_cas_id = '57-14-7' where pollutant_srs_id = '2469';
Update pollutant set pollutant_cas_id = '593-74-8' where pollutant_srs_id = '57471';
Update pollutant set pollutant_cas_id = '13814-96-5' where pollutant_srs_id = '191437';
Update pollutant set pollutant_cas_id = '7440-38-2' where pollutant_srs_id = '149989';
Update pollutant set pollutant_cas_id = '3697-24-3' where pollutant_srs_id = '118240';
Update pollutant set pollutant_cas_id = '602-87-9' where pollutant_srs_id = '58495';
Update pollutant set pollutant_cas_id = '37187-64-7' where pollutant_srs_id = '524672029';
Update pollutant set pollutant_cas_id = '16842-03-8' where pollutant_srs_id = '524671922';
Update pollutant set pollutant_cas_id = '13510-49-1' where pollutant_srs_id = '187930';
Update pollutant set pollutant_cas_id = '74-87-3' where pollutant_srs_id = '5116';
Update pollutant set pollutant_cas_id = '12626-81-2' where pollutant_srs_id = '182568';
Update pollutant set pollutant_cas_id = '7783-70-2' where pollutant_srs_id = '154823';
Update pollutant set pollutant_cas_id = '129-00-0' where pollutant_srs_id = '34843';
Update pollutant set pollutant_cas_id = '107-30-2' where pollutant_srs_id = '24299';
Update pollutant set pollutant_cas_id = '72-55-9' where pollutant_srs_id = '4945';
Update pollutant set pollutant_cas_id = '7488-56-4' where pollutant_srs_id = '150821';
Update pollutant set pollutant_cas_id = '3121-61-7' where pollutant_srs_id = '111963';
Update pollutant set pollutant_cas_id = '12-2-7719' where pollutant_srs_id = '152918';
Update pollutant set pollutant_cas_id = '226-36-8' where pollutant_srs_id = '40774';
Update pollutant set pollutant_cas_id = '65357-69-9' where pollutant_srs_id = '524672102';
Update pollutant set pollutant_cas_id = '25429-29-2' where pollutant_srs_id = '232678';
Update pollutant set pollutant_cas_id = '58-89-9' where pollutant_srs_id = '2923';
Update pollutant set pollutant_cas_id = '19408-74-3' where pollutant_srs_id = '214783';
Update pollutant set pollutant_cas_id = '21679-31-2' where pollutant_srs_id = '220327';
Update pollutant set pollutant_cas_id = '10043-66-0' where pollutant_srs_id = '650515';
Update pollutant set pollutant_cas_id = '61789-51-3' where pollutant_srs_id = '321349';
Update pollutant set pollutant_cas_id = '75-55-8' where pollutant_srs_id = '5652';
Update pollutant set pollutant_cas_id = '607-57-8' where pollutant_srs_id = '59048';
Update pollutant set pollutant_cas_id = '7487-94-7' where pollutant_srs_id = '150789';
Update pollutant set pollutant_cas_id = '7439-96-5' where pollutant_srs_id = '149625';
Update pollutant set pollutant_cas_id = '60-35-5' where pollutant_srs_id = '3384';
Update pollutant set pollutant_cas_id = '194-59-2' where pollutant_srs_id = '40634';
Update pollutant set pollutant_cas_id = '57-74-9' where pollutant_srs_id = '2683';
Update pollutant set pollutant_cas_id = '107-06-2' where pollutant_srs_id = '24117';
Update pollutant set pollutant_cas_id = '7738-94-5' where pollutant_srs_id = '153213';
Update pollutant set pollutant_cas_id = '107-05-1' where pollutant_srs_id = '24109';
Update pollutant set pollutant_cas_id = '373-02-4' where pollutant_srs_id = '44230';
Update pollutant set pollutant_cas_id = '10137-96-9' where pollutant_srs_id = '524672003';
Update pollutant set pollutant_cas_id = '91-94-1' where pollutant_srs_id = '13755';
Update pollutant set pollutant_cas_id = '40321-76-4' where pollutant_srs_id = '650911';
Update pollutant set pollutant_cas_id = '39001-02-0' where pollutant_srs_id = '278218';
Update pollutant set pollutant_cas_id = '82-68-8' where pollutant_srs_id = '9373';
Update pollutant set pollutant_cas_id = '7428-48-0' where pollutant_srs_id = '149450';
Update pollutant set pollutant_cas_id = '111-44-4' where pollutant_srs_id = '27532';
Update pollutant set pollutant_cas_id = '119-90-4' where pollutant_srs_id = '30924';
Update pollutant set pollutant_cas_id = '124-38-9' where pollutant_srs_id = '33548';
Update pollutant set pollutant_cas_id = '13967-50-5' where pollutant_srs_id = '192732';
Update pollutant set pollutant_cas_id = '56-38-2' where pollutant_srs_id = '2147';
Update pollutant set pollutant_cas_id = '506-64-9' where pollutant_srs_id = '49031';
Update pollutant set pollutant_cas_id = '10137-98-1' where pollutant_srs_id = '524672011';
Update pollutant set pollutant_cas_id = '140-88-5' where pollutant_srs_id = '37929';
Update pollutant set pollutant_cas_id = '62-75-9' where pollutant_srs_id = '3707';
Update pollutant set pollutant_cas_id = '191-24-2' where pollutant_srs_id = '40576';
Update pollutant set pollutant_cas_id = '26914-33-0' where pollutant_srs_id = '242784';
Update pollutant set pollutant_cas_id = '72918-21-9' where pollutant_srs_id = '570150';
Update pollutant set pollutant_cas_id = '1308-38-9' where pollutant_srs_id = '82420';
Update pollutant set pollutant_cas_id = '205-82-3' where pollutant_srs_id = '40675';
Update pollutant set pollutant_cas_id = '132-64-9' where pollutant_srs_id = '35709';
Update pollutant set pollutant_cas_id = '624-83-9' where pollutant_srs_id = '63321';
Update pollutant set pollutant_cas_id = '510-15-6' where pollutant_srs_id = '49320';
Update pollutant set pollutant_cas_id = '74-90-8' where pollutant_srs_id = '5140';
Update pollutant set pollutant_cas_id = '106-88-7' where pollutant_srs_id = '23937';
Update pollutant set pollutant_cas_id = '106-94-5' where pollutant_srs_id = '23994';
Update pollutant set pollutant_cas_id = '608-73-1' where pollutant_srs_id = '59220';
Update pollutant set pollutant_cas_id = '1271-28-9' where pollutant_srs_id = '81687';
Update pollutant set pollutant_cas_id = '10099-74-8' where pollutant_srs_id = '167692';
Update pollutant set pollutant_cas_id = '13011-54-6' where pollutant_srs_id = '183285';
Update pollutant set pollutant_cas_id = '7440-43-9' where pollutant_srs_id = '150029';
Update pollutant set pollutant_cas_id = '18454-12-1' where pollutant_srs_id = '212118';
Update pollutant set pollutant_cas_id = '63-25-2' where pollutant_srs_id = '3723';
Update pollutant set pollutant_cas_id = '26601-64-9' where pollutant_srs_id = '240572';
Update pollutant set pollutant_cas_id = '86-74-8' where pollutant_srs_id = '10900';
Update pollutant set pollutant_cas_id = '9-8-7452' where pollutant_srs_id = '524671914';
Update pollutant set pollutant_cas_id = '96-45-7' where pollutant_srs_id = '16857';
Update pollutant set pollutant_cas_id = '83-32-9' where pollutant_srs_id = '9555';
Update pollutant set pollutant_cas_id = '112-59-4' where pollutant_srs_id = '28563';
Update pollutant set pollutant_cas_id = '191-30-0' where pollutant_srs_id = '40584';
Update pollutant set pollutant_cas_id = '630-08-0' where pollutant_srs_id = '65052';
Update pollutant set pollutant_cas_id = '1313-13-9' where pollutant_srs_id = '82784';
Update pollutant set pollutant_cas_id = '111-46-6' where pollutant_srs_id = '27557';
Update pollutant set pollutant_cas_id = '262-12-4' where pollutant_srs_id = '685305';
Update pollutant set pollutant_cas_id = '7550-45-0' where pollutant_srs_id = '151449';
Update pollutant set pollutant_cas_id = '106-89-8' where pollutant_srs_id = '23945';
Update pollutant set pollutant_cas_id = '192-65-4' where pollutant_srs_id = '40600';
Update pollutant set pollutant_cas_id = '764-48-7' where pollutant_srs_id = '524671997';
Update pollutant set pollutant_cas_id = '151-50-8' where pollutant_srs_id = '40261';
Update pollutant set pollutant_cas_id = '7446-14-2' where pollutant_srs_id = '150383';
Update pollutant set pollutant_cas_id = '74-82-8' where pollutant_srs_id = '5066';
Update pollutant set pollutant_cas_id = '28655-71-2' where pollutant_srs_id = '250308';
Update pollutant set pollutant_cas_id = '95-48-7' where pollutant_srs_id = '16147';
Update pollutant set pollutant_cas_id = '108-39-4' where pollutant_srs_id = '25064';
Update pollutant set pollutant_cas_id = '20706-25-6' where pollutant_srs_id = '650762';
Update pollutant set pollutant_cas_id = '78-00-2' where pollutant_srs_id = '6825';
Update pollutant set pollutant_cas_id = '136677-09-3' where pollutant_srs_id = '629345';
Update pollutant set pollutant_cas_id = '72-43-5' where pollutant_srs_id = '4903';
Update pollutant set pollutant_cas_id = '27253-28-7' where pollutant_srs_id = '244889';
Update pollutant set pollutant_cas_id = '7788-96-7' where pollutant_srs_id = '524671906';
Update pollutant set pollutant_cas_id = '7440-61-1' where pollutant_srs_id = '150185';
Update pollutant set pollutant_cas_id = '118-74-1' where pollutant_srs_id = '30346';
Update pollutant set pollutant_cas_id = '12136-91-3' where pollutant_srs_id = '179689';
Update pollutant set pollutant_cas_id = '60-11-7' where pollutant_srs_id = '3269';
Update pollutant set pollutant_cas_id = '109-86-4' where pollutant_srs_id = '26211';
Update pollutant set pollutant_cas_id = '7788-98-9' where pollutant_srs_id = '155622';
Update pollutant set pollutant_cas_id = '100-41-4' where pollutant_srs_id = '19406';
Update pollutant set pollutant_cas_id = '55722-26-4' where pollutant_srs_id = '304964';
Update pollutant set pollutant_cas_id = '110-80-5' where pollutant_srs_id = '26989';
Update pollutant set pollutant_cas_id = '96-09-3' where pollutant_srs_id = '16584';
Update pollutant set pollutant_cas_id = '598-63-0' where pollutant_srs_id = '58123';
Update pollutant set pollutant_cas_id = '36088-22-9' where pollutant_srs_id = '270520';
Update pollutant set pollutant_cas_id = '110-49-6' where pollutant_srs_id = '26708';
Update pollutant set pollutant_cas_id = '10108-64-2' where pollutant_srs_id = '168096';
Update pollutant set pollutant_cas_id = '140-29-4' where pollutant_srs_id = '37739';
Update pollutant set pollutant_cas_id = '112-07-2' where pollutant_srs_id = '28100';
Update pollutant set pollutant_cas_id = '123-31-9' where pollutant_srs_id = '32904';
Update pollutant set pollutant_cas_id = '7529-27-3' where pollutant_srs_id = '151225';
Update pollutant set pollutant_cas_id = '2051-60-7' where pollutant_srs_id = '687368';
Update pollutant set pollutant_cas_id = '1306-19-0' where pollutant_srs_id = '82313';
Update pollutant set pollutant_cas_id = '106-44-5' where pollutant_srs_id = '23606';
Update pollutant set pollutant_cas_id = '12079-65-1' where pollutant_srs_id = '179184';
Update pollutant set pollutant_cas_id = '101-77-9' where pollutant_srs_id = '20347';
Update pollutant set pollutant_cas_id = '62-73-7' where pollutant_srs_id = '3681';
Update pollutant set pollutant_cas_id = '7787-49-7' where pollutant_srs_id = '155457';
Update pollutant set pollutant_cas_id = '10034-82-9' where pollutant_srs_id = '524672094';
Update pollutant set pollutant_cas_id = '7785-87-7' where pollutant_srs_id = '155259';
Update pollutant set pollutant_cas_id = '11103-86-9' where pollutant_srs_id = '173146';
Update pollutant set pollutant_cas_id = '94-75-7' where pollutant_srs_id = '15651';
Update pollutant set pollutant_cas_id = '2050-68-2' where pollutant_srs_id = '687350';
Update pollutant set pollutant_cas_id = '100-42-5' where pollutant_srs_id = '19414';
Update pollutant set pollutant_cas_id = '112-36-7' where pollutant_srs_id = '28365';
Update pollutant set pollutant_cas_id = '13943-58-3' where pollutant_srs_id = '192591';
Update pollutant set pollutant_cas_id = '540-84-1' where pollutant_srs_id = '51961';
Update pollutant set pollutant_cas_id = '7803-51-2' where pollutant_srs_id = '156588';
Update pollutant set pollutant_cas_id = '3268-87-9' where pollutant_srs_id = '113837';
Update pollutant set pollutant_cas_id = '136-52-7' where pollutant_srs_id = '36699';
Update pollutant set pollutant_cas_id = '53742-07-7' where pollutant_srs_id = '299743';
Update pollutant set pollutant_cas_id = '532-27-4' where pollutant_srs_id = '50740';
Update pollutant set pollutant_cas_id = '92-87-5' where pollutant_srs_id = '14498';
Update pollutant set pollutant_cas_id = '115-86-6' where pollutant_srs_id = '29272';
Update pollutant set pollutant_cas_id = '108-31-6' where pollutant_srs_id = '25015';
Update pollutant set pollutant_cas_id = '74-88-4' where pollutant_srs_id = '5124';
Update pollutant set pollutant_cas_id = '76-44-8' where pollutant_srs_id = '6262';
Update pollutant set pollutant_cas_id = '91-58-7' where pollutant_srs_id = '13532';
Update pollutant set pollutant_cas_id = '75-15-0' where pollutant_srs_id = '5348';
Update pollutant set pollutant_cas_id = '7718-54-9' where pollutant_srs_id = '152868';
Update pollutant set pollutant_cas_id = '111-76-2' where pollutant_srs_id = '27847';
Update pollutant set pollutant_cas_id = '1308-14-1' where pollutant_srs_id = '82412';
Update pollutant set pollutant_cas_id = '543-90-8' where pollutant_srs_id = '52654';
Update pollutant set pollutant_cas_id = '75-05-8' where pollutant_srs_id = '5272';
Update pollutant set pollutant_cas_id = '53-70-3' where pollutant_srs_id = '1685';
Update pollutant set pollutant_cas_id = '7784-42-1' where pollutant_srs_id = '155085';
Update pollutant set pollutant_cas_id = '10060-12-5' where pollutant_srs_id = '524671898';
Update pollutant set pollutant_cas_id = '1335-32-6' where pollutant_srs_id = '85753';
Update pollutant set pollutant_cas_id = '79-34-5' where pollutant_srs_id = '7773';
Update pollutant set pollutant_cas_id = '80-62-6' where pollutant_srs_id = '8458';
Update pollutant set pollutant_cas_id = '57117-41-6' where pollutant_srs_id = '308817';
Update pollutant set pollutant_cas_id = '7012-37-5' where pollutant_srs_id = '689331';
Update pollutant set pollutant_cas_id = '111-42-2' where pollutant_srs_id = '27516';
Update pollutant set pollutant_cas_id = '90-12-0' where pollutant_srs_id = '12849';
Update pollutant set pollutant_cas_id = '110-71-4' where pollutant_srs_id = '26906';
Update pollutant set pollutant_cas_id = '13586-82-8' where pollutant_srs_id = '188771';
Update pollutant set pollutant_cas_id = '7440-41-7' where pollutant_srs_id = '150003';
Update pollutant set pollutant_cas_id = '120-12-7' where pollutant_srs_id = '31013';
Update pollutant set pollutant_cas_id = '60851-34-5' where pollutant_srs_id = '317966';
Update pollutant set pollutant_cas_id = '62-38-4' where pollutant_srs_id = '3582';
Update pollutant set pollutant_cas_id = '7758-97-6' where pollutant_srs_id = '153528';
Update pollutant set pollutant_cas_id = '12060-00-3' where pollutant_srs_id = '178400';
Update pollutant set pollutant_cas_id = '1317-36-8' where pollutant_srs_id = '83345';
Update pollutant set pollutant_cas_id = '224-42-0' where pollutant_srs_id = '40741';
Update pollutant set pollutant_cas_id = '100-02-7' where pollutant_srs_id = '19117';
Update pollutant set pollutant_cas_id = '7722-64-7' where pollutant_srs_id = '152975';
Update pollutant set pollutant_cas_id = '1313-99-1' where pollutant_srs_id = '82891';
Update pollutant set pollutant_cas_id = '7789-42-6' where pollutant_srs_id = '155846';
Update pollutant set pollutant_cas_id = '12018-19-8' where pollutant_srs_id = '175257';
Update pollutant set pollutant_cas_id = '67-56-1' where pollutant_srs_id = '4283';
Update pollutant set pollutant_cas_id = '13462-88-9' where pollutant_srs_id = '186833';
Update pollutant set pollutant_cas_id = '79-10-7' where pollutant_srs_id = '7617';
Update pollutant set pollutant_cas_id = '7783-79-1' where pollutant_srs_id = '689554';
Update pollutant set pollutant_cas_id = '7783-00-8' where pollutant_srs_id = '154484';
Update pollutant set pollutant_cas_id = '629-14-1' where pollutant_srs_id = '64634';
Update pollutant set pollutant_cas_id = '208-96-8' where pollutant_srs_id = '40717';
Update pollutant set pollutant_cas_id = '53-96-3' where pollutant_srs_id = '1719';
Update pollutant set pollutant_cas_id = '218-01-9' where pollutant_srs_id = '40733';
Update pollutant set pollutant_cas_id = '584-84-9' where pollutant_srs_id = '55939';
Update pollutant set pollutant_cas_id = '1746-01-6' where pollutant_srs_id = '91918';
Update pollutant set pollutant_cas_id = '2051-24-3' where pollutant_srs_id = '96222';
Update pollutant set pollutant_cas_id = '1314-24-5' where pollutant_srs_id = '524672086';
Update pollutant set pollutant_cas_id = '7795-91-7' where pollutant_srs_id = '524671989';
Update pollutant set pollutant_cas_id = '78-82-0' where pollutant_srs_id = '7344';
Update pollutant set pollutant_cas_id = '207-08-9' where pollutant_srs_id = '40709';
Update pollutant set pollutant_cas_id = '2807-30-9' where pollutant_srs_id = '107573';
Update pollutant set pollutant_cas_id = '10377-66-9' where pollutant_srs_id = '170811';
Update pollutant set pollutant_cas_id = '85-01-8' where pollutant_srs_id = '10199';
Update pollutant set pollutant_cas_id = '7440-36-0' where pollutant_srs_id = '149963';
Update pollutant set pollutant_cas_id = '92203-02-6' where pollutant_srs_id = '602896';
Update pollutant set pollutant_cas_id = '14336-70-0' where pollutant_srs_id = '776583';
Update pollutant set pollutant_cas_id = '14220-17-8' where pollutant_srs_id = '194076';
Update pollutant set pollutant_cas_id = '1336-36-3' where pollutant_srs_id = '85878';
Update pollutant set pollutant_cas_id = '822-06-0' where pollutant_srs_id = '71209';
Update pollutant set pollutant_cas_id = '1303-28-2' where pollutant_srs_id = '82057';
Update pollutant set pollutant_cas_id = '111-77-3' where pollutant_srs_id = '27854';
Update pollutant set pollutant_cas_id = '38998-75-3' where pollutant_srs_id = '278200';
Update pollutant set pollutant_cas_id = '7787-47-5' where pollutant_srs_id = '155440';
Update pollutant set pollutant_cas_id = '121-44-8' where pollutant_srs_id = '31708';
Update pollutant set pollutant_cas_id = '123-91-1' where pollutant_srs_id = '33241';
Update pollutant set pollutant_cas_id = '68-12-2' where pollutant_srs_id = '4416';
Update pollutant set pollutant_cas_id = '14302-87-5' where pollutant_srs_id = '17016817';
Update pollutant set pollutant_cas_id = '193-39-5' where pollutant_srs_id = '40626';
Update pollutant set pollutant_cas_id = '75-01-4' where pollutant_srs_id = '5231';
Update pollutant set pollutant_cas_id = '7664-38-2' where pollutant_srs_id = '152363';
Update pollutant set pollutant_cas_id = '2381-21-7' where pollutant_srs_id = '961656';
Update pollutant set pollutant_cas_id = '75-00-3' where pollutant_srs_id = '5223';
Update pollutant set pollutant_cas_id = '622-08-2' where pollutant_srs_id = '62398';
Update pollutant set pollutant_cas_id = '34465-46-8' where pollutant_srs_id = '266965';
Update pollutant set pollutant_cas_id = '203-12-3' where pollutant_srs_id = '524671880';
Update pollutant set pollutant_cas_id = '7789-00-6' where pollutant_srs_id = '155630';
Update pollutant set pollutant_cas_id = '542-88-1' where pollutant_srs_id = '52498';
Update pollutant set pollutant_cas_id = '56-49-5' where pollutant_srs_id = '2188';
Update pollutant set pollutant_cas_id = '70648-26-9' where pollutant_srs_id = '525212';
Update pollutant set pollutant_cas_id = '50922-29-7' where pollutant_srs_id = '290031';
Update pollutant set pollutant_cas_id = '7789-12-0' where pollutant_srs_id = '650457';
Update pollutant set pollutant_cas_id = '112-35-6' where pollutant_srs_id = '28357';
Update pollutant set pollutant_cas_id = '78-30-8' where pollutant_srs_id = '7013';
Update pollutant set pollutant_cas_id = '18540-29-9' where pollutant_srs_id = '212357';
Update pollutant set pollutant_cas_id = '78-93-3' where pollutant_srs_id = '7443';
Update pollutant set pollutant_cas_id = '98-07-7' where pollutant_srs_id = '17756';
Update pollutant set pollutant_cas_id = '10143-56-3' where pollutant_srs_id = '524671963';
Update pollutant set pollutant_cas_id = '56-23-5' where pollutant_srs_id = '2071';
Update pollutant set pollutant_cas_id = '1308-06-1' where pollutant_srs_id = '82396';
Update pollutant set pollutant_cas_id = '13598-51-1' where pollutant_srs_id = '17119066';
Update pollutant set pollutant_cas_id = '12710-36-0' where pollutant_srs_id = '524672060';
Update pollutant set pollutant_cas_id = '1582-09-8' where pollutant_srs_id = '89060';
Update pollutant set pollutant_cas_id = '1120-71-4' where pollutant_srs_id = '79012';
Update pollutant set pollutant_cas_id = '79-11-8' where pollutant_srs_id = '7625';
Update pollutant set pollutant_cas_id = '540-88-5' where pollutant_srs_id = '51979';
Update pollutant set pollutant_cas_id = '79-06-1' where pollutant_srs_id = '7575';
Update pollutant set pollutant_cas_id = '131-11-3' where pollutant_srs_id = '35295';
Update pollutant set pollutant_cas_id = '57117-44-9' where pollutant_srs_id = '308825';
Update pollutant set pollutant_cas_id = '143-33-9' where pollutant_srs_id = '39305';
Update pollutant set pollutant_cas_id = '51207-31-9' where pollutant_srs_id = '291005';
Update pollutant set pollutant_cas_id = '108-38-3' where pollutant_srs_id = '25056';
Update pollutant set pollutant_cas_id = '123-38-6' where pollutant_srs_id = '32953';
Update pollutant set pollutant_cas_id = '7664-39-3' where pollutant_srs_id = '152371';
Update pollutant set pollutant_cas_id = '6018-89-9' where pollutant_srs_id = '524672078';
Update pollutant set pollutant_cas_id = '3775-85-7' where pollutant_srs_id = '524671971';
Update pollutant set pollutant_cas_id = '1332-21-4' where pollutant_srs_id = '85282';
Update pollutant set pollutant_cas_id = '8-4-7446' where pollutant_srs_id = '150359';
Update pollutant set pollutant_cas_id = '9-5-7789' where pollutant_srs_id = '155663';
Update pollutant set pollutant_cas_id = '101-14-4' where pollutant_srs_id = '19943';
Update pollutant set pollutant_cas_id = '112-50-5' where pollutant_srs_id = '28480';
Update pollutant set pollutant_cas_id = '195-19-7' where pollutant_srs_id = '524672417';
Update pollutant set pollutant_cas_id = '7-5-7783' where pollutant_srs_id = '154526';
Update pollutant set pollutant_cas_id = '10325-94-7' where pollutant_srs_id = '170217';
Update pollutant set pollutant_cas_id = '156-62-7' where pollutant_srs_id = '40477';
Update pollutant set pollutant_cas_id = '108-88-3' where pollutant_srs_id = '25452';
Update pollutant set pollutant_cas_id = '14307-33-6' where pollutant_srs_id = '194795';
Update pollutant set pollutant_cas_id = '112-34-5' where pollutant_srs_id = '28340';
Update pollutant set pollutant_cas_id = '7446-27-7' where pollutant_srs_id = '150458';
Update pollutant set pollutant_cas_id = '7778-50-9' where pollutant_srs_id = '153916';
Update pollutant set pollutant_cas_id = '7723-14-0' where pollutant_srs_id = '153049';
Update pollutant set pollutant_cas_id = '86-73-7' where pollutant_srs_id = '10892';
Update pollutant set pollutant_cas_id = '79-46-9' where pollutant_srs_id = '7872';
Update pollutant set pollutant_cas_id = '67-66-3' where pollutant_srs_id = '4317';
Update pollutant set pollutant_cas_id = '100-44-7' where pollutant_srs_id = '19430';
Update pollutant set pollutant_cas_id = '98-86-2' where pollutant_srs_id = '18333';
Update pollutant set pollutant_cas_id = '51-28-5' where pollutant_srs_id = '1313';
Update pollutant set pollutant_cas_id = '90-04-0' where pollutant_srs_id = '12815';
Update pollutant set pollutant_cas_id = '14302-87-5' where pollutant_srs_id = '17016817';
Update pollutant set pollutant_cas_id = '7779-90-0' where pollutant_srs_id = '154203';
Update pollutant set pollutant_cas_id = '120-55-8' where pollutant_srs_id = '31278';
Update pollutant set pollutant_cas_id = '75-35-4' where pollutant_srs_id = '5538';
Update pollutant set pollutant_cas_id = '684-93-5' where pollutant_srs_id = '67462';
Update pollutant set pollutant_cas_id = '75-25-2' where pollutant_srs_id = '5447';
Update pollutant set pollutant_cas_id = '10124-43-3' where pollutant_srs_id = '168328';
Update pollutant set pollutant_cas_id = '98-95-3' where pollutant_srs_id = '18408';
Update pollutant set pollutant_cas_id = '108-05-4' where pollutant_srs_id = '24828';
Update pollutant set pollutant_cas_id = '7439-92-1' where pollutant_srs_id = '149583';
Update pollutant set pollutant_cas_id = '16925-25-0' where pollutant_srs_id = '206292';
Update pollutant set pollutant_cas_id = '64-67-5' where pollutant_srs_id = '3889';
Update pollutant set pollutant_cas_id = '10025-87-3' where pollutant_srs_id = '166488';
Update pollutant set pollutant_cas_id = '133-06-2' where pollutant_srs_id = '35790';
Update pollutant set pollutant_cas_id = '2-8-7496' where pollutant_srs_id = '524671872';
Update pollutant set pollutant_cas_id = '1319-77-3' where pollutant_srs_id = '83550';
Update pollutant set pollutant_cas_id = '1345-04-6' where pollutant_srs_id = '86272';
Update pollutant set pollutant_cas_id = '140-05-6' where pollutant_srs_id = '524672052';
Update pollutant set pollutant_cas_id = '77-78-1' where pollutant_srs_id = '6684';
Update pollutant set pollutant_cas_id = '2223-93-0' where pollutant_srs_id = '98954';
Update pollutant set pollutant_cas_id = '929-37-3' where pollutant_srs_id = '524671955';
Update pollutant set pollutant_cas_id = '71-55-6' where pollutant_srs_id = '4796';
Update pollutant set pollutant_cas_id = '203-33-8' where pollutant_srs_id = '40659';
Update pollutant set pollutant_cas_id = '107-98-2' where pollutant_srs_id = '24778';
Update pollutant set pollutant_cas_id = '1327-53-3' where pollutant_srs_id = '84749';
Update pollutant set pollutant_cas_id = '75-34-3' where pollutant_srs_id = '5520';
Update pollutant set pollutant_cas_id = '1336-93-2' where pollutant_srs_id = '85886';
Update pollutant set pollutant_cas_id = '88-06-2' where pollutant_srs_id = '11536';
Update pollutant set pollutant_cas_id = '78-59-1' where pollutant_srs_id = '7187';
Update pollutant set pollutant_cas_id = '112-27-6' where pollutant_srs_id = '28282';
Update pollutant set pollutant_cas_id = '7440-47-3' where pollutant_srs_id = '150060';
Update pollutant set pollutant_cas_id = '106-42-3' where pollutant_srs_id = '23580';
Update pollutant set pollutant_cas_id = '95-47-6' where pollutant_srs_id = '16139';
Update pollutant set pollutant_cas_id = '1333-82-0' where pollutant_srs_id = '85571';
Update pollutant set pollutant_cas_id = '534-52-1' where pollutant_srs_id = '51102';
Update pollutant set pollutant_cas_id = '832-69-9' where pollutant_srs_id = '71852';
Update pollutant set pollutant_cas_id = '79-44-7' where pollutant_srs_id = '7864';
Update pollutant set pollutant_cas_id = '16065-83-1' where pollutant_srs_id = '203000';
Update pollutant set pollutant_cas_id = '1304-56-9' where pollutant_srs_id = '82214';
Update pollutant set pollutant_cas_id = '10143-53-0' where pollutant_srs_id = '168823';
Update pollutant set pollutant_cas_id = '71-43-2' where pollutant_srs_id = '4754';
Update pollutant set pollutant_cas_id = '302-01-2' where pollutant_srs_id = '41541';
Update pollutant set pollutant_cas_id = '95-95-4' where pollutant_srs_id = '16519';
Update pollutant set pollutant_cas_id = '106-93-4' where pollutant_srs_id = '23986';
Update pollutant set pollutant_cas_id = '91-57-6' where pollutant_srs_id = '13524';
Update pollutant set pollutant_cas_id = '107-13-1' where pollutant_srs_id = '24182';
Update pollutant set pollutant_cas_id = '23436-19-3' where pollutant_srs_id = '223925';
Update pollutant set pollutant_cas_id = '95-53-4' where pollutant_srs_id = '16196';
Update pollutant set pollutant_cas_id = '189-55-9' where pollutant_srs_id = '40543';
Update pollutant set pollutant_cas_id = '14307-35-8' where pollutant_srs_id = '194803';
Update pollutant set pollutant_cas_id = '75-56-9' where pollutant_srs_id = '5660';
Update pollutant set pollutant_cas_id = '8030-70-4' where pollutant_srs_id = '159749';
Update pollutant set pollutant_cas_id = '55673-89-7' where pollutant_srs_id = '304782';
Update pollutant set pollutant_cas_id = '91-22-5' where pollutant_srs_id = '13342';
Update pollutant set pollutant_cas_id = '1309-60-0' where pollutant_srs_id = '82537';
Update pollutant set pollutant_cas_id = '308069-13-8' where pollutant_srs_id = '17150145';
Update pollutant set pollutant_cas_id = '107-02-8' where pollutant_srs_id = '24075';
Update pollutant set pollutant_cas_id = '1589-49-7' where pollutant_srs_id = '650317';
Update pollutant set pollutant_cas_id = '13597-99-4' where pollutant_srs_id = '189100';
Update pollutant set pollutant_cas_id = '120-82-1' where pollutant_srs_id = '31435';
Update pollutant set pollutant_cas_id = '7664-41-7' where pollutant_srs_id = '152389';
Update pollutant set pollutant_cas_id = '98-82-8' where pollutant_srs_id = '18309';
Update pollutant set pollutant_cas_id = '7782-50-5' where pollutant_srs_id = '154328';
Update pollutant set pollutant_cas_id = '106-51-4' where pollutant_srs_id = '23671';
Update pollutant set pollutant_cas_id = '55684-94-1' where pollutant_srs_id = '304824';
Update pollutant set pollutant_cas_id = '122-99-6' where pollutant_srs_id = '32664';
Update pollutant set pollutant_cas_id = '112-25-4' where pollutant_srs_id = '28266';
Update pollutant set pollutant_cas_id = '75-09-2' where pollutant_srs_id = '5306';
Update pollutant set pollutant_cas_id = '92-52-4' where pollutant_srs_id = '14183';
Update pollutant set pollutant_cas_id = '14018-95-2' where pollutant_srs_id = '193094';
Update pollutant set pollutant_cas_id = '1317-42-6' where pollutant_srs_id = '83402';
Update pollutant set pollutant_cas_id = '57835-92-4' where pollutant_srs_id = '17001371';
Update pollutant set pollutant_cas_id = '11-3-7775' where pollutant_srs_id = '153775';
Update pollutant set pollutant_cas_id = '334-88-3' where pollutant_srs_id = '42762';
Update pollutant set pollutant_cas_id = '127-18-4' where pollutant_srs_id = '34157';
Update pollutant set pollutant_cas_id = '92-67-1' where pollutant_srs_id = '14308';
Update pollutant set pollutant_cas_id = '143-22-6' where pollutant_srs_id = '39248';
Update pollutant set pollutant_cas_id = '192-97-2' where pollutant_srs_id = '40618';
Update pollutant set pollutant_cas_id = '1317-34-6' where pollutant_srs_id = '83329';
Update pollutant set pollutant_cas_id = '10215-33-5' where pollutant_srs_id = '524671864';
Update pollutant set pollutant_cas_id = '7783-16-6' where pollutant_srs_id = '524672045';
Update pollutant set pollutant_cas_id = '10101-97-0' where pollutant_srs_id = '167858';
Update pollutant set pollutant_cas_id = '14859-67-7' where pollutant_srs_id = '197491';
Update pollutant set pollutant_cas_id = '10025-73-7' where pollutant_srs_id = '166421';
Update pollutant set pollutant_cas_id = '133-90-4' where pollutant_srs_id = '35956';
Update pollutant set pollutant_cas_id = '1309-64-4' where pollutant_srs_id = '82545';
Update pollutant set pollutant_cas_id = '7446-34-6' where pollutant_srs_id = '150474';
Update pollutant set pollutant_cas_id = '114-26-1' where pollutant_srs_id = '28928';
Update pollutant set pollutant_cas_id = '7778-39-4' where pollutant_srs_id = '153866';
Update pollutant set pollutant_cas_id = '206-44-0' where pollutant_srs_id = '40691';
Update pollutant set pollutant_cas_id = '120-80-9' where pollutant_srs_id = '31427';
Update pollutant set pollutant_cas_id = '96-12-8' where pollutant_srs_id = '16618';
Update pollutant set pollutant_cas_id = '111-96-6' where pollutant_srs_id = '28027';
Update pollutant set pollutant_cas_id = '57117-31-4' where pollutant_srs_id = '308809';
Update pollutant set pollutant_cas_id = '1327-33-9' where pollutant_srs_id = '84681';
Update pollutant set pollutant_cas_id = '14274-82-9' where pollutant_srs_id = '194522';
Update pollutant set pollutant_cas_id = '542-75-6' where pollutant_srs_id = '52449';
Update pollutant set pollutant_cas_id = '124-17-4' where pollutant_srs_id = '33449';
Update pollutant set pollutant_cas_id = '12018-01-8' where pollutant_srs_id = '175208';
Update pollutant set pollutant_cas_id = '1634-04-4' where pollutant_srs_id = '89870';
Update pollutant set pollutant_cas_id = '108-95-2' where pollutant_srs_id = '25510';
Update pollutant set pollutant_cas_id = '60-34-4' where pollutant_srs_id = '3376';
Update pollutant set pollutant_cas_id = '10049-05-5' where pollutant_srs_id = '167304';
Update pollutant set pollutant_cas_id = '106-46-7' where pollutant_srs_id = '23622';
Update pollutant set pollutant_cas_id = '22967-92-6' where pollutant_srs_id = '222877';
Update pollutant set pollutant_cas_id = '1330-20-7' where pollutant_srs_id = '84970';
Update pollutant set pollutant_cas_id = '56-55-3' where pollutant_srs_id = '2212';
Update pollutant set pollutant_cas_id = '75-44-5' where pollutant_srs_id = '5587';
Update pollutant set pollutant_cas_id = '84-74-2' where pollutant_srs_id = '10025';
Update pollutant set pollutant_cas_id = '78-87-5' where pollutant_srs_id = '7393';
Update pollutant set pollutant_cas_id = '108-10-1' where pollutant_srs_id = '24851';
Update pollutant set pollutant_cas_id = '463-58-1' where pollutant_srs_id = '46508';
Update pollutant set pollutant_cas_id = '14269-63-7' where pollutant_srs_id = '194514';
Update pollutant set pollutant_cas_id = '151-56-4' where pollutant_srs_id = '40279';
Update pollutant set pollutant_cas_id = '39227-28-6' where pollutant_srs_id = '711986';
Update pollutant set pollutant_cas_id = '41903-57-5' where pollutant_srs_id = '284596';
Update pollutant set pollutant_cas_id = '10588-01-9' where pollutant_srs_id = '172593';
Update pollutant set pollutant_cas_id = '7440-48-4' where pollutant_srs_id = '150078';
Update pollutant set pollutant_cas_id = '111-15-9' where pollutant_srs_id = '27284';
Update pollutant set pollutant_cas_id = '557-21-1' where pollutant_srs_id = '54213';
Update pollutant set pollutant_cas_id = '57-12-5' where pollutant_srs_id = '2444';
Update pollutant set pollutant_cas_id = '10143-54-1' where pollutant_srs_id = '524671948';
Update pollutant set pollutant_cas_id = '593-60-2' where pollutant_srs_id = '57448';
Update pollutant set pollutant_cas_id = '13770-89-3' where pollutant_srs_id = '190868';
Update pollutant set pollutant_cas_id = '14255-04-0' where pollutant_srs_id = '194316';
Update pollutant set pollutant_cas_id = '3333-67-3' where pollutant_srs_id = '114728';
Update pollutant set pollutant_cas_id = '1345-16-0' where pollutant_srs_id = '86322';
Update pollutant set pollutant_cas_id = '106-50-3' where pollutant_srs_id = '23663';
Update pollutant set pollutant_cas_id = '74-83-9' where pollutant_srs_id = '5074';
Update pollutant set pollutant_cas_id = '75-07-0' where pollutant_srs_id = '5280';
Update pollutant set pollutant_cas_id = '111-10-4' where pollutant_srs_id = '27235';
Update pollutant set pollutant_cas_id = '8001-35-2' where pollutant_srs_id = '156919';
Update pollutant set pollutant_cas_id = '12640-89-0' where pollutant_srs_id = '182592';
Update pollutant set pollutant_cas_id = '27310-21-0' where pollutant_srs_id = '524671856';
Update pollutant set pollutant_cas_id = '764-99-8' where pollutant_srs_id = '69625';
Update pollutant set pollutant_cas_id = '107-21-1' where pollutant_srs_id = '24257';
Update pollutant set pollutant_cas_id = '4206-61-5' where pollutant_srs_id = '688515';
Update pollutant set pollutant_cas_id = '50-32-8' where pollutant_srs_id = '1115';
Update pollutant set pollutant_cas_id = '301-04-2' where pollutant_srs_id = '41483';
Update pollutant set pollutant_cas_id = '1314-56-3' where pollutant_srs_id = '83089';
Update pollutant set pollutant_cas_id = '57-57-8' where pollutant_srs_id = '2600';
Update pollutant set pollutant_cas_id = '87-86-5' where pollutant_srs_id = '11437';
Update pollutant set pollutant_cas_id = '13765-19-0' where pollutant_srs_id = '190694';
Update pollutant set pollutant_cas_id = '4439-24-1' where pollutant_srs_id = '124859';
Update pollutant set pollutant_cas_id = '13982-63-3' where pollutant_srs_id = '192856';
Update pollutant set pollutant_cas_id = '57-97-6' where pollutant_srs_id = '2733';
Update pollutant set pollutant_cas_id = '122-66-7' where pollutant_srs_id = '32425';
Update pollutant set pollutant_cas_id = '85-44-9' where pollutant_srs_id = '10355';
Update pollutant set pollutant_cas_id = '9-5-7446' where pollutant_srs_id = '150367';
Update pollutant set pollutant_cas_id = '1306-23-6' where pollutant_srs_id = '82321';
Update pollutant set pollutant_cas_id = '10101-53-8' where pollutant_srs_id = '167759';
Update pollutant set pollutant_cas_id = '16672-39-2' where pollutant_srs_id = '205153';
Update pollutant set pollutant_cas_id = '62-53-3' where pollutant_srs_id = '3632';
Update pollutant set pollutant_cas_id = '10025-91-9' where pollutant_srs_id = '166496';
Update pollutant set pollutant_cas_id = '7647-01-0' where pollutant_srs_id = '152231';
Update pollutant set pollutant_cas_id = '1314-80-3' where pollutant_srs_id = '83147';
Update pollutant set pollutant_cas_id = '110-54-3' where pollutant_srs_id = '26740';
Update pollutant set pollutant_cas_id = '1314-06-3' where pollutant_srs_id = '82909';
Update pollutant set pollutant_cas_id = '7440-02-0' where pollutant_srs_id = '149674';
Update pollutant set pollutant_cas_id = '23495-12-7' where pollutant_srs_id = '224030';
Update pollutant set pollutant_cas_id = '13981-52-7' where pollutant_srs_id = '192815';
Update pollutant set pollutant_cas_id = '87-68-3' where pollutant_srs_id = '11346';
Update pollutant set pollutant_cas_id = '35822-46-9' where pollutant_srs_id = '270140';
Update pollutant set pollutant_cas_id = '136677-10-6' where pollutant_srs_id = '629352';
Update pollutant set pollutant_cas_id = '42397-65-9' where pollutant_srs_id = '693218';
Update pollutant set pollutant_cas_id = '10294-56-1' where pollutant_srs_id = '170027';
Update pollutant set pollutant_cas_id = '1317-35-7' where pollutant_srs_id = '83337';
Update pollutant set pollutant_cas_id = '13138-45-9' where pollutant_srs_id = '184259';
Update pollutant set pollutant_cas_id = '37871-00-4' where pollutant_srs_id = '275438';
Update pollutant set pollutant_cas_id = '205-99-2' where pollutant_srs_id = '40683';
Update pollutant set pollutant_cas_id = '57653-85-7' where pollutant_srs_id = '694281';
Update pollutant set pollutant_cas_id = '77-47-4' where pollutant_srs_id = '6494';
Update pollutant set pollutant_cas_id = '13463-39-3' where pollutant_srs_id = '186866';
Update pollutant set pollutant_cas_id = '67-42-5' where pollutant_srs_id = '4218';
Update pollutant set pollutant_cas_id = '106-99-0' where pollutant_srs_id = '24042';
Update pollutant set pollutant_cas_id = '14977-61-8' where pollutant_srs_id = '197954';
Update pollutant set pollutant_cas_id = '101-68-8' where pollutant_srs_id = '20263';
Update pollutant set pollutant_cas_id = '13966-00-2' where pollutant_srs_id = '192690';
Update pollutant set pollutant_cas_id = '112-49-2' where pollutant_srs_id = '28472';
Update pollutant set pollutant_cas_id = '91-20-3' where pollutant_srs_id = '13326';
Update pollutant set pollutant_cas_id = '544-92-3' where pollutant_srs_id = '52886';
Update pollutant set pollutant_cas_id = '13530-68-2' where pollutant_srs_id = '188128';
Update pollutant set pollutant_cas_id = '117-81-7' where pollutant_srs_id = '29934';
Update pollutant set pollutant_cas_id = '7790-80-9' where pollutant_srs_id = '156323';
Update pollutant set pollutant_cas_id = '10124-36-4' where pollutant_srs_id = '168294';
Update pollutant set pollutant_cas_id = '30402-15-4' where pollutant_srs_id = '256289';
Update pollutant set pollutant_cas_id = '121-14-2' where pollutant_srs_id = '31575';
Update pollutant set pollutant_cas_id = '59-89-2' where pollutant_srs_id = '3194';
Update pollutant set pollutant_cas_id = '693-21-0' where pollutant_srs_id = '67819';
Update pollutant set pollutant_cas_id = '10031-13-7' where pollutant_srs_id = '524672037';
Update pollutant set pollutant_cas_id = '1002-67-1' where pollutant_srs_id = '524671930';
Update pollutant set pollutant_cas_id = '67562-39-4' where pollutant_srs_id = '358382';
Update pollutant set pollutant_cas_id = '779-02-2' where pollutant_srs_id = '70367';

DROP VIEW IF EXISTS vw_emissions_by_facility_and_cas;
DROP VIEW IF EXISTS vw_report_summary; 

ALTER TABLE emission
    ALTER COLUMN pollutant_code TYPE character varying (12); 

CREATE VIEW vw_emissions_by_facility_and_cas
    AS
     SELECT concat(e.id, rpa.id) AS id,
    fs.frs_facility_id,
    fs.name AS facility_name,
    er.year,
    p.pollutant_name,
    p.pollutant_cas_id,
    e.total_emissions * rpa.percent * 0.01 AS apportioned_emissions,
    rpt.release_point_identifier,
        CASE
            WHEN rptc.description::text = 'Fugitive'::text THEN 'non-point'::text
            ELSE 'point'::text
        END AS release_point_type,
    e.emissions_uom_code
   FROM emission e,
    reporting_period rp,
    emissions_process ep,
    facility_site fs,
    emissions_unit eu,
    emissions_report er,
    release_point rpt,
    release_point_appt rpa,
    release_point_type_code rptc,
    pollutant p
  WHERE e.reporting_period_id = rp.id 
  AND rp.emissions_process_id = ep.id 
  AND ep.emissions_unit_id = eu.id 
  AND eu.facility_site_id = fs.id 
  AND fs.report_id = er.id 
  AND ep.id = rpa.emissions_process_id 
  AND rpa.release_point_id = rpt.id 
  AND rpt.type_code::text = rptc.code::text
  AND e.pollutant_code = p.pollutant_code;

CREATE VIEW vw_report_summary
    AS
SELECT row_number() OVER (ORDER BY p.pollutant_cas_id) AS id,
    p.pollutant_cas_id,
    p.pollutant_name,
    p.pollutant_type,
    COALESCE(fugitive_amount.calculated_emissions_tons, 0::numeric) AS fugitive_total,
    COALESCE(stack_amount.calculated_emissions_tons, 0::numeric) AS stack_total,
    'tons'::text AS uom,
    COALESCE(sum(e.calculated_emissions_tons), 0::numeric) AS emissions_tons_total,
    COALESCE(previous_year_total.calculated_emissions_tons, 0::numeric) AS previous_year_total,
    er.year AS report_year,
    fs.id AS facility_site_id
   FROM emission e
     JOIN reporting_period repper ON repper.id = e.reporting_period_id
     JOIN emissions_process ep ON ep.id = repper.emissions_process_id
     JOIN release_point_appt rpa ON ep.id = rpa.emissions_process_id
     JOIN release_point rp ON rp.id = rpa.release_point_id
     JOIN release_point_type_code rptc ON rptc.code::text = rp.type_code::text
     JOIN facility_site fs ON fs.id = rp.facility_site_id
     JOIN emissions_report er ON er.id = fs.report_id
     JOIN pollutant p ON p.pollutant_code = e.pollutant_code
     LEFT JOIN ( SELECT sum(e_1.calculated_emissions_tons) AS calculated_emissions_tons,
            er_1.year,
            fs_1.id AS facility_id,
            p_1.pollutant_cas_id
           FROM emission e_1
             JOIN reporting_period repper_1 ON repper_1.id = e_1.reporting_period_id
             JOIN emissions_process ep_1 ON ep_1.id = repper_1.emissions_process_id
             JOIN release_point_appt rpa_1 ON ep_1.id = rpa_1.emissions_process_id
             JOIN release_point rp_1 ON rp_1.id = rpa_1.release_point_id
             JOIN release_point_type_code rptc_1 ON rptc_1.code::text = rp_1.type_code::text
             JOIN facility_site fs_1 ON fs_1.id = rp_1.facility_site_id
             JOIN emissions_report er_1 ON er_1.id = fs_1.report_id
             JOIN pollutant p_1 on p_1.pollutant_code = e_1.pollutant_code
          WHERE rptc_1.description::text = 'Fugitive'::text
          GROUP BY er_1.year, p_1.pollutant_cas_id, fs_1.id) fugitive_amount ON fugitive_amount.year = er.year AND fugitive_amount.pollutant_cas_id::text = p.pollutant_cas_id::text AND fugitive_amount.facility_id = fs.id
     LEFT JOIN ( SELECT sum(e_1.calculated_emissions_tons) AS calculated_emissions_tons,
            er_1.year,
            fs_1.id AS facility_id,
            p_1.pollutant_cas_id
           FROM emission e_1
             JOIN reporting_period repper_1 ON repper_1.id = e_1.reporting_period_id
             JOIN emissions_process ep_1 ON ep_1.id = repper_1.emissions_process_id
             JOIN release_point_appt rpa_1 ON ep_1.id = rpa_1.emissions_process_id
             JOIN release_point rp_1 ON rp_1.id = rpa_1.release_point_id
             JOIN release_point_type_code rptc_1 ON rptc_1.code::text = rp_1.type_code::text
             JOIN facility_site fs_1 ON fs_1.id = rp_1.facility_site_id
             JOIN emissions_report er_1 ON er_1.id = fs_1.report_id
             JOIN pollutant p_1 on p_1.pollutant_code = e_1.pollutant_code
          WHERE rptc_1.description::text <> 'Fugitive'::text
          GROUP BY er_1.year, p_1.pollutant_cas_id, fs_1.id) stack_amount ON stack_amount.year = er.year AND stack_amount.pollutant_cas_id::text = p.pollutant_cas_id::text AND stack_amount.facility_id = fs.id
     LEFT JOIN ( SELECT sum(e_1.calculated_emissions_tons) AS calculated_emissions_tons,
            er_1.year,
            p_1.pollutant_cas_id,
            fs_1.id AS facility_id
           FROM emission e_1
             JOIN reporting_period repper_1 ON repper_1.id = e_1.reporting_period_id
             JOIN emissions_process ep_1 ON ep_1.id = repper_1.emissions_process_id
             JOIN release_point_appt rpa_1 ON ep_1.id = rpa_1.emissions_process_id
             JOIN release_point rp_1 ON rp_1.id = rpa_1.release_point_id
             JOIN release_point_type_code rptc_1 ON rptc_1.code::text = rp_1.type_code::text
             JOIN facility_site fs_1 ON fs_1.id = rp_1.facility_site_id
             JOIN emissions_report er_1 ON er_1.id = fs_1.report_id
             JOIN pollutant p_1 on p_1.pollutant_code = e_1.pollutant_code
          GROUP BY er_1.year, p_1.pollutant_cas_id, fs_1.id) previous_year_total ON previous_year_total.year = (er.year - 1) AND previous_year_total.facility_id = fs.id
  GROUP BY er.year, fs.id, p.pollutant_cas_id, p.pollutant_name, 'tons'::text, p.pollutant_type, (COALESCE(fugitive_amount.calculated_emissions_tons, 0::numeric)), (COALESCE(stack_amount.calculated_emissions_tons, 0::numeric)), (COALESCE(previous_year_total.calculated_emissions_tons, 0::numeric));

ALTER TABLE control_pollutant DROP COLUMN pollutant_name;

ALTER TABLE control_pollutant DROP COLUMN pollutant_cas_id;

ALTER TABLE control_pollutant
    ALTER COLUMN pollutant_code TYPE character varying (12);

ALTER TABLE control_pollutant
    ADD CONSTRAINT control_pollutant_pollutant_fkey FOREIGN KEY (pollutant_code)
    REFERENCES pollutant (pollutant_code) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION;
    
ALTER TABLE emission DROP COLUMN pollutant_name;

ALTER TABLE emission DROP COLUMN pollutant_cas_id;

ALTER TABLE emission DROP COLUMN pollutant_type;

ALTER TABLE emission
    ADD CONSTRAINT emissions_pollutant_fkey FOREIGN KEY (pollutant_code)
    REFERENCES pollutant (pollutant_code) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION;
