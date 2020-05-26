package com.trelloiii.kurilka2.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.trelloiii.kurilka2.dto.EventType;
import com.trelloiii.kurilka2.dto.ObjectType;
import com.trelloiii.kurilka2.dto.Payload;
import com.trelloiii.kurilka2.dto.WsEventDto;
import org.apache.logging.log4j.util.TriConsumer;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.util.function.BiConsumer;

@Component
public class WsSender {
    private final SimpMessagingTemplate template;
    private final ObjectMapper mapper;

    public WsSender(SimpMessagingTemplate template, ObjectMapper mapper) {
        this.template = template;
        this.mapper = mapper;
    }

    public <T> BiConsumer<EventType,Payload<T>> getSender(ObjectType objectType, Class<?> view){
        ObjectWriter writer=mapper
                .setConfig(mapper.getSerializationConfig())
                .writerWithView(view);
        return (EventType type, Payload<T> payload)->{
            String value=null;
            try {
                value=writer.writeValueAsString(payload.getPayload());
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            template.convertAndSend("/topic/"+payload.getTopic(),new WsEventDto(objectType,type,value));
        };
    }
}
