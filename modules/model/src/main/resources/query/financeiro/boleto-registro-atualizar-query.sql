UPDATE TGFFIN 
   SET 
      NOSSONUM = :NOSSONUM, 
      LINHADIGITAVEL = :LINHADIGITAVEL, 
      CODIGOBARRA = :CODIGOBARRA,
      EMVPIX = :EMVPIX,
      AD_BR_REGISTRADO = 'S',
      AD_BR_REGISTRADO_DATA = SYSDATE
 WHERE NUFIN = :NUFIN
   AND ROWNUM <= 1