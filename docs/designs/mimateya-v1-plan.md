# MimateYa v1 — Plan revisado (CEO review, SCOPE REDUCTION, profundidad: estrategia)

Fuente: `docs/designs/mimateya-barber-agenda.md` (APPROVED). Rama: develop. Fecha: 2026-11-01.
Profundidad: **strategy-only** (capacidades, no contratos de métodos). Los dueños de implementación deben probar cada "must prove".

## Decision ledger

| ID / owner | Contrato y evidencia | Current | Proposed | Status | Aprobación exacta |
|---|---|---|---|---|---|
| D2 premisas / usuario | 5 premisas (solo-barbero, Android, sin app cliente, promos como reglas, Firebase, éxito=2 semanas) | Aceptadas | — | approved | Respuesta D2 = A |
| D3 enfoque / usuario | Android + Firebase (Auth+Firestore), `businessId` | B | — | approved | Respuesta D3 = B |
| D5 modo | SCOPE REDUCTION | SR | — | approved | Respuesta D5 = A |
| SR1 referidos | Reglas de "referido válido" indefinidas (doc, Open Questions) | Fuera de v1 | — | approved (deferred) | Respuesta D6 = A |
| SR2 importar Google Doc | Formato desconocido, no visto | Fuera de v1; carga manual desde cero | — | approved (deferred) | Instrucción directa del usuario: "no importar nada, que empiece desde 0 y si necesita ingresar los datos manuales en la app, que lo haga" |
| V1-AUTH método de login | Instrucción del usuario | Registro manual con email y contraseña (Firebase Auth) | — | approved | Usuario: "que se registre manualmente con email y contraseña" |
| V1-ENV ambientes | Instrucción del usuario | Tres ambientes: dev, QA, prod (proyectos Firebase separados) | — | approved | Usuario: "la app tendrá su ambiente de desarrollo, qa y prod" |
| V1-RULES reglas de seguridad | Riesgo S3 | Reglas abiertas aceptadas solo en dev/QA con datos de prueba; prod con reglas por negocio antes de datos reales | — | approved (riesgo aceptado, acotado a dev/QA) | Usuario: "de momento no importa que queden abiertos, esto será a modo prueba primero"; la condición de prod es mía y no se ha contradicho |
| V1-ONCE completar corte | Requisito del usuario | Tocar completar descuenta exactamente una vez | — | approved (requisito) | Usuario: "se completa el corte y hace su único descuento"; mecanismo y test los prueba T2 |
| V1-PROMO-SEM semántica de "corte completado" | Descuento al marcar completado: aprobado. Corrección de errores: sin definir | Descuento al completar | Reversa con movimiento inverso | approved | Usuario: "si le da completar por error, sí darle una opción de revertir la operación de completar". Mecanismo (movimiento inverso en el historial) lo prueba T2 |
| V1-GRATIS N+1 regla exacta de tiquetera | Contador por cliente; al llegar a N el siguiente corte es gratis | El corte gratis no expira hasta que el cliente lo use | — | approved | Usuario: "el corte gratis no expira hasta que el cliente lo use". Usuario confirmó que se agenda como turno normal y que a ese turno se le muestra al barbero un aviso de que es gratis por el beneficio ("sí quedó bien tu supuesto") |

**Approval readiness: PASS** para D2, D3, D5, SR1, SR2, V1-AUTH, V1-ENV, V1-RULES, V1-ONCE, V1-PROMO-SEM, V1-GRATIS (cada uno con su respuesta citada arriba).

## Alcance v1 (aprobado)
Login del barbero · clientes identificados por teléfono (con saldo inicial manual) · agenda del día (crear/mover/cancelar/completar turno) · promos tiquetera y paquete prepago (mismo motor de saldo) · historial de movimientos auditable · Firebase con offline · `businessId` en todos los datos.

