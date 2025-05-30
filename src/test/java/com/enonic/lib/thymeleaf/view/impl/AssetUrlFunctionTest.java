package com.enonic.lib.thymeleaf.view.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;

import com.enonic.lib.thymeleaf.view.ViewFunctionParams;
import com.enonic.xp.portal.url.AssetUrlParams;
import com.enonic.xp.portal.url.PortalUrlService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AssetUrlFunctionTest
{
    private PortalUrlService urlService;

    private AssetUrlFunction function;

    @BeforeEach
    void setUp()
    {
        urlService = mock( PortalUrlService.class );
        function = new AssetUrlFunction( urlService );
    }

    @Test
    void testName()
    {
        assertEquals( "assetUrl", function.getName() );
    }

    @Test
    void testExecute()
    {
        // prepare
        ViewFunctionParams params = mock( ViewFunctionParams.class );

        Multimap<String, String> args = LinkedHashMultimap.create();
        args.put( "_path", "some/path" );
        args.put( "_application", "appName" );
        args.put( "_type", "absolute" );
        args.put( "a", "1" );
        args.put( "b", "2" );
        args.put( "b", "3" );

        when( params.getArgs() ).thenReturn( args );

        when( urlService.assetUrl( any( AssetUrlParams.class ) ) ).thenReturn( "mockedUrl" );

        // test
        Object result = function.execute( params );

        // verify
        ArgumentCaptor<AssetUrlParams> captor = ArgumentCaptor.forClass( AssetUrlParams.class );
        verify( urlService ).assetUrl( captor.capture() );

        AssetUrlParams capturedParams = captor.getValue();

        assertEquals( "some/path", capturedParams.getPath() );
        assertEquals( "appName", capturedParams.getApplication() );
        assertEquals( "absolute", capturedParams.getType() );

        Multimap<String, String> queryParams = capturedParams.getParams();

        assertEquals( 1, queryParams.get( "a" ).size() );
        assertTrue( queryParams.get( "a" ).contains( "1" ) );

        assertEquals( 2, queryParams.get( "b" ).size() );
        assertTrue( queryParams.get( "b" ).contains( "2" ) );
        assertTrue( queryParams.get( "b" ).contains( "3" ) );

        assertEquals( "mockedUrl", result );
    }
}
