delete from tokuteikizai_master_arch where kizaicode in (select m_distinct.kizaicode from 
    (select distinct(kizaicode) from tokuteikizai_master_arch) as m_distinct
    left outer join
    (select distinct(kizaicode) from visit_conduct_kizai) as c_distinct
    on m_distinct.kizaicode = c_distinct.kizaicode
    where c_distinct.kizaicode is null);
