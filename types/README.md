# Types for Thymeleaf library

Type definitions for the [Enonic XP Thymeleaf library](https://github.com/enonic/lib-thymeleaf).

## Install

```bash
npm i --save-dev @enonic-types/lib-thymeleaf
```

## Setup

Add lib-thymeleaf to `compilerOptions.paths` in the `tsconfig.json` file used for your application's server-side TypeScript code:

```json
{
  "compilerOptions": {
    "paths": {
      "/lib/thymeleaf": ["./node_modules/@enonic-types/lib-thymeleaf"]
    }
  }
}
```

## Use

```ts
import {render} from '/lib/thymeleaf';

export function get() {
    const view = resolve('view/fruits.html');

    const model = {
        fruits: [
            {name: 'Apple', color: 'Red'},
            {name: 'Pear', color: 'Green'},
        ],
    };

    return {
        body: render(view, model, {mode: 'HTML'}),
        contentType: 'text/html',
    };
}
```

All imports from `/lib/thymeleaf` should now be typed.

## API

### `render(view: ResourceKey, model: Record<string, unknown>, options?: RenderOptions): string`

Renders a view using Thymeleaf and returns the rendered output.

| Parameter | Type                      | Description                                                        |
|-----------|---------------------------|--------------------------------------------------------------------|
| `view`    | `ResourceKey`             | Location of the view. Use `resolve(...)` to resolve a view.        |
| `model`   | `Record<string, unknown>` | Model that is passed to the view.                                  |
| `options` | `RenderOptions`           | *Optional.* Rendering options.                                     |

### `RenderOptions`

| Property | Type           | Description                          |
|----------|----------------|--------------------------------------|
| `mode`   | `TemplateMode` | Rendering mode. Defaults to `HTML`.  |

### `TemplateMode`

`'HTML' | 'XML' | 'TEXT' | 'JAVASCRIPT' | 'CSS' | 'RAW'`

Any other value falls back to `HTML` at runtime.

## Documentation

[Thymeleaf library](https://developer.enonic.com/docs/thymeleaf-library)
