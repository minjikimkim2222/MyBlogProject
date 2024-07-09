package org.myblog.domain.comment.exception;

public class CommentNotFoundException extends RuntimeException{
    public CommentNotFoundException(String msg){
        super(msg);
    }
}
