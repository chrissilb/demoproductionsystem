# demoproductionsystem

Production system demonstrator of the ESC component framework

Shows especially:
* Expansions in order to handle state machines
* Agent-oriented programming via proactively detecting its environment by Tick events


## Highlights
A production system and its production lines proactively observe their environment via Ticks. They find their optimal workload this way:

<img src="pics/productionsystem1.gif" width="600" />

<img src="pics/productionsystem2.png" width="600" />

<img src="pics/productionsystem3.png" width="600" />

Expansions are a way to extend the Component interface. In order to establish a state machine they can react situation-specific:

```Java
@Service(type=ProductionLine.class, inherits=ProductionAgent.class)
public class ProductionLineImpl {

   @Expansion private ProductionLineState state;
   @Expansion private PLRole role;
   …
   @Tick(activateMethod="startTick", deactivateMethod="stopTick", suspendMethod="suspendTick", resumeMethod="resumeTick")

   public void tick() {
      System.out.println("ProductionLine.tick(): " + thiz);
      role.tick();
   }
}
```

If PLRole of a ProductionLine is set to PLInactive an invocation of pl.start() leads to this:

```Java
@Service(type=PLInactive.class, inherits=PLRole.class)
public class PLInactiveImpl { 

   @Thiz
   private PLInactive thiz;

   public void start() {
      thiz.getProductionLine().setRole(PLIdle.class);
      thiz.getProductionLine().getProductionSystem().registerProductionLine(thiz.getProductionLine());
   }
}
```

## Installation
See https://github.com/chrissilb/escframework#installation


## Usage
See https://gwasch.de/escframework.php - "Folien" (only available in German so far)

