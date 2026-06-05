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
    this.chatClient = builder.build();
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
