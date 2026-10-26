
package com.example.demo.exceptions;

public class ActionNotAllowed extends RuntimeException{

    public ActionNotAllowed()
    {
        super("Action is not allowed");
    }

    public ActionNotAllowed(String message)
    {
        super(message);
    }
}
