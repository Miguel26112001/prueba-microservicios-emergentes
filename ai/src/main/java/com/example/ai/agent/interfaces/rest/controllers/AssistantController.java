package com.example.ai.agent.interfaces.rest.controllers;

import com.example.ai.agent.domain.model.commands.AskAssistantCommand;
import com.example.ai.agent.domain.model.responses.AssistantResponse;
import com.example.ai.agent.domain.services.AssistantService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/assistant")
public class AssistantController {

  private final AssistantService assistantService;

  public AssistantController(AssistantService assistantService) {
    this.assistantService = assistantService;
  }

  @PostMapping("/chat")
  public ResponseEntity<AssistantResponse> chat(
      @RequestBody AskAssistantCommand command) {

    return ResponseEntity.ok(
        assistantService.handle(command)
    );
  }
}