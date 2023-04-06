CREATE TABLE `ImplementingPartner` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `orgName` varchar(50) NOT NULL,
  `password` varchar(100) NOT NULL,
  `enabled` tinyint(1) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `orgName` (`orgName`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

ALTER TABLE `OrgUnit`
  ADD implementingPartnerId int(11),
  ADD FOREIGN KEY (implementingPartnerId) REFERENCES ImplementingPartner(id);
