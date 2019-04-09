package planificadorrr;

import java.util.Scanner;

/**
 * @author Humberto Serafín Castillo López
 * @author Luis Ignacio Hernández Sánchez
 * @author Mario Alberto Suárez Espinoza
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
        int memoria;
        
        //Creacion de instancias de ProcesosListos, ProcesosEjecucion, CPU
        ProcesosListos agendador = new ProcesosListos();
        Scanner teclado = new Scanner(System.in);
        System.out.println("Memoria: ");
        memoria = teclado.nextInt();
        ProcesosEjecucion despachador = new ProcesosEjecucion(memoria);
        
        System.out.println("Quantum: ");
        int q = teclado.nextInt();
        CPU cpu = new CPU(q);
        do{ //Ciclo para obtener datos de procesos que se crearán
            System.out.println("\nIngresa datos del proceso");
            agendador.nuevoProceso(memoria);
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
                for(int i=0;i<tamano;i++){//ciclo intenta subir todos los procesos de la cola de procesos listos a la de ejecucion, hasta que se llene la memoria
                    //Validacion de memoria disponible
                    if(agendador.colaProcListos.get(0).tamProceso <= despachador.memoria){
                        despachador.activacion(agendador.colaProcListos.get(0));
                        agendador.sacarProceso();
                    }else{//no hay memoria suficiente
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
                    }else{//termino su tiempo de ejecucion antes de acabar el quantum
                        cpu.liberar();
                        cpu.procesoEjecutando.tiempoFinal = tiempo;
                        tiempo--;
                        //Registrar en procesos terminados
                        despachador.procesosTerminados.add(cpu.procesoEjecutando);
                    }
                }else{//termino su quantum
                    if(cpu.procesoEjecutando.tiempoEjec == 0){//también terminó de ejecutarse
                        cpu.liberar();
                        cpu.procesoEjecutando.tiempoFinal = tiempo;
                        tiempo--;
                        //Registrar en procesos terminados
                        despachador.procesosTerminados.add(cpu.procesoEjecutando);
                    }else{//terminó su quantum, pero no de ejecutarse.
                        //previo
                        tiempo--;
                        cpu.liberar();
                        //Envia proceso del CPU a la cola de Procesos Listos
                        agendador.agendar(cpu.procesoEjecutando);
                    }
                }
            }
            tiempo = tiempo + 1;
            //Valida si todos los procesos han terminado de ejecutarse
            if(agendador.colaProcesosIniciales.size() == despachador.procesosTerminados.size()){
                bandera = false;
            }
        }
        for(int c = 0; c < despachador.procesosTerminados.size(); c++){
            despachador.procesosTerminados.get(c).tiemposCriticos();
            tiempoEsperaPromedio = tiempoEsperaPromedio + despachador.procesosTerminados.get(c).tiempoEspRel;
            tiempoRespuestaPromedio = tiempoRespuestaPromedio + despachador.procesosTerminados.get(c).tiempoRespRel;
            tiempoEjecucionPromedio = tiempoEjecucionPromedio + despachador.procesosTerminados.get(c).tiempoEjecRel;
        }
        int n = despachador.procesosTerminados.size();
        if (n==0){
            System.out.println("No se insertó ningun proceso.");
        }else{
            float tEspProm = tiempoEsperaPromedio / n;
            float tRespProm = tiempoRespuestaPromedio / n;
            float tEjecProm = tiempoEjecucionPromedio / n;
        
            System.out.println("\n----- Tiempos Finales -----\n");
            System.out.println("Tiempo de Espera promedio: "+String.format("%.2f", tEspProm));
            System.out.println("Tiempo de Respuesta promedio: "+String.format("%.2f", tRespProm));
            System.out.println("Tiempo de Ejecucion promedio: "+String.format("%.2f", tEjecProm));
        }
    }
}