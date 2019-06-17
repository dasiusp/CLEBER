# CLEBER
Certificados Livremente Emitidos, Burocratizados E Ratificados pelo DASI

# Visão do negócio de funcionalidades

[![imagem negocio](assets/Diagramas/CLEBER%20-%20Vis%C3%A3o%20Neg%C3%B3cio.png)](assets/Diagramas/CLEBER%20-%20Vis%C3%A3o%20Neg%C3%B3cio.png)


## Organização do Projeto


### Tecnologias

#### Spring Boot

O CLEBER é um projeto em [Spring Boot](https://spring.io/projects/spring-boot), escrito na linguagem de programação [Kotlin](https://kotlinlang.org/)

#### Testes
Os testes do projeto são escritos através do framework [Kotlintest](http://kotlintest.io)

#### Executando Localmente

Para executar localmente, é necessário executar a _task_ `bootrun`, que é fornecida pelo Spring para a ferramenta de build `gradle`.

Na raíz do projeto, execute:

Linux:
```
./gradlew bootrun
```

Windows:
```
gradlew.bat bootrun
```

E a aplicação principal deverá subir, permitindo acesso aos endpoints HTTP