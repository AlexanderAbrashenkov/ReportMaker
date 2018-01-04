UPDATE record_transaction AS rt
SET days_between_visits = res.days
FROM (
       SELECT
         r.id,
         date_part('day', r2.datetime - r.datetime) AS days
       FROM record_transaction r
         JOIN record_transaction r2 ON r.client_id = r2.client_id AND r.visit_number = r2.visit_number - 1
       WHERE r.client_has_next_visit = 1
     ) res
WHERE rt.id = res.id