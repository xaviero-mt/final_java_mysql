CREATE TABLE IF NOT EXISTS `tb_borrados` (
`id_multa` int(11) NOT NULL ,
  `dni` varchar(8) DEFAULT NULL,
  `tipo_multa` varchar(30) DEFAULT NULL,
  `monto` decimal(6,2) DEFAULT NULL,
  `correo` varchar(80) DEFAULT NULL,
  `punto` int(11) DEFAULT NULL,
  `fec_regi` datetime DEFAULT CURRENT_TIMESTAMP
);
CREATE TRIGGER tr_borrados BEFORE DELETE
ON multa
FOR EACH ROW BEGIN
insert into tb_borrados SELECT * FROM multa WHERE id_multa = old.id_multa;
END ;