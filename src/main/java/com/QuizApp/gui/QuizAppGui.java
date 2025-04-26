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
import java.util.ArrayList;
import java.util.Collections;


// JavaFX - Componentes
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.Node;
import javafx.application.Platform;



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
	
	
	private VBox cadastroPaneGlobal; // declarado no início da classe


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
    	
        this.primaryStage = primaryStage; // Salva a referência do palco principal (janela principal)

        // Inicializa o DAO para gerenciar os usuários
        quizDAO = new QuizAppDAO();
        // Carrega a lista de usuários do banco de dados para um ObservableList
        usuariosList = FXCollections.observableArrayList(quizDAO.listarTodos());

        // Cria um VBox principal para organizar os elementos verticalmente com espaçamento de 15
        VBox conteudo = new VBox(15);
        conteudo.setPadding(new Insets(20)); // Define margens internas de 20 px
        conteudo.setAlignment(Pos.CENTER); // Centraliza os elementos no VBox

        // Cria o StackPane principal que vai conter o VBox
        StackPane root = new StackPane(conteudo);

        // Cria o painel de login
        VBox loginPane = criarLoginPane();
        // Cria o painel de cadastro, passando o loginPane como referência (para alternar)
        VBox cadastroPane = criarCadastroPane(loginPane);

        // ------------------- Finalização -------------------
        // Adiciona os dois painéis (login e cadastro) dentro do VBox principal
        conteudo.getChildren().addAll(loginPane, cadastroPane);
        
        // inserirQuestoesExemplo(); // <<< Comentado: era para rodar uma vez para popular questões no banco

        // Cria a cena, define o tamanho da janela (300x350) e adiciona o layout raiz
        Scene scene = new Scene(root, 300, 350);

        aplicarEstilo(scene); // Aplica o estilo (CSS ou outras configurações visuais)

        primaryStage.setTitle("Login do Quiz"); // Define o título da janela
        primaryStage.setScene(scene); // Define a cena principal da janela
        primaryStage.show(); // Exibe a janela
    }

    
    // Cria o painel de login com campos de usuário, senha e botões de ação
    private VBox criarLoginPane() {
	    // Cria um VBox para agrupar o campo de login
	    VBox loginBox = new VBox(5);
	    loginBox.setAlignment(Pos.CENTER); // Centraliza os elementos dentro do loginBox
	
	    // Cria o label e o campo de texto para o login
	    Label lblLogin = new Label("Login:");
	    txtNome = new TextField(); // Campo de texto para o nome do usuário
	    txtNome.setMaxWidth(200); // Define largura máxima para o campo
	    loginBox.getChildren().addAll(lblLogin, txtNome); // Adiciona o label e o campo ao VBox loginBox
	
	    // Cria um VBox para o campo de senha
	    VBox senhaBox = new VBox(5);
	    senhaBox.setAlignment(Pos.CENTER); // Centraliza os elementos dentro do senhaBox
	
	    // Cria o label e o campo de senha
	    Label lblSenha = new Label("Senha:");
	    txtSenha = new PasswordField(); // Campo de senha (texto oculto)
	    txtSenha.setMaxWidth(200); // Define largura máxima para o campo
	    senhaBox.getChildren().addAll(lblSenha, txtSenha); // Adiciona o label e o campo ao VBox senhaBox
	
	    // Agrupa loginBox e senhaBox dentro de um VBox maior (inputPane)
	    VBox inputPane = new VBox(10, loginBox, senhaBox);
	    inputPane.setAlignment(Pos.CENTER); // Centraliza o inputPane
	
	    // Cria os botões de "Entrar" e "Cadastre-se"
	    Button btnEntrar = new Button("Entrar");
	    Button btnCadastrarSe = new Button("Cadastre-se");
	
	    // Cria um HBox para agrupar os botões lado a lado
	    HBox buttonPane = new HBox(10, btnEntrar, btnCadastrarSe);
	    buttonPane.setAlignment(Pos.CENTER); // Centraliza os botões no HBox
	    buttonPane.setMaxWidth(Double.MAX_VALUE); // Faz o HBox ocupar toda a largura possível
	
	    // Define largura mínima para os botões para ficarem do mesmo tamanho
	    btnEntrar.setPrefWidth(100);
	    btnCadastrarSe.setPrefWidth(100);
	
	    // Agrupa os campos e botões em um VBox final (loginPane)
	    VBox loginPane = new VBox(10, inputPane, buttonPane);
	    loginPane.setAlignment(Pos.CENTER); // Centraliza tudo no loginPane
	
	    // Define ação do botão "Entrar" quando clicado
	    btnEntrar.setOnAction(e -> {
	        String nome = txtNome.getText().trim(); // Obtém o texto do campo nome
	        String senha = txtSenha.getText().trim(); // Obtém o texto do campo senha
	
	        if (!nome.isEmpty() && !senha.isEmpty()) { // Verifica se os campos não estão vazios
	            QuizModel usuario = quizDAO.buscarPorNomeESenha(nome, senha); // Busca o usuário no banco
	
	            if (usuario != null) { // Se encontrou o usuário
	                usuarioLogado = usuario.getNome(); // Salva o nome do usuário logado
	                ((Stage) ((Node) e.getSource()).getScene().getWindow()).close(); // Fecha a janela atual
	
	                if (usuario.getAdmin()) { // Se for administrador
	                    showAlert(Alert.AlertType.INFORMATION, "Bem-vindo", "Login como administrador.");
	                    abrirPainelAdmin(); // Abre painel de administrador
	                } else { // Se for usuário comum
	                    showAlert(Alert.AlertType.INFORMATION, "Bem-vindo", "Login como usuário.");
	                    abrirPainelUsuario(); // Abre painel de usuário
	                }
	            } else { // Se não encontrou o usuário
	                showAlert(Alert.AlertType.ERROR, "Erro", "Usuário ou senha inválidos.");
	            }
	        } else { // Se campos estiverem vazios
	            showAlert(Alert.AlertType.WARNING, "Aviso", "Preencha todos os campos.");
	        }
	    });
	
	    // Define ação do botão "Cadastre-se" para alternar entre painéis
	    btnCadastrarSe.setOnAction(e -> alternarPanes(cadastroPaneGlobal, loginPane));
	
	    loginPane.setUserData(btnCadastrarSe); // (Opcional) associa o botão ao painel
	
	    return loginPane; // Retorna o painel de login pronto
	}

    // Cria o painel de cadastro com campos para novo usuário, senha, opção de administrador e botões de ação
	private VBox criarCadastroPane(VBox loginPane) {
	    VBox cadastroPane = new VBox(10);
	    cadastroPane.setAlignment(Pos.CENTER);
	    cadastroPane.setVisible(false); // Inicialmente invisível
	    cadastroPane.setManaged(false); // Não ocupa espaço no layout enquanto invisível
	
	    // Campo para novo login
	    Label lblNovoLogin = new Label("Novo login:");
	    TextField txtNovoLogin = new TextField();
	    txtNovoLogin.setMaxWidth(200);
	
	    // Campo para nova senha
	    Label lblNovaSenha = new Label("Nova senha:");
	    TextField txtNovaSenha = new TextField();
	    txtNovaSenha.setMaxWidth(200);
	
	    // Checkbox para definir se é administrador
	    CheckBox chkAdmin = new CheckBox("É administrador?");
	
	    // Botões para cadastrar e voltar
	    Button btnCadastrar = new Button("Cadastrar");
	    Button btnVoltar = new Button("Voltar");
	
	    // Adiciona todos os elementos ao painel de cadastro
	    cadastroPane.getChildren().addAll(lblNovoLogin, txtNovoLogin, lblNovaSenha, txtNovaSenha, chkAdmin, btnCadastrar, btnVoltar);
	
	    // Ação do botão "Cadastrar"
	    btnCadastrar.setOnAction(e -> {
	        String nome = txtNovoLogin.getText().trim();
	        String senha = txtNovaSenha.getText().trim();
	        boolean admin = chkAdmin.isSelected();
	
	        if (nome.isEmpty() || senha.isEmpty()) {
	            showAlert(Alert.AlertType.WARNING, "Erro", "Preencha todos os campos!");
	            return;
	        }
	
	        if (quizDAO.nomeExiste(nome)) {
	            showAlert(Alert.AlertType.WARNING, "Erro", "Este nome de login já está sendo usado.");
	            return;
	        }
	
	        // Se passou pelas validações, cadastra o novo usuário
	        cadastrarUsuario(nome, senha, admin);
	
	        // Limpa os campos após cadastro
	        txtNovoLogin.clear();
	        txtNovaSenha.clear();
	        chkAdmin.setSelected(false);
	
	        // Volta para o painel de login
	        alternarPanes(loginPane, cadastroPane);
	    });
	
	    // Ação do botão "Voltar" para retornar ao login
	    btnVoltar.setOnAction(e -> alternarPanes(loginPane, cadastroPane));
	
	    // Guarda o painel de cadastro para acesso futuro
	    cadastroPaneGlobal = cadastroPane;
	
	    return cadastroPane; // Retorna o painel de cadastro
	}

    
    // Alterna a visibilidade entre dois painéis, mostrando um e escondendo o outro
	private void alternarPanes(Node mostrar, Node esconder) {
	    esconder.setVisible(false); // Esconde o painel
	    esconder.setManaged(false); // Remove o painel do gerenciamento de layout
	    mostrar.setVisible(true);   // Torna o outro painel visível
	    mostrar.setManaged(true);   // Inclui o painel no gerenciamento de layout
	}

	
    // Inicia o quiz exibindo perguntas, gerenciando tempo, pontuação e controle de fluxo
	private void iniciarQuiz(String dificuldade, String usuario) {
	    Stage quizStage = new Stage(); // Cria uma nova janela (Stage) para o quiz
	    quizStage.setTitle("Quiz"); // Define o título da janela
	
	    final Timeline[] timeline = new Timeline[1]; // Array para permitir parar o timer dentro de expressões lambda
	
	    // Buscar e embaralhar as questões
	    QuestaoDAO dao = new QuestaoDAO(); // Cria uma instância do DAO de questões
	    List<Questao> questoes = dao.listarPorDificuldade(dificuldade); // Busca as questões filtradas pela dificuldade
	    Collections.shuffle(questoes); // Embaralha as questões para ordem aleatória
	
	    if (questoes.isEmpty()) { // Se não encontrar questões para a dificuldade
	        showAlert(Alert.AlertType.INFORMATION, "Aviso", "Nenhuma questão encontrada para esta dificuldade."); // Mostra alerta
	        return; // Sai do método
	    }
	
	    // Controle de pontuação e progresso
	    final int[] acertos = {0}; // Quantidade de acertos
	    final int[] pontos = {0}; // Pontuação acumulada
	    final int[] indiceAtual = {0}; // Índice da questão atual
	
	    // Timer
	    final IntegerProperty segundos = new SimpleIntegerProperty(60); // Inicializa o contador de tempo em 60 segundos
	    final Label tempoLabel = new Label(); // Cria um label para mostrar o tempo restante
	    tempoLabel.textProperty().bind(Bindings.concat("Tempo restante: ", segundos, "s")); // Atualiza o label dinamicamente
	
	    // Área da pergunta
	    TextArea txtEnunciado = new TextArea(); // Cria um campo de texto para o enunciado
	    txtEnunciado.setWrapText(true); // Permite que o texto quebre linha automaticamente
	    txtEnunciado.setEditable(false); // Impede edição do enunciado
	    txtEnunciado.setFocusTraversable(false); // Impede foco automático no campo
	    txtEnunciado.setStyle("-fx-font-size: 14px;"); // Define o tamanho da fonte
	    txtEnunciado.setMaxWidth(380); // Define largura máxima do campo
	    txtEnunciado.setPrefRowCount(4); // Define a altura (número de linhas)
	
	    // Alternativas
	    ToggleGroup grupo = new ToggleGroup(); // Agrupa os RadioButtons para permitir apenas uma seleção
	    RadioButton rbA = new RadioButton(); // Alternativa A
	    RadioButton rbB = new RadioButton(); // Alternativa B
	    RadioButton rbC = new RadioButton(); // Alternativa C
	    RadioButton rbD = new RadioButton(); // Alternativa D
	    rbA.setToggleGroup(grupo); // Adiciona A ao grupo
	    rbB.setToggleGroup(grupo); // Adiciona B ao grupo
	    rbC.setToggleGroup(grupo); // Adiciona C ao grupo
	    rbD.setToggleGroup(grupo); // Adiciona D ao grupo
	
	    Button btnProximo = new Button("Próximo"); // Botão para avançar para a próxima questão
	
	    // Exibir pergunta atual
	    Runnable exibirQuestao = () -> { // Função para exibir a questão atual
	        if (indiceAtual[0] >= questoes.size()) { // Se terminou todas as questões
	            timeline[0].stop(); // Para o timer
	            finalizarQuiz(quizStage, acertos[0], pontos[0], questoes, usuario); // Finaliza o quiz
	            return;
	        }
	
	        Questao q = questoes.get(indiceAtual[0]); // Obtém a questão atual
	        txtEnunciado.setText(q.getEnunciado()); // Exibe o enunciado
	        rbA.setText(q.getOpcaoA()); // Exibe alternativa A
	        rbB.setText(q.getOpcaoB()); // Exibe alternativa B
	        rbC.setText(q.getOpcaoC()); // Exibe alternativa C
	        rbD.setText(q.getOpcaoD()); // Exibe alternativa D
	        grupo.selectToggle(null); // Desseleciona qualquer alternativa
	    };
	
	    btnProximo.setOnAction(e -> { // Ação ao clicar em "Próximo"
	        RadioButton selecionada = (RadioButton) grupo.getSelectedToggle(); // Obtém a alternativa selecionada
	
	        if (selecionada != null) { // Se alguma alternativa foi selecionada
	            Questao q = questoes.get(indiceAtual[0]); // Obtém a questão atual
	
	            // Obtém a resposta correta
	            String correta = switch (q.getCorreta()) {
	                case "A" -> q.getOpcaoA();
	                case "B" -> q.getOpcaoB();
	                case "C" -> q.getOpcaoC();
	                case "D" -> q.getOpcaoD();
	                default -> "";
	            };
	
	            String respostaUsuario = selecionada.getText(); // Pega o texto da resposta escolhida
	            if (respostaUsuario.equals(correta)) { // Se a resposta estiver correta
	                acertos[0]++; // Incrementa acertos
	
	                // Define a pontuação com base na dificuldade
	                int valor = switch (q.getDificuldade().toLowerCase()) {
	                    case "fácil" -> 1;
	                    case "médio" -> 2;
	                    case "difícil" -> 3;
	                    default -> 1;
	                };
	                pontos[0] += valor; // Soma os pontos
	            }
	        }
	
	        indiceAtual[0]++; // Avança para a próxima questão
	        if (indiceAtual[0] >= questoes.size()) { // Se não há mais questões
	            timeline[0].stop(); // Para o timer
	            finalizarQuiz(quizStage, acertos[0], pontos[0], questoes, usuario); // Finaliza o quiz
	        } else {
	            exibirQuestao.run(); // Exibe a próxima questão
	        }
	    });
	
	    // Layout do quiz
	    VBox layout = new VBox(10, // Espaçamento de 10px entre os componentes
	        tempoLabel,
	        txtEnunciado,
	        rbA, rbB, rbC, rbD,
	        btnProximo
	    );
	
	    layout.setPadding(new Insets(20)); // Margem interna de 20px
	    layout.setAlignment(Pos.CENTER); // Centraliza todos os componentes
	
	    Scene scene = new Scene(layout, 400, 350); // Cria a cena com tamanho 400x350
	    aplicarEstilo(scene); // Aplica estilo visual (CSS)
	
	    quizStage.setScene(scene); // Define a cena no palco
	    quizStage.show(); // Exibe a janela
	
	    // Iniciar Timer
	    timeline[0] = new Timeline( // Cria uma nova timeline para o contador
	        new KeyFrame(Duration.seconds(1), e -> { // A cada segundo
	            segundos.set(segundos.get() - 1); // Decrementa 1 segundo
	            if (segundos.get() <= 0) { // Se o tempo esgotou
	                timeline[0].stop(); // Para o timer
	                tempoLabel.textProperty().unbind(); // Remove o binding
	                tempoLabel.setText("Tempo esgotado!"); // Atualiza o label
	                finalizarQuiz(quizStage, acertos[0], pontos[0], questoes, usuario); // Finaliza o quiz
	            }
	        })
	    );
	    timeline[0].setCycleCount(60); // Define 60 ciclos (60 segundos)
	    timeline[0].play(); // Inicia o timer
	
	    exibirQuestao.run(); // Exibe a primeira questão
	}


    // Finaliza o quiz, exibe o resultado, salva a pontuação e retorna ao painel do usuário
	private void finalizarQuiz(Stage quizStage, int acertos, int pontos, List<Questao> questoes, String usuario) {
	    // Executa o que está dentro no thread principal da aplicação JavaFX (necessário para atualizar a interface)
	    Platform.runLater(() -> {
	
	        // Exibe um alerta informando os acertos e a pontuação final do usuário
	        showAlert(
	            Alert.AlertType.INFORMATION,
	            "Quiz finalizado!",
	            "Você acertou " + acertos + " de " + questoes.size() + " questões.\n" +
	            "Pontuação total: " + pontos + " pontos."
	        );
	
	        // Cria uma instância do DAO de Pontuação para interagir com o banco de dados
	        PontuacaoDAO pdao = new PontuacaoDAO();
	
	        // Salva ou atualiza a pontuação do usuário no banco
	        pdao.salvarOuAtualizarPontuacao(usuario, pontos);
	
	        // Fecha a janela do quiz
	        quizStage.close();
	
	        // Abre novamente o painel principal do usuário
	        abrirPainelUsuario();
	    });
	}

    
    // Abre a janela de visualização do ranking de pontuação para o administrador
	private void abrirRankingAdmin() {
	    Stage stage = new Stage(); // Cria uma nova janela (Stage)
	    stage.setTitle("Ranking de Pontuação"); // Define o título da janela
	
	    TableView<Pontuacao> tabela = new TableView<>(); // Cria uma tabela para exibir as pontuações
	    tabela.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY); // Ajusta o tamanho das colunas automaticamente
	
	    // Cria a coluna para exibir o nome do usuário
	    TableColumn<Pontuacao, String> colUsuario = new TableColumn<>("Usuário");
	    colUsuario.setCellValueFactory(new PropertyValueFactory<>("usuario")); // Liga a coluna à propriedade 'usuario'
	
	    // Cria a coluna para exibir a pontuação
	    TableColumn<Pontuacao, Integer> colPontos = new TableColumn<>("Pontuação");
	    colPontos.setCellValueFactory(new PropertyValueFactory<>("pontos")); // Liga a coluna à propriedade 'pontos'
	
	    // Cria a coluna para exibir a data da pontuação
	    TableColumn<Pontuacao, LocalDateTime> colData = new TableColumn<>("Data");
	    colData.setCellValueFactory(new PropertyValueFactory<>("data")); // Liga a coluna à propriedade 'data'
	
	    // Adiciona as colunas à tabela
	    tabela.getColumns().addAll(colUsuario, colPontos, colData);
	
	    // Instancia o DAO de pontuação
	    PontuacaoDAO dao = new PontuacaoDAO();
	    // Busca todas as pontuações no banco de dados
	    List<Pontuacao> lista = dao.listarTodas();
	    // Converte a lista para um ObservableList e exibe na tabela
	    tabela.setItems(FXCollections.observableArrayList(lista));
	    
	    // Cria o botão para excluir uma pontuação do ranking
	    Button btnExcluirRanking = new Button("Excluir do Ranking");
	    btnExcluirRanking.setOnAction(e -> { // Define a ação do botão
	        Pontuacao selecionada = tabela.getSelectionModel().getSelectedItem(); // Pega a pontuação selecionada na tabela
	        if (selecionada != null) { // Se uma linha foi selecionada
	            PontuacaoDAO pdao = new PontuacaoDAO(); // Cria o DAO para exclusão
	
	            pdao.excluir(selecionada); // Exclui a pontuação selecionada do banco de dados
	
	            tabela.getItems().remove(selecionada); // Remove a pontuação da tabela exibida
	            showAlert(Alert.AlertType.INFORMATION, "Sucesso", "Pontuação removida com sucesso."); // Mostra confirmação
	        } else { // Se nenhuma linha foi selecionada
	            showAlert(Alert.AlertType.WARNING, "Aviso", "Selecione uma pontuação para excluir."); // Mostra alerta
	        }
	    });
	
	    // Cria o botão para voltar ao painel admin
	    Button btnVoltar = new Button("Voltar");
	    btnVoltar.setOnAction(e -> { // Define a ação do botão
	        Stage stageAtual = (Stage) ((Node) e.getSource()).getScene().getWindow(); // Obtém a janela atual
	        stageAtual.close(); // Fecha a janela do ranking
	        abrirPainelAdmin(); // Volta para o painel do administrador
	    });
	
	    // Agrupa os botões em um HBox
	    HBox botoes = new HBox(10, btnExcluirRanking, btnVoltar);
	    botoes.setAlignment(Pos.CENTER); // Centraliza os botões
	
	    // Cria o layout principal com a tabela e os botões
	    VBox layout = new VBox(10, tabela, botoes);
	    layout.setPadding(new Insets(20)); // Define margens internas
	    layout.setAlignment(Pos.CENTER); // Centraliza o conteúdo
	
	    // Cria a cena com o layout e define o tamanho da janela
	    Scene scene = new Scene(layout, 500, 400);
	    aplicarEstilo(scene); // Aplica o estilo visual
	
	    stage.setScene(scene); // Define a cena na janela
	    stage.show(); // Exibe a janela
	}

    
    // Abre a janela de visualização do ranking de pontuação para o usuário
	private void abrirRankingUser() {
	    Stage stage = new Stage(); // Cria uma nova janela (Stage)
	    stage.setTitle("Ranking de Pontuação"); // Define o título da janela
	
	    TableView<Pontuacao> tabela = new TableView<>(); // Cria uma tabela para exibir as pontuações
	    tabela.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY); // Ajusta automaticamente o tamanho das colunas
	
	    // Cria a coluna para exibir o nome do usuário
	    TableColumn<Pontuacao, String> colUsuario = new TableColumn<>("Usuário");
	    colUsuario.setCellValueFactory(new PropertyValueFactory<>("usuario")); // Liga a coluna à propriedade 'usuario'
	
	    // Cria a coluna para exibir a pontuação
	    TableColumn<Pontuacao, Integer> colPontos = new TableColumn<>("Pontuação");
	    colPontos.setCellValueFactory(new PropertyValueFactory<>("pontos")); // Liga a coluna à propriedade 'pontos'
	
	    // Cria a coluna para exibir a data da pontuação
	    TableColumn<Pontuacao, LocalDateTime> colData = new TableColumn<>("Data");
	    colData.setCellValueFactory(new PropertyValueFactory<>("data")); // Liga a coluna à propriedade 'data'
	
	    // Adiciona todas as colunas à tabela
	    tabela.getColumns().addAll(colUsuario, colPontos, colData);
	
	    // Instancia o DAO de pontuação
	    PontuacaoDAO dao = new PontuacaoDAO();
	    // Busca todas as pontuações no banco de dados
	    List<Pontuacao> lista = dao.listarTodas();
	    // Converte a lista para um ObservableList e exibe na tabela
	    tabela.setItems(FXCollections.observableArrayList(lista));
	    
	    // Cria o botão "Voltar" para retornar ao painel do usuário
	    Button btnVoltar = new Button("Voltar");
	    btnVoltar.setOnAction(e -> { // Define a ação do botão
	        Stage stageAtual = (Stage) ((Node) e.getSource()).getScene().getWindow(); // Obtém a janela atual
	        stageAtual.close(); // Fecha a janela do ranking
	        abrirPainelUsuario(); // Volta para o painel do usuário
	    });
	
	    // Agrupa o botão de voltar em um HBox
	    HBox botoes = new HBox(10, btnVoltar);
	    botoes.setAlignment(Pos.CENTER); // Centraliza o botão no HBox
	
	    // Cria o layout principal da janela, contendo a tabela e o botão
	    VBox layout = new VBox(10, tabela, botoes);
	    layout.setPadding(new Insets(20)); // Define margens internas
	    layout.setAlignment(Pos.CENTER); // Centraliza todo o conteúdo
	
	    // Cria a cena com o layout e define o tamanho da janela
	    Scene scene = new Scene(layout, 500, 400);
	    aplicarEstilo(scene); // Aplica o estilo visual
	
	    stage.setScene(scene); // Define a cena no palco
	    stage.show(); // Exibe a janela
	}

  
    // Abre o painel principal para o administrador com opções de gerenciamento
	private void abrirPainelAdmin() {
	    // Cria uma nova janela (Stage) separada
	    Stage adminStage = new Stage();
	    adminStage.setTitle("Painel do Administrador"); // Define o título da janela
	
	    // Cria o layout vertical (VBox) com espaçamento entre os botões
	    VBox painel = new VBox(15); // 15px de espaçamento entre os componentes
	    painel.setPadding(new Insets(20)); // Margem interna de 20px
	    painel.setAlignment(Pos.CENTER); // Centraliza os elementos no meio da tela
	
	    // Criação dos botões do painel admin
	    Button btnCadastrarQuestao = new Button("Cadastrar Nova Questão"); // Botão para cadastrar nova questão
	    Button btnVisualizarQuestoes = new Button("Visualizar Questões"); // Botão para visualizar questões cadastradas
	    Button btnGerenciarUsuarios = new Button("Gerenciar Usuários"); // Botão para gerenciar usuários
	    Button btnVerRanking = new Button("Ver Ranking"); // Botão para ver o ranking
	    Button btnLogout = new Button("Logout"); // Botão para fazer logout
	    Button btnSair = new Button("Sair"); // Botão para sair da aplicação
	
	    // Define uma largura padrão para todos os botões
	    btnCadastrarQuestao.setPrefWidth(200);
	    btnVisualizarQuestoes.setPrefWidth(200);
	    btnGerenciarUsuarios.setPrefWidth(200);
	    btnVerRanking.setPrefWidth(200);
	    btnLogout.setPrefWidth(200);
	    btnSair.setPrefWidth(200);
	
	    // Ações dos botões
	
	    // Ação do botão "Cadastrar Nova Questão"
	    btnCadastrarQuestao.setOnAction(e -> {
	        Stage stageAtual = (Stage) ((Node) e.getSource()).getScene().getWindow(); // Obtém a janela atual
	        stageAtual.close(); // Fecha o painel atual
	        abrirCadastroQuestoes(); // Abre o painel de cadastro de questões
	    });
	
	    // Ação do botão "Visualizar Questões"
	    btnVisualizarQuestoes.setOnAction(e -> {
	        System.out.println("Visualizar Questões clicado."); // Apenas um log (pode remover depois se quiser)
	        Stage stageAtual = (Stage) ((Node) e.getSource()).getScene().getWindow(); // Obtém a janela atual
	        stageAtual.close(); // Fecha o painel atual
	        abrirVisualizarQuestoes(); // Abre o painel de visualização de questões
	    });
	
	    // Ação do botão "Gerenciar Usuários"
	    btnGerenciarUsuarios.setOnAction(e -> {
	        Stage stageAtual = (Stage) ((Node) e.getSource()).getScene().getWindow(); // Obtém a janela atual
	        stageAtual.close(); // Fecha o painel atual
	        abrirGerenciarUsuarios(); // Abre o painel de gerenciamento de usuários
	    });
	
	    // Ação do botão "Ver Ranking"
	    btnVerRanking.setOnAction(e -> {
	        Stage stageAtual = (Stage) ((Node) e.getSource()).getScene().getWindow(); // Obtém a janela atual
	        stageAtual.close(); // Fecha o painel atual
	        abrirRankingAdmin(); // Abre o ranking de pontuações (admin)
	    });
	
	    // Ação do botão "Logout"
	    btnLogout.setOnAction(e -> {
	        adminStage.close(); // Fecha a janela do painel admin
	        start(new Stage()); // Reabre a tela de login
	    });
	
	    // Ação do botão "Sair"
	    btnSair.setOnAction(e -> {
	        System.exit(0); // Encerra completamente a aplicação
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
	
	    // Cria a cena da janela admin com o painel
	    Scene scene = new Scene(painel, 300, 300); // Define tamanho da janela 300x300
	    aplicarEstilo(scene); // Aplica o estilo visual (CSS)
	
	    adminStage.setScene(scene); // Define a cena no palco
	    adminStage.show(); // Exibe a nova janela
	}


    
    // Abre o painel principal para o usuário escolher dificuldade, ver ranking ou voltar
	private void abrirPainelUsuario() {
	    // Cria uma nova janela (Stage) separada
	    Stage userStage = new Stage();
	    userStage.setTitle("Escolha de Dificuldade"); // Define o título da janela
	
	    // ComboBox com opções de dificuldade
	    ComboBox<String> comboBox = new ComboBox<>();
	    comboBox.getItems().addAll("Fácil", "Médio", "Difícil"); // Adiciona as opções
	    comboBox.setPromptText("Selecione a dificuldade"); // Texto inicial exibido no ComboBox
	
	    // Botão para iniciar o quiz
	    Button btnIniciarQuiz = new Button("Iniciar Quiz");
	    btnIniciarQuiz.setDisable(true); // Desativado até selecionar uma opção
	
	    // Ação ao clicar no botão "Iniciar Quiz"
	    btnIniciarQuiz.setOnAction(e -> {
	        String dificuldade = comboBox.getValue(); // Obtém a dificuldade selecionada
	        System.out.println("Iniciando quiz na dificuldade: " + dificuldade); // Log no console
	        iniciarQuiz(dificuldade, usuarioLogado); // Inicia o quiz passando a dificuldade e usuário logado
	        userStage.close(); // Fecha a janela atual
	    });
	
	    // Ação do botão "Ver Ranking"
	    Button btnVerRanking = new Button("Ver Ranking");
	    btnVerRanking.setOnAction(e -> {
	        Stage stageAtual = (Stage) ((Node) e.getSource()).getScene().getWindow(); // Obtém a janela atual
	        stageAtual.close(); // Fecha a janela do painel usuário
	        abrirRankingUser(); // Abre a tela de ranking de usuários
	    });
	
	    // Botão "Voltar"
	    Button btnVoltar = new Button("Voltar");
	    // Ação ao clicar em "Voltar" → fecha tela e reabre login
	    btnVoltar.setOnAction(e -> {
	        userStage.close(); // Fecha esta janela
	        start(new Stage()); // Reabre a tela de login chamando o método start
	    });
	
	    // Quando o usuário escolher uma dificuldade, habilita o botão de iniciar quiz
	    comboBox.setOnAction(e -> {
	        String selecionado = comboBox.getValue(); // Obtém a opção selecionada
	        if (selecionado != null && !selecionado.isEmpty()) {
	            btnIniciarQuiz.setDisable(false); // Habilita o botão se uma dificuldade foi selecionada
	        }
	    });
	
	    // Layout da tela
	    VBox painel = new VBox(15); // VBox com espaçamento de 15px
	    painel.setPadding(new Insets(20)); // Margem interna de 20px
	    painel.setAlignment(Pos.CENTER); // Centraliza os elementos
	    painel.getChildren().addAll(comboBox, btnIniciarQuiz, btnVerRanking, btnVoltar); // Adiciona todos os elementos ao painel
	
	    // Exibe a janela
	    Scene scene = new Scene(painel, 300, 200); // Cria a cena com o painel e tamanho 300x200
	    aplicarEstilo(scene); // Aplica o estilo visual (CSS)
	
	    userStage.setScene(scene); // Define a cena no palco
	    userStage.show(); // Exibe a janela
	
	    System.out.println("Painel Usuário aberto."); // Log no console para debug
	}


   
    // Abre a janela para cadastro de uma nova questão
	private void abrirCadastroQuestoes() {
	    Stage cadastroStage = new Stage(); // Cria uma nova janela (Stage)
	    cadastroStage.setTitle("Cadastrar Nova Questão"); // Define o título da janela
	
	    // Campo de enunciado (TextArea para texto longo)
	    Label lblEnunciado = new Label("Enunciado da questão:"); // Label para o enunciado
	    TextArea txtEnunciado = new TextArea(); // Área de texto para digitar o enunciado
	    txtEnunciado.setPrefRowCount(4); // Define altura inicial da área de texto
	    txtEnunciado.setWrapText(true); // Permite quebra de linha automática no texto
	
	    // Campos de alternativas
	    TextField txtA = new TextField(); // Campo para alternativa A
	    txtA.setPromptText("Alternativa A"); // Texto de dica
	
	    TextField txtB = new TextField(); // Campo para alternativa B
	    txtB.setPromptText("Alternativa B"); // Texto de dica
	
	    TextField txtC = new TextField(); // Campo para alternativa C
	    txtC.setPromptText("Alternativa C"); // Texto de dica
	
	    TextField txtD = new TextField(); // Campo para alternativa D
	    txtD.setPromptText("Alternativa D"); // Texto de dica
	
	    // ComboBox para selecionar a alternativa correta
	    ComboBox<String> comboCorreta = new ComboBox<>();
	    comboCorreta.getItems().addAll("A", "B", "C", "D"); // Opções possíveis
	    comboCorreta.setPromptText("Alternativa correta"); // Texto inicial no ComboBox
	
	    // ComboBox para selecionar a dificuldade
	    ComboBox<String> comboDificuldade = new ComboBox<>();
	    comboDificuldade.getItems().addAll("Fácil", "Médio", "Difícil"); // Opções de dificuldade
	    comboDificuldade.setPromptText("Dificuldade"); // Texto inicial no ComboBox
	
	    // Botões
	    Button btnSalvar = new Button("Salvar"); // Botão para salvar a nova questão
	    Button btnVoltar = new Button("Voltar"); // Botão para voltar ao painel admin
	
	    // Ação do botão voltar
	    btnVoltar.setOnAction(e -> {
	        cadastroStage.close(); // Fecha a janela de cadastro
	        abrirPainelAdmin(); // Retorna para o painel do administrador
	    });
	
	    // Ação do botão salvar
	    btnSalvar.setOnAction(e -> {
	        String enunciado = txtEnunciado.getText().trim(); // Pega o enunciado
	        String a = txtA.getText().trim(); // Pega alternativa A
	        String b = txtB.getText().trim(); // Pega alternativa B
	        String c = txtC.getText().trim(); // Pega alternativa C
	        String d = txtD.getText().trim(); // Pega alternativa D
	        String correta = comboCorreta.getValue(); // Pega a alternativa correta
	        String dificuldade = comboDificuldade.getValue(); // Pega a dificuldade
	
	        // Validação: verifica se todos os campos foram preenchidos
	        if (enunciado.isEmpty() || a.isEmpty() || b.isEmpty() || c.isEmpty() || d.isEmpty()
	                || correta == null || dificuldade == null) {
	            showAlert(Alert.AlertType.WARNING, "Campos obrigatórios", "Preencha todos os campos."); // Alerta de erro
	            return; // Sai do método
	        }
	
	        // Cria o objeto Questao com os dados preenchidos
	        Questao novaQuestao = new Questao(enunciado, a, b, c, d, correta, dificuldade);
	
	        // Salva a nova questão no banco de dados
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
	    VBox layout = new VBox(10); // Cria o layout vertical com espaçamento de 10px
	    layout.setPadding(new Insets(20)); // Define margens internas
	    layout.setAlignment(Pos.CENTER_LEFT); // Alinha os elementos à esquerda
	    layout.getChildren().addAll( // Adiciona todos os componentes no layout
	            lblEnunciado, txtEnunciado,
	            txtA, txtB, txtC, txtD,
	            comboCorreta, comboDificuldade,
	            btnSalvar, btnVoltar
	    );
	
	    // Cria a cena e define seu tamanho
	    Scene scene = new Scene(layout, 400, 500);
	    aplicarEstilo(scene); // Aplica estilo visual (CSS)
	
	    cadastroStage.setScene(scene); // Define a cena no palco
	    cadastroStage.show(); // Exibe a janela
	}

    
    // Cadastra um novo usuário no banco de dados
	private void cadastrarUsuario(String nome, String senha, boolean admin) {
	    // Verifica se nome e senha não estão vazios
	    if (!nome.isEmpty() && !senha.isEmpty()) {
	        try {
	            // Cria um novo objeto QuizModel com os dados fornecidos
	            QuizModel novoUsuario = new QuizModel(nome, senha, admin);
	
	            // Salva o novo usuário no banco usando o DAO
	            quizDAO.salvar(novoUsuario);
	
	            // Define o tipo de usuário para a mensagem
	            String tipo = admin ? "administrador" : "usuário";
	
	            // Exibe mensagem de sucesso
	            showAlert(Alert.AlertType.INFORMATION, "Cadastro", "Cadastro realizado com sucesso como " + tipo + "!");
	
	        } catch (Exception e) {
	            // Em caso de erro durante o cadastro, exibe mensagem de erro
	            showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao cadastrar o usuário.");
	            e.printStackTrace(); // Imprime o erro no console para debug
	        }
	    } else {
	        // Se nome ou senha estiverem vazios, exibe alerta de erro
	        showAlert(Alert.AlertType.WARNING, "Erro", "Preencha todos os campos!");
	    }
	}

    
    // Abre a janela de visualização das questões cadastradas, com opções para editar ou excluir
	private void abrirVisualizarQuestoes() {
	    Stage stage = new Stage(); // Cria uma nova janela (Stage)
	    stage.setTitle("Questões Cadastradas"); // Define o título da janela
	
	    // Tabela para exibir as questões
	    TableView<Questao> tabela = new TableView<>();
	    tabela.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY); // Ajusta o tamanho automático das colunas
	
	    // Coluna: Enunciado
	    TableColumn<Questao, String> colEnunciado = new TableColumn<>("Enunciado");
	    colEnunciado.setCellValueFactory(new PropertyValueFactory<>("enunciado")); // Liga a coluna ao campo 'enunciado'
	
	    // Coluna: Dificuldade
	    TableColumn<Questao, String> colDificuldade = new TableColumn<>("Dificuldade");
	    colDificuldade.setCellValueFactory(new PropertyValueFactory<>("dificuldade")); // Liga a coluna ao campo 'dificuldade'
	
	    tabela.getColumns().addAll(colEnunciado, colDificuldade); // Adiciona as colunas à tabela
	
	    // Dados do banco de dados
	    QuestaoDAO dao = new QuestaoDAO(); // Cria uma instância do DAO de Questão
	    List<Questao> lista = dao.listarTodas(); // Busca todas as questões no banco
	    ObservableList<Questao> dados = FXCollections.observableArrayList(lista); // Converte a lista para ObservableList
	    tabela.setItems(dados); // Define os dados na tabela
	
	    // Botões de ação
	    Button btnEditar = new Button("Editar"); // Botão para editar uma questão
	    Button btnExcluir = new Button("Excluir"); // Botão para excluir uma questão
	    Button btnVoltar = new Button("Voltar"); // Botão para voltar ao painel admin
	
	    // Ação do botão Editar
	    btnEditar.setOnAction(e -> {
	        Questao selecionada = tabela.getSelectionModel().getSelectedItem(); // Obtém a questão selecionada
	        if (selecionada != null) { // Se uma questão foi selecionada
	            stage.close(); // Fecha a tela atual
	            abrirCadastroQuestoesEdicao(selecionada); // Abre a tela de edição da questão selecionada
	        } else {
	            showAlert(Alert.AlertType.WARNING, "Aviso", "Selecione uma questão para editar."); // Alerta se nada for selecionado
	        }
	    });
	
	    // Ação do botão Excluir
	    btnExcluir.setOnAction(e -> {
	        Questao selecionada = tabela.getSelectionModel().getSelectedItem(); // Obtém a questão selecionada
	        if (selecionada != null) { // Se uma questão foi selecionada
	            dao.excluir(selecionada); // Exclui do banco de dados
	            tabela.getItems().remove(selecionada); // Remove a questão da tabela na tela
	        } else {
	            showAlert(Alert.AlertType.WARNING, "Aviso", "Selecione uma questão para excluir."); // Alerta se nada for selecionado
	        }
	    });
	
	    // Ação do botão Voltar
	    btnVoltar.setOnAction(e -> {
	        Stage stageAtual = (Stage) ((Node) e.getSource()).getScene().getWindow(); // Obtém a janela atual
	        stageAtual.close(); // Fecha a tela atual
	        abrirPainelAdmin(); // Volta para o painel do administrador
	    });
	
	    // Layout para os botões
	    HBox botoes = new HBox(10, btnEditar, btnExcluir, btnVoltar); // Agrupa os botões horizontalmente com espaçamento de 10px
	    botoes.setAlignment(Pos.CENTER); // Centraliza os botões
	    botoes.setPadding(new Insets(10)); // Margem interna de 10px
	
	    // Layout principal
	    VBox layout = new VBox(10, tabela, botoes); // Agrupa tabela + botões verticalmente
	    layout.setPadding(new Insets(15)); // Define margens internas de 15px
	
	    // Cria a cena com o layout e define seu tamanho
	    Scene scene = new Scene(layout, 600, 400);
	    aplicarEstilo(scene); // Aplica o estilo visual (CSS)
	
	    stage.setScene(scene); // Define a cena na janela
	    stage.show(); // Exibe a janela
	}

    
    // Abre a janela para edição de uma questão já cadastrada
	private void abrirCadastroQuestoesEdicao(Questao questao) {
	    Stage stage = new Stage(); // Cria uma nova janela (Stage)
	    stage.setTitle("Editar Questão"); // Define o título da janela
	
	    // Campos preenchidos com os dados da questão
	    Label lblEnunciado = new Label("Enunciado da questão:"); // Label para o enunciado
	    TextArea txtEnunciado = new TextArea(questao.getEnunciado()); // Campo de texto já preenchido com o enunciado
	    txtEnunciado.setPrefRowCount(4); // Define altura inicial da área de texto
	    txtEnunciado.setWrapText(true); // Permite quebra de linha automática
	
	    // Campos para as alternativas já preenchidos
	    TextField txtA = new TextField(questao.getOpcaoA());
	    txtA.setPromptText("Alternativa A");
	
	    TextField txtB = new TextField(questao.getOpcaoB());
	    txtB.setPromptText("Alternativa B");
	
	    TextField txtC = new TextField(questao.getOpcaoC());
	    txtC.setPromptText("Alternativa C");
	
	    TextField txtD = new TextField(questao.getOpcaoD());
	    txtD.setPromptText("Alternativa D");
	
	    // ComboBox para selecionar a alternativa correta
	    ComboBox<String> comboCorreta = new ComboBox<>();
	    comboCorreta.getItems().addAll("A", "B", "C", "D"); // Opções possíveis
	    comboCorreta.setValue(questao.getCorreta()); // Define o valor atual da questão
	
	    // ComboBox para selecionar a dificuldade
	    ComboBox<String> comboDificuldade = new ComboBox<>();
	    comboDificuldade.getItems().addAll("Fácil", "Médio", "Difícil"); // Opções de dificuldade
	    comboDificuldade.setValue(questao.getDificuldade()); // Define o valor atual da questão
	
	    // Botões
	    Button btnSalvar = new Button("Salvar"); // Botão para salvar alterações
	    Button btnCancelar = new Button("Cancelar"); // Botão para cancelar edição
	
	    // Ação do botão Cancelar
	    btnCancelar.setOnAction(e -> stage.close()); // Fecha a janela sem salvar
	
	    // Ação do botão Salvar
	    btnSalvar.setOnAction(e -> {
	        // Captura os dados atualizados
	        String enunciado = txtEnunciado.getText().trim();
	        String a = txtA.getText().trim();
	        String b = txtB.getText().trim();
	        String c = txtC.getText().trim();
	        String d = txtD.getText().trim();
	        String correta = comboCorreta.getValue();
	        String dificuldade = comboDificuldade.getValue();
	
	        // Validação: campos obrigatórios
	        if (enunciado.isEmpty() || a.isEmpty() || b.isEmpty() || c.isEmpty() || d.isEmpty()
	                || correta == null || dificuldade == null) {
	            showAlert(Alert.AlertType.WARNING, "Campos obrigatórios", "Preencha todos os campos."); // Alerta se faltar preenchimento
	            return; // Sai do método sem salvar
	        }
	
	        // Atualiza o objeto Questao original
	        questao.setEnunciado(enunciado);
	        questao.setOpcaoA(a);
	        questao.setOpcaoB(b);
	        questao.setOpcaoC(c);
	        questao.setOpcaoD(d);
	        questao.setCorreta(correta);
	        questao.setDificuldade(dificuldade);
	
	        // Salva no banco de dados
	        QuestaoDAO dao = new QuestaoDAO();
	        dao.atualizar(questao); // Atualiza usando o método merge (no DAO)
	
	        // Mensagem de sucesso
	        showAlert(Alert.AlertType.INFORMATION, "Sucesso", "Questão atualizada com sucesso!");
	
	        stage.close(); // Fecha a janela de edição
	
	        abrirVisualizarQuestoes(); // Reabre a lista de questões atualizada
	    });
	
	    // Layout
	    VBox layout = new VBox(10, // VBox com espaçamento de 10px
	            lblEnunciado, txtEnunciado,
	            txtA, txtB, txtC, txtD,
	            comboCorreta, comboDificuldade,
	            new HBox(10, btnSalvar, btnCancelar) // Botões lado a lado
	    );
	
	    layout.setPadding(new Insets(20)); // Define margens internas
	    layout.setAlignment(Pos.CENTER_LEFT); // Alinha o conteúdo à esquerda
	
	    // Cria a cena
	    Scene scene = new Scene(layout, 500, 500);
	    aplicarEstilo(scene); // Aplica o estilo visual
	
	    stage.setScene(scene); // Define a cena na janela
	    stage.show(); // Exibe a janela
	}
	
	
	// Abre a janela de gerenciamento de usuários (somente usuários comuns)
	private void abrirGerenciarUsuarios() {
	    Stage stage = new Stage(); // Cria uma nova janela (Stage)
	    stage.setTitle("Gerenciar Usuários"); // Define o título da janela
	
	    // Tabela para exibir os usuários
	    TableView<QuizModel> tabela = new TableView<>();
	    tabela.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY); // Ajuste automático das colunas
	
	    // Coluna: Nome/Login do usuário
	    TableColumn<QuizModel, String> colNome = new TableColumn<>("Login");
	    colNome.setCellValueFactory(new PropertyValueFactory<>("nome")); // Liga a coluna à propriedade 'nome'
	
	    tabela.getColumns().addAll(colNome); // Adiciona a coluna à tabela
	
	    // Dados da tabela (apenas usuários comuns, exclui admins)
	    List<QuizModel> todos = quizDAO.listarTodos(); // Busca todos os usuários do banco
	    ObservableList<QuizModel> dados = FXCollections.observableArrayList(); // Lista observável para a tabela
	    for (QuizModel u : todos) {
	        if (!u.getAdmin()) { // Se não for administrador
	            dados.add(u); // Adiciona à lista
	        }
	    }
	    tabela.setItems(dados); // Define os dados na tabela
	
	    // Botões
	    Button btnExcluir = new Button("Excluir Usuário"); // Botão para excluir usuário selecionado
	    Button btnVoltar = new Button("Voltar"); // Botão para voltar ao painel admin
	
	    // Ação do botão Excluir
	    btnExcluir.setOnAction(e -> {
	        QuizModel selecionado = tabela.getSelectionModel().getSelectedItem(); // Obtém o usuário selecionado
	        if (selecionado != null) { // Se algum usuário foi selecionado
	            quizDAO.excluir(selecionado); // Exclui o usuário do banco
	            dados.remove(selecionado); // Remove da lista da tabela
	            showAlert(Alert.AlertType.INFORMATION, "Sucesso", "Usuário excluído."); // Alerta de sucesso
	        } else {
	            showAlert(Alert.AlertType.WARNING, "Aviso", "Selecione um usuário."); // Alerta se nada foi selecionado
	        }
	    });
	
	    // Ação do botão Voltar
	    btnVoltar.setOnAction(e -> {
	        stage.close(); // Fecha a janela atual
	        abrirPainelAdmin(); // Volta para o painel do administrador
	    });
	
	    // Layout para os botões
	    HBox botoes = new HBox(10, btnExcluir, btnVoltar); // Agrupa botões horizontalmente com espaçamento de 10px
	    botoes.setAlignment(Pos.CENTER); // Centraliza os botões
	
	    // Layout principal
	    VBox layout = new VBox(10, tabela, botoes); // Agrupa tabela + botões verticalmente
	    layout.setPadding(new Insets(20)); // Define margens internas
	    layout.setAlignment(Pos.CENTER); // Centraliza o conteúdo
	
	    // Cria a cena com o layout e define seu tamanho
	    Scene scene = new Scene(layout, 400, 300);
	    aplicarEstilo(scene); // Aplica o estilo visual (CSS)
	
	    stage.setScene(scene); // Define a cena na janela
	    stage.show(); // Exibe a janela
	}


    
	// Exibe uma caixa de diálogo (alerta) para o usuário
	private void showAlert(Alert.AlertType type, String title, String message) {
	    Alert alert = new Alert(type); // Cria um novo alerta do tipo especificado (INFORMATION, WARNING, ERROR, etc.)
	    alert.setTitle(title); // Define o título da janela do alerta
	    alert.setHeaderText(null); // Remove o cabeçalho (deixa só o título e a mensagem)
	    alert.setContentText(message); // Define o texto da mensagem que será exibida
	    alert.showAndWait(); // Exibe o alerta e aguarda o usuário fechar
	}

    
    private void aplicarEstilo(Scene scene) {
    	scene.getStylesheets().add(getClass().getResource("/com/QuizApp/gui/estilo.css").toExternalForm());
    }
    
   public static void main(String[] args) {
        launch(args); // Chama o start()
    }
    

}


//tempórário, apenas para inserir questões em massa no banco
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
