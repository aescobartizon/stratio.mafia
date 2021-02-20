# stratio.mafia
Prueba de desarrollo - Test seguimiento de la Mafia

# Diseño Técnico para escalabilidad y reusabilidad de componentes
La implementación se ha realizado bajo la arquitectura de  microservicios de forma que es sistema está diseñado para un despliegue del componente para dar soporte a una organización. 

El mismo componente utiliza una factoría para implementación específica para mantener la estructura de la organización. En el momento de despliegue se puede sobrecargar la variable de entorno y especificar una implementación determinada según las características de la organización.

```
## public class MafiaHierarchyFatory implements AbstractFactory<MafiaHierarchy>

mafia.graph.implementation=Jgrapht
#mafia.graph.implementation=neo4j
#mafia.graph.implementation=sparkGraphx

```

En la versión actual la implementación real está realizada sobre Jgrapht las otras dos implementaciones está sin implementar se podría probar como funciona la factoría congiendo en runtime la implementación concreta especificada en la variable mafia.graph.implementation.

Este diseño es debido a que aunque se puede escalar teniendo diversas instancias de microsevicios para dar soporte a distintas organizaciones, es necesario implementaciones diferentes para diferentes tamaños y complejidades de organizaciones. así la Jgrapht es la más sencilla pero si tuvieramos organizaciones más complejas realizaríamos la implementaciones sobre una base de datos de grafos como neo4j o sobre un cluster de sparh usando el módulo de GraphX.

La implementación concreta de Jgrapht se ha decidido porque es necesario una implementación basada en teoría de grafos, ya que actualmente se puede tener una estructura puramente piramidal, pero con este enfoque estaríamos preparados por si se modificara la estructura de información o si tuvieramos que analizar la importancia de la información dando pesos a los edges.


# Funcionalidad

Actualmente se pueden dispone de un api Rest para insertar en el sistema los informatens origen y destino de forma que se va alimentando el grafo y se dispone de de varios endpoint donde se puede cambiar el estado de un jefe, obtener los jefes activos activos actualmente y otro para obtener los informantes de un determinado jefe.

El sistema funciona tal especificación siendo el concepto de jefe configurable desde la siguiente propiedad mafia.boss.reporters=2. Actualmente para las pruebas está a 2 informantes pero es configurable. El sistema cuando un Jefe deja de estar libre, posibles estados [ FREE, ENCARCELADO, ASSESINADO ], se elige otro jefe y las personas a su cargo pasan a estar reportando al siguiente jefe, si no hay jefes disponibles se promociona. 

Si un jefe recupera el estado de libre recupera a todas las personas que tenía antes informandole.


La funcionalidad se puede probar por api rest o ejecutando los test unitarios, concretamente el test 

@Test
public void testMafiaHierarchyReplaceBossExist() 



