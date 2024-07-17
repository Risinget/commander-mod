package risinget.commander;

import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;

import java.io.FileWriter;
import java.io.IOException;

public class HistoryChat {

    public HistoryChat() {
        // Registrar el evento para escuchar mensajes del chat
        ClientReceiveMessageEvents.CHAT.register((message, signedMessage, sender, params, receptionTimestamp) -> {
            

            System.out.println("Mensaje: " + message.getString());
            System.out.println("Firmado: " + signedMessage);
            System.out.println("Remitente: " + sender);
            System.out.println("Parametros: " + params);
            System.out.println("Timestamp: " + receptionTimestamp);
            // Guardar el mensaje en un archivo
            saveChatMessage(message.getString());

        });
    }

    public void saveChatMessage(String message) {
        try (FileWriter writer = new FileWriter("mod_chat_history.txt", true)) {
            writer.write(message + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
