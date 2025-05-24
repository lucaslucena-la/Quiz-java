
# Instruções para Rodar o Projeto JavaFX - Quiz

Este projeto é feito em Java usando JavaFX e Maven.

## ✅ Requisitos

1. Java JDK 17 ou superior instalado
2. Apache Maven instalado e no PATH
3. JavaFX SDK 21.0.7 baixado e extraído

## 📥 Download do JavaFX

Acesse: https://gluonhq.com/products/javafx/

Baixe a versão:
- **JavaFX SDK 21.0.7 – Windows x64**
- Extraia para uma pasta como:

```
C:\javafx-sdk-21.0.7
```

## 📁 Estrutura esperada

O projeto deve conter:

```
Trabalho-quiz/
├── pom.xml
├── src/
├── target/
├── README.txt
```

## ▶️ Como Rodar o Projeto

1. **Abra o terminal (CMD)**

2. **Vá até a pasta do projeto**

```cmd
cd "C:\caminho\para\Trabalho-quiz"
```

3. **Defina a variável com o caminho da pasta `lib` do JavaFX**

```cmd
set PATH_TO_FX=C:\javafx-sdk-21.0.7\lib
```

4. **Execute o projeto com Maven**

```cmd
mvn compile exec:java
```

Se tudo estiver correto, o aplicativo JavaFX será aberto.

## ❗ Dicas

- Se `mvn` não funcionar, adicione o Maven ao PATH do Windows.
- Se der erro de vídeo (QuantumRenderer), talvez o computador precise de atualização de driver gráfico ou forçar o modo software.

## 📎 Links úteis

- JDK 21: https://www.oracle.com/java/technologies/javase/jdk21-archive-downloads.html
- Maven: https://maven.apache.org/download.cgi
- JavaFX: https://gluonhq.com/products/javafx/
