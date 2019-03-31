/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package planificadorrr;

import java.util.ArrayList;

/**
 *
 * @author ASUS
 */
public class ProcesosEjecucion {
    ArrayList<Proceso> colaListoEjec = new ArrayList<>();
    int memoria;
    ArrayList<Proceso> procesosTerminados = new ArrayList<>();

    public ProcesosEjecucion(int memoria) {
        this.memoria = memoria;
    }
    
    //Metodo para agregar proceso a cola de procesos listos para ejecucion
    //Debe recibir tamaño de memoria
    public void activacion(Proceso n){
        colaListoEjec.add(n);
        memoria = memoria - n.tamProceso;   //Actualizacion de memoria
        //Impresion de memoria
        System.out.println("Subio el proceso "+n.idProceso+" restan "+memoria+" unidades de memoria");
        this.imprimeProcesosListosEjec();
    }
    
    public void imprimeProcesosListosEjec(){
        String salida = "";
        for(int i = 0; i < colaListoEjec.size(); i++){
            salida = " | "+colaListoEjec.get(i).idProceso+salida;
        }
        System.out.println("\nCola de Procesos Listos para Ejecucion");
        System.out.println(salida);
        System.out.println("");
    }
    
    public void ejecutarProceso(){
        colaListoEjec.remove(0);
        this.imprimeProcesosListosEjec();
    }
    
    public void terminarProceso(Proceso n){
        Boolean existe = false;
        for(int a = 0; a < procesosTerminados.size(); a++){
            if(n.idProceso.equals(procesosTerminados.get(a).idProceso)){
                existe = true;
                break;
            }
        }
        if(existe == false){
            procesosTerminados.add(n);
        }
    }
}
