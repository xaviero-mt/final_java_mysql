DELIMITER $$
DROP PROCEDURE IF EXISTS borrar_multa;
CREATE PROCEDURE borrar_multa(
IN  _pi_id_multa   INT,
OUT _p_cod_error   INT,
OUT _p_msj_error   VARCHAR(100),
OUT _po_id_multa   INT
)
BEGIN
DELETE FROM multa WHERE id_multa = _pi_id_multa ;
SET _p_cod_error = 0 ;
SET _p_msj_error = 'SE HA BORRADO LA MULTA';
SET _po_id_multa = _pi_id_multa ;
END $$
DELIMITER ;