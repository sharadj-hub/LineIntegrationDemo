package com.bmw.rest;

import static com.google.common.util.concurrent.Futures.getUnchecked;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.activation.FileTypeMap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.yaml.snakeyaml.Yaml;

import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.objectmapper.ModelObjectMapper;
import com.linecorp.bot.model.response.BotApiResponse;
import com.linecorp.bot.model.richmenu.RichMenu;
import com.linecorp.bot.model.richmenu.RichMenuIdResponse;
import com.linecorp.bot.model.richmenu.RichMenuListResponse;
import com.linecorp.bot.model.richmenu.RichMenuResponse; 

@SpringBootApplication 
public class LineIntegrationDemoApplication {

	//@Value("${line.bot.channelToken}")
    //private static String token ; // Your LINE token

	private static LineMessagingClient lineMessagingClient = 
                            LineMessagingClient.builder("VLegefuh+y0/9fI8zOf0GCX4INMRoya1WRIngf+50Y5mjliYohmt/p9QlZjhNlcVGEsTwMHPZ/VFXl+1WTU3ltqaT4d2JiTd9QfTihFIcCRyn7MuvBze+hpe1kpEwpDFrSEK5F+S0SZqUmFHcXzhcAdB04t89/1O/w1cDnyilFU=").build();

    public static void main(String[] args) throws IOException {

//    	System.out.println("token:"+token);
        // YAML
        String pathYamlHome = "rich-menu-home.yml";
//        String pathYamlMore = "richmenu-more.yml";
        // Rich Image Menu
        String pathImageHome = "rich-menu-home.jpg";
        //String pathImageMore = "richmenu-more.jpg";
      
        String richMenuId;

        // Create 1st Rich Menu (Home Menu)
        richMenuId = createRichMenu(pathYamlHome);
        imageUploadRichMenu(richMenuId, pathImageHome);

        // Create 2nd Rich Menu (More Menu)
       // richMenuId = createRichMenu(pathYamlMore);
       // imageUploadRichMenu(richMenuId, pathImageMore);

        listRichMenu(); // Show created Rich Menus
        SpringApplication.run(LineIntegrationDemoApplication.class, args);

    }

    private static String createRichMenu(String path) throws IOException {
        RichMenu richMenu = loadYaml(path);
        System.out.println("richMenu :: "+ richMenu);

        RichMenuIdResponse richMenuResponse = getUnchecked(
                                    lineMessagingClient.createRichMenu(richMenu));
        System.out.println("Successfully finished.");
        System.out.println("richMenuResponse :: "+ richMenuResponse);
        return richMenuResponse.getRichMenuId();
    }

    private static void imageUploadRichMenu(String richMenuId, String path) throws IOException {
        String contentType = FileTypeMap.getDefaultFileTypeMap().getContentType(path);
        System.out.println("Content-Type: "+ contentType);

        byte[] bytes = Files.readAllBytes(Paths.get(path));

        BotApiResponse botApiResponse = getUnchecked(
                                    lineMessagingClient.setRichMenuImage(
                                        richMenuId, contentType, bytes));
        System.out.println("Successfully finished.");
        System.out.println("botApiResponse :: "+ botApiResponse);
    }
  
    private static List<String> listRichMenu() {
        List<String> listMenuString = new ArrayList<>();

        RichMenuListResponse richMenuListResponse = getUnchecked(
                                    lineMessagingClient.getRichMenuList());
        List<RichMenuResponse> listMenus = richMenuListResponse.getRichMenus();
        System.out.println("You have {"+listMenus.size()+"} RichMenus");

        System.out.println("Successfully finished.");
        listMenus.forEach(richMenuResponse -> {
            listMenuString.add(richMenuResponse.getRichMenuId());
            System.out.println("richMenuResponse :: "+ richMenuResponse);
        });

        return listMenuString;
    }

    private static RichMenu loadYaml(String path) throws IOException {
        final Yaml YAML = new Yaml();
        final ObjectMapper OBJECT_MAPPER = ModelObjectMapper
                .createNewObjectMapper()
                .configure(Feature.ALLOW_UNQUOTED_FIELD_NAMES, true)
                .configure(Feature.ALLOW_COMMENTS, true)
                .configure(Feature.ALLOW_SINGLE_QUOTES, true)
                .configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true)
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true)
                .configure(SerializationFeature.INDENT_OUTPUT, true);

        Object yamlAsObject;
        try(FileInputStream is = new FileInputStream(path)) {
            yamlAsObject = YAML.load(is);
        }

        return OBJECT_MAPPER.convertValue(yamlAsObject, RichMenu.class);
    }
}
