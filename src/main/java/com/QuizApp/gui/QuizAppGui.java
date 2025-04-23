// Pacote da interface gráfica
package com.QuizApp.gui;

// Importações do DAO e do modelo de dados
//Importações do DAO e do modelo de dados
import com.QuizApp.dao.QuizAppDAO;
import com.QuizApp.dao.QuestaoDAO;
import com.QuizApp.dao.PontuacaoDAO;

import com.QuizApp.model.QuizModel;
import com.QuizApp.model.Questao;
import com.QuizApp.model.Pontuacao;

//Extras para salvar data
import java.time.LocalDateTime;


// Java
import java.util.List;

// JavaFX - Componentes
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.Node;


// JavaFX - Layouts
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

// JavaFX - Animações e bindings
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

// JavaFX - Dados dinâmicos
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


// Classe principal da aplicação gráfica, estendendo Application (JavaFX)
public class QuizAppGui extends Application {

    // Campos de entrada para nome 
    private TextField txtNome;
    
    private PasswordField txtSenha;


    // ListView para exibir os usuários cadastrados
    private ListView<QuizModel> listViewUsuarios;

    // Lista observável que alimenta o ListView (automático ao atualizar)
    private ObservableList<QuizModel> usuariosList;

    // Objeto para acessar o banco de dados (DAO)
    private QuizAppDAO quizDAO;

    // Método principal que inicia a aplicação JavaFX
    private Stage primaryStage;
    
    private String usuarioLogado; // armazena o nome do usuário logado

    
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
        txtSenha = new PasswordField();
        txtSenha.setMaxWidth(200);
        senhaBox.getChildren().addAll(lblSenha, txtSenha);

        VBox inputPane = new VBox(10, loginBox, senhaBox);
        inputPane.setAlignment(Pos.CENTER);

        Button btnEntrar = new Button("Entrar");
      
        //Definição do botão entrar   
        btnEntrar.setOnAction(e -> {
            String nome = txtNome.getText().trim();
            String senha = txtSenha.getText().trim();

            if (!nome.isEmpty() && !senha.isEmpty()) {
                QuizModel usuario = quizDAO.buscarPorNomeESenha(nome, senha);

                if (usuario != null) {
                    usuarioLogado = usuario.getNome(); 
                    
                    Stage stageAtual = (Stage) ((Node) e.getSource()).getScene().getWindow();
                    stageAtual.close();

                    if (usuario.getAdmin()) {
                        showAlert(Alert.AlertType.INFORMATION, "Bem-vindo", "Login como administrador.");
                        abrirPainelAdmin();
                    } else {
                        showAlert(Alert.AlertType.INFORMATION, "Bem-vindo", "Login como usuário.");
                        abrirPainelUsuario(); 
                    }
                } else {
                    showAlert(Alert.AlertType.ERROR, "Erro", "Usuário ou senha inválidos.");
                }
            } else {
                showAlert(Alert.AlertType.WARNING, "Aviso", "Preencha todos os campos.");
            }
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
            
         // Verifica se os campos obrigatórios estão preenchidos
            if (nome.isEmpty() || senha.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Erro", "Preencha todos os campos!");
                return; // sai da função se tiver algo vazio
            }

            // 🚨 Verifica se o nome de login já existe no banco
            if (quizDAO.nomeExiste(nome)) {
                showAlert(Alert.AlertType.WARNING, "Erro", "Este nome de login já está sendo usado. Escolha outro.");
                return; // impede o cadastro se o nome já existir
            }

            cadastrarUsuario(nome, senha, admin);

            // Limpa os campos e volta para o login
            txtNovoLogin.clear();
            txtNovaSenha.clear();
            chkAdmin.setSelected(false);
            btnVoltar.fire();
        });


        // ------------------- Finalização -------------------
        conteudo.getChildren().addAll(loginPane, cadastroPane);
        
        //inserirQuestoesExemplo(); // <<< Roda uma única vez


