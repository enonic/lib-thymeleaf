package com.enonic.lib.thymeleaf.view.impl;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.google.common.collect.Multimap;

import com.enonic.lib.thymeleaf.view.ViewFunction;
import com.enonic.lib.thymeleaf.view.ViewFunctionParams;
import com.enonic.xp.portal.PortalRequestAccessor;
import com.enonic.xp.portal.url.PortalUrlService;
import com.enonic.xp.portal.url.ProcessHtmlParams;

import static com.enonic.lib.thymeleaf.view.ParamsHelper.multipleValues;
import static com.enonic.lib.thymeleaf.view.ParamsHelper.singleValue;

@Component(immediate = true)
public final class ProcessHtmlFunction
    implements ViewFunction
{
    private final PortalUrlService urlService;

    @Activate
    public ProcessHtmlFunction( @Reference final PortalUrlService urlService )
    {
        this.urlService = urlService;
    }

    @Override
    public String getName()
    {
        return "processHtml";
    }

    @Override
    public Object execute( final ViewFunctionParams params )
    {
        final ProcessHtmlParams urlParams = new ProcessHtmlParams();

        final Multimap<String, String> arguments = params.getArgs();

        urlParams.portalRequest( PortalRequestAccessor.get() ); // TODO: remove this, XP8 must resolve the request
        urlParams.type( singleValue( arguments, "_type" ) );
        urlParams.value( singleValue( arguments, "_value" ) );
        urlParams.imageWidths( Objects.requireNonNullElse( multipleValues( arguments, "_imageWidths" ), List.<String>of() ).stream().map(
            Integer::parseInt ).collect( Collectors.toUnmodifiableList() ) );
        urlParams.imageSizes( singleValue( arguments, "_imageSizes" ) );

        arguments.forEach( ( key, value ) -> urlParams.getParams().put( key, value ) );

        return this.urlService.processHtml( urlParams );
    }
}
