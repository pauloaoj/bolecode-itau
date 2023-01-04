# Desenvolvimento para Guaraná Mineiro

Foi desenvolvido um projeto base com a estrutura de módulos JAR para o ERP Sankhya, tendo como objetivo inicial a automação do registro de boleto utilizando a API de integração do banco ITAÚ.

## Banco de dados Sankhya

* Criação da tabela **AD_LOG** na tela **Construtor de Telas** no Sankhya;
* Criação de colunas na tabela **TGFFIN** para a estrutura de registro de boletos (AD_BR_REGISTRADO, AD_BR_REGISTRADO_DATA, AD_BR_TENTATIVA);
  * Campo **AD_BR_REGISTRADO**: É gravado 'S' para quando o boleto for registrado e para controlar itens já registrados.
  * Campo **AD_BR_REGISTRADO_DATA**: Data de quando foi realizado o registro de boleto.
  * Campo **AD_BR_TENTATIVA**: Controla a quantidade de vezes que o financeiro pode ser executado para registrar o boleto.

## Instalando e configurando a IDE Eclipse

* Instale JAVA 8 (JDK) neste link: https://www.oracle.com/br/java/technologies/javase/javase8-archive-downloads.html
* Instale a IDE Eclipse neste link: https://www.eclipse.org/downloads

Com as ferramentas instaladas siga os passos de configuração do projeto a seguir.

## Configurando o projeto

Com a Eclipse IDE aberto selecione o modo de exebição de projeto **Top Level Elements -> Working Sets**, logo após crie a Working Set **gm-sankhya**.

![gm1](https://user-images.githubusercontent.com/7584116/203638921-4a3f1870-3f4d-4b7b-b991-8ef8ed2303b5.png)

Clone o projeto para alguma pasta em especifico de sua preferência. Pode ser executado por **SSH** se possuir uma chave de autenticação ou **HTTPS**. Para executar o comando é necessário ter instalado o git na maquina, que pode ser encontrado aqui https://git-scm.me/ .

```git
git clone git@github.com:pauloaoj/gm-sankhya.git
```

Comando via **HTTPS** vai ser preciso autenticar com usuário e senha

```git
git clone https://github.com/pauloaoj/gm-sankhya.git
```
### Importando o projeto

Com o projeto clonado, clique sobre a **Working Set -> gm-sankhya** botão direito e vá a opção **Import**, selecione **Existing Maven Projects**.

![gm2](https://user-images.githubusercontent.com/7584116/203640635-9225aeaa-cb24-4e99-a286-afb2558f551a.png)

Selecione apenas o projeto **gm-sankhya** e clique em **finish**.

![gm3](https://user-images.githubusercontent.com/7584116/203640808-002d3e51-78d6-4557-928d-ff6b0e7dfd10.png)

Logo após esse procedimento repita o mesmo para importar os demais projetos que são dependentes do projeto principal **gm-sankhya**.

![gm4](https://user-images.githubusercontent.com/7584116/203641137-265142d6-ac91-4cc9-9002-97dfea465eaf.png)

### Configurando o ambiente de compilação dos JAR

Crie a configuração de instalação das bibliotecas do sankhya no projeto.

![gm5](https://user-images.githubusercontent.com/7584116/203642342-9041cd7e-8dec-41b5-b56f-0b53dfa57a93.png)

Crie a configuração de geração do JAR em produção

![gm6](https://user-images.githubusercontent.com/7584116/203642415-b39c799f-4635-4727-b42d-7aa0ad580ebe.png)

![gm7](https://user-images.githubusercontent.com/7584116/203642434-faa751e7-f8f8-4028-bd4a-61d68ce6e74c.png)

Crie a configuração de geração de JAR em staging (TESTE)

![gm8](https://user-images.githubusercontent.com/7584116/203642526-65753b97-3e8b-4cb6-ba1e-c6af03ed2adb.png)

![gm9](https://user-images.githubusercontent.com/7584116/203642543-4ec5cb93-8bfe-4048-966d-627a4435b283.png)