## NOT in scope
- **Referidos** — diferido (D6=A): reglas indefinidas; va a TODOS.md.
- **Importar Google Doc** — rechazado para v1 por el usuario (D7): todo se carga a mano desde cero. No va a TODOS.
- Reserva por el cliente (app/link web), iOS, otros negocios/spas, publicidad, cobros, notificaciones, multi-barbero: ya fuera de v1 por premisas D2.

## What already exists
Solo la plantilla Compose (`app/src/main/java/com/salvarez/mimateyaandroid/MainActivity.kt`, tema en `ui/theme/`). `minSdk=26` (`app/build.gradle.kts:14`). Sin Firebase, sin navegación, sin tests reales. Se reutiliza: tema Material3 y estructura de módulo único.

## Dream state delta
HOY plantilla vacía -> v1 app del barbero (agenda + 2 promos, nube) -> 12 meses: clientes reservan, referidos, iOS, otros negocios. v1 deja el modelo (`businessId`, promos como reglas, movimientos) que habilita todo eso sin reescribir.

## Revisión por secciones

### S1 Arquitectura — 3 hallazgos
```
 [App Android: Compose UI] -> [ViewModels] -> [Repositorio] -> [Firestore SDK (cache offline)]
                                                   |-> [Firebase Auth]
 Firestore: businesses/{businessId}/{clients, appointments, promoPlans, promoBalances, ledger}
```
- WARNING: el **saldo** es dato derivado; guardarlo solo como contador es frágil. Must prove: saldo = suma del `ledger` (o contador actualizado en la misma transacción que el movimiento).
- WARNING: escrituras concurrentes (dos dispositivos, o doble toque) pueden descontar dos veces. Must prove: transacción Firestore + id de movimiento idempotente por turno.
- OK: punto único de falla = Firebase/Google; riesgo aceptado en D3. Rollback = reinstalar APK anterior; datos en nube intactos.

### S2 Errores y rescate (capacidades)
| Capacidad | Falla | Rescate | Usuario ve | Estado |
|---|---|---|---|---|
| Completar corte (descuento) | Sin red / escritura rechazada | Cola offline de Firestore + indicador "pendiente de sincronizar" | Estado visible, no pérdida | must prove |
| Completar corte | Doble toque | Idempotencia por turno | Un solo descuento | **GAP hasta probar** |
| Login | Token expirado / sin red | Reintento + mensaje | Pantalla de reingreso | must prove |
| Reglas de seguridad | Mal escritas: abiertas o bloqueando todo | Reglas por `businessId` + test con emulador | — | must prove |
No usar catch-all; registrar qué operación, cliente y turno fallaron.

### S3 Seguridad
- Datos personales de clientes (nombre, teléfono): clasificarlos como PII. Impacto Medio, probabilidad Media.
- Autorización: cada documento bajo `businessId`; las reglas deben verificar que `request.auth.uid` es dueño del negocio. Riesgo aceptado por el usuario solo en dev/QA con datos de prueba (V1-RULES). Condición: reglas por negocio desplegadas en prod antes de datos reales de clientes; el modo de prueba de Firestore además expira a los 30 días.
- Secretos: `google-services.json` fuera de git público si el repo es público (`.gitignore` actual debe revisarse).
- Auditoría: el `ledger` es el registro de operaciones sensibles (saldos).

### S4 Flujo de datos y bordes
Flujo: `Marcar completado -> validar turno y cliente -> transacción(ledger + saldo) -> UI`. Sombras: cliente sin promo (corte normal), saldo 0, saldo negativo (prohibido), turno ya completado (rechazar), cancelar turno completado (reversa con movimiento inverso), teléfono duplicado (clave única por negocio), nombre con tildes/emoji.
| Interacción | Borde | ¿Manejado? |
|---|---|---|
| Completar turno | doble toque / stale | pendiente (V1-PROMO-SEM) |
| Agenda | dos turnos misma hora | pendiente: decidir si se permite solapar |
| Zona horaria / cambio de día | turno cerca de medianoche | must prove (guardar UTC, mostrar local Colombia) |

