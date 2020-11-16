package ru.atom.chat.server;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

@Controller
@RequestMapping("chat")
public class ChatController {
    private Queue<String> messages = new ConcurrentLinkedQueue<>();
    private Map<String, String> usersOnline = new ConcurrentHashMap<>();
    private Map<String, String> banList = new ConcurrentHashMap<>();

    /**
     * curl -X POST -i localhost:8080/chat/login -d "name=I_AM_STUPID"
     */
    @RequestMapping(
            path = "login",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> login(@RequestParam("name") String name) {
        if (name.length() < 1) {
            return ResponseEntity.badRequest().body("Too short name, sorry :(");
        }
        if (name.length() > 20) {
            return ResponseEntity.badRequest().body("Too long name, sorry :(");
        }
        if (usersOnline.containsKey(name)) {
            return ResponseEntity.badRequest().body("Already logged in:(");
        }
        if (banList.containsKey(name)) {
            return ResponseEntity.badRequest().body("user [" + name + "] was banned");
        }
        usersOnline.put(name, name);
        messages.add("[" + name + "] logged in");
        return ResponseEntity.ok().build();
    }

    /**
     * curl -i localhost:8080/chat/online
     */
    @RequestMapping(
            path = "online",
            method = RequestMethod.GET,
            produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity online() {
        String responseBody = String.join("\n", usersOnline.keySet().stream().sorted().collect(Collectors.toList()));
        return ResponseEntity.ok(responseBody);
    }

    /**
     * curl -X POST -i localhost:8080/chat/logout -d "name=I_AM_STUPID"
     */
    @RequestMapping(
            path = "logout",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> logout(@RequestParam("name") String name) {
        if (!usersOnline.containsKey(name)) {
            return ResponseEntity.badRequest().body("user [" + name + "] didn't logged in");
        } else {
            usersOnline.remove(name);
            messages.add("[" + name + "] logged out");
            return ResponseEntity.ok().build();
        }
    }

    /**
     * curl -X POST -i localhost:8080/chat/say -d "name=I_AM_STUPID&msg=Hello everyone in this chat"
     */
    @RequestMapping(
            path = "say",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> say(@RequestParam("name") String name, @RequestParam("msg") String msg) {
        if (!usersOnline.containsKey(name)) {
            return ResponseEntity.badRequest().body("user [" + name + "] didn't logged in");
        }
        if (banList.containsKey(name)) {
            return ResponseEntity.badRequest().body("user [" + name + "] was banned");
        } else {
            messages.add("[" + name + "]: " + msg);
            return ResponseEntity.ok().build();
        }
    }

    /**
     * curl -i localhost:8080/chat/chat
     */
    @RequestMapping(
            path = "chat",
            method = RequestMethod.GET,
            produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity chat() {
        String responseBody = String.join("\n", messages.stream().collect(Collectors.toList()));
        return ResponseEntity.ok(responseBody);
    }

    /**
     * curl -X POST -i localhost:8080/chat/ban -d "name=I_AM_STUPID"
     */
    @RequestMapping(
            path = "ban",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> ban(@RequestParam("name") String name) {
        if (banList.containsKey(name)) {
            return ResponseEntity.badRequest().body("user [" + name + "] already banned");
        } else if (!usersOnline.containsKey(name)) {
            return ResponseEntity.badRequest().body("user [" + name + "] didn't logged in");
        } else {
            banList.put(name, name);
            usersOnline.remove(name, name);
            messages.add("[" + name + "] was banned");
            return ResponseEntity.ok().build();
        }
    }

    /**
     * curl -i localhost:8080/chat/banList
     */
    @RequestMapping(
            path = "banList",
            method = RequestMethod.GET,
            produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity viewBanList() {
        String responseBody = String.join("\n", banList.keySet().stream().sorted().collect(Collectors.toList()));
        return ResponseEntity.ok(responseBody);
    }

    /**
     * curl -X POST -i localhost:8080/chat/unban -d "name=I_AM_STUPID"
     */
    @RequestMapping(
            path = "unban",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> unban(@RequestParam("name") String name) {
        if (!banList.containsKey(name)) {
            return ResponseEntity.badRequest().body("user [" + name + "] wasn't banned");
        } else {
            banList.remove(name, name);
            messages.add("[" + name + "] was unbanned");
            return ResponseEntity.ok().build();
        }
    }

    /**
     * curl -X PUT -i localhost:8080/chat/rename -d "oldName=I_AM_STUPID&newName=Just_Do_It"
     */
    @RequestMapping(
            path = "rename",
            method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> rename(@RequestParam("oldName") String oldName,
                                         @RequestParam("newName") String newName) {
        if (!usersOnline.containsKey(oldName)) {
            return ResponseEntity.badRequest().body("user [" + oldName + "] didn't logged in");
        } else {
            usersOnline.remove(oldName);
            usersOnline.put(newName, newName);
            messages.add("[" + oldName + "] " + " rename to: " + "[" + newName + "]");
            return ResponseEntity.ok().build();
        }
    }
}
