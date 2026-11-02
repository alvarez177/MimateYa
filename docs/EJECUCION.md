# Plan de ejecución — MimateYa v1

Este archivo es la **fuente de verdad del avance**. Si la sesión se cierra o se acaban los tokens, se retoma desde aquí.

**Estados permitidos (exactamente):** `sin empezar` · `en progreso` · `terminado`

**Reglas de uso**
- Antes de empezar un paso: cambiar su estado a `en progreso` y escribir la fecha en "Registro".
- Al terminar (con su verificación aprobada): `terminado` y la fecha.
- Un paso `en progreso` al abrir la sesión significa que quedó a medias: revisar `git status` y la rama antes de seguir.
- Nunca hay más de un paso `en progreso`.
- Cada paso vive en su rama `feature/<nombre>` creada desde `develop`. No se hace commit sin autorización del usuario.
- Máximo 25 archivos por paso (los HTML de aprendizaje no cuentan). Si hace falta más para que compile o funcione, se pregunta al usuario.
- Al terminar cada paso se genera `docs/aprendizaje/NN-<paso>.html` explicando el código en español.
- Plan aprobado: `/Users/sebastianalvarez/.claude/plans/typed-munching-peach.md` (copia de las reglas y del detalle del paso 1).

**Próximo paso:** Paso 2 (`feature/core-errors`), cuando el usuario integre el pull request del Paso 1 a `develop`

## Pasos

| # | Rama | Objetivo | Depende de | Estado |
|---|------|----------|------------|--------|
| 1 | `feature/project-setup` | Gradle modular (`:app`, `:core`, `:feature:agenda`, `:feature:clients`, `:feature:promos`), Hilt, MockK, versiones al día, ambientes dev/QA/prod con icono y nombre propios; la app compila y abre | — | terminado (pull request pendiente de integrar) |
| 2 | `feature/core-errors` | `AppError`, `AppResult`, `ErrorMapper`, `ErrorMessageMapper` en español, con pruebas primero | 1 | sin empezar |
| 3 | `feature/promo-engine` | Tabla de comportamientos, modelos de dominio y `PromoEngine` en Kotlin puro (tests primero) | 2 | sin empezar |
| 4 | `feature/promo-use-cases` | `LedgerRepository`, `CompleteCut` (id fijo, idempotente), `RevertCut`; tests con MockK primero | 3 | sin empezar |
| 5 | `feature/design-system` | Tema claro y oscuro, tokens aprobados, fuentes locales en `res/font` | 1 | sin empezar |
| 6 | `feature/firebase-setup` | Tres ambientes (dev, QA, prod) con flavors, Firestore y Auth, `DataSource` propio y mapeo de excepciones | 2 | sin empezar |
| 6b | `feature/app-distribution` | Firebase App Distribution: subir las variantes dev, QA y prod desde Gradle y descargarlas desde la app Firebase App Tester; grupo de testers por ambiente. También crea la llave de firma (keystore) de prod, fuera del repo y con copia de seguridad tuya, y configura la firma de la variante prod release. Pasos manuales tuyos: iniciar sesión en Firebase CLI, crear el grupo de testers y guardar la llave y su contraseña | 6 | sin empezar |
| 7 | `feature/auth` | Registro (con nombre de la barbería) e inicio de sesión con email y contraseña, MVI, navegación inicial | 5, 6 | sin empezar |
| 8 | `feature/clients` | Crear y buscar clientes por teléfono, ficha con saldo e historial, "Revertir" | 4, 6 | sin empezar |
| 9 | `feature/agenda-data` | Modelo de turno, repositorio y casos de uso, zona horaria UTC a Bogotá, tests primero | 6, 8 | sin empezar |
| 10 | `feature/agenda-ui` | Agenda de hoy, "Nuevo turno", botón flotante, estados cargando/vacío/error | 7, 9 | sin empezar |
| 11 | `feature/complete-cut` | Completar corte con ledger, animación, "Deshacer", aviso de corte gratis | 4, 10 | sin empezar |
| 12 | `feature/promos-plans` | Pestaña Promos: planes, crear/editar tiquetera y paquete, detalle por plan | 4, 8 | sin empezar |
| 13 | `feature/promos-sell` | Vender un plan a un cliente y clientes con saldo | 12 | sin empezar |
| 14 | `feature/profile-settings` | Perfil: nombre de la barbería editable y cerrar sesión | 7 | sin empezar |
| 15 | `feature/prod-readiness` | Reglas de Firestore por negocio, lista manual de verificación en prod, **versionamiento** (versión inicial, `versionCode` calculado, etiqueta de git y `CHANGELOG.md`) y registrar en Play App Signing la misma llave usada en App Tester (para que la firma coincida con lo ya instalado) | 6, 11 | sin empezar |
| 16 | — | Validación con el barbero real | 11, 13 | sin empezar |

## Criterio de "terminado" por paso
Pruebas unitarias en verde (`./gradlew testDebugUnitTest`), la app compila (`./gradlew assembleDebug`), el HTML de aprendizaje del paso generado, el comportamiento revisado con el usuario y el usuario autorizó el commit.

## Registro
| Fecha | Paso | Nota |
|-------|------|------|
| 2026-11-01 | — | Revisiones CEO, ingeniería y diseño terminadas; planes y mockups escritos |
| 2026-11-01 | 1 | Plan nativo aprobado; rama `feature/project-setup` creada desde `develop`; paso 1 en progreso |
| 2026-11-01 | 1 | Commit de documentación hecho (`daffa0f`). Código del paso 1 escrito y verificado (build de los tres ambientes y pruebas en verde, Hilt+KSP compatible con AGP 9.4.1); HTML en `docs/aprendizaje/01-project-setup.html`; falta tu revisión y autorización del commit |
| 2026-11-02 | 1 | Iconos por ambiente añadidos; commit y pull request hacia `develop` creados; paso 1 terminado, a la espera de que el usuario integre |
