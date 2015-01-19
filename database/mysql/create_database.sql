CREATE SCHEMA `competitors`
  DEFAULT CHARACTER SET utf8
  COLLATE utf8_general_ci;
CREATE USER 'user_tmz'@'localhost'
  IDENTIFIED BY '123';
GRANT ALL PRIVILEGES ON `tmz`.* TO 'user_tmz'@'localhost'
WITH GRANT OPTION;

CREATE TABLE `prices` (
  `ID` int(10) NOT NULL AUTO_INCREMENT,
  `competitor` varchar(250) CHARACTER SET cp1251 DEFAULT NULL,
  `SCU` varchar(45) CHARACTER SET cp1251 DEFAULT NULL,
  `kindShoes` varchar(250) CHARACTER SET cp1251 DEFAULT NULL,
  `upperMaterial` varchar(250) CHARACTER SET cp1251 DEFAULT NULL,
  `lining` varchar(250) CHARACTER SET cp1251 DEFAULT NULL,
  `context` varchar(150) CHARACTER SET cp1251 DEFAULT NULL,
  `season` varchar(100) CHARACTER SET cp1251 DEFAULT NULL,
  `priceDate` date DEFAULT NULL,
  `price` bigint(20) DEFAULT NULL,
  `category` varchar(450) CHARACTER SET cp1251 DEFAULT NULL,
  `sole` varchar(250) CHARACTER SET cp1251 DEFAULT NULL,
  `country` varchar(200) CHARACTER SET cp1251 DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=265 DEFAULT CHARSET=utf8;





