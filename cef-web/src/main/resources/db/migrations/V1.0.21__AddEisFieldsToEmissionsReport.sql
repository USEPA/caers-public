

alter table emissions_report
	add eis_last_trans_id varchar(32);

alter table emissions_report
	add eis_last_sub_status varchar(32);

alter table emissions_report
	add eis_is_passed boolean default false;

alter table emissions_report
	add eis_comments varchar(2000);

