package com.enonic.lib.thymeleaf.view;

public interface ViewFunction
{
    String getName();

    Object execute( ViewFunctionParams params );
}
