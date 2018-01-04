UPDATE record_transaction rt
SET visit_number = 1
WHERE rt.client_id IS NULL