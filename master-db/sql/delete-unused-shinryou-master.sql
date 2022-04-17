delete from shinryoukoui_master_arch where shinryoucode in (select m_distinct.shinryoucode from 
    (select distinct(shinryoucode) from shinryoukoui_master_arch) as m_distinct
    left outer join
    (select distinct(shinryoucode) from visit_shinryou) as s_distinct
    on m_distinct.shinryoucode = s_distinct.shinryoucode
    left outer join
    (select distinct(shinryoucode) from visit_conduct_shinryou) as c_distinct
    on m_distinct.shinryoucode = c_distinct.shinryoucode
    where s_distinct.shinryoucode is null and c_distinct.shinryoucode is null);
