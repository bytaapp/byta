package ml.byta.byta.Server;

import ml.byta.byta.DataBase.Object;

public interface RequestsToServer {

    // Pide los chats.
    void getChatsAndMessages();

    // Pide los objetos cuando estás logueado.
    void getObjectsLogged();

}
