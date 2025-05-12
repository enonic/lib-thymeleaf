package com.enonic.lib.thymeleaf.view.impl;

import javax.servlet.http.HttpServletRequest;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;

import com.enonic.lib.thymeleaf.view.ViewFunctionParams;
import com.enonic.xp.portal.PortalRequest;
import com.enonic.xp.portal.PortalRequestAccessor;
import com.enonic.xp.portal.url.ContextPathType;
import com.enonic.xp.portal.url.IdentityUrlParams;
import com.enonic.xp.portal.url.PortalUrlService;
import com.enonic.xp.security.IdProviderKey;
import com.enonic.xp.web.vhost.VirtualHost;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class LoginUrlFunctionTest
{

    private PortalRequest mockRequest;

    @BeforeEach
    void setUp()
    {
        mockRequest = mock( PortalRequest.class );

        PortalRequestAccessor.set( mockRequest );
    }

    @AfterEach
    void destroy()
    {
        PortalRequestAccessor.remove();
    }

    @Test
    void testExecute()
    {
        PortalUrlService urlService = mock( PortalUrlService.class );

        VirtualHost virtualHost = mock( VirtualHost.class );
        when( virtualHost.getDefaultIdProviderKey() ).thenReturn( IdProviderKey.from( "idProviderName" ) );

        HttpServletRequest rawRequest = mock( HttpServletRequest.class );
        when( rawRequest.getAttribute( VirtualHost.class.getName() ) ).thenReturn( virtualHost );

        when( mockRequest.getRawRequest() ).thenReturn( rawRequest );

        Multimap<String, String> args = LinkedHashMultimap.create();
        args.put( "_type", "absolute" );
        args.put( "_redirect", "redirectUrl" );
        args.put( "a", "1" );
        args.put( "b", "2" );
        args.put( "b", "3" );

        ViewFunctionParams params = mock( ViewFunctionParams.class );

        when( params.getArgs() ).thenReturn( args );

        when( urlService.identityUrl( any( IdentityUrlParams.class ) ) ).thenReturn( "mockedUrl" );

        // test
        LoginUrlFunction function = new LoginUrlFunction( urlService );
        Object result = function.execute( params );

        // verify
        ArgumentCaptor<IdentityUrlParams> captor = ArgumentCaptor.forClass( IdentityUrlParams.class );
        verify( urlService ).identityUrl( captor.capture() );

        IdentityUrlParams capturedParams = captor.getValue();

        assertEquals( "absolute", capturedParams.getType() );
        assertEquals( ContextPathType.VHOST, capturedParams.getContextPathType() );
        assertEquals( "redirectUrl", capturedParams.getRedirectionUrl() );
        assertEquals( IdProviderKey.from( "idProviderName" ), capturedParams.getIdProviderKey() );
        assertEquals( "login", capturedParams.getIdProviderFunction() );

        Multimap<String, String> queryParams = capturedParams.getParams();

        assertEquals( 1, queryParams.get( "a" ).size() );
        assertTrue( queryParams.get( "a" ).contains( "1" ) );

        assertEquals( 2, queryParams.get( "b" ).size() );
        assertTrue( queryParams.get( "b" ).contains( "2" ) );
        assertTrue( queryParams.get( "b" ).contains( "3" ) );

        assertEquals( "mockedUrl", result );
    }

    @Test
    void testExecuteWithIdProviderName()
    {
        PortalUrlService urlService = mock( PortalUrlService.class );

        Multimap<String, String> args = LinkedHashMultimap.create();
        args.put( "_type", "absolute" );
        args.put( "_redirect", "redirectUrl" );
        args.put( "_idProvider", "myIdProvider" );
        args.put( "a", "1" );
        args.put( "b", "2" );
        args.put( "b", "3" );

        ViewFunctionParams params = mock( ViewFunctionParams.class );

        when( params.getArgs() ).thenReturn( args );

        when( urlService.identityUrl( any( IdentityUrlParams.class ) ) ).thenReturn( "mockedUrl" );

        // test
        LoginUrlFunction function = new LoginUrlFunction( urlService );
        Object result = function.execute( params );

        // verify
        ArgumentCaptor<IdentityUrlParams> captor = ArgumentCaptor.forClass( IdentityUrlParams.class );
        verify( urlService ).identityUrl( captor.capture() );

        IdentityUrlParams capturedParams = captor.getValue();

        assertEquals( "absolute", capturedParams.getType() );
        assertEquals( ContextPathType.VHOST, capturedParams.getContextPathType() );
        assertEquals( "redirectUrl", capturedParams.getRedirectionUrl() );
        assertEquals( IdProviderKey.from( "myIdProvider" ), capturedParams.getIdProviderKey() );
        assertEquals( "login", capturedParams.getIdProviderFunction() );

        Multimap<String, String> queryParams = capturedParams.getParams();

        assertEquals( 1, queryParams.get( "a" ).size() );
        assertTrue( queryParams.get( "a" ).contains( "1" ) );

        assertEquals( 2, queryParams.get( "b" ).size() );
        assertTrue( queryParams.get( "b" ).contains( "2" ) );
        assertTrue( queryParams.get( "b" ).contains( "3" ) );

        assertEquals( "mockedUrl", result );
    }
}
