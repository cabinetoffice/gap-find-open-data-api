ALTER TABLE grant_submission ADD COLUMN attachment_id UUID;


CREATE TABLE grant_attachment (
  grant_attachment_id UUID NOT NULL,
   submission_id UUID,
   question_id VARCHAR(255) NOT NULL,
   version INTEGER NOT NULL,
   created TIMESTAMP WITHOUT TIME ZONE NOT NULL,
   created_by BIGINT,
   last_updated TIMESTAMP WITHOUT TIME ZONE NOT NULL,
   status VARCHAR(255) NOT NULL,
   filename VARCHAR(255) NOT NULL,
   location TEXT NOT NULL,
   CONSTRAINT pk_grant_attachment PRIMARY KEY (grant_attachment_id)
);

ALTER TABLE grant_attachment ADD CONSTRAINT FK_GRANT_ATTACHMENT_ON_CREATED_BY FOREIGN KEY (created_by) REFERENCES grant_applicant (id);

ALTER TABLE grant_attachment ADD CONSTRAINT FK_GRANT_ATTACHMENT_ON_SUBMISSION FOREIGN KEY (submission_id) REFERENCES grant_submission (id);