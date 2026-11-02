# TODOS

## Referidos (P3)
- **What:** promo de 2 referidos que se cortan = 1 corte gratis.
- **Why:** tercera promo del barbero.
- **Context:** diferido en CEO review (D6=A). Definir antes "referido válido". Reutiliza el ledger de `PromoEngine`.
- **Depends on:** motor de saldo (T2).

## Offline completo con Room (P2)
- **What:** Room como fuente local y sincronización propia con Firestore.
- **Why:** el usuario quiere operar sin internet; Firestore ya cachea, pero Room da control propio.
- **Context:** pedido por el usuario como "otra etapa". El repositorio ya vive detrás de un `DataSource` en `:domain` para que Room sea otra implementación.
- **Depends on:** v1 estable y ledger con ids fijos (D3=A).

## Turnos solapados (P3)
- **What:** definir qué pasa si el barbero agenda dos turnos a la misma hora (avisar, permitir o bloquear).
- **Why:** evita cruces de agenda.
- **Context:** diferido por el usuario para no añadir complejidad a la v1. En v1 no se valida el solape.
- **Depends on:** agenda básica funcionando.

## Accesibilidad (P2)
- **What:** lector de pantalla (TalkBack), texto escalable al 200 %, contraste mínimo 4.5:1, descripciones de botones.
- **Why:** diseño completo; hoy hay dos colores del boceto bajo el contraste mínimo (#8A919C y #0E7A4E).
- **Context:** diferido por el usuario para no añadir complejidad a la v1. Subir el puntaje de la Pasada 6 de 3/10.
- **Depends on:** pantallas de la v1 construidas.
