/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package planificadorrr;

/**
 *
 * @author ASUS
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
        //this.procesoEjecutando = null;
        this.disponible = true;
        this.quantum = quantumInicial;
    }
}
