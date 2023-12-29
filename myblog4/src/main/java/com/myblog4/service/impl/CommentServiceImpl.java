package com.myblog4.service.impl;

import com.myblog4.entity.Comment;
import com.myblog4.entity.Post;
import com.myblog4.exception.ResourceNotFound;
import com.myblog4.payload.CommentDto;
import com.myblog4.repository.CommentRepository;
import com.myblog4.repository.PostRepository;
import com.myblog4.service.CommentService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {
    private CommentRepository commentRepo;
    private PostRepository postRepo;

    private ModelMapper modelMapper;

    public CommentServiceImpl(CommentRepository commentRepo, PostRepository postRepo, ModelMapper modelMapper) {
        this.commentRepo = commentRepo;
        this.postRepo = postRepo;
        this.modelMapper= modelMapper;
    }

    @Override
    public CommentDto createComment(long postId, CommentDto commentDto) {
        Post post = postRepo.findById(postId).orElseThrow(
                () -> new ResourceNotFound("Post Not Found with this Id:" + postId)
        );

        Comment comment = mapToEntity(commentDto);
        comment.setPost(post);
        Comment c = commentRepo.save(comment);
        return mapToDto(c);
    }

    @Override
    public void deleteCommentById(long postId, long commentId) {
        Post post = postRepo.findById(postId).orElseThrow(
                () -> new ResourceNotFound("Post Not Found with this Id:" + postId)
        );
        commentRepo.deleteById(commentId);
    }
    //getByPostId
    @Override
    public List<CommentDto> getCommentsByPostId(long postId) {
        List<Comment> comments = commentRepo.findByPostId(postId);
        List<CommentDto> dtos = comments.stream().map(comment -> mapToDto(comment)).collect(Collectors.toList());
        return dtos;
    }
    //update
    @Override
    public CommentDto updateComment(long commentId, CommentDto commentDto) {
        Comment com=commentRepo.findById(commentId).get();
        Post post = postRepo.findById(com.getPost().getId()).get();
        com.getPost().setId(com.getPost().getId());
        com.setName(commentDto.getName());
        com.setEmail(commentDto.getEmail());
        com.setBody(commentDto.getBody());
        Comment updatedCom= commentRepo.save(com);
        return mapToDto(updatedCom);
//        Comment comment = mapToEntity(commentDto);
//        comment.setPost(post);
//        comment.setId(commentId);
//        Comment saveComment = commentRepo.save(comment);
//        CommentDto dto = mapToDto(saveComment);
//        return dto;
    }

    Comment mapToEntity(CommentDto dto){
        Comment comment = modelMapper.map(dto, Comment.class);
//        Comment comment= new Comment();
//        comment.setName(dto.getName());
//        comment.setEmail(dto.getEmail());
//        comment.setBody(dto.getBody());
        return comment;
    }
    CommentDto mapToDto(Comment comment){
        CommentDto dto = modelMapper.map(comment, CommentDto.class);
//        CommentDto dto= new CommentDto();
//        dto.setName(comment.getName());
//        dto.setEmail(comment.getEmail());
//        dto.setBody(comment.getBody());
        return dto;
    }
}
