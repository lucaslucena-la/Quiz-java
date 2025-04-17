// Pacote da interface gráfica
package com.QuizApp.gui;

// Importações do DAO e do modelo de dados
import com.QuizApp.dao.QuizAppDAO;
import com.QuizApp.model.QuizModel;

// Importações do JavaFX
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
import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;



// Classe principal da aplicação gráfica, estendendo Application (JavaFX)
public class QuizAppGui extends Application {

    // Campos de entrada para nome e senha (ou email)
    private TextField txtNome, txtSenha;

    // ListView para exibir os usuários cadastrados
    private ListView<QuizModel> listViewUsuarios;

    // Lista observável que alimenta o ListView (automático ao atualizar)
    private ObservableList<QuizModel> usuariosList;

    // Objeto para acessar o banco de dados (DAO)
    private QuizAppDAO quizDAO;

    // Método principal que inicia a aplicação JavaFX
    public void start(Stage primaryStage) {
        // Inicializa DAO
        quizDAO = new QuizAppDAO();
        usuariosList = FXCollections.observableArrayList(quizDAO.listarTodos());

        // VBox principal
        VBox conteudo = new VBox(15);
        conteudo.setPadding(new Insets(20));
        conteudo.setAlignment(Pos.CENTER);

        StackPane root = new StackPane(conteudo);

        // ------------------- LOGIN PANE -------------------
        VBox loginBox = new VBox(5);
        loginBox.setAlignment(Pos.CENTER);
        Label lblLogin = new Label("Login:");
        txtNome = new TextField();
        txtNome.setMaxWidth(200);
        loginBox.getChildren().addAll(lblLogin, txtNome);

        VBox senhaBox = new VBox(5);
        senhaBox.setAlignment(Pos.CENTER);
        Label lblSenha = new Label("Senha:");
        txtSenha = new TextField();
        txtSenha.setMaxWidth(200);
        senhaBox.getChildren().addAll(lblSenha, txtSenha);

        VBox inputPane = new VBox(10, loginBox, senhaBox);
        inputPane.setAlignment(Pos.CENTER);

        Button btnEntrar = new Button("Entrar");
        Button btnCadastrarSe = new Button("Cadastre-se");

        HBox buttonPane = new HBox(10, btnEntrar, btnCadastrarSe);
        buttonPane.setAlignment(Pos.CENTER);

        VBox loginPane = new VBox(10, inputPane, buttonPane);
        loginPane.setAlignment(Pos.CENTER);

        // ------------------- CADASTRO PANE -------------------
        VBox cadastroPane = new VBox(10);
        cadastroPane.setAlignment(Pos.CENTER);
        cadastroPane.setVisible(false);
        cadastroPane.setManaged(false); // impede que ocupe espaço quando invisível

        Label lblNovoLogin = new Label("Novo login:");
        TextField txtNovoLogin = new TextField();
        txtNovoLogin.setMaxWidth(200);

        Label lblNovaSenha = new Label("Nova senha:");
        TextField txtNovaSenha = new TextField();
        txtNovaSenha.setMaxWidth(200);

        CheckBox chkAdmin = new CheckBox("É administrador?");
        Button btnCadastrar = new Button("Cadastrar");
        Button btnVoltar = new Button("Voltar");

        cadastroPane.getChildren().addAll(lblNovoLogin, txtNovoLogin, lblNovaSenha, txtNovaSenha, chkAdmin, btnCadastrar, btnVoltar);

        // ------------------- Troca de telas -------------------
        btnCadastrarSe.setOnAction(e -> {
            loginPane.setVisible(false);
            loginPane.setManaged(false);

            cadastroPane.setVisible(true);
            cadastroPane.setManaged(true);
        });

        btnVoltar.setOnAction(e -> {
            cadastroPane.setVisible(false);
            cadastroPane.setManaged(false);

            loginPane.setVisible(true);
            loginPane.setManaged(true);
        });

        // ------------------- Ação de cadastro -------------------
        btnCadastrar.setOnAction(e -> {
            String nome = txtNovoLogin.getText().trim();
            String senha = txtNovaSenha.getText().trim();
            boolean admin = chkAdmin.isSelected();

            if (!nome.isEmpty() && !senha.isEmpty()) {
                QuizModel novoUsuario = new QuizModel(nome, senha, admin);
                quizDAO.salvar(novoUsuario);
                showAlert(Alert.AlertType.INFORMATION, "Cadastro", "Usuário cadastrado com sucesso!");

                // Limpa os campos e volta à tela de login
                txtNovoLogin.clear();
                txtNovaSenha.clear();
                chkAdmin.setSelected(false);
                btnVoltar.fire();
            } else {
                showAlert(Alert.AlertType.WARNING, "Erro", "Preencha todos os campos!");
            }
        });

        // ------------------- Finalização -------------------
        conteudo.getChildren().addAll(loginPane, cadastroPane);

        Scene scene = new Scene(root, 300, 350);
        primaryStage.setTitle("Login do Quiz");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    
    private void cadastrarSe() {
    	//janela secundária
    	Stage cadastroStage =  new Stage();
    	cadastroStage.setTitle("Cadastro de novo usuário");
    	
    	// Layout em grade
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20));
        grid.setHgap(10);
        grid.setVgap(10);
        
