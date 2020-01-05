package chat;

import chat.model.ChatRoom;
import chat.model.Message;
import chat.model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import javax.persistence.Query;
import java.sql.*;
import java.util.List;

/**
 * Controlador de la aplicación. Por favor, revise detenidamente la clase y complete las partes omitidas
 * atendiendo a los comentarios indicados mediante @TODO
 */
public class Controller {

    private Session session;

    /**
     * Crea un nuevo controlador
     */
    public Controller () {

        StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure()
                .build();

        SessionFactory sessionFactory = new MetadataSources(registry)
                .buildMetadata()
                .buildSessionFactory();

        this.session = sessionFactory.openSession();
    }

    /**
     * Crea un nuevo usuario en la aplicación
     * @param username nombre de usuario
     * @return el nuevo usuario creado
     * @throws SQLException
     */
    public User createUser(String username) throws SQLException {

        // @TODO complete este metodo para crear de forma presistente un usuario
        User user = new User(username);
        session.beginTransaction();
        session.saveOrUpdate(user);
        session.getTransaction().commit();
        return user;
    }

    /**
     * Crea una nueva sala de chat
     * @param user usuario que crea la sala de chat
     * @param name nombre de la sala de chat
     * @return la nueva sala de chat creada
     * @throws SQLException
     */
    public ChatRoom createChatRoom (User user, String name) throws SQLException {

        // @TODO complete este metodo para crear de forma presistente una sala de chat
        ChatRoom newchat= new ChatRoom(name, user);
        session.beginTransaction();
        session.saveOrUpdate(newchat);
        session.getTransaction().commit();
        return newchat;
    }

    /**
     * Crea un nuevo mensaje en una sala de chat
     * @param user usuario que crea el mensaje
     * @param chatRoom sala de chat en la que se crea el mensaje
     * @param text contenido del mensaje
     * @throws SQLException
     */
    public void sendMessage (User user, ChatRoom chatRoom, String text) throws SQLException {

        // @TODO complete este metodo para crear de forma presistente un mensaje

        Message message = new Message(text, chatRoom, user);
        user.getMessages().add(message);
        chatRoom.getMessages().add(message);
        session.beginTransaction();
        session.saveOrUpdate(message);
        session.saveOrUpdate(chatRoom);
        session.getTransaction().commit();
    }

    /**
     * Recupera un listado con todas las salas de chat
     * @return listado con las salas de chat
     * @throws SQLException
     */
    public List<ChatRoom> getChatRooms () throws SQLException {

        // @TODO complete este metodo para devolver todas las salas de chat
        Query query = session.createQuery("FROM ChatRoom");
        List<ChatRoom> list = query.getResultList();
        return list;
    }
}
