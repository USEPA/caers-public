
ALTER TABLE emission ALTER COLUMN total_manual_entry set DEFAULT false;

update emission e
    set total_manual_entry = total_direct_entry
    from calculation_method_code cmc where e.emissions_calc_method_code = cmc.code;