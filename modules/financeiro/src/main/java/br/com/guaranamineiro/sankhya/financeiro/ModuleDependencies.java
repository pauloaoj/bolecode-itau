package br.com.guaranamineiro.sankhya.financeiro;

import br.com.guaranamineiro.sankhya.model.query.FinanceiroQueryMap;
import br.com.guaranamineiro.sankhya.model.query.GeralQueryMap;
import br.com.guaranamineiro.sankhya.model.util.QuerySet;

public class ModuleDependencies {
	
	public static void init() throws Exception {
		
	   QuerySet.writeQueries(GeralQueryMap.class, FinanceiroQueryMap.class);
		
	}

}
