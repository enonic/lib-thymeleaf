package com.enonic.lib.thymeleaf.view.impl;

import org.osgi.service.component.annotations.Component;

import com.enonic.lib.thymeleaf.view.ViewFunction;
import com.enonic.lib.thymeleaf.view.ViewFunctionParams;
import com.enonic.xp.image.ImageHelper;

@Component(immediate = true)
public final class ImagePlaceholderFunction
    implements ViewFunction
{
    @Override
    public String getName()
    {
        return "imagePlaceholder";
    }

    @Override
    public Object execute( final ViewFunctionParams params )
    {
        final int width = params.getRequiredValue( "width", Integer.class );
        final int height = params.getRequiredValue( "height", Integer.class );
        return ImageHelper.createImagePlaceholder( width, height );
    }
}
