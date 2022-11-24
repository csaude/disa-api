CREATE TABLE `OrgUnit` (
  `code` varchar(15) NOT NULL,
  `province` varchar(50) NOT NULL,
  `district` varchar(50) NOT NULL,
  `facility` varchar(50) NOT NULL,
  PRIMARY KEY (`code`),
  FULLTEXT KEY `search` (`province`,`district`,`facility`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
