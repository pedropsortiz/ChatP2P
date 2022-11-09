package br.ufsm.csi.redes.swing;
import br.ufsm.csi.redes.model.Mensagem;
import br.ufsm.csi.redes.model.Usuario;
import br.ufsm.csi.redes.thread.PeerThread;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.Set;

import static br.ufsm.csi.redes.model.Usuario.StatusUsuario.*;

/**
 *
 * User: Rafael
 * Date: 13/10/14
 * Time: 10:28
 *
 */
public class ChatClientSwing extends JFrame {

    private Usuario meuUsuario = new Usuario();
    private final String endBroadcast = "255.255.255.255";
    private JList listaChat;
    private DefaultListModel dfListModel;
    private JTabbedPane tabbedPane = new JTabbedPane();
    private Set<Usuario> chatsAbertos = new HashSet<>();

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
                        chatsAbertos.remove(painel.getUsuario());
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

    private JComponent criaLista() {
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

    class PainelChatPVT extends JPanel {

        JTextArea areaChat;
        JTextField campoEntrada;
        Usuario usuario;

        PainelChatPVT(Usuario usuario) {
            setLayout(new GridBagLayout());
            areaChat = new JTextArea();
            this.usuario = usuario;
            areaChat.setEditable(false);
            campoEntrada = new JTextField();
            campoEntrada.addActionListener(e -> {
                ((JTextField) e.getSource()).setText("");
                areaChat.append(meuUsuario.getNome() + "> " + e.getActionCommand() + "\n");
                Mensagem mensagemUsuario = new Mensagem(e.getActionCommand(), meuUsuario);
                // TODO: Enviar a mensagem do usu�rio remetente ao destinat�rio
                new Thread(new PeerThread(mensagemUsuario, usuario)).start();
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
