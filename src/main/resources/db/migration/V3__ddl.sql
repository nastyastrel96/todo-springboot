ALTER TABLE items
ADD COLUMN parent_item_id INT REFERENCES items;

ALTER TABLE items
ADD COLUMN updating_time time;