### S5 Calidad de código
Riesgo de sobre-ingeniería: motor de promos "genérico" desde el día 1. Mantener solo dos tipos (tiquetera, paquete) detrás de una interfaz pequeña; no construir un DSL de reglas.

### S6 Pruebas
Diagrama de cobertura: [Motor de saldo] unit (descuento, saldo 0, N+1, reversa) · [Reglas Firestore] emulador · [Agenda] unit + UI de Compose · [Sync offline] manual en dispositivo real.
Test de las 2 a.m. del viernes: "completar corte dos veces rápido deja un solo descuento". Test hostil: teléfonos duplicados y reversas encadenadas. No hay tests hoy salvo los de ejemplo.

### S7 Rendimiento
~10 clientes/día: sin problema de escala. Vigilar lecturas Firestore del plan gratis (50k lecturas/día): cargar solo el día visible de la agenda, no toda la colección.

### S8 Observabilidad
Firebase Crashlytics (gratis) para fallos; log estructurado por operación de saldo. Sin dashboards en v1. Si hay un reclamo "me debían un corte", el `ledger` permite reconstruir qué pasó: ese es el requisito de depuración.

### S9 Despliegue
Distribución: APK directo o prueba interna de Play Console. Rollback: reinstalar versión anterior; no hay migraciones destructivas si el esquema solo agrega campos. Verificación tras instalar: login, crear cliente, crear turno, completar corte, ver saldo bajar, repetir sin red.

### S10 Trayectoria
Reversibilidad 3/5 (Firestore atado a Google; el modelo con `businessId` y repositorio aislado facilita migrar). Deuda: sin tests de reglas ni CI al inicio. Un nuevo ingeniero lo entiende si el `ledger` y el repositorio están documentados.

### S11 Diseño/UX (hay UI)
Estados por pantalla (agenda, cliente, promo): cargando, vacío ("aún no hay turnos hoy"), error, éxito, parcial (pendiente de sincronizar). Foco: el barbero lo usa de pie, con una mano, entre cortes: acciones de un toque, letra grande, "completar corte" siempre visible. Requisito aprobado: un turno gratis por beneficio muestra al barbero un aviso visible (ej. "Corte gratis por tiquetera") en la agenda y al completarlo. Recomendación: /plan-design-review antes de implementar pantallas.

## Error & Rescue Registry y Failure Modes
| Capacidad | Falla | ¿Rescate? | ¿Test? | Usuario ve | ¿Log? |
|---|---|---|---|---|---|
| Completar corte | doble toque | unknown | unknown | unknown | unknown |
| Completar corte | sin red | sí (cola Firestore) | unknown | indicador | unknown |
| Reglas Firestore | abiertas/bloqueadas | unknown | unknown | silencioso si abiertas | N |
| Login | token expirado | unknown | unknown | unknown | unknown |
CRITICAL GAPS: 0. Doble descuento: requisito aprobado (V1-ONCE), prueba en T2. Reglas abiertas: riesgo aceptado en dev/QA (V1-RULES), condición de prod en T1. Dueño: implementación.

## Diagramas producidos
Arquitectura (S1), flujo de datos (S4), flujo de errores (S2). Estado de turno: `programado -> completado | cancelado | no-show`; `completado -> reversado` solo con movimiento inverso. Despliegue y rollback: descritos en S9.
Stale diagrams: ninguno (repo sin diagramas).

## Voz externa
Codex no instalado; revisión nativa no ejecutada (no se lanzaron subagentes sin pedido). Cobertura externa: **no disponible**.

## Implementation Tasks
- [ ] **T1 (P1, human: ~1d / CC: ~30min)** — Firebase — Tres proyectos (dev, QA, prod), Auth por email/clave; reglas por `businessId` obligatorias en prod (opcionales en dev/QA)
  - Surfaced by: S3 — reglas de seguridad
  - Files: to be determined
  - Verify: test del emulador rechaza acceso cruzado
