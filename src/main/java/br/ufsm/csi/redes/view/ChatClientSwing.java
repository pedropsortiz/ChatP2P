package br.ufsm.csi.redes.view;
import br.ufsm.csi.redes.model.Mensagem;
import br.ufsm.csi.redes.model.Usuario;
import br.ufsm.csi.redes.c_s.ClienteThread;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

import static br.ufsm.csi.redes.model.Usuario.StatusUsuario.*;

public class ChatClientSwing extends JFrame {

    private Usuario meuUsuario = new Usuario();
    private final String endBroadcast = "255.255.255.255";
    private JList listaChat;
    private DefaultListModel dfListModel;
    private JTabbedPane tabbedPane = new JTabbedPane();
    private ArrayList<Usuario> chatsAbertos = new ArrayList<>();
    private ArrayList<ClienteThread> threadConexao = new ArrayList<>();

    public ChatClientSwing() throws UnknownHostException {
        setLayout(new GridBagLayout());
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Status");

        ButtonGroup group = new ButtonGroup();
        JRadioButtonMenuItem rbMenuItem = new JRadioButtonMenuItem(DISPONIVEL.name());
        rbMenuItem.setSelected(true);
        rbMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                ChatClientSwing.this.meuUsuario.setStatus(DISPONIVEL);
            }
        });
        group.add(rbMenuItem);
        menu.add(rbMenuItem);

        rbMenuItem = new JRadioButtonMenuItem(NAO_PERTURBE.name());
        rbMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                ChatClientSwing.this.meuUsuario.setStatus(NAO_PERTURBE);
            }
        });
        group.add(rbMenuItem);
        menu.add(rbMenuItem);

        rbMenuItem = new JRadioButtonMenuItem(VOLTO_LOGO.name());
        rbMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                ChatClientSwing.this.meuUsuario.setStatus(VOLTO_LOGO);
            }
        });
        group.add(rbMenuItem);
        menu.add(rbMenuItem);

        this.setJMenuBar(menuBar);

        tabbedPane.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                if (e.getButton() == MouseEvent.BUTTON3) {
                    JPopupMenu popupMenu =  new JPopupMenu();
                    int tab = tabbedPane.getUI().tabForCoordinate(tabbedPane, e.getX(), e.getY());
                    JMenuItem item = new JMenuItem("Fechar");
                    item.addActionListener(e1 -> {
                        PainelChatPVT painel = (PainelChatPVT) tabbedPane.getComponentAt(tab);
                        for (Usuario user: chatsAbertos
                             ) {
                            if (user.equals(painel.getUsuario())){
                                Integer indexConexao = chatsAbertos.indexOf(user);
                                ClienteThread conexaoCliente = threadConexao.get(indexConexao);
                                try {
                                    conexaoCliente.stop();
                                } catch (IOException ex) {
                                    throw new RuntimeException(ex);
                                }

                                threadConexao.remove(indexConexao);
                                chatsAbertos.remove(indexConexao);
                            }
                        }
                        tabbedPane.remove(tab);
                        //TODO: Desconectar o meuUsu�rio com o Usu�rio desligado
                    });
                    popupMenu.add(item);
                    popupMenu.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });
        //
        add(new JScrollPane(criaLista()), new GridBagConstraints(0, 0, 1, 1, 0.1, 1, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
        add(tabbedPane, new GridBagConstraints(1, 0, 1, 1, 1, 1, GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
        setSize(800, 600);
        final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        final int x = (screenSize.width - this.getWidth()) / 2;
        final int y = (screenSize.height - this.getHeight()) / 2;
        this.setLocation(x, y);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("\uD83E\uDDD1\u200D\uD83D\uDCBB Chat P2P - Redes de Computadores");
        String nomeUsuario = JOptionPane.showInputDialog(this, "Digite seu nickname: ");
        this.meuUsuario = new Usuario(nomeUsuario, DISPONIVEL, InetAddress.getLocalHost(), null);
        setVisible(true);
        menuBar.add(new JMenu("\uD83E\uDD20 - " + meuUsuario.getNome()));
        menuBar.add(menu);
    }

    public JComponent criaLista() {
        dfListModel = new DefaultListModel();
        listaChat = new JList(dfListModel);
        listaChat.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                JList list = (JList) evt.getSource();
                if (evt.getClickCount() == 2) {
                    int index = list.locationToIndex(evt.getPoint());
                    Usuario user = (Usuario) list.getModel().getElementAt(index);
                    if (chatsAbertos.add(user)) {
                        //TODO: Estabelecer conex�o do meuUsuario com o usu�rio selecionado nesse ponto
                        ClienteThread conexao = new ClienteThread(user.getEndereco(), 8081);
                        threadConexao.add(conexao);
                        tabbedPane.add(user.toString(), new PainelChatPVT(user));
                    }
                }
            }
        });
        return listaChat;
    }

    public void adicionaUsuario(Usuario usuario){
        dfListModel.addElement(usuario);
    }

    public void removeUsuario(Usuario usuario){
        dfListModel.removeElement(usuario);
    }

    public DefaultListModel retornarListaUsuarios(){
        return dfListModel;
    }

    public void atualizarUsuario(Usuario usuario){
        int usuarioIndex = dfListModel.indexOf(usuario);
        dfListModel.set(usuarioIndex, usuario);
    }

    public String retornarNomeUsuario(){
        return this.meuUsuario.getNome();
    }

    public Usuario.StatusUsuario retornarStatusUsuario(){
        return this.meuUsuario.getStatus();
    }

    public class PainelChatPVT extends JPanel {

        static JTextArea areaChat;
        JTextField campoEntrada;
        Usuario usuario;

        ClienteThread conexaoUsuario;

        public static void addMensagem(String mensagem){
            areaChat.append(mensagem);
        }

        PainelChatPVT(Usuario usuario) {
            setLayout(new GridBagLayout());
            areaChat = new JTextArea();
            this.usuario = usuario;
            areaChat.setEditable(false);
            campoEntrada = new JTextField();
            campoEntrada.addActionListener(e -> {
                ((JTextField) e.getSource()).setText("");
                Mensagem mensagemUsuario = new Mensagem(e.getActionCommand(), meuUsuario, usuario);
                for (Usuario user: chatsAbertos
                ) {
                    if (user.equals(usuario)){
                        Integer indexConexao = chatsAbertos.indexOf(user);
                        ClienteThread conexao = threadConexao.get(indexConexao);
                        conexao.start();
                        conexao.setMensagem(mensagemUsuario);
                        try {
                            conexao.stop();
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                }
                // TODO: Enviar a mensagem do usu�rio remetente ao destinat�rio
            });
            add(new JScrollPane(areaChat), new GridBagConstraints(0, 0, 1, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
            add(campoEntrada, new GridBagConstraints(0, 1, 1, 1, 1, 0, GridBagConstraints.SOUTH, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
        }

        public Usuario getUsuario() {
            return usuario;
        }

        public void setUsuario(Usuario usuario) {
            this.usuario = usuario;
        }

    }

    public static void main(String[] args) throws UnknownHostException {
        new ChatClientSwing();

    }

}