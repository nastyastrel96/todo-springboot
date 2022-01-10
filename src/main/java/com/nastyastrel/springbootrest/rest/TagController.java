package com.nastyastrel.springbootrest.rest;

import com.nastyastrel.springbootrest.model.tags.Tag;
import com.nastyastrel.springbootrest.model.user.User;
import com.nastyastrel.springbootrest.service.tag.TagService;
import com.nastyastrel.springbootrest.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
public class TagController {
    private final TagService tagService;
    private final UserService userService;

    @Autowired
    public TagController(TagService tagService, UserService userService) {
        this.tagService = tagService;
        this.userService = userService;
    }

    @PostMapping("/tags")
    @ResponseStatus(HttpStatus.CREATED)
    public void save(@RequestBody Tag tag) {
        User user = userService.getAuthenticatedUser().orElseThrow();
        tag.setUserId(user.getUserId());
        tagService.save(tag);
    }

    @PutMapping("/tags/todo/{itemId}/tag/{tagId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Tag> assignTag(@PathVariable Long itemId,
                                         @PathVariable Long tagId) {
        User user = userService.getAuthenticatedUser().orElseThrow();
        return tagService.assignTag(user.getUserId(), itemId, tagId);
    }

    @DeleteMapping("/tags/todo/{itemId}/tag/{tagId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Tag> deleteTag(@PathVariable Long itemId,
                                         @PathVariable Long tagId) {
        User user = userService.getAuthenticatedUser().orElseThrow();
        return tagService.deleteTag(user.getUserId(), itemId, tagId);
    }


}
