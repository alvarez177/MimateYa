# MimateYa (Android)

App Android para que un barbero gestione su agenda y sus promociones (tiquetera y paquete prepago). Proyecto de aprendizaje con un cliente real. Costo objetivo: ~$0.

## Idioma
- **Toda la comunicación con el usuario va en español.**
- **El código va en inglés**: nombres de clases, funciones, variables, ramas y mensajes de commit. Los textos que ve el barbero en la app son en español y viven en recursos (`strings.xml`).

## Contexto obligatorio (leer antes de planear o implementar)
@docs/EJECUCION.md
@docs/designs/mimateya-v1-plan.md
@docs/designs/mimateya-barber-agenda.md
@TODOS.md

Mockups oficiales (HTML con 12 pantallas, modo claro y oscuro): `docs/designs/mockups/v2.html` y capturas `v2-claro.png`, `v2-oscuro.png`.

## Cómo retomar una sesión
1. Leer `docs/EJECUCION.md`. Si hay un paso `en progreso`, retomar ese: revisar `git status`, la rama actual y lo que ya está hecho antes de seguir.
2. Si no hay ninguno, el siguiente es el primero `sin empezar` cuyas dependencias estén `terminado`.
3. Estados permitidos: `sin empezar`, `en progreso`, `terminado`. Actualizar el estado en `docs/EJECUCION.md` al empezar y al terminar cada paso. Nunca más de un paso `en progreso`.

## Decisiones ya tomadas (no reabrir sin evidencia nueva)
- Arquitectura: Clean Architecture, MVI, Hilt. Módulos por feature (`:feature:agenda`, `:feature:clients`, `:feature:promos`) más `:core` y `:app`; dentro de cada feature, paquetes `domain`, `data` y `presentation`.
- Flujo de capas (preferencia del usuario): `ViewModel` → `UseCase` → `Repository` → `DataSource`. La interfaz del repositorio vive en `domain`; su implementación y los `DataSource` (Firestore hoy, Room después) viven en `data`. El `DataSource` existe para probar repositorios con MockK sin Firebase y para separar fuente remota y local. **Siempre hay un caso de uso** entre el ViewModel y el repositorio, aunque solo reenvíe la llamada, para mantener una estructura uniforme (decisión del usuario; no preguntar). Los casos de uso **siempre terminan en `UseCase`** (por ejemplo `CompleteCutUseCase`), para no confundirlos con modelos como `CompletedCut`.
- Backend: Firebase (Auth con email y contraseña, Firestore) con tres ambientes: dev, QA y prod.
- Descuento único: el historial de cortes completados (`CompletedCut`) es la única fuente de verdad, con ids fijos (turno + tipo); el saldo lo calcula `PromoEngine`. Revertir (completado por error) **borra** el corte completado y devuelve el turno a "programado", juntos en una sola escritura; no se guarda historial de reversas (decisión del usuario, 2026-11-03).
- Concurrencia: corrutinas y `Flow` (preferencia del usuario). Casos de uso como funciones `suspend`; estado de pantalla en `StateFlow` leído con `collectAsStateWithLifecycle()`; `viewModelScope` en los ViewModels; despachadores (`Dispatchers`) inyectados con Hilt, nunca escritos dentro del código; pruebas con `runTest` de `kotlinx-coroutines-test`. Las dependencias de corrutinas se agregan cuando el paso que las usa las necesite.
- Manejo de errores claro y organizado: `AppError`, `AppResult` y un solo mapeador de excepciones de Firebase.
- Pruebas: solo unitarias con MockK (repositorios, dominio y ViewModels), nada que toque Firebase, y TDD: primero los comportamientos de negocio y sus pruebas, después el código.
- Pruebas explícitas: no usar comodines de MockK como `any()` salvo que sea el único camino; verificar con los valores concretos esperados, y para "nunca se llamó" usar `verify { mock wasNot Called }`.
- Nombres de pruebas unitarias: estilo `given ... when ... then ...` con backticks, legibles como una frase, por ejemplo `` `given wrong password when the user tries to login then should fail` ``.
- Diseño: tokens, tipografía local y pantallas según los mockups oficiales.
- Fuera de la v1: referidos, importar datos, Room y sincronización propia, turnos solapados, accesibilidad, reserva por parte del cliente, iOS.

## Forma de trabajo con git
- Cada feature parte de `develop` y vive en una rama `feature/<especificación>`; una rama por cada cosa a implementar.
- **Nunca hacer commit sin autorización explícita del usuario.**
- Los commits van a nombre del usuario (identidad de git configurada). **Sin** línea `Co-Authored-By` ni mención de Claude.

## Comandos
- Ambientes (variantes de `:app`): `dev` (`.dev`), `qa` (`.qa`) y `prod` (sin sufijo); se instalan a la vez y cada una muestra su nombre.
- Pruebas unitarias: `./gradlew testDebugUnitTest testDevDebugUnitTest` (los módulos de librería usan `testDebugUnitTest`; `:app`, por tener variantes, usa `testDevDebugUnitTest`)
- Compilar un ambiente: `./gradlew assembleDevDebug` (o `assembleQaDebug`, `assembleProdDebug`)
