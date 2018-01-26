package ml.byta.byta.Server;

import ml.byta.byta.DataBase.Object;

public interface RequestsToServer {

    // Pide los chats.
    void getChats();

    // Pide los objetos likeados
    void getLikedObject();

    // Pide los objetos cuando estás logueado.
    void getObjectsLogged();

    // Pide todos los mensajes
    void getMessages();

}
