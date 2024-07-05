package org.myblog.domain.post.exception;

public class PostNotFoundException extends RuntimeException{
    public PostNotFoundException(String msg){
        super(msg);
    }
}
