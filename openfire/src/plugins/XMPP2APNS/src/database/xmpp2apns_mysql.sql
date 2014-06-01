# $Revision$
# $Date$

INSERT INTO ofVersion (name, version) VALUES ('xmpp2apns', 1);

CREATE TABLE ofAPNS (
	JID				VARCHAR(200)		NOT NULL,
	devicetoken		VARCHAR	(1000)		NOT NULL,
	devicetype		Int (11)	        NOT NULL,
	badge_number	Int	(11),
   PRIMARY KEY (JID)
);