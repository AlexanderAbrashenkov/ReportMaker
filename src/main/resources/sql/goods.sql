SELECT a.title, a.sales, a.amount,
a.sales / a.total :: FLOAT as part
from (
SELECT
CASE WHEN lower(g.title) LIKE '%сертификат%'
THEN 'Сертификат'
ELSE g.title END as title,
sum(rt.cost)          AS sales,
sum(rt.amount * -1)   AS amount,
  (SELECT sum(rt.cost)
   FROM goods_transaction rt
   WHERE rt.type_id = 1
         AND extract(YEAR FROM rt.create_date) = 2018
         AND extract(MONTH FROM rt.create_date) = 2) as total
FROM goods_transaction rt
JOIN good g ON rt.good_id = g.id
WHERE rt.type_id = 1
AND extract(YEAR FROM rt.create_date) = 2018
AND extract(MONTH FROM rt.create_date) = 2
GROUP BY 1) a
ORDER BY a.sales DESC
LIMIT 100;
