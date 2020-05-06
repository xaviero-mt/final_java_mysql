DELIMITER $$
DROP PROCEDURE IF EXISTS borrar_multa;
CREATE PROCEDURE borrar_multa(
IN  _pi_id_multa   INT
)
BEGIN
DELETE FROM multa WHERE id_multa = _pi_id_multa ;

END $$
DELIMITER ;