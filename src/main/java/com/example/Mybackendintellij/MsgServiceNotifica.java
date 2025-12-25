package com.example.Mybackendintellij;

import com.example.Mybackendintellij.model.ChatMessage;
import com.example.Mybackendintellij.repository.MessageRepository;
import com.example.Mybackendintellij.repository.UserRepoMsg;
import org.springframework.stereotype.Service;

@Service
public class MsgServiceNotifica {

    private final UserRepoMsg userRepoMsg;
    private final FcmService fcmService;
    private final MessageRepository messageRepository;


    public MsgServiceNotifica(UserRepoMsg userRepoMsg, FcmService fcmService, MessageRepository messageRepository) {
        this.userRepoMsg = userRepoMsg;
        this.fcmService = fcmService;
        this.messageRepository = messageRepository;
    }

    public void saveMessageAndNotify(ChatMessage msg){

        messageRepository.save(msg);
        String receiverToken = userRepoMsg.findFcmTokenByUserId(msg.getReceiverId());
        fcmService.sendNotification(receiverToken , "New Message" , msg.getContent());
    }
}
