package com.enonic.lib.thymeleaf.view.impl;

import java.util.List;
import java.util.Locale;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;

import com.enonic.lib.thymeleaf.view.ViewFunctionParams;
import com.enonic.xp.app.ApplicationKey;
import com.enonic.xp.i18n.LocaleService;
import com.enonic.xp.i18n.MessageBundle;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class LocalizeFunctionTest
{
    @Test
    void testExecute()
    {
        // prepare
        LocaleService localeService = mock( LocaleService.class );
        ViewFunctionParams params = mock( ViewFunctionParams.class );
        MessageBundle messageBundle = mock( MessageBundle.class );

        Multimap<String, String> args = LinkedHashMultimap.create();
        args.put( "_key", "key" );
        args.put( "_locale", "en" );
        args.put( "_values", "phrases" );
        args.put( "_application", "myApp" );

        when( params.getArgs() ).thenReturn( args );

        when( localeService.getBundle( any( ApplicationKey.class ), any( Locale.class ) ) ).thenReturn( messageBundle );

        // test
        LocalizeFunction function = new LocalizeFunction( localeService );
        function.execute( params );

        // verify
        ArgumentCaptor<String> keyCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Object[]> paramsCaptor = ArgumentCaptor.forClass(Object[].class);

        verify(messageBundle).localize(keyCaptor.capture(), paramsCaptor.capture());

        assertEquals("key", keyCaptor.getValue());
        assertEquals( List.of("phrases").toArray()[0], paramsCaptor.getValue()[0]);
    }
}
