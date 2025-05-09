package com.enonic.lib.thymeleaf.view.impl;

import org.junit.jupiter.api.Test;

import com.enonic.lib.thymeleaf.view.ViewFunctionParams;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ImagePlaceholderFunctionTest
{
    @Test
    void testExecute()
    {
        // prepare
        ViewFunctionParams params = mock( ViewFunctionParams.class );

        when( params.getRequiredValue( eq( "width" ), any() ) ).thenReturn( 768 );
        when( params.getRequiredValue( eq( "height" ), any() ) ).thenReturn( 450 );

        // test
        ImagePlaceholderFunction function = new ImagePlaceholderFunction();
        Object result = function.execute( params );

        // verify
        assertNotNull( result );
    }
}
