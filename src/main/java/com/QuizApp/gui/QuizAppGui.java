package com.QuizApp.gui;


import com.QuizApp.dao.QuizAppDAO ;
import com.QuizApp.model.QuizModel;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class QuizAppGui extends Application {
    private TextField txtNome, txtSenha;
    private ListView<QuizModel> listViewUsuarios;
    private ObservableList<QuizModel> usuariosList;
    private QuizAppDAO quizDAO;

    @Override
    public void start(Stage primaryStage) {
    	quizDAO = new QuizAppDAO();
        usuariosList = FXCollections.observableArrayList(quizDAO.listarTodos());

        // Layout principal
        VBox root = new VBox(10);
        root.setPadding(new Insets(10));

        // Painel de entrada
        GridPane inputPane = new GridPane();
        inputPane.setHgap(10);
        inputPane.setVgap(10);

        inputPane.add(new Label("Nome:"), 0, 0);
        txtNome = new TextField();
        inputPane.add(txtNome, 1, 0);

        inputPane.add(new Label("Senha:"), 0, 1);
        txtSenha = new TextField();
        inputPane.add(txtSenha, 1, 1);

   

        // Botões
        HBox buttonPane = new HBox(10);
        Button btnCadastrar = new Button("Cadastrar");
        Button btnAlterar = new Button("Alterar");
        Button btnExcluir = new Button("Excluir");
        buttonPane.getChildren().addAll(btnCadastrar, btnAlterar, btnExcluir);

        // ListView
        listViewUsuarios = new ListView<>(usuariosList);
        listViewUsuarios.setPrefHeight(300);
        listViewUsuarios.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                txtNome.setText(newVal.getNome());
                txtSenha.setText(newVal.getEmail());
            }
        });

        // Adiciona tudo ao layout
        root.getChildren().addAll(inputPane, buttonPane, listViewUsuarios);

        // Ações dos botões
        btnCadastrar.setOnAction(e -> cadastrarUsuario());
        btnAlterar.setOnAction(e -> alterarUsuario());
        btnExcluir.setOnAction(e -> excluirUsuario());

        // Configura a cena
        Scene scene = new Scene(root, 400, 500);
        primaryStage.setTitle("Gerenciamento de Usuários - JavaFX");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void cadastrarUsuario() {
        String nome = txtNome.getText().trim();
        String senha = txtSenha.getText().trim();

        if (!nome.isEmpty() && !senha.isEmpty()) {
            try {
               
                QuizModel usuario = new QuizModel(nome, senha);
                quizDAO.salvar(usuario);
                atualizarLista();
                limparCampos();
                showAlert(Alert.AlertType.INFORMATION, "Sucesso", "Usuário cadastrado com sucesso!");
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.ERROR, "Erro", "Idade inválida!");
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Erro", "Preencha todos os campos!");
        }
    }

    private void alterarUsuario() {
    	QuizModel selecionado = listViewUsuarios.getSelectionModel().getSelectedItem();
        if (selecionado != null) {
            selecionado.setNome(txtNome.getText().trim());
            selecionado.setEmail(txtSenha.getText().trim());
  

            quizDAO.atualizar(selecionado);
            atualizarLista();
            limparCampos();
            showAlert(Alert.AlertType.INFORMATION, "Sucesso", "Usuário alterado com sucesso!");
        } else {
            showAlert(Alert.AlertType.WARNING, "Erro", "Selecione um usuário para alterar!");
        }
    }

    private void excluirUsuario() {
    	QuizModel selecionado = listViewUsuarios.getSelectionModel().getSelectedItem();
        if (selecionado != null) {
        	quizDAO.excluir(selecionado);
            atualizarLista();
            limparCampos();
            showAlert(Alert.AlertType.INFORMATION, "Sucesso", "Usuário excluído com sucesso!");
        } else {
            showAlert(Alert.AlertType.WARNING, "Erro", "Selecione um usuário para excluir!");
        }
    }

    private void atualizarLista() {
        usuariosList.setAll(quizDAO.listarTodos());
    }

    private void limparCampos() {
        txtNome.clear();
        txtSenha.clear();
        listViewUsuarios.getSelectionModel().clearSelection();
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

