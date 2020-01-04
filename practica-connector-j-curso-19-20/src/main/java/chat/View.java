package chat;

import chat.model.ChatRoom;
import chat.model.Message;
import com.mysql.cj.protocol.x.MessageConstants;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.text.BoxView;
import javax.swing.text.DefaultCaret;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

/**
 * Implementa la interfaz gráfica basada en línea de comandos de la aplicación. Esta clase no
 * debe ser modificada.
 */
/*
public class View {

    private Controller controller;

    private String username;
    private long userId;

    public View () {
    }

    public void show() throws SQLException {
        this.showLoginMenu();
    }

    private void showLoginMenu() throws SQLException {
        System.out.println("\nBienvenido a nuestro chat\nPor favor, ingresa tu nombre de usuario: ");

        do {
            Scanner s = new Scanner(System.in);
            this.username = s.nextLine().trim();
        } while (username.length() == 0);

        this.controller = new Controller();

        this.userId = controller.createUser(username);

        this.showMainMenu();
    }

    private void showMainMenu() throws SQLException {

        int option;

        do {
            System.out.println("\n¡Hola " + username + "!\nElija una acción:\n [1] Acceder a una sala de chat\n [2] Crear una sala de chat\n [3] Salir");

            Scanner s = new Scanner(System.in);
            String line = s.nextLine().trim();

            try {
                option = Integer.parseInt(line);
            } catch (NumberFormatException e) {
                option = 0;
            }
        } while (option < 1 || option > 3);

        switch (option) {
            case 1:  this.showAccessChatRoomMenu();
                     break;

            case 2:  this.showCreateChatRoomMenu();
                     break;

            default: System.out.println("\n¡Hasta pronto!");
        }

    }

    private void showCreateChatRoomMenu() throws SQLException {
        String name;

        do {
            System.out.println("\nEscriba el nombre de la sala de chat:");

            Scanner s = new Scanner(System.in);
            name = s.nextLine().trim();
        } while (name.length() == 0);

        controller.createChatRoom(this.userId, name);

        this.showMainMenu();
    }

    private void showAccessChatRoomMenu() throws SQLException {

        int option;

        List<ChatRoom> chatRooms = controller.getChatRooms();

        do {
            System.out.print("\nElija la sala de chat:");

            for (int i = 0; i < chatRooms.size(); i++) {
                System.out.print("\n [" + (i+1) + "] " + chatRooms.get(i).getName());
            }

            System.out.println("\n [" + (chatRooms.size()+1) + "] Volver");

            Scanner s = new Scanner(System.in);
            String line = s.nextLine().trim();

            try {
                option = Integer.parseInt(line);
            } catch (NumberFormatException e) {
                option = 0;
            }
        } while (option < 0 || option > (chatRooms.size() + 1));

        if (option == (chatRooms.size()+1)) {
            this.showMainMenu();

        } else {
            ChatRoom chatRoom = chatRooms.get(option - 1);
            long chatRoomId = chatRoom.getId();
            this.showChatRoomMenu(chatRoomId);
        }
    }

    private void showChatRoomMenu(long chatRoomId) throws SQLException {
        int option;

        do {
            System.out.println("\nElija una acción:\n [1] Leer mensajes\n [2] Enviar un mensaje\n [3] Volver");

            Scanner s = new Scanner(System.in);
            String line = s.nextLine().trim();

            try {
                option = Integer.parseInt(line);
            } catch (NumberFormatException e) {
                option = 0;
            }
        } while (option < 1 || option > 3);

        switch (option) {
            case 1:  this.showChatRoomMessages(chatRoomId);
                     break;

            case 2:  this.showSendMessageMenu(chatRoomId);
                     break;

            default: this.showMainMenu();
        }
    }

    private void showChatRoomMessages(long chatRoomId) throws SQLException {
        List<Message> messages = controller.getMessages(chatRoomId);

        System.out.println("\nMensajes de la sala de chat:");

        if (messages.size() == 0) {
            System.out.println("\nNo se han encontrado mensajes en esta sala de chat");
        } else {
            for (Message message : messages) {
                System.out.print("\n" + message.toString());
            }
        }

        System.out.println("\n\nPresione ENTER para continuar");

        Scanner s = new Scanner(System.in);
        s.nextLine();

        this.showChatRoomMenu(chatRoomId);
    }

    private void showSendMessageMenu(long chatRoomId) throws SQLException {

        String text;

        do {
            System.out.println("\nEscriba el contenido del mensaje:");

            Scanner s = new Scanner(System.in);
            text = s.nextLine().trim();
        } while (text.length() == 0);

        controller.sendMessage(this.userId, chatRoomId, text);

        this.showChatRoomMenu(chatRoomId);
    }


}
*/

public class View {

    private Controller controller;

    private volatile boolean inReady;

    private String username;
    private long userId;
    private JFrame frame;
    private JPanel inputPanel;
    private JPanel outputPanel;
    private JTextArea textInput;
    private JTextArea textOutput;
    private JScrollPane scrollInput;
    private JScrollPane scrollOutput;
    private JButton sendButton;