        Scene scene = new Scene(root, 300, 350);
        aplicarEstilo(scene);
        primaryStage.setTitle("Login do Quiz");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    
    private void iniciarQuiz(String dificuldade, String usuario) {
        Stage quizStage = new Stage();
        quizStage.setTitle("Quiz");

        // Buscar questões do banco pela dificuldade
        QuestaoDAO dao = new QuestaoDAO();
        List<Questao> questoes = dao.listarPorDificuldade(dificuldade); // método que vamos criar

        if (questoes.isEmpty()) {
            showAlert(Alert.AlertType.INFORMATION, "Aviso", "Nenhuma questão encontrada para esta dificuldade.");
            return;
        }
        
        //controle de pontos
        final int[] acertos = {0};
        final int[] pontos = {0};


        // Controle de índice
        final int[] indiceAtual = {0};
        final Label tempoLabel = new Label("Tempo restante: 60s");
        final Timeline[] timer = {null};

        // Elementos dinâmicos
        Label lblEnunciado = new Label();
        ToggleGroup grupo = new ToggleGroup();
        RadioButton rbA = new RadioButton();
        RadioButton rbB = new RadioButton();
        RadioButton rbC = new RadioButton();
        RadioButton rbD = new RadioButton();
        rbA.setToggleGroup(grupo);
        rbB.setToggleGroup(grupo);
        rbC.setToggleGroup(grupo);
        rbD.setToggleGroup(grupo);

        Button btnProximo = new Button("Próximo");

        // Função para exibir a questão
        Runnable exibirQuestao = () -> {
            if (indiceAtual[0] >= questoes.size()) {
            	showAlert(
            		    Alert.AlertType.INFORMATION,
            		    "Quiz finalizado!",
            		    "Você acertou " + acertos[0] + " de " + questoes.size() + " questões.\n" +
            		    "Pontuação total: " + pontos[0] + " pontos."
            		);
            	
            	// Salva no ranking
            	PontuacaoDAO pdao = new PontuacaoDAO();
            	pdao.salvarOuAtualizarPontuacao(usuario, pontos[0]);

            	
            	
                quizStage.close();
                abrirPainelUsuario(); 

                return;
            }

            Questao q = questoes.get(indiceAtual[0]);
            lblEnunciado.setText(q.getEnunciado());
            rbA.setText(q.getOpcaoA());
            rbB.setText(q.getOpcaoB());
            rbC.setText(q.getOpcaoC());
            rbD.setText(q.getOpcaoD());

            grupo.selectToggle(null); // desmarca seleção
        };

        btnProximo.setOnAction(e -> {
            // Pega a resposta marcada
            RadioButton selecionada = (RadioButton) grupo.getSelectedToggle();

            if (selecionada != null) {
                Questao q = questoes.get(indiceAtual[0]);

                // Recupera o texto da resposta correta com base na letra marcada
                String correta = switch (q.getCorreta()) {
                    case "A" -> q.getOpcaoA();
                    case "B" -> q.getOpcaoB();
                    case "C" -> q.getOpcaoC();
                    case "D" -> q.getOpcaoD();
                    default -> "";
                };

                // Compara com a opção escolhida pelo usuário
                String respostaUsuario = selecionada.getText();
                if (respostaUsuario.equals(correta)) {
                    acertos[0]++;

                    // Adiciona pontuação baseada na dificuldade
                    int valor = switch (q.getDificuldade().toLowerCase()) {
                        case "fácil" -> 2;
                        case "médio" -> 3;
                        case "difícil" -> 4;
                        default -> 1;
                    };
                    pontos[0] += valor;
                }
            }

            // Próxima pergunta
            indiceAtual[0]++;
            exibirQuestao.run();
        });


        VBox layout = new VBox(10,
                tempoLabel,
                lblEnunciado,
                rbA, rbB, rbC, rbD,
                btnProximo
        );
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout, 400, 350);
        quizStage.setScene(scene);
        aplicarEstilo(scene);

        quizStage.show();