- [ ] **T2 (P1, human: ~1d / CC: ~30min)** — Promos — Motor de saldo con ledger e idempotencia por turno
  - Surfaced by: S1/S2/S4 — doble descuento
  - Files: to be determined
  - Verify: unit test doble toque = un movimiento
- [ ] **T3 (P2, human: ~2h / CC: ~10min)** — Mostrar al barbero la semántica aprobada (completar/revertir, gratis sin expiración, gratis como turno normal con aviso)
  - Surfaced by: Ledger — decisiones pendientes
  - Verify: respuestas escritas del barbero
- [ ] **T4 (P2, human: ~1h / CC: ~10min)** — Revisar `.gitignore` por `google-services.json` y decidir repo público/privado
  - Surfaced by: S3 — secretos
  - Files: `.gitignore`

## TODOS propuestos
- **Referidos (P3)**: promo de 2 referidos = 1 corte gratis. Why: tercera promo del barbero. Depende de: definir "referido válido" y del motor de saldo (T2). Esfuerzo: human M / CC S. Decisión D6=A (diferido).

## Completion Summary
Mode: SCOPE REDUCTION · Sections 1–11 revisadas · Unresolved: 0 · Critical gaps: 0 · Lake Score: N/A (todas las preguntas fueron de tipo no puntuado) · Outside voice: unavailable · Reality rows: premise-table y numbers con hallazgos; `unverified=3`.

## Eng review: scope record (plan-eng-review, 2026-11-01)
Target: este plan (`docs/designs/mimateya-v1-plan.md`). Scope Challenge result: `scope accepted as-is` (el CEO review ya recortó referidos e import).
feature answers: none proposed; structure: D2 = modularización por feature con paquetes domain/data/presentation dentro de cada feature (respuesta libre del usuario: "digo que por features y dentro de cada features los packages de domain data y presentation"); accepted scope: Clean Architecture, MVI, Hilt (preferencias aprobadas por el usuario), módulos por feature; Room y offline completo quedan para una etapa posterior (el usuario lo pidió así); pending remedies: R1 mecanismo del descuento único (T2), R2 reglas de seguridad de prod (T1), R3 zona horaria, R4 pruebas.

## Eng review: decision ledger (plan-eng-review)

| ID | Contrato y evidencia | Current | Status | Aprobación exacta |
|---|---|---|---|---|
| E-PREF arquitectura | Preferencias del usuario | Clean Architecture, MVI, Hilt; Room en etapa posterior | approved | Mensaje del usuario ("me voy con arquitectura limpia... MVI... hilt... room") |
| D2 modularización | `libs.versions.toml` sin módulos | Módulos por feature; dentro de cada uno paquetes domain/data/presentation; `:core` compartido | approved | Respuesta libre a D2: "por features y dentro de cada features los packages de domain data y presentation" |
| D3 / R1 descuento único | Plan CEO S1 (transacción vs ledger) | Ledger como única fuente, id fijo (turno+tipo), batch; saldo lo calcula `PromoEngine`; reversa = movimiento inverso | approved | Respuesta D3 = A |
| D4 / R2 frontera | Completar toca agenda, clientes, promos | `CompleteCutUseCase` como interfaz en `:core`, implementada en `:feature:promos`, cableada por Hilt en `:app` | approved | Respuesta D4 = A |
| T-STRAT pruebas | Instrucción del usuario | Solo pruebas unitarias con MockK (repositorio, dominio, ViewModels); nada con Firebase; TDD (comportamientos y pruebas primero) | approved | Mensaje del usuario ("nada que involucre firebase... mockk... TDD") |
| R4 reglas prod | V1-RULES (CEO) | Verificación manual con lista antes de datos reales en prod; sin emulador | approved | Consecuencia de T-STRAT; D5 no se respondió y queda sin efecto |
| E-ERR manejo de errores | Preferencia del usuario | Manejo de errores claro y organizado (AppError, AppResult, un solo mapeador); ver adenda | approved | Mensaje del usuario ("me gusta mucho tener de forma clara y organizada el manejo de errores") |
| R5 turnos solapados | CEO S4 "pendiente" | Fuera de v1: sin política ni validación de solapes; se tratará después | deferred | Usuario: "no le demos más mente a lo de turnos solapados, luego le damos énfasis a esa parte, para no meter tanta complejidad a la V1" |

