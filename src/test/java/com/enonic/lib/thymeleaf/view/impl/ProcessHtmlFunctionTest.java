package com.enonic.lib.thymeleaf.view.impl;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;

import com.enonic.lib.thymeleaf.view.ViewFunctionParams;
import com.enonic.xp.portal.url.ContextPathType;
import com.enonic.xp.portal.url.PortalUrlService;
import com.enonic.xp.portal.url.ProcessHtmlParams;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ProcessHtmlFunctionTest
{
    @Test
    void testExecute()
    {
        // prepare
        PortalUrlService urlService = mock( PortalUrlService.class );
        ViewFunctionParams params = mock( ViewFunctionParams.class );

        Multimap<String, String> args = LinkedHashMultimap.create();
        args.put( "_type", "absolute" );
        args.put( "_contextPath", "relative" );
        args.put( "_value", "html" );
        args.put( "_imageWidths", "768" );
        args.put( "_imageWidths", "1024" );
        args.put( "_imageSizes", "imageSizes" );
        args.put( "a", "1" );
        args.put( "b", "2" );
        args.put( "b", "3" );

        when( params.getArgs() ).thenReturn( args );

        when( urlService.processHtml( any( ProcessHtmlParams.class ) ) ).thenReturn( "processedHtml" );

        // test
        ProcessHtmlFunction function = new ProcessHtmlFunction( urlService );
        Object result = function.execute( params );

        // verify
        ArgumentCaptor<ProcessHtmlParams> captor = ArgumentCaptor.forClass( ProcessHtmlParams.class );
        verify( urlService ).processHtml( captor.capture() );

        ProcessHtmlParams capturedParams = captor.getValue();

        assertEquals( "absolute", capturedParams.getType() );
        assertEquals( ContextPathType.RELATIVE, capturedParams.getContextPathType() );
        assertEquals( "html", capturedParams.getValue() );
        assertEquals( List.of( 768, 1024 ), capturedParams.getImageWidths() );
        assertEquals( "imageSizes", capturedParams.getImageSizes() );

        Multimap<String, String> queryParams = capturedParams.getParams();

        assertEquals( 1, queryParams.get( "a" ).size() );
        assertTrue( queryParams.get( "a" ).contains( "1" ) );

        assertEquals( 2, queryParams.get( "b" ).size() );
        assertTrue( queryParams.get( "b" ).contains( "2" ) );
        assertTrue( queryParams.get( "b" ).contains( "3" ) );

        assertEquals( "processedHtml", result );
    }
}
