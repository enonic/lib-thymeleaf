package com.enonic.lib.thymeleaf.view.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;

import com.enonic.lib.thymeleaf.view.ViewFunctionParams;
import com.enonic.xp.portal.url.ComponentUrlParams;
import com.enonic.xp.portal.url.PortalUrlService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ComponentUrlFunctionTest
{
    private PortalUrlService urlService;

    private ComponentUrlFunction function;

    @BeforeEach
    void setUp()
    {
        urlService = mock( PortalUrlService.class );
        function = new ComponentUrlFunction( urlService );
    }

    @Test
    void testName()
    {
        assertEquals( "componentUrl", function.getName() );
    }

    @Test
    void testExecute()
    {
        // prepare
        ViewFunctionParams params = mock( ViewFunctionParams.class );

        Multimap<String, String> args = LinkedHashMultimap.create();
        args.put( "_type", "absolute" );
        args.put( "_id", "id" );
        args.put( "_path", "some/path" );
        args.put( "_component", "component" );
        args.put( "a", "1" );
        args.put( "b", "2" );
        args.put( "b", "3" );

        when( params.getArgs() ).thenReturn( args );

        when( urlService.componentUrl( any( ComponentUrlParams.class ) ) ).thenReturn( "mockedUrl" );

        // test
        Object result = function.execute( params );

        // verify
        ArgumentCaptor<ComponentUrlParams> captor = ArgumentCaptor.forClass( ComponentUrlParams.class );
        verify( urlService ).componentUrl( captor.capture() );

        ComponentUrlParams capturedParams = captor.getValue();

        assertEquals( "absolute", capturedParams.getType() );
        assertEquals( "id", capturedParams.getId() );
        assertEquals( "some/path", capturedParams.getPath() );
        assertEquals( "component", capturedParams.getComponent() );

        Multimap<String, String> queryParams = capturedParams.getParams();

        assertEquals( 1, queryParams.get( "a" ).size() );
        assertTrue( queryParams.get( "a" ).contains( "1" ) );

        assertEquals( 2, queryParams.get( "b" ).size() );
        assertTrue( queryParams.get( "b" ).contains( "2" ) );
        assertTrue( queryParams.get( "b" ).contains( "3" ) );

        assertEquals( "mockedUrl", result );
    }
}
