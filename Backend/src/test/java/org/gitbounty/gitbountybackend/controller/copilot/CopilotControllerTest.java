package org.gitbounty.gitbountybackend.controller.copilot;

import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class CopilotControllerTest {

    @Test
    void chat_ShouldReturnOk_WhenChatClientResponds() {
        ChatClient.Builder builder = mock(ChatClient.Builder.class);
        ChatClient chatClient = mock(ChatClient.class, RETURNS_DEEP_STUBS);

        when(builder.defaultSystem(anyString())).thenReturn(builder);
        when(builder.build()).thenReturn(chatClient);
        when(chatClient.prompt().user("Hello").call().content()).thenReturn("Hi, how can I help?");

        CopilotController controller = new CopilotController(builder);

        ResponseEntity<CopilotController.ChatResponse> response =
                controller.chat(new CopilotController.ChatRequest("Hello"));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().response()).isEqualTo("Hi, how can I help?");
    }

    @Test
    void chat_ShouldReturnInternalServerError_WhenChatClientFails() {
        ChatClient.Builder builder = mock(ChatClient.Builder.class);
        ChatClient chatClient = mock(ChatClient.class, RETURNS_DEEP_STUBS);

        when(builder.defaultSystem(anyString())).thenReturn(builder);
        when(builder.build()).thenReturn(chatClient);
        when(chatClient.prompt().user("Hello").call().content())
                .thenThrow(new RuntimeException("AI unavailable"));

        CopilotController controller = new CopilotController(builder);

        ResponseEntity<CopilotController.ChatResponse> response =
                controller.chat(new CopilotController.ChatRequest("Hello"));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().response())
                .isEqualTo("Sorry, Jemala is currently offline. Please try again later.");
    }
}