package com.nastyastrel.springbootrest;

import com.nastyastrel.springbootrest.model.tags.Tag;
import com.nastyastrel.springbootrest.model.tags.TagDTO;
import com.nastyastrel.springbootrest.model.todo.TaskState;
import com.nastyastrel.springbootrest.model.todo.TodoItem;
import com.nastyastrel.springbootrest.repository.TodoItemRepository;
import com.nastyastrel.springbootrest.service.tag.TagService;
import com.nastyastrel.springbootrest.service.todo.TodoItemService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;


import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
public class TodoItemServiceIntegrationTests {
    @Autowired
    private TodoItemService todoItemService;
    @Autowired
    private TagService tagService;
    @Autowired
    private TodoItemRepository todoItemRepository;

    @Container
    private static final PostgreSQLContainer<?> container = new PostgreSQLContainer<>(DockerImageName.parse("postgres:14.3"));
    private static final Long itemID = 1L;
    private static final Long userID = 1L;

    @DynamicPropertySource
    public static void overrideProps(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", container::getJdbcUrl);
        registry.add("spring.datasource.username", container::getUsername);
        registry.add("spring.datasource.password", container::getPassword);
    }

    @Test
    void itShouldFindThatAmountOfTodoItemsThatWeHaveSaved() {
        //when
        var answer = todoItemService.findAll(userID);

        //then
        assertThat(answer).hasSize(3);
    }

    @Test
    void itShouldAttachTagToTodoItem() {
        //given
        Tag tag = new Tag(1L, "Срочно", userID);
        TagDTO tagDTO = TagDTO.fromTag(tag);

        //when
        var answer = tagService.attachTag(userID, itemID, 1L);

        //then
        assertEquals(tagDTO, answer.getTags().get(answer.getTags().size() - 1));
    }

    @Test
    void itShouldDetachTagFromTodoItem() {
        //when
        tagService.detachTag(userID, itemID, 1L);

        //then
        assertThat(todoItemRepository.findById(itemID).get().getTags()).hasSize(1);

    }

    @Test
    void itShouldSaveTodoItem() {
        //given
        TodoItem todoItem = new TodoItem("Buy tickets", TaskState.DONE, LocalDateTime.now(), userID);

        //when
        todoItemService.save(todoItem);

        //then
        assertThat(todoItemService.findAll(userID)).hasSize(3);
    }
}
