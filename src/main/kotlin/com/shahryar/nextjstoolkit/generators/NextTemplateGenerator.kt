package com.shahryar.nextjstoolkit.generators

enum class SourceLanguage {
    TYPESCRIPT,
    JAVASCRIPT
}

data class TemplateDefinition(
    val title: String,
    private val typescriptExtension: String,
    private val javascriptExtension: String,
    private val typescriptContent: String,
    private val javascriptContent: String
) {
    fun fileName(language: SourceLanguage): String {
        val extension = when (language) {
            SourceLanguage.TYPESCRIPT -> typescriptExtension
            SourceLanguage.JAVASCRIPT -> javascriptExtension
        }
        return "$baseName.$extension"
    }

    fun content(language: SourceLanguage): String = when (language) {
        SourceLanguage.TYPESCRIPT -> typescriptContent.trimIndent() + "\n"
        SourceLanguage.JAVASCRIPT -> javascriptContent.trimIndent() + "\n"
    }

    private val baseName: String
        get() = when (title) {
            "Page Component" -> "page"
            "Layout Component" -> "layout"
            "Loading Component" -> "loading"
            "Error Component" -> "error"
            "Not Found Component" -> "not-found"
            "API Route" -> "route"
            "Proxy" -> "proxy"
            "Server Action" -> "actions"
            else -> title.lowercase().replace(" ", "-")
        }
}

object NextTemplateGenerator {

    val templates = listOf(
        TemplateDefinition(
            title = "Page Component",
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
            """
        ),
        TemplateDefinition(
            title = "Layout Component",
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
            """
        ),
        TemplateDefinition(
            title = "Loading Component",
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
            """
        ),
        TemplateDefinition(
            title = "Error Component",
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
            """
        ),
        TemplateDefinition(
            title = "Not Found Component",
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
            """
        ),
        TemplateDefinition(
            title = "API Route",
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
            """
        ),
        TemplateDefinition(
            title = "Proxy",
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
            """
        ),
        TemplateDefinition(
            title = "Server Action",
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
            """
        )
    )
}
