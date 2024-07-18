package org.myblog.domain.series.exception;

public class SeriesNotFoundException extends RuntimeException{
    public SeriesNotFoundException(String msg){
        super(msg);
    }
}
