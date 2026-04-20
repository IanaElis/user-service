package com.alex.project.exceptions.service;

public class RecordAlreadyExistException extends RuntimeException {
    public RecordAlreadyExistException(String message) {
        super(message);
    }
}
