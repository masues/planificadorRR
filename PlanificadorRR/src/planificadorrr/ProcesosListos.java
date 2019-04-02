package planificadorrr;
import java.util.ArrayList;
import java.util.Scanner;
/**
 * @author Humberto Serafín Castillo López
 * @author Mario Alberto Suárez Espinoza
 * @author Luis Ignacio Hernández Sánchez
 */
public class ProcesosListos {
    //Colecciones de procesos
    ArrayList<Proceso> colaProcListos = new ArrayList<>();
    ArrayList<Proceso> colaProcesosIniciales = new ArrayList<>();
    int c;
    
    String id;
    String nombre;
    int tamaño;
    int tEjec;
    int prioridad;
    int tES;
    int tLlegada;
    
    //Creacion de un nuevo proceso
    public void nuevoProceso(){
        //Lectura de datos del proceso
        Scanner teclado = new Scanner(System.in);
        System.out.println("ID del proceso: ");
        id = teclado.nextLine();
        System.out.println("Nombre del proceso: ");
        nombre = teclado.nextLine();
        System.out.println("Tamaño del proceso[Kb]: ");
        tamaño = teclado.nextInt();
        System.out.println("Tiempo de rafaga del proceso[ms]: ");
        tEjec = teclado.nextInt();
        System.out.println("Prioridad del proceso: ");
        prioridad = teclado.nextInt();
        System.out.println("Tiempo de operaciones E/S del proceso[ms]: ");
        tES = teclado.nextInt();
        System.out.println("Tiempo de llegada del proceso[ms]: ");
        tLlegada = teclado.nextInt();
        
        if(colaProcesosIniciales.isEmpty()){    //Valida si la cola de procesos iniciales está vacía
            Proceso process = new Proceso(id, nombre, tamaño, tEjec, prioridad, tES, tLlegada);
            colaProcesosIniciales.add(process);
        }else{
            for(c = 0; c < colaProcesosIniciales.size(); c++){
                //Busca ID repetidos en los procesos creados
                if(id.equals(colaProcesosIniciales.get(c).idProceso)){
                    System.out.println("El proceso ya existe");
                    break;
                }
            }
            Proceso process = new Proceso(id, nombre, tamaño, tEjec, prioridad, tES, tLlegada);
            colaProcesosIniciales.add(process);
        }
    }
    
    public void imprimeProcesosListos(){    //Impresion de la cola de Procesos Listos
        String salida = "";
        for(int i = 0; i < colaProcListos.size(); i++){
            salida = " | "+colaProcListos.get(i).idProceso+salida;
        }
        System.out.println("\nCola de Procesos Listos");
        System.out.println(salida);
        System.out.println("");
    }
    
    public void sacarProceso(){
        colaProcListos.remove(0);
        this.imprimeProcesosListos();
    }
    
    public void agendar(Proceso n){
        colaProcListos.add(n);
        this.imprimeProcesosListos();
    }
}