**Approval readiness: PASS** para E-PREF, D2, D3, D4, T-STRAT, R4. R5 es un diferimiento de alcance aprobado por el usuario.

## Secciones de la revisión de ingeniería

**S1 Arquitectura.** 3 hallazgos, ya resueltos: transacciones no sirven offline (`[CRITICAL] 9/10`, D3), acoplamiento agenda-promos (`[CRITICAL] 8/10`, D4), dependencias viejas `lifecycle-runtime-ktx 2.6.1` y `activity-compose 1.8.0` (`libs.versions.toml:6,7`, `[INFORMATIONAL] 8/10`). Diagrama:
```
 :app (Hilt, navegación) -> :feature:{agenda,clients,promos} (domain|data|presentation MVI) -> :core (modelos, interfaces)
 Completar: Intent -> agenda.presentation -> CompleteCutUseCase(:core) -> promos.domain -> DataSource(interfaz) -> Firestore(batch, id fijo)
```
**S2 Calidad.** Casos de uso solo con lógica (CompleteCut, RevertCut, gratis N+1); `domain` sin Android ni Firebase; repositorio recibe un `DataSource` propio para poder usar MockK sin Firestore. 3 informativos.

**S3 Pruebas (TDD).** Orden obligatorio: primero la tabla de comportamientos, luego la prueba que falla, luego el código.
| Comportamiento de negocio | Prueba (MockK/JVM) | Falla si |
|---|---|---|
| Tiquetera: tras N cortes el N+1 es gratis y no expira | `PromoEngineTest` | la regla o la expiración cambian |
| Paquete: cada corte baja el saldo; en 0 se rechaza | `PromoEngineTest` | permite saldo negativo |
| Completar dos veces el mismo turno = una entrada | `CompleteCutTest` (DataSource mockeado) | el id deja de ser determinista |
| Revertir devuelve el saldo con movimiento inverso | `RevertCutTest` | borra en vez de revertir |
| Turno gratis expone `isFreeByBenefit` y su mensaje | `AgendaViewModelTest` (estado) | el aviso no aparece |
| Zona horaria: UTC guardado, Bogotá mostrado, 23:30 | `TimeMapperTest` | cambia de día |
| Registro y login: email inválido, clave débil, error de red | `AuthViewModelTest` | el error no llega al estado |
Cobertura propuesta: 0/17 caminos probados (no hay código). Gap aceptado: la vista en pantalla y las reglas de prod se verifican a mano; `no_claim`: ninguna prueba toca Firebase real.

**S4 Rendimiento.** `[INFORMATIONAL] 8/10`: agenda por día con rango sobre `startUtc` bajo `businesses/{id}/appointments` (sin índice compuesto); movimientos por cliente, decenas de documentos `[estimated]`; el plan gratis permite 50.000 lecturas/día frente a ~10 clientes/día. Escuchar solo el día visible. Sin N+1: el saldo se calcula en memoria sobre una lectura.

## Voz externa
Codex no está instalado y no lancé subagentes sin que lo pidieras. Voz externa **no disponible** (`OUTSIDE_VOICE_UNAVAILABLE`); no cuenta como revisión limpia.

