ALTER TABLE archiveConversations ADD withPartId VARCHAR(255)    NOT NULL;

UPDATE ofVersion SET version=3 WHERE name='archive';
