package org.lecture.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.lecture.model.CompilationReport;
import org.lecture.model.FilePatch;
import org.lecture.model.SourceContainer;
import org.lecture.repository.SourceContainerRepository;
import org.lecture.service.CompilerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;
import java.util.List;

/**
 * Created by rene on 14.12.15.
 */
public class UserSourceHandler extends AbstractWebSocketHandler {

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
    ObjectMapper mapper = new ObjectMapper();
    List<FilePatch> patches = null;
    //.substring(1,message.getPayloadLength()-1).replaceAll("\\\\","")
    try {
      patches = mapper.readValue(message.getPayload(), mapper.getTypeFactory().constructCollectionType(List.class, FilePatch.class));
    } catch (Exception e ){
      e.printStackTrace();
      throw e;
    }
    /*ObjectMapper mapper = new ObjectMapper();
    TypeFactory typeFactory = mapper.getTypeFactory();
    List<FilePatch> patches =
        mapper.readValue(textString, typeFactory.constructCollectionType(List.class, FilePatch.class));*/
    System.out.println("all parts read: "+ patches);
    CompilationReport report = compilerService.patchAndCompileUserSource((String)session.getAttributes().get("Container-Id"), patches);
    System.out.println("report is: "+report);
    session.sendMessage(new TextMessage(
        objectMapper.writeValueAsBytes(report)));
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