        iniciarTimer(tempoLabel, quizStage, acertos, pontos, questoes, usuario);
        exibirQuestao.run(); // exibe a primeira questão
    }
    
    private void iniciarTimer(Label tempoLabel, Stage quizStage, int[] acertos, int[] pontos, List<Questao> questoes, String usuario) {
        IntegerProperty segundos = new SimpleIntegerProperty(60);
        tempoLabel.textProperty().bind(Bindings.concat("Tempo restante: ", segundos, "s"));

        Timeline timeline = new Timeline(
            new KeyFrame(Duration.seconds(1), e -> {
                segundos.set(segundos.get() - 1);

                if (segundos.get() <= 0) {
                    ((Timeline) e.getSource()).stop();
                    tempoLabel.textProperty().unbind();
                    tempoLabel.setText("Tempo esgotado!");

                    // Mostra o resumo final
                    showAlert(
                        Alert.AlertType.INFORMATION,
                        "Tempo encerrado",
                        "Você acertou " + acertos[0] + " de " + questoes.size() + " questões.\n" +
                        "Pontuação total: " + pontos[0] + " pontos."
                    );

                    // Salva no ranking
                    PontuacaoDAO pdao = new PontuacaoDAO();
                    pdao.salvar(new Pontuacao(usuario, pontos[0], LocalDateTime.now()));

                    // Fecha a janela
                    quizStage.close();
                    
                    abrirPainelUsuario(); 

                    
                }
            })
        );

        timeline.setCycleCount(60);
        timeline.play();
    }

    
    private void abrirRankingAdmin() {
        Stage stage = new Stage();
        stage.setTitle("Ranking de Pontuação");

        TableView<Pontuacao> tabela = new TableView<>();
        tabela.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Pontuacao, String> colUsuario = new TableColumn<>("Usuário");
        colUsuario.setCellValueFactory(new PropertyValueFactory<>("usuario"));

        TableColumn<Pontuacao, Integer> colPontos = new TableColumn<>("Pontuação");
        colPontos.setCellValueFactory(new PropertyValueFactory<>("pontos"));

        TableColumn<Pontuacao, LocalDateTime> colData = new TableColumn<>("Data");
        colData.setCellValueFactory(new PropertyValueFactory<>("data"));

        tabela.getColumns().addAll(colUsuario, colPontos, colData);

        PontuacaoDAO dao = new PontuacaoDAO();
        List<Pontuacao> lista = dao.listarTodas();
        tabela.setItems(FXCollections.observableArrayList(lista));
        
        Button btnExcluirRanking = new Button("Excluir do Ranking");
        btnExcluirRanking.setOnAction(e -> {
            Pontuacao selecionada = tabela.getSelectionModel().getSelectedItem();
            if (selecionada != null) {
                PontuacaoDAO pdao = new PontuacaoDAO();
                
                // Exclui diretamente a pontuação selecionada
                pdao.excluir(selecionada); 
                
                tabela.getItems().remove(selecionada);
                showAlert(Alert.AlertType.INFORMATION, "Sucesso", "Pontuação removida com sucesso.");
            } else {
                showAlert(Alert.AlertType.WARNING, "Aviso", "Selecione uma pontuação para excluir.");
            }
        });


        Button btnVoltar = new Button("Voltar");
        btnVoltar.setOnAction(e -> {
            Stage stageAtual = (Stage) ((Node) e.getSource()).getScene().getWindow();
            stageAtual.close(); // Fecha a tela de ranking
            abrirPainelAdmin(); // Volta para o painel admin
        });


        HBox botoes = new HBox(10, btnExcluirRanking, btnVoltar);
        botoes.setAlignment(Pos.CENTER);

        VBox layout = new VBox(10, tabela, botoes);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout, 500, 400);
        aplicarEstilo(scene);

        stage.setScene(scene);
        stage.show();
    }
    
    private void abrirRankingUser() {
        Stage stage = new Stage();
        stage.setTitle("Ranking de Pontuação");

        TableView<Pontuacao> tabela = new TableView<>();
        tabela.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Pontuacao, String> colUsuario = new TableColumn<>("Usuário");
        colUsuario.setCellValueFactory(new PropertyValueFactory<>("usuario"));

        TableColumn<Pontuacao, Integer> colPontos = new TableColumn<>("Pontuação");
        colPontos.setCellValueFactory(new PropertyValueFactory<>("pontos"));

        TableColumn<Pontuacao, LocalDateTime> colData = new TableColumn<>("Data");
        colData.setCellValueFactory(new PropertyValueFactory<>("data"));

        tabela.getColumns().addAll(colUsuario, colPontos, colData);

        PontuacaoDAO dao = new PontuacaoDAO();
        List<Pontuacao> lista = dao.listarTodas();
        tabela.setItems(FXCollections.observableArrayList(lista));
        
  
        Button btnVoltar = new Button("Voltar");
        btnVoltar.setOnAction(e -> {
            Stage stageAtual = (Stage) ((Node) e.getSource()).getScene().getWindow();
            stageAtual.close(); // Fecha a tela de ranking
            abrirPainelAdmin(); // Volta para o painel admin
        });


        HBox botoes = new HBox(10, btnVoltar);
        botoes.setAlignment(Pos.CENTER);

        VBox layout = new VBox(10, tabela, botoes);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout, 500, 400);
        aplicarEstilo(scene);

        stage.setScene(scene);
        stage.show();
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
        Button btnGerenciarUsuarios = new Button("Gerenciar Usuários");

        Button btnVerRanking = new Button("Ver Ranking");
        Button btnLogout = new Button("Logout");
        Button btnSair = new Button("Sair");

        // Define uma largura padrão para todos os botões
        btnCadastrarQuestao.setPrefWidth(200);
        btnVisualizarQuestoes.setPrefWidth(200);
        btnGerenciarUsuarios.setPrefWidth(200);
        btnVerRanking.setPrefWidth(200);
        btnLogout.setPrefWidth(200);
        btnSair.setPrefWidth(200);

        // Ações dos botões (no momento apenas mensagens no terminal)
        
        // Ação do botão "Cadastrar Nova Questão"
        btnCadastrarQuestao.setOnAction(e -> {
            // Fecha o panel atual (admin)
            Stage stageAtual = (Stage) ((Node) e.getSource()).getScene().getWindow();
            stageAtual.close();

            // Abre o panel de cadastro
            abrirCadastroQuestoes();
            
            
        });

        // Ação do botão "Visualizar Questões"
        btnVisualizarQuestoes.setOnAction(e -> {
            System.out.println("Visualizar Questões clicado.");
            
            Stage stageAtual = (Stage) ((Node) e.getSource()).getScene().getWindow();
            stageAtual.close();
            abrirVisualizarQuestoes(); // ← Chamada futura para a tela de visualização
        });
        
      btnGerenciarUsuarios.setOnAction(e -> {
    	  Stage stageAtual = (Stage) ((Node) e.getSource()).getScene().getWindow();
    	  stageAtual.close();
    	  abrirGerenciarUsuarios();
      });

        // Ação do botão "Ver Ranking"
        btnVerRanking.setOnAction(e -> {
            // Fecha o painel atual (admin)
            Stage stageAtual = (Stage) ((Node) e.getSource()).getScene().getWindow();
            stageAtual.close();

            // Abre a tela de ranking
            abrirRankingAdmin();
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
            btnGerenciarUsuarios,
            btnVerRanking,
            btnLogout,
            btnSair
        );

        // Cria e exibe a cena da janela admin
        Scene scene = new Scene(painel, 300, 300);
        aplicarEstilo(scene);

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
        
     // Ação ao clicar no botão "Iniciar Quiz"
        btnIniciarQuiz.setOnAction(e -> {
            String dificuldade = comboBox.getValue();
            System.out.println("Iniciando quiz na dificuldade: " + dificuldade);
            iniciarQuiz(dificuldade, usuarioLogado); 
            userStage.close(); // Fecha a janela atual se quiser
        });
        
        // Ação do botão "Ver Ranking"
        Button btnVerRanking = new Button("Ver Ranking");
        btnVerRanking.setOnAction(e -> {
            // Fecha o painel atual (admin)
            Stage stageAtual = (Stage) ((Node) e.getSource()).getScene().getWindow();
            stageAtual.close();

            // Abre a tela de ranking
            abrirRankingUser();
        });


        // Botão "Voltar"
        Button btnVoltar = new Button("Voltar");
        // Ação ao clicar em "Voltar" → fecha tela e reabre login
        btnVoltar.setOnAction(e -> {
        	userStage.close();        // Fecha esta janela
        	start(new Stage());       // Reabre a tela de login (reusando o método start)
        });
        
        // Quando o usuário escolher uma dificuldade, habilita o botão
        comboBox.setOnAction(e -> {
            String selecionado = comboBox.getValue();
            if (selecionado != null && !selecionado.isEmpty()) {
                btnIniciarQuiz.setDisable(false);
            }
        });
       
        

        // Layout da tela
        VBox painel = new VBox(15);
        painel.setPadding(new Insets(20));
        painel.setAlignment(Pos.CENTER);
        painel.getChildren().addAll(comboBox, btnIniciarQuiz,btnVerRanking, btnVoltar);

        // Exibe a janela
        Scene scene = new Scene(painel, 300, 200);
        aplicarEstilo(scene);

        userStage.setScene(scene);
        userStage.show();

        System.out.println("Painel Usuário aberto.");
    }

   
    private void abrirCadastroQuestoes() {
        Stage cadastroStage = new Stage();
        cadastroStage.setTitle("Cadastrar Nova Questão");

        // Campo de enunciado (TextArea para texto longo)
        Label lblEnunciado = new Label("Enunciado da questão:");
        TextArea txtEnunciado = new TextArea();
        txtEnunciado.setPrefRowCount(4);
        txtEnunciado.setWrapText(true);

        // Campos de alternativas
        TextField txtA = new TextField();
        txtA.setPromptText("Alternativa A");

        TextField txtB = new TextField();
        txtB.setPromptText("Alternativa B");

        TextField txtC = new TextField();
        txtC.setPromptText("Alternativa C");

        TextField txtD = new TextField();
        txtD.setPromptText("Alternativa D");

        // ComboBox para selecionar a alternativa correta
        ComboBox<String> comboCorreta = new ComboBox<>();
        comboCorreta.getItems().addAll("A", "B", "C", "D");
        comboCorreta.setPromptText("Alternativa correta");

        // ComboBox para selecionar a dificuldade
        ComboBox<String> comboDificuldade = new ComboBox<>();
        comboDificuldade.getItems().addAll("Fácil", "Médio", "Difícil");
        comboDificuldade.setPromptText("Dificuldade");

        // Botões
        Button btnSalvar = new Button("Salvar");
        Button btnVoltar = new Button("Voltar");

        // Ação do botão voltar
        btnVoltar.setOnAction(e -> {
        	cadastroStage.close();
        	
        	abrirPainelAdmin();
        });

        // Ação do botão salvar
        btnSalvar.setOnAction(e -> {
            String enunciado = txtEnunciado.getText().trim();
            String a = txtA.getText().trim();
            String b = txtB.getText().trim();
            String c = txtC.getText().trim();
            String d = txtD.getText().trim();
            String correta = comboCorreta.getValue();
            String dificuldade = comboDificuldade.getValue();

            if (enunciado.isEmpty() || a.isEmpty() || b.isEmpty() || c.isEmpty() || d.isEmpty()
                    || correta == null || dificuldade == null) {
                showAlert(Alert.AlertType.WARNING, "Campos obrigatórios", "Preencha todos os campos.");
                return;
            }

            
            // Cria o objeto Questao com os dados preenchidos
            Questao novaQuestao = new Questao(enunciado, a, b, c, d, correta, dificuldade);

            // Salva no banco usando o DAO
            QuestaoDAO dao = new QuestaoDAO();
            dao.salvar(novaQuestao);

            // Mensagem de sucesso
            showAlert(Alert.AlertType.INFORMATION, "Sucesso", "Questão salva com sucesso!");

	         // Fecha a tela de cadastro
	         cadastroStage.close();
	
	         // Retorna para o painel admin
	         abrirPainelAdmin();


        });

        // Layout
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER_LEFT);
        layout.getChildren().addAll(
                lblEnunciado, txtEnunciado,
                txtA, txtB, txtC, txtD,
                comboCorreta, comboDificuldade,
                btnSalvar, btnVoltar
        );

        Scene scene = new Scene(layout, 400, 500);
        aplicarEstilo(scene);

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

    
    private void abrirVisualizarQuestoes() {
    	Stage stage = new Stage();
        stage.setTitle("Questões Cadastradas");

        // Tabela
        TableView<Questao> tabela = new TableView<>();
        tabela.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Coluna: Enunciado
        TableColumn<Questao, String> colEnunciado = new TableColumn<>("Enunciado");
        colEnunciado.setCellValueFactory(new PropertyValueFactory<>("enunciado"));

        // Coluna: Dificuldade
        TableColumn<Questao, String> colDificuldade = new TableColumn<>("Dificuldade");
        colDificuldade.setCellValueFactory(new PropertyValueFactory<>("dificuldade"));

        tabela.getColumns().addAll(colEnunciado, colDificuldade);

        // Dados do banco
        QuestaoDAO dao = new QuestaoDAO();
        List<Questao> lista = dao.listarTodas();
        ObservableList<Questao> dados = FXCollections.observableArrayList(lista);
        tabela.setItems(dados);

        // Botões
        Button btnEditar = new Button("Editar");
        Button btnExcluir = new Button("Excluir");
        Button btnVoltar = new Button("Voltar");

        btnEditar.setOnAction(e -> {
            Questao selecionada = tabela.getSelectionModel().getSelectedItem();
            if (selecionada != null) {
                stage.close();
                abrirCadastroQuestoesEdicao(selecionada);
            } else {
                showAlert(Alert.AlertType.WARNING, "Aviso", "Selecione uma questão para editar.");
            }
        });

        btnExcluir.setOnAction(e -> {
            Questao selecionada = tabela.getSelectionModel().getSelectedItem();
            if (selecionada != null) {
                dao.excluir(selecionada);
                tabela.getItems().remove(selecionada); // remove da tabela direto
            } else {
                showAlert(Alert.AlertType.WARNING, "Aviso", "Selecione uma questão para excluir.");
            }
        });

        btnVoltar.setOnAction(e -> {
            Stage stageAtual = (Stage) ((Node) e.getSource()).getScene().getWindow();
            stageAtual.close(); 
            abrirPainelAdmin(); // Volta para o painel admin
        });


        HBox botoes = new HBox(10, btnEditar, btnExcluir, btnVoltar);
        botoes.setAlignment(Pos.CENTER);
        botoes.setPadding(new Insets(10));

        VBox layout = new VBox(10, tabela, botoes);
        layout.setPadding(new Insets(15));

        Scene scene = new Scene(layout, 600, 400);
        aplicarEstilo(scene);

        stage.setScene(scene);
        stage.show();
    }
    
    private void abrirCadastroQuestoesEdicao(Questao questao) {
        Stage stage = new Stage();
        stage.setTitle("Editar Questão");

        // Campos preenchidos com os dados da questão
        Label lblEnunciado = new Label("Enunciado da questão:");
        TextArea txtEnunciado = new TextArea(questao.getEnunciado());
        txtEnunciado.setPrefRowCount(4);
        txtEnunciado.setWrapText(true);

        TextField txtA = new TextField(questao.getOpcaoA());
        txtA.setPromptText("Alternativa A");

        TextField txtB = new TextField(questao.getOpcaoB());
        txtB.setPromptText("Alternativa B");

        TextField txtC = new TextField(questao.getOpcaoC());
        txtC.setPromptText("Alternativa C");

        TextField txtD = new TextField(questao.getOpcaoD());
        txtD.setPromptText("Alternativa D");

        ComboBox<String> comboCorreta = new ComboBox<>();
        comboCorreta.getItems().addAll("A", "B", "C", "D");
        comboCorreta.setValue(questao.getCorreta());

        ComboBox<String> comboDificuldade = new ComboBox<>();
        comboDificuldade.getItems().addAll("Fácil", "Médio", "Difícil");
        comboDificuldade.setValue(questao.getDificuldade());

        // Botões
        Button btnSalvar = new Button("Salvar");
        Button btnCancelar = new Button("Cancelar");

        btnCancelar.setOnAction(e -> stage.close());

        btnSalvar.setOnAction(e -> {
            // Atualiza os dados do objeto
            String enunciado = txtEnunciado.getText().trim();
            String a = txtA.getText().trim();
            String b = txtB.getText().trim();
            String c = txtC.getText().trim();
            String d = txtD.getText().trim();
            String correta = comboCorreta.getValue();
            String dificuldade = comboDificuldade.getValue();

            if (enunciado.isEmpty() || a.isEmpty() || b.isEmpty() || c.isEmpty() || d.isEmpty()
                    || correta == null || dificuldade == null) {
                showAlert(Alert.AlertType.WARNING, "Campos obrigatórios", "Preencha todos os campos.");
                return;
            }

            // Atualiza os campos da questão original
            questao.setEnunciado(enunciado);
            questao.setOpcaoA(a);
            questao.setOpcaoB(b);
            questao.setOpcaoC(c);
            questao.setOpcaoD(d);
            questao.setCorreta(correta);
            questao.setDificuldade(dificuldade);

            // Salva no banco (merge)
            QuestaoDAO dao = new QuestaoDAO();
            dao.atualizar(questao); // método que faremos no DAO

            showAlert(Alert.AlertType.INFORMATION, "Sucesso", "Questão atualizada com sucesso!");
            stage.close();
            abrirVisualizarQuestoes(); // recarrega a visualização
        });

        VBox layout = new VBox(10,
                lblEnunciado, txtEnunciado,
                txtA, txtB, txtC, txtD,
                comboCorreta, comboDificuldade,
                new HBox(10, btnSalvar, btnCancelar)
        );

        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER_LEFT);

        Scene scene = new Scene(layout, 500, 500);
        aplicarEstilo(scene);

        stage.setScene(scene);
        stage.show();
    
    }


	
    
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    private void abrirGerenciarUsuarios() {
        Stage stage = new Stage();
        stage.setTitle("Gerenciar Usuários");

        // Tabela de usuários
        TableView<QuizModel> tabela = new TableView<>();
        tabela.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<QuizModel, String> colNome = new TableColumn<>("Login");
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));

        tabela.getColumns().addAll(colNome);

        // Dados da tabela (exclui admins se quiser)
        List<QuizModel> todos = quizDAO.listarTodos();
        ObservableList<QuizModel> dados = FXCollections.observableArrayList();
        for (QuizModel u : todos) {
            if (!u.getAdmin()) { // opcional: mostrar só usuários comuns
                dados.add(u);
            }
        }
        tabela.setItems(dados);

        // Botões
        Button btnExcluir = new Button("Excluir Usuário");
        Button btnVoltar = new Button("Voltar");

        btnExcluir.setOnAction(e -> {
            QuizModel selecionado = tabela.getSelectionModel().getSelectedItem();
            if (selecionado != null) {
                quizDAO.excluir(selecionado);
                dados.remove(selecionado);
                showAlert(Alert.AlertType.INFORMATION, "Sucesso", "Usuário excluído.");
            } else {
                showAlert(Alert.AlertType.WARNING, "Aviso", "Selecione um usuário.");
            }
        });

        btnVoltar.setOnAction(e -> {
            stage.close();
            abrirPainelAdmin();
        });

        HBox botoes = new HBox(10, btnExcluir, btnVoltar);
        botoes.setAlignment(Pos.CENTER);
        VBox layout = new VBox(10, tabela, botoes);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout, 400, 300);
        aplicarEstilo(scene);
        stage.setScene(scene);
        stage.show();
    } 

    private void aplicarEstilo(Scene scene) {
    	scene.getStylesheets().add(getClass().getResource("/com/QuizApp/gui/estilo.css").toExternalForm());
    }
    
   public static void main(String[] args) {
        launch(args); // Chama o start()
    }
    

}







