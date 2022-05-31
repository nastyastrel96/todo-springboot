package com.nastyastrel.springbootrest.rest;

import com.nastyastrel.springbootrest.model.tags.Tag;
import com.nastyastrel.springbootrest.model.todo.TodoItem;
import com.nastyastrel.springbootrest.model.user.User;
import com.nastyastrel.springbootrest.service.tag.TagService;
import com.nastyastrel.springbootrest.service.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping(path = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class TagController {
    private final TagService tagService;
    private final UserService userService;

    @PostMapping("/tags")
    @ResponseStatus(HttpStatus.CREATED)
    public void save(@RequestBody Tag tag, Principal principal) {
        User user = userService.getAuthenticatedUser(principal);
        tag.setUserId(user.getUserId());
        tagService.save(tag);
    }

    @PutMapping("/tags/todo/{itemId}/tag/{tagId}")
    public ResponseEntity<TodoItem> assignTag(@PathVariable Long itemId,
                                              @PathVariable Long tagId,
                                              Principal principal) {
        User user = userService.getAuthenticatedUser(principal);
        return new ResponseEntity<>(tagService.attachTag(user.getUserId(), itemId, tagId), HttpStatus.OK);
    }

    @DeleteMapping("/tags/todo/{itemId}/tag/{tagId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTag(@PathVariable Long itemId,
                          @PathVariable Long tagId,
                          Principal principal) {
        User user = userService.getAuthenticatedUser(principal);
        tagService.detachTag(user.getUserId(), itemId, tagId);
    }


}
