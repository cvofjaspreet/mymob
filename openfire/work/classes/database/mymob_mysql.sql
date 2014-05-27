# $Revision$
# $Date$

INSERT INTO ofVersion (name, version) VALUES ('mymob', 0);
  
Create TABLE ofShare (
shareId        BIGINT        AUTO_INCREMENT,
shareDate      BIGINT        NOT NULL,
text           VARCHAR(2000)       NULL,
imageUrl       VARCHAR(200)       NULL,
videoUrl       VARCHAR(200)        NULL,
thumbUrl       VARCHAR(200)        NULL,
jid            VARCHAR(200)       NOT NULL,
type           VARCHAR(200)       NOT NULL,
PRIMARY KEY (shareId)
);