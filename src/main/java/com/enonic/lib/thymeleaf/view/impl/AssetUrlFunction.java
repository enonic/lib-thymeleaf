package com.enonic.lib.thymeleaf.view.impl;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.google.common.collect.Multimap;

import com.enonic.lib.thymeleaf.view.ViewFunction;
import com.enonic.lib.thymeleaf.view.ViewFunctionParams;
import com.enonic.xp.portal.PortalRequestAccessor;
import com.enonic.xp.portal.url.AssetUrlParams;
import com.enonic.xp.portal.url.PortalUrlService;

import static com.enonic.lib.thymeleaf.view.ParamsHelper.singleValue;

@Component(immediate = true)
public final class AssetUrlFunction
    implements ViewFunction
{
    private final PortalUrlService urlService;

    @Activate
    public AssetUrlFunction( @Reference final PortalUrlService urlService )
    {
        this.urlService = urlService;
    }

    @Override
    public String getName()
    {
        return "assetUrl";
    }

    @Override
    public Object execute( final ViewFunctionParams params )
    {
        final AssetUrlParams urlParams = new AssetUrlParams();

        final Multimap<String, String> arguments = params.getArgs();

        urlParams.portalRequest( PortalRequestAccessor.get() ); // TODO: remove this, XP8 must resolve the request
        urlParams.path( singleValue( arguments, "_path" ) );
        urlParams.application( singleValue( arguments, "_application" ) );
        urlParams.type( singleValue( arguments, "_type" ) );

        arguments.forEach( ( key, value ) -> urlParams.getParams().put( key, value ) );

        return this.urlService.assetUrl( urlParams );
    }
}
