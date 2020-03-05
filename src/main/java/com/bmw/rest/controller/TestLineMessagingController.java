/**
 * 
 */
package com.bmw.rest.controller;

import static java.util.Collections.singletonList;

import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.ReplyMessage;
import com.linecorp.bot.model.action.MessageAction;
import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TemplateMessage;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.message.template.ButtonsTemplate;
import com.linecorp.bot.model.response.BotApiResponse;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;

/**
 * @author QXZ0V7C
 *
 */
@LineMessageHandler
public class TestLineMessagingController {
    @Autowired
    private LineMessagingClient lineMessagingClient;
    

    @EventMapping
    public void handleTextMessageEvent(MessageEvent<TextMessageContent> event) throws Exception {
        TextMessageContent message = event.getMessage();
        handleTextContent(event.getReplyToken(), event, message);
    }

    @EventMapping
    public void handleOtherEvent(Event event) {
        System.out.println("Received message(Ignored): {}"+ event);
    }

    private void reply(@NonNull String replyToken, @NonNull Message message) {
        reply(replyToken, singletonList(message));
    }
    
    private void reply(@NonNull String replyToken, @NonNull List<Message> messages) {
        reply(replyToken, messages, false);
    }

    private void reply(@NonNull String replyToken,
                       @NonNull List<Message> messages,
                       boolean notificationDisabled) {
        try {
            BotApiResponse apiResponse = lineMessagingClient
                    .replyMessage(new ReplyMessage(replyToken, messages, notificationDisabled))
                    .get();
            System.out.println("Sent messages: {}"+ apiResponse);
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    private void replyText(@NonNull String replyToken, @NonNull String message) {
        if (replyToken.isEmpty()) {
            throw new IllegalArgumentException("replyToken must not be empty");
        }
        if (message.length() > 1000) {
            message = message.substring(0, 1000 - 2) + "……";
        }
        this.reply(replyToken, new TextMessage(message));
    }

    private void handleTextContent(String replyToken, Event event, TextMessageContent content)
            throws Exception {
        final String text = content.getText();

        System.out.println("Got text message from replyToken:"+replyToken+" text:"+ text);
        switch (text) {
            case "Test-Drive": {

                URI imageUrl = createUri("/images/series.jpg");
                ButtonsTemplate buttonsTemplate = new ButtonsTemplate(
                        imageUrl,
                        "BMW Series",
                        "Please choose series",
                        Arrays.asList(
                                new MessageAction("3-Series",
                                                   "3-Series"),
                                new MessageAction("X-Series",
                                                   "X-Series")
                        ));
                TemplateMessage templateMessage = new TemplateMessage("Button alt text", buttonsTemplate);
                this.reply(replyToken, templateMessage);
                break;
            
            }
            case "3-Series": {
                URI imageUrl1 = createUri("/images/models.jpg");
                ButtonsTemplate buttonsTemplate1 = new ButtonsTemplate(
                        imageUrl1,
                        "BMW Model",
                        "Please choose model",
                        Arrays.asList(
                                new MessageAction("325d",
                                                   "325d"),
                                new MessageAction("330d",
                                                   "330d"),
                                new MessageAction("335d",
                                        			"335d")
                        ));
                TemplateMessage templateMessage1 = new TemplateMessage("Button2 alt text", buttonsTemplate1);
                this.reply(replyToken, templateMessage1);
                break;
            
            
            }
            case "325d": {
            	System.out.println("Returns echo message {replyToken}:"+replyToken+",{text}:"+ text);
                this.replyText(
                        replyToken,
                        "You are a registed user for test-drive of "+text+". Thanks!!");
                break;
            }
            case "330d": {
            	System.out.println("Returns echo message {replyToken}:"+replyToken+",{text}:"+ text);
                this.replyText(
                        replyToken,
                        "Please register yourself with BMW. Thanks!!");
                break;
            }
            default:
                System.out.println("Returns echo message {replyToken}:"+replyToken+",{text}:"+ text);
                this.replyText(
                        replyToken,
                        text
                );
                break;
        }
    }

    private static URI createUri(String path) {
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                                          .path(path).build()
                                          .toUri();
    }

}
