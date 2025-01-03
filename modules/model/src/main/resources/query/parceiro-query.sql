SELECT 
   TGFPAR.CODPARC, 
   TGFPAR.NOMEPARC, 
   TGFPAR.RAZAOSOCIAL, 
   TGFPAR.TIPPESSOA, 
   TGFPAR.CGC_CPF, 
   TSIUFS.UF, 
   TSIUFS.DESCRICAO AS NOME_UF, 
   TSICID.NOMECID, 
   TSIBAI.NOMEBAI, 
   TGFPAR.CEP, 
   TGFPAR.COMPLEMENTO, 
   TGFPAR.NUMEND, 
   TSIEND.NOMEEND
  FROM TGFPAR
     INNER JOIN TSICID ON (TGFPAR.CODCID = TSICID.CODCID)
     INNER JOIN TSIUFS ON (TSICID.UF = TSIUFS.CODUF)
     INNER JOIN TSIBAI ON (TGFPAR.CODBAI = TSIBAI.CODBAI)
     INNER JOIN TSIEND ON (TGFPAR.CODEND = TSIEND.CODEND)
 WHERE TGFPAR.CODPARC = :CODPARC