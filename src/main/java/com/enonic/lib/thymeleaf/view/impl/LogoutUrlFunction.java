package com.enonic.lib.thymeleaf.view.impl;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.google.common.collect.Multimap;

import com.enonic.lib.thymeleaf.view.ViewFunction;
import com.enonic.lib.thymeleaf.view.ViewFunctionParams;
import com.enonic.xp.portal.PortalRequestAccessor;
import com.enonic.xp.portal.url.IdentityUrlParams;
import com.enonic.xp.portal.url.PortalUrlService;
import com.enonic.xp.security.IdProviderKey;
import com.enonic.xp.web.vhost.VirtualHost;
import com.enonic.xp.web.vhost.VirtualHostHelper;

import static com.enonic.lib.thymeleaf.view.impl.ParamsHelper.singleValue;

@Component(immediate = true)
public final class LogoutUrlFunction
    implements ViewFunction
{
    private final PortalUrlService urlService;

    @Activate
    public LogoutUrlFunction( @Reference final PortalUrlService urlService )
    {
        this.urlService = urlService;
    }

    @Override
    public String getName()
    {
        return "logoutUrl";
    }

    @Override
    public Object execute( final ViewFunctionParams params )
    {
        final IdentityUrlParams urlParams = new IdentityUrlParams();

        final Multimap<String, String> arguments = params.getArgs();

        urlParams.portalRequest( PortalRequestAccessor.get() ); // TODO: remove this, XP must resolve the request
        urlParams.idProviderFunction( "logout" );
        urlParams.idProviderKey( retrieveIdProviderKey( singleValue( arguments, "_idProvider" ) ) );
        urlParams.type( singleValue( arguments, "_type" ) );
        urlParams.contextPathType( singleValue( arguments, "_contextPath" ) );
        urlParams.redirectionUrl( singleValue( arguments, "_redirect" ) );

        arguments.forEach( ( key, value ) -> urlParams.getParams().put( key, value ) );

        return this.urlService.identityUrl( urlParams );
    }

    private IdProviderKey retrieveIdProviderKey( final String idProviderKey )
    {
        if ( idProviderKey != null )
        {
            return IdProviderKey.from( idProviderKey );
        }

        final VirtualHost virtualHost = VirtualHostHelper.getVirtualHost( PortalRequestAccessor.get().getRawRequest() );
        if ( virtualHost != null )
        {
            return virtualHost.getDefaultIdProviderKey();
        }
        return null;
    }
}