## NOT in scope
Referidos, Room/sincronización offline propia y turnos solapados (TODOS.md); pruebas con Firebase o emulador (rechazado por el usuario); pruebas de UI con Compose (rechazado para v1, se revisa a mano); import de Google Doc (rechazado en CEO).

## What already exists
Plantilla Compose y tema (`ui/theme/`). Nada más se reutiliza.

## Failure modes
| Camino | Falla | ¿Rescate? | ¿Prueba? | Usuario ve |
|---|---|---|---|---|
| Completar corte | doble toque | sí (id fijo) | `CompleteCutTest` | un descuento |
| Completar sin red | escritura pendiente | sí (cola de Firestore) | no (a mano) | "pendiente de sincronizar" |
| Reglas prod | abiertas | lista manual | no (a mano) | silencioso si se olvida: **aceptado y acotado** |
CRITICAL GAPS: 0 (reglas abiertas en dev/QA aceptadas por el usuario; en prod hay lista manual obligatoria).

## Paralelización
Secuencial: `:core` y el motor de promos bloquean a los demás. Luego agenda y clientes pueden ir en paralelo.

## Implementation Tasks (TDD)
- [ ] **T1 (P1, human: ~2h / CC: ~10min)** — docs — Escribir la tabla de comportamientos del motor de promos antes de cualquier código. Verify: aprobada por el usuario.
- [ ] **T2 (P1, human: ~1d / CC: ~30min)** — `:feature:promos` — `PromoEngineTest`, `CompleteCutTest`, `RevertCutTest` primero, luego el código. Verify: `./gradlew testDebugUnitTest`.
- [ ] **T3 (P1, human: ~1d / CC: ~30min)** — Gradle — Módulos `:app`, `:core`, `:feature:*`, Hilt, MockK, versiones al día. Verify: `./gradlew assembleDebug`.
- [ ] **T4 (P1, human: ~1d / CC: ~30min)** — Firebase — tres proyectos dev/QA/prod, Auth email/clave; lista manual de reglas para prod.
- [ ] **T5 (P2, human: ~4h / CC: ~15min)** — agenda — `AgendaViewModelTest` (aviso gratis) y `TimeMapperTest`.
- [ ] **T6 (P2, human: ~2h / CC: ~10min)** — barbero — validar con él los comportamientos aprobados (completar/revertir, gratis sin expiración, aviso).

## Completion Summary
Eng review: scope accepted as-is · 8 hallazgos (2 críticos resueltos) · Unresolved: 0 (R5 diferido) · Critical gaps: 0 · Outside voice: unavailable · Reality rows: premise-table y numbers con hallazgos, `unverified=2`.

## Eng review: adenda de arquitectura (manejo de errores)
Preferencia del usuario (aprobada): manejo de errores **claro y organizado** en todo el código. Forma propuesta, a validar con pruebas en T7 (no es una decisión pendiente, es el diseño de partida):
```
 data (Firebase) --excepción--> ErrorMapper (único lugar con try/catch) --> AppError (tipado)
 domain: use case devuelve AppResult<T> (Success | Failure(AppError))
 presentation: ViewModel recibe AppResult --> UiState.error / Effect con mensaje en español (un solo ErrorMessageMapper)
```
- `AppError` sellada en `:core`: Network, Unauthenticated, InvalidCredentials, Validation(campo), NotFound, Conflict, Unknown(causa).
- Sin `catch (Exception)` suelto: solo el mapeador de la capa `data` captura excepciones de Firebase; lo inesperado se vuelve `Unknown` conservando la causa.
- Mensajes al usuario en recursos de texto (español), uno por tipo de error; los errores se registran sin datos personales.
- Pruebas con MockK, primero (TDD): mapeador, casos de uso y ViewModels, cada ruta de error.

## Design review (plan-design-review)

