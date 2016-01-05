package org.lecture.controller;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.lecture.model.FilePatch;
import org.lecture.model.SourceContainer;
import org.lecture.repository.SourceContainerRepository;
import org.lecture.service.CompilerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.List;

/**
 * Created by rene on 14.12.15.
 */
public class UserSourceHandler extends TextWebSocketHandler {

  @Autowired
  SourceContainerRepository codesubmissionRepository;

  @Autowired
  CompilerService compilerService;

  private ObjectMapper objectMapper = new ObjectMapper();


  @Override
  public void afterConnectionEstablished(WebSocketSession session) throws Exception {
    String taskId = extractTaskId(session.getHandshakeHeaders());
    String userId = extractUserId(session.getHandshakeHeaders());
    SourceContainer container = codesubmissionRepository.findByUserIdAndTaskId(userId, taskId);
    if (container == null) {
      container = new SourceContainer(userId,taskId);
      container = codesubmissionRepository.save(container);
    }
    this.compilerService.addToCache(container);
    session.getAttributes().put("Container-Id",container.getId());
    session.sendMessage(new TextMessage(objectMapper.writeValueAsBytes(container)));
  }

  @Override
  protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
    MappingIterator<FilePatch> iterator = new ObjectMapper().reader(FilePatch.class).readValues(message.toString());
    List<FilePatch> patches = iterator.readAll();
    session.sendMessage(new TextMessage(
        objectMapper.writeValueAsBytes(compilerService.patchAndCompileUserSource((String)session.getAttributes().get("Container-Id"), patches))));
  }

  @Override
  public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
    this.compilerService.removeFromCache((String)session.getAttributes().get("Container-Id"));
  }

  private String extractUserId(HttpHeaders headers) {
    return headers.get("User-Id").get(0);
  }
  private String extractTaskId(HttpHeaders headers) {
    return headers.get("Task-Id").get(0);
  }

}
