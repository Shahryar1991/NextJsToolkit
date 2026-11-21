# Next.js Toolkit

A WebStorm 2026.2 plugin starter that adds a **Next.js** submenu to:

- Project View context menu
- Editor context menu

The submenu contains TypeScript and JavaScript template groups.

## Requirements

- WebStorm 2026.2 or newer
- IntelliJ IDEA 2026.2 or newer
- JDK 21
- Gradle 9.0 or newer
- Internet access for the first Gradle sync (to resolve the IDE and plugin dependencies)

## Build and run

Open this folder in IntelliJ IDEA with the Gradle and Plugin DevKit plugins enabled.

Then run:

```bash
./gradlew runIde
```

On Windows:

```powershell
.\gradlew.bat runIde
```

If the project does not include a Gradle wrapper yet, create one using an installed Gradle 9.x:

```bash
gradle wrapper --gradle-version 9.0.0
```

Build the installable plugin ZIP:

```bash
./gradlew buildPlugin
```

The ZIP is generated under:

```text
build/distributions/
```

In WebStorm, install it from:

`Settings → Plugins → Gear icon → Install Plugin from Disk...`

## Menu

Right-click a folder in the Project tool window, or right-click in an editor:

`New → Next.js → TypeScript / JavaScript`

If you right-click a file, the generated file is placed beside that file.

## Included templates

- Page Component
- Layout Component
- Loading Component
- Error Component
- Not Found Component
- API Route
- Proxy
- Server Action

## Notes

- The Proxy template follows the Next.js 16 `proxy` convention. Place `proxy.ts` / `proxy.js` at the project root (or under `src`, depending on the project structure), not inside an arbitrary route segment.
- Templates are intentionally minimal starter code. Add project-specific conventions, imports, validation, and error handling as needed.
- The plugin currently refuses to overwrite an existing file.
- `vendor` email in `plugin.xml` is a placeholder; replace it before publishing.
