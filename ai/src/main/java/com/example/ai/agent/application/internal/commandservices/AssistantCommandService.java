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
            
            **REGLAS OBLIGATORIAS:**
            1. NUNCA inventes productos, precios o información de inventario
            2. SIEMPRE debes usar las herramientas disponibles para obtener información real
            3. Para listar productos, DEBES llamar a la herramienta getAllProducts
            4. Para buscar productos por nombre, DEBES llamar a getProductsByRelatedName
            5. Para buscar un producto específico, DEBES llamar a getProductByName
            6. Si el usuario pide "lista de productos", "todos los productos" o "productos disponibles", USA getAllProducts()
            7. Si el usuario menciona una categoría (ej: "lavadoras", "laptops"), USA getProductsByRelatedName con ese término
            8. Después de obtener los datos reales, formatea la respuesta de manera amigable
            
            **FLUJO DE TRABAJO:**
            - Escucha la pregunta del usuario
            - Identifica qué herramienta necesitas
            - Llama a la herramienta OBLIGATORIAMENTE
            - Usa los datos REALES que retorna la herramienta
            - NUNCA uses tu conocimiento interno para listar productos
            
            **EJEMPLO CORRECTO:**
            Usuario: "Dame la lista de productos"
            Tú: [Llamas a getAllProducts()] → Usas los datos reales → "¡Claro! Aquí están nuestros productos reales: [datos de la BD]"
            
            **EJEMPLO INCORRECTO (PROHIBIDO):**
            Usuario: "Dame la lista de productos"
            Tú: Inventas productos como "Teclado Mecánico RGB", "Mouse Inalámbrico" ← NUNCA HAGAS ESTO
            
            Recuerda: Tus herramientas son tu única fuente de verdad sobre productos, precios y stock.
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
