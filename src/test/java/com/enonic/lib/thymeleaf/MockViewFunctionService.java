package com.enonic.lib.thymeleaf;

import com.enonic.lib.thymeleaf.view.ViewFunctionParams;
import com.enonic.lib.thymeleaf.view.ViewFunctionService;

public final class MockViewFunctionService
    implements ViewFunctionService
{
    @Override
    public Object execute( final ViewFunctionParams params )
    {
        return params.getName() + "(" + params.getArgs().toString() + ")";
    }
}
