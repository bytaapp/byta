package ml.byta.byta.Server;

public interface RequestsToServer {

    // Pide los chats.
    void getChatsAndMessages();

    // Pide los objetos en el inicio.
    void getObjects();

    // Hace la petición de login la primera vez.
    void login();

}
