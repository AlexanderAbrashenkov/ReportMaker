WITH numbered AS (
    SELECT
      rt.datetime,
      rt.client_id,
      row_number()
      OVER (
        PARTITION BY rt.client_id
        ORDER BY rt.datetime ) AS rn,
      rt.ctid                  AS id
    FROM record_transaction rt
      LEFT JOIN staff s ON s.id = rt.staff_id
    WHERE rt.attendance = 1
          AND (rt.staff_id IS NULL OR s.use_in_records LIKE '1')
)
UPDATE record_transaction
SET visit_number = n.rn
FROM numbered n
WHERE n.id = record_transaction.ctid;