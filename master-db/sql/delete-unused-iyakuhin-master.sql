delete from iyakuhin_master_arch where iyakuhincode in (select m_distinct.iyakuhincode from 
    (select distinct(iyakuhincode) from iyakuhin_master_arch) as m_distinct
    left outer join
    (select distinct(d_iyakuhincode) as iyakuhincode from visit_drug) as d_distinct
    on m_distinct.iyakuhincode = d_distinct.iyakuhincode
    left outer join
    (select distinct(iyakuhincode) from visit_conduct_drug) as c_distinct
    on m_distinct.iyakuhincode = c_distinct.iyakuhincode
    where d_distinct.iyakuhincode is null and c_distinct.iyakuhincode is null);
