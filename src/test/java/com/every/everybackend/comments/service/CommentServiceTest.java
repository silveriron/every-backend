package com.every.everybackend.comments.service;

import com.every.everybackend.base.MockTest;
import com.every.everybackend.comments.entity.CommentEntity;
import com.every.everybackend.comments.repository.CommentRepository;
import com.every.everybackend.comments.service.command.CreateCommentCommand;
import com.every.everybackend.comments.service.command.DeleteCommentCommand;
import com.every.everybackend.comments.service.command.GetCommentsCommand;
import com.every.everybackend.comments.service.command.UpdateCommentCommand;
import com.every.everybackend.common.exception.ApiException;
import com.every.everybackend.common.exception.errorcode.CommentErrorCode;
import com.every.everybackend.users.entity.UserEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CommentServiceTest extends MockTest {

    @Mock
    private CommentRepository commentRepository;

    @InjectMocks
    private CommentService commentService;

    @DisplayName("올바른 댓글 정보로 댓글 생성을 요청하면 댓글이 생성된다.")
    @Test
    void createComment() {
        // given
        CreateCommentCommand command = new CreateCommentCommand(
                1L,
                "content",
                UserEntity.builder()
                        .email("test@test.com")
                        .build());

        // when
        commentService.createComment(command);

        // then
        verify(commentRepository).save(CommentEntity.builder()
                .postId(command.postId())
                .content(command.content())
                .user(command.user())
                .build());
    }

    @DisplayName("올바른 게시글 ID로 댓글 목록을 조회하면 댓글 목록이 반환된다.")
    @Test
    void getComments() {
        // given
        Long postId = 1L;

        UserEntity userEntity = UserEntity.builder()
                .email("test@test.com")
                .build();

        GetCommentsCommand command = new GetCommentsCommand(postId);

        List<CommentEntity> commentEntities = List.of("1", "2", "3").stream().map(it -> CommentEntity.builder()
                .postId(postId)
                .content("content" + it)
                .user(userEntity)
                .build()).toList();

        when(commentRepository.findAllByPostId(postId)).thenReturn(commentEntities);

        // when
        List<CommentEntity> comments = commentService.getComments(command);

        // then
        assertEquals(commentEntities, comments);
    }

    @DisplayName("올바른 댓글 ID, 게시글 ID, 유저로 댓글을 수정하면 댓글이 수정된다.")
    @Test
    void updateComment() {
        // given
        Long postId = 1L;
        Long commentId = 1L;

        UserEntity userEntity = UserEntity.builder()
                .email("test@test.com")
                .build();

        CommentEntity commentEntity = CommentEntity.builder()
                .postId(postId)
                .content("content")
                .user(userEntity)
                .build();

        UpdateCommentCommand command = new UpdateCommentCommand(commentId, postId, "new content", userEntity);

        when(commentRepository.findByIdAndPostIdAndUser(commentId, postId, userEntity)).thenReturn(java.util.Optional.of(commentEntity));
        // when
        commentService.updateComment(command);

        // then
        verify(commentRepository).save(commentEntity);
    }

    @DisplayName("존재하지 않는 댓글 수정을 요청하면 예외가 발생한다.")
    @Test
    void updateCommentWithNotExistComment() {
        // given
        Long postId = 1L;
        Long commentId = 1L;

        UserEntity userEntity = UserEntity.builder()
                .email("test@test.com")
                .build();

        UpdateCommentCommand command = new UpdateCommentCommand(commentId, postId, "new content", userEntity);

        when(commentRepository.findByIdAndPostIdAndUser(commentId, postId, userEntity)).thenReturn(java.util.Optional.empty());

        // when
        ApiException apiException = assertThrows(ApiException.class, () -> commentService.updateComment(command));

        // then
        assertEquals(apiException.getErrorResponse().getCode(), CommentErrorCode.NOT_FOUND_COMMENT.getCode());
    }


    @DisplayName("올바른 댓글 ID, 게시글 ID, 유저로 댓글을 삭제하면 댓글이 삭제된다.")
    @Test
    void deleteComment() {
        // given
        Long postId = 1L;
        Long commentId = 1L;

        UserEntity userEntity = UserEntity.builder()
                .email("test@test.com")
                .build();

        DeleteCommentCommand command = new DeleteCommentCommand(commentId, postId, userEntity);

        // when
        commentService.deleteComment(command);

        // then
        verify(commentRepository).deleteByIdAndPostIdAndUser(commentId, postId, userEntity);
    }
}