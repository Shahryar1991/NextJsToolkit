
package com.shahryar.nextjstoolkit.generators

import com.shahryar.nextjstoolkit.icons.NextIcons
import javax.swing.Icon

enum class SourceLanguage {
    TYPESCRIPT,
    JAVASCRIPT
}

enum class NameType {
    COMPONENT,
    ROUTE,
    FILE
}

data class TemplateDefinition(
    val title: String,
    val baseName: String,
    private val typescriptExtension: String,
    private val javascriptExtension: String,
    private val typescriptContent: String,
    private val javascriptContent: String,
    val requiresName: Boolean = false,
    val icon: Icon? = null,
    val nameType: NameType = NameType.FILE
) {

    fun fileName(
        language: SourceLanguage,
        customName: String? = null
    ): String {
        val extension = when (language) {
            SourceLanguage.TYPESCRIPT -> typescriptExtension
            SourceLanguage.JAVASCRIPT -> javascriptExtension
        }

        val name = when {
            nameType == NameType.ROUTE -> baseName

            requiresName -> customName
                ?.takeIf { it.isNotBlank() }
                ?: throw IllegalArgumentException("A name is required.")

            else -> baseName
        }

        return "$name.$extension"
    }

    fun content(
        language: SourceLanguage,
        componentName: String? = null
    ): String {
        val source = when (language) {
            SourceLanguage.TYPESCRIPT -> typescriptContent
            SourceLanguage.JAVASCRIPT -> javascriptContent
        }

        return source
            .trimIndent()
            .replace(
                "{{COMPONENT_NAME}}",
                componentName ?: "Component"
            ) + "\n"
    }
}

object NextTemplateGenerator {

