package com.enonic.lib.thymeleaf.view.impl;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.google.common.collect.Multimap;

import com.enonic.lib.thymeleaf.view.ViewFunction;
import com.enonic.lib.thymeleaf.view.ViewFunctionParams;
import com.enonic.xp.portal.PortalRequestAccessor;
import com.enonic.xp.portal.url.ComponentUrlParams;
import com.enonic.xp.portal.url.PortalUrlService;

import static com.enonic.lib.thymeleaf.view.ParamsHelper.singleValue;

@Component(immediate = true)
public final class ComponentUrlFunction
    implements ViewFunction
{
    private final PortalUrlService urlService;

    @Activate
    public ComponentUrlFunction( @Reference final PortalUrlService urlService )
    {
        this.urlService = urlService;
    }

    @Override
    public String getName()
    {
        return "componentUrl";
    }

    @Override
    public Object execute( final ViewFunctionParams params )
    {
        final ComponentUrlParams urlParams = new ComponentUrlParams();

        final Multimap<String, String> arguments = params.getArgs();

        urlParams.portalRequest( PortalRequestAccessor.get() ); // TODO: remove this, XP8 must resolve the request
        urlParams.type( singleValue( arguments, "_type" ) );
        urlParams.id( singleValue( arguments, "_id" ) );
        urlParams.path( singleValue( arguments, "_path" ) );
        urlParams.component( singleValue( arguments, "_component" ) );

        arguments.forEach( ( key, value ) -> urlParams.getParams().put( key, value ) );

        return this.urlService.componentUrl( urlParams );
    }
}
