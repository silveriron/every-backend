package com.every.everybackend.comments.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CommentEntityTest {

    @DisplayName("댓글 수정")
    @Test
    void update() {
        // given
        String content = "댓글 내용";
        CommentEntity comment = CommentEntity.builder()
                .content(content)
                .build();
        String updatedContent = "수정된 댓글 내용";

        // when
        comment.update(updatedContent);

        // then
        assertEquals(updatedContent, comment.getContent());
    }

}