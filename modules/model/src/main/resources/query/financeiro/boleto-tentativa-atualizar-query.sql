UPDATE TGFFIN SET AD_BR_TENTATIVA = NVL(AD_BR_TENTATIVA, 0) + 1 
 WHERE NUFIN = :NUFIN
   AND ROWNUM <= 1