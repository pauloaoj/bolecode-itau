package br.com.guaranamineiro.sankhya.model.service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import br.com.guaranamineiro.sankhya.model.BoletoService;
import br.com.guaranamineiro.sankhya.model.constant.MessageType;
import br.com.guaranamineiro.sankhya.model.entity.Financeiro;
import br.com.guaranamineiro.sankhya.model.entity.Parceiro;
import br.com.guaranamineiro.sankhya.model.util.UtilDate;
import br.com.guaranamineiro.sankhya.model.util.UtilDecimal;
import br.com.guaranamineiro.sankhya.model.util.UtilEnv;
import br.com.guaranamineiro.sankhya.model.util.UtilException;
import br.com.guaranamineiro.sankhya.model.util.UtilProperties;
import br.com.guaranamineiro.sankhya.model.util.UtilString;
import br.com.sankhya.jape.dao.JdbcWrapper;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class BoletoITAUService implements BoletoService {

   //private FinanceiroRepository financeiroRepository;

   public BoletoITAUService(JdbcWrapper jdbc) {
      //financeiroRepository = RepositoryFactory.getFinanceiroRepository(jdbc);
   }

   @Override
   public JsonObject autenticar() throws Exception {

      Properties properties = UtilProperties.get();

      JsonObject param = new JsonObject();
      param.addProperty("client_id", properties.getProperty("ITAU_API_LOGIN_CLIENT_ID"));
      param.addProperty("client_secret", properties.getProperty("ITAU_API_LOGIN_CLIENT_SECRET"));

      RequestBody body = RequestBody.create(MediaType.parse("application/json"), param.toString());
      Request request = new Request.Builder()
            .header("Content-Type", "application/json")
            .url(properties.getProperty("ITAU_API_LOGIN"))
            .post(body)
            .build();

      OkHttpClient client = new OkHttpClient().newBuilder()
            .connectTimeout(200, TimeUnit.SECONDS)
            .writeTimeout(200, TimeUnit.SECONDS)
            .readTimeout(200, TimeUnit.SECONDS)
            .build();

      Response response = client.newCall(request).execute();

      JsonObject retorno = new JsonObject();
      retorno.addProperty("statusCode", response.code());
      if (response.code() == 200 && response.body() != null) {
         String bodyResponse = response.body().string();
         if (bodyResponse != null && !bodyResponse.isEmpty()) {
            JsonParser parser = new JsonParser();
            retorno.add("data", parser.parse(bodyResponse).getAsJsonObject());
         }
      } else {
         throw new UtilException(MessageType.API, "Não foi possível gerar o token de autenticação do ITAU.");
      }

      return retorno;
   }

   @Override
   public JsonObject registrar(JsonObject login, JsonObject param) throws Exception {
      
      Properties properties = UtilProperties.get();
      
      String type = login.get("data").getAsJsonObject().get("token_type").getAsString();
      String token = login.get("data").getAsJsonObject().get("access_token").getAsString();
      
      String authorization = String.format("%s %s", type, token);
              
      RequestBody body = RequestBody.create(MediaType.parse("application/json"), param.toString());      
      
      Request.Builder builder = new Request.Builder();
      builder.addHeader("Authorization", authorization);
      if (UtilEnv.isStaging()) {
         builder.header("x-sandbox-token", token);   
      }      
      builder.header("x-correlationID", UUID.randomUUID().toString());
      builder.url(String.format("%s/boletos_pix", properties.get("ITAU_API_RECEBIMENTOS_CONCILIACOES")));
      builder.post(body);
      
      Request request = builder.build();
      
      OkHttpClient client = new OkHttpClient().newBuilder()
              .connectTimeout(200, TimeUnit.SECONDS)
              .writeTimeout(200, TimeUnit.SECONDS)
              .readTimeout(200, TimeUnit.SECONDS)
              .build();
      
      Response response = client.newCall(request).execute();
      
      JsonObject retorno = new JsonObject();
      retorno.addProperty("statusCode", response.code());
      if ((response.code() == 200 || response.code() == 201) && response.body() != null) {
          String bodyResponse = response.body().string();
          if (bodyResponse != null && !bodyResponse.isEmpty()) {
             JsonParser parser = new JsonParser();
             retorno.add("data", parser.parse(bodyResponse).getAsJsonObject());
          }
      } else {
         throw new UtilException(MessageType.API, "Não foi possível registrar o boleto no ITAU.");
      }
      
      return retorno;
   }

   @Override
   public <T, P> JsonObject corpo(T financeiro, P parceiro) throws Exception {
      Properties properties = UtilProperties.get();

      Financeiro fin = (Financeiro) financeiro;
      Parceiro par = (Parceiro) parceiro;

      JsonObject corpo = new JsonObject();
      corpo.addProperty("etapa_processo_boleto", properties.getProperty("ITAU_DADOS_ETAPA_PROCESSO"));
      corpo.add("beneficiario", getCorpoBeneficiario());
      corpo.add("dado_boleto", getCorpoBoleto(fin, par));
      corpo.add("dados_qrcode", getCorpoPix());

      return corpo;
   }

   private JsonObject getCorpoBeneficiario() throws Exception {
      Properties properties = UtilProperties.get();

      String idBeneficiario = String.format("%s%s%s", properties.getProperty("ITAU_DADOS_AGENCIA"),
            properties.getProperty("ITAU_DADOS_CONTA"), properties.getProperty("ITAU_DADOS_DAC"));

      JsonObject beneficiario = new JsonObject();
      beneficiario.addProperty("id_beneficiario", idBeneficiario);

      return beneficiario;
   }

   private JsonObject getCorpoPagador(Parceiro par) throws Exception {

      JsonObject pessoa = new JsonObject();
      pessoa.addProperty("nome_pessoa", UtilString.removerAcentos(par.getNome()));
      pessoa.addProperty("nome_fantasia", UtilString.removerAcentos(par.getNomeRazaoSocial()));

      JsonObject tipoPessoa = new JsonObject();
      tipoPessoa.addProperty("codigo_tipo_pessoa", par.getTipo());
      if (par.getTipo().equals("F")) {
         tipoPessoa.addProperty("numero_cadastro_pessoa_fisica", par.getCpfCnpj());
      } else {
         tipoPessoa.addProperty("numero_cadastro_nacional_pessoa_juridica", par.getCpfCnpj());
      }
      pessoa.add("tipo_pessoa", tipoPessoa);

      JsonObject endereco = new JsonObject();
      endereco.addProperty("nome_logradouro", String.format("%s, %s", UtilString.removerAcentos(par.getNomeEndereco()), par.getNumero()));
      endereco.addProperty("nome_bairro", UtilString.removerAcentos(par.getNomeBairro()));
      endereco.addProperty("nome_cidade", UtilString.removerAcentos(par.getNomeCidade()));
      endereco.addProperty("sigla_UF", par.getUf());
      endereco.addProperty("numero_CEP", par.getCep());

      JsonObject pagador = new JsonObject();
      pagador.add("pessoa", pessoa);
      pagador.add("endereco", endereco);

      return pagador;
   }
   
   private String getVlrTotalTitulo(BigDecimal vlr) throws Exception {
      String vlrTotalTitulo = UtilDecimal.formatPtBr(vlr);
      vlrTotalTitulo = vlrTotalTitulo.replace(",", "");
      vlrTotalTitulo = String.format("%017d", Long.valueOf(vlrTotalTitulo));
      
      return vlrTotalTitulo;
   }
   
   private JsonArray getMensagemCobranca(List<String> mensagemLista) throws Exception {
      
      JsonObject msg = null;
      JsonArray arr = new JsonArray();
      
      for (String mensagem : mensagemLista) {
         msg = new JsonObject();
         msg.addProperty("mensagem", mensagem);
         
         arr.add(msg);
      }
      
      return arr;
   }
   
   private JsonArray getCorpoBoletoIndividual(Financeiro fin) throws Exception {
      
      JsonObject boleto = new JsonObject();
      boleto.addProperty("numero_nosso_numero", String.format("%08d", fin.getBoletoProximoNossoNumero()));
      boleto.addProperty("data_vencimento", UtilDate.stringToFormat(fin.getDataVencimento(), "yyyy-MM-dd"));
      boleto.addProperty("valor_titulo", getVlrTotalTitulo(fin.getVlrDesdobramento()));
      boleto.addProperty("data_limite_pagamento", UtilDate.stringToFormat(fin.getDataVencimento(), "yyyy-MM-dd"));

      JsonArray arr = new JsonArray();
      arr.add(boleto);
      
      return arr;
   }

   private JsonObject getCorpoBoleto(Financeiro fin, Parceiro par) throws Exception {
      Properties properties = UtilProperties.get();

      JsonObject boleto = new JsonObject();
      boleto.addProperty("tipo_boleto", properties.getProperty("ITAU_DADOS_TIPO_BOLETO"));      
      boleto.addProperty("codigo_carteira", properties.getProperty("ITAU_DADOS_CARTEIRA"));
      boleto.addProperty("valor_total_titulo", getVlrTotalTitulo(fin.getVlrDesdobramento()));
      boleto.addProperty("codigo_especie", properties.getProperty("ITAU_DADOS_ESPECIE"));
      boleto.addProperty("data_emissao", UtilDate.stringToFormat(fin.getDataEmissao(), "yyyy-MM-dd"));
      
      JsonObject protesto = new JsonObject();
      protesto.addProperty("protesto", properties.getProperty("ITAU_DADOS_PROTESTO"));
      
      JsonObject negativacao = new JsonObject();
      negativacao.addProperty("negativacao", properties.getProperty("ITAU_DADOS_NEGATIVACAO"));
      negativacao.addProperty("quantidade_dias_negativacao", properties.getProperty("ITAU_DADOS_NEGATIVACAO_DIAS"));
      
      //boleto.add("protesto", protesto);
      boleto.add("negativacao", negativacao);
      boleto.add("pagador", getCorpoPagador(par));
      boleto.add("dados_individuais_boleto", getCorpoBoletoIndividual(fin));
      
      List<String> listaMensagem = Arrays.asList("teste");
      boleto.add("lista_mensagem_cobranca", getMensagemCobranca(listaMensagem));      

      JsonObject juros = new JsonObject();
      juros.addProperty("data_juros", UtilDate.stringToFormat(fin.getDataVencimento(), "yyyy-MM-dd"));
      juros.addProperty("codigo_tipo_juros", properties.getProperty("ITAU_DADOS_JUROS"));
      //juros.addProperty("valor_juros", properties.getProperty("ITAU_DADOS_JUROS_VALOR"));
      //juros.addProperty("percentual_juros", properties.getProperty("ITAU_DADOS_JUROS_PERCENTUAL"));
      
      boleto.add("juros", juros);
   
      return boleto;
   }

   private JsonObject getCorpoPix() throws Exception {
      Properties properties = UtilProperties.get();
      
      JsonObject corpo = new JsonObject();
      corpo.addProperty("chave", properties.getProperty("ITAU_DADOS_QRCODE_CHAVE"));

      return corpo;
   }
   
}