//tempórário
/*private void inserirQuestoesExemplo() {
        List<Questao> lista = new ArrayList<>();

        lista.add(new Questao("Qual a capital do Brasil?", "São Paulo", "Rio de Janeiro", "Brasília", "Belo Horizonte", "C", "Fácil"));
        lista.add(new Questao("Quanto é 5 x 3?", "15", "10", "20", "8", "A", "Fácil"));
        lista.add(new Questao("Qual é o maior planeta do sistema solar?", "Terra", "Saturno", "Júpiter", "Vênus", "C", "Médio"));
        lista.add(new Questao("Qual a fórmula da água?", "H2O", "CO2", "NaCl", "HCl", "A", "Fácil"));
        lista.add(new Questao("Em que continente fica o Egito?", "Ásia", "Europa", "África", "Oceania", "C", "Fácil"));
        lista.add(new Questao("Quem escreveu 'Dom Casmurro'?", "Machado de Assis", "José de Alencar", "Carlos Drummond", "Clarice Lispector", "A", "Médio"));
        lista.add(new Questao("Quantos segundos tem uma hora?", "3600", "60", "600", "1200", "A", "Fácil"));
        lista.add(new Questao("Qual desses é um número primo?", "12", "18", "17", "20", "C", "Médio"));
        lista.add(new Questao("Qual é a raiz quadrada de 144?", "10", "11", "13", "12", "D", "Médio"));
        lista.add(new Questao("Qual o símbolo químico do Ferro?", "Fe", "Ir", "F", "Fr", "A", "Difícil"));

        QuestaoDAO dao = new QuestaoDAO();
        dao.salvarEmLote(lista);

        showAlert(Alert.AlertType.INFORMATION, "Sucesso", "10 questões adicionadas ao banco!");
    }*/
