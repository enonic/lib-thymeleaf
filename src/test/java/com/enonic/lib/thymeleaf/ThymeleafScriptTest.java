package com.enonic.lib.thymeleaf;

import java.util.TimeZone;
import java.util.regex.Pattern;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;

import com.enonic.lib.thymeleaf.view.ViewFunctionService;
import com.enonic.xp.resource.ResourceKey;
import com.enonic.xp.testing.ScriptRunnerSupport;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ThymeleafScriptTest
    extends ScriptRunnerSupport
{
    @Override
    public String getScriptTestFile()
    {
        return "/thymeleaf-test.js";
    }

    @Override
    protected void initialize()
        throws Exception
    {
        super.initialize();

        addService( ViewFunctionService.class, new MockViewFunctionService() );

        TimeZone.setDefault( TimeZone.getTimeZone( "GMT" ) );
    }

    private void assertHtmlEquals( final String expectedHtml, final String actualHtml )
    {
        assertEquals( normalizeText( expectedHtml ), normalizeText( actualHtml ) );
    }

    public void assertHtmlEquals( final ResourceKey resource, final String actualHtml )
    {
        assertHtmlEquals( loadResource( resource ).readString(), actualHtml );
    }

    private String normalizeText( final String text )
    {
        final Iterable<String> lines = Splitter.on( Pattern.compile( "(\r\n|\n|\r)" ) ).trimResults().split( text );
        return Joiner.on( "\n" ).join( lines );
    }
}