        // Campos
        Label lblLogin = new Label("Login:");
        TextField txtLogin = new TextField();

        Label lblSenha = new Label("Senha:");
        PasswordField txtSenha = new PasswordField();

        CheckBox chkAdmin = new CheckBox("Administrador");

        Button btnCadastrar = new Button("Cadastrar");
        
        // Adiciona os elementos ao GridPane
        grid.add(lblLogin, 0, 0);
        grid.add(txtLogin, 1, 0);

        grid.add(lblSenha, 0, 1);
        grid.add(txtSenha, 1, 1);

        grid.add(chkAdmin, 1, 2);

        grid.add(btnCadastrar, 1, 3);
        
     // Define ação do botão cadastrar
        btnCadastrar.setOnAction(e -> {
            String nome = txtLogin.getText().trim();
            String senha = txtSenha.getText().trim();
            boolean admin = chkAdmin.isSelected();

            if (!nome.isEmpty() && !senha.isEmpty()) {
                QuizModel novoUsuario = new QuizModel(nome, senha,chkAdmin.isSelected());
                novoUsuario.setAdmin(admin); // você precisa ter esse campo no modelo!

                quizDAO.salvar(novoUsuario);
                atualizarLista(); // Atualiza a tela principal
                showAlert(Alert.AlertType.INFORMATION, "Cadastro", "Usuário cadastrado com sucesso!");
                cadastroStage.close(); // Fecha a janela
            } else {
                showAlert(Alert.AlertType.WARNING, "Erro", "Preencha todos os campos!");
            }
        });
        
        Scene scene = new Scene(grid, 300, 200);
        cadastroStage.setScene(scene);
        cadastroStage.show();
    }

    // Método para cadastrar um novo usuário
    private void cadastrarUsuario() {
        String nome = txtNome.getText().trim();
        String senha = txtSenha.getText().trim();

        if (!nome.isEmpty() && !senha.isEmpty()) {
            try {
                QuizModel usuario = new QuizModel(nome, senha, false);
                quizDAO.salvar(usuario);
                atualizarLista(); // Atualiza a ListView
                limparCampos();   // Limpa os campos
                showAlert(Alert.AlertType.INFORMATION, "Sucesso", "Usuário cadastrado com sucesso!");
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.ERROR, "Erro", "Idade inválida!");
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Erro", "Preencha todos os campos!");
        }
    }
    
    
    private void fazerLogin() {
    	
    }

    // Método para alterar um usuário já existente
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

    // Método para excluir um usuário
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

    // Recarrega os dados do banco na lista da interface
    private void atualizarLista() {
        usuariosList.setAll(quizDAO.listarTodos());
    }

    // Limpa os campos de texto e desmarca seleção da lista
    private void limparCampos() {
        txtNome.clear();
        txtSenha.clear();
        listViewUsuarios.getSelectionModel().clearSelection();
    }

    // Mostra um alerta na tela
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Método principal que inicia a aplicação
    public static void main(String[] args) {
        launch(args); // Chama o start()
    }
}
