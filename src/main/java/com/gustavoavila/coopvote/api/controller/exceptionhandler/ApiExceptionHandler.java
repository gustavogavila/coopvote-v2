package com.gustavoavila.coopvote.api.controller.exceptionhandler;

import com.gustavoavila.coopvote.domain.exceptions.AgendaNotFoundException;
import com.gustavoavila.coopvote.domain.exceptions.DuplicatedVoteException;
import com.gustavoavila.coopvote.domain.exceptions.VotingSessionClosedException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static java.util.Objects.isNull;

@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(AgendaNotFoundException.class)
    public ResponseEntity<?> handleAgendaNotFoundException(AgendaNotFoundException ex, WebRequest webRequest) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        Problem problem = createProblem("Entity not found", ex.getMessage(), status);
        return handleExceptionInternal(ex, problem, new HttpHeaders(), status, webRequest);
    }

    @ExceptionHandler(DuplicatedVoteException.class)
    public ResponseEntity<?> handleDuplicatedVoteException(DuplicatedVoteException ex, WebRequest webRequest) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        Problem problem = createProblem("Duplicate information", ex.getMessage(), status);
        return handleExceptionInternal(ex, problem, new HttpHeaders(), status, webRequest);
    }

    @ExceptionHandler(VotingSessionClosedException.class)
    public ResponseEntity<?> handleVotingSessionClosedException(VotingSessionClosedException ex, WebRequest webRequest) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        Problem problem = createProblem("Voting Session Closed", ex.getMessage(), status);
        return handleExceptionInternal(ex, problem, new HttpHeaders(), status, webRequest);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleUncaught(Exception ex, WebRequest request) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        String detail = "An unexpected internal system error has occurred. Please try again and if the problem " +
                "persists, contact your system administrator";
        ex.printStackTrace();
        Problem problem = createProblem("System error", detail, status);
        return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status,
                                                                  WebRequest webRequest) {

        Problem problem = createProblem("Message not readable",
                "The request body is invalid. Check syntax error.", status);
        return handleExceptionInternal(ex, problem, new HttpHeaders(), status, webRequest);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex,
                                                             Object body,
                                                             HttpHeaders headers,
                                                             HttpStatus status,
                                                             WebRequest request) {

        if (isNull(body)) {
            Problem problem = new Problem();
            problem.setStatus(status.value());
            problem.setTitle(status.getReasonPhrase());
            body = problem;
        } else if(body instanceof String) {
            Problem problem = new Problem();
            problem.setStatus(status.value());
            problem.setTitle((String) body);
            body = problem;
        }

        return super.handleExceptionInternal(ex, body, headers, status, request);
    }

    private Problem createProblem(String title, String detail, HttpStatus status) {
        Problem problem = new Problem();
        problem.setStatus(status.value());
        problem.setTitle(title);
        problem.setDetail(detail);
        return problem;
    }
}
