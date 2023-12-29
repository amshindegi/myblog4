package com.myblog4.service;

import com.myblog4.entity.Comment;
import com.myblog4.payload.CommentDto;

import java.util.List;

public interface CommentService {
    public CommentDto createComment(long postId, CommentDto commentDto);

    public void deleteCommentById(long postId, long commentId);
    List<CommentDto> getCommentsByPostId(long postId);

    CommentDto updateComment(long commentId, CommentDto commentDto);
}
