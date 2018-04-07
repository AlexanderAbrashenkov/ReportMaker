-- Не привязанные наименования услуг
SELECT DISTINCT cs.title
FROM service_lib sl
  JOIN concrete_service cs on cs.service_id = sl.service_id
where sl.service_group_id is NULL
ORDER BY 1;

-- установить id группы по наименованию услуги
UPDATE service_lib
SET service_group_id = 19
WHERE service_id in (
  SELECT cs.service_id
  from concrete_service cs
  WHERE cs.title = 'Хэйр Тату'
);

-- все группы услуг
SELECT * FROM service_group