SELECT rt.city_id, c.name as city_name,
FROM record_transaction rt
JOIN record_transaction_service_list rtsl ON rt.id = rtsl.record_transaction_id
JOIN concrete_service cs on rtsl.service_list_uid = cs.uid
JOIN city c on c.id = rt.city_id