Mockups **oficiales del proyecto por decisión del usuario** (hechos a mano en HTML/CSS; el generador de imágenes no tiene cuota gratis): `~/.gstack/projects/alvarez177-MimateYa/designs/boceto-html/v2.html` (capturas `v2-claro.png`, `v2-oscuro.png`). Fuentes empaquetadas localmente.

| ID | Decisión | Estado | Aprobación |
|---|---|---|---|
| 1A navegación | Barra inferior de 3 destinos solo con texto (Agenda, Clientes, Promos), sin íconos; Agenda es inicio | approved | Usuario: "me gusta la opción A", "sin íconos en la parte inferior" |
| Perfil/Ajustes | Ícono de perfil arriba a la derecha abre pantalla Perfil (nombre de la barbería, cerrar sesión) | approved | Usuario: "está perfecto" tras ver el boceto |
| Estados | Tabla de estados (cargando, vacío, error, éxito, parcial) con animaciones cortas y reducir movimiento | approved | Usuario: "Sí, apruebo" |
| Primer arranque | Estado vacío cálido "Tu agenda está lista"; nombre de la barbería al registrarse y editable en Perfil; cliente se crea dentro de "Nuevo turno"; "Agregar otro" en Clientes | approved | Usuario: "El nombre que lo pida al inicio... y sí, apruebo" |
| Botón flotante | Única forma de crear turnos; la lista deja espacio para que no tape; huecos libres solo informativos | approved | Usuario: "que solo sea el botón flotante" |
| Tipografía | Bricolage Grotesque (títulos) y Figtree (texto), empaquetadas dentro de la app (`res/font`), sin depender de Google | approved | Usuario: "que no dependa de Google... local" |
| Colores | Los del boceto: ink #12161C, ink-2 #4A5260, paper #FAFAF8, card #FFFFFF, line #E3E5E8, accent #1D4ED8, free #0E7A4E / #DDF3E8, muted #8A919C, danger #B42318; versión oscura propuesta en el boceto | approved | Usuario: "me gustaron tal cual" |
| Modo claro y oscuro | Siguen el ajuste del sistema | approved | Usuario: "sí, manejar el modo oscuro y claro" |
| Nuevo turno | Solo cliente (buscar o crear ahí mismo) y hora; hoy por defecto | approved | Usuario: "lo menos complejo posible" y "está perfecto" |
| Promos | Crear/editar planes (tiquetera o paquete), ver clientes por plan, vender plan (registrar venta = ya pagó) | approved | Usuario: "sí súper" |
| Registro | Botón principal "Crear cuenta", divisor "¿Ya tienes cuenta?" y botón secundario "Iniciar sesión" | approved | Usuario pidió separarlos |
| Accesibilidad | Fuera de v1: lector de pantalla, escalado de texto al 200 %, etc. | deferred | Usuario: "quítale lo del TalkBack" y "quita el tema de accesibilidad... aún no" |
| Contraste | #8A919C y #0E7A4E bajo 4.5:1 según cálculo aproximado | accepted risk | Usuario eligió los colores del boceto tal cual |

## Approved Mockups

| Pantalla | Archivo | Dirección | Notas |
|---|---|---|---|
| 12 pantallas: registro, primer arranque, agenda, nuevo turno, ficha de cliente, promos, nuevo plan, detalle de plan, vender plan, completar (animación), cargando, perfil | `~/.gstack/projects/alvarez177-MimateYa/designs/boceto-html/v2.html` (capturas `v2-claro.png`, `v2-oscuro.png`) | Claro y oscuro, acento azul, verde solo para lo gratis, tipografía local | Aprobadas por el usuario como mockups oficiales. Archivos fuera del repo; copiar a `docs/designs/` si se quiere versionar |

## Forma de trabajo (decisión del usuario)
- Cada feature parte de la rama `develop` y vive en una rama `feature/<especificación>` creada por cada cosa a implementar.
- **No se hace commit sin autorización explícita del usuario.**
- Los commits van con el nombre del usuario (identidad de git del usuario); sin línea `Co-Authored-By` ni otra atribución de Claude.

