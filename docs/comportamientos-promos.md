# Comportamientos del motor de promos (aprobados)

Aprobada por el usuario el 2026-11-03. Es la fuente de las pruebas de `PromoEngine` (TDD).

**Regla general:** un cliente tiene **como máximo una promo a la vez**: una tiquetera o un paquete. El motor siempre trabaja sobre esa única promo.

**Cómo se guarda cada corte:** al completarse, cada corte queda registrado con cómo se cobró: `PAID` (pagado), `FREE_BY_PUNCH_CARD` (gratis por tiquetera) o `FROM_PACKAGE` (descontado del paquete). En el código cada uno es un `CompletedCut` (corte completado: su `id` y su cobro `charge`). Así un corte ya hecho nunca cambia de naturaleza aunque se revierta otro.

## Tiquetera (cada N cortes pagados, el siguiente es gratis)

| # | Situación (tiquetera de 10) | Resultado |
|---|---|---|
| T1 | Lleva entre 0 y 9 cortes pagados | El próximo corte es **normal**. La ficha muestra el avance, por ejemplo "4 de 10" |
| T2 | Llega a 10 cortes pagados | El próximo corte es **gratis** |
| T3 | Usa el corte gratis | El contador **vuelve a 0** y empieza a juntar otros 10 |
| T4 | Tiene el gratis pendiente y pasa el tiempo | **No vence**: sigue disponible hasta que lo use |
| T5 | El corte gratis no cuenta como pagado | Después del gratis el contador queda en 0, no en 1 |

## Paquete prepago (ej. 10 cortes por $150.000)

| # | Situación | Resultado |
|---|---|---|
| P1 | Recién vendido | Le quedan **10 cortes** |
| P2 | Cada corte completado | Le quedan 9, 8, ... |
| P3 | Le quedan 0 | El paquete está **agotado**: el próximo corte es **normal** (se cobra aparte) |

## Revertir y cálculo

**Qué es revertir** (decisión del usuario, 2026-11-03): el barbero tocó "Completar" por error, por ejemplo con el cliente todavía en la silla. Revertir **borra el corte completado** (y con él su cobro) y **devuelve el turno a "programado"**, las dos cosas juntas. No queda rastro del error: el historial solo guarda cortes que de verdad pasaron. Cuando el barbero termine, completa el turno otra vez de forma normal.

| # | Situación | Resultado |
|---|---|---|
| R1 | Se revierte un corte | Todo queda **exactamente como antes** de ese corte |
| R2 | Se revierte un corte gratis | El gratis **vuelve a estar disponible** |
| R3 | Se revierte un corte pagado viejo después de usar el gratis | El gratis **sigue como usado**: no se regala otro |
| R4 | Cómo se obtiene el saldo | Nunca se guarda: siempre se **calcula** a partir de los cortes completados, en orden |
| G1 | Cliente sin promo | El próximo corte es **normal** |

## Pendiente para el Paso 13 (vender o asignar promo)

Como un cliente solo puede tener una promo, hay que decidir cómo se cambia de tiquetera a paquete: un paquete termina solo al agotarse, pero una tiquetera nunca termina. ¿El barbero puede "finalizar" la tiquetera? ¿Qué pasa si tenía un gratis pendiente?
