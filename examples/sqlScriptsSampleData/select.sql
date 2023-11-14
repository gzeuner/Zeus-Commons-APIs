SELECT
    d.entity_id,
    d.attribute_key,
    d.attribute_value,
    m.metadata_key,
    m.metadata_value
FROM
    eav_data d
LEFT JOIN
    eav_metadata m ON d.entity_id = m.entity_id
WHERE
    d.entity_id = '48a85e9126f9615c29db16f5cc12e3b6654fd7028b1f3578c088067ec9e2928f' and ATTRIBUTE_KEY like '%agent_code%' and METADATA_KEY = 'agent_code' and METADATA_TYPE = 'columnClassName';
