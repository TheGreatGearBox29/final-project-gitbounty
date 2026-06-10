ALTER TABLE issues
    ADD COLUMN number INT;

UPDATE issues i
    JOIN (
    SELECT id,
    ROW_NUMBER() OVER (PARTITION BY repository_id ORDER BY created_at, id) AS issue_number
    FROM issues
    ) numbered_issues ON i.id = numbered_issues.id
    SET i.number = numbered_issues.issue_number;

ALTER TABLE issues
    MODIFY COLUMN number INT NOT NULL;

ALTER TABLE issues
    ADD CONSTRAINT uq_issues_repository_number UNIQUE (repository_id, number);