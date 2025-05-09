package com.enonic.lib.thymeleaf.view.impl;

import java.text.MessageFormat;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.google.common.collect.Multimap;

import com.enonic.lib.thymeleaf.view.ViewFunction;
import com.enonic.lib.thymeleaf.view.ViewFunctionParams;
import com.enonic.xp.i18n.LocaleService;
import com.enonic.xp.i18n.MessageBundle;
import com.enonic.xp.portal.PortalRequestAccessor;

import static com.enonic.lib.thymeleaf.view.impl.ParamsHelper.multipleValues;
import static com.enonic.lib.thymeleaf.view.impl.ParamsHelper.singleValue;

@Component(immediate = true)
public final class LocalizeFunction
    implements ViewFunction
{
    private final LocaleService localeService;

    static final String NO_MATCHING_BUNDLE = "no localization bundle found in application ''{0}''";

    private static final String NOT_TRANSLATED_MESSAGE = "NOT_TRANSLATED";

    @Activate
    public LocalizeFunction( @Reference final LocaleService localeService )
    {
        this.localeService = localeService;
    }

    @Override
    public String getName()
    {
        return "i18n.localize";
    }

    @Override
    public Object execute( final ViewFunctionParams params )
    {
        final LocalizeParams localizeParams = new LocalizeParams( PortalRequestAccessor.get() );

        final Multimap<String, String> arguments = params.getArgs();

        localizeParams.key( singleValue( arguments, "_key" ) );
        localizeParams.locale( singleValue( arguments, "_locale" ) );
        localizeParams.values( multipleValues( arguments, "_values" ) );
        localizeParams.application( singleValue( arguments, "_application" ) );

        final MessageBundle bundle = this.localeService.getBundle( localizeParams.getApplicationKey(), localizeParams.getLocale() );

        if ( bundle == null )
        {
            return MessageFormat.format( NO_MATCHING_BUNDLE, localizeParams.getApplicationKey() );
        }
        final String localizedMessage = bundle.localize( localizeParams.getKey(), localizeParams.getParams() );

        return localizedMessage != null ? localizedMessage : NOT_TRANSLATED_MESSAGE;
    }
}