## Design Review Completion Summary
```
  | System Audit         | Sin DESIGN.md; plantilla con morado por defecto (Color.kt:5-10) |
  | Step 0               | 3/10 inicial; foco en las 7 dimensiones                       |
  | Pass 1  (Info Arch)  | 3/10 -> 9/10                                                  |
  | Pass 2  (States)     | 3/10 -> 9/10                                                  |
  | Pass 3  (Journey)    | 4/10 -> 9/10                                                  |
  | Pass 4  (AI Slop)    | 7/10 -> 9/10                                                  |
  | Pass 5  (Design Sys) | 2/10 -> 8/10 (riesgo de contraste aceptado)                   |
  | Pass 6  (Responsive) | 3/10 -> 3/10 (accesibilidad diferida por el usuario)          |
  | Pass 7  (Decisions)  | 4 resueltas (perfil, nuevo turno, íconos, modo oscuro), 0 abiertas |
  | NOT in scope         | accesibilidad, íconos en la barra, tablet y horizontal        |
  | Approved Mockups     | 12 pantallas (boceto HTML v2), aprobadas por el usuario como oficiales |
  | Decisions made       | 13 agregadas al plan                                          |
  | Overall design score | 2/10 -> 3/10 (el mínimo de las pasadas calificadas es la de accesibilidad) |
```
Nota: el puntaje global es el más bajo de las seis pasadas; sube a 8+ cuando se retome accesibilidad (TODOS.md).

## Design: tareas (se suman a las de ingeniería)
- [ ] **T7 (P1, human: ~1d / CC: ~30min)** — `:core` — Modelo de errores (`AppError`, `AppResult`, mapeadores) con pruebas primero. Verify: `./gradlew testDebugUnitTest`.
- [ ] **T8 (P1, human: ~4h / CC: ~15min)** — tema — Tokens de color claro/oscuro y fuentes en `res/font`; reemplazar el morado de plantilla. Verify: ejecutar la app en ambos modos.
- [ ] **T9 (P2, human: ~2d / CC: ~1h)** — presentación — Pantallas del boceto v2 (registro, agenda, nuevo turno, ficha, promos, nuevo plan, detalle, vender, perfil) con MVI.
- [ ] **T10 (P2, human: ~2h / CC: ~10min)** — animación — Completar corte (tarjeta, check, barra, "Deshacer" 6 s) y reducir movimiento.

## GSTACK REVIEW REPORT

| Review | Trigger | Why | Runs | Status | Findings |
|--------|---------|-----|------|--------|----------|
| CEO Review | `/plan-ceo-review` | Scope & strategy | 3 | CLEAR | mode: SCOPE_REDUCTION, 0 critical gaps |
| Outside Review | codex (no instalado) | Independent 2nd opinion | 0 | unavailable | no completed external review |
| Eng Review | `/plan-eng-review` | Architecture & tests (required) | 3 | CLEAR | 9 issues, 0 critical gaps |
| Design Review | `/plan-design-review` | UI/UX gaps | 1 | ISSUES OPEN | score: 2/10 → 3/10, 13 decisions |
| DX Review | `/plan-devex-review` | Developer experience gaps | 0 | — | — |

**OUTSIDE COVERAGE:** codex, plan-review, unavailable (CLI no instalado); sin revisión externa completada. Mockups: el generador de imágenes no tuvo cuota gratis; el usuario aprobó el boceto HTML v2 como mockup oficial.
**VERDICT:** CEO + ENG CLEARED — ready to implement. Design review ISSUES OPEN: accesibilidad diferida por el usuario.

**UNRESOLVED DECISIONS:**
- Accesibilidad fuera de v1 (lector de pantalla, texto al 200 %): diferida por el usuario, se retoma después
- Contraste de #8A919C y #0E7A4E bajo 4.5:1: riesgo aceptado por el usuario
