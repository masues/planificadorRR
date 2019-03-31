/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package planificadorrr;

import java.util.Scanner;

/**
 *
 * @author ASUS
 */
public class PlanificadorRR {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        int tiempo = 0;
        Boolean bandera = true;
        float tiempoEsperaPromedio = 0;
        float tiempoRespuestaPromedio = 0;
        float tiempoEjecucionPromedio = 0;
        char resp = 'y';
        
        //Creacion de instancias de ProcesosListos, ProcesosEjecucion, CPU
        ProcesosListos agendador = new ProcesosListos();
        Scanner teclado = new Scanner(System.in);
        System.out.println("Memoria: ");
        int m = teclado.nextInt();
        ProcesosEjecucion despachador = new ProcesosEjecucion(m);
        
        System.out.println("Quantum: ");
        int q = teclado.nextInt();
        CPU cpu = new CPU(q);
        
        do{ //Ciclo para obtener datos de procesos que se crearán
            System.out.println("\nIngresa datos del proceso");
            agendador.nuevoProceso();
            System.out.println("\nDeseas agregar otro proceso [y/n]");
            Scanner tec = new Scanner(System.in);
            resp = tec.next().charAt(0);
        }while(resp == 'y');
        
        while(bandera == true){
            //Creacion de procesos
            for(int c = 0; c < agendador.colaProcesosIniciales.size(); c++){
                if(tiempo == agendador.colaProcesosIniciales.get(c).tiempoLlegada){
                    boolean noSeRepite = true;
                    for(int i=0;i<agendador.colaProcListos.size();i++){
                        if(agendador.colaProcListos.get(i).idProceso.equals(agendador.colaProcesosIniciales.get(c).idProceso))
                            noSeRepite=false;
                    }
                    for(int i=0;i<despachador.colaListoEjec.size();i++){
                        if(despachador.colaListoEjec.get(i).idProceso.equals(agendador.colaProcesosIniciales.get(c).idProceso))
                            noSeRepite=false;
                    }
                    if(noSeRepite){
                        agendador.colaProcListos.add(agendador.colaProcesosIniciales.get(c));
                        agendador.imprimeProcesosListos();
                    }
                }
            }
            
            //Valida si hay procesos listos para agregar a procesos en ejecucion
            if(!(agendador.colaProcListos.isEmpty())){//trata de subir procesos de la cola de listos a la cola de proc listos para ejecución
                int tamano =agendador.colaProcListos.size();
                for(int i=0;i<tamano;i++){
                    //Validacion de memoria disponible
                    if(agendador.colaProcListos.get(0).tamProceso <= despachador.memoria){
                        despachador.activacion(agendador.colaProcListos.get(0));
                        agendador.sacarProceso();
                        /*//Validacion de disponibilidad del CPU
                        if(cpu.disponible == true){
                            cpu.recibirProceso(despachador.colaListoEjec.get(0));   //Envio del proceso al CPU
                            despachador.ejecutarProceso();
                            //Actualizacion de memoria
                            despachador.memoria = despachador.memoria + cpu.procesoEjecutando.tamProceso;
                            //Actualizacion datos del proceso
                            if(cpu.procesoEjecutando.primeraVez == false){
                                cpu.procesoEjecutando.primeraVez = true;
                                cpu.procesoEjecutando.tPrimerSubCPU = tiempo;
                            }
                            //Validacion de la ultima subida a CPU
                            if(cpu.procesoEjecutando.tiempoEjec <= cpu.quantum){
                                cpu.procesoEjecutando.tUltimaSubCPU = tiempo;
                                cpu.procesoEjecutando.tiempoPrevioCPU = cpu.procesoEjecutando.tiempoRafaga - cpu.procesoEjecutando.tiempoEjec;
                            }
                        }//en caso contrario, hay un proceso en la CPU, entonces continuar*/
                    }
                    else{//no hay memoria suficiente
                            //if(cpu.quantum==cpu.quantumInicial)
                                System.out.println("\nMemoria insuficiente para subir a "+agendador.colaProcListos.get(0).idProceso+"\n");
                    }
                }
            }
            //trata de meter head de colaEjec en CPU
            if(cpu.disponible == true){
                //Valida que haya procesos en espera de subir a CPU
                if(!(despachador.colaListoEjec.isEmpty())){
                    cpu.recibirProceso(despachador.colaListoEjec.get(0));
                    despachador.ejecutarProceso();
                    //Actualizacion de memoria
                    despachador.memoria = despachador.memoria + cpu.procesoEjecutando.tamProceso;
                    if(cpu.procesoEjecutando.primeraVez == false){
                        cpu.procesoEjecutando.primeraVez = true;
                        cpu.procesoEjecutando.tPrimerSubCPU = tiempo;
                    }
                    if(cpu.procesoEjecutando.tiempoEjec <= cpu.quantum){
                        cpu.procesoEjecutando.tUltimaSubCPU = tiempo;
                        cpu.procesoEjecutando.tiempoPrevioCPU = cpu.procesoEjecutando.tiempoRafaga - cpu.procesoEjecutando.tiempoEjec;
                    }
                } 
            }//si el CPU no está vacio continuar
            
            if(cpu.procesoEjecutando != null){
                if(cpu.quantum > 0){
                    if(cpu.procesoEjecutando.tiempoEjec > 0){
                        cpu.ejecucion();
                    }
                    //Comprueba si el proceso termina despues del ultimo decremento
                    //if(cpu.procesoEjecutando.tiempoEjec == 0){
                    else{//termino su tiempo de ejecucion antes de acabar el quantum
                        cpu.liberar();
                        cpu.procesoEjecutando.tiempoFinal = tiempo;
                        tiempo--;
                        //Registrar en procesos terminados
                        despachador.procesosTerminados.add(cpu.procesoEjecutando);
                        //despachador.terminarProceso(cpu.procesoEjecutando);
                        //System.out.println("cpu.quantum < 0");
                        //despachador.procesosTerminados.get(0).tiempoFinal = tiempo;
                        //Actualizacion de memoria
                        //despachador.memoria = despachador.memoria + despachador.procesosTerminados.get(despachador.procesosTerminados.size()-1).tamProceso;
                    }
                }else{//termino su quantum
                    if(cpu.procesoEjecutando.tiempoEjec == 0){//también terminó de ejecutarse
                        cpu.liberar();
                        cpu.procesoEjecutando.tiempoFinal = tiempo;
                        tiempo--;
                        //Registrar en procesos terminados
                        despachador.procesosTerminados.add(cpu.procesoEjecutando);
                        //despachador.terminarProceso(cpu.procesoEjecutando);
                        //System.out.println("tiempo ejec == 0");
                        //despachador.procesosTerminados.get(0).tiempoFinal = tiempo;
                        //Actualizacion de memoria
                        //despachador.memoria = despachador.memoria + despachador.procesosTerminados.get(despachador.procesosTerminados.size()-1).tamProceso;
                    }else{//terminó su quantum, pero no de ejecutarse.
                        //previo
                        tiempo--;
                        cpu.liberar();
                        //Envia proceso del CPU a la cola de Procesos Listos
                        agendador.agendar(cpu.procesoEjecutando);
                        //despachador.memoria = despachador.memoria + cpu.procesoEjecutando.tamProceso;
                    }
                }
            }
            tiempo = tiempo + 1;
            //Valida si todos los procesos han terminado de ejecutarse
            if(agendador.colaProcesosIniciales.size() == despachador.procesosTerminados.size()){
                bandera = false;
            }
            //System.out.println("Iniciales: "+agendador.colaProcesosIniciales.size()+"Terminados: "+despachador.procesosTerminados.size());
        }
        for(int c = 0; c < despachador.procesosTerminados.size(); c++){
            //System.out.println(despachador.procesosTerminados.get(c).idProceso);
            despachador.procesosTerminados.get(c).tiemposCriticos();
            //System.out.println(despachador.procesosTerminados.get(c).tiempoEspRel);
            //System.out.println(despachador.procesosTerminados.get(c).tiempoRespRel);
            //System.out.println(despachador.procesosTerminados.get(c).tiempoEjecRel);
            tiempoEsperaPromedio = tiempoEsperaPromedio + despachador.procesosTerminados.get(c).tiempoEspRel;
            tiempoRespuestaPromedio = tiempoRespuestaPromedio + despachador.procesosTerminados.get(c).tiempoRespRel;
            tiempoEjecucionPromedio = tiempoEjecucionPromedio + despachador.procesosTerminados.get(c).tiempoEjecRel;
        }
        int n = despachador.procesosTerminados.size();
        float tEspProm = tiempoEsperaPromedio / n;
        float tRespProm = tiempoRespuestaPromedio / n;
        float tEjecProm = tiempoEjecucionPromedio / n;
        
        System.out.println("\n----- Tiempos Finales -----\n");
        System.out.println("Tiempo de Espera promedio: "+String.format("%.2f", tEspProm));
        System.out.println("Tiempo de Respuesta promedio: "+String.format("%.2f", tRespProm));
        System.out.println("Tiempo de Ejecucion promedio: "+String.format("%.2f", tEjecProm));
    }
    
}
