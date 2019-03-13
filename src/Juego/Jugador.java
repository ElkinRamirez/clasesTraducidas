/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Juego;

import Piezas.*;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 *
 * @author Nicolás
 */
public class Jugador implements Serializable {
    private static final long serialVersionUID = 1L;
    private String nombre;
    private Integer juegosJugados;
    private Integer juegosGanados;
    private ArrayList<Pieza> piezas = new ArrayList<>();
    
    public Jugador(String nombre){
        this.nombre=nombre.trim();
        juegosJugados=new Integer(0);
        juegosGanados=new Integer(0);
    }
    public String Nombre(){
        return nombre;
    }
    public Integer juegosJugados(){
        return juegosJugados;
    }
    public Integer juegosGanados(){
        return juegosGanados;
    }
    public Integer porcentajeVictorias(){
        return new Integer((juegosGanados*100)/juegosJugados);
    }
    public void cargarJuegosJugados(){
        juegosJugados++;
    }
    public void cargarJuegosGanados(){
        juegosGanados++;
    }
    public static ArrayList<Jugador> fetch_jugadores(){
        Jugador jugadortemporal;
        ObjectInputStream entrada=null;
        ArrayList<Jugador> jugadores=new ArrayList<Jugador>();
        try{
            File inFile=new File(System.getProperty("user.dir")+File.separator+"chessgamedata.dat");
            entrada = new ObjectInputStream(new FileInputStream(inFile));
            try{
                while(true){
                    jugadortemporal=(Jugador)entrada.readObject();
                    jugadores.add(jugadortemporal);
                }
            }catch(EOFException e){
                entrada.close();
            }            
        }catch(FileNotFoundException e){
            jugadores.clear();
            return jugadores;
        }catch(IOException e){
            e.printStackTrace();
            try{
                entrada.close();
            }catch(IOException e1){}
            JOptionPane.showMessageDialog(null,"No se pueden leer los archivos del juego");
        }catch(ClassNotFoundException e){
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Archivos del juego dañados. Click en Ok para continuar y crear nuevos archivos.");
        }catch(Exception e1){
            e1.printStackTrace();
        }
        return jugadores;
    }
    
    public void cargar_Jugadores(){
        ObjectInputStream entrada=null;
        ObjectOutputStream salida=null;
        Jugador jugadortemporal;
        File inputFile=null;
        File outputFile=null;
        try{
            inputFile = new File(System.getProperty("user.dir")+ File.separator + "chessgamedata.dat");
            outputFile = new File(System.getProperty("user.dir")+ File.separator + "tempfile.dat");
        }catch(SecurityException e){
            JOptionPane.showMessageDialog(null,"Permiso de escritura y lecatura denegado. No se puede iniciar.");
            System.exit(0);
        }
        boolean jugadorNoExiste;
        try{
            if(outputFile.exists()==false){
                outputFile.createNewFile();
            }if (inputFile.exists()==false){
                salida=new ObjectOutputStream(new java.io.FileOutputStream(outputFile,true));
                salida.writeObject(this);
            }else{
                entrada = new ObjectInputStream(new FileInputStream(inputFile));
                salida=new ObjectOutputStream(new FileOutputStream(outputFile));
                jugadorNoExiste=true;
                try{
                    while(true){
                        jugadortemporal=(Jugador)entrada.readObject();
                        if(jugadortemporal.Nombre().equals(Nombre())){
                            salida.writeObject(this);
                            jugadorNoExiste=false;                          
                        }else{
                            salida.writeObject(jugadortemporal);
                        }
                    }
                }catch(EOFException e){
                    entrada.close();
                }if (jugadorNoExiste){
                    salida.writeObject(this);
                }
                inputFile.delete();
                salida.close();
                File newF= new File(System.getProperty("user.dir")+File.separator+"chessgamedata.dat");
                if(outputFile.renameTo(newF)==false){
                    System.out.println("Renombrada de archivo fallida.");
                }
            }              
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,"No se pueden leer/escribir los archivos del juego requeridos. presione Ok para continuar");
        }catch(ClassNotFoundException e){
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,"Archivos del juego dañados. Presione Ok para crear uno");
        }catch(Exception e){
            
        }   
    }
}
