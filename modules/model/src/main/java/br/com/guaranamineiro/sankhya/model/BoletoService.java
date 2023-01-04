package br.com.guaranamineiro.sankhya.model;

import com.google.gson.JsonObject;

public interface BoletoService {

   JsonObject autenticar() throws Exception;
   <F, P> JsonObject corpo(F financeiro, P parceiro) throws Exception;
   JsonObject registrar(JsonObject login, JsonObject param) throws Exception;

}
