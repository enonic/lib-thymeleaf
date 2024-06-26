package com.enonic.lib.thymeleaf;

import java.util.HashSet;
import java.util.Set;

import org.thymeleaf.dialect.AbstractProcessorDialect;
import org.thymeleaf.dialect.IExpressionObjectDialect;
import org.thymeleaf.expression.IExpressionObjectFactory;
import org.thymeleaf.processor.IProcessor;

final class ExtensionDialectImpl
    extends AbstractProcessorDialect
    implements IExpressionObjectDialect
{
    private final MapExpressionObjectFactory expressionObjectFactory;

    ExtensionDialectImpl()
    {
        super( "Portal", "portal", 1000 );

        this.expressionObjectFactory = new MapExpressionObjectFactory();
        this.expressionObjectFactory.put( "js", new JavascriptExecutor() );
    }

    @Override
    public Set<IProcessor> getProcessors( final String dialectPrefix )
    {
        final Set<IProcessor> processors = new HashSet<>();
        processors.add( new ComponentProcessor( dialectPrefix ) );
        return processors;
    }

    @Override
    public IExpressionObjectFactory getExpressionObjectFactory()
    {
        return this.expressionObjectFactory;
    }
}
