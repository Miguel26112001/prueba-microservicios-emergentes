package com.example.ai.agent.application.internal.commandservices;

import com.example.ai.agent.domain.model.commands.AskAssistantCommand;
import com.example.ai.agent.domain.model.responses.AssistantResponse;
import com.example.ai.agent.domain.services.AssistantService;
import com.example.ai.agent.infrastructure.tools.UserTools;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class AssistantCommandService implements AssistantService {

  private final UserTools userTools;
  private final ChatClient chatClient;

  public AssistantCommandService(
      UserTools userTools,
      ChatClient.Builder builder) {
    this.userTools = userTools;
    this.chatClient = builder.build();
  }

  @Override
  public AssistantResponse handle(AskAssistantCommand command) {

    String answer = chatClient.prompt()
        .tools(userTools)
        .user(command.message())
        .call()
        .content();

    return new AssistantResponse(answer);
  }
}
