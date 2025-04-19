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
    private Stage primaryStage;
    
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage; // salva referência


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
      
        //Definição do botão entrar   
        btnEntrar.setOnAction(e -> {
            String nome = txtNome.getText().trim();
            String senha = txtSenha.getText().trim();

            fazerLogin(nome, senha); // 👈 chamada ao novo método
        });


        
        Button btnCadastrarSe = new Button("Cadastre-se");

        HBox buttonPane = new HBox(10, btnEntrar, btnCadastrarSe);
        buttonPane.setAlignment(Pos.CENTER);

        VBox loginPane = new VBox(10, inputPane, buttonPane);
        loginPane.setAlignment(Pos.CENTER);

        // ------------------- ENTRAR PANE -------------------
        VBox EntrarPane = new VBox(10);
        
        EntrarPane.setAlignment(Pos.CENTER);
        EntrarPane.setVisible(false);
        EntrarPane.setManaged(false); // impede que ocupe espaço quando invisível
        
        
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

            cadastrarUsuario(nome, senha, admin);

            // Limpa os campos e volta para o login
            txtNovoLogin.clear();
            txtNovaSenha.clear();
            chkAdmin.setSelected(false);
            btnVoltar.fire();
        });


        // ------------------- Finalização -------------------
        conteudo.getChildren().addAll(loginPane, cadastroPane);

        Scene scene = new Scene(root, 300, 350);
        primaryStage.setTitle("Login do Quiz");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    // Método que abre uma nova janela para o painel do administrador
    private void abrirPainelAdmin() {
        // Cria uma nova janela (Stage) separada
        Stage adminStage = new Stage();
        adminStage.setTitle("Painel do Administrador");

        // Cria o layout vertical (VBox) com espaçamento entre os botões
        VBox painel = new VBox(15); // 15px entre os elementos
        painel.setPadding(new Insets(20)); // margem interna
        painel.setAlignment(Pos.CENTER); // centraliza os elementos no meio da tela

        // Criação dos 5 botões do painel admin
        Button btnCadastrarQuestao = new Button("Cadastrar Nova Questão");
        Button btnVisualizarQuestoes = new Button("Visualizar Questões");
        Button btnVerRanking = new Button("Ver Ranking");
        Button btnLogout = new Button("Logout");
        Button btnSair = new Button("Sair");

        // Define uma largura padrão para todos os botões
        btnCadastrarQuestao.setPrefWidth(200);
        btnVisualizarQuestoes.setPrefWidth(200);
        btnVerRanking.setPrefWidth(200);
        btnLogout.setPrefWidth(200);
        btnSair.setPrefWidth(200);

        // Ações dos botões (no momento apenas mensagens no terminal)
        
        // Ação do botão "Cadastrar Nova Questão"
        btnCadastrarQuestao.setOnAction(e -> {
            System.out.println("Cadastrar Questão clicado.");
            // abrirCadastrarQuestao(); // ← Aqui você pode chamar a tela real futuramente
        });

        // Ação do botão "Visualizar Questões"
        btnVisualizarQuestoes.setOnAction(e -> {
            System.out.println("Visualizar Questões clicado.");
            // abrirVisualizarQuestoes(); // ← Chamada futura para a tela de visualização
        });

        // Ação do botão "Ver Ranking"
        btnVerRanking.setOnAction(e -> {
            System.out.println("Ver Ranking clicado.");
            // abrirRanking(); // ← Aqui você pode abrir a tela do ranking depois
        });

        // Ação do botão "Logout"
        btnLogout.setOnAction(e -> {
            adminStage.close(); // Fecha a janela do painel admin
            adminStage.close();        // Fecha esta janela
            start(new Stage());       // Reabre a tela de login (reusando o método start)
        });

        // Ação do botão "Sair"
        btnSair.setOnAction(e -> {
            System.exit(0); // Encerra completamente o aplicativo
        });

        // Adiciona todos os botões ao VBox
        painel.getChildren().addAll(
            btnCadastrarQuestao,
            btnVisualizarQuestoes,
            btnVerRanking,
            btnLogout,
            btnSair
        );

        // Cria e exibe a cena da janela admin
        Scene scene = new Scene(painel, 300, 300);
        adminStage.setScene(scene);
        adminStage.show(); // Exibe a nova janela
    }


 // Método que abre a tela do usuário com seleção de dificuldade e botão para iniciar o quiz
    private void abrirPainelUsuario() {
        // Cria uma nova janela (Stage) separada
        Stage userStage = new Stage();
        userStage.setTitle("Escolha de Dificuldade");

        // ComboBox com opções de dificuldade
        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.getItems().addAll("Fácil", "Médio", "Difícil");
        comboBox.setPromptText("Selecione a dificuldade");

        // Botão para iniciar o quiz
        Button btnIniciarQuiz = new Button("Iniciar Quiz");
        btnIniciarQuiz.setDisable(true); // desativado até selecionar uma opção

        // Botão "Voltar"
        Button btnVoltar = new Button("Voltar");
        
        // Quando o usuário escolher uma dificuldade, habilita o botão
        comboBox.setOnAction(e -> {
            String selecionado = comboBox.getValue();
            if (selecionado != null && !selecionado.isEmpty()) {
                btnIniciarQuiz.setDisable(false);
            }
        });
        

        // Ação ao clicar no botão "Iniciar Quiz"
        btnIniciarQuiz.setOnAction(e -> {
            String dificuldade = comboBox.getValue();
            System.out.println("Iniciando quiz na dificuldade: " + dificuldade);
            // Aqui você chama o método para carregar o quiz
            // iniciarQuiz(dificuldade);
            userStage.close(); // Fecha a janela atual se quiser
        });
        
     // Ação ao clicar em "Voltar" → fecha tela e reabre login
        btnVoltar.setOnAction(e -> {
            userStage.close();        // Fecha esta janela
            start(new Stage());       // Reabre a tela de login (reusando o método start)
        });

        // Layout da tela
        VBox painel = new VBox(15);
        painel.setPadding(new Insets(20));
        painel.setAlignment(Pos.CENTER);
        painel.getChildren().addAll(comboBox, btnIniciarQuiz, btnVoltar);

        // Exibe a janela
        Scene scene = new Scene(painel, 300, 200);
        userStage.setScene(scene);
        userStage.show();

        System.out.println("Painel Usuário aberto.");
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
 // Método para cadastrar um novo usuário (usuário comum ou admin)
    private void cadastrarUsuario(String nome, String senha, boolean admin) {
        if (!nome.isEmpty() && !senha.isEmpty()) {
            try {
                QuizModel novoUsuario = new QuizModel(nome, senha, admin);
                quizDAO.salvar(novoUsuario);

                String tipo = admin ? "administrador" : "usuário";
                showAlert(Alert.AlertType.INFORMATION, "Cadastro", "Cadastro realizado com sucesso como " + tipo + "!");

            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao cadastrar o usuário.");
                e.printStackTrace();
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Erro", "Preencha todos os campos!");
        }
    }


    
    private void fazerLogin(String nome, String senha) {
        if (!nome.isEmpty() && !senha.isEmpty()) {
            QuizModel usuario = quizDAO.buscarPorNomeESenha(nome, senha);

            if (usuario != null) {
                if (usuario.getAdmin()) {
                    showAlert(Alert.AlertType.INFORMATION, "Bem-vindo", "Login como administrador.");
                    primaryStage.close(); // fecha a tela de login
                    abrirPainelAdmin();
                    System.out.println("Admin? " + usuario.getAdmin());

                } else {
                    showAlert(Alert.AlertType.INFORMATION, "Bem-vindo", "Login como usuário.");
                    primaryStage.close(); // fecha a tela de login
                    abrirPainelUsuario();
                    System.out.println("Admin? " + usuario.getAdmin());

                }
            } else {
                showAlert(Alert.AlertType.ERROR, "Erro", "Usuário ou senha inválidos.");
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Aviso", "Preencha todos os campos.");
        }
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
