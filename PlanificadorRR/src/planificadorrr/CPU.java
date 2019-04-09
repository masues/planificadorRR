package planificadorrr;

/**
 * @author Humberto Serafín Castillo López
 * @author Luis Ignacio Hernández Sánchez
 * @author Mario Alberto Suárez Espinoza
 */
public class CPU {
    int quantumInicial;
    int quantum;
    Proceso procesoEjecutando = null;
    Boolean disponible = true;

    public CPU(int quantumInicial) {
        this.quantumInicial = quantumInicial;
        this.quantum = quantumInicial;
    } 
    
    public void recibirProceso(Proceso n){
        this.procesoEjecutando = n;
        this.disponible = false;
    }
    
    public void ejecucion(){
        System.out.println(procesoEjecutando.idProceso+" en ejecucion "+procesoEjecutando.tiempoEjec+" [ms]");
        procesoEjecutando.tiempoEjec = procesoEjecutando.tiempoEjec - 1;
        quantum = quantum - 1;
    }
    
    public void liberar(){
        this.disponible = true;
        this.quantum = quantumInicial;
    }
}
