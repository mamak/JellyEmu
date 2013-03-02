package jelly;

import java.util.Scanner;
import system.*;

public class Commands {
    public static class Console extends Thread{
        public static boolean active = false;
        
        public Console(){
            active=false;
            Debug.print("Initialisation de l'invite de commande...");
            System.out.println("======================================================");
            System.out.println("Bienvenue sur l'invite de commandes de Jelly-emu.");
            System.out.println("Pour avoir la liste des commandes disponibles, veuillez utiliser la commande HELP, ou ?");
        }

        @Override
        public void run(){
            Scanner sc = new Scanner(System.in);
            active=true;
            while(!this.isInterrupted()){
                System.out.print(">>");
                String command = sc.nextLine();
                Commands.exec(command, -10);
            }
        }

        public static void prepareDebugLine(){
            System.out.print("\b\b");
        }

        public static void newLine(){
            System.out.print(">>");
        }
    }

    public static void exec(String command, int id){
        String[] cmdArgs = command.split(" ");

        switch(cmdArgs[0].toLowerCase().trim()){
            case "": break;
            case "exit" :
                shutdown(cmdArgs, id);
                break;
            case "shutdown":
                shutdown(cmdArgs, id);
                break;
            case "help":
                commandsList(id);
                break;
            case "?":
                commandsList(id);
                break;
            case "man":
                help(cmdArgs, id);
                break;
            case "reloadconf":
                reloadConfig(id);
                break;
            case "debug":
                debug(cmdArgs, id);
                break;
            default:
                notFound(cmdArgs, id);
        }
    }

    protected static void shutdown(String[] args, int id){
        if(id != -10)
            return;
        
        if(args.length<2)
        {
            Jelly.turnoff();
            return;
        }

        if(args[1].toLowerCase().equals("-r")){
            Jelly.restart();
            return;
        }
    }

    protected static void commandsList(int id){
        if(id == -10){
            System.out.println("Liste des commandes disponibles : ");
            System.out.println("EXIT : arrête le serveur\nSHUTDOWN : arrête ou redémarre le serveur"
                    + "\nHELP (ou ?) : affiche ce menu\nMAN [commande] : Donne des information sur la commande"
                    + "\nRELOADCONF : recharge le fichier de configurations"
                    + "\nDEBUG [ON/OFF] : active / désactiver le mode débug"
            );
        }
    }

    protected static void notFound(String[] args, int id){
        if(id == -10){
            System.out.println("Commande '"+args[0]+"' inexistante.\nVous pouvez utiliser HELP ou ? pour avoir la liste des commandes disponibles.");
        }
    }

    protected static void help(String[] args, int id){
        if(id == -10){
            if(args.length == 1){
                System.out.println("Nom de la commande manquante.");
                return;
            }

            switch(args[1].toLowerCase().trim()){
                case "exit":
                    System.out.println("Arrête instantanément le serveur après une sauvegarde.");
                    System.out.println("Utilisation :\n\tEXIT");
                    System.out.println("Pas d'arguments.");
                    break;
                case "shutdown":
                    System.out.println("Envoit un signal d'arrêt au serveur. Permet d'arrêter complètement ou de redémarrer.");
                    System.out.println("Utilisation : \n\tSHUTDOWN [options]");
                    System.out.println("Options :\n\t-r : Redémarre le serveur");
                    break;
                case "help":
                    System.out.println("Permet d'afficher la liste des commandes disponibles.");
                    System.out.println("Utilisation :\n\tHELP");
                    System.out.println("Pas d'arguments.");
                    break;
                case "man":
                    System.out.println("Affiche des information sur une commande en particulier.");
                    System.out.println("Utilisation :\n\tMAN command");
                    System.out.println("Arguments :\n\tcommand : le nom de la commande, obligatoire.");
                    break;
                default:
                    System.out.println("Désolé, pas d'information trouvée pour la commande "+args[1]);
            }
        }
    }

    protected static void reloadConfig(int id){
        if(id == -10){
            Jelly._config.load();
        }
    }

    protected static void debug(String[] args, int id){
        if(id!=-10)
            return;

        if(args.length==1){
            System.out.println("Nombre d'arguments invalide !");
            return;
        }
        switch(args[1].toLowerCase()){
            case "on":
                Jelly.DEBUG=true;
                System.out.println("Mode débug activé !");
                break;
            case "off":
                Jelly.DEBUG=false;
                System.out.println("Mode débug désactivé !");
                break;
            default:
                System.out.println("Argument invalide : "+args[1]);
        }
    }
}
