UPDATE record_transaction
SET client_has_next_visit = 1
FROM (
       SELECT
         r.client_id,
         max(r.visit_number) AS max_visit
       FROM record_transaction r
       WHERE r.client_id IS NOT NULL
       GROUP BY r.client_id
     ) a
WHERE record_transaction.client_id = a.client_id
      AND record_transaction.visit_number < a.max_visit
      AND record_transaction.visit_number > 0