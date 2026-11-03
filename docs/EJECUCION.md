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

**Paso en curso:** ninguno (Paso 3 a la espera de integrar; siguiente: Paso 4 `feature/promo-use-cases`)

## Pasos

| # | Rama | Objetivo | Depende de | Estado |
|---|------|----------|------------|--------|
| 1 | `feature/project-setup` | Gradle modular (`:app`, `:core`, `:feature:agenda`, `:feature:clients`, `:feature:promos`), Hilt, MockK, versiones al día, ambientes dev/QA/prod con icono y nombre propios; la app compila y abre | — | terminado (integrado en `develop`, PR #1) |
| 2 | `feature/core-errors` | `AppError`, `AppResult` con `resolve`, `ErrorMapper`, `appResultOf`, `ErrorMessageMapper` en español, con pruebas primero | 1 | terminado (integrado en `develop`, PR #2) |
| 3 | `feature/promo-engine` | Tabla de comportamientos, modelos de dominio y `PromoEngine` en Kotlin puro (tests primero) | 2 | terminado (PR hacia `develop`, a la espera de integrar) |
| 4 | `feature/promo-use-cases` | `CompletedCutRepository`, `CompleteCutUseCase` (id fijo, idempotente), `RevertCutUseCase` (borra el corte completado) como funciones `suspend`; añade `kotlinx-coroutines` y `kotlinx-coroutines-test` (`runTest`) y los despachadores inyectables; tests con MockK primero | 3 | sin empezar |
| 5 | `feature/design-system` | Tema claro y oscuro, tokens aprobados, fuentes locales en `res/font` | 1 | sin empezar |
| 6 | `feature/firebase-setup` | Tres ambientes (dev, QA, prod) con flavors, Firestore y Auth, `DataSource` propio con `suspend`/`Flow` (añade `kotlinx-coroutines-play-services` y `callbackFlow`) y mapeo de excepciones | 2 | sin empezar |
| 6b | `feature/app-distribution` | Firebase App Distribution: subir las variantes dev, QA y prod desde Gradle y descargarlas desde la app Firebase App Tester; grupo de testers por ambiente. También crea la llave de firma (keystore) de prod, fuera del repo y con copia de seguridad tuya, y configura la firma de la variante prod release. Pasos manuales tuyos: iniciar sesión en Firebase CLI, crear el grupo de testers y guardar la llave y su contraseña | 6 | sin empezar |
| 7 | `feature/auth` | Registro (con nombre de la barbería) e inicio de sesión con email y contraseña, MVI, navegación inicial | 5, 6 | sin empezar |
| 8 | `feature/clients` | Crear y buscar clientes por teléfono, ficha con saldo e historial de cortes, "Revertir" | 4, 6 | sin empezar |
| 9 | `feature/agenda-data` | Modelo de turno, repositorio y casos de uso, zona horaria UTC a Bogotá, tests primero | 6, 8 | sin empezar |
| 10 | `feature/agenda-ui` | Agenda de hoy, "Nuevo turno", botón flotante, estados cargando/vacío/error | 7, 9 | sin empezar |
| 11 | `feature/complete-cut` | Completar corte (guarda un `CompletedCut`), animación, "Deshacer", aviso de corte gratis; revertir borra el cobro y devuelve el turno a "programado" en una sola escritura | 4, 10 | sin empezar |
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
| 2026-11-02 | 1 | Pull request #1 integrado en `develop` (squash, `d4841c0`); rama local y remota borradas |
| 2026-11-02 | 2 | Rama `feature/core-errors` creada desde `develop`; paso 2 en progreso: primero la tabla de comportamientos, a la espera de tu aprobación |
| 2026-11-02 | 2 | Tabla de comportamientos aprobada (sin sesión expirada). Pruebas primero (rojo) y luego código (verde): 18 pruebas en `:core`, proyecto completo compila; HTML en `docs/aprendizaje/02-core-errors.html`; falta tu revisión y autorización del commit |
| 2026-11-03 | 2 | Se quitó `AppResult.fold` (un `when` hace lo mismo). `AppResult.mapSuccess` (renombrado desde `map` para no confundirlo con el `map` de colecciones y `Flow`) se conserva por decisión del usuario; **revisar al terminar el Paso 9** si se usó en código real (por ejemplo en un `Flow<AppResult<...>>`) y quitarlo si no |
| 2026-11-03 | 2 | Se agregó `AppResult.resolve(onSuccess, onFailure)` (equivalente a `fold`, con nombre elegido por el usuario) con sus 2 pruebas primero; 18 pruebas en verde |
| 2026-11-03 | 2 | Decisión final del usuario: `AppResult` tiene **solo** `resolve(onSuccess, onFailure)`; se quitó `mapSuccess` y sus 2 pruebas (ya no aplica la revisión del Paso 9). 16 pruebas en verde |
| 2026-11-03 | 2 | Corrección: `resolve` tiene la forma del `map` original (una lambda `transform`, transforma solo el éxito, el fallo pasa intacto); ya no tiene dos lambdas. 16 pruebas en verde |
| 2026-11-03 | 2 | Pruebas sin `any()` (`wasNot Called`). Flujo de capas ViewModel → UseCase → Repository → DataSource registrado en `CLAUDE.md`. Commit y pull request hacia `develop` creados; paso 2 terminado, a la espera de que el usuario integre |
| 2026-11-03 | 2 | Pull request #2 integrado en `develop` (squash, `ef2c14b`); ramas borradas |
| 2026-11-03 | 3 | Decisión: siempre un caso de uso entre ViewModel y repositorio. Rama `feature/promo-engine` creada; paso 3 en progreso: tabla de comportamientos a la espera de aprobación |
| 2026-11-03 | 3 | Tabla aprobada (`docs/comportamientos-promos.md`; un cliente solo tiene una promo a la vez). Pruebas primero (rojo) y luego `PromoEngine` y modelos (verde): 23 pruebas en `:feature:promos`, 39 en el proyecto. Mutaciones manuales: las 3 reglas clave quedan protegidas (una prueba agregada). HTML en `docs/aprendizaje/03-promo-engine.html`. Pendiente para el Paso 13: cómo cambiar de tiquetera a paquete. Falta tu revisión y autorización del commit |
| 2026-11-03 | 3 | Decisión del usuario: revertir (completado por error) **borra** el movimiento del cobro y devuelve el turno a "programado"; no se guarda historial de reversas. Se quitó `CutReverted`: `LedgerEntry` es `(id, charge)`. Pruebas primero (rojo) y luego código (verde): 21 pruebas en `:feature:promos`, 37 en el proyecto. Tabla, `CLAUDE.md` y HTML actualizados. Falta tu revisión y autorización del commit |
| 2026-11-03 | 3 | Renombrado `LedgerEntry` → `CompletedCut` (más claro que el término contable "ledger"); `PunchCard` se queda. En el Paso 4 el repositorio se llamará `CompletedCutRepository`. 21 pruebas en verde |
| 2026-11-03 | 3 | Convención del usuario: todo caso de uso termina en `UseCase` (registrada en `CLAUDE.md`) |
| 2026-11-03 | 3 | HTML extra `docs/aprendizaje/03b-motor-vs-caso-de-uso.html`. Pruebas en verde (21 en `:feature:promos`, 16 en `:core`, 1 en `:app`) y los tres ambientes compilan. Commit y pull request hacia `develop` creados; paso 3 terminado, a la espera de que el usuario integre |
