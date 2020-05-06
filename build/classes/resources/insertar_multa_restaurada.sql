DELIMITER $$
DROP PROCEDURE IF EXISTS insertar_multa_restaurada;
CREATE PROCEDURE insertar_multa_restaurada(
    IN  _pi_dni        VARCHAR(8),
    IN  _pi_fecha      DATE,
    IN  _pi_punto      INT,
    IN  _pi_monto      NUMERIC,
    IN  _pi_correo     VARCHAR(80),
    IN  _pi_tipo_multa VARCHAR(20),
    OUT _p_cod_error   INT,
    OUT _p_msj_error   VARCHAR(100),
    OUT _po_id_multa   INT
)
this_proc:BEGIN
Declare _v_id_multa int ;
    INSERT INTO `sat`.`multa`
             (`dni`  , `tipo_multa`  , `monto`  , `correo`  , `punto`  , `fec_regi`)
      VALUES (_pi_dni, _pi_tipo_multa, _pi_monto, _pi_correo, _pi_punto, _pi_fecha)
    ;
    SELECT LAST_INSERT_ID() INTO _v_id_multa;
    SET _p_cod_error = 0;
    SET _p_msj_error = 'Se registró la multa.';
    SET _po_id_multa = _v_id_multa;
END $$ 
DELIMITER ;

