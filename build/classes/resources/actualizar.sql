DELIMITER $$
DROP PROCEDURE IF EXISTS actualizar_multa ;
CREATE PROCEDURE actualizar_multa(
    IN  _pi_id_multa   INT,
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
    DECLARE _v_cant_multas INT;
    DECLARE _v_id_multa    INT;
    DECLARE _v_cant_puntos INT;
    DECLARE _v_array1      INT;
    DECLARE _v_array2      INT;
    --
    IF _pi_monto > 1000 THEN
        SET _p_cod_error = 1;
        SET _p_msj_error = 'La multa no puede ser mayor a 1000 soles.';
        LEAVE this_proc;
    END IF;
    IF _pi_tipo_multa = 'Alta Velocidad' and (_pi_monto <400 | _pi_monto >570) THEN
        SET _p_cod_error = 2;
        SET _p_msj_error = 'La cantidad no debe ser menor a 400 ni mayor a 570.';
        LEAVE this_proc;
    END IF;
    IF _pi_tipo_multa = 'Luz Roja' and (_pi_monto <130 | _pi_monto >250) THEN
        SET _p_cod_error = 3;
        SET _p_msj_error = 'La cantidad no debe ser menor a 130 ni mayor a 250.';
        LEAVE this_proc;
    END IF;
    IF _pi_tipo_multa = 'Mal Estacionado' and (_pi_monto <100 | _pi_monto >190) THEN
        SET _p_cod_error = 4;
        SET _p_msj_error = 'La cantidad no debe ser menor a 100 ni mayor a 190.';
        LEAVE this_proc;
    END IF;
    IF _pi_tipo_multa = 'Pico y Placa' and (_pi_monto <130 | _pi_monto >330) THEN
        SET _p_cod_error = 5;
        SET _p_msj_error = 'La cantidad no debe ser menor a 130 ni mayor a 330.';
        LEAVE this_proc;
    END IF;
    
    IF _pi_tipo_multa = 'Alta Velocidad' AND (_pi_punto <  10 | _pi_punto > 30) THEN
        SET _p_cod_error = 6;
        SET _p_msj_error = 'Los Puntos deben ser min 10 - max 30.';
        LEAVE this_proc;
    END IF;
    IF _pi_tipo_multa = 'Luz Roja' AND (_pi_punto <  3 | _pi_punto > 14) THEN
        SET _p_cod_error = 7;
        SET _p_msj_error = 'Los Puntos deben ser min 3  - max 14.';
        LEAVE this_proc;
    END IF;
    IF _pi_tipo_multa = 'Mal estacionado' AND (_pi_punto <  15 | _pi_punto > 30) THEN
        SET _p_cod_error = 8;
        SET _p_msj_error = 'Los Puntos deben ser min 15 - max 30.';
        LEAVE this_proc;
    END IF;
    IF _pi_tipo_multa = 'Pico y placa' AND (_pi_punto <  13 | _pi_punto > 20 )THEN
        SET _p_cod_error = 9;
        SET _p_msj_error = 'Los Puntos deben ser min 13 - max 20.';
        LEAVE this_proc;
    END IF;
    
    SELECT COUNT(1)
      INTO _v_cant_multas
      FROM multa
     WHERE dni            = _pi_dni
       AND DATE(fec_regi) = _pi_fecha;
    IF _v_cant_multas >= 2 THEN
        SET _p_cod_error = 10;
        SET _p_msj_error = 'Hubo un erroe al contabilizar las multas.';
        LEAVE this_proc;
    END IF;
    IF _v_cant_multas >= 2 THEN
        SET _p_cod_error = 11;
        SET _p_msj_error = 'No se puede registrar mas de 2 multas por día.';
        LEAVE this_proc;
    END IF;
    SELECT SUM(punto) - punto INTO _v_cant_puntos
      FROM multa
     WHERE dni            = _pi_dni ;
    IF _v_cant_puntos = -1 THEN
        SET _p_cod_error = 12;
        SET _p_msj_error = 'Hubo un error al contabilizar los puntos.';
        LEAVE this_proc;
    END IF;
    IF _v_cant_puntos + _pi_punto > 100 THEN
        SET _p_cod_error = 13;
        SET _p_msj_error = CONCAT( 'con ', _pi_punto ,' supera los 100 puntos maximos.');
        LEAVE this_proc;
    END IF;
    --calcular el incremento dependiendo del puntaje
  --
    UPDATE multa SET dni=_pi_dni , tipo_multa=_pi_tipo_multa , monto=_pi_monto , correo=_pi_correo , punto=_pi_punto , fec_regi=_pi_fecha 
    WHERE id_multa=_pi_id_multa ;
    SET _p_cod_error = 0;
    SET _p_msj_error = 'Se Actualizo la multa.';
    SET _po_id_multa = _pi_id_multa ;
END $$ 
DELIMITER ;

