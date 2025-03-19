package risinget.commander.commands;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.http.HttpResponse;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.concurrent.CompletableFuture;

import com.mojang.brigadier.arguments.StringArgumentType;

import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import risinget.commander.config.ConfigCommander;
import risinget.commander.utils.FormatterUtils;
import risinget.commander.utils.Prefix;

public class GeminiAICommand {
  
    public GeminiAICommand(){
      ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            dispatcher.register(ClientCommandManager.literal("gemini")
                .then(ClientCommandManager.argument("prompt", StringArgumentType.greedyString())
                    .executes(context -> {
                        String prompt = StringArgumentType.getString(context, "prompt");
                        CompletableFuture<String> futureResponse = sendRequestAndGetResponse(prompt);
                        futureResponse.thenAccept(response -> {
                          System.out.println("Respuesta: " + response);
                          String outputWithText = Prefix.COMMANDER + " &7GeminiAI:&r "+response;
                          MutableText feedbackText = FormatterUtils.parseAndFormatText(outputWithText)
                              .styled(style -> style
                              .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,Text.literal("Click para copiar")))
                              .withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, response)));
                          context.getSource().sendFeedback(feedbackText);
                        }).exceptionally(ex -> {
                          System.err.println("Error: " + ex.getMessage());
                          String outputWithText = Prefix.COMMANDER + " &7Sucedió un error ): :&r "+ex.getMessage();
                          MutableText feedbackText = FormatterUtils.parseAndFormatText(outputWithText)
                              .styled(style -> style
                              .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,Text.literal("Click para copiar")))
                              .withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, ex.getMessage())));
                          context.getSource().sendFeedback(feedbackText);
                          return null;
                        });
                        return 1;
                    })
                )
            );
        });
    }

    public static CompletableFuture<String> sendRequestAndGetResponse(String prompt) {
      return CompletableFuture.supplyAsync(() -> {
        // URL de la API
        String url = "https://generativelanguage.googleapis.com/v1beta/models/"+ConfigCommander.getSelectedModel().getModelName()+":generateContent?key=" + ConfigCommander.getApiKeyGemini();
        // Cuerpo de la solicitud en formato JSON
        String jsonBody = String.format(
          """
            {
              "contents": [
                {
                  "role": "user",
                  "parts": [
                    {
                      "text": "Dame un comando para generar un cubo vacio en minecraft 5x5 de piedra, en texto plano sin nada adicional. "
                    }
                  ]
                },
                {
                  "role": "model",
                  "parts": [
                    {
                      "text": "`/fill ~-2 ~-2 ~-2 ~2 ~2 ~2 minecraft:stone hollow`\\n"
                    }
                  ]
                },
                {
                  "role": "user",
                  "parts": [
                    {
                      "text": "Dame un comando para generar un cubo vacio en minecraft 5x5 de piedra, en texto plano sin nada adicional.\\n\\ny también responderas preguntas según lo debido al usuario que pregunte. sin comillas ni nada de formatos MARKDOWN..."
                    }
                  ]
                },
                {
                  "role": "model",
                  "parts": [
                    {
                      "text": "/fill ~-2 ~-2 ~-2 ~2 ~2 ~2 minecraft:stone hollow\\n"
                    }
                  ]
                },
                {
                  "role": "user",
                  "parts": [
                    {
                      "text": "cuando se creo youtube, siempre responde muy resumido y directo, tampoco tan seco, siempre responde muy informativo sin con algunas palabras extra, por ejemplo: Youtube se creo en 2005 en fecha tal. por el creador xxxx."
                    }
                  ]
                },
                {
                  "role": "model",
                  "parts": [
                    {
                      "text": "YouTube se creó el 14 de febrero de 2005 por Chad Hurley, Steve Chen y Jawed Karim.\\n"
                    }
                  ]
                },
                {
                  "role": "user",
                  "parts": [
                    {
                      "text": "para que sirve chatgpt"
                    }
                  ]
                },
                {
                  "role": "model",
                  "parts": [
                    {
                      "text": "ChatGPT sirve para generar texto, traducir idiomas, escribir diferentes tipos de contenido creativo y responder preguntas de manera informativa.\\n"
                    }
                  ]
                },
                {
                  "role": "user",
                  "parts": [
                    {
                      "text": "Que es tunned model"
                    }
                  ]
                },
                {
                  "role": "model",
                  "parts": [
                    {
                      "text": "Un \\"tuned model\\" (modelo afinado)  es un modelo de lenguaje grande (como GPT) que ha sido entrenado adicionalmente en un conjunto de datos específico para mejorar su rendimiento en una tarea o dominio particular.  En esencia, se toma un modelo pre-entrenado y se le da un entrenamiento extra para que se especialice en algo.\\n"
                    }
                  ]
                },
                {
                  "role": "user",
                  "parts": [
                    {
                      "text": "%s"
                    }
                  ]
                }
              ],
              "generationConfig": {
                "temperature": 1,
                "topK": 40,
                "topP": 0.95,
                "maxOutputTokens": 8192,
                "responseMimeType": "text/plain"
              }
            }
            """, prompt);

        // Crear el cliente HTTP
        HttpClient client = HttpClient.newHttpClient();

        // Crear la solicitud HTTP
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .POST(BodyPublishers.ofString(jsonBody))
                .build();

        try {
            // Enviar la solicitud y obtener la respuesta
            HttpResponse<String> response = client.send(request, BodyHandlers.ofString());

            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(response.body());

            // Si hay un error, devolver el mensaje de error
            if (rootNode.has("error")) {
                return rootNode.path("error").path("message").asText();
            }
            // String modelVersion = rootNode.path("modelVersion").asText("Unknown Model Version");
            // Extraer el texto si no hay error
            return rootNode.path("candidates")
                          .path(0)
                          .path("content")
                          .path("parts")
                          .path(0)
                          .path("text")
                          .asText(); // Valor por defecto si no hay texto

        } catch (Exception e) {
            e.printStackTrace();
            return "Error al realizar la solicitud: " + e.getMessage();
        }

      });

    }
}