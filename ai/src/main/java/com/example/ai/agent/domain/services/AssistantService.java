package com.example.ai.agent.domain.services;

import com.example.ai.agent.domain.model.commands.AskAssistantCommand;
import com.example.ai.agent.domain.model.responses.AssistantResponse;

public interface AssistantService {

  AssistantResponse handle(AskAssistantCommand command);
}
