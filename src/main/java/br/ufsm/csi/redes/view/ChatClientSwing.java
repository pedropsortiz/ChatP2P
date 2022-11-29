package br.ufsm.csi.redes.view;
import br.ufsm.csi.redes.model.Mensagem;
import br.ufsm.csi.redes.model.Usuario;
import br.ufsm.csi.redes.c_s.Cliente;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import static br.ufsm.csi.redes.model.Usuario.StatusUsuario.*;


public class ChatClientSwing extends JFrame {

    private Usuario meuUsuario;
    private JList listaChat;
    private DefaultListModel dfListModel;
    private JTabbedPane tabbedPane = new JTabbedPane();
    private ArrayList<Usuario> chatsAbertos = new ArrayList<>();

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
                        try {
                            painel.conexao.stop();
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                        tabbedPane.remove(tab);
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
                    try {
                        iniciaChat(user);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                }
            }
        });
        return listaChat;
    }

    private void iniciaChat(Usuario user) throws IOException {
        chatsAbertos.add(user);
        Cliente cliente = new Cliente(user.getEndereco(), 8081);
        PainelChatPVT novaTab = new PainelChatPVT(user);
        cliente.setTab(novaTab);
        cliente.start();
        novaTab.conexao = cliente;
        synchronized (tabbedPane) {
            tabbedPane.add(user.toString(), novaTab);
        }
    }

    public void iniciaChat(Socket conexao) throws IOException {
        Usuario usuario = getUsuario(conexao.getInetAddress());
        Cliente cliente = new Cliente(conexao);
        PainelChatPVT novaTab = new PainelChatPVT(usuario);
        cliente.setTab(novaTab);
        cliente.start();
        novaTab.conexao = cliente;
        synchronized (tabbedPane) {
            assert usuario != null;
            tabbedPane.add(usuario.toString(), novaTab);
        }
    }

    private Usuario getUsuario(InetAddress address) {
        DefaultListModel listaUsuarios = retornarListaUsuarios();
        for (int i = 0; i < listaUsuarios.getSize(); i++){
            Usuario user = (Usuario) listaUsuarios.get(i);
            if (user.getEndereco().equals(address)){
                return user;
            }
        }
        return null;
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

        public JTextArea areaChat;
        public JTextField campoEntrada;
        Usuario usuario;
        Cliente conexao;

        PainelChatPVT(Usuario usuario) {
            setLayout(new GridBagLayout());
            areaChat = new JTextArea();
            this.usuario = usuario;
            areaChat.setEditable(false);
            campoEntrada = new JTextField();
            campoEntrada.addActionListener(e -> {
                ((JTextField) e.getSource()).setText("");
                Mensagem mensagemUsuario = new Mensagem(e.getActionCommand(), meuUsuario, usuario);
                try {
                    areaChat.append(mensagemUsuario.mensagem());
                    this.conexao.setMensagem(mensagemUsuario);
                } catch (IOException ex) {
                    throw new RuntimeException("Erro ao enviar a mensagem: " + ex);
                }
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