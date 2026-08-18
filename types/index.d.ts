import type { ResourceKey } from "@enonic-types/core";

declare module "/lib/thymeleaf" {
    /**
     * Rendering mode. Defaults to `HTML`. Any other value falls back to `HTML` at runtime.
     */
    export type TemplateMode = "HTML" | "XML" | "TEXT" | "JAVASCRIPT" | "CSS" | "RAW";

    export interface RenderOptions {
        /**
         * Rendering mode. Defaults to `HTML`.
         */
        mode?: TemplateMode;
    }

    /**
     * Renders a view using Thymeleaf.
     *
     * @param view Location of the view. Use `resolve(...)` to resolve a view.
     * @param model Model that is passed to the view.
     * @param options Rendering options.
     * @returns The rendered output.
     */
    export function render(view: ResourceKey, model: Record<string, unknown>, options?: RenderOptions): string;
}

export {};
