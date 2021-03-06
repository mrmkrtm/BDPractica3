package chat;

import chat.model.ChatRoom;
import chat.model.Message;
import com.mysql.cj.xdevapi.PreparableStatement;

import java.awt.print.PrinterAbortException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Controlador de la aplicación. Por favor, revise detenidamente la clase y complete las partes omitidas
 * atendiendo a los comentarios indicados mediante @TODO
 */
public class Controller {

    // @TO/DO: Sustituya xxxx por los parámetros de su conexión
    private final String DB_SERVER = "localhost";
    private final int DB_PORT = 13306;
    private final String DB_NAME = "chat";
    private final String DB_USER = "root";
    private final String DB_PASS = "pass";

    private Connection conn;

    /**
     * Crea un nuevo controlador
     */
    public Controller () {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver").newInstance();

            String url = "jdbc:mysql://" + DB_SERVER + ":" + DB_PORT + "/" + DB_NAME;
            this.conn = DriverManager.getConnection(url, DB_USER, DB_PASS);

        } catch (Exception e) {
            System.err.println("Ha ocurrido un error al conectar con la base de datos");
            e.printStackTrace();
        }
    }

    /**
     * Crea un nuevo usuario en la aplicación
     * @param username nombre de usuario
     * @return id del nuevo usuario creado
     * @throws SQLException
     */
    public long createUser(String username) throws SQLException {

        // @TO/DO: complete este método para que cree un nuevo usuario en la base de datos
        //
        // Al haberse definido el atributo 'id' de la tabla 'users' como AUTO_INCREMENT, este será generado
        // automáticamente por la base de datos. Para recuperar el 'id' generado para un usuario deberá:
        //
        //  (1) Instanciar un nuevo PreparedStatement dando como valor al segundo parámetro Statement.RETURN_GENERATED_KEYS
        //  (2) Recuperar el id generado mediante una llamada al método privado this.getAutogeneratedId(stmt)

        PreparedStatement stmt = conn.prepareStatement("INSERT INTO users(username) VALUES(?)", Statement.RETURN_GENERATED_KEYS);

        stmt.setString(1, username);

        stmt.executeUpdate();

        long id = this.getAutogeneratedId(stmt);

        stmt.close();

        return id;
    }

    /**
     * Crea una nueva sala de chat
     * @param userId id del usuario que crea la sala de chat
     * @param chatRoomName nombre de la sala de chat
     * @return id de la nueva sala de chat creada
     * @throws SQLException
     */
    public long createChatRoom (long userId, String chatRoomName) throws SQLException {

        // @TO/DO: complete este método para que cree una nueva sala de chat en la base de datos
        //
        // Al haberse definido el atributo 'id' de la tabla 'chatrooms' como AUTO_INCREMENT, este será generado
        // automáticamente por la base de datos. Para recuperar el 'id' generado para una sala de chat deberá:
        //
        //  (1) Instanciar un nuevo PreparedStatement dando como valor al segundo parámetro Statement.RETURN_GENERATED_KEYS
        //  (2) Recuperar el id generado mediante una llamada al método privado this.getAutogeneratedId(stmt)

        PreparedStatement stmt = conn.prepareStatement("INSERT INTO chatrooms(name, createdBy) VALUES(?, ?)", Statement.RETURN_GENERATED_KEYS);

        stmt.setString(1, chatRoomName);
        stmt.setLong(2, userId);

        stmt.executeUpdate();

        long id = this.getAutogeneratedId(stmt);

        stmt.close();

        return id;

    }

    /**
     * Crea un nuevo mensaje en una sala de chat
     * @param userId id del usuario que crea el mensaje
     * @param chatRoomId id de la sala de chat en la que se crea el mensaje
     * @param text contenido del mensaje
     * @throws SQLException
     */
    public void sendMessage (long userId, long chatRoomId, String text) throws SQLException {

        // @TO/DO: complete este método para que cree un nuevo mensaje en la base de datos
        //
        // Tenga en cuenta que las columnas 'id' y 'ts' generan el valor de sus atributos de forma automática. No
        // es necesario definir ningún valor para las mismas.
        //
        // El 'id' del mensaje no es necesario recuperarlo.
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO messages(text, chatroom, createdBy) VALUES(?, ?, ?)");

        stmt.setString(1, text);
        stmt.setLong(2, chatRoomId);
        stmt.setLong(3, userId);

        stmt.executeUpdate();

        stmt.close();
    }

    /**
     * Recupera los mensajes de una sala de chat
     * @param chatRoomId id de la sala de chat
     * @return Lista de mensajes
     * @throws SQLException
     */
    public List<Message> getMessages (long chatRoomId) throws SQLException {

        // @TO/DO: complete este método para que consulte los mensajes de una sala de chat y los devuelva como una lista de objetos Message
        //
        // Tenga en cuenta que la consulta a la base de datos le devolverá un ResultSet que deberá transformar
        // en una lista de instancias de objetos Message. Consulte la clase chat.model.Message para ver como crear
        // instancias de la misma

        ArrayList<Message> messages = new ArrayList<Message>();

        PreparedStatement stmt = conn.prepareStatement("SELECT text, createdBy FROM messages WHERE chatroom = ?");

        stmt.setLong(1, chatRoomId);


        ResultSet rs = stmt.executeQuery();

        while( rs.next() ){
            String text = rs.getString("text");
            String createdBy = rs.getString("createdBy");

            messages.add( new Message(text, createdBy) );
        }


        stmt.close();

        return messages;
    }

    /**
     * Recupera un listado con todas las salas de chat
     * @return listado con las salas de chat
     * @throws SQLException
     */
    public List<ChatRoom> getChatRooms () throws SQLException {

        // @TO/DO: complete este método para que consulte todas las salas de chat y las devuelva como una lista de objetos ChatRoom
        //
        // Tenga en cuenta que la consulta a la base de datos le devolverá un ResultSet que deberá transformar
        // en una lista de instancias de objetos ChatRoom. Consulte la clase chat.model.ChatRoom para ver como crear
        // instancias de la misma

        ArrayList<ChatRoom> chatRooms = new ArrayList<ChatRoom>();

        PreparedStatement stmt = conn.prepareStatement("SELECT id, name FROM chatrooms");


        ResultSet rs = stmt.executeQuery();

        while( rs.next() ){
            long id = rs.getLong("id");
            String name = rs.getString("name");

            chatRooms.add( new ChatRoom(id, name) );
        }

        stmt.close();

        return chatRooms;
    }

    /**
     * Devuelve el id generado por un Statement
     * @param stmt el Statement
     * @return el id
     * @throws SQLException
     */
    private long getAutogeneratedId (Statement stmt) throws SQLException {
        ResultSet keys = stmt.getGeneratedKeys();
        keys.next();
        return keys.getLong(1);
    }
}
