package com.every.everybackend.comments.service;

import com.every.everybackend.comments.entity.CommentEntity;
import com.every.everybackend.comments.repository.CommentRepository;
import com.every.everybackend.comments.service.command.CreateCommentCommand;
import com.every.everybackend.comments.service.command.GetCommentsCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    public void createComment(CreateCommentCommand command) {

        CommentEntity commentEntity = CommentEntity.builder()
                .postId(command.postId())
                .content(command.content())
                .user(command.user())
                .build();

        commentRepository.save(commentEntity);
    }

    public List<CommentEntity> getComments(GetCommentsCommand command) {

        return commentRepository.findAllByPostId(command.postId());
    }
}
