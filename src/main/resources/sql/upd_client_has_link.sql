UPDATE client
SET has_link = 1
WHERE comment LIKE '%http%vk.com%'
      OR email LIKE '%http%vk.com%'