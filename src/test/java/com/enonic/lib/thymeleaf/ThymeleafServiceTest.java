package com.enonic.lib.thymeleaf;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.enonic.lib.thymeleaf.view.ViewFunctionService;
import com.enonic.xp.resource.ResourceKey;
import com.enonic.xp.resource.ResourceProblemException;
import com.enonic.xp.testing.ScriptTestSupport;
import com.enonic.xp.trace.Trace;
import com.enonic.xp.trace.TraceManager;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ThymeleafServiceTest
    extends ScriptTestSupport
{
    private ThymeleafService service;

    @Override
    protected void initialize()
        throws Exception
    {
        super.initialize();

        addService( ViewFunctionService.class, new MockViewFunctionService() );

        this.service = new ThymeleafService();
        this.service.initialize( newBeanContext( ResourceKey.from( "myapp:/site" ) ) );
    }

    @Test
    public void testProcess()
    {
        final ThymeleafProcessor processor = this.service.newProcessor();
        processor.setView( ResourceKey.from( "myapp:/view/test.html" ) );
        processor.setModel( null );
        processor.setMode( null );
        processor.process();
    }

    @Test
    public void testProcessError()
    {
        final ThymeleafProcessor processor = this.service.newProcessor();
        processor.setView( ResourceKey.from( "myapp:/view/error.html" ) );
        processor.setModel( null );
        processor.setMode( null );

        ResourceProblemException exception = assertThrows( ResourceProblemException.class, processor::process );
        assertNotNull( exception );
    }

    @Test
    public void testWithTracingEnabled()
    {
        final TraceManager manager = Mockito.mock( TraceManager.class );
        final Trace trace = Mockito.mock( Trace.class );
        Mockito.when( manager.newTrace( Mockito.any(), Mockito.any() ) ).thenReturn( trace );
        com.enonic.xp.trace.Tracer.setManager( manager );

        final ThymeleafProcessor processor = this.service.newProcessor();
        processor.setView( ResourceKey.from( "myapp:/view/test.html" ) );
        processor.setModel( null );
        processor.setMode( null );
        processor.process();
    }
}
