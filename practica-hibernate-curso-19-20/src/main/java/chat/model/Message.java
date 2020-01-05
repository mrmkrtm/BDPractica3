package chat.model;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

/**
 * Modela un mensaje. Por favor, revise detenidamente la clase y complete las partes omitidas
 * atendiendo a los comentarios indicados mediante @TODO
 */
// @TODO completar las anotaciones de la clase
    @Entity
    @Table(name="messages")
public class Message {

    // @TODO completar las anotaciones del atributo id (autogenerado)
    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false, unique= true)
    private Long id;

    // @TODO completar las anotaciones del atributo text
    @Column(name = "text", nullable = false)
    private String text;

    // @TODO completar las anotaciones del atributo chatRoom
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "chatRoom", nullable = false)
    private ChatRoom chatRoom;

    // @TODO completar las anotaciones del atributo creator
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "creator", nullable = false)
    private User creator;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ts")
    private Date ts;

    public Message () {

    }

    public Message (String text, ChatRoom chatRoom, User creator) {
        this.text = text;
        this.chatRoom = chatRoom;
        this.creator = creator;
    }

    public String getText() {
        return this.text;
    }

    public User getCreator() {
        return this.creator;
    }

    public ChatRoom getChatRoom() {
        return this.chatRoom;
    }

    public String toString() {
        return "[" + this.creator.getUsername() + "]: " + text;
    }
}
