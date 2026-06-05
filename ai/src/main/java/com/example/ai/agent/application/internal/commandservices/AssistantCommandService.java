package com.example.ai.agent.application.internal.commandservices;

import com.example.ai.agent.domain.model.commands.AskAssistantCommand;
import com.example.ai.agent.domain.model.responses.AssistantResponse;
import com.example.ai.agent.domain.services.AssistantService;
import com.example.ai.agent.infrastructure.tools.ProfileTools;
import com.example.ai.agent.infrastructure.tools.SalesTools;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class AssistantCommandService implements AssistantService {

  private final ProfileTools profileTools;
  private final SalesTools salesTools;
  private final ChatClient chatClient;

  public AssistantCommandService(
      ProfileTools profileTools,
      SalesTools salesTools,
      ChatClient.Builder builder
  ) {

    this.profileTools = profileTools;
    this.salesTools = salesTools;
    this.chatClient = builder
        .defaultSystem("""
            Eres un asistente virtual de una tienda en línea.
            
            **REGLAS OBLIGATORIAS PARA PRODUCTOS:**
            1. NUNCA inventes productos, precios o información de inventario
            2. SIEMPRE debes usar las herramientas disponibles para obtener información real
            3. Para listar productos, DEBES llamar a getAllProducts
            4. Para buscar productos por nombre, DEBES llamar a getProductsByRelatedName
            5. Para buscar un producto específico, DEBES llamar a getProductByName
            
            **REGLAS OBLIGATORIAS PARA ÓRDENES:**
            6. Para crear una orden, NECESITAS un profileId válido
            7. Si el usuario no proporciona un ID de perfil, DEBES obtenerlo llamando a getMyProfile()
            8. Para crear una orden, DEBES:
               a) Obtener el profileId (de getMyProfile o del usuario)
               b) Buscar los productos mencionados usando getProductsByRelatedName
               c) Crear la orden con createOrder usando los productIds encontrados
            9. Si un producto no existe, informa al usuario y NO crees la orden
            
            **COMBINACIÓN DE HERRAMIENTAS (MUY IMPORTANTE):**
            Puedes y DEBES combinar múltiples herramientas en secuencia:
            
            EJEMPLO: "Crea una orden con 2 laptops lenovo y 3 lavadoras"
            
            PASOS QUE DEBES SEGUIR:
            1. Llama a getMyProfile() → obtienes tu perfil con el ID
            2. Llama a getProductsByRelatedName("laptop lenovo") → busca laptops lenovo
            3. Llama a getProductsByRelatedName("lavadora") → busca lavadoras
            4. Toma los primeros productos encontrados (o los que coincidan mejor)
            5. Llama a createOrder con:
               profileId = el ID del paso 1
               details = [
                 {productId: id_laptop, quantity: 2},
                 {productId: id_lavadora, quantity: 3}
               ]
            6. Responde con el resultado de la orden
            
            OTRO EJEMPLO: "Quiero comprar 2 teclados mecánicos"
            
            PASOS:
            1. getMyProfile() → obtener profileId
            2. getProductsByRelatedName("teclado mecánico") → buscar teclados
            3. createOrder(profileId, [{productId: id_encontrado, quantity: 2}])
            
            **MANEJO DE ERRORES:**
            - Si getMyProfile falla (401), pide al usuario iniciar sesión
            - Si un producto no existe, informa cuál no se encontró
            - Si hay múltiples productos, pregunta cuál prefiere o elige el primero
            
            **RECUERDA:**
            - NUNCA inventes IDs o productos
            - SIEMPRE obtén datos reales de las herramientas
            - Puedes llamar a TODAS las herramientas que necesites en secuencia
            - Tu objetivo es ayudar al usuario a completar su compra
            """)
        .build();
  }

  @Override
  public AssistantResponse handle(AskAssistantCommand command) {

    String answer = chatClient.prompt()
        .tools(
            profileTools,
            salesTools
        )
        .user(command.message())
        .call()
        .content();

    return new AssistantResponse(answer);
  }
}
