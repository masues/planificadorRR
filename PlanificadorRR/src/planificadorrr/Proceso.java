/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package planificadorrr;

/**
 * @author Humberto Serafin Castillo Lopez
 * @author Mario Alberto Suárez Espinoza
 */
public class Proceso {
    public String idProceso;
    public String nomProceso;
    public int tamProceso;  //tamaño de memoria usado
    public int tiempoEjec;  //tiempo restante por ejecutar
    public int prioridad;
    public int tiempoOper;  //operaciones de E/S
    public int tiempoLlegada;
    public int tiempoPrevioCPU; //tiempo que se ha ejecutado el proceso
    public int tUltimaSubCPU;   //ultima vez que el proceso sube al CPU
    public int tPrimerSubCPU;   //tiempo de primer subida al CPU
    public int tiempoRafaga;    //tiempo necesario para ejecutar
    public boolean primeraVez;  //valida la primera vez que sube al CPU
    public int tiempoFinal; //tiempo en que termina de ser ejecutado
    public int tiempoEspRel;    //tiempo de espera del proceso
    public int tiempoEjecRel;   //tiempo de ejecucion del proceso
    public int tiempoRespRel;   //tiempo de espera del proceso
    
    public Proceso(String idProc, String nom, int tam, int rafaga, int prioridad, int operacion, int llegada){
        this.idProceso = idProc;
        this.nomProceso = nom;
        this.tamProceso = tam;
        this.tiempoRafaga = rafaga;
        this.prioridad = prioridad;
        this.tiempoOper = operacion;
        this.tiempoLlegada = llegada;
        
        this.tiempoEjec = tiempoRafaga; //copia el valor de rafaga
        this.primeraVez = false;    //inicia valor de primera vez en el CPU
        this.tiempoPrevioCPU = 0;   //inicia tiempo previo ejecutado
    }
    
    public void tiemposCriticos(){
        this.tiempoEspRel = tUltimaSubCPU - tiempoPrevioCPU - tiempoLlegada;
        this.tiempoRespRel = tPrimerSubCPU - tiempoLlegada;
        this.tiempoEjecRel = tiempoFinal - tiempoLlegada;
        //System.out.println(tiempoEjecRel);
    }
}