    public View () {
        inReady = true;

        //output
        outputPanel = new JPanel();
        outputPanel.setBorder( new TitledBorder( new EtchedBorder(), "output" ) );

        textOutput = new JTextArea(10, 35);
        textOutput.setText("");
        textOutput.setEditable(false);
        textOutput.setLineWrap(true);

        DefaultCaret caret = (DefaultCaret)textOutput.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

        scrollOutput = new JScrollPane(textOutput);
        scrollOutput.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

        outputPanel.add(scrollOutput);

        //input
        inputPanel = new JPanel();
        inputPanel.setBorder( new TitledBorder( new EtchedBorder(), "input" ) );

        textInput = new JTextArea(5, 25);
        textInput.setText("");
        textInput.setLineWrap(true);

        scrollInput = new JScrollPane(textInput);
        scrollInput.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        inputPanel.add(scrollInput);

        //send button
        sendButton = new JButton("ENVIAR");
        sendButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(!inReady){
                    inReady = true;
                }
            }
        });

        inputPanel.add(sendButton);

        //frame
        frame = new JFrame();
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                System.exit(0);
            }
        });


        JPanel container = new JPanel();
        container.setLayout( new BoxLayout(container, BoxLayout.PAGE_AXIS));
        container.add(outputPanel);
        container.add(inputPanel);

        frame.add(container);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public void show() throws SQLException {
        this.showLoginMenu();
    }

    private void showLoginMenu() throws SQLException {
        textOutput.append("\nBienvenido a nuestro chat\nPor favor, ingresa tu nombre de usuario: ");

        do {
            inReady = false;
            while(!inReady){
                try{
                    Thread.sleep(500);
                } catch( InterruptedException e){
                }
            }
            String in = textInput.getText();
            textInput.setText("");
            this.username = in.trim();
        } while (username.length() == 0);

        this.controller = new Controller();

        this.userId = controller.createUser(username);

        this.showMainMenu();
    }

    private void showMainMenu() throws SQLException {

        int option;

        do {
            textOutput.append("\n¡Hola " + username + "!\nElija una acción:\n [1] Acceder a una sala de chat\n [2] Crear una sala de chat\n [3] Salir");

            inReady = false;
            while(!inReady){
                try{
                    Thread.sleep(500);
                } catch( InterruptedException e){
                }
            }
            String in = textInput.getText();
            textInput.setText("");
            String line = in.trim();

            try {
                option = Integer.parseInt(line);
            } catch (NumberFormatException e) {
                option = 0;
            }
        } while (option < 1 || option > 3);

        switch (option) {
            case 1:  this.showAccessChatRoomMenu();
                break;

            case 2:  this.showCreateChatRoomMenu();
                break;

            default: textOutput.append("\n¡Hasta pronto!");
        }

    }

    private void showCreateChatRoomMenu() throws SQLException {
        String name;

        do {
            textOutput.append("\nEscriba el nombre de la sala de chat:");

            inReady = false;
            while(!inReady){
                try{
                    Thread.sleep(500);
                } catch( InterruptedException e){
                }
            }
            String in = textInput.getText();
            textInput.setText("");
            name = in.trim();

        } while (name.length() == 0);

        controller.createChatRoom(this.userId, name);

        this.showMainMenu();
    }

    private void showAccessChatRoomMenu() throws SQLException {

        int option;

        List<ChatRoom> chatRooms = controller.getChatRooms();

        do {
            textOutput.append("\nElija la sala de chat:");

            for (int i = 0; i < chatRooms.size(); i++) {
                textOutput.append("\n [" + (i+1) + "] " + chatRooms.get(i).getName());
            }

            textOutput.append("\n [" + (chatRooms.size()+1) + "] Volver");

            inReady = false;
            while(!inReady){
                try{
                    Thread.sleep(500);
                } catch( InterruptedException e){
                }
            }
            String in = textInput.getText();
            textInput.setText("");
            String line = in.trim();

            try {
                option = Integer.parseInt(line);
            } catch (NumberFormatException e) {
                option = 0;
            }
        } while (option < 0 || option > (chatRooms.size() + 1));

        if (option == (chatRooms.size()+1)) {
            this.showMainMenu();

        } else {
            ChatRoom chatRoom = chatRooms.get(option - 1);
            long chatRoomId = chatRoom.getId();
            this.showChatRoomMenu(chatRoomId);
        }
    }

    private void showChatRoomMenu(long chatRoomId) throws SQLException {
        int option;

        do {
            textOutput.append("\nElija una acción:\n [1] Leer mensajes\n [2] Enviar un mensaje\n [3] Volver");

            inReady = false;
            while(!inReady){
                try{
                    Thread.sleep(500);
                } catch( InterruptedException e){
                }
            }
            String in = textInput.getText();
            textInput.setText("");
            String line = in.trim();

            try {
                option = Integer.parseInt(line);
            } catch (NumberFormatException e) {
                option = 0;
            }
        } while (option < 1 || option > 3);

        switch (option) {
            case 1:  this.showChatRoomMessages(chatRoomId);
                break;

            case 2:  this.showSendMessageMenu(chatRoomId);
                break;

            default: this.showMainMenu();
        }
    }

    private void showChatRoomMessages(long chatRoomId) throws SQLException {
        List<Message> messages = controller.getMessages(chatRoomId);

        textOutput.append("\nMensajes de la sala de chat:");

        if (messages.size() == 0) {
            textOutput.append("\nNo se han encontrado mensajes en esta sala de chat");
        } else {
            for (Message message : messages) {
                textOutput.append("\n" + message.toString());
            }
        }

        textOutput.append("\n\nPresione ENVIAR para continuar");
        inReady = false;
        while(!inReady){
            try{
                Thread.sleep(500);
            } catch( InterruptedException e){
            }
        }

        this.showChatRoomMenu(chatRoomId);
    }

    private void showSendMessageMenu(long chatRoomId) throws SQLException {

        String text;

        do {
            textOutput.append("\nEscriba el contenido del mensaje:");

            inReady = false;
            while(!inReady){
                try{
                    Thread.sleep(500);
                } catch( InterruptedException e){
                }
            }
            String in = textInput.getText();
            textInput.setText("");
            text = in.trim();

        } while (text.length() == 0);

        controller.sendMessage(this.userId, chatRoomId, text);

        this.showChatRoomMenu(chatRoomId);
    }
}