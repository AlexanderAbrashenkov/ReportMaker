SELECT
  ci.name              AS "Город",
  gt.type              AS "Тип",
  g.title              AS "Наименование",
  st.title             AS "Объект",
  u.title              AS "Измерение",
  DATE(gt.create_date) AS "Дата",
  gt.cost_per_unit     AS "Цена за единицу",
  gt.cost              AS "Стоимость",
  gt.discount          AS "Скидка",
  s.name               AS "Мастер",
  sup.title            AS "Поставщик",
  c.name               AS "Клиент"
FROM goods_transaction gt
  JOIN city ci ON ci.id = gt.city_id
  JOIN good g ON g.id = gt.good_id
  LEFT JOIN storage st ON st.id = gt.storage_id
  LEFT JOIN unit u ON u.id = gt.unit_id
  LEFT JOIN staff s ON s.id = gt.staff_id
  LEFT JOIN supplier sup ON sup.id = gt.supplier_id
  LEFT JOIN client c ON c.id = gt.client_id
WHERE gt.type_id = 1