    val templates = listOf(

        // Page
        TemplateDefinition(
            title = "Page Component",
            baseName = "page",
            typescriptExtension = "tsx",
            javascriptExtension = "jsx",
            typescriptContent = """
                export default function Page() {
                    return (
                        <main>
                            <h1>Page</h1>
                        </main>
                    );
                }
            """,
            javascriptContent = """
                export default function Page() {
                    return (
                        <main>
                            <h1>Page</h1>
                        </main>
                    );
                }
            """,
            icon = NextIcons.PAGE
        ),

        // Layout
        TemplateDefinition(
            title = "Layout Component",
            baseName = "layout",
            typescriptExtension = "tsx",
            javascriptExtension = "jsx",
            typescriptContent = """
                import type { ReactNode } from "react";

                export default function Layout({
                    children,
                }: Readonly<{
                    children: ReactNode;
                }>) {
                    return (
                        <section>
                            {children}
                        </section>
                    );
                }
            """,
            javascriptContent = """
                export default function Layout({ children }) {
                    return (
                        <section>
                            {children}
                        </section>
                    );
                }
            """,
            icon = NextIcons.LAYOUT
        ),

        // Loading
        TemplateDefinition(
            title = "Loading Component",
            baseName = "loading",
            typescriptExtension = "tsx",
            javascriptExtension = "jsx",
            typescriptContent = """
                export default function Loading() {
                    return <div>Loading...</div>;
                }
            """,
            javascriptContent = """
                export default function Loading() {
                    return <div>Loading...</div>;
                }
            """,
            icon = NextIcons.LOADING
        ),

        // Error
        TemplateDefinition(
            title = "Error Component",
            baseName = "error",
            typescriptExtension = "tsx",
            javascriptExtension = "jsx",
            typescriptContent = """
                "use client";

                type ErrorProps = {
                    error: Error & { digest?: string };
                    reset: () => void;
                };

                export default function Error({ error, reset }: ErrorProps) {
                    return (
                        <main>
                            <h2>Something went wrong!</h2>
                            <button onClick={() => reset()}>
                                Try again
                            </button>
                        </main>
                    );
                }
            """,
            javascriptContent = """
                "use client";

                export default function Error({ error, reset }) {
                    return (
                        <main>
                            <h2>Something went wrong!</h2>
                            <button onClick={() => reset()}>
                                Try again
                            </button>
                        </main>
                    );
                }
            """,
            icon = NextIcons.ERROR
        ),

        // Not Found
        TemplateDefinition(
            title = "Not Found Component",
            baseName = "not-found",
            typescriptExtension = "tsx",
            javascriptExtension = "jsx",
            typescriptContent = """
                export default function NotFound() {
                    return (
                        <main>
                            <h1>404 - Page Not Found</h1>
                            <p>The requested page could not be found.</p>
                        </main>
                    );
                }
            """,
            javascriptContent = """
                export default function NotFound() {
                    return (
                        <main>
                            <h1>404 - Page Not Found</h1>
                            <p>The requested page could not be found.</p>
                        </main>
                    );
                }
            """,
            icon = NextIcons.NOT_FOUND
        ),

        // API Route
        TemplateDefinition(
            title = "API Route",
            baseName = "route",
            typescriptExtension = "ts",
            javascriptExtension = "js",
            typescriptContent = """
                import { NextResponse } from "next/server";

                export async function GET() {
                    return NextResponse.json({
                        message: "OK",
                    });
                }
            """,
            javascriptContent = """
                import { NextResponse } from "next/server";

                export async function GET() {
                    return NextResponse.json({
                        message: "OK",
                    });
                }
            """,
            icon = NextIcons.API_ROUTE
        ),

        // Proxy
        TemplateDefinition(
            title = "Proxy",
            baseName = "proxy",
            typescriptExtension = "ts",
            javascriptExtension = "js",
            typescriptContent = """
                import type { NextRequest } from "next/server";
                import { NextResponse } from "next/server";

                export function proxy(request: NextRequest) {
                    return NextResponse.next();
                }

                export const config = {
                    matcher: ["/((?!_next/static|_next/image|favicon.ico).*)"],
                };
            """,
            javascriptContent = """
                import { NextResponse } from "next/server";

                export function proxy(request) {
                    return NextResponse.next();
                }

                export const config = {
                    matcher: ["/((?!_next/static|_next/image|favicon.ico).*)"],
                };
            """,
            icon = NextIcons.PROXY
        ),

        // Server Action
        TemplateDefinition(
            title = "Server Action",
            baseName = "actions",
            requiresName = true,
            typescriptExtension = "ts",
            javascriptExtension = "js",
            typescriptContent = """
                "use server";

                export async function createSomething(formData: FormData) {
                    // Validate input and perform the server-side operation.
                }
            """,
            javascriptContent = """
                "use server";

                export async function createSomething(formData) {
                    // Validate input and perform the server-side operation.
                }
            """,
            icon = NextIcons.SERVER_ACTION
        ),

        // Dynamic Page
        TemplateDefinition(
            title = "Dynamic Page",
            baseName = "page",
            typescriptExtension = "tsx",
            javascriptExtension = "jsx",
            requiresName = true,
            nameType = NameType.ROUTE,
            typescriptContent = """
                type PageProps = {
                    params: Promise<{
                        slug: string;
                    }>;
                };

                export default async function Page({ params }: PageProps) {
                    const { slug } = await params;

                    return (
                        <main>
                            <h1>Dynamic Page</h1>
                            <p>Slug: {slug}</p>
                        </main>
                    );
                }
            """,
            javascriptContent = """
                export default async function Page({ params }) {
                    const { slug } = await params;

                    return (
                        <main>
                            <h1>Dynamic Page</h1>
                            <p>Slug: {slug}</p>
                        </main>
                    );
                }
            """,
            icon = NextIcons.PAGE
        ),

        // Custom Component
        TemplateDefinition(
            title = "Component",
            baseName = "component",
            typescriptExtension = "tsx",
            javascriptExtension = "jsx",
            requiresName = true,
            nameType = NameType.COMPONENT,
            typescriptContent = """
                export default function {{COMPONENT_NAME}}() {
                    return (
                        <div>
                            {{COMPONENT_NAME}}
                        </div>
                    );
                }
            """,
            javascriptContent = """
                export default function {{COMPONENT_NAME}}() {
                    return (
                        <div>
                            {{COMPONENT_NAME}}
                        </div>
                    );
                }
            """,
            icon = NextIcons.COMPONENT
        )
    )
}
