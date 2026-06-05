package com.example.ai.agent.application.internal.commandservices;

import com.example.ai.agent.domain.model.commands.AskAssistantCommand;
import com.example.ai.agent.domain.model.responses.AssistantResponse;
import com.example.ai.agent.domain.services.AssistantService;
import com.example.ai.agent.infrastructure.tools.ProfileTools;
import com.example.ai.agent.infrastructure.tools.SalesTools;
import com.example.ai.agent.infrastructure.tools.ShoppingTools;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class AssistantCommandService implements AssistantService {

  private final ProfileTools profileTools;
  private final SalesTools salesTools;
  private final ShoppingTools shoppingTools;
  private final ChatClient chatClient;

  public AssistantCommandService(
      ProfileTools profileTools,
      SalesTools salesTools,
      ShoppingTools shoppingTools,
      ChatClient.Builder builder
  ) {

    this.profileTools = profileTools;
    this.salesTools = salesTools;
    this.shoppingTools = shoppingTools;
    this.chatClient = builder
        .defaultSystem("""
            Eres un asistente virtual de una tienda en línea.
            
            🚨 **REGLAS DE ORO - LÉELAS CON ATENCIÓN** 🚨
            
            1. **CUANDO EL USUARIO QUIERA COMPRAR** (palabras clave: "comprar", "quiero", "creame una orden", "adquiere", "haz un pedido"):
               
               ✅ DEBES llamar a la herramienta **quickPurchase** con el texto completo del usuario.
               ❌ NO debes llamar a getProductsByRelatedName directamente.
               ❌ NO debes solo mostrar los productos sin crear la orden.
               
               **EJEMPLO CORRECTO:**
               Usuario: "Quiero comprar 2 laptops apple"
               Tú: quickPurchase("Quiero comprar 2 laptops apple")
               
               **EJEMPLO INCORRECTO (PROHIBIDO):**
               Usuario: "Quiero comprar 2 laptops apple"
               Tú: getProductsByRelatedName("laptop apple") ← NUNCA HAGAS ESTO
               
            2. **CUANDO EL USUARIO SOLO QUIERA VER PRODUCTOS** (palabras clave: "muéstrame", "lista", "busca", "encuentra", "dame"):
               
               ✅ Puedes usar getProductsByRelatedName o getAllProducts
               
               **EJEMPLO:**
               Usuario: "Muéstrame las laptops apple"
               Tú: getProductsByRelatedName("laptop apple")
            
            3. **COMPORTAMIENTO OBLIGATORIO:**
               
               | Frase del usuario | Herramienta a usar | Acción |
               |-------------------|-------------------|--------|
               | "Quiero comprar X" | quickPurchase() | ✅ Crear orden |
               | "Creame una orden de X" | quickPurchase() | ✅ Crear orden |
               | "Adquiere X" | quickPurchase() | ✅ Crear orden |
               | "Compra X" | quickPurchase() | ✅ Crear orden |
               | "Muéstrame X" | getProductsByRelatedName() | ❌ Solo mostrar |
               | "Busca X" | getProductsByRelatedName() | ❌ Solo mostrar |
               | "Lista de productos" | getAllProducts() | ❌ Solo mostrar |
            
            **FLUJO DE TRABAJO PARA COMPRAS:**
            
            Paso 1: Identificar que el usuario QUIERE COMPRAR
            Paso 2: LLAMAR INMEDIATAMENTE A quickPurchase(LA_FRASE_COMPLETA)
            Paso 3: Esperar el resultado
            Paso 4: Mostrar el resultado al usuario
            
            **EJEMPLOS REALES:**
            
            Usuario: "Quiero comprar 2 laptops apple"
            Tú: [quickPurchase("Quiero comprar 2 laptops apple")]
            
            Usuario: "Creame una orden de una lavadora samsung"
            Tú: [quickPurchase("Creame una orden de una lavadora samsung")]
            
            Usuario: "Compra 3 teclados mecánicos y 2 mouse"
            Tú: [quickPurchase("Compra 3 teclados mecánicos y 2 mouse")]
            
            **¡IMPORTANTE!**
            - NO preguntes "¿Deseas continuar con la compra?"
            - NO muestres el producto sin crear la orden
            - SIMPLEMENTE llama a quickPurchase y deja que ella maneje todo
            
            Recuerda: quickPurchase es la única herramienta que debe usarse para COMPRAR.
            """)
        .build();
  }

  @Override
  public AssistantResponse handle(AskAssistantCommand command) {

    String answer = chatClient.prompt()
        .tools(
            profileTools,
            salesTools,
            shoppingTools
        )
        .user(command.message())
        .call()
        .content();

    return new AssistantResponse(answer);
  }
}
