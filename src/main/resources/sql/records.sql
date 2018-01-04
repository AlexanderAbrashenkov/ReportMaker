SELECT
  r.city_id,
  c.name,
  --extract(YEAR FROM r.datetime) as year,
  --extract(MONTH FROM r.datetime) as mon,
  count(*)
FROM
  (SELECT
     rt.*,
     cl.name,
     cl.phone,
     s.name,
     s.specialization,
     s.use_in_records
   FROM record_transaction rt
     LEFT JOIN client cl ON cl.id = rt.client_id
     LEFT JOIN staff s ON s.id = rt.staff_id
   WHERE
     --rt.deleted = 0
     --AND rt.city_id = 33599
     /*AND rt.datetime > '2017-06-01 00:00:00'
       and rt.datetime < '2017-07-01 00:00:00'*/
     rt.attendance = 1
     AND (rt.staff_id IS NULL OR s.use_in_records LIKE '1')
     AND rt.client_has_next_visit = 1
   ORDER BY rt.datetime DESC
  ) r
  LEFT JOIN city c ON c.id = r.city_id
GROUP BY r.city_id, c.name--, year, mon
ORDER BY c.name